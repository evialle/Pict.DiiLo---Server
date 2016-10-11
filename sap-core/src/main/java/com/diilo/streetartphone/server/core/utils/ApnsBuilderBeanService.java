/**
 * 
 */
package com.diilo.streetartphone.server.core.utils;

import java.io.InputStream;
import java.net.Proxy;
import java.net.Socket;

import javax.net.ssl.SSLContext;

import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ReconnectPolicy;

/**
 * @author Eric
 */
public interface ApnsBuilderBeanService {

	String getFileNameCertification();

	void setFileNameCertification(String fileNameCertification);

	void setNoErrorDetection(boolean isNoErrorDetection);

	void setApnsDelegate(ApnsDelegate apnsDelegate);

	void setProxy(Proxy proxy);

	void setNonBlocking(boolean isNonBlocking);

	void setQueue(boolean isQueue);

	void setMaxPoolConnections(int maxPoolConnections);

	void setProxySocket(Socket proxySocket);

	void setPortSocksProxy(int portSocksProxy);

	void setHostSocksProxy(String hostSocksProxy);

	void setReconnectPolicy(ReconnectPolicy reconnectPolicy);

	void setProductionDestination(boolean isProductionDestination);

	void setSandboxDestination(boolean isSandboxDestination);

	void setPortFeedbackDestination(int portFeedbackDestination);

	void setHostFeedbackDestination(String hostFeedbackDestination);

	void setPortGatewayDestination(int portGatewayDestination);

	void setHostGatewayDestination(String hostGatewayDestination);

	void setSslContext(SSLContext sslContext);

	void setInputStreamCertification(InputStream inputStreamCertification);

	void setPasswordCertification(String passwordCertification);

	boolean isNoErrorDetection();

	ApnsDelegate getApnsDelegate();

	Proxy getProxy();

	boolean isNonBlocking();

	boolean isQueue();

	int getMaxPoolConnections();

	Socket getProxySocket();

	int getPortSocksProxy();

	String getHostSocksProxy();

	ReconnectPolicy getReconnectPolicy();

	boolean isProductionDestination();

	boolean isSandboxDestination();

	int getPortFeedbackDestination();

	String getHostFeedbackDestination();

	int getPortGatewayDestination();

	String getHostGatewayDestination();

	SSLContext getSslContext();

	InputStream getInputStreamCertification();

	String getPasswordCertification();

	ApnsService build();

}
