package abstracta.jmeter.plugins.http2.sampler.gui;

import abstracta.jmeter.plugins.http2.sampler.HTTP2Settings;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HTTP2SettingsGui extends AbstractSamplerGui {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private JPanel http2SettingsPanel = new JPanel();
    
    private JPanel webRequestPanel = new JPanel();
    private JPanel webServerPanel = new JPanel();
    private JPanel timeoutPanel = new JPanel();

    private JLabel connectionTimeoutLabel = new JLabel();
    private JLabel contentEncodingLabel = new JLabel();

    private JLabel protocolLabel = new JLabel();
    private JLabel domainLabel = new JLabel();
    private JLabel portLabel = new JLabel();


    private JTextField responseTimeout = new JTextField();
    private JTextField contentEncoding= new JTextField();
    
    private JTextField protocol = new JTextField();
    private JCheckBox serverPush = new JCheckBox();
    private JTextField domain = new JTextField();
    private JTextField port = new JTextField();
    
    private JLabel maxConcStreamsLabel = new JLabel();
    private JTextField maxConcurrentStreams = new JTextField();
    
    private JLabel maxFrameSizeLabel = new JLabel();
    private JTextField maxFrameSize = new JTextField();
    
    private JLabel maxHeaderListSizeLabel = new JLabel();
    private JTextField maxHeaderListSize = new JTextField();

    public HTTP2SettingsGui(){
        super();
        init();
    }

    @Override
    public String getStaticLabel() {
        return "HTTP2 Settings";
    }

    public String getLabelResource() {
        return "HTTP2 Settings";
    }

    public TestElement createTestElement() {
    	HTTP2Settings sampler = new HTTP2Settings();

        modifyTestElement(sampler);

        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof HTTP2Settings) {
            HTTP2Settings sampler = (HTTP2Settings) element;
            domain.setText(sampler.getDomain());
            port.setText(sampler.getServerPort());
            
            responseTimeout.setText(sampler.getResponseTimeout());
            
            protocol.setText(sampler.getProtocolScheme());
            contentEncoding.setText(sampler.getContentEncoding());
            
            serverPush.setSelected(sampler.isServerPush());

        }
    }

    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        if (element instanceof HTTP2Settings) {
            HTTP2Settings HTTP2Settings = (HTTP2Settings) element;
            HTTP2Settings.setDomain(domain.getText());
            HTTP2Settings.setPort(port.getText());
            
            HTTP2Settings.setResponseTimeout(responseTimeout.getText());
            
            HTTP2Settings.setProtocolScheme(protocol.getText());
            HTTP2Settings.setContentEncoding(contentEncoding.getText());
            
            HTTP2Settings.setServerPush(serverPush.isSelected());

        }
        
      
    }

    @Override
    public void clearGui() {
        super.clearGui();
        domain.setText("");
        port.setText("");
        
        responseTimeout.setText("");
        
        protocol.setText("");
        contentEncoding.setText("");
        
        serverPush.setSelected(false);
      
    }

    private void init() {
    	setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        
        initComponents();
        JPanel emptyPanel = new JPanel();
        emptyPanel.setMaximumSize(new Dimension());
        
        add(http2SettingsPanel, BorderLayout.CENTER);
        add(makeTitlePanel(), BorderLayout.NORTH);
    }
    
    private void initComponents() {

        domainLabel.setText("Server Name or IP:");
        portLabel.setText("Port Number:");

        webServerPanel.setBorder(BorderFactory.createTitledBorder("Web Server"));

        GroupLayout webServerPanelLayout = new javax.swing.GroupLayout(webServerPanel);
        webServerPanel.setLayout(webServerPanelLayout);
        webServerPanelLayout.setHorizontalGroup(
                webServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(webServerPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(domainLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(domain)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(portLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        webServerPanelLayout.setVerticalGroup(
                webServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(webServerPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(webServerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(domainLabel)
                                        .addComponent(domain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(portLabel)
                                        .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        timeoutPanel.setBorder(BorderFactory.createTitledBorder("Timeout (miliseconds)"));
        connectionTimeoutLabel.setText("Response:");


        javax.swing.GroupLayout timeoutPanelLayout = new javax.swing.GroupLayout(timeoutPanel);
        timeoutPanel.setLayout(timeoutPanelLayout);
        timeoutPanelLayout.setHorizontalGroup(
                timeoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(timeoutPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(connectionTimeoutLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(responseTimeout)
                                .addContainerGap())
        );
        timeoutPanelLayout.setVerticalGroup(
                timeoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(timeoutPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(timeoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(connectionTimeoutLabel)
                                        .addComponent(responseTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contentEncodingLabel.setText("Content encoding:");

        protocolLabel.setText("Protocol [http/https]:");
        

        webRequestPanel.setBorder(BorderFactory.createTitledBorder("HTTP Request"));

        protocol.setToolTipText("");

        serverPush.setText("Server Push");
        maxConcStreamsLabel.setText("Max. Concurrent Streams");
        maxFrameSizeLabel.setText("Max. Frame Size");
        maxHeaderListSizeLabel.setText("Max. Header List Size");

        javax.swing.GroupLayout webRequestPanelLayout = new javax.swing.GroupLayout(webRequestPanel);
        webRequestPanel.setLayout(webRequestPanelLayout);
        webRequestPanelLayout.setHorizontalGroup(
                webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                                .addComponent(protocolLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(protocol, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(contentEncodingLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(contentEncoding, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                        		.addComponent(serverPush)
                                        		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        		.addComponent(maxConcStreamsLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        		.addComponent(maxConcurrentStreams, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        		.addComponent(maxFrameSizeLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        		.addComponent(maxFrameSize, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        		.addComponent(maxHeaderListSizeLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        		.addComponent(maxHeaderListSize, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                .addContainerGap())
        );
        webRequestPanelLayout.setVerticalGroup(
                webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(protocolLabel)
                                        .addComponent(protocol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(contentEncodingLabel)
                                        .addComponent(contentEncoding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addContainerGap()
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                		.addComponent(serverPush)
                                		.addComponent(maxConcStreamsLabel)
                                		.addComponent(maxConcurrentStreams)
                                		.addComponent(maxFrameSizeLabel)
                                		.addComponent(maxFrameSize)
                                		.addComponent(maxHeaderListSizeLabel)
                                		.addComponent(maxHeaderListSize))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addContainerGap())
        );


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(http2SettingsPanel);
        http2SettingsPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(webRequestPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(webServerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(timeoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                )
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(timeoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(webServerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(webRequestPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }
}
