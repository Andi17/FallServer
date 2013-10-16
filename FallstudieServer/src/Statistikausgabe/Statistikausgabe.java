package Statistikausgabe;

import java.util.ArrayList;
import java.util.List;

import Com.ComStatistik;
import Zugriffsschicht.Benutzer;
import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Statistik;
import Zugriffsschicht.Strichart;
import Zugriffsschicht.Zugriffschicht;

//Anforderung 4.2.9 Anonymität des Erfassers muss berücksichtigt werden.

public class Statistikausgabe {
	
	private Zugriffschicht dbZugriff;
	
	public Statistikausgabe (Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}
	
	public List<ComStatistik> getBereichsStatistik(String benutzername, int kalendarwoche, int jahr){
		List<ComStatistik> unsortiert = getStrichartStatistik(benutzername, kalendarwoche, jahr);
		List<ComStatistik> sortiert = new ArrayList<ComStatistik>();
		return null;
	}
	
	public List<ComStatistik> getStrichartStatistik (String benutzername, int kalendarwoche, int jahr){
		List<ComStatistik> rueckgabe = new ArrayList<ComStatistik>();
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
		for(int i=0; i<stricharten.size(); i++){
			List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
			rueckgabe.addAll(orgaEinheit.getStatistik(kalendarwoche, jahr, stricharten.get(i).getIdStrichart(), stricharten.get(i).getStrichbez(), 1, hilfsListe));
		}
		
		return rueckgabe;
	}

}
