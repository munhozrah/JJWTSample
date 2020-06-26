package br.com.munhozrah.jjwtsample.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;

public class KeyGenerator {
	private static final String ALIAS = "jwtSecret";
	private static final char[] PASSWD = "AbuhvEOWxGHo".toCharArray(); //keypass and storepass are the same
	private static final String KEYSTORE_NAME = "server.keystore";
	private static final String CONFIG_DIR = "jboss.server.config.dir";
	private static final String KEYSTORE_TYPE = "JKS";

	//keytool -genkeypair -alias jwtSecret -keyalg RSA -keysize 2048 -keystore $JBOSS_HOME/standalone/configuration/server.keystore -storepass AbuhvEOWxGHo -keypass AbuhvEOWxGHo -storetype pkcs12 -validity 365 -dname "CN=SimpleJJWT, OU=MunhozRah, O=Edu, L=Porto Alegre, ST=RS, C=BR"
	public Key generateKey() {
		FileInputStream fis = null;
		Key key = null;

		try {
			KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
			fis = new FileInputStream(System.getProperty(CONFIG_DIR) + File.separator + KEYSTORE_NAME);
			keyStore.load(fis, PASSWD);

			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ALIAS, new KeyStore.PasswordProtection(PASSWD));
		    key = privateKeyEntry.getPrivateKey();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return key;
	}
}