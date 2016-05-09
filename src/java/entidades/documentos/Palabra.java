/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.documentos;

/**
 * Esta clase representa las palabras que aparecen dentro de cada documento
 * @author Matías
 */
public class Palabra {
    private String palabra;
    private int frecuencia;

    public Palabra(){}
    
    public Palabra(String palabra){
        this.palabra = palabra;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    @Override
    public String toString(){
        String cadena = "";
        cadena += "Nombre: " + getPalabra() + " .";
        cadena += "Frecuencia " + getFrecuencia();
        return cadena;
    }
}
