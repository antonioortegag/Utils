
package es.aemps.nucleo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <b>MD5</b>
 * Clase que permite obtener el MD5
 * 
 */
public class MD5 {

	private static final Log LOG = LogFactory.getLog(MD5.class);

	private MD5() {
		super();
	}

	/**
	 * Metodo que obtiene el MD5 de un array de bytes a partir de una clave determinada
	 * @param buffer
	 * @return MD5 del parametro pasado
	 */
	public static String getMD5(final byte[] buffer) {

		String ret = null;

		try {
			final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            final byte[] digest = md5.digest();
            final StringBuffer hexString = new StringBuffer();

			String aux = null;
            for (int i = 0; i < digest.length; i++) {
            	aux = Integer.toHexString(0xFF & digest[i]);

			    if (aux.length() < 2) {
			    	aux = "0" + aux;
			    }

			    hexString.append(aux);
			}

			ret = hexString.toString();		
		} catch (NoSuchAlgorithmException e) {
			LOG.error("No existe el algoritmo MD5");
		}
		
		return ret;
	}
}
