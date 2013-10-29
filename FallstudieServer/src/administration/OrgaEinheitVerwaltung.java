package administration;

import java.util.ArrayList;
import java.util.List;

import kommunikationsklassen.ComOrgaEinheit;

import zugriffsschicht.Benutzer;
import zugriffsschicht.OrgaEinheit;
import zugriffsschicht.Zugriffschicht;


public class OrgaEinheitVerwaltung {

	private Zugriffschicht dbZugriff;

	public OrgaEinheitVerwaltung(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	//Gibt ein Kommunikationsobjekt zur�ck. null wenn die orgaEinheitBez nicht vergeben ist.
	public ComOrgaEinheit getOrgaEinheit(String orgaEinheitBez) {
		OrgaEinheit orga = dbZugriff
				.getOrgaEinheitvonBezeichnung(orgaEinheitBez);
		if (orga != null) {
			String ueberOrgaEinheitString;
			OrgaEinheit ueberOrgaEinheit = dbZugriff
					.getOrgaEinheitZuidOrgaEinheit(orga.getIdUeberOrgaEinheit());
			if (ueberOrgaEinheit != null) ueberOrgaEinheitString = ueberOrgaEinheit.getOrgaEinheitBez();
			else ueberOrgaEinheitString = "Keine �bergeordnete Einheit";
			
			String leitername = orga.getLeitername();
			if(leitername == null || leitername.equals(""))leitername = "Kein Leiter";
			
			return new ComOrgaEinheit(orga.getIdOrgaEinheit(),
					orga.getIdUeberOrgaEinheit(), ueberOrgaEinheitString,
					orga.getOrgaEinheitBez(), leitername,
					orga.getIdLeiterBerechtigung(),
					orga.getIdMitarbeiterBerechtigung(), orga.isZustand(),
					orga.getOrgaEinheitTyp());
		}

		else
			return null;
	}

	// Gibt alle OrgaEinheiten zur�ck.
	public List<ComOrgaEinheit> getAlleOrgaEinheiten(boolean nurAktive) {

		List<OrgaEinheit> ListOrga = dbZugriff.getOrgaEinheiten(nurAktive,
				false);
		List<ComOrgaEinheit> rueckgabe = new ArrayList<ComOrgaEinheit>();

		for (OrgaEinheit orga : ListOrga) {
			String ueberOrgaEinheitString;
			OrgaEinheit ueberOrgaEinheit = dbZugriff
					.getOrgaEinheitZuidOrgaEinheit(orga.getIdUeberOrgaEinheit());
			if (ueberOrgaEinheit != null) {
				ueberOrgaEinheitString = ueberOrgaEinheit.getOrgaEinheitBez();
			} else
				ueberOrgaEinheitString = "Keine �bergeordnete Einheit";
			rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(), orga
					.getIdUeberOrgaEinheit(), ueberOrgaEinheitString, orga
					.getOrgaEinheitBez(), orga.getLeitername(), orga
					.getIdLeiterBerechtigung(), orga
					.getIdMitarbeiterBerechtigung(), orga.isZustand(), orga
					.getOrgaEinheitTyp()));
		}
		return rueckgabe;

	}

	//Gibt eine Liste von Strings zur�ck, die die Bezeichnungen aller OrgaEinheiten enth�lt.
	public List<String> getAlleOrgaEinheitenBezeichnungenMitTyp(String typ) {
		List<OrgaEinheit> listeOrgaEinheiten = dbZugriff
				.getOrgaEinheitenZuTyp(typ);
		List<String> rueckgabe = new ArrayList<String>();
		for (OrgaEinheit orga : listeOrgaEinheiten) {
			rueckgabe.add(orga.getOrgaEinheitBez());
		}
		return rueckgabe;
	}

	// f�gt neue OrgaEinheit hinzu, gibt true zur�ck wenn geklappt
	public boolean neueOrgaEinheit(int idUeberOrgaEinheit,
			String OrgaEinheitBez, String Leitername, boolean Zustand,
			String OrgaEinheitTyp) {
		Benutzer leiter = dbZugriff.getBenutzervonBenutzername(Leitername);
		if(leiter!=null){
			if (gibtEsOrgaEinheit(OrgaEinheitBez) || dbZugriff.getBenutzervonBenutzername(Leitername).isLeiter())
				return false;
			else {
				OrgaEinheit orga = dbZugriff.neueOrgaEinheit(idUeberOrgaEinheit,
						OrgaEinheitBez, Leitername, Zustand, OrgaEinheitTyp);
				dbZugriff.getBenutzervonBenutzername(Leitername).setidOrgaEinheit(orga.getIdOrgaEinheit());
				return true;
			}
		}
		else {
			Leitername = null;
			OrgaEinheit orga = dbZugriff.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername, Zustand, OrgaEinheitTyp);
			if (orga == null)
				return false;
			else return true;
		}

	}

	// Gibt true zur�ck wenn es schon eine OrgaEinheit mit der Bezeichnung gibt, sonst false.
	public boolean gibtEsOrgaEinheit(String bezeichnung) {
		OrgaEinheit orgaEinheit = null;
		orgaEinheit = dbZugriff.getOrgaEinheitvonBezeichnung(bezeichnung);
		if (orgaEinheit == null)
			return false;
		else
			return true;
	}

	// Setzt die OrgaEinheit mit der Bezeichnung auf inaktiv.
	//Gibt true zur�ck, wenn der Zustand ge�ndert wurde, ansonsten false.
	public boolean OrgaEinheitZustandAendern(String OrgaEinheit,
			boolean neuerZustand) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		if (orgaEinheit != null) {
			return orgaEinheit.setZustand(neuerZustand);
		} else
			return false;
	}

	//�ndert den Leiter der entsprechenden OrgaEinheit auf den String leitername.
	//Gibt true zur�ck, wenn der Leiter ge�ndert wurde, ansonsten false.
	public boolean OrgaEinheitLeiterAendern(String OrgaEinheit, String leitername) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		if(orgaEinheit!=null && leitername.equals("Kein Leiter")){
			orgaEinheit.setLeitername(null);
			return true;
		}
		Benutzer benutzer = dbZugriff.getBenutzervonBenutzername(leitername);
		if (orgaEinheit != null && benutzer!=null
				&& !benutzer.isLeiter()) {
			if(benutzer.setidOrgaEinheit(orgaEinheit.getIdOrgaEinheit()) &&
					orgaEinheit.setLeitername(leitername))
			return true;
			else return false;
		} else
			return false;
	}
	
	//�ndert die Bezeichnung der entsprechenden OrgaEinheit auf den String neueBezeichnung.
	//Gibt true zur�ck, wenn die Bezeichnung ge�ndert wurde, ansonsten false.
	public boolean OrgaEinheitBezeichnungAendern(String betroffeneOrgaEinheit, String neueBezeichnung) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(betroffeneOrgaEinheit);
		if (orgaEinheit != null
				&& dbZugriff.getOrgaEinheitvonBezeichnung(neueBezeichnung)==null) {
			return orgaEinheit.setOrgaEinheitBez(neueBezeichnung);
		} else
			return false;
	}
	
	//�ndert die �bergeordnete Einheit der entsprechenden OrgaEinheit auf die ID, 
	//die der Bezeichnung des Strings ueberOrgaEinheit entspricht.
	//Gibt true zur�ck, wenn die idUeberOrgaEinheit ge�ndert wurde, ansonsten false.
	public boolean OrgaEinheitUeberOrgaEinheitAendern(String OrgaEinheit, String ueberOrgaEinheit){
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		OrgaEinheit ueberOrgaEinheitObject = dbZugriff.getOrgaEinheitvonBezeichnung(ueberOrgaEinheit);
		if(orgaEinheit != null && ueberOrgaEinheitObject!= null){
			return orgaEinheit.setIdUeberOrgaEinheit(ueberOrgaEinheitObject.getIdOrgaEinheit());
		}
		else return false;
	}

	//Gibt eine Liste mit Strings zur�ck, die alle m�glichen Typen repr�sentiert.
	public List<String> getOrgaEinheitTypen() {
		return dbZugriff.getOrgaEinheitTypen();
	}
}
