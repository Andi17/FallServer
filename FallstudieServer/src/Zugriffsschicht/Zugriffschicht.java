package Zugriffsschicht;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Optionen.Optionen;

import jdbc.JdbcAccess;

public class Zugriffschicht {

	private JdbcAccess db;

	public Zugriffschicht(JdbcAccess db) {
		this.db = db;
	}

	/*
	 * BENUTZER
	 */
	public Benutzer neuerBenutzerErstellen(String Benutzername,
			String Passwort, int idOrgaEinheit, boolean Gesperrt) {
		Benutzer rueckgabe = null;
		try {
			rueckgabe = new Benutzer(Benutzername, Passwort, idOrgaEinheit,
					Gesperrt, db);
		} catch (SQLException e) {
			System.out.println("Zugriffschicht: neuerBenutzerErstellen: "+e);
		}
		return rueckgabe;
	}
	public Benutzer getBenutzervonBenutzername(String Benutzername) {
		Benutzer rueckgabe = null;
		try {
			ResultSet resultSet;
			resultSet = db
					.executeQueryStatement("SELECT * FROM Benutzer WHERE Benutzername = '"
							+ Benutzername + "'");
			if(resultSet.next())
			rueckgabe = new Benutzer(resultSet, db, this);
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}



	public List<Benutzer> getAlleBenutzer() {
		ResultSet resultSet;
		List<Benutzer> rueckgabe = new ArrayList<Benutzer>();
		try {
			resultSet = db.executeQueryStatement("SELECT * FROM Benutzer ORDER BY Benutzername");
			while (resultSet.next()) {
				rueckgabe.add(new Benutzer(resultSet, db, this));
			}
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		return rueckgabe;
	}

	/*
	 * Statistik
	 */
	public Statistik neueStatistikErstellen(int idOrgaEinheit,
			int kalenderWoche, int jahr, int idStrichart, int strichanzahl) {
		Statistik rueckgabe = null;
		try {
			rueckgabe = new Statistik(idOrgaEinheit, kalenderWoche, jahr,
					idStrichart, strichanzahl, db);
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}

	public List<Statistik> getStatistikzuOrgaEinheitinKWundJahr(int idOrgaEinheit,
			int kalenderWoche, int jahr) {
		ResultSet resultSet;
		List<Statistik> rueckgabe = new ArrayList<Statistik>();
		try {
			resultSet = db
					.executeQueryStatement("SELECT * FROM Statistik WHERE idOrgaEinheit = '"
							+ idOrgaEinheit
							+ "' AND KalenderWoche = '"
							+ kalenderWoche + "' AND Jahr = '" + jahr + "'");
			while (resultSet.next()) {
				rueckgabe.add(new Statistik(resultSet, db));
			}
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		return rueckgabe;
	}

	/*
	 * Berechtigung
	 */
	
	public int getBerechtigungzuLeitername(String Benutzername) {
		ResultSet resultSet;
		int rueckgabe = 0;
		try {
			resultSet = db
					.executeQueryStatement("SELECT Leiterberechtigung FROM " +
							"OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE " +
							"Leitername = '" + Benutzername + "'");
			if(resultSet.next())
			rueckgabe = resultSet.getInt("Leiterberechtigung");
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}

	public int getBerechtigungzuMitarbeiter(String Benutzername) {
		ResultSet resultSet;
		int rueckgabe = 0;
		try {
			resultSet = db
					.executeQueryStatement("SELECT Mitarbeiterberechtigung FROM " +
							"Benutzer NATURAL JOIN OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE " +
							"Benutzername = '" + Benutzername + "'");
			if(resultSet.next())
			rueckgabe = resultSet.getInt("Mitarbeiterberechtigung");
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}

	/*public Berechtigung getBerechtigungzuidBerechtigung(int idBerechtigung) {
		Berechtigung rueckgabe = null;
		ResultSet resultSet;
		try {
			resultSet = db
					.executeQueryStatement("SELECT * FROM Berechtigungen WHERE idBerechtigung = '"
							+ idBerechtigung + "'");
			if(resultSet.next())
			rueckgabe = new Berechtigung(resultSet, db);
			resultSet.close();
		} catch (SQLException e) {
			System.out.println("Zugriffschicht/getBerechtigungzuidBerechtigung :"+e);
		}
		return rueckgabe;
	}*/
	
	/*
	 * ORGAEINHEIT
	 */
	
	public OrgaEinheit neueOrgaEinheit(int idUeberOrgaEinheit, String OrgaEinheitBez, String Leitername,
			boolean Zustand, String OrgaEinheitTyp){
		OrgaEinheit rueckgabe = null;
		try {
			rueckgabe = new OrgaEinheit(idUeberOrgaEinheit, OrgaEinheitBez, Leitername,
			Zustand, OrgaEinheitTyp, db, this);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}
	
	public OrgaEinheit getOrgaEinheitvonBezeichnung(String bezeichnung){
		OrgaEinheit rueckgabe = null;
		try {
			ResultSet resultSet = db.executeQueryStatement("SELECT * FROM OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE OrgaEinheitBez = '" + bezeichnung + "'");

			if(resultSet.next())
				rueckgabe = new OrgaEinheit(resultSet, db, this);
			resultSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rueckgabe;
	}
	
	public List<OrgaEinheit> getOrgaEinheiten(boolean nurAktive, boolean nurStrichberechtigte){
		ResultSet resultSet;
		List<OrgaEinheit> rueckgabe = new ArrayList<OrgaEinheit>();
		try {
			if(nurStrichberechtigte){
				resultSet = db
						.executeQueryStatement("SELECT * FROM OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE MitarbeiterBerechtigung = 2 ORDER BY OrgaEinheitBez");
			}
			else{
				if(nurAktive)resultSet = db
						.executeQueryStatement("SELECT * FROM OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE Zustand = 1 ORDER BY OrgaEinheitBez");
				else resultSet = db
						.executeQueryStatement("SELECT * FROM OrgaEinheiten NATURAL JOIN OrgaEinheitTyp ORDER BY OrgaEinheitBez");
			}
			while(resultSet.next()){
			rueckgabe.add(new OrgaEinheit(resultSet, db, this));
			}
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}
	
	public OrgaEinheit getOrgaEinheitZuidOrgaEinheit(int idOrgaEinheit){
		ResultSet resultSet;
		OrgaEinheit rueckgabe = null;
		try {
			resultSet = db
					.executeQueryStatement("SELECT * FROM OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE idOrgaEinheit = '"
							+ idOrgaEinheit + "'");
			if(resultSet.next())
			rueckgabe = new OrgaEinheit(resultSet, db, this);
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}
	
	public OrgaEinheit getOrgaEinheitZuLeitername(String Leitername) {
		OrgaEinheit rueckgabe = null;
		ResultSet resultSet;
		try {
			resultSet = db
					.executeQueryStatement("SELECT * FROM OrgaEinheiten NATURAL JOIN OrgaEinheitTyp WHERE Leitername = '"
							+ Leitername + "'");
			if(resultSet.next())
			rueckgabe = new OrgaEinheit(resultSet, db, this);
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);

		}
		return rueckgabe;
	}
	
	public List<String> getOrgaEinheitTypen(){
		try {
			List<String> listTypen = new ArrayList<String>();
			ResultSet result = db.executeQueryStatement("SELECT OrgaEinheitTyp FROM OrgaEinheitTyp");
			while(result.next())listTypen.add(result.getString("OrgaEinheitTyp"));
			return listTypen;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Strichart
	 */
	public Strichart neueStrichartErstellen(String strichbezeichnung, boolean zustand){
		Strichart rueckgabe = null;
		try {
			rueckgabe = new Strichart(strichbezeichnung, zustand, db);
		} catch (SQLException e) {
			System.out.println("Zugriffschicht: neueStrichartErstellen: "+e);
		}
		return rueckgabe;
	}
	
	//Gibt Strichart zurück, wenns die Strichart nicht gibt null
	public Strichart getStrichart(String strichbezeichnung){
		Strichart rueckgabe = null;
		ResultSet resultSet;
		try {
			resultSet = db
					.executeQueryStatement("SELECT * FROM Stricharten WHERE Strichbez = '" + strichbezeichnung + "'");
			if(resultSet.next())rueckgabe = new Strichart(resultSet, db);
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);

		}
		return rueckgabe;
	}
	
	//Gibt liste mit allen Stricharten zurück. wenn true übergeben wird werden nur die aktive zurück gegeben.
	public List<Strichart> getAlleStricharten(boolean nurAktive) {
		ResultSet resultSet;
		List<Strichart> listeStricharten = new ArrayList<Strichart>();
		try {
			if(!nurAktive)resultSet = db.executeQueryStatement("SELECT * FROM Stricharten");
			else resultSet = db.executeQueryStatement("SELECT * FROM Stricharten WHERE Zustand = 1");
			while (resultSet.next()) {
				listeStricharten.add(new Strichart(resultSet, db));
			}
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		return listeStricharten;
	}

	public Arbeitsschritt neuerArbeitsschritt(int idOrgaEinheit, Date datum, int idStrichart,
			int strichzahl, int kalendarwoche, int jahr){
		Arbeitsschritt rueckgabe = null;
		try {
			rueckgabe = new Arbeitsschritt(idOrgaEinheit, idStrichart, datum, strichzahl, kalendarwoche, jahr, db);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}
	
	public boolean arbeitsschritteLoeschen(Date datum){
		long aktuelleZeit = datum.getTime();
		long zuLoeschenZeit = aktuelleZeit + Optionen.getSpeicherdauer() * 24 * 60 * 60 * 1000;
		Date zuLoeschenDatum = new Date(zuLoeschenZeit);
		Timestamp timestamp = new Timestamp(zuLoeschenDatum.getTime());
		try {
			db.executeUpdateStatement("DELETE FROM Arbeitsschritte WHERE Timestamp < '" + timestamp + "'");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

	public Statistik neueStatistik(int idOrgaEinheit, int kalendarwoche, int jahr,
			int idStrichart, int strichanzahl){
		Statistik rueckgabe = null;
		try {
			rueckgabe = new Statistik(idOrgaEinheit, kalendarwoche, jahr, idStrichart, strichanzahl, db);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}
	

	public void disconnect() throws SQLException {
		db.disconnect();
	}

}
