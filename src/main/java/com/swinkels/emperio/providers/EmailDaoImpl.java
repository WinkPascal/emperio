package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Email;

public class EmailDaoImpl extends MariadbBaseDao implements EmailDao{


	@Override
	public void save(Email email) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"insert into email (verzendtijd, BedrijfBedrijfsnaam, onderwerp, inhoud) \n"
					+ "values(sysdate(),?, \n"
					+ "?, \n"
					+ "?)");
			pstmt.setString(1, email.getBedrijf().getBedrijfsNaam());
			pstmt.setString(2,email.getOnderwerp());
			pstmt.setString(3,email.getInhoud());
			System.out.println(pstmt);

			pstmt.executeUpdate();
			
			getIdFromEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getIdFromEmail(Email email) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id \n"
					+ "from email \n "
					+ "where BedrijfBedrijfsnaam = '"+email.getBedrijf().getBedrijfsNaam()+"' \n"
					+ "ORDER BY verzendtijd desc \n"
					+ "LIMIT 1;");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				email.setId(dbResultSet.getInt("id"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getEmail(Email email) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select e.id, e.verzendtijd, e.inhoud, e.onderwerp, e.inhoud, count(k.id) as aantalKlanten \n"
					+ "from email e join klantEmail k on e.id = k.emailId \n "
					+ "where e.BedrijfBedrijfsnaam = '"+email.getBedrijf().getBedrijfsNaam()+"' "
					+ "AND e.id = "+email.getId());
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				email.setId(dbResultSet.getInt("id"));
				email.setVerzendtijd(dbResultSet.getDate("verzendtijd"));
				email.setOnderwerp(dbResultSet.getString("onderwerp"));
				email.setInhoud(dbResultSet.getString("inhoud"));
				email.setAantalKlanten(dbResultSet.getInt("aantalKlanten"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public ArrayList<Email> getEmails(Bedrijf bedrijf, int hoeveelheid) {
	int low =hoeveelheid - 10;
	 ArrayList<Email> emails = new ArrayList<Email>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select e.id, e.verzendtijd,  e.onderwerp, e.inhoud, count(k.id) as aantalKlanten \n"
					+ "from email e left join klantEmail k on e.id = k.emailId \n "
					+ "where e.BedrijfBedrijfsnaam = '"+bedrijf.getBedrijfsNaam()+"' \n" 
					+ "group by e.id \n"
					+ "ORDER BY e.verzendtijd desc \n"
					+ "limit "+low+", "+ hoeveelheid);
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int id = dbResultSet.getInt("id");
				Date verzendtijd = dbResultSet.getDate("verzendtijd");
				String onderwerp = dbResultSet.getString("onderwerp");
				String inhoud = dbResultSet.getString("inhoud");
				int aantalklanten = dbResultSet.getInt("aantalKlanten");
				Email email = new Email(id,verzendtijd,onderwerp,inhoud,aantalklanten);
				emails.add(email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		return emails;
	}
}
