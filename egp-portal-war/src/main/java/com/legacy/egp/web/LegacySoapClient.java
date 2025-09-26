package com.legacy.egp.web;

import org.apache.log4j.Logger;

import javax.xml.soap.*;
import java.net.URL;

/**
 * Legacy SOAP Web Service Client
 * Demonstrates typical SOAP client code from mid-2000s
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
public class LegacySoapClient {
    
    private static final Logger logger = Logger.getLogger(LegacySoapClient.class);
    
    private static final String SOAP_ENDPOINT = "http://legacy-services.internal.corp:8080/LegacyService";
    private static final String SOAP_NAMESPACE = "http://legacy.egp.com/services";
    
    /**
     * Legacy: Get customer information via SOAP
     */
    public String getCustomerInfo(String customerNumber) {
        try {
            logger.info("Calling legacy SOAP service for customer: " + customerNumber);
            
            // Legacy: Create SOAP connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            // Legacy: Create SOAP message
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("leg", SOAP_NAMESPACE);
            
            // Legacy: Create SOAP body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement getCustomerElement = soapBody.addChildElement("getCustomerRequest", "leg");
            SOAPElement customerNumberElement = getCustomerElement.addChildElement("customerNumber", "leg");
            customerNumberElement.addTextNode(customerNumber);
            
            // Legacy: Set SOAP headers
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", SOAP_NAMESPACE + "/getCustomer");
            
            soapMessage.saveChanges();
            
            // Legacy: Call SOAP service
            URL endpoint = new URL(SOAP_ENDPOINT);
            SOAPMessage soapResponse = soapConnection.call(soapMessage, endpoint);
            
            // Legacy: Process response (simplified)
            logger.info("SOAP service call completed for customer: " + customerNumber);
            
            soapConnection.close();
            
            return "Customer data retrieved successfully"; // Simplified response
            
        } catch (Exception e) {
            logger.error("Error calling legacy SOAP service for customer: " + customerNumber, e);
            return "Error retrieving customer data: " + e.getMessage();
        }
    }
    
    /**
     * Legacy: Create case via SOAP
     */
    public String createCase(String customerNumber, String caseType, String subject, String description) {
        try {
            logger.info("Creating case via SOAP for customer: " + customerNumber);
            
            // Legacy: Similar SOAP call structure as above
            // For brevity, returning a mock response
            
            String caseNumber = "CASE-" + System.currentTimeMillis();
            logger.info("Case created via SOAP: " + caseNumber);
            
            return caseNumber;
            
        } catch (Exception e) {
            logger.error("Error creating case via SOAP for customer: " + customerNumber, e);
            return null;
        }
    }
}