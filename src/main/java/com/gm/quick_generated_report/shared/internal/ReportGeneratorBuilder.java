/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportGeneratorBuilder.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.gm.quick_generated_report.shared.exception.ReportException;

import bld.generator.report.excel.BaseSheet;
import bld.generator.report.excel.GenerateExcel;
import bld.generator.report.excel.QuerySheetData;
import bld.generator.report.excel.RowSheet;
import bld.generator.report.excel.SheetData;
import bld.generator.report.excel.data.ReportExcel;

/**
 * Questa classe è responsabile della generazione dei file del report in formato XLSX o CSV a partire dalle classi generate `RowSheet` e `QuerySheetData` create dalle classi `ReportRowClassGenerator` e `ReportSheetClassGenerator`.<br> 
 * Questa classe consente di definire il nome del report, il tipo di classe del report e, facoltativamente, una query SQL per filtrare i dati.
 * 
 * @see ReportQueryBuilder
 * @see ReportBuilder
 */
public class ReportGeneratorBuilder {

	private Class<?> reportClassType;
	private List<?> reportRows;
	private String reportName;
	private String reportQuery;
	private String reportOrder;
	private GenerateExcel generatorExcel;
	
	/**
     * Costruttore privato utilizzato per inizializzare un'istanza di `ReportGeneratorBuilder`.
     *
     * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
     * @param reportClassType Il tipo di classe del report.
     * @param reportName      Il nome del report.
     * @param reportQuery     La query SQL per filtrare i dati (opzionale).
     * @param reportRows 	  Lista di righe del tipo della classe del report usato per fogli statici.
     */
	private ReportGeneratorBuilder(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName, String reportQuery, String reportOrder, List<?> reportRows) {
		this.reportClassType = reportClassType;
		this.reportName = reportName;
		this.generatorExcel = generatorExcel;
		this.reportQuery = reportQuery;
		this.reportOrder = reportOrder;
		this.reportRows = reportRows;
	}
	
	/**
     * Crea un'istanza di `ReportGeneratorBuilder` senza specificare una query SQL.
     *
     * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
     * @param reportClassType Il tipo di classe del report.
     * @param reportName      Il nome del report.
     * @return Un nuovo oggetto `ReportGeneratorBuilder` senza query SQL specificata.
     */
	protected static ReportGeneratorBuilder define(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName) {
		ReportGeneratorBuilder reportGeneratorBuilder = new ReportGeneratorBuilder(generatorExcel, reportClassType, reportName, null, null, null);
		return reportGeneratorBuilder;
	}
	
	/**
     * Crea un'istanza di `ReportGeneratorBuilder` specificando una query SQL.
     *
     * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
     * @param reportClassType Il tipo di classe del report.
     * @param reportName      Il nome del report.
     * @param reportQuery     La query SQL per filtrare i dati.
     * @return Un nuovo oggetto `ReportGeneratorBuilder` con la query SQL specificata.
     */
	protected static ReportGeneratorBuilder define(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName, String reportQuery) {
		ReportGeneratorBuilder reportGeneratorBuilder = new ReportGeneratorBuilder(generatorExcel, reportClassType, reportName, reportQuery, null, null);
		return reportGeneratorBuilder;
	}
	
	/**
	 * Crea un'istanza di `ReportGeneratorBuilder` specificando una query SQL con clausola di ordinamento ORDER BY.
	 *
	 * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
	 * @param reportClassType Il tipo di classe del report.
	 * @param reportName      Il nome del report.
	 * @param reportQuery     La query SQL per filtrare i dati.
	 * @param reportOrder     La clausola ORDER BY per la query SQL per ordinare i dati.
	 * @return Un nuovo oggetto `ReportGeneratorBuilder` con la query SQL specificata.
	 */
	protected static ReportGeneratorBuilder define(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName, String reportQuery, String reportOrder) {
		ReportGeneratorBuilder reportGeneratorBuilder = new ReportGeneratorBuilder(generatorExcel, reportClassType, reportName, reportQuery, reportOrder, null);
		return reportGeneratorBuilder;
	}
	
	/**
	 * Crea un'istanza di `ReportGeneratorBuilder` specificando la lista delle righe del report statico.
	 *
	 * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
	 * @param reportClassType Il tipo di classe del report.
	 * @param reportName      Il nome del report.
	 * @param reportRows 	  Lista di righe del tipo della classe del report.
	 * @return Un nuovo oggetto `ReportGeneratorBuilder` con la query SQL specificata.
	 */
	protected static ReportGeneratorBuilder define(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName, List<?> reportRows) {
		ReportGeneratorBuilder reportGeneratorBuilder = new ReportGeneratorBuilder(generatorExcel, reportClassType, reportName, null, null, reportRows);
		return reportGeneratorBuilder;
	}
	
	/**
     * Genera e restituisce un file XLSX basato sulle classi `RowSheet` e `QuerySheetData` o `SheetData` create dalle classi `ReportRowClassGenerator` e `ReportSheetClassGenerator`.
     *
     * @return Un array di byte contenente il file XLSX generato.
     * @throws Exception Se si verificano errori durante la generazione del report.
     */
	public byte[] generateXlsx() throws Exception {
		if (reportName == null || reportClassType == null) 
			throw new ReportException("Report non configurato: 'reportName' o 'reportClassType' non impostati.");
		List<BaseSheet> baseSheets = reportRows == null 
				? generateReportQuerySheets()
				: generateReportDataSheets();
		return generatorExcel.createBigDataFileXlsx(new ReportExcel(reportName, baseSheets));
	}
	
	/**
     * Genera e restituisce un file CSV basato sulle classi `RowSheet` e `QuerySheetData` o `SheetData` create dalle classi `ReportRowClassGenerator` e `ReportSheetClassGenerator`.
     *
     * @return Un array di byte contenente il file CSV generato.
     * @throws Exception Se si verificano errori durante la generazione del report.
     */
	public byte[] generateCsv() throws Exception {
		if (reportName == null || reportClassType == null) 
			throw new ReportException("Report non configurato: 'reportName' o 'reportClassType' non impostati.");
		List<BaseSheet> baseSheets = reportRows == null 
				? generateReportQuerySheets()
				: generateReportDataSheets();
		byte[] csvBytes = generatorExcel.createBigDataFileXlsx(new ReportExcel(reportName, baseSheets));
		try (InputStream stream = new ByteArrayInputStream(csvBytes)) {
            Workbook workbook = WorkbookFactory.create(stream);
            return ReportUtil.convertXlsxToCSV(workbook.getSheetAt(0), workbook.getSheetAt(0).getSheetName());
        } 
	}
	
	/**
     * Genera le schede di base del report a partire dalle classi `RowSheet` e `QuerySheetData` create dalle classi `ReportRowClassGenerator` e `ReportSheetClassGenerator`.
     *
     * @return Una lista di oggetti `BaseSheet` rappresentanti le schede del report.
     * @throws Exception Se si verificano errori durante la generazione delle schede.
     */
	@SuppressWarnings("unchecked")
	private <T extends RowSheet> List<BaseSheet> generateReportQuerySheets() throws Exception {
		Class<T> rowClass = (Class<T>) ReportRowClassGenerator.generateReportRowClass(reportClassType);
		Class<? extends QuerySheetData<T>> querySheetClass = (Class<? extends QuerySheetData<T>>) ReportSheetClassGenerator.generateReportSheetClass(reportClassType, rowClass, reportQuery, reportOrder);
		
	    QuerySheetData<T> querySheetInstance = querySheetClass.getDeclaredConstructor(String.class).newInstance(reportClassType.getSimpleName());
		querySheetInstance.setListRowSheet(new ArrayList<>());
		
		List<BaseSheet> baseSheets = new ArrayList<BaseSheet>();
		baseSheets.add(querySheetInstance);
		
		return baseSheets;
	}
	
	/**
     * Genera le schede di base del report a partire dalle classi `RowSheet` e `SheetData` create dalle classi `ReportRowClassGenerator` e `ReportSheetClassGenerator`.
     *
     * @return Una lista di oggetti `BaseSheet` rappresentanti le schede del report.
     * @throws Exception Se si verificano errori durante la generazione delle schede.
     */
	@SuppressWarnings("unchecked")
	private <T extends RowSheet> List<BaseSheet> generateReportDataSheets() throws Exception {
		Class<T> rowClass = (Class<T>) ReportRowClassGenerator.generateReportRowClass(reportClassType);
		Class<? extends SheetData<T>> dataSheetClass = (Class<? extends SheetData<T>>) ReportSheetClassGenerator.generateReportSheetClass(reportClassType, rowClass);
		
		SheetData<T> dataSheetInstance = dataSheetClass.getDeclaredConstructor(String.class).newInstance(reportClassType.getSimpleName());
		List<T> rowSheetList = ReportUtil.generateRows(rowClass, reportRows);
		dataSheetInstance.setListRowSheet(rowSheetList);

		List<BaseSheet> baseSheets = new ArrayList<BaseSheet>();
		baseSheets.add(dataSheetInstance);
		
		return baseSheets;
	}
}
