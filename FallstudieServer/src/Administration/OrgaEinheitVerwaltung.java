package Administration;

import java.util.ArrayList;
import java.util.List;

import Com.ComOrgaEinheit;
import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Zugriffschicht;

public class OrgaEinheitVerwaltung {

	private Zugriffschicht dbZugriff;

	public OrgaEinheitVerwaltung(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	public ComOrgaEinheit getOrgaEinheit(String orgaEinheitBez) {
		OrgaEinheit orga = dbZugriff
				.getOrgaEinheitvonBezeichnung(orgaEinheitBez);
		if (orga != null) {
			String ueberOrgaEinheitString;
			OrgaEinheit ueberOrgaEinheit = dbZugriff
					.getOrgaEinheitZuidOrgaEinheit(orga.getIdUeberOrgaEinheit());
			if (ueberOrgaEinheit != null) {
				ueberOrgaEinheitString = ueberOrgaEinheit.getOrgaEinheitBez();
			} else
				ueberOrgaEinheitString = "Keine übergeordnete Einheit";
			return new ComOrgaEinheit(orga.getIdOrgaEinheit(),
					orga.getIdUeberOrgaEinheit(), ueberOrgaEinheitString,
					orga.getOrgaEinheitBez(), orga.getLeitername(),
					orga.getIdLeiterBerechtigung(),
					orga.getIdMitarbeiterBerechtigung(), orga.isZustand(),
					orga.getOrgaEinheitTyp());
		}

		else
			return null;
	}

	// Gibt alle OrgaEinheiten zurück.
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
				ueberOrgaEinheitString = "Keine übergeordnete Einheit";
			rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(), orga
					.getIdUeberOrgaEinheit(), ueberOrgaEinheitString, orga
					.getOrgaEinheitBez(), orga.getLeitername(), orga
					.getIdLeiterBerechtigung(), orga
					.getIdMitarbeiterBerechtigung(), orga.isZustand(), orga
					.getOrgaEinheitTyp()));
		}
		return rueckgabe;

	}

	public List<String> getAlleOrgaEinheitenBezeichnungenMitTyp(String typ) {
		List<OrgaEinheit> listeOrgaEinheiten = dbZugriff
				.getOrgaEinheitenZuTyp(typ);
		List<String> rueckgabe = new ArrayList<String>();
		for (OrgaEinheit orga : listeOrgaEinheiten) {
			rueckgabe.add(orga.getOrgaEinheitBez());
		}
		return rueckgabe;
	}

	// fügt neue OrgaEinheit hinzu, gibt true zurück wenn geklappt
	public boolean neueOrgaEinheit(int idUeberOrgaEinheit,
			String OrgaEinheitBez, String Leitername, boolean Zustand,
			String OrgaEinheitTyp) {
		if (gibtEsOrgaEinheit(OrgaEinheitBez) || dbZugriff.getBenutzervonBenutzername(Leitername).isLeiter())
			return false;
		else {
			OrgaEinheit orga = dbZugriff.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername, Zustand, OrgaEinheitTyp);
			if (orga == null)
				return false;
			else{
				dbZugriff.getBenutzervonBenutzername(Leitername).setidOrgaEinheit(orga.getIdOrgaEinheit());
				return true;
			}
		}

	}

	// Gibt true zurück wenn es schon eine OrgaEinheit mit der Bezeichnung gibt.
	public boolean gibtEsOrgaEinheit(String bezeichnung) {
		OrgaEinheit orgaEinheit = null;
		orgaEinheit = dbZugriff.getOrgaEinheitvonBezeichnung(bezeichnung);
		if (orgaEinheit == null)
			return false;
		else
			return true;
	}

	// Setzt die OrgaEinheit mit der id auf inaktiv.
	public boolean OrgaEinheitZustandAendern(String OrgaEinheit,
			boolean neuerZustand) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		if (orgaEinheit != null) {
			return orgaEinheit.setZustand(neuerZustand);
		} else
			return false;
	}

	public boolean OrgaEinheitLeiterAendern(String OrgaEinheit, String leitername) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		if (orgaEinheit != null
				&& !dbZugriff.getBenutzervonBenutzername(leitername).isLeiter()) {
			if(dbZugriff.getBenutzervonBenutzername(leitername).setidOrgaEinheit(orgaEinheit.getIdOrgaEinheit()) &&
					orgaEinheit.setLeitername(leitername))
			return true;
			else return false;
		} else
			return false;
	}
	
	public boolean OrgaEinheitBezeichnungAendern(String OrgaEinheit, String bezeichnung) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		if (orgaEinheit != null
				&& dbZugriff.getOrgaEinheitvonBezeichnung(bezeichnung)==null) {
			return orgaEinheit.setOrgaEinheitBez(bezeichnung);
		} else
			return false;
	}
	
	public boolean OrgaEinheitUeberOrgaEinheitAendern(String OrgaEinheit, String ueberOrgaEinheit){
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitvonBezeichnung(OrgaEinheit);
		OrgaEinheit ueberOrgaEinheitObject = dbZugriff.getOrgaEinheitvonBezeichnung(ueberOrgaEinheit);
		if(orgaEinheit != null && ueberOrgaEinheitObject!= null){
			return orgaEinheit.setIdUeberOrgaEinheit(ueberOrgaEinheitObject.getIdOrgaEinheit());
		}
		else return false;
	}

	public List<String> getOrgaEinheitTypen() {
		return dbZugriff.getOrgaEinheitTypen();
	}
}
