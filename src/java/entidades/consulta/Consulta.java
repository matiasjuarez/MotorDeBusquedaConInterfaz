/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.consulta;

import entidades.analisisDeDocumentos.ManejadorDeCadenas;
import java.util.ArrayList;

/**
 *
 * @author Matías
 */
public final class Consulta {
    private String textoDeConsulta;
    private ArrayList<String> palabrasDeLaConsulta;
    
    public Consulta(){
        palabrasDeLaConsulta = new ArrayList<>();
    }
    
    public Consulta(String consulta){
        this();
        setTextoDeConsulta(consulta);
    }

    /**
     * @return the textoDeConsulta
     */
    public String getTextoDeConsulta() {
        return textoDeConsulta;
    }

    /**
     * @param textoDeConsulta the textoDeConsulta to set
     */
    public void setTextoDeConsulta(String textoDeConsulta) {
        this.textoDeConsulta = textoDeConsulta;
        this.palabrasDeLaConsulta = obtenerPalabrasDeLaConsulta(textoDeConsulta);
    }
    
    private ArrayList<String> obtenerPalabrasDeLaConsulta(String textoConsulta){
        return ManejadorDeCadenas.recuperarPalabras(
                textoConsulta, ManejadorDeCadenas.MetodoDeSeparacionDeCadenas.CARACTERES_VALIDOS);
    }
    
    public ArrayList<String> getPalabrasDeLaConsulta(){
        return this.palabrasDeLaConsulta;
    }
}
