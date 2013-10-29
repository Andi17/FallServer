package administration;

import java.util.ArrayList;
import java.util.List;

import kommunikationsklassen.ComStrichart;

import zugriffsschicht.Strichart;
import zugriffsschicht.Zugriffschicht;


public class StrichArtVerwaltung {
	
private Zugriffschicht dbZugriff;
	
	
	public StrichArtVerwaltung(Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}
	
	//F�gt eine neue Strichart mit den �bergebenen Attributen hinzu.
	//Gibt true zur�ck wenn erfolgreich, ansonsten false.
	public boolean strichArtHinzufuegen(String strichbezeichnung, boolean zustand){
		if(!gibtEsStrichelBezeichnung(strichbezeichnung)){
			Strichart strichart = dbZugriff.neueStrichartErstellen(strichbezeichnung, zustand);
			if(strichart==null)return false;
			else return true;
		}
		else return false;
		
	}
	
	//Gibt eine Liste mit Kommunikationsobjekten, die jeweils eine Strichart darstellen, zur�ck.
	//ist nurAktive true werden nur die aktiven Stricharten zur�ckgegeben.
	public List<ComStrichart> getAlleStricharten(boolean nurAktive){
		List<Strichart> listeStrichart = dbZugriff.getAlleStricharten(nurAktive);
		List<ComStrichart> rueckgabe = new ArrayList<ComStrichart>();
		for (Strichart strichart : listeStrichart){
				rueckgabe.add(new ComStrichart(strichart.getIdStrichart(), strichart.getStrichbez(), strichart.getZustand()));
		}
		return rueckgabe;
	}
	
	//Gibt ein Kommunikationsobjekt zur�ck, dass die Werte von der Strichart mit der �bergebenen Bezeichnung enth�lt.
	//Gibt es keine Strichart mit der Bezeichnung wird null zur�ck gegeben.
	public ComStrichart getStrichart(String bezeichnung){
		Strichart strichart = dbZugriff.getStrichart(bezeichnung);
		if(strichart!=null){
			ComStrichart rueckgabe = new ComStrichart(strichart.getIdStrichart(), strichart.getStrichbez(), strichart.getZustand());
			return rueckgabe;
		}
		else return null;
	}
	
	//�ndert die Bezeichnung von der Strichart mit der Bezeichnung bezeichnungAlt zu bezeichnungNeu
	//Gibt true zur�ck, wenn es geklappt hat.
	public boolean strichArtBezeichnungAendern(String bezeichnungAlt, String bezeichnungNeu){
		if(gibtEsStrichelBezeichnung(bezeichnungNeu))return false;
		else{
			Strichart strichart = dbZugriff.getStrichart(bezeichnungAlt);
			return strichart.setStrichbez(bezeichnungNeu);
		}
	}

	//Gibt true zur�ck, wenn es schon eine Strichart mit der Bezeichnung strichArtBezeichnung gibt.
	public boolean gibtEsStrichelBezeichnung(String strichArtBezeichnung) {
		Strichart strichart = dbZugriff.getStrichart(strichArtBezeichnung);
		if(strichart == null) return false;
		else return true;
	}

	//�ndert den Zustand von der Strichart mit der Bezeichnung strichArtBezeichnung zu neuerZustand
	//Gibt true zur�ck, wenn es geklappt hat.
	public boolean strichArtZustandSetzen(String strichArtBezeichnung, boolean neuerZustand){
		if(gibtEsStrichelBezeichnung(strichArtBezeichnung))
			return dbZugriff.getStrichart(strichArtBezeichnung).setZustand(neuerZustand);
		else return false;
		
	}
}
