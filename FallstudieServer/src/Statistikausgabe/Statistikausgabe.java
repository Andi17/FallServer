package Statistikausgabe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Com.ComStatistik;
import Com.ComStatistikNeu;
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
		List<ComStatistik> unsortiert = getStrichartStatistik(benutzername, kalendarwoche, jahr);
		List<ComStatistik> sortiert = new ArrayList<ComStatistik>();
		int aktuelleHierarchieStufe = 1;
		ComStatistik aktuelleOrgaEinheit = null;
		for(int x=0; x<unsortiert.size(); x++){
			if(unsortiert.get(x).getHierarchiestufe()==aktuelleHierarchieStufe){
				sortiert.add(unsortiert.get(x));
				aktuelleOrgaEinheit = unsortiert.get(x);
				unsortiert.remove(x);
			}
		}
		sortiert.addAll(getStatistikUnterEinheiten(aktuelleOrgaEinheit, unsortiert));
		return sortiert;
	}
	
	public List<ComStatistikNeu> getBereichsStatistikNeu(String benutzername, int kw, int jahr){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		List<ComStatistikNeu> statistikListe = new ArrayList<ComStatistikNeu>();
		return orgaEinheit.getStatistikAusDatenbankNeu(kw, jahr, 1, statistikListe);
//		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
//		statistikListe = orgaEinheit.getStatistikAusDatenbankNeu(kw, jahr, 1, statistikListe);
//		
//		for(int i=0; i<statistikListe.size(); i++){
//			
//		}
	}
	
	public List<ComStatistik> getBereichsStatistikJahr(String benutzername, int jahr){
		List<ComStatistik> unsortiert = getStrichartStatistikJahr(benutzername, jahr);
		List<ComStatistik> sortiert = new ArrayList<ComStatistik>();
		int aktuelleHierarchieStufe = 1;
		ComStatistik aktuelleOrgaEinheit = null;
		for(int x=0; x<unsortiert.size(); x++){
			if(unsortiert.get(x).getHierarchiestufe()==aktuelleHierarchieStufe){
				sortiert.add(unsortiert.get(x));
				aktuelleOrgaEinheit = unsortiert.get(x);
				unsortiert.remove(x);
			}
		}
		sortiert.addAll(getStatistikUnterEinheiten(aktuelleOrgaEinheit, unsortiert));
		return sortiert;
	}
	
	private List<ComStatistik> getStatistikUnterEinheiten(ComStatistik aktuelle, List<ComStatistik> unsortiert){
		List<Integer> unterOrgaEinheiten = aktuelle.getUnterOrgaEinheiten();
		List<ComStatistik> rueckgabe = new ArrayList<ComStatistik>();
		for(int i=0; i<unterOrgaEinheiten.size(); i++){
			for(int x=0; x<dbZugriff.getAlleStricharten(true).size(); x++){
				ComStatistik statistik = listeDurchsuchen(unsortiert, unterOrgaEinheiten.get(i));
				if(statistik != null){
					rueckgabe.add(statistik);
					if(x+1==dbZugriff.getAlleStricharten(true).size()){
						rueckgabe.addAll(getStatistikUnterEinheiten(statistik, unsortiert));
					}
				}
			}
			
		}
		return rueckgabe;
	}
	
	private ComStatistik listeDurchsuchen(List<ComStatistik> liste, int idOrgaEinheit){
		int i=0;
		boolean wiederholen = true;
		ComStatistik rueckgabe = null;
		while (i<liste.size() && wiederholen==true){
			if(liste.get(i).getIdOrgaEinheit()==idOrgaEinheit){
				rueckgabe = liste.get(i);
				liste.remove(i);
				wiederholen = false;
			}
			i++;
		}
		return rueckgabe;
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
