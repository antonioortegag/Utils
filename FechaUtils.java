/*
 * Proyecto:  nucleo
 * Paquete:   es.aemps.nucleo.utils
 * Archivo:   ConversionDate.java
 *
 */

package es.aemps.nucleo.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.aemps.sgm.errorlog.ErrorLog;

/**
 * <b>ConversionFechas</b> Clase que contiene metodos de conversion de fechas
 * @version $Revision: 26857 $ $Date: 2008-03-28 11:41:14 +0100 (vie, 28 mar
 * 2008) $
 * @author $Author: ftorres $
 * @since 13-ene-2006
 * 
 */

// @PMD:REVIEWED:CyclomaticComplexity: por F. Bermejo el 15/06/06 11:10
public class FechaUtils {

	private FechaUtils() {
		super();
	}

	// @PMD:REVIEWED:ProperLogger: por F. Bermejo el 15/06/06 11:10
	private static final Log LOG = LogFactory.getLog(FechaUtils.class);

	/**
	 * <b>getNumDias</b><br>
	 * <br>
	 * Método que indica el numero de dias entre dos fechas indicadas utilizando
	 * la base de datos de oracle
	 * @param conexion
	 * @param ini
	 * @param fin
	 * @return String
	 * @throws SQLException
	 */
	public static String getNumDias(final Connection conexion, final String ini, final String fin) throws SQLException {

		String ret = null;

		PreparedStatement pst = null;
		ResultSet rst = null;

		try {
			String sentencia = "select to_date(?, 'dd/MM/yyyy') - to_date(?, 'dd/MM/yyyy') FECHA from dual";

			pst = conexion.prepareStatement(sentencia);
			pst.setString(1, ini);
			pst.setString(2, fin);

			rst = pst.executeQuery();
			if (rst.next()) {
				ret = rst.getString("FECHA");
			}
		} finally {
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException e) {
					ErrorLog.guardaErrorLog(LOG, e.getMessage(), "NUCLEO", e);
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					ErrorLog.guardaErrorLog(LOG, e.getMessage(), "NUCLEO", e);
				}
			}
		}

		return ret;

	}

	/**
	 * <b>convertirFormato</b> Metodo que convierte un String de fecha a
	 * formato (AAAA-MM-DD) pasandole como parametro la fecha y el separador que
	 * contiene entre año y mes
	 * @param fecha
	 * @param separador
	 * @return String
	 */
	public static String convertirAFormatoEN(final String fecha, final String separador) {

		String dia = "";
		String mes = "";
		String anno = "";
		final StringTokenizer token = new StringTokenizer(fecha, separador);
		dia = token.nextToken();
		mes = token.nextToken();
		anno = token.nextToken();
		return anno + "-" + mes + "-" + dia;
	}

	/**
	 * <b>convertirAFormatoES</b>
	 * @param fecha
	 * @param separador
	 * @return String
	 */
	public static String convertirAFormatoES(final String fecha, String separador) {

		if (separador.equals("-")) {
			if (fecha.indexOf("-") == -1) {
				separador = "/";
			}
		} else if (separador.equals("/")) {
			if (fecha.indexOf("/") == -1) {
				separador = "-";
			}
		}

		String dia = "";
		String mes = "";
		String anno = "";
		final StringTokenizer token = new StringTokenizer(fecha, separador);
		anno = token.nextToken();
		mes = token.nextToken();
		dia = token.nextToken();

		return dia + "-" + mes + "-" + anno;
	}

	/**
	 * <b>stringToDate</b>
	 * @param fecha
	 * @return Date
	 */
	public static Date stringToDate(final String fecha) {

		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", new Locale("ES"));
		Date date = null;
		try {
			date = new Date(formatter.parse(fecha).getTime());
		} catch (ParseException e) {
			ErrorLog.guardaErrorLog(LOG, "error.parseo", "NUCLEO", e);
		}

		return date;
	}

	/**
	 * <b>stringToDate</b><br>
	 * <br>
	 * Método que convierte una fecha en un objeto java.sql.Date con un formato
	 * indicado
	 * @param fecha
	 * @param formato
	 * @return Date
	 */
	public static Date stringToDate(final String fecha, final String formato) {

		final SimpleDateFormat formatter = new SimpleDateFormat(formato, new Locale("ES"));
		Date date = null;
		try {
			date = new Date(formatter.parse(fecha).getTime());
		} catch (ParseException e) {
			ErrorLog.guardaErrorLog(LOG, "error.parseo", "NUCLEO", e);
		}

		return date;
	}

	/**
	 * <b>calendarToDate</b>
	 * @param cal
	 * @return Date
	 */
	public static Date calendarToDate(final Calendar cal) {

		final StringTokenizer token = new StringTokenizer(cal.getTime().toString(), " ");
		String mes = "";
		String anno = "";
		String dia = "";
		for (int i = 0; token.hasMoreTokens(); i++) {

			if (i == 1) {
				mes = token.nextToken();
			} else if (i == 2) {
				dia = token.nextToken();
			} else if (i == 5) {
				anno = token.nextToken();
			} else {
				token.nextToken();
			}
		}
		mes = parsearMes(mes);
		final Date date = stringToDate(anno + "-" + mes + "-" + dia);

		return date;
	}

	// @PMD:REVIEWED:CyclomaticComplexity: por F. Bermejo el 15/06/06 11:10
	private static String parsearMes(final String mes) {

		String aux = "";

		if ("Jan".equals(mes)) {
			aux = "01";
		} else if ("Feb".equals(mes)) {
			aux = "02";
		} else if ("Mar".equals(mes)) {
			aux = "03";
		} else if ("Apr".equals(mes)) {
			aux = "04";
		} else if ("May".equals(mes)) {
			aux = "05";
		} else if ("Jun".equals(mes)) {
			aux = "06";
		} else if ("Jul".equals(mes)) {
			aux = "07";
		} else if ("Aug".equals(mes)) {
			aux = "08";
		} else if ("Sep".equals(mes)) {
			aux = "09";
		} else if ("Oct".equals(mes)) {
			aux = "10";
		} else if ("Nov".equals(mes)) {
			aux = "11";
		} else if ("Dec".equals(mes)) {
			aux = "12";
		}

		return aux;
	}

	/**
	 * <b>getHoy</b><br>
	 * <br>
	 * Metodo que devuelve la fecha de hoy
	 * @param local
	 * @param formato
	 * @return Strign
	 */
	public static String getFechaHoy(final Locale local, final String formato) {

		final SimpleDateFormat format = new SimpleDateFormat(formato, local);
		return format.format(new java.util.Date());
	}

	/**
	 * <b>getFechaHoyOracle</b><br>
	 * <br>
	 * Devuelve la fecha de hoy utilizando la Base de Datos de Oracle
	 * @param conexion
	 * @param local
	 * @param formato
	 * @return String
	 * @throws SQLException
	 */
	public static String getFechaHoyOracle(final Connection conexion, final Locale local, final String formato)
			throws SQLException {

		String ret = null;

		PreparedStatement pst = null;
		ResultSet rst = null;
		String sentencia = "SELECT TO_CHAR(sysdate, '" + formato + "') FECHA from dual";

		try {
			pst = conexion.prepareStatement(sentencia);

			rst = pst.executeQuery();
			if (rst.next()) {
				ret = rst.getString("FECHA");
			}
		} finally {
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException e) {
					ErrorLog.guardaErrorLog(LOG, e.getMessage(), "NUCLEO", e);
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					ErrorLog.guardaErrorLog(LOG, e.getMessage(), "NUCLEO", e);
				}
			}
		}

		return ret;

	}

	/**
	 * <b>formateaFecha</b><br>
	 * <br>
	 * Formatea una fecha en el forma especificado
	 * @param fecha
	 * @param formato
	 * @return fecha formateada
	 */
	public static String formateaFecha(java.util.Date fecha, String formato) {
		String ret = null;

		SimpleDateFormat sdf = new SimpleDateFormat(formato, new Locale("es"));

		try {
			ret = sdf.format(fecha);
		} catch (Exception e) {
			ErrorLog.guardaErrorLog(LOG, "Error en el parseo de una fecha", "KERNEL", e);
		}

		return ret;
	}

	/**
	 * <b>getHoraActualOracle</b><br>
	 * <br>
	 * Método que devuelve la hora actual utilizando la base de datos de oracle
	 * @param conexion
	 * @return String
	 * @throws SQLException
	 */
	public static String getHoraActualOracle(final Connection conexion) throws SQLException {

		String ret = null;

		PreparedStatement pst = null;
		ResultSet rst = null;
		String sentencia = "SELECT TO_CHAR(sysdate, 'hh24:mi') FECHA from dual";

		try {
			pst = conexion.prepareStatement(sentencia);

			rst = pst.executeQuery();
			if (rst.next()) {
				ret = rst.getString("FECHA");
			}
		} finally {
			if (rst != null) {
				try {
					rst.close();
				} catch (SQLException e) {
					ErrorLog.guardaErrorLog(LOG, e.getMessage(), "NUCLEO", e);
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					ErrorLog.guardaErrorLog(LOG, e.getMessage(), "NUCLEO", e);
				}
			}
		}

		return ret;
	}

	/**
	 * <b>getFechaTitulo</b><br>
	 * <br>
	 * Método que muestra la fecha del tipo: 28 de Febrero de 2008 para un local
	 * determinado
	 * @param local
	 * @return String
	 */
	public static String getFechaTitulo(final Locale local) {

		String fechaHoy1 = null;
		String fechaHoy2 = null;
		String ret = null;

		fechaHoy1 = FechaUtils.getFechaHoy(local, "dd 'de' ");
		fechaHoy2 = FechaUtils.getFechaHoy(local, "MMMMMMMMMMMMMM 'de' yyyy");
		ret = fechaHoy1.concat(fechaHoy2.substring(0, 1).toUpperCase(local)).concat(fechaHoy2.substring(1));

		return ret;
	}

	/**
	 * <b>getFechaDeTitulo</b><br>
	 * <br>
	 * Método que muestra la fecha del tipo: 28 de Febrero de 2008 ATENCION
	 * 30/06/2008 jmmendez + Existe un problema con resepcto a los meses, aunque
	 * se pregunta en los argumentos, no se varía el estado de la sesión (el
	 * idioma) con lo que el mes nos va a venir en el idioma de la sesión (en
	 * pruebas en preproducción, en inglés, con lo que se recomienda NO USAR
	 * ESTA FUNCIÓN)
	 * 
	 * @param conexion
	 * @return String
	 * @throws SQLException
	 * @deprecated UTILIZAR getFechaTitulo(final Locale local)
	 */
	public static String getFechaDeTitulo(final Connection conexion) throws SQLException {

		String dia = null, mes = null, anno = null;

		dia = FechaUtils.getFechaHoyOracle(conexion, new Locale("ES"), "dd");
		mes = FechaUtils.getFechaHoyOracle(conexion, new Locale("ES"), "FMMONTH");
		anno = FechaUtils.getFechaHoyOracle(conexion, new Locale("ES"), "yyyy");

		mes = mes.toLowerCase();
		mes = (mes.charAt(0) + "").toUpperCase() + mes.substring(1);

		return dia + " de " + mes + " de " + anno;
	}

	/**
	 * Añade horas y minutos a una fecha.
	 * Si las horas no son correctas, devuelve la fecha sin añadir nada
	 * @param fecha
	 * @param hora
	 * @return fecha con horas y minutos
	 */
	public static java.util.Date addHora(java.util.Date fecha, String hora) {
		if (fecha== null)
			return null;
		Calendar local = new GregorianCalendar();
		local.setTime(fecha);
		if (Validaciones.isValidDate(hora, "HH:mm")) {
			StringTokenizer tokens = new StringTokenizer(hora, ":");
			String horas = (String) tokens.nextElement();
			String minutos = (String) tokens.nextElement();
			local = new GregorianCalendar(
					local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH), 
					Integer.parseInt(horas), Integer.parseInt(minutos));
		}
		
		return local.getTime();
	}
}
