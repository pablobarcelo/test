package abstracta.jmeter.plugins.http2.sampler.gui;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.protocol.http.gui.HTTPArgumentsPanel;
import org.apache.jmeter.protocol.http.gui.HTTPFileArgsPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import abstracta.jmeter.plugins.http2.sampler.HTTP2Request;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;

import javax.swing.*;

public class HTTP2RequestPanel extends JPanel {

    private HTTPArgumentsPanel argsPanel;
    
    private static final int TAB_PARAMETERS = 0;
    
    // handle tabs
    private int tabRawBodyIndex = 1;   
    private int tabFileUploadIndex = 2;
    
    private HTTPFileArgsPanel filesPanel;
    
    private JPanel webRequestPanel = new JPanel();
    private JPanel webServerPanel = new JPanel();
    private JPanel timeoutPanel = new JPanel();

    private JLabel requestIdLabel = new JLabel();
    private JLabel contentEncodingLabel = new JLabel();
    private JLabel contextPathLabel = new JLabel();

    private JLabel protocolLabel = new JLabel();
    private JLabel responseTimeoutLabel = new JLabel();
    private JLabel domainLabel = new JLabel();
    private JLabel portLabel = new JLabel();


    private JTextField requestId = new JTextField();
    private JTextField contentEncoding = new JTextField();
    private JTextField path = new JTextField();
    
    private JTextField protocol = new JTextField();
    private JPanel parametersPanel = new JPanel();
    private JTextField responseTimeout = new JTextField();
    private JTextField domain = new JTextField(20);
    private JTextField port = new JTextField();
    
    private JCheckBox autoRedirects = new JCheckBox();
    private JCheckBox followRedirects = new JCheckBox();
    private JCheckBox syncRequest = new JCheckBox();
    
    private JLabel methodLabel = new JLabel();
    private JComboBox method = new JComboBox();
    
    private JLabel http2ImplementationLabel = new JLabel();
    private JComboBox http2Implementation = new JComboBox();
    

    
    // Tabbed pane that contains parameters and raw body
    private ValidationTabbedPane postContentTabbedPane;
    
    
    // Body data
    private JSyntaxTextArea postBodyContent;
    


    public HTTP2RequestPanel() {
        initComponents();
        
        
        postContentTabbedPane = new ValidationTabbedPane();
        argsPanel = new HTTPArgumentsPanel();
        postContentTabbedPane.add(JMeterUtils.getResString("post_as_parameters"), argsPanel);// $NON-NLS-1$
        
        int indx = TAB_PARAMETERS;
        
        tabRawBodyIndex = ++indx;
        postBodyContent = JSyntaxTextArea.getInstance(30, 50);// $NON-NLS-1$
        postContentTabbedPane.add(JMeterUtils.getResString("post_body"), JTextScrollPane.getInstance(postBodyContent));// $NON-NLS-1$

        tabFileUploadIndex = ++indx;
        filesPanel = new HTTPFileArgsPanel();
        postContentTabbedPane.add(JMeterUtils.getResString("post_files_upload"), filesPanel);            


        parametersPanel.add(postContentTabbedPane);
    }

    

	private void initComponents() {

        domainLabel.setText(JMeterUtils.getResString("web_server_domain"));
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
        responseTimeoutLabel.setText("Response:");


        javax.swing.GroupLayout timeoutPanelLayout = new javax.swing.GroupLayout(timeoutPanel);
        timeoutPanel.setLayout(timeoutPanelLayout);
        timeoutPanelLayout.setHorizontalGroup(
                timeoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(timeoutPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(responseTimeoutLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(responseTimeout)
                                .addContainerGap())
        );
        timeoutPanelLayout.setVerticalGroup(
                timeoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(timeoutPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(timeoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(responseTimeoutLabel)
                                        .addComponent(responseTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        requestIdLabel.setText("Request Id:");

        contentEncodingLabel.setText("Content encoding:");
        contextPathLabel.setText("Path:");

        protocolLabel.setText("Protocol [http/https]:");
        
        methodLabel.setText("Method:");
        String[] methodList=HTTPSamplerBase.getValidMethodsAsArray();
        for (int x=0;x<methodList.length;x++)
        	 method.addItem(methodList[x]);
        http2ImplementationLabel.setText(JMeterUtils.getResString("http_implementation"));
        http2Implementation.addItem("");
        http2Implementation.addItem("Java");
        http2Implementation.addItem("Jetty");

        webRequestPanel.setBorder(BorderFactory.createTitledBorder("HTTP Request"));


        protocol.setToolTipText("");

        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.LINE_AXIS));
        
        autoRedirects.setText("Redirect Automatically");
        followRedirects.setText("Follow Redirects");
        syncRequest.setText("Synchronized Request");

        javax.swing.GroupLayout webRequestPanelLayout = new javax.swing.GroupLayout(webRequestPanel);
        webRequestPanel.setLayout(webRequestPanelLayout);
        webRequestPanelLayout.setHorizontalGroup(
                webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(parametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                        		.addComponent(http2ImplementationLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(http2Implementation)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(protocolLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(protocol, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(methodLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(method, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(contentEncodingLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(contentEncoding, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(requestIdLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(requestId))
                                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                                        		.addComponent(autoRedirects)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(followRedirects)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(syncRequest)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                                .addComponent(contextPathLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(path)))
                                .addContainerGap())
        );
        webRequestPanelLayout.setVerticalGroup(
                webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(webRequestPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                		.addComponent(http2ImplementationLabel)
                                		.addComponent(http2Implementation)
                                        .addComponent(protocolLabel)
                                        .addComponent(protocol)
                                        .addComponent(methodLabel)
                                        .addComponent(method)
                                        .addComponent(contentEncodingLabel)
                                        .addComponent(contentEncoding)
                                        .addComponent(requestIdLabel)
                                        .addComponent(requestId))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(contextPathLabel)
                                        .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(webRequestPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                		.addComponent(autoRedirects)
                                		.addComponent(followRedirects)
                                		.addComponent(syncRequest))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(parametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                .addContainerGap())
        );


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
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
    
    
    /**
     * 
     */
    class ValidationTabbedPane extends JTabbedPane {

        /**
         * 
         */
        private static final long serialVersionUID = 7014311238367882880L;

        
        @Override
        public void setSelectedIndex(int index) {
            setSelectedIndex(index, true);
        }
        
        /**
         * Apply some check rules if check is true
         *
         * @param index
         *            index to select
         * @param check
         *            flag whether to perform checks before setting the selected
         *            index
         */
        public void setSelectedIndex(int index, boolean check) {
            int oldSelectedIndex = this.getSelectedIndex();
            if(!check || oldSelectedIndex == -1) {
                super.setSelectedIndex(index);
            }
            else if(index != oldSelectedIndex)
            {
                // If there is no data, then switching between Parameters/file upload and Raw should be
                // allowed with no further user interaction.
                if(noData(oldSelectedIndex)) {
                    argsPanel.clear();
                    postBodyContent.setInitialText("");
                    filesPanel.clear();
                    
                    super.setSelectedIndex(index);
                }
                else {
                    boolean filePanelHasData = false;
                    filePanelHasData = filesPanel.hasData();

                    
                    if(oldSelectedIndex == tabRawBodyIndex) {
                        
                        // If RAW data and Parameters match we allow switching
                        if(index == TAB_PARAMETERS && postBodyContent.getText().equals(computePostBody((Arguments)argsPanel.createTestElement()).trim())) {
                            super.setSelectedIndex(index);
                        }
                        else {
                            // If there is data in the Raw panel, then the user should be 
                            // prevented from switching (that would be easy to track).
                            JOptionPane.showConfirmDialog(this,
                                    JMeterUtils.getResString("web_cannot_switch_tab"), // $NON-NLS-1$
                                    JMeterUtils.getResString("warning"), // $NON-NLS-1$
                                    JOptionPane.DEFAULT_OPTION, 
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    else {
                        // can switch from parameter to fileupload
                        if((oldSelectedIndex == TAB_PARAMETERS
                                && index == tabFileUploadIndex)
                             || (oldSelectedIndex == tabFileUploadIndex
                                     && index == TAB_PARAMETERS)) {
                            super.setSelectedIndex(index);
                            return;
                        }
                        
                        // If the Parameter data can be converted (i.e. no names) and there is no data in file upload
                        // we warn the user that the Parameter data will be lost.
                        if(oldSelectedIndex == TAB_PARAMETERS && !filePanelHasData && canConvertParameters()) {
                            Object[] options = {
                                    JMeterUtils.getResString("confirm"), // $NON-NLS-1$
                                    JMeterUtils.getResString("cancel")}; // $NON-NLS-1$
                            int n = JOptionPane.showOptionDialog(this,
                                JMeterUtils.getResString("web_parameters_lost_message"), // $NON-NLS-1$
                                JMeterUtils.getResString("warning"), // $NON-NLS-1$
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[1]);
                            if(n == JOptionPane.YES_OPTION) {
                                convertParametersToRaw();
                                super.setSelectedIndex(index);
                            }
                            else{
                                return;
                            }
                        }
                        else {
                            // If the Parameter data cannot be converted to Raw, then the user should be
                            // prevented from doing so raise an error dialog
                            String messageKey = filePanelHasData?"web_cannot_switch_tab":"web_cannot_convert_parameters_to_raw";
                            JOptionPane.showConfirmDialog(this,
                                    JMeterUtils.getResString(messageKey), // $NON-NLS-1$
                                    JMeterUtils.getResString("warning"), // $NON-NLS-1$
                                    JOptionPane.DEFAULT_OPTION, 
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
        }   
    }
    
    /**
     * Checks if no data is available in the selected tab
     *
     * @param oldSelectedIndex the tab to check for data
     * @return true if neither Parameters tab nor Raw Body tab contain data
     */
    boolean noData(int oldSelectedIndex) {
        if(oldSelectedIndex == tabRawBodyIndex) {
            return StringUtils.isEmpty(postBodyContent.getText().trim());
        }
        else {
            boolean noData = true;
            Arguments element = (Arguments) argsPanel.createTestElement();
            noData &= !filesPanel.hasData();

            
            return noData && StringUtils.isEmpty(computePostBody(element));
        }
    }


    public void setRequestId(String reqId) {
        requestId.setText(reqId);
    }

    public String getRequestId() {
        return requestId.getText();
    }

    public void setContentEncoding(String contentEnc) {
        contentEncoding.setText(contentEnc);
    }

    public String getContentEncoding() {
        return contentEncoding.getText();
    }

    public void setContextPath(String contextPath) {
       path.setText(contextPath);
    }

    public String getContextPath() {
        return path.getText();
    }

    public void setProtocol(String p) {
        protocol.setText(p);
    }

    public String getProtocol() {
        return protocol.getText();
    }
    
    public void setMethod(String m) {
        method.setSelectedItem(m);
    }

    public String getMethod() {
        return (String) method.getSelectedItem();
    }

    public void setResponseTimeout(String responseT) {
        responseTimeout.setText(responseT);
    }

    public String getResponseTimeout() {
        return responseTimeout.getText();
    }

    public void setDomain(String serverAddress) {
        domain.setText(serverAddress);
    }

    public String getDomain() {
        return domain.getText();
    }

    public void setPort(String serverPort) {
        port.setText(serverPort);
    }

    public String getPort() {
        return port.getText();
    }
    
    public void setAutoRedirect(Boolean autoR) {
        autoRedirects.setSelected(autoR);
    }

    public Boolean isAutoRedirect() {
        return autoRedirects.isSelected();
    }

    public void setFollowRedirects(Boolean followR) {
        followRedirects.setSelected(followR);
    }

    public Boolean isFollowRedirects() {
        return followRedirects.isSelected();
    }
    
    public void setSyncRequest(Boolean syncReq) {
        syncRequest.setSelected(syncReq);
    }

    public Boolean isSyncRequest() {
        return syncRequest.isSelected();
    }


    
    /**
     * Compute body data from arguments
     * @param arguments {@link Arguments}
     * @return {@link String}
     */
    private static String computePostBody(Arguments arguments) {
        return computePostBody(arguments, false);
    }

    /**
     * Compute body data from arguments
     * @param arguments {@link Arguments}
     * @param crlfToLF whether to convert CRLF to LF
     * @return {@link String}
     */
    private static String computePostBody(Arguments arguments, boolean crlfToLF) {
        StringBuilder postBody = new StringBuilder();
        for (JMeterProperty argument : arguments) {
            HTTPArgument arg = (HTTPArgument) argument.getObjectValue();
            String value = arg.getValue();
            if (crlfToLF) {
                value = value.replaceAll("\r\n", "\n"); // See modifyTestElement
            }
            postBody.append(value);
        }
        return postBody.toString();
    }
    
    /**
     * 
     * @return true if no argument has a name
     */
    boolean canConvertParameters() {
        Arguments arguments = (Arguments) argsPanel.createTestElement();
        for (int i = 0; i < arguments.getArgumentCount(); i++) {
            if(!StringUtils.isEmpty(arguments.getArgument(i).getName())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Convert Parameters to Raw Body
     */
    void convertParametersToRaw() {
        postBodyContent.setInitialText(computePostBody((Arguments)argsPanel.createTestElement()));
        postBodyContent.setCaretPosition(0);
    }

	public void clear() {
		domain.setText("");
		port.setText(""); // $NON-NLS-1$
		responseTimeout.setText(""); // $NON-NLS-1$
		
		http2Implementation.setSelectedItem(""); // $NON-NLS-1$
		protocol.setText(""); // $NON-NLS-1$
		http2Implementation.setSelectedItem(HTTP2Request.DEFAULT_METHOD); // $NON-NLS-1$
		contentEncoding.setText(""); // $NON-NLS-1$
		requestId.setText("");
	    
		path.setText(""); // $NON-NLS-1$
		
		autoRedirects.setSelected(false);
		followRedirects.setSelected(false);
		syncRequest.setSelected(false);
	    
        argsPanel.clear();
        postBodyContent.setInitialText("");
        filesPanel.clear();

        postContentTabbedPane.setSelectedIndex(TAB_PARAMETERS, false);
		
	}

	public String getHttp2Implementacion() {
		return (String) http2Implementation.getSelectedItem();
	}

	public void setHTTP2Implementation(String http2Impl) {
		http2Implementation.setSelectedItem(http2Impl);		
	}


}
