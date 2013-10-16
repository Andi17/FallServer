package Webservice;

import java.io.IOException;
import java.util.List;

import javax.xml.ws.Endpoint;

import Com.ComBenutzer;
import Com.ComBerechtigung;
import Com.ComOrgaEinheit;
import Com.ComStrichart;
import Optionen.Optionen;

public class Testklasse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Webservice webservice = new Webservice();
		Endpoint endpoint = Endpoint.publish(Optionen.getWebserverURL(),
				webservice);
		// Hier wartet der Server
		System.out.println("web service server running ... press key to stop");
		

//		System.out.println(webservice.OrgaEinheitErstellen("admin", "aic", 2, "Arbeitsgruppe5", null, 4, 2));
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter2", "1", 3);
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter3", "1", 4);
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter4", "1", 5);
//		webservice.benutzerErstellen("admin", "aic", "mitarbeiter5", "1", 6);
//		webservice.orgaEinheitZustandAendern("admin", "aic", 2, true);
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
		
		List<ComBerechtigung> berechtigung = webservice.getAlleBerechtigungen("Admin", "aic");
		if(berechtigung != null){
			for(int i=0; i<berechtigung.size(); i++){
				System.out.println("Berechtigung: " + berechtigung.get(i).getBerechtigungBez());
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
		
//		System.out.println(webservice.neueStrichelart("Admin", "aic", "Brief"));
//		System.out.println(webservice.neueStrichelart("Admin", "aic", "Dokument"));
//		System.out.println(webservice.neueStrichelart("Admin", "aic", "Tackern"));
//		webservice.passwortEntsperren("Admin", "aic", "mitarbeiter1");
//		System.out.println(webservice.stricheln("mitarbeiter2", "1", 3, 7, true));
//		System.out.println(webservice.stricheln("mitarbeiter3", "1", 3, 3, true));
//		System.out.println(webservice.stricheln("mitarbeiter4", "1", 2, 4, true));
//		System.out.println(webservice.stricheln("mitarbeiter5", "1", 1, 6, true));
//		System.out.println(webservice.stricheln("mitarbeiter2", "1", 2, 1, true));
//		System.out.println(webservice.stricheln("mitarbeiter3", "1", 1, 5, true));
//		System.out.println(webservice.stricheln("mitarbeiter4", "1", 3, 7, true));
//		System.out.println(webservice.stricheln("mitarbeiter5", "1", 1, 12, true));
//		System.out.println(webservice.stricheln("mitarbeiter2", "1", 3, 7, true));
		
		endpoint.stop();
		webservice.dbZugriffBeenden();
		System.out.println("Web service Server stopped");
	}

}
