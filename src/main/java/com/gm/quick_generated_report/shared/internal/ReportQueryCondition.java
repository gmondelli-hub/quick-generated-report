/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportQueryCondition.java
*/
package com.gm.quick_generated_report.shared.internal;

/**
 * Questa classe consente di costruire una condizione logica SQL a partire da quelle definite da BinaryCondition. <br>
 * 
 * @see ReportQueryBuilder
 */
public class ReportQueryCondition {

	/**
     * Enumerazione che rappresenta le possibili condizioni binarie per la costruzione della query.
     */
	enum BinaryCondition {
    	IS_NOT_NULL(" IS NOT NULL "), 		
    	IS_NULL(" IS NULL "), 				
    	EQUALS_TO(" = "), 					
    	NOT_EQUALS_TO(" <> "), 				
    	GREATER_THAN(" > "),				
    	GREATER_THAN_OR_EQUAL_TO(" >= "),	
    	LESS_THAN(" < "), 					
    	LESS_THAN_OR_EQUAL_TO(" <= "), 		
    	LIKE(" LIKE "),						
    	NOT_LIKE(" NOT LIKE "),				
    	IS_IN_LIST(" IN "), 				
    	IS_NOT_IN_LIST(" NOT IN ");			
		
		private final String queryCondition;

		private BinaryCondition(String queryCondition) {
			this.queryCondition = queryCondition;
		}
		
		@Override
		public String toString() {
			return queryCondition;
		}
    }
	
	/**
     * Enumerazione che rappresenta le tipologie di direzione per la condizione LIKE.
     */
	public enum LikeConditionDirection {
		LEFT, RIGHT, CENTER
	}
	
	protected final String column;
	protected final Object rightValue;
	protected final BinaryCondition binaryCondition;
	
	private ReportQueryCondition(BinaryCondition binaryCondition,
								String column,
								Object rightValue) {
		this.binaryCondition = binaryCondition;
		this.column = column;
		this.rightValue = rightValue;
	}
	
	/**
     * Crea una condizione per verificare se il valore specificato è nullo.
     *
     * @param column 		Colonna della tabella da verificare per la nullità.
     * @return  `ReportQueryCondition` per la condizione di controllo di nullità, o `null` se `column` è `null`.
     */
	public static ReportQueryCondition isNull(String column) {
		return column == null ? null : new ReportQueryCondition(BinaryCondition.IS_NULL, column, null);
	}

	/**
     * Crea una condizione per verificare se il valore specificato non è nullo.
     *
     * @param column 		Colonna della tabella  da verificare per la non nullità.
     * @return `ReportQueryCondition` per la condizione di controllo di non nullità, o `null` se `column` è `null`.
     */
	public static ReportQueryCondition isNotNull(String column) {
		return column == null ? null : new ReportQueryCondition(BinaryCondition.IS_NOT_NULL, column, null);
	}
	
	/**
     * Crea una condizione per verificare se i valori di sinistra e destra sono uguali.
     *
     * @param column  		Colonna della tabella per il controllo di uguaglianza.
     * @param rightValue 	Il valore di destra per il controllo di uguaglianza.
     * @return `ReportQueryCondition` per la condizione di uguaglianza, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition equalsTo(String column, Object rightValue) {
		return (column == null || rightValue == null) ? null : new ReportQueryCondition(BinaryCondition.EQUALS_TO, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se i valori di sinistra e destra non sono uguali.
     *
     * @param column  		Colonna della tabella per il controllo di non uguaglianza.
     * @param rightValue 	Il valore di destra per il controllo di non uguaglianza.
     * @return `ReportQueryCondition` per la condizione di non uguaglianza, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition notEqualsTo(String column, Object rightValue) {
		return (column == null || rightValue == null) ? null : new ReportQueryCondition(BinaryCondition.NOT_EQUALS_TO, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se il valore di sinistra è maggiore del valore di destra.
     *
     * @param inclusive  	Impostare su `true` per includere l'uguaglianza nel controllo (maggiore o uguale).
     * @param column  		Colonna della tabella per il confronto.
     * @param rightValue 	Il valore di destra per il confronto.
     * @return `ReportQueryCondition` per la condizione di confronto, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition greaterThen(boolean inclusive, String column, Object rightValue) {
		return (column == null || rightValue == null) ? null : new ReportQueryCondition(inclusive 
				? BinaryCondition.GREATER_THAN_OR_EQUAL_TO
				: BinaryCondition.GREATER_THAN, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se il valore di sinistra è minore del valore di destra.
     *
     * @param inclusive  	Impostare su `true` per includere l'uguaglianza nel controllo (minore o uguale).
     * @param column  		Colonna della tabella per il confronto.
     * @param rightValue 	Il valore di destra per il confronto.
     * @return `ReportQueryCondition` per la condizione di confronto, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition lessThen(boolean inclusive, String column, Object rightValue) {
		return (column == null || rightValue == null) ? null : new ReportQueryCondition(inclusive 
				? BinaryCondition.LESS_THAN_OR_EQUAL_TO
				: BinaryCondition.LESS_THAN, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se il valore di sinistra è simile al valore di destra con la direzione specificata.
     *
     * @param likeDirection La direzione per il controllo di condizione LIKE (LEFT, RIGHT o CENTER).
     * @param column     	Colonna della tabella per il controllo LIKE.
     * @param rightValue    Il valore di destra per il controllo LIKE.
     * @return `ReportQueryCondition` per la condizione LIKE, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition like(LikeConditionDirection likeDirection, String column, Object rightValue) {
		if (column == null || rightValue == null) return null;
		switch (likeDirection) {
			case CENTER:
				rightValue = "%" + rightValue + "%";
				break;
			case LEFT:
				rightValue = "%" + rightValue;
				break;
			case RIGHT:
				rightValue = rightValue + "%";
				break;
		}
		return new ReportQueryCondition(BinaryCondition.LIKE, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se il valore di sinistra non è simile al valore di destra con la direzione specificata.
     *
     * @param likeDirection La direzione per il controllo di condizione NOT LIKE (LEFT, RIGHT o CENTER).
     * @param column     	Colonna della tabella per il controllo NOT LIKE.
     * @param rightValue    Il valore di destra per il controllo NOT LIKE.
     * @return `ReportQueryCondition` per la condizione NOT LIKE, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition notLike(LikeConditionDirection likeDirection, String column, Object rightValue) {
		if (column == null || rightValue == null) return null;
		switch (likeDirection) {
			case CENTER:
				rightValue = "%" + rightValue + "%";
				break;
			case LEFT:
				rightValue = "%" + rightValue;
				break;
			case RIGHT:
				rightValue = rightValue + "%";
				break;
		}
		return new ReportQueryCondition(BinaryCondition.NOT_LIKE, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se il valore di sinistra è nell'elenco specificato a destra.
     *
     * @param column  		Colonna della tabella da verificare nell'elenco.
     * @param rightValue	L'elenco di destra per il controllo IN.
     * @return `ReportQueryCondition` per la condizione IN, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition inList(String column, Object rightValue) {
		return (column == null || rightValue == null) ? null : new ReportQueryCondition(BinaryCondition.IS_IN_LIST, column, rightValue);
	}
	
	/**
     * Crea una condizione per verificare se il valore di sinistra non è nell'elenco specificato a destra.
     *
     * @param column  		Colonna della tabella da verificare non nell'elenco.
     * @param rightValue 	L'elenco di destra per il controllo NOT IN.
     * @return `ReportQueryCondition` per la condizione NOT IN, o `null` se uno qualsiasi tra `column` o `rightValue` è `null`.
     */
	public static ReportQueryCondition notInList(String column, Object rightValue) {
		return (column == null || rightValue == null) ? null : new ReportQueryCondition(BinaryCondition.IS_NOT_IN_LIST, column, rightValue);
	}
}
