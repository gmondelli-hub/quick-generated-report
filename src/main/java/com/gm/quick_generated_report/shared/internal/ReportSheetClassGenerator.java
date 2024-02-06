/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportSheetClassGenerator.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import bld.generator.report.excel.QuerySheetData;
import bld.generator.report.excel.RowSheet;
import bld.generator.report.excel.SheetData;
import bld.generator.report.excel.annotation.ExcelHeaderLayout;
import bld.generator.report.excel.annotation.ExcelMarginSheet;
import bld.generator.report.excel.annotation.ExcelQuery;
import bld.generator.report.excel.annotation.ExcelSheetLayout;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Questa classe è responsabile della generazione a runtime di una classe `QuerySheetData` o `SheetData` personalizzata, specifica per rappresentare il
 * layout di un foglio del Report.<br> Le classi generiche `RowSheet` rappresentano le righe del foglio del Report.<br><br>
 * Questa classe è utilizzata per generare classi che estendono `QuerySheetData` o `SheetData` con l'aggiunta delle annotazioni specifiche per il layout del foglio Excel.<br>
 * Le classi generate vengono caricate sia nel ClassLoader `ReportClassLoader` che in quello di default del contesto applicativo, e vengono infine salvate su file system.
 * 
 * @see ReportGeneratorBuilder
 */
class ReportSheetClassGenerator {

	/**
	 * Genera una classe di foglio Excel personalizzata basata su una classe di input specifica.
	 *
	 * @param inputClass   La classe di input da cui generare la classe di foglio Excel.
	 * @param rowClass     La classe generata che rappresenta il layout delle righe del foglio Excel.
	 * @param sqlCondition La condizione SQL per la query del foglio Excel.
	 * @param sqlOrder     La clausola ORDER BY SQL per l'ordinamento della query del foglio Excel.
	 * @return La classe generata che estende `QuerySheetData`.
	 * @throws Exception Se si verifica un errore durante la generazione.
	 */
	@SuppressWarnings("unchecked")
	protected static Class<? extends QuerySheetData<? extends RowSheet>> generateReportSheetClass(Class<?> inputClass, Class<? extends RowSheet> rowClass, String sqlCondition, String sqlOrder) throws Exception {

		// recupero del nome della classe in input e aggiunta del suffisso "ReportSheetQuery"
		String generatedClassName = inputClass.getSimpleName() + "ReportSheetQuery";

		// restituisco la classe generata se è già stata inserita nel ClassLoader
		ReportClassLoader reportClassLoader = ReportClassLoader.newInstance(ReportSheetClassGenerator.class.getClassLoader());
		Class<?> sheetClass = reportClassLoader.findClass(generatedClassName);
		if (sheetClass != null) {
			String reportQuery = ReportUtil.generateSQLQuery(rowClass, inputClass.getSimpleName(), sqlCondition, sqlOrder);
			ExcelQuery excelQueryAnnotation = sheetClass.getDeclaredAnnotation(ExcelQuery.class);
			ReportUtil.changeAnnotationValue(excelQueryAnnotation, "select", reportQuery);
			return (Class<? extends QuerySheetData<? extends RowSheet>>) sheetClass;
		}

		// creazione del builder per la generazione di una classe a partire da una in input
		DynamicType.Builder<?> builder = new ByteBuddy()
				.subclass(TypeDescription.Generic.Builder.parameterizedType(QuerySheetData.class, rowClass).build(), ConstructorStrategy.Default.IMITATE_SUPER_CLASS_PUBLIC)
				.defineConstructor(Visibility.PUBLIC)
				.intercept(MethodCall.invoke(QuerySheetData.class.getDeclaredConstructors()[0]).onSuper().with("sheetName"))
				.method(ElementMatchers.named("getRowClass"))
				.intercept(FixedValue.value(new TypeDescription.ForLoadedType(rowClass))).name(generatedClassName);

		// aggiunta delle annotazioni di classe
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelSheetLayout.class).build());
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelHeaderLayout.class).build());
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelMarginSheet.class)
				.define("bottom", 1.5).define("left", 1.5).define("right", 1.5).define("top", 1.5).build());

		// aggiunta della query al DB
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelQuery.class)
				.define("select", ReportUtil.generateSQLQuery(rowClass, inputClass.getSimpleName(), sqlCondition, sqlOrder))
				.define("nativeQuery", false).build());

		// generazione di una nuova classe con gli stessi metodi della classe in input e
		// che estenda QuerySheetData
		Unloaded<?> classUnloaded = builder.make();
		sheetClass = classUnloaded.load(reportClassLoader, ClassLoadingStrategy.Default.INJECTION).getLoaded();
		reportClassLoader.loadClass(generatedClassName);

		// salvataggio su file system
		String targetDirectory = "target/classes";
		String packageName = "generated_report";
		String className = generatedClassName;
		Path packagePath = Paths.get(targetDirectory, packageName.split("\\."));
		Files.createDirectories(packagePath);
		Path filePath = packagePath.resolve(className + ".class");
		Files.write(filePath, classUnloaded.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		return (Class<? extends QuerySheetData<? extends RowSheet>>) sheetClass;
	}

	/**
	 * Genera una classe di foglio Excel personalizzata basata su una classe di input specifica.
	 *
	 * @param inputClass La classe di input da cui generare la classe di foglio Excel.
	 * @param rowClass   La classe generata che rappresenta il layout delle righe del foglio Excel.
	 * @return La classe generata che estende `QuerySheetData`.
	 * @throws Exception Se si verifica un errore durante la generazione.
	 */
	@SuppressWarnings("unchecked")
	protected static Class<? extends SheetData<? extends RowSheet>> generateReportSheetClass(Class<?> inputClass, Class<? extends RowSheet> rowClass) throws Exception {

		// recupero del nome della classe in input e aggiunta del suffisso "ReportSheetData"
		String generatedClassName = inputClass.getSimpleName() + "ReportSheetData";

		// restituisco la classe generata se è già stata inserita nel ClassLoader
		ReportClassLoader reportClassLoader = ReportClassLoader.newInstance(ReportSheetClassGenerator.class.getClassLoader());
		Class<?> sheetClass = reportClassLoader.findClass(generatedClassName);
		if (sheetClass != null) {
			return (Class<? extends SheetData<? extends RowSheet>>) sheetClass;
		}

		// creazione del builder per la generazione di una classe a partire da una in
		// input
		DynamicType.Builder<?> builder = new ByteBuddy()
				.subclass(TypeDescription.Generic.Builder.parameterizedType(SheetData.class, rowClass).build(), ConstructorStrategy.Default.IMITATE_SUPER_CLASS_PUBLIC)
				.defineConstructor(Visibility.PUBLIC)
				.intercept(MethodCall.invoke(SheetData.class.getDeclaredConstructors()[0]).onSuper().with("sheetName"))
				.method(ElementMatchers.named("getRowClass"))
				.intercept(FixedValue.value(new TypeDescription.ForLoadedType(rowClass))).name(generatedClassName);

		// aggiunta delle annotazioni di classe
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelSheetLayout.class).build());
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelHeaderLayout.class).build());
		builder = builder.annotateType(AnnotationDescription.Builder.ofType(ExcelMarginSheet.class)
				.define("bottom", 1.5).define("left", 1.5).define("right", 1.5).define("top", 1.5).build());

		// generazione di una nuova classe con gli stessi metodi della classe in input e
		// che estenda SheetData
		Unloaded<?> classUnloaded = builder.make();
		sheetClass = classUnloaded.load(reportClassLoader, ClassLoadingStrategy.Default.INJECTION).getLoaded();
		reportClassLoader.loadClass(generatedClassName);

		// salvataggio su file system ReportSheetData.class per solo visualizzazione di test
        String targetDirectory = "report";
        String packageName = "generated_classes";
        String className = generatedClassName;
        Path packagePath = Paths.get(targetDirectory, packageName.split("\\."));
        Files.createDirectories(packagePath);
        Path filePath = packagePath.resolve(className + ".class");
        Files.write(filePath, classUnloaded.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		return (Class<? extends SheetData<? extends RowSheet>>) sheetClass;
	}
}
