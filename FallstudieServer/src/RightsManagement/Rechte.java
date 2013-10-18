package RightsManagement;

import java.util.ArrayList;
import java.util.List;

import Zugriffsschicht.Benutzer;
import Zugriffsschicht.Zugriffschicht;

//Anforderung 4.2.8 wird hier realisiert.

public class Rechte {
	
	public static int nurAdmin = 1;
	public static int stricheln = 2;
	public static int statistikSehen = 3;
	public static int alleBenutzer = 4;
	
	private Zugriffschicht dbZugriff;

	public Rechte(Zugriffschicht dbZugriff) {

		this.dbZugriff = dbZugriff;
	}

	private boolean login(String Benutzername, String Passwort) {
		/*
		 * Überprüfung ob die Logindaten übereinstimmen: Passt Passwort zu
		 * Benutzername.
		 */
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(Benutzername);
		if (benutzer!=null && benutzer.getPasswort().equals(Passwort) && !benutzer.isGesperrt()){
			return true;
		}
		return false;
	}

	public boolean vorgangMoeglich(String benutzername, String Passwort,
			int Vorgang) {
		/*
		 * überprüfung ob login gültig ist rechte suchen vergleichen ob Vorgang
		 * mit erhaltenen rechten möglich ist.
		 */
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

	//Gibt eine liste von chars zurück, a für Adminrechte, d für Strichelrechte und s für Statistikrechte
	public List<Character> erlaubteAnzeigen(String benutzername) {
		List<Character> rueckgabe = new ArrayList<Character>();
		int LeiterRechte = dbZugriff.getBerechtigungzuLeitername(benutzername);
		int MitarbeiterRechte = dbZugriff.getBerechtigungzuMitarbeiter(benutzername);
		if(LeiterRechte == 1 || MitarbeiterRechte == 1){
			rueckgabe.add('a');
		}
		if(LeiterRechte == 2 || MitarbeiterRechte == 2){
			rueckgabe.add('d');
		}
		if(LeiterRechte == 3 || LeiterRechte == 4 || MitarbeiterRechte == 3|| MitarbeiterRechte == 4){
			rueckgabe.add('s');
		}
		return rueckgabe;
	}
	
	//Gibt die Berechtigungsbezeichnung zu der passenden ID.
	public static String getRechtBezeichnung(int rechtID){
		switch(rechtID){
		case 1: return "Admin";
		case 2: return "Stricheln";
		case 3: return "NurEigeneGruppenEinsehbar";
		case 4: return "AlleGruppenEinsebar";
		default: return "Keine Berechtigung";
		}
	}

}
