/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.documentos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta clase representa el conjunto de palabras de un documento determinado
 * @author Matías
 */
public class Vocabulario {
    private HashMap palabras;
    
    public Vocabulario(){
        this(1000);
    }
    
    public Vocabulario(int capacidadInicial){
        palabras = new HashMap(capacidadInicial);
    }
    
    /*
    Agrega una palabraOtroVocabulario al hashMap
    */
    public void introducirPalabra(String nombrePalabra){
        if(palabras.containsKey(nombrePalabra)){
            Palabra palabra = (Palabra)palabras.get(nombrePalabra);
            palabra.setFrecuencia(palabra.getFrecuencia() + 1);
        }
        else{
            Palabra palabra = new Palabra(nombrePalabra);
            palabra.setFrecuencia(1);
            palabras.put(nombrePalabra, palabra);
        }
    }
    
    public void introducirPalabras(ArrayList<String> palabras){
        for(String palabra: palabras){
            introducirPalabra(palabra);
        }
    }
    
    public ArrayList<Palabra> getPalabras(){
        ArrayList<Palabra> words = new ArrayList<>();
        
        Iterator it = palabras.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
    
            Palabra palabra = (Palabra)pair.getValue();
            
            words.add(palabra);
        }
        
        return words;
    }
    
    public boolean contienePalabra(String palabra){
        return palabras.containsKey(palabra);
    }
    
    public Palabra obtenerPalabra(String palabra){
        return (Palabra)palabras.get(palabra);
    }
    
    public int getCantidadPalabras(){
        return palabras.size();
    }
    
    public void limpiarPalabras(){
        palabras.clear();
    }
}
