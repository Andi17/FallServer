package kommunikationsklassen;

public class ComOrgaEinheit {
	private int idOrgaEinheit;
	private int idUeberOrgaEinheit;
	private String ueberOrgaEinheit;
	private String OrgaEinheitBez;
	private String Leitername;
	private int idLeiterBerechtigung;
	private int idMitarbeiterBerechtigung;
	private boolean zustand;
	private String OrgaEinheitTyp;
	
	//Kommunikationsklasse, welches an den Client �bergeben werden kann.
	//Enth�lt alle Informationen �ber eine Organisationseinheit.

	public ComOrgaEinheit(){}
	
	public ComOrgaEinheit(int idOrgaEinheit, int UeberOrgaEinheit, String ueberOrgaEinheit,
			String OrgaEinheitBez, String Leitername, int idLeiterBerechtigung,
			int idMitarbeiterBerechtigung, boolean zustand, String OrgaEinheitTyp) {
		this.idOrgaEinheit = idOrgaEinheit;
		this.idUeberOrgaEinheit = UeberOrgaEinheit;
		this.ueberOrgaEinheit = ueberOrgaEinheit;
		this.OrgaEinheitBez = OrgaEinheitBez;
		this.Leitername = Leitername;
		this.idLeiterBerechtigung = idLeiterBerechtigung;
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
		this.zustand = zustand;
		this.OrgaEinheitTyp = OrgaEinheitTyp;
	}

	public int getIdOrgaEinheit() {
		return idOrgaEinheit;
	}

	public int getIdUeberOrgaEinheit() {
		return idUeberOrgaEinheit;
	}

	public String getUeberOrgaEinheit() {
		return ueberOrgaEinheit;
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

	public int getIdMitarbeiterBerechtigung() {
		return idMitarbeiterBerechtigung;
	}

	public boolean isZustand() {
		return zustand;
	}

	public String getOrgaEinheitTyp() {
		return OrgaEinheitTyp;
	}

	public void setIdOrgaEinheit(int idOrgaEinheit) {
		this.idOrgaEinheit = idOrgaEinheit;
	}

	public void setIdUeberOrgaEinheit(int ueberOrgaEinheit) {
		this.idUeberOrgaEinheit = ueberOrgaEinheit;
	}

	public void setUeberOrgaEinheit(String ueberOrgaEinheit) {
		this.ueberOrgaEinheit = ueberOrgaEinheit;
	}

	public void setOrgaEinheitBez(String orgaEinheitBez) {
		this.OrgaEinheitBez = orgaEinheitBez;
	}

	public void setLeitername(String leitername) {
		this.Leitername = leitername;
	}

	public void setIdLeiterBerechtigung(int idLeiterBerechtigung) {
		this.idLeiterBerechtigung = idLeiterBerechtigung;
	}

	public void setIdMitarbeiterBerechtigung(int idMitarbeiterBerechtigung) {
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
	}

	public void setZustand(boolean zustand) {
		this.zustand = zustand;
	}

	public void setOrgaEinheitTyp(String orgaEinheitTyp) {
		OrgaEinheitTyp = orgaEinheitTyp;
	}
}
