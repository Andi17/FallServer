package Zugriffsschicht;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.JdbcAccess;

public class OrgaEinheit {
	private JdbcAccess db;
	private Zugriffschicht dbZugriff;
	private int idOrgaEinheit;
	private int idUeberOrgaEinheit;
	private String OrgaEinheitBez;
	private String Leitername;
	private int idLeiterBerechtigung;
	private boolean zustand;
	private int idMitarbeiterBerechtigung;


	public OrgaEinheit(ResultSet resultSet, JdbcAccess db, Zugriffschicht dbZugriff) throws SQLException{
		werteSetzen(resultSet);
		this.db = db;
		this.dbZugriff = dbZugriff;
	}

	public OrgaEinheit(int idUeberOrgaEinheit, String OrgaEinheitBez, String Leitername,
			int idLeiterBerechtigung, boolean zustand, int idMitarbeiterBerechtigung, JdbcAccess db, Zugriffschicht dbZugriff) throws SQLException{
		this.db = db;
		this.dbZugriff = dbZugriff;
		String stringZustand;
		if(zustand)stringZustand ="1";
		else stringZustand ="0";
		if(Leitername==null){
			db.executeUpdateStatement("INSERT INTO OrgaEinheiten (" +
					"idUeberOrgaEinheit, OrgaEinheitBez, idLeiterBerechtigung, Zustand, idMitarbeiterBerechtigung) " +
					"VALUES (" + idUeberOrgaEinheit + ", '" + OrgaEinheitBez + "', " 
					+ idLeiterBerechtigung + ", " + stringZustand +", " + idMitarbeiterBerechtigung +")");
			ResultSet resultSet = db.executeQueryStatement("SELECT * FROM OrgaEinheiten WHERE " +
					"idUeberOrgaEinheit = " + idUeberOrgaEinheit +" AND "+
					"OrgaEinheitBez = '" + OrgaEinheitBez +"' AND "+
					"idLeiterBerechtigung = " + idLeiterBerechtigung +" AND "+
					"Zustand = " + stringZustand +" AND "+
					"idMitarbeiterBerechtigung = " + idMitarbeiterBerechtigung);
			resultSet.next();
			werteSetzen(resultSet);
			resultSet.close();
		}
				
		else {
			db.executeUpdateStatement("INSERT INTO OrgaEinheiten (" +
					"idUeberOrgaEinheit, OrgaEinheitBez, Leitername, idLeiterBerechtigung, Zustand, idMitarbeiterBerechtigung) " +
					"VALUES (" + idUeberOrgaEinheit + ", '" + OrgaEinheitBez + "', '" + Leitername +
					"', " + idLeiterBerechtigung + ", " + stringZustand +", " + idMitarbeiterBerechtigung +")");
			ResultSet resultSet = db.executeQueryStatement("SELECT * FROM OrgaEinheiten WHERE " +
					"idUeberOrgaEinheit = " + idUeberOrgaEinheit +" AND "+
					"OrgaEinheitBez = '" + OrgaEinheitBez +"' AND "+
					"Leitername = '" + Leitername +"' AND "+
					"idLeiterBerechtigung = " + idLeiterBerechtigung +" AND "+
					"Zustand = " + stringZustand +" AND "+
					"idMitarbeiterBerechtigung = " + idMitarbeiterBerechtigung);
			resultSet.next();
			werteSetzen(resultSet);
			resultSet.close();
		}
	}
	
	public void werteSetzen(ResultSet resultSet) throws SQLException{
		this.idOrgaEinheit = resultSet.getInt("idOrgaEinheit");
		this.idUeberOrgaEinheit = resultSet.getInt("idUeberOrgaEinheit");
		this.OrgaEinheitBez = resultSet.getString("OrgaEinheitBez");
		this.Leitername = resultSet.getString("Leitername");
		this.idLeiterBerechtigung = resultSet
				.getInt("idLeiterBerechtigung");
		this.zustand = resultSet.getBoolean("Zustand");
		this.idMitarbeiterBerechtigung = resultSet.getInt("idMitarbeiterBerechtigung");
	}
	
	public List<OrgaEinheit> getUnterOrgaEinheiten(){
		ResultSet resultSet;
		List<OrgaEinheit> rueckgabe = new ArrayList<OrgaEinheit>();
		try {
			resultSet = db
					.executeQueryStatement("SELECT * FROM OrgaEinheiten WHERE idUeberOrgaEinheit = " + this.idOrgaEinheit);
			while(resultSet.next()){
			rueckgabe.add(new OrgaEinheit(resultSet, db, dbZugriff));
			}
			resultSet.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rueckgabe;
	}
	
	public String getLeiterBerechtigungBezeichnung(){
		Berechtigung berechtigung = dbZugriff.getBerechtigungzuLeitername(Leitername);
		if(berechtigung!=null)return berechtigung.getBerechtigungbez();
		else return "Keine Berechtigung";
	}
	
	

	public int getIdOrgaEinheit() {
		return idOrgaEinheit;
	}

	public int getIdUeberOrgaEinheit() {
		return idUeberOrgaEinheit;
	}

	public String getOrgaEinheitBez() {
		return OrgaEinheitBez;
	}

	public String getLeitername() {
		return Leitername;
	}

	public int getIdLeiterBerechtigung() {
		return idLeiterBerechtigung;
	}

	public boolean isZustand() {
		return zustand;
	}

	public int getIdMitarbeiterBerechtigung() {
		return idMitarbeiterBerechtigung;
	}

	public boolean setOrgaEinheitBez(String orgaEinheitBez){
		try {
			db.executeUpdateStatement("UPDATE OrgaEinheiten SET orgaEinheitBez = " + orgaEinheitBez +" WHERE idOrgaEinheit = " + idOrgaEinheit);
			OrgaEinheitBez = orgaEinheitBez;
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean setLeitername(String leitername) {
		try {
			db.executeUpdateStatement("UPDATE OrgaEinheiten SET Leitername = '" + leitername + "' WHERE idOrgaEinheit = " + idOrgaEinheit);
			Leitername = leitername;
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean setIdLeiterBerechtigung(int idLeiterBerechtigung) {
		try {
			db.executeUpdateStatement("UPDATE OrgaEinheiten SET idLeiterBerechtigung = " + idLeiterBerechtigung +" WHERE idOrgaEinheit = " + idOrgaEinheit);
			this.idLeiterBerechtigung = idLeiterBerechtigung;
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean setZustand(boolean neuerZustand){
		try {
			String stringZustand;
			if(neuerZustand)stringZustand = "1";
			else stringZustand = "0";
			db.executeUpdateStatement("UPDATE OrgaEinheiten SET Zustand = " + stringZustand +" WHERE idOrgaEinheit = " + idOrgaEinheit);
			zustand = neuerZustand;
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void setIdMitarbeiterBerechtigung(int idMitarbeiterBerechtigung) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheiten SET idMitarbeiterBerechtigung = " + idMitarbeiterBerechtigung +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
	}

	public String getMitarbeiterBerechtigungBezeichnung() {
		String mitarbeiterBezeichnung = "Keine Berechtigung";
		try {
			ResultSet result = db.executeQueryStatement("SELECT Berechtigungsbez FROM Berechtigung WHERE idBerechtigung = " + idMitarbeiterBerechtigung);
			mitarbeiterBezeichnung = result.getString("Berechtigungsbez");
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return mitarbeiterBezeichnung;
	}
	
	public int getAlleStricheInWoche(int kalendarwoche, int jahr, int idStrichart){
		try {
			ResultSet result = db.executeQueryStatement("SELECT SUM(Strichzahl) FROM Arbeitsschritte " +
					"GROUP BY idOrgaEinheit, idStrichart, Kalendarwoche, Jahr HAVING " +
					"idOrgaEinheit = " + idOrgaEinheit + " AND " +
					"idStrichart = " + idStrichart + " AND " +
					"Kalendarwoche = " + kalendarwoche + " AND " +
					"Jahr = " + jahr);
			if(result.next()) return result.getInt(1);
			else return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
