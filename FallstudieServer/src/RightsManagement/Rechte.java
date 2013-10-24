package RightsManagement;

import java.util.ArrayList;
import java.util.List;

import Administration.Verschluesselung;
import Zugriffsschicht.Benutzer;
import Zugriffsschicht.Zugriffschicht;

//Anforderung 4.2.8 wird hier realisiert.
//Klasse zum Verwalten und Überprüfen von Rechten.

public class Rechte {
	
	public static int nurAdmin = 1;
	public static int stricheln = 2;
	public static int statistikSehen = 3;
	public static int alleBenutzer = 4;
	
	private Zugriffschicht dbZugriff;

	public Rechte(Zugriffschicht dbZugriff) {

		this.dbZugriff = dbZugriff;
	}
	
	//Methode für den ersten Login. gibt 1 zurück wenn der Benutzer sich anmelden darf.
	//2, falls es den Benutzer nicht in der DB gibt
	//3, falls der Benutzer gesperrt ist
	//4, falls das Passwort einfach nicht mit dem Passwort in der Datenbank übereinstimmt.
	public int ersterLogin(String benutzername, String passwort){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		
		passwort = Verschluesselung.verschluesseln(passwort);
		
		if (benutzer!=null && benutzer.getPasswort().equals(passwort) && !benutzer.isGesperrt()){
			return 1;
		}
		else if(benutzer==null){
			return 2;
		}
		else if(benutzer.isGesperrt()){
			return 3;
		}
		else {
			return 4;
		}
	}

	//Mehtode beim Überprüfen für die Rechte. Einfach nur zur Sicherheit noch einmal überprüfen,
	//dass Benutzername und Passwort auch wirklich übereinstimmen.
	//Gibt true zurück wenn sie übereinstimmen.
	private boolean login(String Benutzername, String Passwort) {
		
		Passwort = Verschluesselung.verschluesseln(Passwort);
		
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(Benutzername);
		if (benutzer!=null && benutzer.getPasswort().equals(Passwort) && !benutzer.isGesperrt()){
			return true;
		}
		return false;
	}

	//Gibt true zurück, wenn der Benutzer für das Ausführen der Webmethode 
	//(wird über den Integer vorgang übergeben) auch berechtig ist.
	public boolean vorgangMoeglich(String benutzername, String Passwort,
			int Vorgang) {
		if (login(benutzername, Passwort)) {
			if (Vorgang == alleBenutzer) {
				return true;
			}
			else {
				int LeiterRechte = dbZugriff.getBerechtigungzuLeitername(benutzername);
				int MitarbeiterRechte = dbZugriff.getBerechtigungzuMitarbeiter(benutzername);
				if(LeiterRechte == Vorgang || MitarbeiterRechte == Vorgang)
					return true;
			}	
		}
		return false;
	}

	//Gibt eine liste von chars zurück, 3 für Adminrechte, 1 für Strichelrechte 
	//und 2 für Statistikrechte.
	//Wird für den Client benötigt, damit er weiß welche Seiten er anzeigen darf.
	public List<Integer> erlaubteAnzeigen(String benutzername) {
		List<Integer> rueckgabe = new ArrayList<Integer>();
		int LeiterRechte = dbZugriff.getBerechtigungzuLeitername(benutzername);
		int MitarbeiterRechte = dbZugriff.getBerechtigungzuMitarbeiter(benutzername);
		if(LeiterRechte == 1 || MitarbeiterRechte == 1){
			rueckgabe.add(3);
		}
		if(LeiterRechte == 2 || MitarbeiterRechte == 2){
			rueckgabe.add(1);
		}
		if(LeiterRechte == 3 || LeiterRechte == 4 || MitarbeiterRechte == 3|| MitarbeiterRechte == 4){
			rueckgabe.add(2);
		}
		return rueckgabe;
	}

}
