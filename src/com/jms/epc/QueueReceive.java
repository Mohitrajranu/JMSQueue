package com.jms.epc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Properties;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.tibco.tibjms.TibjmsQueueConnectionFactory;
/**
* This example shows how to establish a connection to
* and receive messages from a JMS queue. The classes in this
* package operate on the same JMS queue. Run the classes together to
* witness messages being sent and received, and to browse the queue
* for messages.  This class is used to receive and remove messages
* from the queue.
* @author Copyright (c) 2017 Mohit Raj. All Rights Reserved.
*/
public class QueueReceive implements MessageListener
{
	static Properties props;
 // Defines the JNDI context factory.
	
 //public final static String JNDI_FACTORY="com.tibco.tibjms.naming.TibjmsInitialContextFactory";
 
 private TibjmsQueueConnectionFactory qconFactory;
 private QueueConnection qcon;
 private QueueSession qsession;
 private QueueReceiver qreceiver;
 private Queue queue;
 private boolean quit = false;
/**
 * Message listener interface.
 * @param msg  message
 */
 public void onMessage(Message msg)
 {
	    String msgText = null;
        FileWriter fWriter = null;
		BufferedWriter bWriter = null;
		String newname =null;
		String timeStamp = null;
		String dateFormat = null;
		String epcFileName = null;
		String epcFileDir = null;
		props = new Properties();
		Details1 dt = null;
		boolean fileWrflag = false;
		try {
			dt = new Details1();
			props.load(dt.getFTPfile());
			epcFileDir = props.getProperty("epcXMLFileDir"); 
			epcFileName = epcFileDir+"/"+props.getProperty("epcXMLFileName");
			dateFormat = props.getProperty("epcXMLTimestamp");
			DateFormat ds = new SimpleDateFormat(dateFormat);
	        timeStamp = ds.format(new java.util.Date());
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
   //  System.out.println("Message Received: "+ msgText );
     try{
    	 if (msg instanceof TextMessage) {
    		 msgText = ((TextMessage)msg).getText();
    		 
    		 //msg.getJMSReplyTo();
    	 } else {
    		 msgText = msg.toString();
    	 }
    	 
    	 newname = epcFileName + timeStamp + ".xml";
         fWriter = new FileWriter(newname, true);
         bWriter = new BufferedWriter(fWriter);
         bWriter.write(msgText);
         bWriter.newLine();
         fileWrflag = true;
  }  
     catch (JMSException jmse) {
	     System.err.println("An exception occurred: "+jmse.getMessage());
	    }
     
  catch(Exception e){
      e.printStackTrace();
  }
  finally{
  	try {
			if(bWriter != null){
				bWriter.close();
				bWriter = null;
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
  	try {
			if(fWriter != null){
				fWriter.close();
				fWriter = null;
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
     try{
    	 if(fileWrflag){
    		/*Sender snQueue = new Sender();
    		snQueue.senderQueue(msg);*/
    		 System.out.println("File writing Successfully completed");
    	 }
     }
     catch(Exception e){
         e.printStackTrace();
     }
     if (msgText.equalsIgnoreCase("quit")) {
       synchronized(this) {
         quit = true;
         this.notifyAll(); // Notify main thread to quit
       }
     }
    
 }
 /**
  * Creates all the necessary objects for receiving
  * messages from a JMS queue.
  *
  * @param   ctx    JNDI initial context
  * @param    queueName    name of queue
  * @exception NamingException if operation cannot be performed
  * @exception JMSException if JMS fails to initialize due to internal error
  */
 public void init(Context ctx, String queueName, String userName, String passWord, String jmsFactory)
    throws NamingException, JMSException
 {
    qconFactory = (TibjmsQueueConnectionFactory) ctx.lookup(jmsFactory);
    qcon = qconFactory.createQueueConnection(userName, passWord);
    qsession = qcon.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
    queue = (Queue) ctx.lookup(queueName);
    QueueReceive asyncReceiver = new QueueReceive();
    qreceiver = qsession.createReceiver(queue);
    qreceiver.setMessageListener(asyncReceiver);
    qcon.start();
 }
 /**
  * Closes JMS objects.
  * @exception JMSException if JMS fails to close objects due to internal error
  */
 public void close()throws JMSException
 {
    qreceiver.close();
    qsession.close();
    qcon.close();
 }
/**
 * main() method.
 *
 * @param args  TIBCO Server URL
 * @exception  Exception if execution fails
 */
 public static void main(String[] args) throws Exception {
	 props = new Properties();
	Details1 dt = new Details1();
	String serverURL = null;
	String userName = null;
	String passWord = null;
	String queueIN = null;
	String jmsFactory = null;
	String qConnFactory = null;
	 try {
		 props.load(dt.getFTPfile());
		 serverURL = props.getProperty("serverURL").trim();
		 userName =  props.getProperty("userName").trim();
		 passWord =  props.getProperty("passWord").trim();
		 queueIN = props.getProperty("queueIN").trim();
		 jmsFactory =  props.getProperty("jmsFactory").trim();
		 qConnFactory = props.getProperty("queueConnFactory").trim();
		InitialContext ic = getInitialContext(serverURL,userName,passWord,qConnFactory);
		QueueReceive qr = new QueueReceive();
		qr.init(ic, queueIN, userName, passWord, jmsFactory);
		
		System.out.println(
		    "JMS Ready To Receive Messages (To quit, send a \"quit\" message).");
		// Wait until a "quit" message has been received.
		synchronized(qr) {
		 while (! qr.quit) {
		   try {
		     qr.wait();
		   } catch (InterruptedException ie) {
			   
		   }
		 }
		}
		qr.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 private static InitialContext getInitialContext(String url ,String userName, String passWord, String qconnFactory)
    throws NamingException
 {
    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY, qconnFactory);
    env.put(Context.PROVIDER_URL, url);
    env.put(Context.SECURITY_PRINCIPAL, userName);
    env.put(Context.SECURITY_CREDENTIALS, passWord);
    return new InitialContext(env);
 }
}