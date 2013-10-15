package Administration;

import java.util.ArrayList;
import java.util.List;

import Com.ComBerechtigung;
import Com.ComStrichart;
import Zugriffsschicht.Berechtigung;
import Zugriffsschicht.Strichart;
import Zugriffsschicht.Zugriffschicht;

public class StrichArtVerwaltung {
	
private Zugriffschicht dbZugriff;
	
	
	public StrichArtVerwaltung(Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}
	
	public boolean strichArtHinzufuegen(String strichbezeichnung, boolean zustand){
		Strichart strichart = dbZugriff.neueStrichartErstellen(strichbezeichnung, zustand);
		if(strichart==null)return false;
		else return true;
	}
	
	public List<ComStrichart> getAlleStricharten(boolean nurAktive){
		List<Strichart> listeStrichart = dbZugriff.getAlleStricharten(nurAktive);
		List<ComStrichart> rueckgabe = new ArrayList<ComStrichart>();
		for (Strichart strichart : listeStrichart){
				rueckgabe.add(new ComStrichart(strichart.getIdStrichart(), strichart.getStrichbez(), strichart.getZustand()));
		}
		return rueckgabe;
	}
	
	public boolean strichArtBezeichnungAendern(String bezeichnungAlt, String bezeichnungNeu){
		return false;
	}

	public boolean gibtEsStrichelBezeichnung(String strichelBezeichnung) {
		return false;
	}

}
