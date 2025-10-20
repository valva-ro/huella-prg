1. Descargar y descomprimir [tomcat](https://tomcat.apache.org/download-10.cgi)
2. cd C:\apache-tomcat-10.1.48\bin
3. Arrancar tomcat: startup.bat
4. mvn clean package
5. Copiar
   target/huella-prg.war
   └─ WEB-INF/lib/*
   A la ruta de tomcat C:\apache-tomcat-10.1.48\webapps
6. Detener tomcat: shutdown.bat