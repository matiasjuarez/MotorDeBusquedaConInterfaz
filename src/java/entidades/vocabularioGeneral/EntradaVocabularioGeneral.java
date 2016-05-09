/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.vocabularioGeneral;

/**
 * Cada entrada almacena la informacion necesaria de cada palabra para implementar
 * el vocabulario del modelo vectorial
 * @author Matías
 */
public class EntradaVocabularioGeneral implements Comparable<EntradaVocabularioGeneral>{
    private String palabra;
    private int frecuencia;
    private int frecuenciaMaxima;
    private int documentosEnQueAparece;

    /**
     * @return the palabra
     */
    public String getPalabra() {
        return palabra;
    }

    /**
     * @param palabra the palabra to set
     */
    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    /**
     * @return the frecuencia
     */
    public int getFrecuencia() {
        return frecuencia;
    }

    /**
     * @param frecuencia the frecuencia to set
     */
    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    /**
     * @return the frecuenciaMaxima
     */
    public int getFrecuenciaMaxima() {
        return frecuenciaMaxima;
    }

    /**
     * @param frecuenciaMaxima the frecuenciaMaxima to set
     */
    public void setFrecuenciaMaxima(int frecuenciaMaxima) {
        this.frecuenciaMaxima = frecuenciaMaxima;
    }

    /**
     * @return the documentosEnQueAparece
     */
    public int getDocumentosEnQueAparece() {
        return documentosEnQueAparece;
    }

    /**
     * @param documentosEnQueAparece the documentosEnQueAparece to set
     */
    public void setDocumentosEnQueAparece(int documentosEnQueAparece) {
        this.documentosEnQueAparece = documentosEnQueAparece;
    }
    
    public void aumentarDocumentosEnQueAparece(){
        documentosEnQueAparece++;
    }

    @Override
    public int compareTo(EntradaVocabularioGeneral o) {
        return this.getPalabra().compareTo(o.getPalabra());
    }
}
