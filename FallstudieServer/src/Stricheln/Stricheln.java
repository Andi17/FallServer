package Stricheln;

import java.util.Calendar;
import java.util.Date;

import Zugriffsschicht.Arbeitsschritt;
import Zugriffsschicht.Zugriffschicht;

public class Stricheln {

	private Zugriffschicht dbZugriff;

	public Stricheln(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	//gibt true zurück wenn erfolgreich, speichert strich in datenbank
	public boolean schreibeStricheInDatenbank(String Benutzername, int idStrichart, 
			int strichanzahl, boolean aktuelleWoche) {
		Calendar localCalendar = Calendar.getInstance();
		Date datum = localCalendar.getTime();
		int jahr = localCalendar.get(Calendar.YEAR);
		int kalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		if(!aktuelleWoche) kalendarwoche--;
		if(kalendarwoche<=0){
			kalendarwoche = 52;
			jahr--;
		}
		int idOrgaEinheit = dbZugriff.getBenutzervonBenutzername(Benutzername).getAktuelleOE();
		Arbeitsschritt arbeitsschritt = dbZugriff.neuerArbeitsschritt(idOrgaEinheit, datum, idStrichart, strichanzahl, kalendarwoche, jahr);
		if(arbeitsschritt == null)return false;
		else return true;
	}

}
