# JJWTSample

It's an easy implementation of a restful api secured with jwt, using library jjwt.

1. A keystore is needed to run this sample. It was generated with the following command: 
keytool -genkeypair -alias jwtSecret -keyalg RSA -keysize 2048 -keystore $JBOSS_HOME/standalone/configuration/server.keystore -storepass AbuhvEOWxGHo -keypass AbuhvEOWxGHo -storetype pkcs12 -validity 365 -dname "CN=SimpleJJWT, OU=MunhozRah, O=Edu, L=Porto Alegre, ST=RS, C=BR"

2. Endpoints are:
  2.1 /JJWTSample/rest/users/login/ for issue a token
  2.2 /JJWTSample/rest/sealed?id=999 for a protected method
  2.3 /JJWTSample/rest/sealed?message=XXXX non protected mehod @PermitAll

