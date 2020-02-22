package com.swinkels.emperio.objects.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.providers.Email.EmailDao;
import com.swinkels.emperio.providers.Email.EmailDaoImpl;
import com.swinkels.emperio.providers.Email.KlantEmailDao;
import com.swinkels.emperio.providers.Email.KlantEmailDaoImpl;

public class Email {
	private EmailDao emailDao = new EmailDaoImpl();
	private KlantEmailDao klantEmailDao = new KlantEmailDaoImpl();

	private ArrayList<Klant> klanten = new ArrayList<Klant>();

	private int id;
	private String onderwerp;
	private String inhoud;



	public Email(int id, String onderwerp, String inhoud) {
		this.id = id;
		this.onderwerp = onderwerp;
		this.inhoud = inhoud;
	}



	public void send(List<Klant> klanten) {
		Session session = getSession();
		
		emailDao.save(this);		
		emailDao.getEmail(this);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Security.getKey()));
			
			message.setSubject(onderwerp);
			message.setText(inhoud);
			
			for(Klant klant : klanten) {
				String to = klant.getEmail();
				if(to != null) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					Transport.send(message);
					System.out.println("message sent successfully");
				} else {
					System.out.println("heeft geen email");
				}
				klantEmailDao.save(this, klant);
			}
		} catch (AddressException e) {
			System.out.println(e.getMessage());
		} catch (SendFailedException e) {
			System.out.println("Send failed.<br>" + e.getMessage());
		} catch (MessagingException e) {
			System.out.println("Unexpected error.<br>" + e.getMessage());
		}
	}
	
	private Session getSession() {
		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp-relay.sendinblue.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("winkpascal@gmail.com", "AJD1RqnaTOGzb06B");
			}
		});
		return session;
	}

	public HashMap<String, String> toDto(){
		HashMap<String, String> dto = new HashMap<>();
		dto.put("id", Integer.toString(id));
		dto.put("onderwerp",  onderwerp);
		dto.put("inhoud", inhoud);
		return dto;
	}

//	public send() {
//	    MailjetClient client;
//	    MailjetRequest request;
//	    MailjetResponse response;
//	    client = new MailjetClient(System.getenv("3a201d959b5710469110d035687b83f5"), System.getenv("2a3fdb27e24dca3cc3f02e0f95d24bf9"), new ClientOptions("v3.1"));
//	    request = new MailjetRequest(Emailv31.resource)
//	    .property(Emailv31.MESSAGES, new JSONArray()
//	    .put(new JSONObject()
//	    .put(Emailv31.Message.FROM, new JSONObject()
//	    .put("Email", "wink.pascal@gmail.com")
//	    .put("Name", "Pascal"))
//	    .put(Emailv31.Message.TO, new JSONArray()
//	    .put(new JSONObject()
//	    .put("Email", "wink.pascal@gmail.com")
//	    .put("Name", "Pascal")))
//	    .put(Emailv31.Message.SUBJECT, "Greetings from Mailjet.")
//	    .put(Emailv31.Message.TEXTPART, "My first Mailjet email")
//	    .put(Emailv31.Message.HTMLPART, "<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!")
//	    .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
//	    response = client.post(request);
//	    System.out.println(response.getStatus());
//	    System.out.println(response.getData());
//	}
}
