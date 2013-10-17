package Com;

public class ComOrgaEinheit {
	private int idOrgaEinheit;
	private int UeberOrgaEinheit;
	private String OrgaEinheitBez;
	private String Leitername;
	private int idLeiterBerechtigung;
	private String LeiterBerechtigungBez;
	private int idMitarbeiterBerechtigung;
	private String MitarbeiterBerechtigungBez;
	private boolean zustand;
	private String OrgaEinheitTyp;

	public ComOrgaEinheit(){}
	
	public ComOrgaEinheit(int idOrgaEinheit, int UeberOrgaEinheit,
			String OrgaEinheitBez, String Leitername, int idLeiterBerechtigung,
			String LeiterBerechtigungBez, int idMitarbeiterBerechtigung,
			String MitarbeiterBerechtigungBez, boolean zustand, String OrgaEinheitTyp) {
		this.idOrgaEinheit = idOrgaEinheit;
		this.UeberOrgaEinheit = UeberOrgaEinheit;
		this.OrgaEinheitBez = OrgaEinheitBez;
		this.Leitername = Leitername;
		this.idLeiterBerechtigung = idLeiterBerechtigung;
		this.LeiterBerechtigungBez = LeiterBerechtigungBez;
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
		this.MitarbeiterBerechtigungBez = MitarbeiterBerechtigungBez;
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

	public String getLeiterBerechtigungBez() {
		return LeiterBerechtigungBez;
	}

	public int getIdMitarbeiterBerechtigung() {
		return idMitarbeiterBerechtigung;
	}

	public String getMitarbeiterBerechtigungBez() {
		return MitarbeiterBerechtigungBez;
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

	public void setLeiterBerechtigungBez(String leiterBerechtigungBez) {
		this.LeiterBerechtigungBez = leiterBerechtigungBez;
	}

	public void setIdMitarbeiterBerechtigung(int idMitarbeiterBerechtigung) {
		this.idMitarbeiterBerechtigung = idMitarbeiterBerechtigung;
	}

	public void setMitarbeiterBerechtigungBez(String mitarbeiterBerechtigungBez) {
		this.MitarbeiterBerechtigungBez = mitarbeiterBerechtigungBez;
	}

	public void setZustand(boolean zustand) {
		this.zustand = zustand;
	}

	public void setOrgaEinheitTyp(String orgaEinheitTyp) {
		OrgaEinheitTyp = orgaEinheitTyp;
	}
}
