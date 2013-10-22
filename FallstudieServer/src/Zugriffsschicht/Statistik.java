package Zugriffsschicht;

import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.JdbcAccess;

public class Statistik {
	private int idOrgaEinheit;
	private int kalenderWoche;
	private int jahr;
	private int idStrichart;
	private int strichzahl;
	private JdbcAccess db;

	// Konstruktor wenn die Statistik schon existiert.
	public Statistik(ResultSet resultSet, JdbcAccess db) throws SQLException {
		this.db = db;
		werteSetzen(resultSet);
	}

	// Konstruktor bei Neuanlegen
	public Statistik(int idOrgaEinheit, int kalenderwoche, int jahr,
			int idStrichart, int strichanzahl, JdbcAccess db)
			throws SQLException {
		this.db = db;
		db.executeUpdateStatement("INSERT INTO Statistiken (idOrgaEinheit, " +
				"Kalenderwoche, Jahr, idStrichart, Strichzahl) " +
				"VALUES ( " + idOrgaEinheit + ", " + kalenderwoche + ", " + jahr +
				", " + idStrichart + ", " + strichanzahl + ")");
		ResultSet resultSet = db.executeQueryStatement("SELECT * FROM Statistiken WHERE " +
				"idOrgaEinheit = '" + idOrgaEinheit + "' AND " +
				"Kalenderwoche = '" + kalenderwoche + "' AND " +
				"Jahr = '" + jahr + "' AND " +
				"idStrichart = '" + idStrichart + "' AND " +
				"Strichzahl = " + strichanzahl);
		resultSet.next();
		werteSetzen(resultSet);
		resultSet.close();
	}
	
	//Methode nur um Kurs zu sparen
	private void werteSetzen(ResultSet resultSet) throws SQLException{
		this.idOrgaEinheit = resultSet.getInt("idOrgaEinheit");
		this.kalenderWoche = resultSet.getInt("KalenderWoche");
		this.jahr = resultSet.getInt("Jahr");
		this.idStrichart = resultSet.getInt("idStrichart");
		this.strichzahl = resultSet.getInt("Strichzahl");
	}

	public int getOrgaEinheit() {
		return idOrgaEinheit;
	}

	public int getKalenderWoche() {
		return kalenderWoche;
	}

	public int getJahr() {
		return jahr;
	}

	public int getStrichart() {
		return idStrichart;
	}

	public int getStrichanzahl() {
		return strichzahl;
	}
	
	public String getStrichartBez(){
		try {
			ResultSet result = db.executeQueryStatement("SELECT Strichbez FROM Stricharten WHERE idStrichart = " + idStrichart);
			if(result.next()){
				return result.getString("Strichbez");
			}else{
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
