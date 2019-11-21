package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.service.ServiceFilter;

public class AfspraakDaoImpl extends MariadbBaseDao implements AfspraakDao {
	//wordt gebruikt bij: afsprakenByDate
	public ArrayList<Afspraak> getAfsprakenBetweenDates(Date beginDate, Date eindDate, Bedrijf bedrijf) {
		ArrayList<Afspraak> afsprakenVandaag = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select a.id, a.timestamp, k.naam as klantnaam, \n" + 
					"       b.naam as behandelingnaam, b.lengte, b.prijs\n" + 
					"from klant k join afspraak a on a.klant = k.id \n" + 
					"             join afspraakbehandeling ab on ab.afspraak = a.id \n" + 
					"             join bedrijf m on k.bedrijf = m.email \n" + 
					"             join behandeling b on b.id = ab.behandeling \n" + 
					"where m.email = '"+bedrijf.getEmail()+"' AND \n" + 
					"timestamp BETWEEN '"+ServiceFilter.DateToStringFormatter(beginDate, "yyyy-MM-dd")+"' AND "
							        + "'"+ServiceFilter.DateToStringFormatter(eindDate, "yyyy-MM-dd")+"' "+
					"ORDER BY timestamp");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//Behandeling
				String behandelingnaam = dbResultSet.getString("behandelingnaam");
				Date lengte = ServiceFilter.StringToDateFormatter(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");
				
				Behandeling behandeling = new Behandeling(behandelingnaam, lengte, prijs);

				//Afspraak
				int id = dbResultSet.getInt("id");
				if(afsprakenVandaag.size() == 0){
					//Dit is de eerste afspraak die wordt aangemaakt
					String timestampString = dbResultSet.getString("timestamp");
					timestampString = timestampString.substring(0, timestampString.length()-5);
					Date timestampDate =  ServiceFilter.StringToDateFormatter(timestampString, "yyyy-MM-dd HH:mm");
					
					//klant
					String klantNaam = dbResultSet.getString("klantNaam");
					Klant klant = new Klant(klantNaam);
					
					
					Afspraak newAfspraak = new Afspraak(id, timestampDate, klant);
					newAfspraak.addBehandeling(behandeling);

					afsprakenVandaag.add(newAfspraak);
				} else {
					boolean nietAlleenBehandeling = true;
					for(Afspraak afspraak : afsprakenVandaag) {
						//check of de afspraak al is opghehaalt
						if(afspraak.getId() == id) {
							//alleen de behandeling wordt toegevoegd
							nietAlleenBehandeling = false;
							afspraak.addBehandeling(behandeling);
						}
					}
					if(nietAlleenBehandeling) {
						String timestampString = dbResultSet.getString("timestamp");
						timestampString = timestampString.substring(0, timestampString.length()-5);
						Date timestamp = ServiceFilter.StringToDateFormatter(timestampString,"yyyy-MM-dd HH:mm");
						//klant
						String klantNaam = dbResultSet.getString("klantNaam");
						Klant klant = new Klant(klantNaam);
					
						Afspraak newAfspraak = new Afspraak(id, timestamp, klant);
						newAfspraak.addBehandeling(behandeling);

						afsprakenVandaag.add(newAfspraak);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return afsprakenVandaag;
	}

	public boolean setAfspraak(Afspraak afspraak) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(afspraak.getTimeStamp());
		int dagNummer = calendar.get(Calendar.DAY_OF_WEEK)-1;
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
			  "insert into afspraak(dag_nr, klant, timestamp) values(" 
			+ ""+dagNummer+", "
			+ "'"+afspraak.getKlant().getId()+"', "
			+ "'"+ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"')");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Afspraak getAfspraakId(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from afspraak "
					+ "where klant = '"+afspraak.getKlant().getId()+"' "
					+ "and timestamp = '"+ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//afspraak
				int id = dbResultSet.getInt("id");
				
				afspraak.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return afspraak;
	}

	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date beginDate){
		ArrayList<Afspraak> afspraken = new ArrayList<Afspraak>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);            
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		Date eindDate = calendar.getTime();
		
		String beginDateString = ServiceFilter.DateToStringFormatter(beginDate, "yyyy-MM-dd");
		String eindDateString = ServiceFilter.DateToStringFormatter(eindDate, "yyyy-MM-dd");
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement("SELECT * " + 
					"FROM afspraak " + 
					"WHERE timestamp " + 
					"BETWEEN '"+beginDateString+"' AND " + 
					"'"+eindDateString+"' " + 
					"order by timestamp ");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//afspraak
		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return afspraken;
	}

	public Afspraak getAfspraak(Bedrijf bedrijf, int afspraakId) {
		try (Connection con = super.getConnection()) {
			Afspraak afspraak = new Afspraak();

			PreparedStatement pstmt = con.prepareStatement( 
					"select a.id, a.timestamp, k.naam as klantnaam, \n" + 
					"       b.naam as behandelingnaam, b.lengte, b.prijs, \n" +
					"		k.email as email, k.geslacht as geslacht, k.telefoon as telefoon \n" +
					"from klant k join afspraak a on a.klant = k.id \n" + 
					"             join afspraakbehandeling ab on ab.afspraak = a.id \n" + 
					"             join bedrijf m on k.bedrijf = m.email \n" + 
					"             join behandeling b on b.id = ab.behandeling \n" + 
					"where m.email = '"+bedrijf.getEmail()+"' "+ 
					"and a.id = "+afspraakId);
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//Behandeling
				String behandelingnaam = dbResultSet.getString("behandelingnaam");
				Date lengte = ServiceFilter.StringToDateFormatter(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");
				
				Behandeling behandeling = new Behandeling(behandelingnaam, lengte, prijs);

				//Afspraak
				int id = dbResultSet.getInt("id");
				int i = 1;
				if(i == 1) {
					// de klant en afspraak met de eerste behandeling wordt aangemaakt
					String timestampString = dbResultSet.getString("timestamp");
					timestampString = timestampString.substring(0, timestampString.length()-5);
					Date timestamp = ServiceFilter.StringToDateFormatter(timestampString,"yyyy-MM-dd HH:mm");
					//klant
					String klantNaam = dbResultSet.getString("klantNaam");
					String klantEmail = dbResultSet.getString("email");
					if(klantEmail == null) {
						klantEmail = "-";
					}
					String klantTelefoon = dbResultSet.getString("telefoon");
					if(klantTelefoon == null) {
						klantTelefoon = "-";
					}
					String klantGeslacht = dbResultSet.getString("geslacht");
					
					Klant klant = new Klant(klantNaam, klantEmail, klantTelefoon, klantGeslacht);
				
					afspraak = new Afspraak(id, timestamp, klant);
					afspraak.addBehandeling(behandeling);
					i++;
				} else {
					afspraak.addBehandeling(behandeling);
				}
			}
			return afspraak;
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public ArrayList<Double> getInkomsten(Bedrijf bedrijf, Date date){
		try (Connection con = super.getConnection()) {
			ArrayList<Double> data = new ArrayList<Double>();
			PreparedStatement pstmt = con.prepareStatement(
					"select count(a.id) as afspraken, sum(b.prijs) as inkomsten \n" + 
					"from afspraak a join afspraakbehandeling ab on a.id =ab.afspraak\n" + 
					"				join behandeling b on b.id = ab.behandeling\n" + 
					"WHERE b.bedrijf = '"+ bedrijf.getEmail() +"' \n" + 
					"AND a.timestamp < SYSDATE() \n" + 
					"AND a.timestamp > '"+ServiceFilter.DateToStringFormatter(date, "YYYY-MM-dd")+"';");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				double afspraken = dbResultSet.getDouble("afspraken");
				double inkomsten = dbResultSet.getDouble("inkomsten");
				data.add(afspraken);
				data.add(inkomsten);
				return data;
			} 
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public ArrayList<Dag> getAantalAfsprakenPerDag(Bedrijf bedrijf, Date date) {
		try (Connection con = super.getConnection()) {
			ArrayList<Dag> dagen = new ArrayList<Dag>();
			PreparedStatement pstmt = con.prepareStatement(
					"select a.dag_nr, count(a.id) as aantalAfspraken \n" + 
					"FROM afspraak a join afspraakbehandeling h on a.id = h.afspraak\n" + 
					"				join behandeling b on b.id = h.behandeling\n" + 
					"WHERE b.bedrijf = '"+ bedrijf.getEmail() +"' \n" + 
					"AND timestamp < SYSDATE() \n" + 
					"AND timestamp > "+ServiceFilter.DateToStringFormatter(date, "YYYY-MM-dd")+" "+ 
					"group by a.dag_nr \n" + 
					"ORDER BY a.dag_nr"); 
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int dag_nr = dbResultSet.getInt("dag_nr");
				int aantalAfspraken = dbResultSet.getInt("aantalAfspraken");

				Dag dag = new Dag(dag_nr, aantalAfspraken);
				
				dagen.add(dag);
			} 
			return dagen;
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public ArrayList<Double> getAantalAfsprakenEnInkomstenByklant(Bedrijf bedrijf, Klant klant) {
		try (Connection con = super.getConnection()) {
			ArrayList<Double> data = new ArrayList<Double>();
			PreparedStatement pstmt = con.prepareStatement(
					"select count(a.id) as afspraken, sum(b.prijs) as prijs \n" + 
					"from afspraak a join klant k on a.klant = k.id \n" + 
					"                join afspraakbehandeling l on l.afspraak = a.id\n" + 
					"                join behandeling b on b.id = l.behandeling\n" + 
					"WHERE k.id= "+klant.getId()+" \n" + 
					"AND k.bedrijf = '"+bedrijf.getEmail()+"'"); 
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				double afspraken = dbResultSet.getInt("afspraken");
				double prijs = dbResultSet.getInt("prijs");

				data.add(afspraken);
				data.add(prijs);
			} 
			return data;
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public ArrayList<Afspraak> getLaatste3Afspraken(Bedrijf bedrijf, Klant klant) {
		try (Connection con = super.getConnection()) {
			ArrayList<Afspraak> afspraken = new ArrayList<Afspraak>();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT a.timestamp as timestamp, sum(b.prijs) as prijs \n" + 
					"from afspraak a join afspraakbehandeling f on a.id = f.afspraak \n" + 
					"				join behandeling b on f.behandeling = b.id \n" + 
					"				join klant k on k.id = a.klant \n" + 
					"where b.bedrijf = '"+bedrijf.getEmail()+"' \n" + 
					"AND k.id="+klant.getId()+" \n" + 
					"GROUP by a.timestamp \n" + 
					"limit 5;"); 
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String timestamp = dbResultSet.getString("timestamp");
				
				double prijs = dbResultSet.getDouble("prijs");
				Afspraak afspraak = new Afspraak(ServiceFilter.StringToDateFormatter(timestamp, "YYYY-MM-dd HH:mm"));
				afspraak.setPrijs(prijs);
				
				afspraken.add(afspraak);
			} 
			return afspraken;
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public int getMinutesOfAfspraak(Afspraak afspraak) {
		int minuten = 0;
		try (Connection con = super.getConnection()) {
			StringBuilder preStmt = new StringBuilder();
			preStmt.append("select lengte \n" + 
					"from behandeling \n" + 
					"where bedrijf = '"+afspraak.getBedrijf().getEmail()+"' \n"
				  + "AND id = 0 ");
			
			for(Behandeling behandeling : afspraak.getBehandelingen()) {
				preStmt.append("OR id = "+ behandeling.getId() +" \n");
			}
			PreparedStatement pstmt = con.prepareStatement(preStmt.toString());

			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				 String lengte = dbResultSet.getString("lengte");
				 int uren = Integer.parseInt(lengte.split(":")[0]);
				 minuten = Integer.parseInt(lengte.split(":")[1]) + uren*60;
				 System.out.println(uren);
				 System.out.println(minuten);
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return minuten;
	}

	@Override
	public Klant getKlant(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "select k.id, k.naam, k.email, k.telefoon, k.geslacht, k.adres \n" + 
					  "from klant k join afspraak a on a.klant = k.id 							\n"  
					+ "where a.id = "+afspraak.getId());
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//klant
				int id = dbResultSet.getInt("id");
				System.out.println(id);
				String klantNaam = dbResultSet.getString("naam");
				String klantEmail = dbResultSet.getString("email");
				String klantGeslacht = dbResultSet.getString("geslacht");

				if(klantEmail == null) {
					klantEmail = "-";
				}
				String klantTelefoon = dbResultSet.getString("telefoon");
				if(klantTelefoon == null) {
					klantTelefoon = "-";
				} 
				String klantAdres = dbResultSet.getString("adres");
				if(klantAdres == null) {
					klantAdres = "-";
				} 
				
				Klant klant = new Klant(id, klantNaam, klantEmail, klantTelefoon, klantGeslacht, klantAdres);
				return klant;
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return null;
	}

	
	public ArrayList<Behandeling> getBehandelingen(Afspraak afspraak) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "select b.id, b.naam, b.lengte, b.prijs \n" + 
					  "from behandeling b join afspraakbehandeling n on b.id = n.behandeling				      \n" + 
					  "where n.afspraak = "+afspraak.getId());

			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//klant
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				double prijs = dbResultSet.getDouble("prijs");
				
				String lengteString = dbResultSet.getString("lengte");
				Date lengte = ServiceFilter.StringToDateFormatter(lengteString, "HH:mm");

				Behandeling behandeling = new Behandeling(id, naam, null, lengte, prijs, null);
				behandelingen.add(behandeling);
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return behandelingen;
	}
	public boolean deleteAfspraak(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
				  "delete from afspraakbehandeling \n"
				 + "where afspraak = "+afspraak.getId());
			System.out.println(pstmt);
			
			pstmt.executeUpdate();
			PreparedStatement pstmt1 = con.prepareStatement(
				   "delete from afspraak \n "
				 + "where id = "+afspraak.getId());
			System.out.println(pstmt1);
			pstmt1.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
