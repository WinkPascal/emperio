package com.swinkels.emperio.objects;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

public class Email {
	
	private int pageNumber;
	private ArrayList<Klant> klanten = new ArrayList<Klant>();
	private Bedrijf bedrijf;
	
	private int aantalKlanten;
	
	private String onderwerp;
	private String tekst;

	
	
	public ArrayList<Klant> getKlanten() {
		return klanten;
	}

	public void setKlanten(ArrayList<Klant> klanten) {
		this.klanten = klanten;
	}

	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public String getOnderwerp() {
		return onderwerp;
	}

	public void setOnderwerp(String onderwerp) {
		this.onderwerp = onderwerp;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}
	
	  public static void main(String[] args) throws MailjetException, MailjetSocketTimeoutException {
		    MailjetClient client;
		    MailjetRequest request;
		    MailjetResponse response;
		    client = new MailjetClient(System.getenv("3a201d959b5710469110d035687b83f5"), System.getenv("2a3fdb27e24dca3cc3f02e0f95d24bf9"), new ClientOptions("v3.1"));
		    request = new MailjetRequest(Emailv31.resource)
		    .property(Emailv31.MESSAGES, new JSONArray()
		    .put(new JSONObject()
		    .put(Emailv31.Message.FROM, new JSONObject()
		    .put("Email", "wink.pascal@gmail.com")
		    .put("Name", "Pascal"))
		    .put(Emailv31.Message.TO, new JSONArray()
		    .put(new JSONObject()
		    .put("Email", "wink.pascal@gmail.com")
		    .put("Name", "Pascal")))
		    .put(Emailv31.Message.SUBJECT, "Greetings from Mailjet.")
		    .put(Emailv31.Message.TEXTPART, "My first Mailjet email")
		    .put(Emailv31.Message.HTMLPART, "<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!")
		    .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
		    response = client.post(request);
		    System.out.println(response.getStatus());
		    System.out.println(response.getData());
		  }

}
