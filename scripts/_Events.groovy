eventCompileEnd = {
  new File('grails-app/conf').eachFileMatch(~/.*?Config\.groovy/) { f ->
	ant.copy(file: "${basedir}/grails-app/conf/${f.name}", todir: classesDirPath)
    System.out.println(f.name);
  }
}
