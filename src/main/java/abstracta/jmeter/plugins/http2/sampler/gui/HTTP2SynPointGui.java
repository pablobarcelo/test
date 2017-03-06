package abstracta.jmeter.plugins.http2.sampler.gui;

import org.apache.commons.lang3.StringUtils;
import abstracta.jmeter.plugins.http2.sampler.HTTP2SyncPoint;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HTTP2SynPointGui extends AbstractSamplerGui {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private JPanel http2SyncPointPanel= new JPanel();;
    private JPanel waitReqPanel= new JPanel();
    
    private JLabel requestIdLabel = new JLabel();
    private JTextField requestId = new JTextField(20);

    public HTTP2SynPointGui(){
        super();
        init();

        
    }
    
    private void init() {
    	setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        
        initComponents();
        JPanel emptyPanel = new JPanel();
        emptyPanel.setMaximumSize(new Dimension());
        
        add(http2SyncPointPanel, BorderLayout.CENTER);
        add(makeTitlePanel(), BorderLayout.NORTH);
        
    }

    @Override
    public String getStaticLabel() {
        return "HTTP2 Syncronization Point";
    }

    public String getLabelResource() {
        return "HTTP2 Syncronization Point";
    }

    public TestElement createTestElement() {
        HTTP2SyncPoint sampler = new HTTP2SyncPoint();
        modifyTestElement(sampler);

        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        if (element instanceof HTTP2SyncPoint) {
            HTTP2SyncPoint sampler = (HTTP2SyncPoint) element;
            requestId.setText(sampler.getPropertyAsString(HTTP2SyncPoint.REQUEST_ID));
   
        }
    }

    public void modifyTestElement(TestElement config) {
    	super.configureTestElement(config);
        if(!StringUtils.isEmpty(requestId.getText())) {
                config.setProperty(new StringProperty(HTTP2SyncPoint.REQUEST_ID,
                        requestId.getText()));
         } else {
                config.removeProperty(HTTP2SyncPoint.REQUEST_ID);

        }
              
    }
    
private void initComponents() {
    	
    	waitReqPanel.setBorder(BorderFactory.createTitledBorder("Wait for response of request:"));
    	requestIdLabel.setText("Request ID:");

        GroupLayout waitPanelLayout = new javax.swing.GroupLayout(waitReqPanel);
        waitReqPanel.setLayout(waitPanelLayout);
        waitPanelLayout.setHorizontalGroup(
                waitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(waitPanelLayout.createSequentialGroup()
                        		.addContainerGap()
                                .addComponent(requestIdLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(requestId)
                                .addContainerGap())
        );
        waitPanelLayout.setVerticalGroup(
                waitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(waitPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(waitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                                .addComponent(requestIdLabel)
		                                .addComponent(requestId))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(http2SyncPointPanel);
        http2SyncPointPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                        .addComponent(waitReqPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                        .addComponent(waitReqPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

        );
        this.add(makeTitlePanel(), BorderLayout.NORTH);
        this.add(http2SyncPointPanel, BorderLayout.CENTER);

    }

    @Override
    public void clearGui() {
        super.clearGui();
        requestId.setText("");
    }

}
