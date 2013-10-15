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
	private boolean Zustand;
	private int idMitarbeiterBerechtigung;


	public OrgaEinheit(ResultSet resultSet, JdbcAccess db, Zugriffschicht dbZugriff) throws SQLException{
		werteSetzen(resultSet);
		this.db = db;
		this.dbZugriff = dbZugriff;
	}

	public OrgaEinheit(int idUeberOrgaEinheit, String OrgaEinheitBez, String Leitername,
			int idLeiterBerechtigung, boolean Zustand, int idMitarbeiterBerechtigung, JdbcAccess db, Zugriffschicht dbZugriff) throws SQLException{
		this.db = db;
		this.dbZugriff = dbZugriff;
		db.executeUpdateStatement("INSERT INTO OrgaEinheiten (" +
				"idUeberOrgaEinheit, OrgaEinheitBez, Leitername, idLeiterBerechtigung, Zustand, idMitarbeiterBerechtigung) " +
				"VALUES (" + idUeberOrgaEinheit + ", " + OrgaEinheitBez + ", " + Leitername +
				", " + idLeiterBerechtigung + ", " + Zustand +", " + idMitarbeiterBerechtigung +")");
		ResultSet resultSet = db.executeQueryStatement("SELECT * FROM OrgaEinheiten WHERE " +
				"idUeberOrgaEinheit = " + idUeberOrgaEinheit +" AND "+
				"OrgaEinheitBez = " + OrgaEinheitBez +" AND "+
				"Leitername = " + Leitername +" AND "+
				"idLeiterBerechtigung = " + idLeiterBerechtigung +" AND "+
				"Zustand = " + Zustand +" AND "+
				"idMitarbeiterBerechtigung = " + idMitarbeiterBerechtigung +" AND ");
		resultSet.next();
		werteSetzen(resultSet);
		resultSet.close();
	}
	
	public void werteSetzen(ResultSet resultSet) throws SQLException{
		this.idOrgaEinheit = resultSet.getInt("idOrgaEinheit");
		this.idUeberOrgaEinheit = resultSet.getInt("idUeberOrgaEinheit");
		this.OrgaEinheitBez = resultSet.getString("OrgaEinheitBez");
		this.Leitername = resultSet.getString("Leitername");
		this.idLeiterBerechtigung = resultSet
				.getInt("idLeiterBerechtigung");
		this.Zustand = resultSet.getBoolean("Zustand");
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
		return Zustand;
	}

	public int getIdMitarbeiterBerechtigung() {
		return idMitarbeiterBerechtigung;
	}

	public void setIdUeberOrgaEinheit(int idUeberOrgaEinheit) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheit SET idUeberOrgaEinheit = " + idUeberOrgaEinheit +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		this.idUeberOrgaEinheit = idUeberOrgaEinheit;
	}

	public void setOrgaEinheitBez(String orgaEinheitBez) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheit SET orgaEinheitBez = " + orgaEinheitBez +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		OrgaEinheitBez = orgaEinheitBez;
	}

	public void setLeitername(String leitername) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheit SET Leitername = " + leitername +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		Leitername = leitername;
	}

	public void setIdLeiterBerechtigung(int idLeiterBerechtigung) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheit SET idLeiterBerechtigung = " + idLeiterBerechtigung +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		this.idLeiterBerechtigung = idLeiterBerechtigung;
	}

	public void setZustand(boolean zustand) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheit SET Zustand = " + zustand +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		Zustand = zustand;
	}

	public void setIdMitarbeiterBerechtigung(int idMitarbeiterBerechtigung) throws SQLException {
		db.executeUpdateStatement("UPDATE OrgaEinheit SET idMitarbeiterBerechtigung = " + idMitarbeiterBerechtigung +" WHERE idOrgaEinheit = " + idOrgaEinheit);
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
	}

	public String getMitarbeiterBerechtigungBezeichnung() {
		// TODO Auto-generated method stub
		return null;
	}

}
