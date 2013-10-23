package Statistikausgabe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Com.ComStatistik;
import Zugriffsschicht.Benutzer;
import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Strichart;
import Zugriffsschicht.Zugriffschicht;

//Anforderung 4.2.9 Anonymität des Erfassers muss berücksichtigt werden.

public class Statistikausgabe {
	
	private Zugriffschicht dbZugriff;
	
	public Statistikausgabe (Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}
	
	public List<ComStatistik> getBereichsStatistik(String benutzername, int kalendarwoche, int jahr){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<ComStatistik> statistikListe = new ArrayList<ComStatistik>();
		Calendar localCalendar = Calendar.getInstance();
		int aktuellesJahr = localCalendar.get(Calendar.YEAR);
		int aktuelleKalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		//TODO: Hier muss abgefragt werden, ob das jahr und die kw sich mehr als zwei wochen zurück befinden.
		if(kalendarwoche+2<=aktuelleKalendarwoche && jahr <= aktuellesJahr)
		return orgaEinheit.getBereichsStatistikAusDatenbank(kalendarwoche, jahr, 1, statistikListe);
		else return orgaEinheit.getTemporaereBereichsStatistik(kalendarwoche, aktuellesJahr, 1, statistikListe);
	}
	
	public List<ComStatistik> getBereichsStatistikJahr(String benutzername, int jahr){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<ComStatistik> statistikListe = new ArrayList<ComStatistik>();
		return orgaEinheit.getJahresBereichsStatistikAusDatenbank(jahr, 1, statistikListe);
	}
	
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
			for(Strichart strichart : stricharten){
				List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
				rueckgabe.addAll(orgaEinheit.getStatistikAusDatenbank(kalendarwoche, jahr, strichart.getIdStrichart(), strichart.getStrichbez(), 1, hilfsListe));
			}
		}
		else {
			for(Strichart strichart : stricharten){
				List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
				rueckgabe.addAll(orgaEinheit.getTemporaereStatistik(kalendarwoche, jahr, strichart.getIdStrichart(), strichart.getStrichbez(), 1, hilfsListe));
			}
		}
		return rueckgabe;
	}
	
	public List<ComStatistik> getStrichartStatistikJahr (String benutzername, int jahr){
		List<ComStatistik> rueckgabe = new ArrayList<ComStatistik>();
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
		for(int i=0; i<stricharten.size(); i++){
			List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
			rueckgabe.addAll(orgaEinheit.getJahresStatistikAusDatenbank(jahr, stricharten.get(i).getIdStrichart(), stricharten.get(i).getStrichbez(), 1, hilfsListe));
		}
		return rueckgabe;
	}

}
