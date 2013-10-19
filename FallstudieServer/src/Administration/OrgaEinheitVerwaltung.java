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
		if (orga != null)
			return new ComOrgaEinheit(orga.getIdOrgaEinheit(),
					orga.getIdUeberOrgaEinheit(), orga.getOrgaEinheitBez(),
					orga.getLeitername(), orga.getIdLeiterBerechtigung(),
					orga.getIdMitarbeiterBerechtigung(), orga.isZustand(),
					orga.getOrgaEinheitTyp());
		else
			return null;
	}

	// Gibt alle OrgaEinheiten zurück.
	public List<ComOrgaEinheit> getAlleOrgaEinheiten(boolean nurAktive) {

		List<OrgaEinheit> ListOrga = dbZugriff.getOrgaEinheiten(nurAktive,
				false);
		List<ComOrgaEinheit> rueckgabe = new ArrayList<ComOrgaEinheit>();
		for (OrgaEinheit orga : ListOrga) {
			rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(), orga
					.getIdUeberOrgaEinheit(), orga.getOrgaEinheitBez(), orga
					.getLeitername(), orga.getIdLeiterBerechtigung(), orga
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
		if (gibtEsOrgaEinheit(OrgaEinheitBez))
			return false;
		else {
			OrgaEinheit orga = dbZugriff.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername, Zustand, OrgaEinheitTyp);
			if (orga == null)
				return false;
			else
				return true;
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
	public boolean OrgaEinheitZustandAendern(int idOrgaEinheit,
			boolean neuerZustand) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitZuidOrgaEinheit(idOrgaEinheit);
		if (orgaEinheit != null) {
			return orgaEinheit.setZustand(neuerZustand);
		} else
			return false;
	}

	public boolean OrgaEinheitLeiterAendern(int idOrgaEinheit, String leitername) {
		OrgaEinheit orgaEinheit = dbZugriff
				.getOrgaEinheitZuidOrgaEinheit(idOrgaEinheit);
		if (orgaEinheit != null
				&& !dbZugriff.getBenutzervonBenutzername(leitername).isLeiter()) {
			return orgaEinheit.setLeitername(leitername);
		} else
			return false;
	}

	public List<String> getOrgaEinheitTypen() {
		return dbZugriff.getOrgaEinheitTypen();
	}
}
