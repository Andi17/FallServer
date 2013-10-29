package statistikerstellung;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import optionen.Optionen;

import zugriffsschicht.OrgaEinheit;
import zugriffsschicht.Strichart;
import zugriffsschicht.Zugriffschicht;


//Diese Klasse ist ein TimerTask, die run() Methode wird in einem bestimmten Intervall 
//immer wieder ausgeführt. Aktuelle Einstellung: Bei Start des Servers und dann immer Montags um 1:00.

public class JedeWocheAusfuehren extends TimerTask{
	
	private Zugriffschicht dbZugriff;
	
	public JedeWocheAusfuehren(Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}

	//Erstellt permanente Statistiken und löscht Arbeitsschritte
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Calendar localCalendar = Calendar.getInstance();
		int jahr = localCalendar.get(Calendar.YEAR);
		int kalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		kalendarwoche = kalendarwoche - 2;
		if(kalendarwoche<=0){
			jahr--;
			kalendarwoche = 52 + kalendarwoche;
		}
		List<OrgaEinheit> orgaEinheiten = dbZugriff.getOrgaEinheiten(false, true);
		List<Strichart> stricharten = dbZugriff.getAlleStricharten(false);
		for(int i=0; i<orgaEinheiten.size(); i++){
			for(int x=0; x<stricharten.size(); x++){
				OrgaEinheit orgaEinheit = orgaEinheiten.get(i);
				int idStrichart = stricharten.get(x).getIdStrichart();
				int strichanzahl = orgaEinheit.getAlleStricheInWoche(kalendarwoche, jahr, idStrichart);
				if(strichanzahl!=0)
				dbZugriff.neueStatistikErstellen(orgaEinheit.getIdOrgaEinheit(), kalendarwoche , jahr, idStrichart, strichanzahl);
			
			}
		}
		System.out.println("Neue Statistiken wurden erstellt.");
		
		Date aktuellesDatum = localCalendar.getTime();
		if(dbZugriff.arbeitsschritteLoeschen(aktuellesDatum)){
			System.out.println("Arbeitschritte, die älter sind als " + Optionen.getSpeicherdauer() + " Tage wurden gelöscht.");
		}
	}

}
