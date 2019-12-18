package com.swinkels.emperio.objects;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.swinkels.emperio.providers.EmailDao;
import com.swinkels.emperio.providers.EmailDaoImpl;
import com.swinkels.emperio.providers.KlantEmailDao;
import com.swinkels.emperio.providers.KlantEmailDaoImpl;

public class Email {
	EmailDao emailDao = new EmailDaoImpl();
	KlantEmailDao klantEmailDao = new KlantEmailDaoImpl();

	private ArrayList<Klant> klanten = new ArrayList<Klant>();

	private int id;
	private int pageNumber;
	private Bedrijf bedrijf;
	private String onderwerp;
	private String inhoud;
	private int aantalKlanten;
	private Date verzendtijd;

	public Email(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public Email(int id, Date verzendtijd, String onderwerp, String inhoud, int aantalKlanten) {
		this.id = id;
		this.verzendtijd = verzendtijd;
		this.onderwerp = onderwerp;
		this.inhoud = inhoud;
		this.aantalKlanten = aantalKlanten;
	}
	

	public Date getVerzendtijd() {
		return verzendtijd;
	}

	public void setVerzendtijd(Date verzendtijd) {
		this.verzendtijd = verzendtijd;
	}

	public int getAantalKlanten() {
		return aantalKlanten;
	}

	public void setAantalKlanten(int aantalKlanten) {
		this.aantalKlanten = aantalKlanten;
	}

	public void setKlantEmailDao(KlantEmailDao klantEmailDao) {
		this.klantEmailDao = klantEmailDao;
	}

	public EmailDao getEmailDao() {
		return emailDao;
	}

	public void send() {
		emailDao.save(this);
		for (Klant klant : klanten) {
			klantEmailDao.save(this, klant);
		}
	}

	public void getEmail() {
		emailDao.getEmail(this);
	}


	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	public KlantEmailDao getKlantEmailDao() {
		return klantEmailDao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Email(String onderwerp, String inhoud, ArrayList<Klant> klanten) {
		this.klanten = klanten;
		this.onderwerp = onderwerp;
		this.inhoud = inhoud;
	}

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

	public String getInhoud() {
		return inhoud;
	}

	public void setInhoud(String inhoud) {
		this.inhoud = inhoud;
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
