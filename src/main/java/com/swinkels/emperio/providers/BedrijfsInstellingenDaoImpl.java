package com.swinkels.emperio.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.BedrijfsInstellingen;
import com.swinkels.emperio.service.ServiceFilter;

public class BedrijfsInstellingenDaoImpl extends MariadbBaseDao implements BedrijfsInstellingenDao{

	public boolean save(BedrijfsInstellingen bedrijf) {
		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"insert into bedrijfsinstellingen (bedrijf, kleurKlasse1, maximumPrijsVanKlasse1, "
					+ "kleurKlasse2,maximumPrijsVanKlasse2,kleurKlasse3, telefoonKlantInvoer, emailKlantInvoer, "
					+ "adresKlantInvoer,emailBedrijf ,telefoonBedrijf,adresBedrijf) \n "
					+ "values('"+bedrijf.getBedrijf().getEmail()+"', '"+bedrijf.getKleurKlasse1()+"', \n"
					+ ""+bedrijf.getMaximumPrijsVanKlasse1()+", \n" 
					+ "'"+bedrijf.getKleurKlasse2()+"', "+bedrijf.getMaximumPrijsVanKlasse2()+", '"+bedrijf.getKleurKlasse3()+"', \n "
					+ ""+bedrijf.isTelefoonKlantInvoer()+ ", "+bedrijf.isEmailKlantInvoer() + ", \n"
					+ ""+bedrijf.isAdresKlantInvoer()+", '"+bedrijf.getEmailBedrijf()+"', '"+bedrijf.getTelefoonBedrijf()+"', \n"
					+ "'"+bedrijf.getAdresBedrijf()+"')");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			if(update(bedrijf)) {
				return true;
			}
		}
		return false;
	}

	public boolean update(BedrijfsInstellingen bedrijf) {
		try (Connection con = super.getConnection()) {
			System.out.println("================UPDATE ===============");
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(
					"UPDATE bedrijfsinstellingen SET kleurKlasse1 = '"+bedrijf.getKleurKlasse1()+"', \n"
					+ "maximumPrijsVanKlasse1 = "+bedrijf.getMaximumPrijsVanKlasse1()+", \n"
					+ "kleurKlasse2 = '"+bedrijf.getKleurKlasse2()+"', \n"
					+ "maximumPrijsVanKlasse2 = "+bedrijf.getMaximumPrijsVanKlasse2()+", \n"
					+ "kleurKlasse3 = '"+bedrijf.getKleurKlasse3()+"', \n"
					+ "telefoonKlantInvoer = "+bedrijf.isTelefoonKlantInvoer()+", \n"
					+ "emailKlantInvoer = "+bedrijf.isEmailKlantInvoer() +", \n"
					+ "adresKlantInvoer = "+bedrijf.isAdresKlantInvoer()+", \n"
					+ "emailBedrijf = '"+bedrijf.getEmailBedrijf()+"', \n"
					+ "telefoonBedrijf = '"+bedrijf.getTelefoonBedrijf()+"', \n"
					+ "adresBedrijf = '"+bedrijf.getAdresBedrijf()+"' \n "
					+ "WHERE bedrijf = '"+bedrijf.getBedrijf().getEmail()+"'");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return false;
	}	
}
