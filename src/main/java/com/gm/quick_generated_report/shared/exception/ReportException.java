/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.exception.ReportException.java
*/
package com.gm.quick_generated_report.shared.exception;

/**
 * Eccezione personalizzata per gestire errori specifici dei report.<br><br>
 * 
 * Questa classe estende la classe Exception e fornisce costruttori per consentire la specifica
 * di un messaggio di errore e/o una causa (Throwable) che ha causato l'eccezione.
 * 
 * @see java.lang.Exception
 */
public class ReportException extends Exception {

	private static final long serialVersionUID = 1L;
	
    /**
     * Costruttore che consente di specificare un messaggio di errore personalizzato.
     *
     * @param message Il messaggio di errore personalizzato.
     */
	public ReportException(String message) {
		super(message);
	}

	/**
     * Costruttore che consente di specificare un messaggio di errore personalizzato e una causa.
     *
     * @param message Il messaggio di errore personalizzato.
     * @param cause   La causa (Throwable) che ha causato l'eccezione.
     */
	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
     * Costruttore che consente di specificare una causa.
     *
     * @param cause La causa (Throwable) che ha causato l'eccezione.
     */
	public ReportException(Throwable cause) {
		super(cause);
	}
}