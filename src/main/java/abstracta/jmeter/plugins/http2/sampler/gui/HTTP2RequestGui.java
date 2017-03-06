package abstracta.jmeter.plugins.http2.sampler.gui;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import abstracta.jmeter.plugins.http2.sampler.HTTP2Request;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class HTTP2RequestGui extends AbstractSamplerGui {

    private static final Logger log = LoggingManager.getLoggerForClass();
    
    private JCheckBox retrieveEmbeddedResources;
      
    private HTTP2RequestPanel http2RequestPanel;
    
    private JCheckBox useMD5;
    
    private JLabeledTextField embeddedRE; // regular expression used to match against embedded resource URLs

    private JTextField sourceIpAddr; // does not apply to Java implementation
    
    private JComboBox<String> sourceIpType = new JComboBox<>(HTTPSamplerBase.getSourceTypeList());

    public HTTP2RequestGui(){
        super();
        init();

        
    }
    
    protected JPanel createEmbeddedRsrcPanel() {
        // retrieve Embedded resources
        retrieveEmbeddedResources = new JCheckBox(JMeterUtils.getResString("web_testing_retrieve_images")); // $NON-NLS-1$

        final JPanel embeddedRsrcPanel = new HorizontalPanel();
        embeddedRsrcPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), JMeterUtils
                .getResString("web_testing_retrieve_title"))); // $NON-NLS-1$
        embeddedRsrcPanel.add(retrieveEmbeddedResources);

        
        // Embedded URL match regex
        embeddedRE = new JLabeledTextField(JMeterUtils.getResString("web_testing_embedded_url_pattern"),20); // $NON-NLS-1$
        embeddedRsrcPanel.add(embeddedRE); 
        
        return embeddedRsrcPanel;
    }
    
    protected JPanel createSourceAddrPanel() {
        final JPanel sourceAddrPanel = new HorizontalPanel();
        sourceAddrPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), JMeterUtils
                .getResString("web_testing_source_ip"))); // $NON-NLS-1$

        sourceIpType.setSelectedIndex(HTTPSamplerBase.SourceType.HOSTNAME.ordinal()); //default: IP/Hostname
        sourceAddrPanel.add(sourceIpType);

        sourceIpAddr = new JTextField();
        sourceAddrPanel.add(sourceIpAddr);
        return sourceAddrPanel;
    }
    
    protected JPanel createOptionalTasksPanel() {
        // OPTIONAL TASKS
        final JPanel checkBoxPanel = new VerticalPanel();
        checkBoxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), JMeterUtils
                .getResString("optional_tasks"))); // $NON-NLS-1$

        // Use MD5
        useMD5 = new JCheckBox(JMeterUtils.getResString("response_save_as_md5")); // $NON-NLS-1$

        checkBoxPanel.add(useMD5);

        return checkBoxPanel;
    }

    @Override
    public String getStaticLabel() {
        return "HTTP2 Request";
    }

    public String getLabelResource() {
        return "HTTP2 Request";
    }

    public TestElement createTestElement() {
        HTTP2Request sampler = new HTTP2Request();

        modifyTestElement(sampler);

        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        HTTP2Request sampler = (HTTP2Request) element;
        http2RequestPanel.setDomain(sampler.getDomain());
        http2RequestPanel.setPort(Integer.toString(sampler.getPort()));
        http2RequestPanel.setResponseTimeout(sampler.getResponseTimeout());
        
        http2RequestPanel.setHTTP2Implementation(sampler.getPropertyAsString(HTTP2Request.IMPLEMENTATION));
        http2RequestPanel.setProtocol(sampler.getProtocol());
        http2RequestPanel.setMethod(sampler.getMethod());
        http2RequestPanel.setContentEncoding(sampler.getContentEncoding());
        http2RequestPanel.setRequestId(sampler.getRequestId());
        
        http2RequestPanel.setContextPath(sampler.getContextPath());

        http2RequestPanel.setAutoRedirect(sampler.isAutoRedirect());
        http2RequestPanel.setFollowRedirects(sampler.isFollowRedirects());
        http2RequestPanel.setSyncRequest(sampler.isSyncRequest());
        

        
    }

    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        if (element instanceof HTTP2Request) {
            HTTP2Request http2Request = (HTTP2Request) element;
            http2Request.setDomain(http2RequestPanel.getDomain());
            http2Request.setPort(http2RequestPanel.getPort());
            http2Request.setResponseTimeout(http2RequestPanel.getResponseTimeout());
            
            element.setProperty(HTTP2Request.IMPLEMENTATION, http2RequestPanel.getHttp2Implementacion(),"");
            http2Request.setProtocol(http2RequestPanel.getProtocol());
            http2Request.setMethod(http2RequestPanel.getMethod());
            http2Request.setContentEncoding(http2RequestPanel.getContentEncoding());
            http2Request.setRequestId(http2RequestPanel.getRequestId());
            
            http2Request.setContextPath(http2RequestPanel.getContextPath());

            http2Request.setAutoRedirect(http2RequestPanel.isAutoRedirect());
            http2Request.setFollowRedirects(http2RequestPanel.isFollowRedirects());
            http2Request.setSyncRequest(http2RequestPanel.isSyncRequest());

        }
           
    }
    
    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
    

    @Override
    public void clearGui() {
        super.clearGui();
        retrieveEmbeddedResources.setSelected(false);
        useMD5.setSelected(false);
        embeddedRE.setText(""); // $NON-NLS-1$
        sourceIpAddr.setText(""); // $NON-NLS-1$
        sourceIpType.setSelectedIndex(HTTPSamplerBase.SourceType.HOSTNAME.ordinal()); //default: IP/Hostname
        http2RequestPanel.clear();
    }

    private void init() {
    	setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        
        http2RequestPanel = new HTTP2RequestPanel();
        
        // AdvancedPanel (embedded resources, source address and optional tasks)
        JPanel advancedPanel = new VerticalPanel();
        advancedPanel.add(createEmbeddedRsrcPanel());
        advancedPanel.add(createSourceAddrPanel());
        advancedPanel.add(createOptionalTasksPanel());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(JMeterUtils
                .getResString("web_testing_basic"), http2RequestPanel);
        tabbedPane.add(JMeterUtils
                .getResString("web_testing_advanced"), advancedPanel);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setMaximumSize(new Dimension());

        add(makeTitlePanel(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);        
        add(emptyPanel, BorderLayout.SOUTH);
    }
}
