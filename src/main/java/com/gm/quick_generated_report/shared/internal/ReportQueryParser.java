/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.config.ReportQueryParser.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Questa classe è responsabile della conversione del modello rappresentativo di una condizione "WHERE" di una query SQL in una stringa SQL corrispondente.<br>
 * Questa classe utilizza il modello `ReportQueryCondition` per definire la condizione SQL in base agli operatori e alle condizioni specificate nel modello.
 * 
 * @see ReportQueryBuilder
 */
class ReportQueryParser {

	/**
     * Converte il modello di query specificato in una stringa SQL corrispondente.
     *
     * @param query Il modello di query da convertire.
     * @return La stringa SQL corrispondente alla query.
     */
	protected static String parse(ReportQueryCondition queryCondition) {
		StringBuilder queryConditionBuilder = new StringBuilder();
		queryConditionBuilder.append(queryCondition.column);
		queryConditionBuilder.append(queryCondition.binaryCondition.toString());
		queryConditionBuilder.append(formatValue(queryCondition.rightValue));
		return queryConditionBuilder.toString();
	}
	
	/**
     * Converte il modello di ordinamento query specificato in una stringa SQL corrispondente.
     *
     * @param query Il modello di ordinamento query da convertire.
     * @return La stringa SQL corrispondente alla clausola ORDER BY.
     */
	protected static String parse(ReportQueryOrder ...queryOrderList) {
		List<String> ordByList = new ArrayList<String>();
		for (ReportQueryOrder queryOrder : queryOrderList) {
			StringBuilder queryOrderBuilder = new StringBuilder();
			queryOrderBuilder.append(queryOrder.value);
			queryOrderBuilder.append(queryOrder.orderDirection.toString());
			ordByList.add(queryOrderBuilder.toString());
		}
		return String.join(", ", ordByList);
	}
	
	/**
     * Formatta un valore in una rappresentazione appropriata per essere utilizzato in una query SQL.
     *
     * @param value Il valore da formattare.
     * @return Il valore formattato come stringa SQL.
     */
    private static String formatValue(Object value) {
    	if (value == null) return null;
        if (value instanceof Date) {
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "TO_DATE('" + formatter.format(value) + "', 'YYYY-MM-DD HH24:MI:SS')";
        } else if (value instanceof String) {
        	return "'" + String.valueOf(value) + "'";
        } else {
            return String.valueOf(value);
        }
    }
}
