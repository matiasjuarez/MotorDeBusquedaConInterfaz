/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidadesJSP;

import configuracion.Configuracion;
import entidades.analisisDeDocumentos.ManejadorDeCadenas;
import entidades.documentos.Directorio;
import entidades.documentos.Documento;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matías
 */
public class Index {
    
    public static boolean sePuedeRealizarConsulta(){
        Configuracion configuracion = Configuracion.getInstance();
        
        File vocabulario = new File(configuracion.getURLVocabularioGeneral());
        File mapeo = new File(configuracion.getURLmapeador());
        
        if(!vocabulario.exists() || !mapeo.exists()){
            return false;
        }
        
        Directorio directorioPosteos = new Directorio(configuracion.getCarpetaAlmacenamientoPosteos());
        try {
            directorioPosteos.armarEstructuraDelDirectorio();
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        ArrayList<Documento> posteos = directorioPosteos.getDocumentos();
        
        String caracteresValidos = ManejadorDeCadenas.caracteresValidos;
        
        return existePosteoPorCadaCaracterValido(posteos, caracteresValidos);
    }
    
    private static boolean existePosteoPorCadaCaracterValido(ArrayList<Documento> posteos, String caracteresValidos){
        String nombreDocumento;
        
        char[] caracteres = caracteresValidos.toCharArray();
        int length = caracteres.length;
        boolean seEncontroPosteoParaCaracterValido = false;
        
        for(int i = 0; i < length; i++){
            for(Documento documento: posteos){
                nombreDocumento = documento.getNombre();
                if(nombreDocumento.charAt(0) == caracteres[i]){
                    seEncontroPosteoParaCaracterValido = true;
                    break;
                }
            }
            
            if(!seEncontroPosteoParaCaracterValido){
                return false;
            }
        }
        
        return true;
    }
}
