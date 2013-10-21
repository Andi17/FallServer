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
	
	public int ersterLogin(String benutzername, String passwort){
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		
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

	private boolean login(String Benutzername, String Passwort) {
		/*
		 * �berpr�fung ob die Logindaten �bereinstimmen: Passt Passwort zu
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
		 * �berpr�fung ob login g�ltig ist rechte suchen vergleichen ob Vorgang
		 * mit erhaltenen rechten m�glich ist.
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

	//Gibt eine liste von chars zur�ck, 3 f�r Adminrechte, 1 f�r Strichelrechte und 2 f�r Statistikrechte
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
