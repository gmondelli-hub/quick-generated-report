/**
* @author Giuseppe Mondelli
* @mail giuseppe.mondelli@dxc.com
* @class com.gm.quick_generated_report.shared.internal.ReportClassLoader.java
*/
package com.gm.quick_generated_report.shared.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe estende `ClassLoader` ed è utilizzata per istanziare e caricare classi generate a runtime.<br>
 * Questa classe evita il ricaricamento nel classLoader di una classe già caricata.
 * 
 * @see ReportRowClassGenerator
 * @see ReportSheetClassGenerator
 */
class ReportClassLoader extends ClassLoader {

	private static ReportClassLoader instance = null;
	private final List<Class<?>> loadedClasses = new ArrayList<>(); 
	
	/**
     * Costruttore privato per inizializzare il classLoader con un genitore specificato.
     *
     * @param parent Il classLoader genitore.
     */
	private ReportClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	/**
     * Restituisce un'istanza condivisa di `ReportClassLoader` con il classLoader genitore specificato.
     * 
     * @param parent Il classLoader genitore.
     * @return L'istanza condivisa di `ReportClassLoader`.
     */
	protected static ReportClassLoader newInstance(ClassLoader parent) {
		if (instance == null) {
			instance = new ReportClassLoader(parent);
		}
		return instance;
	}
	
	/**
     * Carica una classe con il nome specificato. Evita il ricaricamento di classi già caricate.
     *
     * @param name Il nome completo della classe da caricare.
     * @return La classe caricata.
     * @throws ClassNotFoundException Se la classe non può essere trovata.
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = super.loadClass(name);
        if (!loadedClasses.contains(loadedClass)) {
            loadedClasses.add(loadedClass);
        }
        return loadedClass;
    }

    /**
     * Restituisce una lista delle classi caricate da questo classLoader.
     *
     * @return La lista delle classi caricate.
     */
    protected List<Class<?>> getLoadedClasses() {
        return loadedClasses;
    }
    
    /**
     * Cerca una classe con il nome specificato tra le classi caricate.
     *
     * @param className Il nome completo della classe da cercare.
     * @return La classe se trovata, altrimenti null.
     */
    @Override
    protected Class<?> findClass(String className) {
        for (Class<?> loadedClass : loadedClasses) {
            if (loadedClass.getName().equals(className)) {
                return loadedClass;
            }
        }
        return null;
    }
}
