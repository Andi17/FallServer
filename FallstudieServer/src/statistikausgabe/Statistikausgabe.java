package statistikausgabe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kommunikationsklassen.ComStatistik;

import zugriffsschicht.Benutzer;
import zugriffsschicht.OrgaEinheit;
import zugriffsschicht.Strichart;
import zugriffsschicht.Zugriffschicht;


//Anforderung 4.2.9 Anonymit�t des Erfassers muss ber�cksichtigt werden.

public class Statistikausgabe {
	
	private Zugriffschicht dbZugriff;
	
	public Statistikausgabe (Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}
	
	//Gibt eine Liste von Kommunikationsobjekten aus, die jeweils Informationen �ber eine Woche
	//einer bestimmten Gruppe mit einer bestimmten Strichart enth�lt.
	//Die Liste ist nach OrgaEinheiten sortiert.
	public List<ComStatistik> getOrgaEinheitStatistik(String benutzername, int kalendarwoche, int jahr){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<ComStatistik> statistikListe = new ArrayList<ComStatistik>();
		Calendar localCalendar = Calendar.getInstance();
		int aktuellesJahr = localCalendar.get(Calendar.YEAR);
		int aktuelleKalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		//Ist die Statistik 2 Wochen oder �lter kann sie direkt aus der Datenbank gelesen werden.
		if(kalendarwoche+2<=aktuelleKalendarwoche && jahr <= aktuellesJahr)
		return orgaEinheit.getOrgaEinheitStatistikAusDatenbank(kalendarwoche, jahr, 1, statistikListe);
		//Andernfalls muss eine tempor�re Statistik erstellt werden.
		else return orgaEinheit.getTemporaereOrgaEinheitStatistik(kalendarwoche, aktuellesJahr, 1, statistikListe);
	}
	
	//Gibt eine Liste in Form von getBereichsStatistik zur�ck, lediglich enthalten hier die 
	//Kommunikationsobjekte Informationen �ber ein ganzes Jahr.
	public List<ComStatistik> getOrgaEinheitStatistikJahr(String benutzername, int jahr){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<ComStatistik> statistikListe = new ArrayList<ComStatistik>();
		return orgaEinheit.getJahresOrgaEinheitStatistikAusDatenbank(jahr, 1, statistikListe);
	}
	
	//Gibt eine Liste von Kommunikationsobjekten aus, die jeweils Informationen �ber eine Woche
	//einer bestimmten Gruppe mit einer bestimmten Strichart enth�lt.
	//Die Liste ist nach Stricharten sortiert.
	public List<ComStatistik> getStrichartStatistik (String benutzername, int kalendarwoche, int jahr){
		List<ComStatistik> rueckgabe = new ArrayList<ComStatistik>();
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
		Calendar localCalendar = Calendar.getInstance();
		int aktuellesJahr = localCalendar.get(Calendar.YEAR);
		int aktuelleKalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		//Ist die Statistik 2 Wochen oder �lter kann sie direkt aus der Datenbank gelesen werden.
		if(kalendarwoche+2<=aktuelleKalendarwoche && jahr <= aktuellesJahr){
			for(Strichart strichart : stricharten){
				List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
				rueckgabe.addAll(orgaEinheit.getStrichartStatistikAusDatenbank(kalendarwoche, jahr, strichart.getIdStrichart(), strichart.getStrichbez(), 1, hilfsListe));
			}
		}
		//Andernfalls muss eine tempor�re Statistik erstellt werden.
		else {
			for(Strichart strichart : stricharten){
				List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
				rueckgabe.addAll(orgaEinheit.getTemporaereStrichartStatistik(kalendarwoche, jahr, strichart.getIdStrichart(), strichart.getStrichbez(), 1, hilfsListe));
			}
		}
		return rueckgabe;
	}
	
	//Gibt eine Liste in Form von getStrichartStatistik zur�ck, lediglich enthalten hier die 
	//Kommunikationsobjekte Informationen �ber ein ganzes Jahr.
	public List<ComStatistik> getStrichartStatistikJahr (String benutzername, int jahr){
		List<ComStatistik> rueckgabe = new ArrayList<ComStatistik>();
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
		for(int i=0; i<stricharten.size(); i++){
			List<ComStatistik> hilfsListe = new ArrayList<ComStatistik>();
			rueckgabe.addAll(orgaEinheit.getJahresStrichartStatistikAusDatenbank(jahr, stricharten.get(i).getIdStrichart(), stricharten.get(i).getStrichbez(), 1, hilfsListe));
		}
		return rueckgabe;
	}

}
