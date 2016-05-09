/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.posteo;

/**
 *
 * @author Matías
 */
public class ElementoListaDePosteo implements Comparable<ElementoListaDePosteo>{
    private String URLDocumento;
    private int frecuencia;

    /**
     * @return the URLDocumento
     */
    public String getURLDocumento() {
        return URLDocumento;
    }

    /**
     * @param URLDocumento the URLDocumento to set
     */
    public void setURLDocumento(String URLDocumento) {
        this.URLDocumento = URLDocumento;
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

    @Override
    public int compareTo(ElementoListaDePosteo o) {
        return o.frecuencia - this.frecuencia;
    }
}
