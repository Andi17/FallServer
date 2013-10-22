package Administration;

import java.util.ArrayList;
import java.util.List;

import Com.ComBenutzer;
import Optionen.Optionen;
import Zugriffsschicht.Benutzer;
import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Zugriffschicht;

public class Benutzerverwaltung {

	private Zugriffschicht dbZugriff;

	public Benutzerverwaltung(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	// Erstellt einen neuen Benutzer und gibt einen boolschen Wert zurück,
	// welcher darstellt, ob der Benutzer erfolgreich angelegt wurde (true) oder
	// nicht (false)
	public boolean benutzerErstellen(String benutzername, String passwort,
			String orgaEinheit) {
		Benutzer neu = null;
		if (!benutzerSchonVorhanden(benutzername)) {
			neu = dbZugriff.neuerBenutzerErstellen(benutzername, passwort,
					orgaEinheit, Optionen.isInitialbelegungbenutzergesperrt());
		}
		if (neu == null) {
			return false;
		} else {
			return true;
		}
	}

	// Holt sich den entsprechenden Benutzer aus der Zugriffsschicht.
	// gibt null zurück wenn kein Benutzer mit dem Benutzernamen existiert.
	public ComBenutzer getBenutzer(String benutzername) {
		Benutzer benutzer = dbZugriff
				.getBenutzervonBenutzername(benutzername);
		OrgaEinheit orga = dbZugriff.getOrgaEinheitZuidOrgaEinheit(benutzer.getAktuelleOE());
		if(benutzer != null) {
			if(orga != null)
			return new ComBenutzer(benutzer.getBenutzername(), benutzer.getPasswort(), benutzer.getAktuelleOE(), orga.getOrgaEinheitBez(), benutzer.isGesperrt());
			else return new ComBenutzer(benutzer.getBenutzername(), benutzer.getPasswort(), benutzer.getAktuelleOE(), "Keine aktuelle Einheit", benutzer.isGesperrt());
		}
		else return null;
	}

	// Rückgabe der verschiedenen Benutzer in einer Liste.
	public List<ComBenutzer> getAlleBenutzer() {
		List<Benutzer> alleBenutzerListe = dbZugriff.getAlleBenutzer();
		List<ComBenutzer> rueckgabe = new ArrayList<ComBenutzer>();
		for (Benutzer benutzer : alleBenutzerListe) {
			rueckgabe.add(new ComBenutzer(benutzer.getBenutzername(), benutzer
					.getPasswort(), benutzer.getAktuelleOE(), benutzer
					.getOrgaEinheitBezeichnung(), benutzer.isGesperrt()));
		}
		return rueckgabe;
	}

	// Löscht den Benuttzer mit der entsprechenden ID.
	public boolean benutzerLoeschen(String benutzername) {
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		if(benutzer!=null) return benutzer.loeschen();
		else return false;
	}

	// Ändert die OrgaEinheit. Muss aus dem String die entsprechende ID
	// herrauslesen.
	public boolean orgaEinheitAendern(String betroffenerBenutzer,
			String orgaEinheitBez) {
		Benutzer benutzer = dbZugriff
				.getBenutzervonBenutzername(betroffenerBenutzer);
		OrgaEinheit orga = dbZugriff.getOrgaEinheitvonBezeichnung(orgaEinheitBez);
		if(benutzer!=null) return benutzer.setidOrgaEinheit(orga.getIdOrgaEinheit());
		else return false;
	}

	// setzt das Passwort des Benutzers mit der entsprechenden benutzerID auf
	// das neue Passwort
	public boolean setPasswort(String betroffenerBenutzer, String neuesPasswort) {
		Benutzer benutzer = dbZugriff
				.getBenutzervonBenutzername(betroffenerBenutzer);
		if(benutzer!=null) return benutzer.setPasswort(neuesPasswort);
		else return false;
	}

	// Sperrt das Passwort.
	public boolean passwortGesperrtSetzen(String benutzername, boolean gesperrt) {
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(benutzername);
		if(benutzer!=null) return benutzer.setGesperrt(gesperrt);
		else return false;
	}

	public boolean benutzerSchonVorhanden(String neuerBenutzername) {
		Benutzer benutzer = dbZugriff
				.getBenutzervonBenutzername(neuerBenutzername);
		if (benutzer != null) {
			return true;
		}
		return false;
	}

	public boolean Benutzernameaendern(String betroffenerBenutzer,
			String neuerBenutzername) {
		Benutzer benutzer = dbZugriff
				.getBenutzervonBenutzername(betroffenerBenutzer);
		if(benutzer!=null)return benutzer.setBenutzername(neuerBenutzername);
		else return false;
	}
	
	public String istBenutzerSchonLeiter(String benutzername){
		Benutzer benutzer = dbZugriff
				.getBenutzervonBenutzername(benutzername);
		if(benutzer!=null){
			if(benutzer.isLeiter()){
				OrgaEinheit orga = dbZugriff.getOrgaEinheitZuLeitername(benutzer.getBenutzername());
				return orga.getOrgaEinheitBez();
			}
			else {
				return "Nein";
			}
		}
		else return "Nein";
		
	}
}
