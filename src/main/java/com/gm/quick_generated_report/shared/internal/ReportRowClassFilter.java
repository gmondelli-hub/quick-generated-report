/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportRowClassFilter.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Questa classe fornisce i filtri utilizzati per filtrare i campi di una classe generata a runtime. <br>
 * I filtri consentono di specificare il tipo di dato dei campi, escludere il campo `serialVersionUID`, e filtrare i campi non statici.
 * 
 * @see ReportRowClassGenerator
 */
class ReportRowClassFilter {

    /**
     * Restituisce un predicato per filtrare i campi generati nella classe `RowSheet` che hanno i tipi di dati consentiti:<br>
     * LONG, DOUBLE, STRING, INTEGER, BOOLEAN, BIGDECIMAL, DATE.
     *
     * @return Il predicato per il filtro dei tipi di dati consentiti.
     */
	protected static Predicate<Field> filterFieldType() {
		Predicate<Field> allowedTypesPredicate = field -> {
            Set<Class<?>> allowedTypes = new HashSet<>();
            allowedTypes.add(Long.class);
            allowedTypes.add(Double.class);
            allowedTypes.add(String.class);
            allowedTypes.add(Integer.class);
            allowedTypes.add(Boolean.class);
            allowedTypes.add(BigDecimal.class);
            allowedTypes.add(Date.class);
            return allowedTypes.contains(field.getType());
        };
        return allowedTypesPredicate;
	}
	
	/**
     * Restituisce un predicato per filtrare il campo `serialVersionUID` richiesto dall'interfaccia Serializable.
     *
     * @return Il predicato per il filtro del campo `serialVersionUID`.
     */
	protected static Predicate<Field> filterFieldSerialVersionUID() {
		Predicate<Field> allowedFieldPredicate = field -> {
            return !field.getName().equalsIgnoreCase("serialVersionUID");
        };
        return allowedFieldPredicate;
	}
	
	/**
     * Restituisce un predicato per filtrare i campi non statici.
     *
     * @return Il predicato per il filtro dei campi non statici.
     */
	protected static Predicate<Field> filterFieldNoStatic() {
		Predicate<Field> allowedFieldPredicate = field -> {
            return !Modifier.isStatic(field.getModifiers());
        };
        return allowedFieldPredicate;
	}
}
