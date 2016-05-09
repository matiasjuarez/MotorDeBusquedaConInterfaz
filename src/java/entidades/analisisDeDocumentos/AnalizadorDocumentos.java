/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.analisisDeDocumentos;

import configuracion.Configuracion;
import entidades.mapeador.MapeadorDeURLs;
import entidades.vocabularioGeneral.VocabularioGeneral;
import entidades.posteo.Posteo;
import entidades.documentos.Documento;
import entidades.documentos.Directorio;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matías
 */
public class AnalizadorDocumentos {
    private static final Configuracion configuracion = Configuracion.getInstance();
    
    public static void procesarDocumentosDeDirectorio(String URLDirectorio){
        try {
            Directorio directorio = new Directorio(URLDirectorio);
            directorio.armarEstructuraDelDirectorio();
            ArrayList<Documento> documentos = directorio.getDocumentos();
            ArrayList<ArrayList<Documento>> lotes = dividirDocumentosEnLotes(documentos);
            
            for(int i = 0; i < lotes.size(); i++){
                procesarLote(lotes.get(i), i);
            }
            
            unirArchivosTemporales();
            
        } catch (IOException ex) {
            Logger.getLogger(AnalizadorDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void unirArchivosTemporales(){
        // VOCABULARIO
        VocabularioGeneral vocabulario = unirVocabulariosTemporalesConVocabularioPrincipal();
        vocabulario.exportarVocabulario(configuracion.getURLVocabularioGeneral());
        vocabulario.limpiarVocabulario();
        
        // MAPEADOR
        MapeadorDeURLs mapeador = unirMapeadoresTemporalesConMapeadorPrincipal();
        mapeador.exportarMapeador(configuracion.getURLmapeador());
        mapeador.limpiarMapeos();
        
        // POSTEO
        unirPosteosTemporalesConPosteoPrincipal();
        /*StringBuilder stringBuilder;
        String carpetaPosteos = configuracion.getCarpetaAlmacenamientoPosteos();
        for(Posteo posteoPorLetra: posteosPorLetra){
            stringBuilder = new StringBuilder();
            stringBuilder.append(carpetaPosteos);
            stringBuilder.append(posteoPorLetra.getLetra());
            stringBuilder.append(".xml");
            
            posteoPorLetra.exportarPosteo(stringBuilder.toString() , true);
        }*/
        limpiarArchivosTemporales();
    }
    
    private static void limpiarArchivosTemporales(){
        Directorio directorioVocabulariosTemp = new Directorio(configuracion.getCarpetaVocabulariosTemporales());
        Directorio directorioMapeadoresTemp = new Directorio(configuracion.getCarpetaMapeadoresTemporales());
        
        Directorio directorioPosteosTemporales = new Directorio(configuracion.getCarpetaPosteosTemporales());
        ArrayList<Directorio> subdirectoriosPosteo = directorioPosteosTemporales.getSubdirectorios();
        for(Directorio subdirectorio: subdirectoriosPosteo){
            subdirectorio.eliminarDocumentosDelDirectorio();
        }
        
        directorioVocabulariosTemp.eliminarDocumentosDelDirectorio();
        directorioMapeadoresTemp.eliminarDocumentosDelDirectorio();
    }
    
    private static Posteo obtenerPosteoPrincipal(ArrayList<Documento> posteos, String letra) throws IOException{
 
        String urlPosteo = configuracion.getCarpetaAlmacenamientoPosteos() + letra + ".xml";
        
        Posteo posteo = new Posteo(letra.charAt(0));
        
        File posteoPrincipal = new File(urlPosteo);
        if(!posteoPrincipal.exists()){
            if(posteos.size() > 0){
                posteoPrincipal = new File(posteos.get(0).getURL());
                Files.copy(posteoPrincipal.toPath(), new File(urlPosteo).toPath());
                posteos.remove(0);
            }
            else{
                Posteo posteoInventado = new Posteo(letra.charAt(0));
                posteoInventado.exportarPosteo(posteoPrincipal.getCanonicalPath(), false);
            }
            
        }
        
        posteo.importarPosteo(urlPosteo);
        return posteo;
    }
    
    private static MapeadorDeURLs obtenerMapeadorPrincipal(ArrayList<Documento> mapeadores) throws IOException{
  
        String URLMapeador = configuracion.getURLmapeador();
        
        MapeadorDeURLs mapeador = new MapeadorDeURLs();
        
        File mapeadorPrincipal = new File(URLMapeador);
        if(!mapeadorPrincipal.exists()){
            mapeadorPrincipal = new File(mapeadores.get(0).getURL());
            Files.copy(mapeadorPrincipal.toPath(), new File(URLMapeador).toPath());
            mapeadores.remove(0);
        }

        mapeador.importarMapeador(URLMapeador);

        return mapeador;
    }
    
    private static VocabularioGeneral obtenerVocabularioPrincipal(ArrayList<Documento> vocabularios) throws IOException{

        String URLVocabulario = configuracion.getURLVocabularioGeneral();
        
        VocabularioGeneral vocabulario = new VocabularioGeneral();
        File vocabularioGeneral = new File(URLVocabulario);
        if(!vocabularioGeneral.exists()){
            vocabularioGeneral = new File(vocabularios.get(0).getURL());
            Files.copy(vocabularioGeneral.toPath(), new File(URLVocabulario).toPath());
            vocabularios.remove(0);
        }

        vocabulario.importarVocabulario(URLVocabulario);

        return vocabulario;
    }
    
    private static void unirPosteosTemporalesConPosteoPrincipal(){
        try {
            String carpetaPosteos = configuracion.getCarpetaAlmacenamientoPosteos();
            StringBuilder stringBuilder;
            
            Directorio directorio = new Directorio(configuracion.getCarpetaPosteosTemporales());
            directorio.armarEstructuraDelDirectorio();
            
            ArrayList<Directorio> subdirectorios = directorio.getSubdirectorios();
            
            for(Directorio subdirectorio: subdirectorios){
                String nombreDirectorio = subdirectorio.getNombre();
                
                subdirectorio.armarEstructuraDelDirectorio();
                ArrayList<Documento> posteos = subdirectorio.getDocumentos();
        
                Posteo posteoPrincipal = obtenerPosteoPrincipal(posteos, nombreDirectorio);

                for(Documento documento: posteos){
                    Posteo posteo = new Posteo(nombreDirectorio.charAt(0));
                    posteo.importarPosteo(documento.getURL());
                    posteoPrincipal.unirPosteo(posteo);
                    posteo.limpiarProfundamente();
                }
                
                stringBuilder = new StringBuilder();
                stringBuilder.append(carpetaPosteos);
                stringBuilder.append(posteoPrincipal.getLetra());
                stringBuilder.append(".xml");
                
                posteoPrincipal.exportarPosteo(stringBuilder.toString(), true);
                
                posteoPrincipal.limpiarProfundamente();
            }
   
        } catch (IOException ex) {
            String mensaje = "Hubo un problema al intentar unir los mapeadores parciales";
            Logger.getLogger(AnalizadorDocumentos.class.getName()).log(Level.SEVERE, mensaje, ex);
            
        }
    }
    
    // mapeadorPrincipal es el mapeador en el que uniremos todos los otros mapeadores
    private static MapeadorDeURLs unirMapeadoresTemporalesConMapeadorPrincipal(){
 
        try {
            Directorio directorio = new Directorio(configuracion.getCarpetaMapeadoresTemporales());
            directorio.armarEstructuraDelDirectorio();
            ArrayList<Documento> mapeadores = directorio.getDocumentos();
        
            MapeadorDeURLs mapeadorPrincipal = obtenerMapeadorPrincipal(mapeadores);
            
            for(Documento documento: mapeadores){
                MapeadorDeURLs otroMapeador = new MapeadorDeURLs();
                otroMapeador.importarMapeador(documento.getURL());
                mapeadorPrincipal.unirMapeador(otroMapeador);
            }
            
            return mapeadorPrincipal;
            
        } catch (IOException ex) {
            String mensaje = "Hubo un problema al intentar unir los mapeadores parciales";
            Logger.getLogger(AnalizadorDocumentos.class.getName()).log(Level.SEVERE, mensaje, ex);
            return null;
        }
    }
    
    // vocabularioPrincipal es el vocabulario en el que uniremos todos los otros vocabularios
    private static VocabularioGeneral unirVocabulariosTemporalesConVocabularioPrincipal(){

        try {
            Directorio directorio = new Directorio(configuracion.getCarpetaVocabulariosTemporales());
            directorio.armarEstructuraDelDirectorio();
            ArrayList<Documento> vocabularios = directorio.getDocumentos();
        
            VocabularioGeneral vocabularioPrincipal = obtenerVocabularioPrincipal(vocabularios);
            
            for(Documento documento: vocabularios){
                VocabularioGeneral otroVocabulario = new VocabularioGeneral();
                otroVocabulario.importarVocabulario(documento.getURL());
                vocabularioPrincipal.unirVocabulario(otroVocabulario);
            }
            
            return vocabularioPrincipal;
            
        } catch (IOException ex) {
            String mensaje = "Hubo un problema al intentar unir los vocabularios parciales";
            Logger.getLogger(AnalizadorDocumentos.class.getName()).log(Level.SEVERE, mensaje, ex);
            return null;
        }
    }
    
    private static void procesarLote(ArrayList<Documento> lote, int numeroDeLote){
        AnalisisDocumental analisis = new AnalisisDocumental();
        analisis.setDocumentos(lote);
        analisis.analizarDocumentos();
        
        String identificadorLote = "lote-" + numeroDeLote + ".xml";
        
        String URLExportacionVocabulario = configuracion.getCarpetaVocabulariosTemporales() + identificadorLote;
        String URLExportacionMapeador = configuracion.getCarpetaMapeadoresTemporales()+ identificadorLote;
        String URLExportacionPosteo = configuracion.getCarpetaPosteosTemporales();
        
        /*HiloExportadorAnalisisDocumental exportador = 
        new HiloExportadorAnalisisDocumental(
        analisis, URLExportacionVocabulario, URLExportacionMapeador, URLExportacionPosteo, identificadorLote);
        exportador.run();  */ 
        
        analisis.exportarVocabulario(URLExportacionVocabulario);
        analisis.exportarPosteo(URLExportacionPosteo, identificadorLote);
        analisis.exportarMapeador(URLExportacionMapeador);
        analisis.limpiarTodo();
    }
    
    private static ArrayList<ArrayList<Documento>> dividirDocumentosEnLotes(ArrayList<Documento> documentos){
        ArrayList<ArrayList<Documento>> lotes = new ArrayList<>();
        
        ArrayList<Documento> nuevoLote = new ArrayList<>();
        int cantidadDocumentosAgregado = 0;
        int cantidadDocumentosMaxima = Configuracion.cantidadDeDocumentosPorLote;
        
        for(Documento documento: documentos){
            
            nuevoLote.add(documento);
            cantidadDocumentosAgregado++;
            
            if(cantidadDocumentosAgregado == cantidadDocumentosMaxima){
                lotes.add(nuevoLote);
                cantidadDocumentosAgregado = 0;
                nuevoLote = new ArrayList<>();
            }
        }
        
        if(cantidadDocumentosAgregado != 0 && cantidadDocumentosAgregado < cantidadDocumentosMaxima){
            lotes.add(nuevoLote);
        }
        
        return lotes;
    }
}
