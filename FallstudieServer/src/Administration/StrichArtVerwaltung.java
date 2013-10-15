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
		if(gibtEsStrichelBezeichnung(bezeichnungNeu))return false;
		else{
			Strichart strichart = dbZugriff.getStrichart(bezeichnungAlt);
			return strichart.setStrichbez(bezeichnungNeu);
		}
	}

	public boolean gibtEsStrichelBezeichnung(String strichArtBezeichnung) {
		Strichart strichart = dbZugriff.getStrichart(strichArtBezeichnung);
		if(strichart == null) return false;
		else return true;
	}

	public boolean strichArtInaktivSetzen(String strichArtBezeichnung){
		if(gibtEsStrichelBezeichnung(strichArtBezeichnung))
			return dbZugriff.getStrichart(strichArtBezeichnung).setZustand(false);
		else return false;
		
	}
}
