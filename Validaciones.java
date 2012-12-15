/*
 * Proyecto:  nucleo
 * Paquete:   es.aemps.nucleo.utils
 * Archivo:   Validaciones.java
 *
 */
package es.aemps.nucleo.utils;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.EmailValidator;

import es.aemps.nucleo.Constantes;

/**
 * <b>Validaciones</b>
 * 
 * 
 */
public class Validaciones {

	private Validaciones() {
		// Constructor privado
	}

	/**
	 * Verifica si el objeto es nulo, blanco o no tiene elementos
	 * 
	 * @param obj
	 * @return <code>true</code> si es nulo o blanco y <code>false</code> en caso contrario
	 * @see es.aemps.nucleo.utils.Validaciones#isNotNullOrBlank(Object)
	 */
	public static boolean isNullOrBlank(final Object obj) {
		boolean ret = true;
		if (obj instanceof String) { // Es una cadena
			final String aux = ((String) obj).trim();
			ret = ("".equals(aux));
		} else {
			if (obj instanceof List) { // Es una lista
				final List aux = ((List) obj);
				ret = (aux.size() < 1);
			} else {
				ret = (obj == null || "".equals(obj));
			}
		}

		return ret;
	}

	/**
	 * Valida que el objeto no sea nulo o blanco
	 * 
	 * @param obj
	 * @return <code>false</code> si es nulo o blanco y <code>true</code> en caso contrario
	 * @see es.aemps.nucleo.utils.Validaciones#isNullOrBlank(Object)
	 */
	public static boolean isNotNullOrBlank(final Object obj) {
		return !isNullOrBlank(obj);
	}

	/**
	 * Valida la fecha con el formato introducido
	 * 
	 * @param sFecha
	 * @param sFormato
	 * @return true si es una fecha válida, false en caso contrario
	 */
	public static boolean isValidDate(final String sFecha, final String sFormato) {

		boolean ret = false;

		try {
			final SimpleDateFormat formateador = new SimpleDateFormat(sFormato, new Locale("es"));
			formateador.setLenient(true);
			final ParsePosition pos = new ParsePosition(0);
			Date dFecha = null;
			dFecha = formateador.parse(sFecha, pos);
			if (dFecha != null) {
				ret = true;
			}
		} catch (Exception e) {
			// Si se produce un error, no es una fecha válida
			ret = false;
		}

		return ret;

	}

	/**
	 * Valida la fecha con el formato introducido
	 * 
	 * @param sFecha
	 * @return true si es una fecha válida, false en caso contrario
	 */
	public static boolean isValidDate(final String sFecha) {
		return isValidDate(sFecha, Constantes.DEFAULT_DATE_FORMAT);
	}

	/**
	 * Valida si la cadena introducida es un numero valido
	 * 
	 * @param sNumber
	 * @return true si es un numero válido, false en caso contrario
	 */
	public static boolean isValidNumber(final String sNumber) {
		boolean ret = true;
		try {
			Long.parseLong(sNumber);
		} catch (Exception e) {
			// Si se produce un error, no es una numero válido
			ret = false;
		}
		return ret;
	}

	/**
	 * Valida si el numero es Float
	 * @param sNumber
	 * @return boolean
	 */
	public static boolean isValidFloat(final String sNumber) {
		boolean ret = true;
		try {
			Float.parseFloat(sNumber);
		} catch (Exception e) {
			// Si se produce un error, no es una numero válido
			ret = false;
		}
		return ret;
	}

	/**
	 * <b>isValidTime</b><br>
	 * <br>
	 * Valida la hora
	 * 
	 * @param sTime
	 * @return boolean
	 */
	public static boolean isValidTime(final String sTime) {

		boolean ret = true;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		try {
			sdf.parse(sTime);
		} catch (ParseException e) {
			ret = false;
		}

		/*
		 * String sHora = null; String sMinutos = null; int cHora = 0; int cMinutos = 0;
		 * 
		 * try { final StringTokenizer token = new StringTokenizer(sTime, ":"); sHora = token.nextToken();
		 * sMinutos = token.nextToken();
		 * 
		 * try { cHora = Integer.parseInt(sHora); cMinutos = Integer.parseInt(sMinutos); } catch
		 * (NumberFormatException exc) { ret = false; }
		 * 
		 * if (ret) { if ((cHora >= 0 && cHora < 24) && (cMinutos > 0 && cMinutos < 60)) { ret = true; } else
		 * { ret = false; } } } catch (Exception e) { // Si se produce un error, no es una hora válida ret =
		 * false; }
		 */

		return ret;

	}

	/**
	 * <b>validateNIF</b><br>
	 * <br>
	 * Realiza la validación del Nif
	 * 
	 * @param value El nif que se quiere validar
	 * @return Si es correcto devuelve el nif, si no lo es devuelve null
	 */
	public static String validateNIF(final String value) {
		String result = null;
		String nif = value.toUpperCase().trim().replaceAll(" ", "").replaceAll("-", "");

		// Quitar los 0's de la izquierda del DNI
		int i = 0;
		boolean salir = false;
		int iDNI = 0;

		/*
		 * En el caso de un NIE, quitamos la primera X para validarlo como un NIF normal
		 */
		if (nif.startsWith("X") || nif.startsWith("Y") || nif.startsWith("Z")) {
			nif = nif.substring(1);
		}

		while (i < nif.length() && !salir) {
			if (nif.charAt(i) >= 0) {
				salir = true;
			} else {
				i++;
			}
		}
		nif = nif.substring(i, nif.length());

		String[] letras = { "T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V",
				"H", "L", "C", "K", "E" };
		String dni = nif.substring(0, nif.length() - 1);
		try {
			iDNI = Integer.parseInt(dni);
		} catch (NumberFormatException e) {
			return result;
		}
		if (dni.length() > 9) {
			return result;
		}

		String letraNif = nif.substring(nif.length() - 1, nif.length());
		String letraAux;
		int numero = iDNI % 23;
		letraAux = letras[numero];
		if (letraAux.equalsIgnoreCase(letraNif)) {
			return nif;
		}
		return result;
	}

	/*
	 * Formato CIF: X NNNNNNNN C X:Letra de tipo de Organización,una de las siguientes ->(
	 * A,B,C,D,E,F,G,H,K,L,M,N,P,Q,S } NNNNNNNN: Numeros. C: Dígito de control,un número ó letra -> A ó 1,B ó
	 * 2,C ó 3,D ó 4,E ó 5,F ó 6, G ó 7,H ó 8,I ó 9,J ó 0
	 */
	/**
	 * <b>validateCIF</b><br>
	 * <br>
	 * Realiza la validación del cif
	 * 
	 * @param value El cif que se quiere validar
	 * @return Devuelve el cif si es correcto, si no devuelve null
	 */

	public static String validateCIF(String value) {

		// Declaracion variables locales
		int iPares = 0;
		int iNones = 0;
		int iParcialNones = 0;
		int iSumTotal = 0;
		int iDigControl = 0;
		String[] letras = { "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "Q", "S", "U",
				"V", "W" };
		String[] control = { "J", "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		String result = null;
		boolean bEncontrado = false;

		try {
			value = value.toUpperCase().trim().replaceAll(" ", "").replaceAll("-", "");
			// Obtenemos la primera letra del CIF
			char cLetra = value.charAt(0);

			// Comprobamos que la letra se encuentre en el array
			for (int i = 0; !bEncontrado && i < letras.length; i++) {
				if (letras[i].equalsIgnoreCase(String.valueOf(cLetra)))
					bEncontrado = true;
			}

			// Se comprueba que la longitud sea 9 y que el primer caracter sea
			// una letra y que sea correcta,
			// dentro del array
			if (value.length() != 9 || Character.isDigit(cLetra) || !bEncontrado) {
				// La longitud debe ser 9
				result = null;
			}
			// Comenzamos con la validacion
			else {
				// Sumamos las cifras pares del numero central XX NNNNNNNN X
				for (int i = 2; i < 8; i += 2) {
					// Para lo pares
					iPares += Integer.parseInt(String.valueOf(value.charAt(i)));
				}
				// Obtenemos las cifras impares las multiplicamos y sumamos
				for (int i = 1; i < 8; i += 2) {
					// Para los impares
					iParcialNones = 2 * Integer.parseInt(String.valueOf(value.charAt(i)));

					// Si tiene mas de 2 cifras, sumamos las cifras entre ellas
					if (iParcialNones > 9) {
						iParcialNones = 1 + (iParcialNones - 10);
					}

					// Lo sumamos al total
					iNones += iParcialNones;
				}

				// Sumamos el resultado de pares e impares
				iSumTotal = iPares + iNones;

				// Obtenemos el digito de control restando cogiento la ultima
				// cifra de la
				// iSumTotal y restandolo a 10
				iDigControl = 10 - (iSumTotal % 10);

				// Si es 10
				if (iDigControl == 10) {
					iDigControl = 0;
				}

				// Verificamos el digito de control (letra o número)
				if (control[iDigControl].equals(value.substring(8, 9))
						|| iDigControl == Integer.parseInt(String.valueOf(value.charAt(8)))) {
					result = value;
				} else {
					result = null;
				}
			}
		} catch (Exception e) {
			result = null;
		}

		return result;
	}

	/*
	 * Formato CIF: X NNNNNNNN C X:Letra de tipo de Organización,una de las siguientes ->(
	 * A,B,C,D,E,F,G,H,K,L,M,N,P,Q,S } NNNNNNNN: Numeros. C: Dígito de control,un número ó letra -> A ó 1,B ó
	 * 2,C ó 3,D ó 4,E ó 5,F ó 6, G ó 7,H ó 8,I ó 9,J ó 0
	 */
	/**
	 * <b>validateCIF</b><br>
	 * <br>
	 * Realiza la validación del cif
	 * 
	 * @param value El cif que se quiere validar
	 * @return Devuelve el cif si es correcto, si no devuelve null
	 */

	public static String validarCIF(String value) {

		// Declaracion variables locales
		int iPares = 0;
		int iNones = 0;
		int iParcialNones = 0;
		int iSumTotal = 0;
		int iDigControl = 0;
		String[] letras = { "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "Q", "S", "U",
				"V", "W" };
		String[] control = { "J", "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		String result = null;
		boolean bEncontrado = false;

		try {
			value = value.toUpperCase();
			// Obtenemos la primera letra del CIF
			char cLetra = value.charAt(0);

			// Comprobamos que la letra se encuentre en el array
			for (int i = 0; !bEncontrado && i < letras.length; i++) {
				if (letras[i].equalsIgnoreCase(String.valueOf(cLetra)))
					bEncontrado = true;
			}

			// Se comprueba que la longitud sea 9 y que el primer caracter sea
			// una letra y que sea correcta,
			// dentro del array
			if (value.length() != 9 || Character.isDigit(cLetra) || !bEncontrado) {
				// La longitud debe ser 9
				result = null;
			}
			// Comenzamos con la validacion
			else {
				// Sumamos las cifras pares del numero central XX NNNNNNNN X
				for (int i = 2; i < 8; i += 2) {
					// Para lo pares
					iPares += Integer.parseInt(String.valueOf(value.charAt(i)));
				}
				// Obtenemos las cifras impares las multiplicamos y sumamos
				for (int i = 1; i < 8; i += 2) {
					// Para los impares
					iParcialNones = 2 * Integer.parseInt(String.valueOf(value.charAt(i)));

					// Si tiene mas de 2 cifras, sumamos las cifras entre ellas
					if (iParcialNones > 9) {
						iParcialNones = 1 + (iParcialNones - 10);
					}

					// Lo sumamos al total
					iNones += iParcialNones;
				}

				// Sumamos el resultado de pares e impares
				iSumTotal = iPares + iNones;

				// Obtenemos el digito de control restando cogiento la ultima
				// cifra de la
				// iSumTotal y restandolo a 10
				iDigControl = 10 - (iSumTotal % 10);

				// Si es 10
				if (iDigControl == 10) {
					iDigControl = 0;
				}

				// Verificamos el digito de control (letra o número)
				if (control[iDigControl].equals(value.substring(8, 9))
						|| iDigControl == Integer.parseInt(String.valueOf(value.charAt(8)))) {
					result = value;
				} else {
					result = null;
				}
			}
		} catch (Exception e) {
			result = null;
		}

		return result;
	}

	/**
	 * <b>isValidTelephone</b><br>
	 * <br>
	 * Método que valida un nº de telñefono, debe contener valores numéricos o los carateres '(', ')', '-' ó
	 * '/'
	 * 
	 * @param telefono
	 * @return boolean
	 */
	public static boolean isValidPhone(final String telefono) {

		boolean ret = true;

		for (int i = 0; i < telefono.length(); i++) {
			char letra = telefono.charAt(i);
			if (letra != '0' && letra != '1' && letra != '2' && letra != '3' && letra != '4' && letra != '5'
					&& letra != '6' && letra != '7' && letra != '8' && letra != '9' && letra != '(' && letra != ')'
					&& letra != '-' && letra != '/') {
				ret = false;
				break;
			}
		}

		return ret;

	}

	/**
	 * <b>isValidTasa</b><br>
	 * <br>
	 * Método que indica si un código de tasa cumple el formato establecido
	 * 
	 * @param codigoTasa
	 * @return boolean
	 */
	public static boolean isValidTasa(String codigoTasa) {

		boolean ret = true;
		String parteFija = null;
		String segundaParte = null;
		String dc = null;
		BigInteger resto = new BigInteger("0");
		BigInteger siete = new BigInteger("7");

		try {
			if (Validaciones.isNullOrBlank(codigoTasa)
					|| (Validaciones.isNotNullOrBlank(codigoTasa) && codigoTasa.length() != 13)) {
				ret = false;
			} else {
				parteFija = codigoTasa.substring(0, 6);
				if (!"791605".equals(parteFija)) {
					ret = false;
				}
			}
			if (ret) {
				segundaParte = codigoTasa.substring(6, codigoTasa.length() - 1);
				dc = codigoTasa.substring(codigoTasa.length() - 1);

				BigInteger todo = new BigInteger(parteFija + segundaParte);
				resto = todo.remainder(siete);
				if (resto.equals(new BigInteger("0"))) {
					if (!"0".equals(dc)) {
						ret = false;
					}
				} else {
					if (!String.valueOf(siete.subtract(resto).intValue()).equals(dc)) {
						ret = false;
					}
				}
			}
		} catch (NumberFormatException e) {
			ret = false;
		} catch (NullPointerException e) {
			ret = false;
		} catch (Exception e) {
			ret = false;
		}

		return ret;

	}

	/**
	 * <b>isValidCp</b><br>
	 * <br>
	 * Método que indica si un codigo postal es válido
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isValidCp(String value) {

		boolean ret = true;

		if (value.length() > 5) {
			ret = false;
		} else {
			for (int i = 0; i < value.length(); i++) {
				char letra = value.charAt(i);
				if (letra != '0' && letra != '1' && letra != '2' && letra != '3' && letra != '4' && letra != '5'
						&& letra != '6' && letra != '7' && letra != '8' && letra != '9') {
					ret = false;
					break;
				}
			}
		}

		return ret;

	}

	/**
	 * 
	 * <b>isValidFaxCorreo</b><br>
	 * <br>
	 * devuelve true si la cadena pasada como parametro no es nula y su formato se corresponde con un correo
	 * electrónico o un teléfono
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isValidFaxCorreo(String value) {
		boolean retorno = false;
		if (Validaciones.isNotNullOrBlank(value)) {
			EmailValidator mailValidator = org.apache.commons.validator.EmailValidator.getInstance();
			if (mailValidator.isValid(value) || isValidPhone(value)) {
				retorno = true;
			}
		}
		return retorno;
	}
	
	/**
	 * 
	 * <b>isValidEmail</b><br>
	 * <br>
	 * Devuelve true si la cadena pasada como parametro no es nula y su formato se corresponde con un correo
	 * electrónico
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isValidEmail(String email) {
		
		boolean matchFound = false;
		if (Validaciones.isNotNullOrBlank(email)) {
		      //Set the email pattern string
		      Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	
		      //Match the given string with the pattern
		      Matcher m = p.matcher(email);
	
		      //check whether match is found 
		      matchFound = !m.matches();
		}
	    return matchFound;
	}


	/**
	 * Devuelve true si la cadena no es <code>null</code> y contiene algún carácter distinto a espacio.
	 * @param text Texto a comprobar
	 * @return boolean
	 */
	public static boolean hasText(String text) {
		return (isNotNullOrBlank(text) && text.replaceAll(" ", "").length() > 0);
	}
}