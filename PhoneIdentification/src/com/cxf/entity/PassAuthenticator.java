package com.cxf.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;



public class PassAuthenticator extends Authenticator  
{  
    public PasswordAuthentication getPasswordAuthentication()  
    {  
        String username = "public_feedback@163.com";  
        String pwd = "123456789#k";  
          
        return new PasswordAuthentication(username, pwd);  
    }  
}  
