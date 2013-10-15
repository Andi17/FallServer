package Administration;

import java.util.ArrayList;
import java.util.List;

import Com.ComOrgaEinheit;
import Zugriffsschicht.Berechtigung;
import Zugriffsschicht.OrgaEinheit;
import Zugriffsschicht.Zugriffschicht;

public class OrgaEinheitVerwaltung {

	private Zugriffschicht dbZugriff;

	public OrgaEinheitVerwaltung(Zugriffschicht dbZugriff) {
		this.dbZugriff = dbZugriff;
	}

	// Gibt alle OrgaEinheiten zurück.
	public List<ComOrgaEinheit> getAlleOrgaEinheiten(boolean nurAktive) {

		List<OrgaEinheit> ListOrga = dbZugriff.getOrgaEinheiten(nurAktive);
		List<ComOrgaEinheit> rueckgabe = new ArrayList<ComOrgaEinheit>();
		for (OrgaEinheit orga : ListOrga) {
			Berechtigung Leiterber = dbZugriff
					.getBerechtigungzuidBerechtigung(orga
							.getIdLeiterBerechtigung());
			Berechtigung Mitarbeiterber = dbZugriff
					.getBerechtigungzuidBerechtigung(orga
							.getIdMitarbeiterBerechtigung());
			if (Leiterber != null) {
				if (Mitarbeiterber != null) {
					rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(),
							orga.getIdUeberOrgaEinheit(), orga
									.getOrgaEinheitBez(), orga.getLeitername(),
							orga.getIdLeiterBerechtigung(), Leiterber
									.getBerechtigungbez(), orga
									.getIdMitarbeiterBerechtigung(),
							Mitarbeiterber.getBerechtigungbez(), orga.isZustand()));
				} else {
					rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(),
							orga.getIdUeberOrgaEinheit(), orga
									.getOrgaEinheitBez(), orga.getLeitername(),
							orga.getIdLeiterBerechtigung(), Leiterber
									.getBerechtigungbez(), orga
									.getIdMitarbeiterBerechtigung(),
							"Keine Berechtigung", orga.isZustand()));
				}
			} else {
				if (Mitarbeiterber != null) {
					rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(),
							orga.getIdUeberOrgaEinheit(), orga
									.getOrgaEinheitBez(), orga.getLeitername(),
							orga.getIdLeiterBerechtigung(),
							"Keine Berechtigung", orga
									.getIdMitarbeiterBerechtigung(),
							Mitarbeiterber.getBerechtigungbez(), orga.isZustand()));
				} else {
					rueckgabe.add(new ComOrgaEinheit(orga.getIdOrgaEinheit(),
							orga.getIdUeberOrgaEinheit(), orga
									.getOrgaEinheitBez(), orga.getLeitername(),
							orga.getIdLeiterBerechtigung(),
							"Keine Berechtigung", orga
									.getIdMitarbeiterBerechtigung(),
							"Keine Berechtigung", orga.isZustand()));
				}
			}
		}
		return rueckgabe;

	}

	// fügt neue OrgaEinheit hinzu, gibt true zurück wenn geklappt
	public boolean neueOrgaEinheit(int idUeberOrgaEinheit,
			String OrgaEinheitBez, String Leitername, int idLeiterBerechtigung,
			boolean Zustand, int idMitarbeiterBerechtigung) {
		if(gibtEsOrgaEinheit(OrgaEinheitBez))return false;
		else {
			OrgaEinheit orga = dbZugriff.neueOrgaEinheit(idUeberOrgaEinheit,
					OrgaEinheitBez, Leitername, idLeiterBerechtigung, Zustand,
					idMitarbeiterBerechtigung);
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
	
	//Löscht OrgaEinheit mit der id.
	public boolean OrgaEinheitLoeschen(int idOrgaEinheit){
		OrgaEinheit orgaEinheit = dbZugriff.getOrgaEinheitZuidOrgaEinheit(idOrgaEinheit);
		if(orgaEinheit!=null){
			orgaEinheit.setZustand(false);
			return true;
		}
		else return false;
	}

}
