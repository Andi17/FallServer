package Zugriffsschicht;

import java.sql.ResultSet;
import java.sql.SQLException;

import Administration.Verschluesselung;
import jdbc.JdbcAccess;

public class Benutzer {
	private JdbcAccess db;
	private Zugriffschicht dbZugriff;
	private String benutzername;
	private String Passwort;
	private int idOrgaEinheit;
	private boolean gesperrt;

	//Konstruktor über ein ResultSet. Abfrage muss dann vorher schon stattgefunden haben.
	public Benutzer(ResultSet resultSet, JdbcAccess db, Zugriffschicht dbZugriff) throws SQLException {
		this.db = db;
		this.dbZugriff = dbZugriff;
		werteSetzen(resultSet);
	}

	//Konstruktor, der einen neuen Benutzer erstellt. Wirft eine Exception wenn etwas
	//nicht funktioniert hat.
	public Benutzer(String Benutzername, String Passwort, int idOrgaEinheit,
			boolean Gesperrt, JdbcAccess db) throws SQLException{
		String GesperrtString = "0";
		if (Gesperrt){
			GesperrtString = "1";
		}
		
		Passwort = Verschluesselung.verschluesseln(Passwort);
		
		
		db.executeUpdateStatement("INSERT INTO Benutzer (Benutzername, " +
				"Passwort, idOrgaEinheit, Gesperrt) " +
				"VALUES ( '" + Benutzername + "', '" + Passwort + "', '" + idOrgaEinheit +
				"', '" + GesperrtString + "')");
		ResultSet resultSet = db.executeQueryStatement("SELECT * FROM Benutzer WHERE " +
				"Benutzername = '" + Benutzername+"'");
		resultSet.next();
		werteSetzen(resultSet);
		resultSet.close();
	}
	
	//Liest die Werte aus dem ResultSet und setzt die Werte entsprechend.
	private void werteSetzen(ResultSet resultSet) throws SQLException{
		this.benutzername = resultSet.getString("Benutzername");
		this.Passwort = resultSet.getString("Passwort");
		this.idOrgaEinheit = resultSet.getInt("idOrgaEinheit");
		this.gesperrt = resultSet.getBoolean("Gesperrt");
	}
	
	public String getOrgaEinheitBezeichnung(){
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(this.idOrgaEinheit);
		if(orgaEinheit!=null)return orgaEinheit.getOrgaEinheitBez();
		else return "Keine Organisationseinheit";
	}
	
	public String getBenutzername() {
		return benutzername;
	}

	public String getPasswort() {
		return Passwort;
	}

	public int getAktuelleOE() {
		return idOrgaEinheit;
	}

	public boolean isGesperrt() {
		return gesperrt;
	}
	
	public boolean isLeiter() {
		try {
			ResultSet result = db.executeQueryStatement("SELECT * FROM OrgaEinheiten WHERE Leitername = '" + benutzername + "'");
			if(result.next())return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Alle Setter Methoden ändern zuerst den Wert in der Datenbank und nur wenn das geklappt hat
	//auch den Wert in diesem Objekt. False wenn ein Fehler aufgetreten ist.
	public boolean setGesperrt(boolean gesperrt) {
		String stringGesperrt;
		if(gesperrt) stringGesperrt = "1";
		else stringGesperrt = "0";
		try {
			db.executeUpdateStatement("UPDATE Benutzer SET Gesperrt = " + stringGesperrt + " WHERE Benutzername = '"+benutzername+"'" );
			this.gesperrt = gesperrt;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setBenutzername(String neuerBenutzername){
		try {
			db.executeUpdateStatement("UPDATE Benutzer SET Benutzername = '" + neuerBenutzername+"' WHERE Benutzername = '"+benutzername+"'");
			db.executeUpdateStatement("UPDATE OrgaEinheiten SET Leitername = '" + neuerBenutzername + "' WHERE Leitername = '" + benutzername + "'");
			this.benutzername = neuerBenutzername;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean setPasswort(String passwort){
		try {
			passwort = Verschluesselung.verschluesseln(passwort);
			db.executeUpdateStatement("UPDATE Benutzer SET Passwort = '" + passwort+"' WHERE Benutzername = '"+benutzername+"'");
			this.Passwort = passwort;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
		
	}

	public boolean setidOrgaEinheit(int aktuelleOE){
		try {
			db.executeUpdateStatement("UPDATE Benutzer SET idOrgaEinheit = " + aktuelleOE+" WHERE Benutzername = '"+benutzername+"'");
			this.idOrgaEinheit = aktuelleOE;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean loeschen() {
		try {
			db.executeUpdateStatement("DELETE FROM Benutzer WHERE Benutzername = '"+benutzername+"'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	
}
