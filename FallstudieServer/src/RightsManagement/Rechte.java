package RightsManagement;

import java.util.ArrayList;
import java.util.List;

import Com.ComBerechtigung;
import Zugriffsschicht.Benutzer;
import Zugriffsschicht.Berechtigung;
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
		 * �berpr�fung ob die Logindaten �bereinstimmen: Passt Passwort zu
		 * Benutzername.
		 */
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(Benutzername);
		if (benutzer!=null && benutzer.getPasswort().equals(Passwort) && !benutzer.isGesperrt()){
			return true;
		}
		return false;
	}

	private int getRechtLeiter(String Benutzername) {
		/*
		 * Ermittelt s�mtliche Rechte f�r den Benutzer Achtung: ebenfalls rechte
		 * wenn man OE-Inhaber ist!
		 */
		Berechtigung BerechtigungLeiter = dbZugriff.getBerechtigungzuLeitername(Benutzername);
		if (BerechtigungLeiter!=null){
			return BerechtigungLeiter.getIdBerechtigung();
		}
		else return 0;
		
	}
	private int getRechtMitarbeiter(String Benutzername){
		Berechtigung BerechtigungMitarbeiter = dbZugriff.getBerechtigungzuMitarbeiter(Benutzername);
		if (BerechtigungMitarbeiter!=null){
			return BerechtigungMitarbeiter.getIdBerechtigung();
		}
		else return 0;
	}

	/*private int[] getRechtemoeglich(int Vorgang) {
		
		 * gibt alle Rechte zum Webservice zur�ck.
		 
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

	public boolean vorgangMoeglich(String Benutzername, String Passwort,
			int Vorgang) {
		/*
		 * �berpr�fung ob login g�ltig ist rechte suchen vergleichen ob Vorgang
		 * mit erhaltenen rechten m�glich ist.
		 */
		if (login(Benutzername, Passwort)) {
			if (Vorgang == alleBenutzer) {
				return true;
			}
			else {
				int LeiterRechte = this.getRechtLeiter(Benutzername);
				int MitarbeiterRechte = this.getRechtMitarbeiter(Benutzername);
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
	
	public List<ComBerechtigung> getAlleBerechtigung (){
		List<Berechtigung> listeBerechtigung = dbZugriff.getAlleBerechtigungen();
		List<ComBerechtigung> rueckgabe = new ArrayList<ComBerechtigung>();
		for (Berechtigung berechtigung : listeBerechtigung){
				rueckgabe.add(new ComBerechtigung(berechtigung.getIdBerechtigung(), berechtigung.getBerechtigungbez()));
		}
		return rueckgabe;
	}

	public char[] erlaubteAnzeigen(String benutzername) {
		// TODO Auto-generated method stub
		List<Character> liste = new ArrayList<Character>();
		int LeiterRechte = this.getRechtLeiter(benutzername);
		int MitarbeiterRechte = this.getRechtMitarbeiter(benutzername);
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

}
