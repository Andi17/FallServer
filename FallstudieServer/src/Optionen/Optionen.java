package Optionen;

public class Optionen {
	private final static boolean initialbelegungBenutzerGesperrt = false;
	private final static boolean initialbelegungOrgaEinheitZustand = true;
	private final static boolean initialbelegungStrichartZustand = true;
	private final static String jdbcurl = "jdbc:mysql://localhost/Elastico";
	private final static String jdbcuser = "root";
	private final static String jdbcpw = "hermann";
	private final static String webserverURL = "http://localhost:8888/WSExample/simple";
	// Anforderung 4.3.1: Speicherdauer soll konfigurierbar sein.
	private static int speicherdauer;

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
}
