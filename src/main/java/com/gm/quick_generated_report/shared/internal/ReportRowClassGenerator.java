/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportRowClassGenerator.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import bld.generator.report.excel.RowSheet;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

/**
 * Questa classe è responsabile della generazione a runtime di una classe basata su un'input di classe specifico.<br><br>
 * La classe generata avrà campi filtrati in base ai criteri definiti da `ReportRowClassFilter`, insieme ai rispettivi metodi getter e setter, e un costruttore all-arguments.<br><br>
 * La classe generata sarà caricata sia nel ClassLoader `ReportClassLoader` che nel ClassLoader predefinito del contesto applicativo, e verrà infine salvata su file system.
 * 
 * @see ReportGeneratorBuilder
 */
class ReportRowClassGenerator {
	
    /**
     * Genera una classe di riga di report a runtime basata su una classe di input specifica.
     *
     * @param inputClass La classe di input da cui generare la classe di riga.
     * @return La classe generata che estende `RowSheet`.
     * @throws Exception Se si verifica un errore durante la generazione.
     */
	@SuppressWarnings("unchecked")
	protected static Class<? extends RowSheet> generateReportRowClass(Class<?> inputClass) throws Exception {
        
		// creazione del nome della classe generata con suffisso "ReportRow"
        String generatedClassName = inputClass.getSimpleName() + "ReportRow";
        
        // restituisco la classe generata se è già stata inserita nel ClassLoader
        ReportClassLoader reportClassLoader = ReportClassLoader.newInstance(ReportRowClassGenerator.class.getClassLoader());
        Class<?> rowClass = reportClassLoader.findClass(generatedClassName);
        if (rowClass != null) {
        	return (Class<? extends RowSheet>) rowClass;
        }
        
        // creazione del builder di generazione della classe, nel Loader per il caricamento della classe nel ClassLoader
        DynamicType.Builder<?> builder = new ByteBuddy()
            .subclass(Object.class)
            .name(generatedClassName)
            .implement(RowSheet.class);
        ReportRowClassBuilder reportRowClassBuilder = ReportRowClassBuilder.defineBuilder(builder);

        // recupero e filtraggio dei soli campi permessi a partire da quelli della classe di input
        Field[] fields = Arrays.stream(inputClass.getDeclaredFields())
                .filter(ReportRowClassFilter.filterFieldSerialVersionUID())
                .filter(ReportRowClassFilter.filterFieldType())
                .filter(ReportRowClassFilter.filterFieldNoStatic())
                .toArray(Field[]::new);
        
        // crezione dei campi della classe generata
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            builder = reportRowClassBuilder
            		.defineField(field, i) // generazione del campo con tipo dinamico
            		.defineGetter(field)   // generazione metodo accessorio GETTER
            		.defineSetter(field)   // generazione metodo accessorio SETTER
            		.intercept(field, i)   // associazione del campo all'implementazione del costruttore
            		.build();
        }
        
        // generazione del costruttore con tutti i campi (ALL_ARGUMENTS)
        builder = reportRowClassBuilder
	        		.defineConstructor()
	        		.build();
        
        // generazione di una nuova classe con gli stessi metodi della classe in input e che estenda RowSheet
		Unloaded<?> classUnloaded = builder.make();
		rowClass = classUnloaded
				.load(ReportRowClassGenerator.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
            	.getLoaded();
		reportClassLoader.loadClass(generatedClassName);
        
		// salvataggio su file system ReportRow.class per solo visualizzazione di test
        String targetDirectory = "report";
        String packageName = "generated_classes";
        String className = generatedClassName;
        Path packagePath = Paths.get(targetDirectory, packageName.split("\\."));
        Files.createDirectories(packagePath);
        Path filePath = packagePath.resolve(className + ".class");
        Files.write(filePath, classUnloaded.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        return (Class<? extends RowSheet>) rowClass;
	}
}
