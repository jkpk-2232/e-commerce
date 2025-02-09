package com.webapp.actions.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class TLSSocketFactory extends SSLSocketFactory {

	private SSLSocketFactory delegate;

	public TLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		SSLContext context = SSLContext.getInstance("TLS");

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init((KeyStore) null);
		TrustManager[] tm = trustManagerFactory.getTrustManagers();

		context.init(null, tm, new java.security.SecureRandom());

		delegate = context.getSocketFactory();
	}

	public SSLSocketFactory getDelegate() {
		return delegate;
	}

	public void setDelegate(SSLSocketFactory delegate) {
		this.delegate = delegate;
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return delegate.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return delegate.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose));
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		return enableTLSOnSocket(delegate.createSocket(host, port));
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
		return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort));
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return enableTLSOnSocket(delegate.createSocket(host, port));
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
		return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort));
	}

	private Socket enableTLSOnSocket(Socket socket) {
		if (socket != null && (socket instanceof SSLSocket)) {
			((SSLSocket) socket).setEnabledProtocols(new String[] { "TLSv1.2" });
		}
		return socket;
	}

}