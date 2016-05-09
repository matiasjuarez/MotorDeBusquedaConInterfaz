/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.documentos;

import java.util.Comparator;

/**
 *
 * @author Matías
 */
public class DocumentoComparadorPuntaje implements Comparator<Documento>{

    @Override
    public int compare(Documento o1, Documento o2) {
        
        float puntaje1 = o1.getPuntajeFrenteAConsulta();
        float puntaje2 = o2.getPuntajeFrenteAConsulta();
        
        if(puntaje1 < puntaje2){
            return 1;
        }
        if(puntaje1 > puntaje2){
            return -1;
        }
        return 0;
    }
    
}
