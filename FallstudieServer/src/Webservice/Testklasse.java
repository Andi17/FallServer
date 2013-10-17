package Webservice;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import javax.xml.ws.Endpoint;

import Com.ComBenutzer;
import Com.ComOrgaEinheit;
import Com.ComStatistik;
import Com.ComStrichart;
import Optionen.Optionen;
import Statistikerstellung.Statistikerstellung;

public class Testklasse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Timer jedeWocheStatistikErstellen = new Timer();
		Webservice webservice = new Webservice(jedeWocheStatistikErstellen);
		Endpoint endpoint = Endpoint.publish(Optionen.getWebserverURL(),
				webservice);
		// Hier wartet der Server
		System.out.println("web service server running ... press key to stop");
		
		List<ComOrgaEinheit> orgaEinheit = webservice.getOrgaEinheiten("admin", "aic", true);
		if(orgaEinheit != null){
			for(int i=0; i<orgaEinheit.size(); i++){
				System.out.println("OEBezeichnung: " + orgaEinheit.get(i).getOrgaEinheitBez() + "\t OELeiter: " + orgaEinheit.get(i).getLeitername());
			}
			System.out.println("");
		}
		List<ComBenutzer> benutzer = webservice.getBenutzer("admin", "aic");
		if(benutzer != null){
			for(int i=0; i<benutzer.size(); i++){
				System.out.println("Benutzername:" + benutzer.get(i).getBenutzername() + "\tPasswort: " + benutzer.get(i).getPasswort() + "\tOrgaEinheit: " + benutzer.get(i).getOrgaEinheitBez());
			}
			System.out.println("");
		}
		
		List<String> OrgaEinheitTyp = webservice.getAlleMoeglichenOrgaEinheitTypen("Admin", "aic");
		if(OrgaEinheitTyp != null){
			for(int i=0; i<OrgaEinheitTyp.size(); i++){
				System.out.println("OrgaEinheitTyp: " + OrgaEinheitTyp.get(i));
			}
			System.out.println("");
		}
	
		List<ComStrichart> strichart = webservice.getStrichelArten("Admin", "aic", true);
		if(strichart != null){
			for(int i=0; i<strichart.size(); i++){
				System.out.println("Strichbezeichnung: " + strichart.get(i).getStrichBez());
			}
			System.out.println("");
		}
		
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter2", "1", 4);
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter3", "1", 4);
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter4", "1", 5);
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter5", "1", 5);

//		System.out.println(webservice.stricheln("mitarbeiter2", "1", 2, 7, true));
//		System.out.println(webservice.stricheln("mitarbeiter3", "1", 1, 3, true));
//		System.out.println(webservice.stricheln("mitarbeiter4", "1", 2, 4, true));
//		System.out.println(webservice.stricheln("mitarbeiter5", "1", 1, 6, true));
//		System.out.println(webservice.stricheln("mitarbeiter2", "1", 2, 1, true));
//		System.out.println(webservice.stricheln("mitarbeiter3", "1", 1, 5, true));
//		System.out.println(webservice.stricheln("mitarbeiter4", "1", 2, 7, true));
//		System.out.println(webservice.stricheln("mitarbeiter5", "1", 1, 12, true));
//		System.out.println(webservice.stricheln("mitarbeiter2", "1", 2, 7, true));
		webservice.benutzerErstellen("Admin", "aic", "Zentralbereichsleiter", "1", 2);
		
		List<ComStatistik> statistiken = webservice.getBereichsStatistik("Zentralbereichsleiter", "1", 42, 2013);
		if(statistiken != null)
		for (int i=0; i<statistiken.size(); i++){
			ComStatistik stat = statistiken.get(i);
			System.out.println("OEBez: " + stat.getOrgaEinheitBez() + "\t\t   StrichBez: " + stat.getStrichBez() + "\t Anzahl: " + stat.getStrichzahl() 
					+ " \t Hierarchieebene: " + stat.getHierarchiestufe());
		}
		jedeWocheStatistikErstellen.cancel();
		endpoint.stop();
		webservice.dbZugriffBeenden();
		System.out.println("Web service Server stopped");
	}

}