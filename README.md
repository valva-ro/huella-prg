# Huella de Carbono (PRG · MVC)

## Instalación y ejecución

1. Descargar y descomprimir [tomcat](https://tomcat.apache.org/download-10.cgi)
2. `mvn clean package`
3. Copiar
   target/huella-prg.war
   └─ WEB-INF/lib/*
   A la ruta de tomcat `C:\apache-tomcat-10.1.48\webapps`
4. `cd C:\apache-tomcat-10.1.48\bin`
5. Arrancar tomcat: `startup.bat`
6. Detener tomcat: `shutdown.bat`



## PRG explicado para dummies ❤️

```
Usuario llena el formulario → POST /huella → doPost
               |  ----> POST de Prg
               v
Validación + Cálculo
               |
               v
response.sendRedirect("/huella?op=...&kg=...&comp=...")
               |  ----> REDIRECT de pRg
               v
GET /huella → doGet
               |  ----> GET de prG
               v
request.setAttribute(...) y forward a huella.jsp
               |
               v
Usuario ve los resultados
```