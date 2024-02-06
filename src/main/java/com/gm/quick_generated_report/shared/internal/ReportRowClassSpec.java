/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportRowClassSpec.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import bld.generator.report.excel.annotation.ExcelCellLayout;
import bld.generator.report.excel.annotation.ExcelColumn;
import bld.generator.report.excel.annotation.ExcelDate;
import bld.generator.report.excel.constant.ColumnDateFormat;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;

/**
 * Questa classe fornisce metodi di supporto per la generazione di classi a runtime che rappresentano righe di fogli del Report. <br>
 * Queste classi generiche includono campi primitivi (String, Number, Boolean e Date), metodi getter e setter per ciascun campo, e un
 * costruttore all-arguments.<br><br>
 * 
 * Le sottoclassi possono utilizzare i metodi forniti da questa classe per definire i campi,
 * i costruttori, i getter e i setter necessari per le classi generiche di riga del Report.
 * 
 * @see ReportRowClassBuilder
 */
class ReportRowClassSpec {

	/**
     * Definisce un campo nella classe generata a runtime.
     *
     * @param builder    Il builder della classe generata.
     * @param field      Il campo da definire.
     * @param fieldIndex L'indice del campo.
     * @return Il builder aggiornato.
     */
	protected static Builder<?> defineField(Builder<?> builder, Field field, int fieldIndex) {
		List<AnnotationDescription> fieldAnnotations = new ArrayList<>();
		if (field.getType() == Date.class) {
			fieldAnnotations.add(AnnotationDescription.Builder.ofType(ExcelDate.class)
					.define("format", ColumnDateFormat.YYYY_MM_DD)
					.build());
		}
		fieldAnnotations.add(AnnotationDescription.Builder.ofType(ExcelColumn.class)
				.define("columnName", ReportUtil.generateReportColumn(field.getName()))
				.define("indexColumn", Double.valueOf(fieldIndex))
				.build());
		fieldAnnotations.add(AnnotationDescription.Builder.ofType(ExcelCellLayout.class)
				.define("horizontalAlignment", HorizontalAlignment.CENTER)
				.build());
		
		return builder.defineField(field.getName(), field.getType(), Modifier.PRIVATE)
				.annotateField(fieldAnnotations);
	}

	/**
     * Definisce un costruttore nella classe generata a runtime.
     *
     * @param builder        Il builder della classe generata.
     * @param interceptor    L'interceptor per il costruttore.
     * @param argumentTypes  I tipi di argomenti per il costruttore.
     * @return Il builder aggiornato.
     */
	protected static Builder<?> defineConstructor(Builder<?> builder, Implementation interceptor, Class<?>[] argumentTypes) {
		return builder.defineConstructor(Visibility.PUBLIC)
                .withParameters(argumentTypes)
                .intercept(interceptor);
	}

	/**
     * Definisce un setter per un campo nella classe generata a runtime.
     *
     * @param builder Il builder della classe generata.
     * @param field   Il campo per il quale definire il setter.
     * @return Il builder aggiornato.
     */
	protected static Builder<?> defineSetter(Builder<?> builder, Field field) {
		String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		return builder.defineMethod(setterName, Void.TYPE, Modifier.PUBLIC)
				.withParameter(field.getType(), field.getName(), Modifier.FINAL)
				.intercept(FieldAccessor.ofField(field.getName()));
	}

	/**
     * Definisce un getter per un campo nella classe generata a runtime.
     *
     * @param builder Il builder della classe generata.
     * @param field   Il campo per il quale definire il getter.
     * @return Il builder aggiornato.
     */
	protected static Builder<?> defineGetter(Builder<?> builder, Field field) {
		String setterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		return builder.defineMethod(setterName, field.getType(), Modifier.PUBLIC)
				.intercept(FieldAccessor.ofField(field.getName()));
	}
}
