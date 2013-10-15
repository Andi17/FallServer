package Webservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import jdbc.JdbcAccess;

import RightsManagement.Rechte;
import Administration.Benutzerverwaltung;
import Administration.Berechtigungsverwaltung;
import Administration.OrgaEinheitVerwaltung;
import Administration.StrichArtVerwaltung;
import Com.ComBenutzer;
import Com.ComBerechtigung;
import Com.ComOrgaEinheit;
import Com.ComStatistik;
import Com.ComStrichart;
import Optionen.Optionen;
import Statistikausgabe.Statistikausgabe;
import Stricheln.Stricheln;
import Zugriffsschicht.Zugriffschicht;

/* 
 * Aus dieser *.java-Datei wurden die *.class-Dateien mit folgender Anweisung generiert:
 * c:\eclipseWorkspace\WebServiceDemo>wsgen -cp bin -s src -d bin de.integrata.SimpleWS
 * Hinweis: wsgen ist nicht unbedingt nötig, bei Änderung des Interfaces von SimpleWS
 *          generiert der Compiler dynamisch die nötigen Klassen und das WSDL File
 */
/**
 * Diese Klasse realisiert einen einfachen Web-Service mit 4 Methoden - die WSDL
 * Datei kann über http://localhost:8888/WSExample/simple?wsdl angezeigt werden
 * - Änderungen werden erst nach Recompile und Neustart dieses Programms wirksam
 */
@WebService
public class Webservice {

	private Zugriffschicht dbZugriff;
	private Rechte rightsManagement;
	private Benutzerverwaltung benutzerVerwaltung;
	private Stricheln stricheln;
	private Statistikausgabe statistikausgabe;
	private OrgaEinheitVerwaltung orgaEinheitVerwaltung;
	private StrichArtVerwaltung strichArtVerwaltung;
	private Berechtigungsverwaltung berechtigungsverwaltung;

	public Webservice() {
		try {
			JdbcAccess jdbc = new JdbcAccess(Optionen.getJdbcurl(),
					Optionen.getJdbcuser(), Optionen.getJdbcpw());
			jdbc.connect();
			dbZugriff = new Zugriffschicht(jdbc);
			rightsManagement = new Rechte(dbZugriff);
			stricheln = new Stricheln(dbZugriff);
			benutzerVerwaltung = new Benutzerverwaltung(dbZugriff);
			statistikausgabe = new Statistikausgabe(dbZugriff);
			orgaEinheitVerwaltung = new OrgaEinheitVerwaltung(dbZugriff);
			strichArtVerwaltung = new StrichArtVerwaltung(dbZugriff);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Anforderung 4.2.4: Anmelden, bzw. Überprüfen des Benutzernamens und
	// Passwort.
	// Gibt true zurück wenn login geklappt hat, wahrscheinlich ändern wir es
	// noch zum char
	// dann kann der grund mit zurück gegeben werden.
	@WebMethod
	public boolean login(String benutzer, String passwort) {
		return rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer);
	}

	// Gibt eine Liste mit allen Benutzern zurück.
	@WebMethod
	public List<ComBenutzer> getBenutzer(String benutzer, String passwort) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.getAlleBenutzer();
		else
			return null;
	}

	// Methode nur für Admin. Anforderung 4.2.1: Erstellt neuen Benutzer.
	// gibt true zurück wenn alles geklappt hat.
	@WebMethod
	public boolean benutzerErstellen(String benutzer, String passwort,
			String benutzername, String neuerBenutzerPasswort, int idOE) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.benutzerErstellen(benutzername,
					neuerBenutzerPasswort, idOE);
		else
			return false;
	}

	// Anforderung 4.2.2: Löscht den Benutzer mit der entsprechenden ID aus der
	// Datenbank.
	@WebMethod
	public boolean benutzerLoeschen(String benutzer, String passwort,
			String zuLoeschenderBenutzer) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.benutzerLoeschen(zuLoeschenderBenutzer);
		else
			return false;
	}

	// Anforderung 4.2.3: Ändert den Benutzer mit der entsprechenden ID zu der
	// entsprechenden Organisationseinheit.
	@WebMethod
	public boolean benutzerOrgaEinheitAendern(String benutzer, String passwort,
			String benutzername, int idorgaEinheit) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.orgaEinheitAendern(benutzername,
					idorgaEinheit);
		else
			return false;
	}

	// Anforderung Arni: Aendert Benutzername.
	@WebMethod
	public boolean benutzernameAendern(String benutzer, String passwort,
			String betroffenerBenutzer, String neuerBenutzername) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.Benutzernameaendern(betroffenerBenutzer,
					neuerBenutzername);
		else
			return false;
	}

	// Anforderung 4.2.5: Setzt das Passwort zurück.
	@WebMethod
	public boolean gibtesBenutzerschon(String benutzer, String passwort,
			String neuerBenutzername) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.benutzerSchonVorhanden(neuerBenutzername);
		else
			return false;
	}

	// Anforderung 4.2.5: Setzt das Passwort zurück.
	@WebMethod
	public boolean neuesPasswortSetzen(String benutzer, String passwort,
			String betroffenerBenutzer, String neuesPasswort) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return benutzerVerwaltung.setPasswort(betroffenerBenutzer,
					neuesPasswort);
		else
			return false;
	}

	// Anforderung 4.2.6: Sperrt den Benutzer. Kein Rückgabewert.
	@WebMethod
	public void passwortSperren(String benutzername) {
		benutzerVerwaltung.passwortSperren(benutzername);
	}

	// Gibt eine Liste mit allen Organisationseinheiten zurück.
	@WebMethod
	public List<ComOrgaEinheit> getOrgaEinheiten(String benutzer, String passwort) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 101))
			return orgaEinheitVerwaltung.getAlleOrgaEinheiten();
		else
			return null;
	}

	// Organisationseinheit hinzufügen.
	@WebMethod
	public boolean OrgaEinheitErstellen(String benutzer, String passwort,
			int idUeberOrgaEinheit, String OrgaEinheitBez, String Leitername,
			int idLeiterBerechtigung, int idMitarbeiterBerechtigung) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return orgaEinheitVerwaltung.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername, idLeiterBerechtigung, Optionen.isInitialbelegungOrgaEinheitZustand(), idMitarbeiterBerechtigung);
		else
			return false;
	}

	//Organisationseinheit löschen.
	@WebMethod
	public boolean orgaEinheitLoeschen(String benutzer, String passwort, int idOrgaEinheit){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitLoeschen(idOrgaEinheit);
		else
			return false;
	}
	
	@WebMethod
	public boolean gibtEsOrgaEinheitSchon(String benutzer, String passwort,
			String orgaEinheitBezeichnung){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
		return orgaEinheitVerwaltung.gibtEsOrgaEinheit(orgaEinheitBezeichnung);
		else return false;
	}

	// Gibt eine Liste von allen möglichen Stricharten zurück.
	@WebMethod
	public List<ComStrichart> getStrichelArten(String benutzer, String passwort) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 4)) {
			return stricheln.getMoeglicheStricharten();
		} else
			return null;
	}

	// Anforderung 4.2.10: Eine neue Strichbezeichnung hinzufügen.
	@WebMethod
	public boolean neueStrichelart(String benutzer, String passwort,
			String strichbezeichnung) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 0))
			return strichArtVerwaltung.strichArtHinzufuegen(strichbezeichnung);
		else
			return false;
	}

	//Ändert die Bezeichnung von einer Strichelart.
	@WebMethod
	public boolean StrichelArtBezeichnungAendern(String benutzer,
			String passwort, String strichelbezeichnungAlt,
			String strichelbezeichnungNeu) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 0))
			return strichArtVerwaltung.strichArtBezeichnungAendern(
					strichelbezeichnungAlt, strichelbezeichnungNeu);
		else
			return false;
	}

	@WebMethod
	public boolean gibtEsStrichelBezeichnungSchon(String benutzer, String passwort,
			String strichelBezeichnungBezeichnung){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 1))
		return strichArtVerwaltung.gibtEsStrichelBezeichnung(strichelBezeichnungBezeichnung);
		else return false;
	}

	// alle Anforderungen aus 4.1 werden hierüber abgedeckt.
	// Speichert Striche entweder für letzte oder diese Woche in die Datenbank.
	// Gibt true zurück wenn erfolgreich.
	@WebMethod
	public boolean stricheln(String benutzer, String passwort, int strichart,
			int strichzahl, boolean aktuelleWoche) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 5)) {
			return stricheln.schreibeStrichInDatenbank(benutzer, strichart,
					aktuelleWoche, strichzahl);
		} else
			return false;
	}

	// Anforderung 4.4.2: Einsicht Gruppenleiter in seine Gruppe
	// Anforderung 4.4.4: Einsicht für bestimmten Zeitraum
	// Anforderung 4.4.5: Leiter einer Organisationseinheit kann Daten in der
	// Ebene unter ihm sehen.
	// Methode bis jetzt erst einmal nur Grundfunktionalität. Muss noch
	// erweitert werden oder neue Methode.
	@WebMethod
	public List<ComStatistik> getStatistik(String benutzer, String passwort,
			int kalendarwoche, int jahr) {
		/*
		 * TODO : Anstatt String[] als übergabewert die Klasse Statistik
		 * übergeben. bei mehreren Statistiken : List<Statistik> Der Server
		 * sucht die OE des Benutzers und gibt alle Statistiken dazu aus.
		 */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 3)) {
			return statistikausgabe.getStatistik(benutzer, kalendarwoche, jahr);
		}
		return null;
	}

	// Gibt ein Array aus char zurück, je nachdem welche Fenster angezeigt
	// werden sollen.
	@WebMethod
	public char[] anzeige(String benutzer, String passwort) {
		/*
		 * 'd'->Dash/Strichelfenster 'b'->Adminrechte 's'->Statistikfenster
		 */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, 2)) {
			return rightsManagement.erlaubteAnzeigen();
		} else
			return null;
	}
	
	@WebMethod
	public List<ComBerechtigung> getAlleBerechtigungen (String benutzer, String passwort){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer)) {
			return berechtigungsverwaltung.getAlleBerechtigung();
		} else
			return null;
	}

	// beendet den Access auf die Datenbank
	private void dbZugriffBeenden() {
		try {
			dbZugriff.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Den Service mittels in Java 6 enthaltenen HTTP-Server veröffentlichen
	 */
	public static void main(String[] args) {

		Webservice webservice = new Webservice();
		Endpoint endpoint = Endpoint.publish(Optionen.getWebserverURL(),
				webservice);
		// Hier wartet der Server
		System.out.println("web service server running ... press key to stop");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		endpoint.stop();
		webservice.dbZugriffBeenden();
		System.out.println("Web service Server stopped");
	}

}
