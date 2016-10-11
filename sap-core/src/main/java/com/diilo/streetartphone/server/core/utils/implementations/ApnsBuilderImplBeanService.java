/**
 * 
 */
package com.diilo.streetartphone.server.core.utils.implementations;

import java.io.InputStream;
import java.net.Proxy;
import java.net.Socket;

import javax.net.ssl.SSLContext;

import com.diilo.streetartphone.server.core.utils.ApnsBuilderBeanService;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.ReconnectPolicy;

/**
 * Class for building ApnsService, equivalent to APNS.newService().build(), but "Spring Friendly". You may
 * inject easily an ApnsService, by configuring this Bean.
 * 
 * @author Eric Vialle
 */
public class ApnsBuilderImplBeanService implements ApnsBuilderBeanService {

	private static int		DEFAULT_PORT_GATEWAY_DESTINATION	= 80;

	private static int		DEFAULT_PORT_FEEDBACK_DESTINATION	= 80;

	private static int		DEFAULT_PORT_SOCKS_PROXY			= 80;

	private static int		NOT_SET_VALUE						= -1;

	private String			fileNameCertification;

	private String			passwordCertification;

	private InputStream		inputStreamCertification;

	private SSLContext		sslContext;

	private String			hostGatewayDestination;

	private int				portGatewayDestination				= DEFAULT_PORT_GATEWAY_DESTINATION;

	private String			hostFeedbackDestination;

	private int				portFeedbackDestination				= DEFAULT_PORT_FEEDBACK_DESTINATION;

	private boolean			isSandboxDestination;

	private boolean			isProductionDestination;

	private ReconnectPolicy	reconnectPolicy;

	private String			hostSocksProxy;

	private int				portSocksProxy						= DEFAULT_PORT_SOCKS_PROXY;

	private Socket			proxySocket;

	private int				maxPoolConnections					= NOT_SET_VALUE;

	private boolean			isQueue;

	private boolean			isNonBlocking;

	private Proxy			proxy;

	private ApnsDelegate	apnsDelegate;

	private boolean			isNoErrorDetection;

	@Override
	public ApnsService build() {
		ApnsServiceBuilder apnsBuildService = APNS.newService();

		if (getFileNameCertification() != null) {
			apnsBuildService.withCert(getFileNameCertification(), getPasswordCertification());
		}
		if (getInputStreamCertification() != null) {
			apnsBuildService.withCert(getInputStreamCertification(), getPasswordCertification());
		}

		if (getSslContext() != null) {
			apnsBuildService.withSSLContext(getSslContext());
		}

		if (getHostGatewayDestination() != null) {
			apnsBuildService.withGatewayDestination(getHostGatewayDestination(), getPortGatewayDestination());
		}

		if (isSandboxDestination()) {
			apnsBuildService.withSandboxDestination();
		}

		if (isProductionDestination()) {
			apnsBuildService.withProductionDestination();
		}

		if (getReconnectPolicy() != null) {
			apnsBuildService.withReconnectPolicy(getReconnectPolicy());
		}

		if (getHostSocksProxy() != null) {
			apnsBuildService.withSocksProxy(getHostSocksProxy(), getPortSocksProxy());
		}

		if (getProxySocket() != null) {
			apnsBuildService.withProxySocket(getProxySocket());
		}

		if (getMaxPoolConnections() != NOT_SET_VALUE) {
			apnsBuildService.asPool(getMaxPoolConnections());
		}

		if (isQueue()) {
			apnsBuildService.asQueued();
		}

		if (isNonBlocking()) {
			apnsBuildService.asNonBlocking();
		}

		if (getApnsDelegate() != null) {
			apnsBuildService.withDelegate(getApnsDelegate());
		}

		if (isNoErrorDetection()) {
			apnsBuildService.withNoErrorDetection();
		}

		return apnsBuildService.build();
	}

	/**
	 * @return the passwordCertification
	 */
	@Override
	public String getPasswordCertification() {

		return passwordCertification;
	}

	/**
	 * @return the inputStreamCertification
	 */
	@Override
	public InputStream getInputStreamCertification() {
		return inputStreamCertification;
	}

	/**
	 * @return the sslContext
	 */
	@Override
	public SSLContext getSslContext() {
		return sslContext;
	}

	/**
	 * @return the hostGatewayDestination
	 */
	@Override
	public String getHostGatewayDestination() {
		return hostGatewayDestination;
	}

	/**
	 * @return the portGatewayDestination
	 */
	@Override
	public int getPortGatewayDestination() {
		return portGatewayDestination;
	}

	/**
	 * @return the hostFeedbackDestination
	 */
	@Override
	public String getHostFeedbackDestination() {
		return hostFeedbackDestination;
	}

	/**
	 * @return the portFeedbackDestination
	 */
	@Override
	public int getPortFeedbackDestination() {
		return portFeedbackDestination;
	}

	/**
	 * @return the isSandboxDestination
	 */
	@Override
	public boolean isSandboxDestination() {
		return isSandboxDestination;
	}

	/**
	 * @return the isProductionDestination
	 */
	@Override
	public boolean isProductionDestination() {
		return isProductionDestination;
	}

	/**
	 * @return the reconnectPolicy
	 */
	@Override
	public ReconnectPolicy getReconnectPolicy() {
		return reconnectPolicy;
	}

	/**
	 * @return the hostSocksProxy
	 */
	@Override
	public String getHostSocksProxy() {
		return hostSocksProxy;
	}

	/**
	 * @return the portSocksProxy
	 */
	@Override
	public int getPortSocksProxy() {
		return portSocksProxy;
	}

	/**
	 * @return the proxySocket
	 */
	@Override
	public Socket getProxySocket() {
		return proxySocket;
	}

	/**
	 * @return the maxPoolConnections
	 */
	@Override
	public int getMaxPoolConnections() {
		return maxPoolConnections;
	}

	/**
	 * @return the isQueue
	 */
	@Override
	public boolean isQueue() {
		return isQueue;
	}

	/**
	 * @return the isNonBlocking
	 */
	@Override
	public boolean isNonBlocking() {
		return isNonBlocking;
	}

	/**
	 * @return the proxy
	 */
	@Override
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @return the apnsDelegate
	 */
	@Override
	public ApnsDelegate getApnsDelegate() {
		return apnsDelegate;
	}

	/**
	 * @return the isNoErrorDetection
	 */
	@Override
	public boolean isNoErrorDetection() {
		return isNoErrorDetection;
	}

	/**
	 * @param passwordCertification
	 *            the passwordCertification to set
	 */
	@Override
	public void setPasswordCertification(String passwordCertification) {
		this.passwordCertification = passwordCertification;
	}

	/**
	 * @param inputStreamCertification
	 *            the inputStreamCertification to set
	 */
	@Override
	public void setInputStreamCertification(InputStream inputStreamCertification) {
		this.inputStreamCertification = inputStreamCertification;
	}

	/**
	 * @param sslContext
	 *            the sslContext to set
	 */
	@Override
	public void setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	/**
	 * @param hostGatewayDestination
	 *            the hostGatewayDestination to set
	 */
	@Override
	public void setHostGatewayDestination(String hostGatewayDestination) {
		this.hostGatewayDestination = hostGatewayDestination;
	}

	/**
	 * @param portGatewayDestination
	 *            the portGatewayDestination to set
	 */
	@Override
	public void setPortGatewayDestination(int portGatewayDestination) {
		this.portGatewayDestination = portGatewayDestination;
	}

	/**
	 * @param hostFeedbackDestination
	 *            the hostFeedbackDestination to set
	 */
	@Override
	public void setHostFeedbackDestination(String hostFeedbackDestination) {
		this.hostFeedbackDestination = hostFeedbackDestination;
	}

	/**
	 * @param portFeedbackDestination
	 *            the portFeedbackDestination to set
	 */
	@Override
	public void setPortFeedbackDestination(int portFeedbackDestination) {
		this.portFeedbackDestination = portFeedbackDestination;
	}

	/**
	 * @param isSandboxDestination
	 *            the isSandboxDestination to set
	 */
	@Override
	public void setSandboxDestination(boolean isSandboxDestination) {
		this.isSandboxDestination = isSandboxDestination;
	}

	/**
	 * @param isProductionDestination
	 *            the isProductionDestination to set
	 */
	@Override
	public void setProductionDestination(boolean isProductionDestination) {
		this.isProductionDestination = isProductionDestination;
	}

	/**
	 * @param reconnectPolicy
	 *            the reconnectPolicy to set
	 */
	@Override
	public void setReconnectPolicy(ReconnectPolicy reconnectPolicy) {
		this.reconnectPolicy = reconnectPolicy;
	}

	/**
	 * @param hostSocksProxy
	 *            the hostSocksProxy to set
	 */
	@Override
	public void setHostSocksProxy(String hostSocksProxy) {
		this.hostSocksProxy = hostSocksProxy;
	}

	/**
	 * @param portSocksProxy
	 *            the portSocksProxy to set
	 */
	@Override
	public void setPortSocksProxy(int portSocksProxy) {
		this.portSocksProxy = portSocksProxy;
	}

	/**
	 * @param proxySocket
	 *            the proxySocket to set
	 */
	@Override
	public void setProxySocket(Socket proxySocket) {
		this.proxySocket = proxySocket;
	}

	/**
	 * @param maxPoolConnections
	 *            the maxPoolConnections to set
	 */
	@Override
	public void setMaxPoolConnections(int maxPoolConnections) {
		this.maxPoolConnections = maxPoolConnections;
	}

	/**
	 * @param isQueue
	 *            the isQueue to set
	 */
	@Override
	public void setQueue(boolean isQueue) {
		this.isQueue = isQueue;
	}

	/**
	 * @param isNonBlocking
	 *            the isNonBlocking to set
	 */
	@Override
	public void setNonBlocking(boolean isNonBlocking) {
		this.isNonBlocking = isNonBlocking;
	}

	/**
	 * @param proxy
	 *            the proxy to set
	 */
	@Override
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @param apnsDelegate
	 *            the apnsDelegate to set
	 */
	@Override
	public void setApnsDelegate(ApnsDelegate apnsDelegate) {
		this.apnsDelegate = apnsDelegate;
	}

	/**
	 * @param isNoErrorDetection
	 *            the isNoErrorDetection to set
	 */
	@Override
	public void setNoErrorDetection(boolean isNoErrorDetection) {
		this.isNoErrorDetection = isNoErrorDetection;
	}

	/**
	 * @param fileNameCertification
	 *            the fileNameCertification to set
	 */
	@Override
	public void setFileNameCertification(String fileNameCertification) {
		this.fileNameCertification = fileNameCertification;
	}

	/**
	 * @return the fileNameCertification
	 */
	@Override
	public String getFileNameCertification() {
		return fileNameCertification;
	}

}
