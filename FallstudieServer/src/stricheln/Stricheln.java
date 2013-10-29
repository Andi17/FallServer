package stricheln;

import java.util.Calendar;
import java.util.Date;

import zugriffsschicht.Arbeitsschritt;
import zugriffsschicht.Zugriffschicht;


public class Stricheln {

	private Zugriffschicht dbZugriff;

	public Stricheln(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	//gibt true zurück wenn erfolgreich, speichert Arbeitsschritt in datenbank.
	public boolean schreibeStricheInDatenbank(String Benutzername, String strichart, 
			int strichanzahl, boolean aktuelleWoche) {
		int idStrichart = dbZugriff.getStrichart(strichart).getIdStrichart();
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
