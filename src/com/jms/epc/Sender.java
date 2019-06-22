package com.jms.epc;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.QueueSender;
import javax.jms.DeliveryMode;
import javax.jms.QueueSession;
import javax.jms.QueueConnection;
import com.tibco.tibjms.TibjmsQueueConnectionFactory;
public class Sender {
	static Properties props;
	public final static String JMSQueue_FACTORY="com.tibco.tibjms.naming.TibjmsInitialContextFactory";
	public void senderQueue (Message msg) throws Exception {
		String userNameJms = null;
		String passwdJms = null;
		String jmsUrl = null;
		String queueOUT = null;
		String queueConnFactory= null;
		Details1 dtJms = null;
		try {
			 new Details1();
			 dtJms = new Details1();
			 props.load(dtJms.getFTPfile());
			 jmsUrl = props.getProperty("serverURL").trim();
			 userNameJms =  props.getProperty("userName").trim();
			 passwdJms =  props.getProperty("passWord").trim();
			 queueOUT = props.getProperty("queueOUT").trim();
			 queueConnFactory =  props.getProperty("jmsFactory").trim();
			 
			 Hashtable env = new Hashtable();
			 env.put(Context.INITIAL_CONTEXT_FACTORY,JMSQueue_FACTORY);
			 env.put(Context.PROVIDER_URL, jmsUrl);
			 env.put(Context.SECURITY_PRINCIPAL, userNameJms);
			 env.put(Context.SECURITY_CREDENTIALS, passwdJms);
			// get the initial context
			InitialContext ctx = new InitialContext(env);

			// lookup the queue object
			Queue queue = (Queue) ctx.lookup(queueOUT);
			// lookup the queue connection factory
			TibjmsQueueConnectionFactory connFactory = (TibjmsQueueConnectionFactory) ctx.lookup(queueConnFactory);
			// create a queue connection
			QueueConnection queueConn = connFactory.createQueueConnection();

			// create a queue session
			QueueSession queueSession = queueConn.createQueueSession(false,Session.DUPS_OK_ACKNOWLEDGE);

			// create a queue sender
			QueueSender queueSender = queueSession.createSender(queue);
			queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// create a simple message to say "Hello"
			TextMessage message = queueSession.createTextMessage();
			message.setText("Hello");
			/*message.setJMSCorrelationID(msg.getJMSCorrelationID());
			message.setJMSReplyTo(msg.getJMSReplyTo());
            */
			// send the message
			queueSender.send(message);

			System.out.println("sent: " + message.getText());

			queueConn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}