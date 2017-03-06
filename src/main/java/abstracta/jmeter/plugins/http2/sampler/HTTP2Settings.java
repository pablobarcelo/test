package abstracta.jmeter.plugins.http2.sampler;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.EncoderCache;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.logging.LoggingManager;

import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;



public class HTTP2Settings extends AbstractSampler implements TestStateListener, ThreadListener {
    private static final long serialVersionUID = 5859387434748163229L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final int DEFAULT_CONNECTION_TIMEOUT = 20000; //20 sec

    //public static final String METHOD = "HTTP2Settings.method";
    public static final String PROTOCOL_SCHEME = "HTTP2Settings.scheme";
    public static final String PORT = "HTTPSampler.port"; // $NON-NLS-1$

    private static final String ARG_VAL_SEP = "="; // $NON-NLS-1$
    private static final String QRY_SEP = "&"; // $NON-NLS-1$

    public static final String RETURN_NO_SAMPLE = "RETURN_NO_SAMPLE";
    public static final String CUSTOM_STATUS_CODE= "RETURN_CUSTOM_STATUS.code";
    public static final String CACHED_MODE_PROPERTY = "cache_manager.cached_resource_mode";

    private static final String HTTP_PREFIX = HTTPConstants.PROTOCOL_HTTP+"://"; // $NON-NLS-1$
    private static final String HTTPS_PREFIX = HTTPConstants.PROTOCOL_HTTPS+"://"; // $NON-NLS-1$
    private static final String DEFAULT_PROTOCOL = HTTPConstants.PROTOCOL_HTTPS;
    
    private static final String QRY_PFX = "?"; // $NON-NLS-1$
    
    /** A number to indicate that the port has not been set. */
    public static final int UNSPECIFIED_PORT = 0;
    public static final String UNSPECIFIED_PORT_AS_STRING = "0"; // $NON-NLS-1$
    
    protected static final String NON_HTTP_RESPONSE_CODE = "Non HTTP response code";

    protected static final String NON_HTTP_RESPONSE_MESSAGE = "Non HTTP response message";
    
    

    public HTTP2Settings() {
        super();
        setName("HTTP2 Connection Settings");
    }

    @Override
    public void setName(String name) {
        if (name != null) {
            setProperty(TestElement.NAME, name);
        }
    }

    @Override
    public String getName() {
        return getPropertyAsString(TestElement.NAME);
    }


    @Override
    public SampleResult sample(Entry entry) {
        return sample();
    }
    
    /**
     * Perform a sample, and return the results
     *
     * @return results of the sampling
     */
    public SampleResult sample() {
        SampleResult res = null;
        try {
            res = sample(getUrl(), null, false, 0);
            if (res != null) {
                res.setSampleLabel(getName());
            }
            return res;
        } catch (Exception e) {
        	return res;
        }
    }
    
    /**
     * Populates the provided HTTPSampleResult with details from the Exception.
     * Does not create a new instance, so should not be used directly to add a subsample.
     *
     * @param e
     *            Exception representing the error.
     * @param res
     *            SampleResult to be modified
     * @return the modified sampling result containing details of the Exception.
     */
    protected HTTPSampleResult errorResult(Throwable e, HTTPSampleResult res) {
        res.setSampleLabel(res.getSampleLabel());
        res.setDataType(SampleResult.TEXT);
        ByteArrayOutputStream text = new ByteArrayOutputStream(200);
        e.printStackTrace(new PrintStream(text));
        res.setResponseData(text.toByteArray());
        res.setResponseCode(NON_HTTP_RESPONSE_CODE+": " + e.getClass().getName());
        res.setResponseMessage(NON_HTTP_RESPONSE_MESSAGE+": " + e.getMessage());
        res.setSuccessful(false);
        return res;
    }

    String getRequestIdForConnectionsMap() {
        return getThreadName() + getRequestId();
    }

 

    protected HTTPSampleResult sample(URL url, String method, boolean followRedirects, int depth) {
    	
        // Create Sample Result
        HTTPSampleResult sampleResult = createSampleResult(url,method);

       
       
        sampleResult.sampleStart();
        
        // End sampling
        sampleResult.sampleEnd();
        sampleResult.setSuccessful(true);

        return sampleResult;
    }
    
    /**
     * Create HTTPSampleResult filling url, method and SampleLabel.
     * Monitor field is computed calling isMonitor()
     * @param url URL
     * @param method HTTP Method
     * @return {@link HTTPSampleResult}
     */
    protected HTTPSampleResult createSampleResult(URL url, String method) {
        HTTPSampleResult res = new HTTPSampleResult();

        res.setSampleLabel(url.toString()); // May be replaced later
        res.setHTTPMethod(method);
        res.setURL(url);
        
        return res;
    }

    
    /**
     * Get the URL, built from its component parts.
     *
     * <p>
     * As a special case, if the path starts with "http[s]://",
     * then the path is assumed to be the entire URL.
     * </p>
     *
     * @return The URL to be requested by this sampler.
     * @throws MalformedURLException if url is malformed
     */
    public URL getUrl() throws MalformedURLException {
        StringBuilder pathAndQuery = new StringBuilder(100);
        String path = this.getContextPath();
        // Hack to allow entire URL to be provided in host field
        if (path.startsWith(HTTP_PREFIX)
                || path.startsWith(HTTPS_PREFIX)) {
            return new URL(path);
        }
        
        String domain = getDomain();
        String protocol = getProtocolScheme();
        
        // HTTP URLs must be absolute, allow file to be relative
        if (!path.startsWith("/")) { // $NON-NLS-1$
            pathAndQuery.append("/"); // $NON-NLS-1$
        }
        pathAndQuery.append(path);

       
        // If default port for protocol is used, we do not include port in URL
        if (isProtocolDefaultPort()) {
            return new URL(protocol, domain, pathAndQuery.toString());
        }
        return new URL(protocol, domain, getPort(), pathAndQuery.toString());
    }
    
    /**
     * Tell whether the default port for the specified protocol is used
     *
     * @return true if the default port number for the protocol is used, false otherwise
     */
    public boolean isProtocolDefaultPort() {
        final int port = getPortIfSpecified();
        final String protocol = getProtocolScheme();
        boolean isDefaultHTTPPort = HTTPConstants.PROTOCOL_HTTP
                .equalsIgnoreCase(protocol)
                && port == HTTPConstants.DEFAULT_HTTP_PORT;
        boolean isDefaultHTTPSPort = HTTPConstants.PROTOCOL_HTTPS
                .equalsIgnoreCase(protocol)
                && port == HTTPConstants.DEFAULT_HTTPS_PORT;
        return port == UNSPECIFIED_PORT ||
                isDefaultHTTPPort ||
                isDefaultHTTPSPort;
    }
    
    /**
     * Get the port number from the port string, allowing for trailing blanks.
     *
     * @return port number or UNSPECIFIED_PORT (== 0)
     */
    public int getPortIfSpecified() {
        String port_s = getPropertyAsString(PORT, UNSPECIFIED_PORT_AS_STRING);
        try {
            return Integer.parseInt(port_s.trim());
        } catch (NumberFormatException e) {
            return UNSPECIFIED_PORT;
        }
    }
    
    /**
     * Get the port; apply the default for the protocol if necessary.
     *
     * @return the port number, with default applied if required.
     */
    public int getPort() {
        final int port = getPortIfSpecified();
        if (port == UNSPECIFIED_PORT) {
            String prot = getProtocolScheme();
            if (HTTPConstants.PROTOCOL_HTTPS.equalsIgnoreCase(prot)) {
                return HTTPConstants.DEFAULT_HTTPS_PORT;
            }
            if (!HTTPConstants.PROTOCOL_HTTP.equalsIgnoreCase(prot)) {
                log.warn("Unexpected protocol: " + prot);
                // TODO - should this return something else?
            }
            return HTTPConstants.DEFAULT_HTTP_PORT;
        }
        return port;
    }
   
    public void setProtocolScheme(String value) {
        setProperty(PROTOCOL_SCHEME, value);
    }

    public String getProtocolScheme() {        
        String protocol = getPropertyAsString(PROTOCOL_SCHEME);
        if (protocol == null || protocol.length() == 0) {
            return DEFAULT_PROTOCOL;
        }
        return protocol;
    }
    
    /**
     * Gets the QueryString attribute of the UrlConfig object, using the
     * specified encoding to encode the parameter values put into the URL
     *
     * @param contentEncoding the encoding to use for encoding parameter values
     * @return the QueryString value
     */
    public String getQueryString(String contentEncoding) {
        // Check if the sampler has a specified content encoding
        if (JOrphanUtils.isBlank(contentEncoding)) {
            // We use the encoding which should be used according to the HTTP spec, which is UTF-8
            contentEncoding = EncoderCache.URL_ARGUMENT_ENCODING;
        }
        StringBuilder buf = new StringBuilder();
        PropertyIterator iter = getArguments().iterator();
        boolean first = true;
        while (iter.hasNext()) {
            HTTPArgument item = null;
            /*
             * N.B. Revision 323346 introduced the ClassCast check, but then used iter.next()
             * to fetch the item to be cast, thus skipping the element that did not cast.
             * Reverted to work more like the original code, but with the check in place.
             * Added a warning message so can track whether it is necessary
             */
            Object objectValue = iter.next().getObjectValue();
            try {
                item = (HTTPArgument) objectValue;
            } catch (ClassCastException e) {
                log.warn("Unexpected argument type: " + objectValue.getClass().getName());
                item = new HTTPArgument((Argument) objectValue);
            }
            final String encodedName = item.getEncodedName();
            if (encodedName.length() == 0) {
                continue; // Skip parameters with a blank name (allows use of optional variables in parameter lists)
            }
            if (!first) {
                buf.append(QRY_SEP);
            } else {
                first = false;
            }
            buf.append(encodedName);
            if (item.getMetaData() == null) {
                buf.append(ARG_VAL_SEP);
            } else {
                buf.append(item.getMetaData());
            }

            // Encode the parameter value in the specified content encoding
            try {
                buf.append(item.getEncodedValue(contentEncoding));
            } catch(UnsupportedEncodingException e) {
                log.warn("Unable to encode parameter in encoding " + contentEncoding + ", parameter value not included in query string");
            }
        }
        return buf.toString();
    }

    public void setDomain(String domain) {
        setProperty("domain", domain);
    }

    public String getDomain() {
        return getPropertyAsString("domain");
    }


    public void setContextPath(String contextPath) {
        setProperty("contextPath", contextPath);
    }

    public String getContextPath() {
        return getPropertyAsString("contextPath");
    }

    public String getServerPort() {
        final String portAsString = getPropertyAsString("serverPort", "0");
        Integer port;
        try {
            port = Integer.valueOf(portAsString);
            return port.toString();
        } catch (NumberFormatException ex) {
            return portAsString;
        }
    }



    public void setrPort(String port) {
        setProperty("sereverPort", port);
    }

    public void setArguments(Arguments arguments) {
        setProperty(new TestElementProperty("arguments", arguments));
    }

    public Arguments getArguments() {
        return (Arguments) getProperty("arguments").getObjectValue();
    }

    public void setStreamingConnection(Boolean streamingConnection) {
        setProperty("streamingConnection", streamingConnection);
    }

    public Boolean isStreamingConnection() {
        return getPropertyAsBoolean("streamingConnection");
    }

    public void setRequestId(String requestId) {
        setProperty("requestId", requestId);
    }

    public String getRequestId() {
        return getPropertyAsString("requestId");
    }

    public String getResponseTimeout() {
        return getPropertyAsString("responseTimeout", "5000");
    }

    public void setResponseTimeout(String responseTimeout) {
        setProperty("responseTimeout", responseTimeout);
    }

    public void setContentEncoding(String contentEncoding) {
        setProperty("contentEncoding", contentEncoding);
    }

    public String getContentEncoding() {
        return getPropertyAsString("contentEncoding", "UTF-8");
    }
    
    public void setServerPush(Boolean serverPush) {
        setProperty("serverPush", serverPush);
    }
    
    public Boolean isServerPush() {
        return getPropertyAsBoolean("serverPush");
    }

    private HeaderManager getHeaderManager() {
        return (HeaderManager) getProperty(HTTPSamplerBase.HEADER_MANAGER).getObjectValue();
    }

    @Override
    public void testStarted() {
        testStarted("unknown");
    }

    @Override
    public void testStarted(String host) {
        try {
            setProperty("connectionTimeoutInt", Integer.parseInt(getResponseTimeout()));
        } catch (NumberFormatException ex) {
            log.warn("Connection timeout is not a number; using the default connection timeout of " + DEFAULT_CONNECTION_TIMEOUT + " ms");
            setProperty("connectionTimeoutInt", DEFAULT_CONNECTION_TIMEOUT);
        }
    }

    @Override
    public void testEnded() {
        testEnded("unknown");

    }

	@Override
	public void threadStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testEnded(String host) {
		// TODO Auto-generated method stub
		
	}

	public void setPort(String port2) {
		// TODO Auto-generated method stub
		
	}



  


}

