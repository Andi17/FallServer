package Webservice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.xml.ws.Endpoint;

import administration.Benutzerverwaltung;
import administration.OrgaEinheitVerwaltung;
import administration.StrichArtVerwaltung;

import optionen.Optionen;

import rechtemanagement.Rechte;
import statistikausgabe.Statistikausgabe;
import statistikerstellung.JedeWocheAusfuehren;
import stricheln.Stricheln;
import zugriffsschicht.Zugriffschicht;

import jdbc.JdbcAccess;
import kommunikationsklassen.ComBenutzer;
import kommunikationsklassen.ComOrgaEinheit;
import kommunikationsklassen.ComStatistik;
import kommunikationsklassen.ComStrichart;


/* 
 * Aus dieser *.java-Datei wurden die *.class-Dateien mit folgender Anweisung generiert:
 * c:\eclipseWorkspace\WebServiceDemo>wsgen -cp bin -s src -d bin de.integrata.SimpleWS
 * Hinweis: wsgen ist nicht unbedingt n�tig, bei �nderung des Interfaces von SimpleWS
 *          generiert der Compiler dynamisch die n�tigen Klassen und das WSDL File
 */
/**
 * Diese Klasse realisiert einen einfachen Web-Service mit 4 Methoden - die WSDL
 * Datei kann �ber http://Domain:8888/Elastico/simple?wsdl angezeigt werden
 * - �nderungen werden erst nach Recompile und Neustart dieses Programms wirksam
 */
@WebService
public class Webservice {

	private Zugriffschicht dbZugriff;
	private Rechte rechteManagement;
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
			rechteManagement = new Rechte(dbZugriff);
			stricheln = new Stricheln(dbZugriff);
			benutzerVerwaltung = new Benutzerverwaltung(dbZugriff);
			statistikausgabe = new Statistikausgabe(dbZugriff);
			orgaEinheitVerwaltung = new OrgaEinheitVerwaltung(dbZugriff);
			strichArtVerwaltung = new StrichArtVerwaltung(dbZugriff);

			Calendar date = Calendar.getInstance();
			date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			date.set(Calendar.HOUR, 1);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			// Schedule to run every Sunday in midnight
			jedeWocheStatistikErstellen.schedule(new JedeWocheAusfuehren(
					dbZugriff), date.getTime(), 1000 * 60 * 60 * 24 * 7);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Anforderung 4.2.4: Anmelden, bzw. �berpr�fen des Benutzernamens und
	// Passwort.
	// Gibt 1 zur�ck f�r erfolgreich,
	// 2 f�r Benutzer gibt es nicht,
	// 3 f�r Benutzer ist gesperrt,
	// 4 f�r Passwort stimmt nicht.
	@WebMethod
	public int login(String benutzer, String passwort) {
		return rechteManagement.ersterLogin(benutzer, passwort);
	}

	// Gibt eine Liste mit allen Benutzern zur�ck.
	@WebMethod
	public List<ComBenutzer> getBenutzer(String benutzer, String passwort) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return benutzerVerwaltung.getAlleBenutzer();
		else
			return null;
	}

	//Gibt einen einzelnen Benutzer oder null zur�ck.
	@WebMethod
	public ComBenutzer getEinzelnenBenutzer(String benutzer, String passwort,
			String benutzername) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return benutzerVerwaltung.getBenutzer(benutzername);
		else
			return null;
	}

	// Methode nur f�r Admin. Anforderung 4.2.1: Erstellt neuen Benutzer.
	// gibt true zur�ck wenn alles geklappt hat.
	@WebMethod
	public boolean benutzerErstellen(String benutzer, String passwort,
			String benutzername, String neuerBenutzerPasswort,
			String orgaEinheit) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return benutzerVerwaltung.benutzerErstellen(benutzername,
					neuerBenutzerPasswort, orgaEinheit);
		else
			return false;
	}

	// Anforderung 4.2.2: L�scht den Benutzer mit dem entsprechenden Namen aus der Datenbank.
	@WebMethod
	public boolean benutzerLoeschen(String benutzer, String passwort,
			String zuLoeschenderBenutzer) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return benutzerVerwaltung.benutzerLoeschen(zuLoeschenderBenutzer);
		else
			return false;
	}

	// Anforderung 4.2.3: �ndert die OrgaEinheit des Benutzers zu der OrgaEinheit mit der Bezeichnung orgaEinheitBez.
	// Getestet, funzt.
	@WebMethod
	public boolean benutzerOrgaEinheitAendern(String benutzer, String passwort,
			String benutzername, String orgaEinheitBez) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return benutzerVerwaltung.orgaEinheitAendern(benutzername,
					orgaEinheitBez);
		else
			return false;
	}

	// Aendert Benutzername.
	@WebMethod
	public boolean benutzernameAendern(String benutzer, String passwort,
			String betroffenerBenutzer, String neuerBenutzername) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return benutzerVerwaltung.Benutzernameaendern(betroffenerBenutzer,
					neuerBenutzername);
		else
			return false;
	}

	//gibt "Nein" zur�ck, wenn der Benutzer noch kein Leiter ist.
	//Ansonsten wird der Name der Einheit, wo der Benutzer Leiter ist, zur�ck gegeben.
	@WebMethod
	public String istBenutzerSchonLeiter(String benutzer, String passwort,
			String benutzername) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return benutzerVerwaltung.istBenutzerSchonLeiter(benutzername);
		else
			return "Keine Rechte";
	}

	// Gibt true zur�ck wenn es schon einen Benutzer mit dem Namen neuerBenutzername gibt.
	@WebMethod
	public boolean gibtesBenutzerschon(String benutzer, String passwort,
			String neuerBenutzername) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return benutzerVerwaltung.benutzerSchonVorhanden(neuerBenutzername);
		else
			return false;
	}

	// Anforderung 4.2.5: Setzt das Passwort zur�ck.
	@WebMethod
	public boolean neuesPasswortSetzen(String benutzer, String passwort,
			String betroffenerBenutzer, String neuesPasswort) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return benutzerVerwaltung.setPasswort(betroffenerBenutzer,
					neuesPasswort);
		else
			return false;
	}

	// Anforderung 4.2.6: Sperrt den Benutzer. Kein R�ckgabewert.
	@WebMethod
	public void passwortSperren(String benutzername) {
		benutzerVerwaltung.passwortGesperrtSetzen(benutzername, true);
	}

	// Anforderung 4.2.6: Entsperrt den Benutzer wieder. Gibt true zur�ck wenn es
	// geklappt hat.
	@WebMethod
	public boolean passwortEntsperren(String benutzer, String passwort,
			String benutzername) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return benutzerVerwaltung.passwortGesperrtSetzen(benutzername,
					false);
		else
			return false;
	}

	// Gibt eine Liste mit allen Organisationseinheiten zur�ck.
	// Wenn nurAktive true ist dann werden nur aktive ausgegeben.
	@WebMethod
	public List<ComOrgaEinheit> getOrgaEinheiten(String benutzer,
			String passwort, boolean nurAktive) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return orgaEinheitVerwaltung.getAlleOrgaEinheiten(nurAktive);
		else
			return null;
	}

	//Gibt eine einzelne Organisationseinheit mit der Bezeichnung orgaEinheitBez zur�ck.
	//Null wenn es keine mit der Bezeichnung gibt.
	@WebMethod
	public ComOrgaEinheit getOrgaEinheitZuName(String benutzer,
			String passwort, String orgaEinheitBez) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return orgaEinheitVerwaltung.getOrgaEinheit(orgaEinheitBez);
		else
			return null;
	}

	// Organisationseinheit mit den �bergebenen Werten hinzuf�gen.
	@WebMethod
	public boolean OrgaEinheitErstellen(String benutzer, String passwort,
			String OrgaEinheitBez, String Leitername, String OrgaEinheitTyp,
			int idUeberOrgaEinheit) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return orgaEinheitVerwaltung.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername,
					Optionen.isInitialbelegungOrgaEinheitZustand(),
					OrgaEinheitTyp);
		else
			return false;
	}

	// Alle m�glichen OrgaEinheitenTypen werden in einer Liste als Strings zur�ck gegeben.
	@WebMethod
	public List<String> getAlleMoeglichenOrgaEinheitTypen(String benutzer,
			String passwort) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin)) {
			return orgaEinheitVerwaltung.getOrgaEinheitTypen();
		} else
			return null;
	}

	//Alle Bezeichnungen von OrgaEinheiten mit dem �bergebenden Typ werden in einer Liste als Strings zur�ck.
	@WebMethod
	public List<String> getAlleOrgaEinheitenBezeichnungenVomTyp(
			String benutzer, String passwort, String typ) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin)) {
			return orgaEinheitVerwaltung
					.getAlleOrgaEinheitenBezeichnungenMitTyp(typ);
		} else
			return null;
	}

	// Den Zustand der Organisationseinheit neu setzen.
	@WebMethod
	public boolean orgaEinheitZustandAendern(String benutzer, String passwort,
			String OrgaEinheit, boolean neuerZustand) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitZustandAendern(OrgaEinheit,
					neuerZustand);
		else
			return false;
	}

	// �ndert den Leiter einer Organisationseinheit.
	@WebMethod
	public boolean orgaEinheitLeiterAendern(String benutzer, String passwort,
			String orgaEinheit, String leitername) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitLeiterAendern(orgaEinheit,
					leitername);
		else
			return false;
	}

	//�ndert die Bezeichnung einer Organisationseinheit.
	@WebMethod
	public boolean orgaEinheitBezeichnungAendern(String benutzer,
			String passwort, String OrgaEinheit, String neueBezeichnung) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitBezeichnungAendern(
					OrgaEinheit, neueBezeichnung);
		else
			return false;
	}

	//�ndert die �bergeordnete Einheit der OrgaEinheit mit der Bezeichnung OrgaEinheit zu der �bergebenen.
	@WebMethod
	public boolean orgaEinheitUeberOrgaEinheitAendern(String benutzer,
			String passwort, String OrgaEinheit, String neueUeberOrgaEinheit) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return orgaEinheitVerwaltung.OrgaEinheitUeberOrgaEinheitAendern(
					OrgaEinheit, neueUeberOrgaEinheit);
		else
			return false;
	}

	// Gibt true zur�ck wenn es die OrgaEinheit schon gibt.
	@WebMethod
	public boolean gibtEsOrgaEinheitSchon(String benutzer, String passwort,
			String orgaEinheitBezeichnung) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return orgaEinheitVerwaltung
					.gibtEsOrgaEinheit(orgaEinheitBezeichnung);
		else
			return false;
	}

	// Gibt eine Liste von allen m�glichen Stricharten zur�ck.
	// Ist nurAktive true werden nur die aktiven zur�ckgegeben.
	@WebMethod
	public List<ComStrichart> getStrichelArten(String benutzer,
			String passwort, boolean nurAktive) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer)) {
			return strichArtVerwaltung.getAlleStricharten(nurAktive);
		} else
			return null;
	}

	//Gibt ein Kommunikationsobjekt zur�ck, dass die Werte von der Strichart mit der �bergebenen Bezeichnung enth�lt.
	//Gibt es keine Strichart mit der Bezeichnung wird null zur�ck gegeben.
	@WebMethod
	public ComStrichart getStrichelArt(String benutzer, String passwort,
			String bezeichnung) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer)) {
			return strichArtVerwaltung.getStrichart(bezeichnung);
		} else
			return null;
	}

	// Anforderung 4.2.10: Eine neue Strichbezeichnung hinzuf�gen.
	@WebMethod
	public boolean neueStrichelart(String benutzer, String passwort,
			String strichbezeichnung) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return strichArtVerwaltung.strichArtHinzufuegen(strichbezeichnung,
					Optionen.isinitialbelegungStrichartZustand());
		else
			return false;
	}

	// �ndert die Bezeichnung von einer Strichelart.
	@WebMethod
	public boolean StrichelArtBezeichnungAendern(String benutzer,
			String passwort, String strichelbezeichnungAlt,
			String strichelbezeichnungNeu) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return strichArtVerwaltung.strichArtBezeichnungAendern(
					strichelbezeichnungAlt, strichelbezeichnungNeu);
		else
			return false;
	}

	// Gibt true zur�ck wenn es die Strichelbezeichnung shcon gibt.
	@WebMethod
	public boolean gibtEsStrichelBezeichnungSchon(String benutzer,
			String passwort, String strichArtBezeichnung) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer))
			return strichArtVerwaltung
					.gibtEsStrichelBezeichnung(strichArtBezeichnung);
		else
			return false;
	}

	// Setzt den Zustand der StrichelArt neu.
	@WebMethod
	public boolean strichelArtZustandAendern(String benutzer, String passwort,
			String strichArtBezeichnung, boolean neuerZustand) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.nurAdmin))
			return strichArtVerwaltung.strichArtZustandSetzen(
					strichArtBezeichnung, neuerZustand);
		else
			return false;
	}

	// alle Anforderungen aus 4.1 werden hier�ber abgedeckt.
	// Speichert Striche entweder f�r letzte oder diese Woche in die Datenbank.
	// Gibt true zur�ck wenn erfolgreich.
	@WebMethod
	public boolean stricheln(String benutzer, String passwort,
			String strichart, int strichanzahl, boolean aktuelleWoche) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.stricheln)) {
			return stricheln.schreibeStricheInDatenbank(benutzer, strichart,
					strichanzahl, aktuelleWoche);
		} else
			return false;
	}

	// Anforderung 4.4.2: Einsicht Gruppenleiter in seine Gruppe
	// Anforderung 4.4.4: Einsicht f�r bestimmten Zeitraum
	// Anforderung 4.4.5: Leiter einer Organisationseinheit kann Daten in der
	// Ebene unter ihm sehen.
	//Gibt eine Liste von Kommunikationsobjekten aus, die jeweils Informationen �ber eine Woche
	//einer bestimmten Gruppe mit einer bestimmten Strichart enth�lt.
	//Die Liste ist nach OrgaEinheiten sortiert.
	@WebMethod
	public List<ComStatistik> getBereichsStatistik(String benutzer,
			String passwort, int kalendarwoche, int jahr) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.statistikSehen)) {
			return statistikausgabe.getOrgaEinheitStatistik(benutzer,
					kalendarwoche, jahr);
		}
		return null;
	}

	//Gibt eine Liste in Form von getBereichsStatistik zur�ck, lediglich enthalten hier die 
	//Kommunikationsobjekte Informationen �ber ein ganzes Jahr.
	@WebMethod
	public List<ComStatistik> getBereichsStatistikJahr(String benutzer,
			String passwort, int jahr) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.statistikSehen)) {
			return statistikausgabe.getOrgaEinheitStatistikJahr(benutzer, jahr);
		}
		return null;
	}

	//Gibt eine Liste von Kommunikationsobjekten aus, die jeweils Informationen �ber eine Woche
	//einer bestimmten Gruppe mit einer bestimmten Strichart enth�lt.
	//Die Liste ist nach Stricharten sortiert.
	@WebMethod
	public List<ComStatistik> getStrichartStatistik(String benutzer,
			String passwort, int kalendarwoche, int jahr) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.statistikSehen)) {
			return statistikausgabe.getStrichartStatistik(benutzer,
					kalendarwoche, jahr);
		}
		return null;
	}

	//Gibt eine Liste in Form von getStrichartStatistik zur�ck, lediglich enthalten hier die 
	//Kommunikationsobjekte Informationen �ber ein ganzes Jahr.
	@WebMethod
	public List<ComStatistik> getStrichartStatistikJahr(String benutzer,
			String passwort, int jahr) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.statistikSehen)) {
			return statistikausgabe.getStrichartStatistikJahr(benutzer, jahr);
		}
		return null;
	}

	// Gibt ein Array aus Integer zur�ck, je nachdem welche Fenster angezeigt
	// werden sollen. In der Liste steht eine 1 zur�ck wenn der Benutzer das Strichelfenster sehen darf.
	// eine 2, falls er das Statistikfenster sehen darf
	// eine 3, falls er das Adminfenster sehen darf.
	@WebMethod
	public List<Integer> anzeige(String benutzer, String passwort) {
		if (rechteManagement.vorgangMoeglich(benutzer, passwort,
				Rechte.alleBenutzer)) {
			return rechteManagement.erlaubteAnzeigen(benutzer);
		} else
			return null;
	}

	// Gibt das aktuelle Jahr zur�ck.
	@WebMethod
	public int getAktuellesJahr() {
		Calendar localCalendar = Calendar.getInstance();
		int jahr = localCalendar.get(Calendar.YEAR);
		return jahr;
	}

	// Gibt die aktuelle Kalendarwoche zur�ck.
	@WebMethod
	public int getAktuelleKalendarwoche() {
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
	 * Den Service mittels in Java 6 enthaltenen HTTP-Server ver�ffentlichen
	 */
	public static void main(String[] args) {
		final Timer jedeWocheStatistikErstellen = new Timer();

		final Webservice webservice = new Webservice(
				jedeWocheStatistikErstellen);
		final Endpoint endpoint = Endpoint.publish(Optionen.getWebserverURL(),
				webservice);
		// Hier wartet der Server
		JFrame fenster = new JFrame("Webserver");
		fenster.setBounds(100, 100, 250, 100);
		JButton beenden = new JButton("Stop");
		fenster.setVisible(true);
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.getContentPane().add(beenden);
		beenden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				jedeWocheStatistikErstellen.cancel();
				endpoint.stop();
				webservice.dbZugriffBeenden();
				System.exit(0);
			}

		});
	}

}
