package Com;

import java.util.List;

public class ComStatistikNeu {
	private String OrgaEinheitBez;
	private String OrgaEinheitTyp;
	private int Hierarchiestufe;
	private int KalenderWoche;
	private int Jahr;
	private List<String> StrichBez;
	private List<Integer> Strichzahl;

	public ComStatistikNeu(){}
	
	public ComStatistikNeu(String OrgaEinheitBez,
			int KalenderWoche, int Jahr, List<String> StrichBez,
			List<Integer> Strichzahl, int Hierarchiestufe, String OrgaEinheitTyp) {
		this.OrgaEinheitBez = OrgaEinheitBez;
		this.KalenderWoche = KalenderWoche;
		this.Jahr = Jahr;
		this.StrichBez = StrichBez;
		this.Strichzahl = Strichzahl;
		this.Hierarchiestufe = Hierarchiestufe;
		this.OrgaEinheitTyp = OrgaEinheitTyp;
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

	public List<String> getStrichBez() {
		return StrichBez;
	}

	public List<Integer> getStrichzahl() {
		return Strichzahl;
	}

	public int getHierarchiestufe() {
		return Hierarchiestufe;
	}
	
	public String getOrgaEinheitTyp() {
		return OrgaEinheitTyp;
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

	public void setStrichBez(List<String> strichBez) {
		StrichBez = strichBez;
	}

	public void setStrichzahl(List<Integer> strichzahl) {
		Strichzahl = strichzahl;
	}

	public void setHierarchiestufe(int hierarchiestufe) {
		Hierarchiestufe = hierarchiestufe;
	}

	public void setOrgaEinheitTyp(String orgaEinheitTyp) {
		OrgaEinheitTyp = orgaEinheitTyp;
	}

}
