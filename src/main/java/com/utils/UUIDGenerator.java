package com.utils;

import java.util.UUID;

public class UUIDGenerator {
	//
	// /** Cached per JVM server IP. */
	// private static String hexServerIP = null;
	//
	// // initialise the secure random instance
	// private static final java.security.SecureRandom seeder = new
	// java.security.SecureRandom();

	public static final String generateUUID() {
		/*
		 * StringBuffer tmpBuffer = new StringBuffer(16); if (hexServerIP ==
		 * null) { java.net.InetAddress localInetAddress = null; try {
		 * localInetAddress = java.net.InetAddress.getLocalHost(); } catch
		 * (java.net.UnknownHostException uhe) { return null; } byte serverIP[]
		 * = localInetAddress.getAddress(); hexServerIP =
		 * hexFormat(getInt(serverIP), 8); }
		 * 
		 * String hashcode = hexFormat(System.identityHashCode(o), 8);
		 * tmpBuffer.append(hexServerIP); tmpBuffer.append(hashcode);
		 * 
		 * long timeNow = System.currentTimeMillis(); int timeLow = (int)
		 * timeNow & 0xFFFFFFFF; int node = seeder.nextInt();
		 * 
		 * StringBuffer guid = new StringBuffer(32);
		 * guid.append(hexFormat(timeLow, 8));
		 * guid.append(tmpBuffer.toString()); guid.append(hexFormat(node, 8));
		 * return guid.toString();
		 */
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

}