package abstracta.jmeter.plugins.http2.sampler;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;



public class HTTP2SyncPoint extends AbstractSampler implements TestStateListener, ThreadListener {
    private static final long serialVersionUID = 5859387434748163229L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final int DEFAULT_CONNECTION_TIMEOUT = 20000; //20 sec
    public static final int DEFAULT_RESPONSE_TIMEOUT = 20000; //20 sec

    public static final String REQUEST_ID = "HTTP2SyncPoint.requestId";



    public static final String RETURN_NO_SAMPLE = "RETURN_NO_SAMPLE";
    public static final String CUSTOM_STATUS_CODE= "RETURN_CUSTOM_STATUS.code";
    public static final String CACHED_MODE_PROPERTY = "cache_manager.cached_resource_mode";
    

    private static final String DEFAULT_PROTOCOL = HTTPConstants.PROTOCOL_HTTPS;
   
    
    /** A number to indicate that the port has not been set. */
    public static final int UNSPECIFIED_PORT = 0;
    public static final String UNSPECIFIED_PORT_AS_STRING = "0"; // $NON-NLS-1$
    
    protected static final String NON_HTTP_RESPONSE_CODE = "Non HTTP response code";

    protected static final String NON_HTTP_RESPONSE_MESSAGE = "Non HTTP response message";
    
    

    public HTTP2SyncPoint() {
        super();
        setName("Syncronizing Point");
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
       
    public String getResponseTimeout() {
        return getPropertyAsString("responseTimeout", "20000");
    }

    public void setResponseTimeout(String responseTimeout) {
        setProperty("responseTimeout", responseTimeout);
    }

    public void setArguments(Arguments arguments) {
        setProperty(new TestElementProperty("arguments", arguments));
    }

    public Arguments getArguments() {
        return (Arguments) getProperty("arguments").getObjectValue();
    }

    

    @Override
    public void testStarted() {
        testStarted("unknown");
    }

    @Override
    public void testStarted(String host) {
        try {
            setProperty("responseTimeoutInt", Integer.parseInt(getResponseTimeout()));
        } catch (NumberFormatException ex) {
            log.warn("Response timeout is not a number; using the default response timeout of " + DEFAULT_RESPONSE_TIMEOUT + " ms");
            setProperty("responseTimeoutInt", DEFAULT_RESPONSE_TIMEOUT);
        }
    }

    @Override
    public void testEnded() {
        testEnded("unknown");

    }

    @Override
    public void testEnded(String host) {
//        for (Http2Client client : connectionsMap.values()) {
//            try {
//                client.close();
//            } catch (Exception ex) {
//                log.warn("Failed to close HTTP/2 client", ex);
//            }
//        }
    }

    @Override
    public void threadStarted() {

    }
    
    /**
     * Perform a sample, and return the results
     *
     * @return results of the sampling
     */
    public SampleResult sample() {
        SampleResult res = null;
        try {
            res = sample(null, null, false, 0);
            if (res != null) {
                res.setSampleLabel(getName());
            }
            return res;
        } catch (Exception e) {
        	return res;
        }
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

     //res.setSampleLabel(url.toString()); // May be replaced later
     res.setHTTPMethod(method);
     res.setURL(url);
     
     return res;
 }

    @Override
    public void threadFinished() {
//        Http2Client client = connectionsMap.get(getConnectionIdForConnectionsMap());
//        if (client != null) {
//            try {
//                client.close();
//                connectionsMap.remove(client);
//            } catch (Exception ex) {
//                log.warn("Failed to close client " + getConnectionIdForConnectionsMap(), ex);
//            }
//        }
    }


}

