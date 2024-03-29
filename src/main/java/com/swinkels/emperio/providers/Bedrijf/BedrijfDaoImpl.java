package com.swinkels.emperio.providers.Bedrijf;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.providers.Bedrijf.BedrijfDao;
import com.swinkels.emperio.providers.MariadbBaseDao;

public class BedrijfDaoImpl extends MariadbBaseDao implements BedrijfDao {
	
	public boolean save(Bedrijf bedrijf) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO bedrijf VALUES('"
				  + bedrijf.getBedrijfsNaam()+"', now(), "	
				  + "'user', '"
				  +	bedrijf.getWachtwoord()+"', '"
				  + bedrijf.getEmail()+"', '"
				  + bedrijf.getTelefoon() + "', '"
				  + bedrijf.getAdres()+"', '"
				  + bedrijf.getWoonplaats()+"', '"
				  +bedrijf.getPostcode()+"')");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}
//	
//	public ArrayList<Bedrijf> getBedrijven(int page){
//		ArrayList<Bedrijf> bedrijven = new ArrayList<Bedrijf>();
//		int top = page * 10;
//		int low = top - 10;
//		try (Connection con = super.getConnection()) {
//			PreparedStatement pstmt = con.prepareStatement(
//					"SELECT email, telefoon, adres, naam \n" + 
//					"FROM bedrijf \n" + 
//					"WHERE role = \"user\" \n" + 
//					"group by email LIMIT "+low+", "+top+"");
//			ResultSet dbResultSet = pstmt.executeQuery();
//			while (dbResultSet.next()) {
//				
//				String email = dbResultSet.getString("email");
//				String telefoon = dbResultSet.getString("telefoon");
//				String adres = dbResultSet.getString("adres");
//				String naam = dbResultSet.getString("naam");
//				bed
//				bedrijven.add(bedrijf);
//			}
//		} catch (SQLException e) {
//			System.out.println(e);
//			return null;
//		}
//		
//		return bedrijven;
//	}
	
//	//wordt gebruikt bij: getWerkdagen
//	public ArrayList<Dag> getWeekRooster(Bedrijf bedrijf){
//		ArrayList<Dag> dagen = new ArrayList<Dag>();
//		try (Connection con = super.getConnection()) {
//			PreparedStatement pstmt = con.prepareStatement(
//					"SELECT dag, openingstijd, sluitingstijd"
//				 + " FROM dag "
//				 + "WHERE BedrijfBedrijfsnaam ='"+bedrijf.getBedrijfsNaam()+"' "
//				 + "ORDER BY dag");
//			ResultSet dbResultSet = pstmt.executeQuery();
//			while (dbResultSet.next()) {
//				String openingsTijd = dbResultSet.getString("openingstijd");
//				String sluitingsTijd = dbResultSet.getString("sluitingstijd");
//				int dagNummer = dbResultSet.getInt("dag");
//				
//				//format de tijden
//				Date openingsTijdDate = ServiceFilter.StringToDateFormatter(openingsTijd, "HH:mm");
//				Date sluitingsTijdDate = ServiceFilter.StringToDateFormatter(sluitingsTijd, "HH:mm");
//				
//				Dag  dag = new Dag(dagNummer, openingsTijdDate, sluitingsTijdDate);
//				
//				dagen.add(dag);
//			}
//		} catch (SQLException e) {
//			System.out.println(e);
//			return null;
//		}
//		
//		return dagen;
//	}
	
	// wordt gebruikt bij
		//getProductenByPage();




	public boolean setInvoerKlant(Bedrijf bedrijf, String contact, boolean telefoon, boolean email, boolean adres) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"UPDATE bedrijf"+
					" SET invoerveldEmail = "+email+", \n" + 
					"invoerveldTelefoon = "+telefoon+", \n" + 
					"invoerveldAdres = "+adres+",\n" + 
					"verplichtContactVeld = '"+contact+"'\n" + 
					"WHERE email='"+ bedrijf.getEmail()+"';");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}	
	
	public void getInfo(Bedrijf bedrijf) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT email, telefoon, adres, woonplaats, postcode \n "
				  + "from bedrijf \n"
				  + "WHERE Bedrijfsnaam = '"+bedrijf.getBedrijfsNaam()+"';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				bedrijf.setTelefoon(dbResultSet.getString("telefoon"));
				bedrijf.setEmail(dbResultSet.getString("email"));
				bedrijf.setAdres(dbResultSet.getString("adres"));
				bedrijf.setWoonplaats(dbResultSet.getString("woonplaats"));
				bedrijf.setPostcode(dbResultSet.getString("postcode"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}		
	}
	
	public void getKlantPaginaSettings(Bedrijf bedrijf) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT b.email, b.telefoon, b.adres, b.woonplaats, b.postcode, i.* "
				  + "from bedrijf b join instellingen i on b.Bedrijfsnaam = i.BedrijfBedrijfsnaam "
				  + "WHERE b.Bedrijfsnaam = '"+bedrijf.getBedrijfsNaam()+"';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				bedrijf.getInstellingen().setKleurKlasse1(dbResultSet.getString("kleurKlasse"));
				bedrijf.getInstellingen().setMaximumPrijsVanKlasse1(dbResultSet.getDouble("maximumprijs1"));
				bedrijf.getInstellingen().setKleurKlasse2(dbResultSet.getString("kleurKlasse2"));
				bedrijf.getInstellingen().setMaximumPrijsVanKlasse2(dbResultSet.getDouble("maximumprijs2"));
				bedrijf.getInstellingen().setKleurKlasse3(dbResultSet.getString("kleurKlasse3"));
				
				bedrijf.getInstellingen().setEmailKlantInvoer(dbResultSet.getBoolean("emailKlantInvoer"));
				bedrijf.getInstellingen().setTelefoonKlantInvoer(dbResultSet.getBoolean("telefoonKlantInover"));
				bedrijf.getInstellingen().setAdresKlantInvoer(dbResultSet.getBoolean("AdresKlantInvoer"));

				bedrijf.getInstellingen().setBedrijfsAdresString(dbResultSet.getString("bedrijfsEmail"));
				bedrijf.getInstellingen().setBedrijfsTelefoonString(dbResultSet.getString("bedrijfsTelefoon"));
				bedrijf.getInstellingen().setBedrijfsAdresString(dbResultSet.getString("bedrijfsAdres"));
				
				bedrijf.setTelefoon(dbResultSet.getString("telefoon"));
				bedrijf.setEmail(dbResultSet.getString("email"));
				bedrijf.setAdres(dbResultSet.getString("adres"));
				bedrijf.setWoonplaats(dbResultSet.getString("woonplaats"));
				bedrijf.setPostcode(dbResultSet.getString("postcode"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}		
	}

	public boolean needsSetup(String username) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "SELECT count(dagnummer) as dagen "
					+ "FROM dag "
					+ "WHERE BedrijfBedrijfsnaam = '"+username+"';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				if(dbResultSet.getInt("dagen") < 7) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}			
		return false;
	}

}
