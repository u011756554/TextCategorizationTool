/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.activation.DataHandler;

/**
 *
 * @author rafael
 */
public class Email implements Serializable{
    
    StringBuffer content;
    
    String eMailTo;
    
    public Email(){
        content = new StringBuffer();
        eMailTo = "";
    }

    public String geteMailTo() {
        return eMailTo;
    }

    public void seteMailTo(String eMailTo) {
        this.eMailTo = eMailTo;
    }
    
    public void send(){
        StringBuffer contentHeader = new StringBuffer();
        
        InetAddress resolvedor;
        try {
            resolvedor = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
            String canonicalHostName = resolvedor.getCanonicalHostName();
            contentHeader.append("Host Name: " + canonicalHostName + "\n");
            contentHeader.append("Network IPs: \n");
            
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements();)
            {
                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();
                while(a.hasMoreElements()){
                    InetAddress address = a.nextElement();
                    contentHeader.append("- " + address.getHostAddress() + "\n");
                }
                
            }
            contentHeader.append("Configuration Details: \n");
        } catch (Exception ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(eMailTo.equals("")){
            return;
        }
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                         protected PasswordAuthentication getPasswordAuthentication() 
                         {
                               return new PasswordAuthentication("experiment.manager.tct@gmail.com", "aq!sw@de#");
                         }
                    });
        /** Ativa Debug para sessão */
        session.setDebug(true);
        try {

              Message message = new MimeMessage(session);
              message.setFrom(new InternetAddress("experiment.manager.tct@gmail.com")); //Remetente



              Address[] toUser = InternetAddress.parse(eMailTo);  
              message.setRecipients(Message.RecipientType.TO, toUser);
              message.setSubject("Experiments have been completed");//Assunto
              message.setText(contentHeader.toString() + content.toString());
              /**Método para enviar a mensagem criada*/
              Transport.send(message);
         } catch (MessagingException e) {
              throw new RuntimeException(e);
        }
        
    }

    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }
    
    public static void emailInterface(ConfigurationBase configuration){
        int retorno = JOptionPane.showConfirmDialog(null, "Before saving the configuration, do you want to receive an e-mail when the experimente are completede?", "TCT", JOptionPane.YES_OPTION);
        if(retorno == JOptionPane.YES_OPTION){
            String email = JOptionPane.showInputDialog("E-mail");
            if(email != null & email.length() > 00){
                configuration.setEmailAdress(email);
            }else{
                configuration.setEmailAdress("");
            }
        }
    }
    
}
