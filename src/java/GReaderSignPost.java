/*
 * Google Reader OAuth sample code.
 *
 * Copyright (C) 2010 Jayesh Salvi (http://www.altcanvas.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * INSTRUCTIONS:
 *
 * To get consumer key and secret for your app check out:
 * http://code.google.com/apis/accounts/docs/RegistrationForWebAppsAuto.html
 *
 * Required libraries:
 * Signpost (OAuth implementation library): 
 *  http://oauth-signpost.googlecode.com/files/signpost-core-1.2.1.1.jar
 * Apache Codec (requirement of Signpost) :
 *  http://download.filehat.com/apache/commons/codec/binaries/commons-codec-1.4-bin.tar.gz
 *
 *  Build instructions:
 *  javac -classpath=<path-of-signpost-core.jar> GReaderSignPost.java
 *
 *  Run instructions:
 *  java -classpath=.:<path-of-signpost-core.jar>:<path-of-commons-codec.jar> GReaderSignPost
 */

import java.io.*;
import java.net.*;
import oauth.signpost.*;
import oauth.signpost.basic.*;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class GReaderSignPost {

    public GReaderSignPost() {};

    private static final String CONSUMER_KEY = "everfeeds.com";
    private static final String CONSUMER_SECRET = "mucd4gqA1yLtrY6eMzZo3IYe";
    private static final String APPNAME = "ReaderScope";
    private static final String scope = 
        "http://www.google.com/reader/api";
    public static final String reqtokenURL =
        "https://www.google.com/accounts/OAuthGetRequestToken";
    public static final String authorizeURL =
        "https://www.google.com/accounts/OAuthAuthorizeToken";
    public static final String accessTokenURL = 
        "https://www.google.com/accounts/OAuthGetAccessToken";

    private static String accessToken = null;
    private static String tokenSecret = null;

    public static void main(String[] args) 
    {
        try {
            OAuthConsumer oac = new DefaultOAuthConsumer(
                CONSUMER_KEY, CONSUMER_SECRET);

            OAuthProvider oap = new DefaultOAuthProvider(
                reqtokenURL+"?scope="+scope+"&"+"xoauth_displayname="+APPNAME,
                accessTokenURL, 
                authorizeURL+"?hl=en&btmpl=mobile");
            String url = oap.retrieveRequestToken(oac, OAuth.OUT_OF_BAND);
            System.out.println("Open this URL in browser and Grant Access to "+
                            APPNAME+": "+url);
            System.out.print("Enter Verification code: ");
            BufferedReader ibr = 
                new BufferedReader(new InputStreamReader(System.in));
            String verifCode = ibr.readLine();

            oap.retrieveAccessToken(oac, verifCode);
            accessToken = oac.getToken();
            tokenSecret = oac.getTokenSecret();

            getSubscriptionList();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void getSubscriptionList() throws Exception
    {
        OAuthConsumer consumer = new DefaultOAuthConsumer(
                CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(accessToken, tokenSecret);
        String url = 
            "http://www.google.com/reader/api/0/subscription/list?output=json";
        HttpURLConnection conn = 
            (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10*1000);
        conn.setReadTimeout(25*1000);

        consumer.sign(conn);

        conn.connect();

        if(conn.getResponseCode() != 200) {
            System.out.println("Error: "+
                conn.getResponseCode()+" "+conn.getResponseMessage());
            return;
        }

        InputStream is = conn.getInputStream();

        BufferedReader br = 
            new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String line;
        while (null != (line = br.readLine())) {
            buf.append(line).append("\n");
        }
        System.out.println(buf.toString());
    }
}