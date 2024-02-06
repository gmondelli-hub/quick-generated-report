/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportRowClassBuilder.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.StubMethod;

/**
 * Questa classe è responsabile della creazione di classi a runtime che rappresentano righe di fogli del Report. <br>
 * Utilizza la classe ReportRowClassSpec per definire i campi, i costruttori, i getter e i setter necessari per le classi generiche di riga del Report.
 * 
 * @see ReportRowClassGenerator
 */
class ReportRowClassBuilder {

	private static Builder<?> builder = null;
	private Implementation interceptor = StubMethod.INSTANCE;
	private List<Class<?>> types = new ArrayList<>();
	
	private ReportRowClassBuilder(Builder<?> builder) {
        ReportRowClassBuilder.builder = builder;
    }
	
	protected static ReportRowClassBuilder defineBuilder(Builder<?> builder) {
		return new ReportRowClassBuilder(builder);
    }
	
	protected ReportRowClassBuilder defineField(Field field, int fieldIndex) {
    	ReportRowClassBuilder.builder = ReportRowClassSpec.defineField(ReportRowClassBuilder.builder, field, fieldIndex);
        return this;
    }

	protected ReportRowClassBuilder defineGetter(Field field) {
    	ReportRowClassBuilder.builder = ReportRowClassSpec.defineGetter(ReportRowClassBuilder.builder, field);
        return this;
    }

	protected ReportRowClassBuilder defineSetter(Field field) {
    	ReportRowClassBuilder.builder = ReportRowClassSpec.defineSetter(ReportRowClassBuilder.builder, field);
        return this;
    }
    
	protected ReportRowClassBuilder defineConstructor() {
    	Class<?>[] argumentTypes = types.toArray(new Class<?>[types.size()]);
    	ReportRowClassBuilder.builder = ReportRowClassSpec.defineConstructor(ReportRowClassBuilder.builder, interceptor, argumentTypes);
    	return this;
    }

	protected ReportRowClassBuilder intercept(Field field, int fieldIndex) {
    	types.add(field.getType());
    	interceptor = FieldAccessor.ofField(field.getName())
				.setsArgumentAt(fieldIndex)
				.andThen(interceptor);
    	return this;
    }
    
	protected Builder<?> build() {
    	return builder;
    }
}
