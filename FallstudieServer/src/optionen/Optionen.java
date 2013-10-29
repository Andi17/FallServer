package optionen;

//Hier werden sind alle Optionen enthalten.

public class Optionen {
	private final static boolean initialbelegungBenutzerGesperrt = false;
	private final static boolean initialbelegungOrgaEinheitZustand = true;
	private final static boolean initialbelegungStrichartZustand = true;
	private final static String jdbcurl = "jdbc:mysql://localhost/Elastico";
	private final static String jdbcuser = "root";
	private final static String jdbcpw = "1234";
	private final static String webserverURL = "http://" + System.getenv("Computername") + ":8888/Elastico/simple";
	// Anforderung 4.3.1: Speicherdauer soll konfigurierbar sein.
	private final static int speicherdauerTage = 90;

	public static boolean isInitialbelegungbenutzergesperrt() {
		return initialbelegungBenutzerGesperrt;
	}
	
	public static boolean isInitialbelegungOrgaEinheitZustand(){
		return initialbelegungOrgaEinheitZustand;
	}

	public static boolean isinitialbelegungStrichartZustand(){
		return initialbelegungStrichartZustand;
	}
	public static String getJdbcuser() {
		return jdbcuser;
	}

	public static String getJdbcpw() {
		return jdbcpw;
	}

	public static String getJdbcurl() {
		return jdbcurl;
	}

	public static String getWebserverURL() {
		return webserverURL;
	}

	public static int getSpeicherdauer() {
		return speicherdauerTage;
	}
}
