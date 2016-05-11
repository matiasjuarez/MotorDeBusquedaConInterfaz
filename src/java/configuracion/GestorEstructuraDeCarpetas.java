/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import entidades.documentos.Directorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import configuracion.Configuracion;
import configuracion.ExcepcionDeCreacionDeEstructuraDeMotor;
import entidades.analisisDeDocumentos.ManejadorDeCadenas;
import java.io.File;
/**
 *
 * @author Matías
 */
public class GestorEstructuraDeCarpetas {
    private static final Configuracion configuracion = Configuracion.getInstance();
    
    public static void limpiarArchivosTemporales(){        
        Directorio directorioVocabulariosTemp = new Directorio(configuracion.getCarpetaVocabulariosTemporales());
        Directorio directorioMapeadoresTemp = new Directorio(configuracion.getCarpetaMapeadoresTemporales());
        
        Directorio directorioPosteosTemporales = new Directorio(configuracion.getCarpetaPosteosTemporales());
  
        try {
            directorioPosteosTemporales.armarEstructuraDelDirectorio();
        } catch (IOException ex) {
            String mensaje = "Algo salio mal al intentar crear la estructura del directorio";
            Logger.getLogger(GestorEstructuraDeCarpetas.class.getName()).log(Level.SEVERE, mensaje, ex);
        }

        ArrayList<Directorio> subdirectoriosPosteo = directorioPosteosTemporales.getSubdirectorios();
        for(Directorio subdirectorio: subdirectoriosPosteo){
            subdirectorio.eliminarDocumentosDelDirectorio();
        }
        
        directorioVocabulariosTemp.eliminarDocumentosDelDirectorio();
        directorioMapeadoresTemp.eliminarDocumentosDelDirectorio();
    }
    
    public static void limpiarArchivosDeTrabajoDelMotor(){
        Directorio directorioPosteos = new Directorio(configuracion.getCarpetaAlmacenamientoPosteos());
        File vocabularioGeneral = new File(configuracion.getURLVocabularioGeneral());
        File mapeador = new File(configuracion.getURLmapeador());
        
        vocabularioGeneral.setWritable(true);
        vocabularioGeneral.delete();
        
        mapeador.setWritable(true);
        mapeador.delete();
        
        directorioPosteos.eliminarDocumentosDelDirectorio();
    }
    
    public void construirEstructuraDeCarpetasParaElMotor() throws ExcepcionDeCreacionDeEstructuraDeMotor, IOException{
        if(configuracion.getCarpetaBase() != null) {
            
            File file = new File(configuracion.getCarpetaBase());
            
            if(file.exists()){
                throw new ExcepcionDeCreacionDeEstructuraDeMotor(
                        "Ya existe un archivo con la URL especificada para la carpeta base: " + 
                                configuracion.getCarpetaBase());
            }
            
            crearCarpeta(configuracion.getCarpetaBase());
            
            crearCarpeta(configuracion.getCarpetaResources());
            
            crearCarpeta(configuracion.getCarpetaExportaciones());
            crearCarpeta(configuracion.getCarpetaDeDocumentos());
            
            crearCarpeta(configuracion.getCarpetaTemporales());
            crearCarpeta(configuracion.getCarpetaAlmacenamientoPosteos());
            
            crearCarpeta(configuracion.getCarpetaVocabulariosTemporales());
            crearCarpeta(configuracion.getCarpetaMapeadoresTemporales());
            crearCarpeta(configuracion.getCarpetaPosteosTemporales());
            
            crearSubcarpetasTemporalesDePosteo();
        }
        else{
            throw new ExcepcionDeCreacionDeEstructuraDeMotor("No se ha especificado una URL para la carpeta base");
        }
    }
    
    private void crearSubcarpetasTemporalesDePosteo() throws ExcepcionDeCreacionDeEstructuraDeMotor, IOException{
        String caracteresValidos = ManejadorDeCadenas.caracteresValidos;
        int length = caracteresValidos.length();
        
        for(int i = 0; i < length; i++){
            crearCarpeta(configuracion.getCarpetaPosteosTemporales() + 
                    caracteresValidos.charAt(i));
        }
    }
    
    private void crearCarpeta(String URL) throws ExcepcionDeCreacionDeEstructuraDeMotor, IOException{
        File file = new File(URL);
        
        if(!file.mkdir()){
            throw new ExcepcionDeCreacionDeEstructuraDeMotor(
                    "Algo paso mientras se intentaba crear el directorio con la siguiente URL: " + file.getCanonicalPath());
        }
    }
}
