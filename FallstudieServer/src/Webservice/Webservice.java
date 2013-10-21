package Webservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import jdbc.JdbcAccess;

import RightsManagement.Rechte;
import Administration.Benutzerverwaltung;
import Administration.OrgaEinheitVerwaltung;
import Administration.StrichArtVerwaltung;
import Com.ComBenutzer;
import Com.ComOrgaEinheit;
import Com.ComStatistik;
import Com.ComStrichart;
import Optionen.Optionen;
import Statistikausgabe.Statistikausgabe;
import Statistikerstellung.JedeWocheAusfuehren;
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

	public Webservice(Timer jedeWocheStatistikErstellen) {
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
			
		    Calendar date = Calendar.getInstance();
		    date.set(
		      Calendar.DAY_OF_WEEK,
		      Calendar.MONDAY
		    );
		    date.set(Calendar.HOUR, 1);
		    date.set(Calendar.MINUTE, 0);
		    date.set(Calendar.SECOND, 0);
		    date.set(Calendar.MILLISECOND, 0);
		    // Schedule to run every Sunday in midnight
		    jedeWocheStatistikErstellen.schedule(
		      new JedeWocheAusfuehren(dbZugriff),
		      date.getTime(),
		      1000 * 60 * 60 * 24 * 7
		    );
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Anforderung 4.2.4: Anmelden, bzw. Überprüfen des Benutzernamens und
	// Passwort.
	// Gibt 1 zurück für erfolgreich,
	// 2 für Benutzer gibt es nicht,
	// 3 für Benutzer ist gesperrt,
	// 4 für Passwort stimmt nicht.
	//Getestet, funzt.
	@WebMethod
	public int login(String benutzer, String passwort) {
		return rightsManagement.ersterLogin(benutzer, passwort);
	}

	// Gibt eine Liste mit allen Benutzern zurück.
	//Getestet, funzt.
	@WebMethod
	public List<ComBenutzer> getBenutzer(String benutzer, String passwort) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return benutzerVerwaltung.getAlleBenutzer();
		else
			return null;
	}
	
	@WebMethod
	public ComBenutzer getEinzelnenBenutzer(String benutzer, String passwort, String benutzername){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return benutzerVerwaltung.getBenutzer(benutzername);
		else
			return null;
	}

	// Methode nur für Admin. Anforderung 4.2.1: Erstellt neuen Benutzer.
	// gibt true zurück wenn alles geklappt hat.
	//Getestet, funzt.
	@WebMethod
	public boolean benutzerErstellen(String benutzer, String passwort,
			String benutzername, String neuerBenutzerPasswort, String orgaEinheit) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return benutzerVerwaltung.benutzerErstellen(benutzername,
					neuerBenutzerPasswort, orgaEinheit);
		else
			return false;
	}

	// Anforderung 4.2.2: Löscht den Benutzer mit der entsprechenden ID aus der
	// Datenbank.
	//Getestet, funzt.
	@WebMethod
	public boolean benutzerLoeschen(String benutzer, String passwort,
			String zuLoeschenderBenutzer) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return benutzerVerwaltung.benutzerLoeschen(zuLoeschenderBenutzer);
		else
			return false;
	}

	// Anforderung 4.2.3: Ändert den Benutzer mit der entsprechenden ID zu der
	// entsprechenden Organisationseinheit.
	//Getestet, funzt.
	@WebMethod
	public boolean benutzerOrgaEinheitAendern(String benutzer, String passwort,
			String benutzername, String orgaEinheitBez) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return benutzerVerwaltung.orgaEinheitAendern(benutzername,
					orgaEinheitBez);
		else
			return false;
	}

	// Aendert Benutzername.
	//Getestet, funzt.
	@WebMethod
	public boolean benutzernameAendern(String benutzer, String passwort,
			String betroffenerBenutzer, String neuerBenutzername) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return benutzerVerwaltung.Benutzernameaendern(betroffenerBenutzer,
					neuerBenutzername);
		else
			return false;
	}
	
	//Gibt true zurück wenn Benutzer schon Leiter ist. False wenn er noch kein Leiter ist.
	//Getestet, funzt.
	@WebMethod
	public boolean istBenutzerSchonLeiter(String benutzer, String passwort, String benutzername){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return benutzerVerwaltung.istBenutzerSchonLeiter(benutzername);
		else
			return false;
	}

	// Fragt, ob es benutzer schon gibt.
	//Getestet, funzt.
	@WebMethod
	public boolean gibtesBenutzerschon(String benutzer, String passwort,
			String neuerBenutzername) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return benutzerVerwaltung.benutzerSchonVorhanden(neuerBenutzername);
		else
			return false;
	}

	// Anforderung 4.2.5: Setzt das Passwort zurück.
	//Getestet, funzt.
	@WebMethod
	public boolean neuesPasswortSetzen(String benutzer, String passwort,
			String betroffenerBenutzer, String neuesPasswort) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return benutzerVerwaltung.setPasswort(betroffenerBenutzer,
					neuesPasswort);
		else
			return false;
	}

	// Anforderung 4.2.6: Sperrt den Benutzer. Kein Rückgabewert.
	//Getestet, funzt.
	@WebMethod
	public void passwortSperren(String benutzername) {
		benutzerVerwaltung.passwortGesperrtSetzen(benutzername, true);
	}
	
	//Anforderung 4.2.6: Entsperrt den Benutzer wieder. Gibt true zurück wenn geklappt.
	//Getestet, funzt.
	@WebMethod
	public boolean passwortEntsperren(String benutzer, String passwort, String benutzername){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return benutzerVerwaltung.passwortGesperrtSetzen(benutzername, false);
		else
			return false;
	}

	// Gibt eine Liste mit allen Organisationseinheiten zurück.
	//Wenn nurAktive true ist dann werden nur aktive ausgegeben.
	@WebMethod
	public List<ComOrgaEinheit> getOrgaEinheiten(String benutzer, String passwort, boolean nurAktive) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return orgaEinheitVerwaltung.getAlleOrgaEinheiten(nurAktive);
		else
			return null;
	}
	
	@WebMethod
	public ComOrgaEinheit getOrgaEinheitZuName(String benutzer, String passwort, String orgaEinheitBez){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
			return orgaEinheitVerwaltung.getOrgaEinheit(orgaEinheitBez);
		else
			return null;
	}

	// Organisationseinheit hinzufügen.
	//Geteste, funzt. Eventuell noch überprüfen ob es den Leitername auch wirklich gibt.
	//TODO: wenn es noch keinen leiter gibt!!
	@WebMethod
	public boolean OrgaEinheitErstellen(String benutzer, String passwort,
			String OrgaEinheitBez, String Leitername,
			String OrgaEinheitTyp, int idUeberOrgaEinheit) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return orgaEinheitVerwaltung.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername, Optionen.isInitialbelegungOrgaEinheitZustand(), OrgaEinheitTyp);
		else
			return false;
	}
	
	//Alle möglichen OrgaEinheitenTypen werden zurück gegeben.
	@WebMethod
	public List<String> getAlleMoeglichenOrgaEinheitTypen(String benutzer, String passwort){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin)){
			return orgaEinheitVerwaltung.getOrgaEinheitTypen();
		}	
		else return null;
	}
	
	@WebMethod
	public List<String> getAlleOrgaEinheitenBezeichnungenVomTyp(String benutzer, String passwort, String typ){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin)){
			return orgaEinheitVerwaltung.getAlleOrgaEinheitenBezeichnungenMitTyp(typ);
		}	
		else return null;
	}

	//Den Zustand der Organisationseinheit neu setzen.
	//Getetstet, funzt.
	@WebMethod
	public boolean orgaEinheitZustandAendern(String benutzer, String passwort, String OrgaEinheit, boolean neuerZustand){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitZustandAendern(OrgaEinheit, neuerZustand);
		else
			return false;
	}
	
	//Ändert den Leiter einer Organisationseinheit.
	//Getestet, funzt.
	@WebMethod
	public boolean orgaEinheitLeiterAendern(String benutzer, String passwort, String OrgaEinheit, String leitername){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitLeiterAendern(OrgaEinheit, leitername);
		else
			return false;
	}
	
	@WebMethod
	public boolean orgaEinheitBezeichnungAendern(String benutzer, String passwort, String OrgaEinheit, String neueBezeichnung){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitBezeichnungAendern(OrgaEinheit, neueBezeichnung);
		else
			return false;
	}
	
	@WebMethod
	public boolean orgaEinheitUeberOrgaEinheitAendern(String benutzer, String passwort, String OrgaEinheit, String neueUeberOrgaEinheit){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitUeberOrgaEinheitAendern(OrgaEinheit, neueUeberOrgaEinheit);
		else
			return false;
	}
	
	//Gibt true zurück wenn es die OrgaEinheit schon gibt. 
	//Getestet, funzt.
	@WebMethod
	public boolean gibtEsOrgaEinheitSchon(String benutzer, String passwort,
			String orgaEinheitBezeichnung){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
		return orgaEinheitVerwaltung.gibtEsOrgaEinheit(orgaEinheitBezeichnung);
		else return false;
	}

	// Gibt eine Liste von allen möglichen Stricharten zurück.
	//Ist nurAktive true werden nur die aktiven zurückgegeben.
	//Getestet, funzt.
	@WebMethod
	public List<ComStrichart> getStrichelArten(String benutzer, String passwort, boolean nurAktive) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer)) {
			return strichArtVerwaltung.getAlleStricharten(nurAktive);
		} else
			return null;
	}
	
	@WebMethod
	public ComStrichart getStrichelArt(String benutzer, String passwort, String bezeichnung){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer)) {
			return strichArtVerwaltung.getStrichart(bezeichnung);
		} else
			return null;
	}

	// Anforderung 4.2.10: Eine neue Strichbezeichnung hinzufügen.
	//Getestet, funzt.
	@WebMethod
	public boolean neueStrichelart(String benutzer, String passwort,
			String strichbezeichnung) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return strichArtVerwaltung.strichArtHinzufuegen(strichbezeichnung, Optionen.isinitialbelegungStrichartZustand());
		else
			return false;
	}

	//Ändert die Bezeichnung von einer Strichelart.
	//Getestet, funzt.
	@WebMethod
	public boolean StrichelArtBezeichnungAendern(String benutzer,
			String passwort, String strichelbezeichnungAlt,
			String strichelbezeichnungNeu) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return strichArtVerwaltung.strichArtBezeichnungAendern(
					strichelbezeichnungAlt, strichelbezeichnungNeu);
		else
			return false;
	}

	//Gibt true zurück wenn es die Strichelbezeichnung shcon gibt.
	//getestet, funzt.
	@WebMethod
	public boolean gibtEsStrichelBezeichnungSchon(String benutzer, String passwort,
			String strichArtBezeichnung){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer))
		return strichArtVerwaltung.gibtEsStrichelBezeichnung(strichArtBezeichnung);
		else return false;
	}
	
	//Setzt den Zustand der StrichelArt neu.
	//Getestet, funzt.
	@WebMethod
	public boolean strichelArtZustandAendern(String benutzer, String passwort,
			String strichArtBezeichnung, boolean neuerZustand){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.nurAdmin))
			return strichArtVerwaltung.strichArtZustandSetzen(strichArtBezeichnung, neuerZustand);
			else return false;
	}

	// alle Anforderungen aus 4.1 werden hierüber abgedeckt.
	// Speichert Striche entweder für letzte oder diese Woche in die Datenbank.
	// Gibt true zurück wenn erfolgreich.
	// Getestet, funzt.
	@WebMethod
	public boolean stricheln(String benutzer, String passwort, String strichart,
			int strichanzahl, boolean aktuelleWoche) {
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.stricheln)) {
			return stricheln.schreibeStricheInDatenbank(benutzer, strichart,
					strichanzahl, aktuelleWoche);
		} else
			return false;
	}

	// Anforderung 4.4.2: Einsicht Gruppenleiter in seine Gruppe
	// Anforderung 4.4.4: Einsicht für bestimmten Zeitraum
	// Anforderung 4.4.5: Leiter einer Organisationseinheit kann Daten in der
	// Ebene unter ihm sehen.
	// Getestet, funzt.
	//TODO: Jahresstatistik
	@WebMethod
	public List<ComStatistik> getBereichsStatistik(String benutzer, String passwort, int kalendarwoche, int jahr) { 
		/*Übergaben: int Jahr ist immer >0  int kw=0 --> Es soll die 
		 * Jahresstatistik geliefert werden  Liste wird nach Bereichen 
		 * sortiert ausgegeben */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.statistikSehen)) {
			return statistikausgabe.getBereichsStatistik(benutzer, kalendarwoche, jahr);
		}
		return null;
		}
	
	@WebMethod
	public List<ComStatistik> getBereichsStatistikJahr(String benutzer, String passwort, int jahr) { 
		/*Übergaben: int Jahr ist immer >0  int kw=0 --> Es soll die 
		 * Jahresstatistik geliefert werden  Liste wird nach Bereichen 
		 * sortiert ausgegeben */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.statistikSehen)) {
			return statistikausgabe.getBereichsStatistikJahr(benutzer, jahr);
		}
		return null;
		}
	
	//Getestet, funzt.
	//TODO: Jahresstatistik
	@WebMethod
	public List<ComStatistik> getStrichartStatistik(String benutzer, String passwort, int kalendarwoche, int jahr) {
		/* * Übergaben und Lieferung identisch zu gibStatistik (jetzt
		 *  gibBereichsStatistik) * Liste wird anders sortiert, (nach 
		 *  Kategorie übergeben) */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.statistikSehen)) {
			return statistikausgabe.getStrichartStatistik(benutzer, kalendarwoche, jahr);
		}
		return null; 
	}	
	
	@WebMethod
	public List<ComStatistik> getStrichartStatistikJahr(String benutzer, String passwort, int jahr) {
		/* * Übergaben und Lieferung identisch zu gibStatistik (jetzt
		 *  gibBereichsStatistik) * Liste wird anders sortiert, (nach 
		 *  Kategorie übergeben) */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.statistikSehen)) {
			return statistikausgabe.getStrichartStatistikJahr(benutzer, jahr);
		}
		return null; 
	}

	// Gibt ein Array aus char zurück, je nachdem welche Fenster angezeigt
	// werden sollen.
	//Getestet, funzt.
	@WebMethod
	public List<Integer> anzeige(String benutzer, String passwort) {
		/*
		 * 1->Dash/Strichelfenster 2->Statistikfenster 3->Adminrechte
		 */
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer)) {
			return rightsManagement.erlaubteAnzeigen(benutzer);
		} else
			return null;
	}
	
	/*//gibt alle Berechtigungen zurück. 
	//Getestet, funzt.
	@WebMethod
	public List<ComBerechtigung> getAlleBerechtigungen (String benutzer, String passwort){
		if (rightsManagement.vorgangMoeglich(benutzer, passwort, Rechte.alleBenutzer)) {
			return rightsManagement.getAlleBerechtigung();
		} else
			return null;
	}*/
	
	//Gibt das aktuelle Jahr zurück.
	//Getestet, funzt.
	@WebMethod
	public int getAktuellesJahr(){
		Calendar localCalendar = Calendar.getInstance();
		int jahr = localCalendar.get(Calendar.YEAR);
		return jahr;
	}
	
	//Gibt die aktuelle Kalendarwoche zurück.
	//Getestet, funzt.
	@WebMethod
	public int getAktuelleKalendarwoche(){
		Calendar localCalendar = Calendar.getInstance();
		int kalendarwoche = localCalendar.get(Calendar.WEEK_OF_YEAR);
		return kalendarwoche;
	}

	// beendet den Access auf die Datenbank
	public void dbZugriffBeenden() {
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
		Timer jedeWocheStatistikErstellen = new Timer();

		Webservice webservice = new Webservice(jedeWocheStatistikErstellen);
		Endpoint endpoint = Endpoint.publish(Optionen.getWebserverURL(),
				webservice);
		// Hier wartet der Server
		System.out.println("web service server running ... press key to stop");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		jedeWocheStatistikErstellen.cancel();
		endpoint.stop();
		webservice.dbZugriffBeenden();
		System.out.println("Web service Server stopped");
	}

}
