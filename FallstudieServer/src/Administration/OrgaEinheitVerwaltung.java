package Administration;

import java.util.ArrayList;
import java.util.List;

import Com.ComOrgaEinheit;
import RightsManagement.Rechte;
import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Zugriffschicht;

public class OrgaEinheitVerwaltung {

	private Zugriffschicht dbZugriff;

	public OrgaEinheitVerwaltung(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	// Gibt alle OrgaEinheiten zurück.
	public List<ComOrgaEinheit> getAlleOrgaEinheiten(boolean nurAktive) {

		List<OrgaEinheit> ListOrga = dbZugriff.getOrgaEinheiten(nurAktive,
				false);
		List<ComOrgaEinheit> rueckgabe = new ArrayList<ComOrgaEinheit>();
		for (OrgaEinheit orga : ListOrga) {
			String leiterberechtigung = Rechte.getRechtBezeichnung(orga
					.getIdLeiterBerechtigung());
			String mitarbeiterberechtigung = Rechte.getRechtBezeichnung(orga
					.getIdMitarbeiterBerechtigung());
			rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(), orga
					.getIdUeberOrgaEinheit(), orga.getOrgaEinheitBez(), orga
					.getLeitername(), orga.getIdLeiterBerechtigung(),
					leiterberechtigung, orga.getIdMitarbeiterBerechtigung(),
					mitarbeiterberechtigung, orga.isZustand(), orga
							.getOrgaEinheitTyp()));
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

	public List<String> getOrgaEinheitTypen(){
		return dbZugriff.getOrgaEinheitTypen();
	}
}
