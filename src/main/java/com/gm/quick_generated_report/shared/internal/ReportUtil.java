/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.util.ReportUtil.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.gm.quick_generated_report.shared.exception.ReportException;

import bld.generator.report.excel.RowSheet;

/**
 * Classe di utilità per la generazione di report e la gestione delle annotazioni.
 */
class ReportUtil {

	private ReportUtil() {}
	
    /**
     * Restituisce il nome della colonna del report formattata in maiuscolo con spaziatura tra le parole.<br>
     * Esempio: columnName: "partitaIva" in "PARTITA IVA"
     *
     * @param columnName Il nome della colonna da formattare.
     * @return Il nome della colonna formattato.
     */
	protected static String generateReportColumn(String columnName) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < columnName.length(); i++) {
		    char c = columnName.charAt(i);
		    if (Character.isUpperCase(c) && i > 0) {
		        result.append(' ');
		    }
		    result.append(Character.toUpperCase(c));
		}
		return result.toString();
	}
	
    /**
     * Genera una query SQL per il recupero dei dati da una tabella del database.
     *
     * @param rowClass        La classe che rappresenta una riga nella tabella.
     * @param tableName       Il nome della tabella del database.
     * @param whereCondition  La condizione WHERE per la query (può essere nullo o vuoto).
     * @param orderBy  		  La clausola ORDER BY per la query SQL per ordinare i dati..
     * @return La query SQL generata.
     */
	protected static <T extends RowSheet> String generateSQLQuery(Class<T> rowClass, String tableName, String whereCondition, String orderBy) {
    	// generazione delle colonne per SELECT
    	StringBuilder columnsBuilder = new StringBuilder();
        Field[] fields = rowClass.getDeclaredFields();
        for (Field field : fields) {
            if (columnsBuilder.length() > 0) {
            	columnsBuilder.append(", ");
            }
            columnsBuilder.append("item.").append(field.getName());
        }
        String tableColumns = columnsBuilder.toString();
    	// generazione della query
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT new " + rowClass.getName() + "( " +  tableColumns +  " ) ");
        queryBuilder.append(" FROM " + tableName + " item ");
        if (!StringUtils.isBlank(whereCondition)) queryBuilder.append(" WHERE " + whereCondition);
        if (!StringUtils.isBlank(orderBy)) queryBuilder.append(" ORDER BY " + orderBy);
        
        return queryBuilder.toString();
    }
	
	/**
     * Genera una lista di righe del report a partire dai dati in ingresso
     *
     * @param rowClass      La classe che rappresenta una riga nella tabella.
     * @param rows       	Lista di dati da trasformare in righe della tabella.
     * @return La lista di righe del report.
	 * @throws ReportException 
     */
	protected static <R extends RowSheet, S> List<R> generateRows(Class<R> generatedRowClass, List<S> originalRows) throws ReportException {
		List<R> generatedRows = new ArrayList<R>();
		for (S originalRow : originalRows) {
			R generatedRow = createGeneratedRow(generatedRowClass, originalRow);
			generatedRows.add(generatedRow);
		}
		return generatedRows;
	}
	
	/**
	 * Converte la riga della classe di input in una riga del report.
	 * 
	 * @param <R>                   Tipo classe riga generata del report.
	 * @param <S>                   Tipo classe riga originale.
	 * @param generatedRowClass     Classe della riga generata.
	 * @param originalRow           Oggetto della riga originale.
	 * @return                      Oggetto della riga generata a partire dalla riga originale.
	 * @throws ReportException      Eccezione che indica un errore durante la conversione.
	 */
    private static <R extends RowSheet, S> R createGeneratedRow(Class<R> generatedRowClass, S originalRow) throws ReportException {
        try {
            R generatedRow = generatedRowClass.getDeclaredConstructor().newInstance();
            Field[] generatedRowFields = generatedRowClass.getDeclaredFields();
            Field[] originalRowFields = Arrays.stream(originalRow.getClass().getDeclaredFields())
                    .filter(ReportRowClassFilter.filterFieldSerialVersionUID())
                    .filter(ReportRowClassFilter.filterFieldType())
                    .filter(ReportRowClassFilter.filterFieldNoStatic())
                    .toArray(Field[]::new);
            for (Field originalRowField : originalRowFields) {
                originalRowField.setAccessible(true);
                for (Field generatedRowField : generatedRowFields) {
                    generatedRowField.setAccessible(true);
                    if (!StringUtils.isBlank(originalRowField.getName()) && originalRowField.getName().equals(generatedRowField.getName()) && originalRowField.getType().equals(generatedRowField.getType())) {
                        Object value = originalRowField.get(originalRow);
                        generatedRowField.set(generatedRow, value);
                        break;
                    }
                }
            }
            return generatedRow;
        } catch (Exception e) {
            throw new ReportException(e);
        }
    }
    
   /**
     * Modifica il valore di una specifica chiave di annotazione.
     *
     * @param annotation L'annotazione il cui valore deve essere modificato.
     * @param key        La chiave dell'annotazione il cui valore deve essere cambiato.
     * @param newValue   Il nuovo valore per la chiave dell'annotazione.
     */
    @SuppressWarnings("unchecked")
	protected static void changeAnnotationValue(Annotation annotation, String key, Object newValue){
        Object handler = Proxy.getInvocationHandler(annotation);
        Field field;
        try {
        	field = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        field.setAccessible(true);
        Map<String, Object> memberValues;
        try {
            memberValues = (Map<String, Object>) field.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        memberValues.put(key,newValue);
    }
    
    /**
     * Converte un foglio XLSX nel formato CSV.
     *
     * @param sheet     Il foglio XLSX da convertire.
     * @param sheetName Il nome del foglio.
     * @return I dati CSV come array di byte.
     * @throws IOException Se si verifica un errore di I/O durante la conversione.
     */
	protected static byte[] convertXlsxToCSV(Sheet sheet, String sheetName) throws IOException {
        StringBuilder data = new StringBuilder();
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                CellType type = cell.getCellType();
                appendCellValue(data, cell, type);
                if (cell.getColumnIndex() != row.getLastCellNum() - 1) {
                    data.append(",");
                }
            }
            data.append('\n');
        }
        return data.toString().getBytes();
    }
    
    /**
     * Appende il valore di una cella al StringBuilder in base al tipo di cella.
     *
     * @param data Il StringBuilder in cui appendere il valore della cella.
     * @param cell La cella di cui ottenere il valore.
     * @param type Il tipo di cella (BOOLEAN, NUMERIC, STRING, o altro).
     */
    private static void appendCellValue(StringBuilder data, Cell cell, CellType type) {
        switch (type) {
            case BOOLEAN:
                data.append(cell.getBooleanCellValue());
                break;
            case NUMERIC:
                data.append(cell.getNumericCellValue());
                break;
            case STRING:
                String cellValue = cell.getStringCellValue();
                if (!cellValue.isEmpty()) {
                    cellValue = cellValue.replaceAll("\"", "\"\"");
                    data.append("\"").append(cellValue).append("\"");
                }
                break;
            default:
                data.append(cell);
        }
    }
}
