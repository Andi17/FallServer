package Statistikausgabe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
		int hoechsteHierarchieStufe = 0;
		for(int x=0; x<unsortiert.size(); x++){
			if(unsortiert.get(x).getHierarchiestufe() > hoechsteHierarchieStufe){
				hoechsteHierarchieStufe = unsortiert.get(x).getHierarchiestufe();
			}
		}
		for(int x=1; x<=hoechsteHierarchieStufe; x++){
			int i=0;
			boolean wiederholen = true;
			while (i<unsortiert.size() && wiederholen==true){
				if(unsortiert.get(i).getHierarchiestufe()==x){
					sortiert.add(unsortiert.get(i));
					unsortiert.remove(i);
//					wiederholen = false;
				}
				if(i+1>=unsortiert.size())wiederholen=false;
				i++;
			}
		}
		return sortiert;
	}
	
//	private ComStatistik listeDurchsuchen(List<ComStatistik> liste){
//		for(int i=0; i<liste.size(); i++){
//			
//		}
//	}
	
	//Gibt eine Liste von comStatistik zurück.
	//Entweder aus Datenbank oder temporäre, wenn diese oder letzte woche abgefragt wird.
	public List<ComStatistik> getStrichartStatistik (String benutzername, int kalendarwoche, int jahr){
		List<ComStatistik> rueckgabe = new ArrayList<ComStatistik>();
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
		Calendar localCalendar = Calendar.getInstance();
		int aktuellesJahr = localCalendar.get(Calendar.YEAR);
		int aktuelleKalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		//TODO: Hier muss abgefragt werden, ob das jahr und die kw sich mehr als zwei wochen zurück befinden.
		if(kalendarwoche+2<=aktuelleKalendarwoche && jahr <= aktuellesJahr){
			for(int i=0; i<stricharten.size(); i++){
				List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
				rueckgabe.addAll(orgaEinheit.getStatistikAusDatenbank(kalendarwoche, jahr, stricharten.get(i).getIdStrichart(), stricharten.get(i).getStrichbez(), 1, hilfsListe));
			}
		}
		else {
			for(int i=0; i<stricharten.size(); i++){
				List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
				rueckgabe.addAll(orgaEinheit.getTemporaereStatistik(kalendarwoche, jahr, stricharten.get(i).getIdStrichart(), stricharten.get(i).getStrichbez(), 1, hilfsListe));
			}
		}
		return rueckgabe;
	}

}
