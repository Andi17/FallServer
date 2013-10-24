package Administration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Verschluesselung {
	
	//Anforderung 4.2.7
	//diese Klasse soll die Benutzernamen und die Passwörter verschlüsseln, bevor sie mit der
	//Datenbank abzugleichen.
	
	private static String password = "";
	
	//Verschlüsslt das Passwort und gibt den verschlüsselten String zurück.
	public static String verschluesseln(String passwort) {
		password = passwort;

	    MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes()); 
			byte byteData[] = md.digest();
			//convert the byte to hex format method 2
		    StringBuffer hexString = new StringBuffer();
			for (int i=0;i<byteData.length;i++) {
				String hex=Integer.toHexString(0xff & byteData[i]);
			     	if(hex.length()==1) hexString.append('0');
			     	hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	    
		
	}

}
