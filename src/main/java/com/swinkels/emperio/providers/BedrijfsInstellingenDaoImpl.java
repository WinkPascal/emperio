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
					"insert into bedrijfsIntellingen (kleurKlasse1, minimumPrijsVanKlasse1, maximumPrijsVanKlasse1, "
					+ "kleurKlasse2,maximumPrijsVanKlasse2,kleurKlasse3, telefoonKlantInvoer, emailKlantInvoer, "
					+ "adresKlantInvoer,emailBedrijf ,telefoonBedrijf,adresBedrijf  	 "
					+ "values('"+bedrijf.getKleurKlasse1()+"', "+bedrijf.getMinimumPrijsVanKlasse1()+", "+bedrijf.getMaximumPrijsVanKlasse1()+", " 
					+ "'"+bedrijf.getKleurKlasse2()+"', "+bedrijf.getMaximumPrijsVanKlasse2()+", '"+bedrijf.getKleurKlasse3()+"', "+bedrijf.isTelefoonKlantInvoer()+ ", "+bedrijf.isEmailKlantInvoer() 
					+ ", "+bedrijf.isAdresKlantInvoer()+", '"+bedrijf.getEmailBedrijf()+"', '"+bedrijf.getTelefoonBedrijf()+"', '"+bedrijf.getAdresBedrijf()+"')"
					+ " where bedrijf = "+bedrijf.getBedrijf().getEmail()+";");
			System.out.println(pstmt);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
