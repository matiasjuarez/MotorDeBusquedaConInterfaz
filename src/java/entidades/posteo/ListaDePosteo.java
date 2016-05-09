/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.posteo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Matías
 */
public class ListaDePosteo implements Comparable<ListaDePosteo>{
    private HashMap elementosDePosteo;
    private String palabra; // La palabra a la que corresponde esta elementosDePosteo
    
    public ListaDePosteo(String palabra){
        this();
        this.palabra = palabra;
    }
    
    public ListaDePosteo(){
        this.elementosDePosteo = new HashMap(200);
    }
    
    public String getPalabra(){
        return palabra;
    }
    
    public void limpiarElementos(){
        elementosDePosteo.clear();
    }
    
    public void insertarNuevoElemento(ElementoListaDePosteo otroElemento){
        ElementoListaDePosteo elemento = 
                (ElementoListaDePosteo) elementosDePosteo.get(otroElemento.getURLDocumento());
        
        if(elemento == null){
            elementosDePosteo.put(otroElemento.getURLDocumento(), otroElemento);
        }
        
    }
    
    public void insertarNuevosElementos(ArrayList<ElementoListaDePosteo> elementos){
        for(ElementoListaDePosteo elemento: elementos){
            insertarNuevoElemento(elemento);
        }
    }
        
    public ArrayList<ElementoListaDePosteo> getElementosDePosteoOrdenadosPorFrecuencia(){
        ArrayList<ElementoListaDePosteo> elementosDeListaDePosteo =
                getElementosDePosteo();
        
        Collections.sort(elementosDeListaDePosteo);
        
        return elementosDeListaDePosteo;
    }
    
    public ArrayList<ElementoListaDePosteo> getElementosDePosteo(){
        ArrayList<ElementoListaDePosteo> elementosDeListaDePosteo = new ArrayList<>();
        
        Iterator it = elementosDePosteo.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
    
            ElementoListaDePosteo elementoDeListaDePosteo = 
                    (ElementoListaDePosteo)pair.getValue();
            
            elementosDeListaDePosteo.add(elementoDeListaDePosteo);
        }
        
        return elementosDeListaDePosteo;
    }

    @Override
    public int compareTo(ListaDePosteo o) {
        return this.getPalabra().compareTo(o.getPalabra());
    }

    /**
     * @param palabra the palabra to set
     */
    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }
}
