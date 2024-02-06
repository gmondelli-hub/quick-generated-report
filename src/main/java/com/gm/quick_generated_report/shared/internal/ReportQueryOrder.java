/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportQueryOrder.java
*/
package com.gm.quick_generated_report.shared.internal;

/**
 * Questa classe consente di costruire una clausola ORDER BY SQL a partire da quelle definite da OrderDirection. <br>
 * 
 * @see ReportQueryBuilder
 */
public class ReportQueryOrder {

	/**
     * Enumerazione che rappresenta le possibili direzioni per la costruzione della clausola ORDER BY.
     */
	public enum OrderDirection {
    	ASC(" ASC "), 		
    	DESC(" DESC ");		
		
		private final String queryOrder;

		private OrderDirection(String queryOrder) {
			this.queryOrder = queryOrder;
		}
		
		@Override
		public String toString() {
			return queryOrder;
		}
    }
	
	protected final Object value;
	protected final OrderDirection orderDirection;
	
	private ReportQueryOrder(OrderDirection orderDirection, Object value) {
		this.orderDirection = orderDirection;
		this.value = value;
	}
	
	/**
     * Crea la struttura per la clausola ORDER BY con direzione specificata
     *
     * @param orderDirection La direzione per la clausola ORDER BY (ASC o DESC).
     * @param column     Il valore per la clausola ORDER BY.
     * @return `ReportQueryOrder` per la clausola ORDER BY, o `null` se `column` è `null`.
     */
	public static ReportQueryOrder orderBy(OrderDirection orderDirection, String column) {
		if (column == null) return null;
		return new ReportQueryOrder(orderDirection, column);
	}
}
