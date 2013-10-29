package kommunikationsklassen;

public class ComBenutzer {
	private String Benutzername;
	private String Passwort;
	private int    idOrgaEinheit;
	private String OrgaEinheitBez;
	private boolean Gesperrt;
	
	//Kommunikationsklasse, welches an den Client übergeben werden kann.
	//Enthält alle Informationen über einen Benutzer.
	
	public ComBenutzer(){}
	
	public ComBenutzer(String Benutzername, String Passwort, int idOrgaEinheit, String OrgaEinheitBez, boolean Gesperrt){
		this.Benutzername = Benutzername;
		this.Passwort = Passwort;
		this.idOrgaEinheit = idOrgaEinheit;
		this.OrgaEinheitBez = OrgaEinheitBez;
		this.Gesperrt = Gesperrt;
	}


	public void setBenutzername(String benutzername) {
		Benutzername = benutzername;
	}


	public void setPasswort(String passwort) {
		Passwort = passwort;
	}


	public void setIdOrgaEinheit(int idOrgaEinheit) {
		this.idOrgaEinheit = idOrgaEinheit;
	}


	public void setOrgaEinheitBez(String orgaEinheitBez) {
		OrgaEinheitBez = orgaEinheitBez;
	}


	public void setGesperrt(boolean gesperrt) {
		Gesperrt = gesperrt;
	}


	public String getBenutzername() {
		return Benutzername;
	}


	public String getPasswort() {
		return Passwort;
	}


	public int getIdOrgaEinheit() {
		return idOrgaEinheit;
	}


	public String getOrgaEinheitBez() {
		return OrgaEinheitBez;
	}


	public boolean isGesperrt() {
		return Gesperrt;
	}

}
