package com.legacy.egp.ejb;

import org.apache.log4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Legacy Audit Message-Driven Bean
 * Processes audit messages from JMS queue
 * 
 * @author EGP Development Team
 * @version 1.0
 * @since 2008
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/EgpAuditQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
    }
)
public class AuditMDB implements MessageListener {
    
    private static final Logger logger = Logger.getLogger(AuditMDB.class);
    private static final Logger auditLogger = Logger.getLogger("com.legacy.egp.audit");
    
    @PersistenceContext(unitName = "egp-pu")
    private EntityManager entityManager;
    
    @Override
    public void onMessage(Message message) {
        logger.debug("Received audit message");
        
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String auditData = textMessage.getText();
                
                // Legacy: Simple audit processing
                processAuditMessage(auditData);
                
                auditLogger.info("Audit message processed: " + auditData);
                
            } else {
                logger.warn("Received non-text message: " + message.getClass().getName());
            }
            
        } catch (JMSException e) {
            logger.error("Error processing JMS message", e);
            // Legacy: No retry mechanism, just log and continue
            
        } catch (Exception e) {
            logger.error("Error processing audit message", e);
            // Legacy: Swallow exceptions to prevent message redelivery
        }
    }
    
    private void processAuditMessage(String auditData) {
        // Legacy: Simple audit processing
        // In a real system, this would parse the audit data and store it
        
        logger.debug("Processing audit data: " + auditData);
        
        try {
            // Legacy: Direct SQL insert for audit log
            entityManager.createNativeQuery(
                "INSERT INTO egp_audit_log (table_name, record_id, action, new_values, timestamp) " +
                "VALUES ('SYSTEM', 0, 'AUDIT', ?, CURRENT_TIMESTAMP)"
            ).setParameter(1, auditData).executeUpdate();
            
        } catch (Exception e) {
            logger.error("Failed to insert audit record", e);
        }
    }
}