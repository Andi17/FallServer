package Statistikerstellung;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Strichart;
import Zugriffsschicht.Zugriffschicht;

public class Statistikerstellung extends TimerTask{
	
	private Zugriffschicht dbZugriff;
	
	public Statistikerstellung(Zugriffschicht dbZugriff){
		this.dbZugriff = dbZugriff;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Neue Statistiken wurden erstellt.");
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
				OrgaEinheit a = orgaEinheiten.get(i);
				int idStrichart = stricharten.get(x).getIdStrichart();
				int strichanzahl = a.getAlleStricheInWoche(kalendarwoche, jahr, idStrichart);
				if(strichanzahl!=0)
				dbZugriff.neueStatistik(a.getIdOrgaEinheit(), kalendarwoche , jahr, idStrichart, strichanzahl);
			
			}
		}
	}

}
