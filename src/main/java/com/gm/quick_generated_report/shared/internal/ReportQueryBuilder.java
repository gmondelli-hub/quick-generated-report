/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportQueryBuilder.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bld.generator.report.excel.GenerateExcel;

/**
 * Questa classe è responsabile della costruzione di una query SQL basata su condizioni e operatori specificati.<br>
 * Permette di definire condizioni di ricerca basate su colonne, valori e condizioni specificate. <br>
 * Le condizioni possono essere concatenate utilizzando gli operatori logici "AND" e "OR" dell'enum 'ReportQueryOperator' e possono distinguersi in differenti tipologie descritte dall'enum 'ReportQueryCondition'.
 * 
 * @see ReportBuilder
 */
public class ReportQueryBuilder {

	private Class<?> reportClassType;
	private String reportName;
	private GenerateExcel generatorExcel;
    private List<ReportQueryCondition> reportConditions;
    
    /**
     * Costruttore privato utilizzato per inizializzare un'istanza di `ReportQueryBuilder`.
     *
     * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
     * @param reportClassType Il tipo di classe del report.
     * @param reportName      Il nome del report.
     */
    private ReportQueryBuilder(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName) {
		this.reportClassType = reportClassType;
		this.reportName = reportName;
		this.generatorExcel = generatorExcel;
		reportConditions = new ArrayList<>();
    }
    
    /**
     * Crea un'istanza di `ReportQueryBuilder`.
     *
     * @param generatorExcel  L'oggetto `GenerateExcel` utilizzato per la generazione di report.
     * @param reportClassType Il tipo di classe del report.
     * @param reportName      Il nome del report.
     * @return Un nuovo oggetto `ReportQueryBuilder`.
     */
    protected static ReportQueryBuilder define(GenerateExcel generatorExcel, Class<?> reportClassType, String reportName) {
    	ReportQueryBuilder builder = new ReportQueryBuilder(generatorExcel, reportClassType, reportName);
    	return builder;
    }

    /**
     * Aggiunge una condizione "AND" alla query.
     *
     * @param queryCondition La condizione da applicare.
     * @return L'istanza corrente di `ReportQueryBuilder` per consentire la concatenazione delle condizioni.
     */
    public ReportQueryBuilder addCondition(ReportQueryCondition queryCondition) {
    	reportConditions.add(queryCondition);
        return this;
    }

    /**
     * Costruisce la query SQL completa basata sulle condizioni specificate.
     *
     * @return Un oggetto `ReportGeneratorBuilder` che può essere utilizzato per generare un report.
     */
    public ReportGeneratorBuilder build() {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        for (int i = 0; i < reportConditions.size(); i++) {
        	ReportQueryCondition queryCondition = reportConditions.get(i);
        	if (queryCondition == null || StringUtils.isBlank(ReportQueryParser.parse(queryCondition))) {
            	continue;
        	}
        	if (sqlQueryBuilder.length() > 0) {
        		sqlQueryBuilder.append(" AND ");
        	}
        	sqlQueryBuilder.append(" ( " + ReportQueryParser.parse(queryCondition) + " ) ");
		}
        String reportQuery = sqlQueryBuilder.toString();
        return ReportGeneratorBuilder.define(generatorExcel, reportClassType, reportName, reportQuery);
    }

    /**
     * Costruisce la query SQL completa basata sulle condizioni specificate e con un ordinamento ORDER BY.
     *
     * @param reportQueryOrder La clausola ORDER BY per la query SQL per ordinare i dati.
     * @return Un oggetto `ReportGeneratorBuilder` che può essere utilizzato per generare un report.
     */
    public ReportGeneratorBuilder build(ReportQueryOrder ...reportQueryOrder) {
    	StringBuilder sqlQueryBuilder = new StringBuilder();
    	for (int i = 0; i < reportConditions.size(); i++) {
    		ReportQueryCondition queryCondition = reportConditions.get(i);
    		if (queryCondition == null || StringUtils.isBlank(ReportQueryParser.parse(queryCondition))) {
    			continue;
    		}
    		if (sqlQueryBuilder.length() > 0) {
    			sqlQueryBuilder.append(" AND ");
    		}
    		sqlQueryBuilder.append(" ( " + ReportQueryParser.parse(queryCondition) + " ) ");
    	}
    	String reportQuery = sqlQueryBuilder.toString();
    	String reportOrder = ReportQueryParser.parse(reportQueryOrder);
    	return ReportGeneratorBuilder.define(generatorExcel, reportClassType, reportName, reportQuery, reportOrder);
    }
}
