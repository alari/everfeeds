/*
  A simple Evernote API demo application that authenticates with the
  Evernote web service, lists all notebooks and notes in the user's account,
  and creates a simple test note in the default notebook.
  
  Before running this sample, you must change the API consumer key
  and consumer secret to the values that you received from Evernote.
  
  To compile (Unix):
    javac -classpath .:../../../lib/java/libthrift.jar:../../../lib/java/log4j-1.2.14.jar:../../../lib/java/evernote-api-*.jar EDAMDemo.java
  
  To run:
     java -classpath .:../../../lib/java/libthrift.jar:../../../lib/java/log4j-1.2.14.jar:../../../lib/java/evernote-api-*.jar EDAMDemo myuser mypass
 */

import java.util.*;
import java.io.*;
import java.security.MessageDigest;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.THttpClient;

import com.evernote.edam.type.*;
import com.evernote.edam.userstore.*;
import com.evernote.edam.error.*;
import com.evernote.edam.notestore.*;

public class EDAMDemo {

  public static void main(String args[]) throws Exception {

    // NOTE: You must change the consumer key and consumer secret to the 
    //       key and secret that you received from Evernote
    String consumerKey = "en-edamtest";
    String consumerSecret = "0123456789abcdef";
    
    String evernoteHost = "sandbox.evernote.com";
    String userStoreUrl = "https://" + evernoteHost + "/edam/user";
    String noteStoreUrlBase = "http://" + evernoteHost + "/edam/note/";

    if (args.length < 2) {
      System.err.println("Arguments:  <username> <password>");
      return;
    }
    String username = args[0];
    String password = args[1];

    // Set up the UserStore and check that we can talk to the server
    THttpClient userStoreTrans = new THttpClient(userStoreUrl);
    TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
    UserStore.Client userStore = new UserStore.Client(userStoreProt,
        userStoreProt);
    boolean versionOk = userStore.checkVersion("Evernote's EDAMDemo (Java)",
        com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
        com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR);
    if (!versionOk) {
      System.err.println("Incomatible EDAM client protocol version");
      return;
    }

    // Authenticate as a user & password
    AuthenticationResult authResult = null;
    try {
      authResult = userStore.authenticate(username, password, consumerKey, consumerSecret);
    } catch (EDAMUserException ex) {
      String parameter = ex.getParameter();
      EDAMErrorCode errorCode = ex.getErrorCode();
      
      System.err.println("Authentication failed (parameter: " + parameter + " errorCode: " + errorCode + ")");
      
      if (errorCode == EDAMErrorCode.INVALID_AUTH) {
        if (parameter.equals("consumerKey")) {
          if (consumerKey.equals("en-edamtest")) {
            System.err.println("You must replace the variables consumerKey and consumerSecret with the values you received from Evernote.");
          } else {
            System.err.println("Your consumer key was not accepted by " + evernoteHost);
          }
          System.err.println("If you do not have an API Key from Evernote, you can request one from http://www.evernote.com/about/developer/api");
        } else if (parameter.equals("username")) {
          System.err.println("You must authenticate using a username and password from " + evernoteHost);
          if (evernoteHost.equals("www.evernote.com") == false) {
            System.err.println("Note that your production Evernote account will not work on " + evernoteHost + ",");
            System.err.println("you must register for a separate test account at https://" + evernoteHost + "/Registration.action");
          }
        } else if (parameter.equals("password")) {
          System.err.println("The password that you entered is incorrect");
        }
      }

      return;
    }
    User user = authResult.getUser();
    String authToken = authResult.getAuthenticationToken();

    // Set up the NoteStore
    System.out.println("Notes for " + user.getUsername() + ":");
    String noteStoreUrl = noteStoreUrlBase + user.getShardId();
    THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
    TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
    NoteStore.Client noteStore = new NoteStore.Client(noteStoreProt,
        noteStoreProt);

    // List all of the notes in the account
    List<Notebook> notebooks = (List<Notebook>) noteStore
        .listNotebooks(authToken);
    Notebook defaultNotebook = notebooks.get(0);
    for (Notebook notebook : notebooks) {
      System.out.println("Notebook: " + notebook.getName());
      NoteFilter filter = new NoteFilter();
      filter.setOrder(NoteSortOrder.CREATED.getValue());
      filter.setAscending(true);
      filter.setNotebookGuid(notebook.getGuid());
      NoteList noteList = noteStore.findNotes(authToken, filter, 0, 100);
      List<Note> notes = (List<Note>) noteList.getNotes();
      for (Note note : notes) {
        System.out.println(" * " + note.getTitle());
      }
      if (notebook.isDefaultNotebook()) {
        defaultNotebook = notebook;
      }
    }

    // Create a note containing a little text, plus the "enlogo.png" image
    Resource resource = new Resource();
    resource.setData(readFileAsData("enlogo.png"));
    resource.setMime("image/png");
    Note note = new Note();
    note.setTitle("Test note from EDAMDemo.java");
    note.setCreated(System.currentTimeMillis());
    note.setUpdated(System.currentTimeMillis());
    note.setActive(true);
    note.setNotebookGuid(defaultNotebook.getGuid());
    note.addToResources(resource);
    String hashHex = bytesToHex(resource.getData().getBodyHash());
    String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml.dtd\">"
        + "<en-note>Here's the Evernote logo:<br/>"
        + "<en-media type=\"image/png\" hash=\"" + hashHex + "\"/>"
        + "</en-note>";
    note.setContent(content);
    Note createdNote = noteStore.createNote(authToken, note);
    System.out.println("Note created. GUID: " + createdNote.getGuid());
  }

  private static Data readFileAsData(String fileName) throws Exception {
    FileInputStream in = new FileInputStream(fileName);
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    byte[] block = new byte[10240];
    int len;
    while ((len = in.read(block)) >= 0) {
      byteOut.write(block, 0, len);
    }
    in.close();
    byte[] body = byteOut.toByteArray();
    Data data = new Data();
    data.setSize(body.length);
    data.setBodyHash(MessageDigest.getInstance("MD5").digest(body));
    data.setBody(body);
    return data;
  }

  public static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte hashByte : bytes) {
      int intVal = 0xff & hashByte;
      if (intVal < 0x10) {
        sb.append('0');
      }
      sb.append(Integer.toHexString(intVal));
    }
    return sb.toString();
  }

}
