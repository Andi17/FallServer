package Com;

public class ComOrgaEinheit {
	private int idOrgaEinheit;
	private int UeberOrgaEinheit;
	private String OrgaEinheitBez;
	private String Leitername;
	private int idLeiterBerechtigung;
	private int idMitarbeiterBerechtigung;
	private boolean zustand;
	private String OrgaEinheitTyp;

	public ComOrgaEinheit(){}
	
	public ComOrgaEinheit(int idOrgaEinheit, int UeberOrgaEinheit,
			String OrgaEinheitBez, String Leitername, int idLeiterBerechtigung,
			int idMitarbeiterBerechtigung, boolean zustand, String OrgaEinheitTyp) {
		this.idOrgaEinheit = idOrgaEinheit;
		this.UeberOrgaEinheit = UeberOrgaEinheit;
		this.OrgaEinheitBez = OrgaEinheitBez;
		this.Leitername = Leitername;
		this.idLeiterBerechtigung = idLeiterBerechtigung;
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
		this.zustand = zustand;
		this.setOrgaEinheitTyp(OrgaEinheitTyp);
	}

	public int getIdOrgaEinheit() {
		return idOrgaEinheit;
	}

	public int getUeberOrgaEinheit() {
		return UeberOrgaEinheit;
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

	public void setUeberOrgaEinheit(int ueberOrgaEinheit) {
		this.UeberOrgaEinheit = ueberOrgaEinheit;
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
