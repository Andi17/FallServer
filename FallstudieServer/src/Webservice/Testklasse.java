package Webservice;

import java.io.IOException;
import java.util.List;

import javax.xml.ws.Endpoint;

import Com.ComBenutzer;
import Com.ComBerechtigung;
import Com.ComOrgaEinheit;
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
		
		
//		System.out.println(webservice.OrgaEinheitErstellen("admin", "aic", 0, "Test", "neuer", 4, 3));
//		webservice.passwortSperren("neuer");
//		webservice.passwortEntsperren("admin", "aic", "neuer");
		webservice.getAlleBerechtigungen("Admin", "aic");
		System.out.println(webservice.benutzerErstellen("admin", "aic", "hatKeineRehcte", "p", 0));
		List<ComOrgaEinheit> orgaEinheit = webservice.getOrgaEinheiten("neuer", "lala", true);
		if(orgaEinheit != null){
			for(int i=0; i<orgaEinheit.size(); i++){
				System.out.println("OEBezeichnung: " + orgaEinheit.get(i).getOrgaEinheitBez() + "\t OELeiter: " + orgaEinheit.get(i).getLeitername());
			}
		}
		List<ComBenutzer> benutzer = webservice.getBenutzer("neuer", "lala");
		if(benutzer != null){
			for(int i=0; i<benutzer.size(); i++){
				System.out.println("Benutzername:" + benutzer.get(i).getBenutzername() + "\tPasswort: " + benutzer.get(i).getPasswort() + "\tOrgaEinheit: " + benutzer.get(i).getOrgaEinheitBez());
			}
		}
		
		List<ComBerechtigung> berechtigung = webservice.getAlleBerechtigungen("Admin", "aic");
		if(berechtigung != null){
			for(int i=0; i<berechtigung.size(); i++){
				System.out.println("Berechtigung: " + berechtigung.get(i).getBerechtigungBez());
			}
		}
		
//		System.out.println(webservice.istBenutzerSchonLeiter("admin", "aic", "neuer"));
//		System.out.println(webservice.istBenutzerSchonLeiter("admin", "aic", "hatKeineRehcte"));
		
			
		
		
		endpoint.stop();
		webservice.dbZugriffBeenden();
		System.out.println("Web service Server stopped");
	}

}
