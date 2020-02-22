package com.swinkels.emperio.providers.Afspraak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.behandeling.BehandelingBuilder;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantBuilder;
import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.AfspraakBuilder;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.objects.statestieken.Statestieken;
import com.swinkels.emperio.providers.Afspraak.AfspraakDao;
import com.swinkels.emperio.providers.MariadbBaseDao;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.DatabaseDateAdapter;

public class AfspraakDaoImpl extends MariadbBaseDao implements AfspraakDao {
	
	public List<Afspraak> getAfsprakenFromKlantId(int klantId){
		List<Afspraak> afspraken = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT a.id as id, a.timestamp as timestamp, sum(b.prijs) as prijs  \n" + 
					"from afspraak a left join afspraakBehandeling b on a.id = b.afspraakId  \n" + 
					"join klant k on k.id = a.klantId \n"+
					"where k.BedrijfBedrijfsnaam  = '"+Security.getKey()+"' \n" + 
					"AND k.id = "+klantId+" \n" +
					"group by a.id "+ 
					"order by a.timestamp desc \n" + 
					"limit 5;");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String timestampString = dbResultSet.getString("timestamp");
				Date timestamp = DatabaseDateAdapter.StringToDate(timestampString, "yyyy-MM-dd HH:mm");
				Double prijs = dbResultSet.getDouble("prijs");
				int id = dbResultSet.getInt("id");
				Afspraak afspraak = new Afspraak(id, timestamp, prijs);
				afspraken.add(afspraak);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return afspraken;
	}
	//wordt gebruikt bij: afsprakenByDate
	
	
	public List<Afspraak> getAfsprakenBetweenDates(Date beginDate, Date eindDate) {	
		ArrayList<Afspraak> afspraken = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select a.id, a.timestamp, k.naam as naam, sum(b.prijs) as prijs \n" + 
					"from klant k join afspraak a on a.klantId = k.id \n"
					+ "left join afspraakBehandeling b on a.id = b.AfspraakId \n" + 
					"where k.BedrijfBedrijfsnaam = '"+Security.getKey()+"' AND \n" + 
					"a.timestamp BETWEEN '"+DatabaseDateAdapter.DateToString(beginDate, "yyyy-MM-dd")+"' AND "
							        + "'"+DatabaseDateAdapter.DateToString(eindDate, "yyyy-MM-dd")+"' "+
					"group by a.id "
					+ "ORDER BY a.timestamp");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				Klant klant = new KlantBuilder()
						.setNaam(dbResultSet.getString("naam"))
						.build();
				Date timestamp = DatabaseDateAdapter.StringToDate(dbResultSet.getString("timestamp"), "yyyy-MM-dd HH:mm");
				Afspraak afspraak = new Afspraak(dbResultSet.getInt("id"), timestamp, dbResultSet.getDouble("prijs"));
				afspraak.setKlant(klant);
				afspraken.add(afspraak);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return afspraken;
	}

	public boolean setAfspraak(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
			  "insert into afspraak(dagnummer, klantId, timestamp) values("
			+ Adapter.getDagNummerFromDate(afspraak.getTimeStamp())+" ,"		
			+ "'"+afspraak.getKlant().getId()+"', "
			+ "'"+DatabaseDateAdapter.DateToString(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"')");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void getAfspraakId(Afspraak afspraak) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select id from afspraak "
					+ "where klantId = '"+afspraak.getKlant().getId()+"' "
					+ "and timestamp = '"+DatabaseDateAdapter.DateToString(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm")+"'");
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				afspraak.setId(dbResultSet.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public ArrayList<Afspraak> getAfsprakenWeek(Bedrijf bedrijf, Date beginDate){
		ArrayList<Afspraak> afspraken = new ArrayList<Afspraak>();
		Date eindDate = Adapter.getNextWeek(beginDate);
		
		String beginDateString = DatabaseDateAdapter.DateToString(beginDate, "yyyy-MM-dd");
		String eindDateString = DatabaseDateAdapter.DateToString(eindDate, "yyyy-MM-dd");
		
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
			Afspraak afspraak = new AfspraakBuilder().make();;

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
				Date lengte = DatabaseDateAdapter.StringToDate(dbResultSet.getString("lengte"), "HH:mm");
				double prijs = dbResultSet.getDouble("prijs");
				
				Behandeling behandeling = new BehandelingBuilder()
						.setNaam(behandelingnaam)
						.setLengte(lengte)
						.setPrijs(prijs)
						.make();

				//Afspraak
				int id = dbResultSet.getInt("id");
				int i = 1;
				if(i == 1) {
					// de klant en afspraak met de eerste behandeling wordt aangemaakt
					String timestampString = dbResultSet.getString("timestamp");
					timestampString = timestampString.substring(0, timestampString.length()-5);
					Date timestamp = DatabaseDateAdapter.StringToDate(timestampString,"yyyy-MM-dd HH:mm");
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
					
					Klant klant = new KlantBuilder()
							.setId(id)
							.setNaam(klantNaam)
							.setEmail(klantEmail)
							.setTel(klantTelefoon)
							.setGeslacht(klantGeslacht)
							.make();
					afspraak = new AfspraakBuilder()
							.setId(id)
							.setTimestamp(timestamp)
							.setKlant(klant)
							.make();
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
	
	public void getInkomsten(Statestieken bedrijf, Date date){
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select count(a.id) as afspraken, sum(b.prijs) as inkomsten \n" + 
					"from afspraak a join afspraakBehandeling ab on a.id =ab.afspraakId\n" + 
					"				join behandeling b on b.id = ab.behandelingId\n" + 
					"WHERE b.BedrijfBedrijfsnaam = '"+ bedrijf.getBedrijfsNaam() +"' \n" + 
					"AND a.timestamp < SYSDATE() \n" + 
					"AND a.timestamp > '"+DatabaseDateAdapter.DateToString(date, "YYYY-MM-dd")+"';");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				bedrijf.setHoeveelheidAfspraken(dbResultSet.getInt("afspraken"));
				bedrijf.setHoeveelheidInkomsten(dbResultSet.getDouble("inkomsten"));
			} 
		}catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public void getAantalAfsprakenPerDag(Bedrijf bedrijf, Date date) {
		ArrayList<Dag> dagen = new ArrayList<Dag>();
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select a.dagnummer, count(a.id) as aantalAfspraken \n" + 
					"FROM afspraak a join afspraakBehandeling h on a.id = h.afspraakId \n" + 
					"				join behandeling b on b.id = h.behandelingId \n" + 
					"where b.BedrijfBedrijfsnaam = '"+ bedrijf.getBedrijfsNaam() + "' \n" +
					"and a.timestamp < SYSDATE() \n" +
					"and a.timestamp > '"+ DatabaseDateAdapter.DateToString(date, "YYYY-MM-dd") + "'\n"+ 
					"group by a.dagnummer \n" + 
					"ORDER BY a.dagnummer"); 
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				int dag_nr = dbResultSet.getInt("dagnummer");
				int aantalAfspraken = dbResultSet.getInt("aantalAfspraken");
				Dag dag = new Dag(dag_nr, aantalAfspraken);
				dagen.add(dag);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		bedrijf.setDagen(dagen);
	}
	
	public void getAantalAfsprakenEnInkomstenByklant(Bedrijf bedrijf, Klant klant) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					"select count(a.id) as afspraken, sum(b.prijs) as prijs \n" + 
					"from afspraak a join klant k on a.klantId = k.id \n" + 
					"                join afspraakBehandeling l on l.afspraakId = a.id \n" + 
					"                join behandeling b on b.id = l.behandelingId \n" + 
					"WHERE k.id= "+klant.getId()+" \n" + 
					"AND k.BedrijfBedrijfsnaam  = '"+bedrijf.getEmail()+"'"); 
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {				
				klant.setHoeveeleheidAfspraken(dbResultSet.getInt("afspraken"));
				klant.setHoeveelheidInkomsten(dbResultSet.getInt("prijs"));
			} 
		}catch (SQLException e) {
			e.printStackTrace();
		} 		
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
				
				Klant klant = new KlantBuilder()
						.setId(id)
						.setNaam(klantNaam)
						.setEmail(klantEmail)
						.setTel(klantTelefoon)
						.setGeslacht(klantGeslacht)
						.setAdres(klantAdres)
						.make();
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
					  "from behandeling b join afspraakbehandeling n on b.id = n.behandeling \n" + 
					  "where n.afspraak = "+afspraak.getId());

			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				//klant
				int id = dbResultSet.getInt("id");
				String naam = dbResultSet.getString("naam");
				double prijs = dbResultSet.getDouble("prijs");
				
				String lengteString = dbResultSet.getString("lengte");
				Date lengte = DatabaseDateAdapter.StringToDate(lengteString, "HH:mm");

				Behandeling behandeling = new BehandelingBuilder()
						.setId(id)
						.setNaam(naam)
						.setLengte(lengte)
						.setPrijs(prijs)
						.make();
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

	
	public HashMap<String, String> getGeslachtenVanAfspraken(Statestieken bedrijf,Date date) {
		HashMap<String, String> geslachten = new HashMap<String, String>();
		
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(
					  "select count(a.id) as afspraken, b.geslacht as geslacht \n" + 
					  "from afspraak a join afspraakBehandeling n on a.id = n.afspraakId\n" + 
					  "join behandeling b on n.Behandelingid = b.id\n" + 
					  "WHERE b.BedrijfBedrijfsnaam = '"+ bedrijf.getBedrijfsNaam() +"' \n" + 
					  "AND timestamp < SYSDATE() \n" + 
					  "AND timestamp > "+DatabaseDateAdapter.DateToString(date, "YYYY-MM-dd")+" \n"+
					  "group by b.geslacht \n");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				String afspraken = dbResultSet.getString("afspraken");
				String geslacht = dbResultSet.getString("geslacht");
				
				geslachten.put(geslacht, afspraken);
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return geslachten;
	}
	
	public List<Double> getPrijzenVanAfspraak(Statestieken statestieken, Date date){
		List<Double> afspraken = new ArrayList<Double>();
		try (Connection con = super.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(
					  "SELECT sum(b.prijs) as prijs \n" + 
					  "from behandeling b join afspraakBehandeling n on b.id = n.Behandelingid \n" + 
					  "join afspraak a on n.afspraakId = a.id \n" + 
					  "WHERE b.BedrijfBedrijfsnaam = '"+ statestieken.getBedrijfsNaam() +"' \n" + 
					  "AND timestamp < SYSDATE() \n" + 
					  "AND timestamp > "+DatabaseDateAdapter.DateToString(date, "YYYY-MM-dd")+" \n"+
					  "group by a.id ");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				afspraken.add(dbResultSet.getDouble("prijs"));
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return afspraken;
	}
	public List<Date> getLengtesVanAfspraak(Statestieken statestieken, Date date){
		List<Date> lengtes = new ArrayList<Date>();
		try (Connection con = super.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(
					  "select b.lengte \n" + 
					  "from behandeling b join afspraakBehandeling n on b.id = n.Behandelingid \n" + 
					  "join afspraak a on a.id = n.Behandelingid \n" + 
					  "WHERE b.BedrijfBedrijfsnaam = '"+ statestieken.getBedrijfsNaam() +"' \n" + 
					  "AND timestamp < SYSDATE() \n" + 
					  "AND timestamp > "+DatabaseDateAdapter.DateToString(date, "YYYY-MM-dd")+" \n"+
					  "group by a.id ");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				lengtes.add(DatabaseDateAdapter.StringToDate(dbResultSet.getString("lengte"), "HH:mm"));
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return lengtes;
	}
	
	public List<Afspraak> getInkomstenForStatistics(Statestieken statestieken, Date date){
		List<Afspraak> afspraken = new ArrayList<Afspraak>();
		try (Connection con = super.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(
					  "select a.timestamp datum, count(b.prijs) as prijs \n" + 
					  "from behandeling b join afspraakBehandeling n on b.id = n.Behandelingid \n" + 
					  "join afspraak a on a.id = n.Behandelingid \n" +
					  "WHERE b.BedrijfBedrijfsnaam = '"+ statestieken.getBedrijfsNaam() +"' \n" + 
					  "AND timestamp < SYSDATE() \n" + 
					  "AND timestamp > "+DatabaseDateAdapter.DateToString(date, "YYYY-MM-dd")+" \n"+
					  "group by a.timestamp ");
			System.out.println(pstmt);
			ResultSet dbResultSet = pstmt.executeQuery();
			while (dbResultSet.next()) {
				AfspraakBuilder builder = new AfspraakBuilder();
				builder.setTimestamp(DatabaseDateAdapter.StringToDate(dbResultSet.getString("datum"), "yyyy-MM-dd HH:mm"));
				builder.setPrijs(dbResultSet.getDouble("prijs"));
				afspraken.add(builder.make());
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return afspraken;
	}
}
