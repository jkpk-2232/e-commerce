package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class EncryptionUtils {

	private static Logger logger = Logger.getLogger(EncryptionUtils.class);

	private static Cipher ecipher;

	private static Cipher dcipher;

	protected static Properties cryptoProperties = null;

	private static final String PASS_PHRASE = "passPhrase";
	// 8-bytes Salt
	private static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };

	// Iteration count
	private static int iterationCount = 19;

	private static void initilizeEncrypter(String passPhrase) {

		try {

			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			ecipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameters to the cipthers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
		} catch (InvalidKeySpecException e) {
			System.out.println("EXCEPTION: InvalidKeySpecException");
		} catch (NoSuchPaddingException e) {
			System.out.println("EXCEPTION: NoSuchPaddingException");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("EXCEPTION: NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			System.out.println("EXCEPTION: InvalidKeyException");
		}
	}

	private static void initilizeDecrypter(String passPhrase) {

		try {

			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameters to the cipthers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
		} catch (InvalidKeySpecException e) {
			System.out.println("EXCEPTION: InvalidKeySpecException");
		} catch (NoSuchPaddingException e) {
			System.out.println("EXCEPTION: NoSuchPaddingException");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("EXCEPTION: NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			System.out.println("EXCEPTION: InvalidKeyException");
		}
	}

	private static String encrypt(String str) {

		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);
			// logger.info(" encrypt password => " + enc.toString());
			// Encode bytes to base64 to get a string
			//return new sun.misc.UCEncoder().encode(enc);
			return new String(new Base64().encode(enc));
		} catch (IllegalStateException e) {
			logger.error("Cipher Error,", e);
			e.printStackTrace();
			initilizeEncrypter(getCryptoAppProperty(PASS_PHRASE));
			encrypt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String encryptString(String str) {

		initilizeEncrypter(getCryptoAppProperty(PASS_PHRASE));

		String desEncrypted = EncryptionUtils.encrypt(str);

		return desEncrypted;
	}

	public static String decryptByte(String str) throws IOException {

		initilizeDecrypter(getCryptoAppProperty(PASS_PHRASE));

		String desDecrypted = EncryptionUtils.decrypt(str);

		return desDecrypted;
	}

	public static String decrypt(String dec) throws IOException {

		String password = "";

		try {

			// Decode base64 to get bytes
			// String dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
			//byte[] bytes = new sun.misc.UCDecoder().decodeBuffer(dec);
			byte[] bytes = new Base64().decode(dec.getBytes());
			// Decrypt
			byte[] utf8 = dcipher.doFinal(bytes);

			// Decode using utf-8
			password = new String(utf8, "UTF8");
			return password;

		} catch (BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		}

		return null;
	}

	static {
		loadWebAppProperties();
	}

	protected static void loadWebAppProperties() {

		if (cryptoProperties == null) {

			try {

				InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("crypto.properties");

				if (in == null) {
					logger.info("Falied to load properties.");
				} else {
					cryptoProperties = new Properties();
					cryptoProperties.load(in);
				}

			} catch (IOException ioe) {

				cryptoProperties = null;
				logger.info("Falied to load crypto properties.", ioe);
			}
		}
	}

	public static String getCryptoAppProperty(String key) {

		if (cryptoProperties == null) {
			return null;
		}

		return cryptoProperties.getProperty(key);
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(decryptByte("ILn3Iqb0Jb0="));
	}

}