package Zugriffsschicht;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;


import jdbc.JdbcAccess;

public class Arbeitsschritt {
	private int idArbeitsschritt;
	private int idOrgaEinheit;
	private int idStrichart;
	private Date Datum;
	private int Strichzahl;
	private int kalendarwoche;
	private int jahr;

	public Arbeitsschritt(ResultSet resultSet, JdbcAccess db)
			throws SQLException {
		werteSetzen(resultSet);
	}

	public Arbeitsschritt(int idOrgaEinheit, int idStrichart, Date datum,
			int strichanzahl, int kalendarWoche, int jahr, JdbcAccess db) throws SQLException {
		PreparedStatement statement = db.getConnection().prepareStatement("INSERT INTO Arbeitsschritte ("
				+ "idOrgaEinheit, idStrichart, Timestamp, Strichzahl, Kalendarwoche, Jahr) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?)");
		statement.setInt(1, idOrgaEinheit);
		statement.setInt(2, idStrichart);
		statement.setTimestamp(3, dateToSqlTimestamp(datum));
		statement.setInt(4, strichanzahl);
		statement.setInt(5, kalendarWoche);
		statement.setInt(6, jahr);
		statement.execute();
		ResultSet resultSet = db
				.executeQueryStatement("SELECT * FROM Arbeitsschritte WHERE "
						+ "idOrgaEinheit = " + idOrgaEinheit 
						+ " AND idStrichart = " + idStrichart
						+ " AND Timestamp = '" + dateToSqlTimestamp(datum)
						+ "' AND Strichzahl = "+ strichanzahl
						+ " AND Kalendarwoche = " + kalendarWoche
						+ " AND Jahr = " + jahr);
		if(resultSet.next())werteSetzen(resultSet);
		resultSet.close();
	}

	public void werteSetzen(ResultSet resultSet) throws SQLException {
		this.idArbeitsschritt = resultSet.getInt("idArbeitsschritt");
		this.idOrgaEinheit = resultSet.getInt("idOrgaEinheit");
		this.idStrichart = resultSet.getInt("idStrichart");
		this.Datum = sqlTimestampToDate(resultSet.getTimestamp("Timestamp"));
		this.Strichzahl = resultSet.getInt("Strichzahl");
		this.kalendarwoche = resultSet.getInt("Kalendarwoche");
		this.jahr = resultSet.getInt("Jahr");
	}


	public Date sqlTimestampToDate(Timestamp timestamp) {
		Date date = new Date(timestamp.getTime());
		return date;
	}

	public Timestamp dateToSqlTimestamp(Date date) {
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp;
	}
	public int getIdArbeitsschritt() {
		return idArbeitsschritt;
	}
	public int getIdOrgaEinheit() {
		return idOrgaEinheit;
	}
	public Date getDatum() {
		return Datum;
	}
	public int getIdStrichart() {
		return idStrichart;
	}
	public int getStrichzahl() {
		return Strichzahl;
	}

	public int getJahr() {
		return jahr;
	}

	public int getKalendarwoche() {
		return kalendarwoche;
	}
}
