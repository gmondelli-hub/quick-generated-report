/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportBuilder.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import bld.generator.report.excel.GenerateExcel;
import bld.generator.report.excel.impl.GenerateExcelImpl;

/**
 * Questa classe è un componente Spring e può essere iniettata in altre parti dell'applicazione per creare generatori di report personalizzati. <br>
 * Fornisce metodi per la generazione di report con o senza l'uso di una query SQL.<br><br>
 */
@Component
@ComponentScan("bld.generator.report")
public class ReportBuilder {
	
	private static ReportBuilder INSTANCE = null;
	private GenerateExcel generatorExcel;
	
	/**
     * Costruttore di ReportBuilder.
     * 
     * @param generatorExcel L'oggetto `GenerateExcel` utilizzato per la generazione di report.
     */
	private ReportBuilder(GenerateExcel generatorExcel) {
		this.generatorExcel = generatorExcel;
	}
	
	/**
	 * Costruttore pubblico di ReportBuilder per i moduli importatori senza utilizzo.
	 * 
	 */
	public static ReportBuilder getInstance() {
		if (INSTANCE == null) INSTANCE = new ReportBuilder(new GenerateExcelImpl());
		return INSTANCE;
	}
	
	/**
     * Crea un generatore di report senza specificare una query SQL.
     * 
     * @param reportClassType 	Il tipo di classe del report.
     * @param reportName 		Il nome del report.
     * @return Un oggetto `ReportGeneratorBuilder` configurato per la generazione di report senza query SQL.
     */
	public ReportGeneratorBuilder build(Class<?> reportClassType, String reportName) {
		return ReportGeneratorBuilder.define(generatorExcel, reportClassType, reportName);
	}
	
	/**
     * Crea un generatore di report statico.
     * 
     * @param reportClassType 	Il tipo di classe del report.
     * @param reportName 		Il nome del report.
     * @param reportRows 		Lista di righe del report.
     * @return Un oggetto `ReportGeneratorBuilder` configurato per la generazione di report statico.
     */
	public ReportGeneratorBuilder build(Class<?> reportClassType, String reportName, List<?> reportRows) {
		return ReportGeneratorBuilder.define(generatorExcel, reportClassType, reportName, reportRows);
	}
	
	/**
     * Crea un generatore di report specificando una query SQL.
     * 
     * @param reportClassType 	Il tipo di classe del report.
     * @param reportName 		Il nome del report.
     * @return Un oggetto `ReportQueryBuilder` configurato per la generazione di report con query SQL.
     */
	public ReportQueryBuilder buildWithCondition(Class<?> reportClassType, String reportName) {
		return ReportQueryBuilder.define(generatorExcel, reportClassType, reportName);
	}
}
