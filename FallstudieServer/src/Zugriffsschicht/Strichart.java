package Zugriffsschicht;

import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.JdbcAccess;

public class Strichart {
	private JdbcAccess db;
	private int idStrichart;
	private String StrichBez;
	private boolean zustand;

	public Strichart(String StrichBez, boolean Zustand,
			JdbcAccess db) throws SQLException {
		db.executeUpdateStatement("INSERT INTO Stricharten (StrichBez, " +
				"Zustand) " +
				"VALUES ( '" + StrichBez + "', " + Zustand + ")");
		ResultSet resultSet = db.executeQueryStatement("SELECT * FROM Stricharten WHERE " +
				"StrichBez = '" + StrichBez + "' AND "+
				"Zustand = " + Zustand );
		resultSet.next();
		werteSetzen(resultSet);
		resultSet.close();
	}
	public Strichart(ResultSet resultSet, JdbcAccess db)throws SQLException{
		werteSetzen(resultSet);
		this.db = db;
	}
	
	public void werteSetzen(ResultSet resultSet) throws SQLException{
		this.idStrichart = resultSet.getInt("idStrichart");
		this.StrichBez = resultSet.getString("StrichBez");
		this.zustand = resultSet.getBoolean("Zustand");
	}

	public int getIdStrichart() {
		return idStrichart;
	}

	public void setIdStrichart(int idStrichart) {
		this.idStrichart = idStrichart;
	}

	public String getStrichbez() {
		return StrichBez;
	}

	public boolean setStrichbez(String strichbez) {
		try {
			db.executeUpdateStatement("UPDATE Stricharten SET Strichbez = '" + strichbez + "' WHERE idStrichart = " + idStrichart);
			StrichBez = strichbez;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getZustand() {
		return zustand;
	}

	public boolean setZustand(boolean neuerZustand) {
		try {
			String stringZustand;
			if(neuerZustand)stringZustand = "1";
			else stringZustand ="0";
			db.executeUpdateStatement("UPDATE Stricharten SET Zustand = " + stringZustand + " WHERE idStrichart = " + idStrichart);
			this.zustand = neuerZustand;
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
