package RightsManagement;

import java.util.ArrayList;
import java.util.List;

import Zugriffsschicht.Benutzer;
import Zugriffsschicht.Zugriffschicht;

//Anforderung 4.2.8 wird hier realisiert.

public class Rechte {
	
	public static int nurAdmin = 1;
	public static int stricheln = 2;
	public static int nurEigeneGruppenEinsehbar = 3;
	public static int alleGruppenEinsehbar = 4;
	public static int alleBenutzer = 5;
	
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

	/*private int[] getRechtemoeglich(int Vorgang) {
		
		 * gibt alle Rechte zum Webservice zurück.
		 
		List<Berechtigung> BerechtigungListe = dbZugriff.getBerechtigungenZuWebmethode(Vorgang);
		
		int[] ret = null;
		if (BerechtigungListe!=null){
			ret = new int[BerechtigungListe.size()];
			int zaehler = 0;
			for(Berechtigung B : BerechtigungListe){
				ret[zaehler] = B.getIdBerechtigung();
				zaehler++;
			}
		}
		return ret;
	}*/

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
				if(LeiterRechte == Vorgang || MitarbeiterRechte == Vorgang)return true;
//				int[] MoeglicheRechte = this.getRechtemoeglich(Vorgang);
//				for(int i = 0; i < MoeglicheRechte.length;i++){
//					if (MoeglicheRechte[i] == MitarbeiterRechte || MoeglicheRechte[i] == LeiterRechte){
//						return true;
//					}					
//				}
			}	
		}
		return false;
	}
	
//	public List<ComBerechtigung> getAlleBerechtigung (){
//		List<Berechtigung> listeBerechtigung = dbZugriff.getAlleBerechtigungen();
//		List<ComBerechtigung> rueckgabe = new ArrayList<ComBerechtigung>();
//		for (Berechtigung berechtigung : listeBerechtigung){
//				rueckgabe.add(new ComBerechtigung(berechtigung.getIdBerechtigung(), berechtigung.getBerechtigungbez()));
//		}
//		return rueckgabe;
//	}

	public char[] erlaubteAnzeigen(String benutzername) {
		// TODO Auto-generated method stub
		List<Character> liste = new ArrayList<Character>();
		int LeiterRechte = dbZugriff.getBerechtigungzuLeitername(benutzername);
		int MitarbeiterRechte = dbZugriff.getBerechtigungzuMitarbeiter(benutzername);
		if(LeiterRechte == 1 || MitarbeiterRechte == 1){
			liste.add('a');
		}
		if(LeiterRechte == 2 || MitarbeiterRechte == 2){
			liste.add('d');
		}
		if(LeiterRechte == 3 || LeiterRechte == 4 || MitarbeiterRechte == 3|| MitarbeiterRechte == 4){
			liste.add('s');
		}
		char[] rueckgabe = new char[liste.size()];
		for(int i=0; i<liste.size(); i++){
			rueckgabe[i] = liste.get(i);
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
