package com.filter.ws;

import java.util.Set;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.DOMException;

@SuppressWarnings("restriction")
public class AuthenticationSoapHandler implements SOAPHandler<SOAPMessageContext>  {

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext context) {
		
	}

	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}

	public boolean handleMessage(SOAPMessageContext context) {
		HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST); 
		HttpSession session = request.getSession();
		System.out.println("session="+session.getId());
		
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		String username = "";
        String password = "";
        if (!isRequest) {
        	SOAPMessage soapMsg = context.getMessage();
        	try {
        		username = soapMsg.getSOAPHeader().getElementsByTagName("username").item(0).getTextContent();
        		password = soapMsg.getSOAPHeader().getElementsByTagName("password").item(0).getTextContent();
        	} catch (DOMException e) {
        		e.printStackTrace();
        	} catch (SOAPException e) {
        		e.printStackTrace();
        	}
        	if (username.equals("admin") && password.equals("admin")) {
        		return true;
        	}
        	else
        		return false;
        }
        return false;
	}

}
