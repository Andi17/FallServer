package Com;

import java.util.List;

public class ComStatistik {
	private int idOrgaEinheit;
	private String OrgaEinheitBez;
	private int KalenderWoche;
	private int Jahr;
	private String StrichBez;
	private int idStrichBez;
	private int Strichzahl;
	private int Hierarchiestufe;
	private String OrgaEinheitTyp;
	private List<Integer> unterOrgaEinheiten;
	
	//Kommunikationsklasse, welches an den Client übergeben werden kann.
	//Enthält alle Informationen über eine Statistik.

	public ComStatistik(){}
	
	public ComStatistik(int idOrgaEinheit, String OrgaEinheitBez,
			int KalenderWoche, int Jahr, String StrichBez, int idStrichBez,
			int Strichzahl, int Hierarchiestufe, String OrgaEinheitTyp ,List<Integer> unterOrgaEinheiten) {
		this.idOrgaEinheit = idOrgaEinheit;
		this.OrgaEinheitBez = OrgaEinheitBez;
		this.KalenderWoche = KalenderWoche;
		this.Jahr = Jahr;
		this.StrichBez = StrichBez;
		this.setIdStrichBez(idStrichBez);
		this.Strichzahl = Strichzahl;
		this.Hierarchiestufe = Hierarchiestufe;
		this.OrgaEinheitTyp = OrgaEinheitTyp;
		this.unterOrgaEinheiten= unterOrgaEinheiten;
	}

	public int getIdOrgaEinheit() {
		return idOrgaEinheit;
	}

	public String getOrgaEinheitBez() {
		return OrgaEinheitBez;
	}

	public int getKalenderWoche() {
		return KalenderWoche;
	}

	public int getJahr() {
		return Jahr;
	}

	public String getStrichBez() {
		return StrichBez;
	}

	public int getIdStrichBez() {
		return idStrichBez;
	}

	public int getStrichzahl() {
		return Strichzahl;
	}

	public int getHierarchiestufe() {
		return Hierarchiestufe;
	}

	public List<Integer> getUnterOrgaEinheiten() {
		return unterOrgaEinheiten;
	}

	public String getOrgaEinheitTyp() {
		return OrgaEinheitTyp;
	}

	public void setIdOrgaEinheit(int idOrgaEinheit) {
		this.idOrgaEinheit = idOrgaEinheit;
	}

	public void setOrgaEinheitBez(String orgaEinheitBez) {
		OrgaEinheitBez = orgaEinheitBez;
	}

	public void setKalenderWoche(int kalenderWoche) {
		KalenderWoche = kalenderWoche;
	}

	public void setJahr(int jahr) {
		Jahr = jahr;
	}

	public void setStrichBez(String strichBez) {
		StrichBez = strichBez;
	}

	public void setStrichzahl(int strichzahl) {
		Strichzahl = strichzahl;
	}

	public void setHierarchiestufe(int hierarchiestufe) {
		Hierarchiestufe = hierarchiestufe;
	}

	public void setIdStrichBez(int idStrichBez) {
		this.idStrichBez = idStrichBez;
	}

	public void setUnterOrgaEinheiten(List<Integer> unterOrgaEinheiten) {
		this.unterOrgaEinheiten = unterOrgaEinheiten;
	}

	public void setOrgaEinheitTyp(String orgaEinheitTyp) {
		OrgaEinheitTyp = orgaEinheitTyp;
	}

}
