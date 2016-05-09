/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Matías
 */
public final class Configuracion {
    
    private static Configuracion configuracion;
    
    public static final boolean USAR_MAPEO = true;
    public static final int cantidadDeDocumentosPorLote = 10;
    public static final int documentosAConsiderarPorListaDePosteo = 2;
    public static final int documentosADevolverAnteConsulta = 5;
    
    private String carpetaBase;
    
    private String carpetaResources;
    
    private String carpetaExportaciones;
    private String carpetaDeDocumentos;
    
    private String carpetaTemporales;
    private String carpetaAlmacenamientoPosteos;
    
    private String carpetaMapeadoresTemporales;
    private String carpetaVocabulariosTemporales;
    private String carpetaPosteosTemporales;
    
    private String URLmapeador;
    
    private String URLVocabularioGeneral;
    
    public static final String marcaElementoRootMapeador = "mapeador";
    public static final String marcaMapeoMapeador = "mapeo";
    public static final String marcaURLMapeador = "URL";
    public static final String marcaMappingMapeador = "map";
    public static final String nombreMapeador = "mapeador.xml";
    
    public static final String marcaElementoRootVocabulario = "vocabulario";
    public static final String marcaEntradaVocabulario = "e";
    public static final String marcaPalabraVocabulario = "p";
    public static final String marcaFrecuenciaVocabulario = "f";
    public static final String marcaFrecuenciaMaximaVocabulario = "fm";
    public static final String marcaCantidadDocumentosVocabulario = "d";
    public static final String nombreVocabularioGeneral = "vocabularioGeneral.xml";
    
    public static final String marcaElementoRootPosteo = "posteo";
    public static final String marcaLetraPosteo = "le";
    public static final String marcaListaPosteo = "li";
    public static final String marcaPalabraPosteo = "p";
    public static final String marcaElementoPosteo = "e";
    public static final String marcaURLDocumentoPosteo = "URL";
    public static final String marcaFrecuenciaPosteo = "f";
    
    private Configuracion(){
        
    }
    
    public static Configuracion getInstance(){
        if(configuracion == null){
            configuracion = new Configuracion();
        }
        
        return configuracion;
    }

    /**
     * @return the URLmapeador
     */
    public String getURLmapeador() {
        return URLmapeador;
    }

    /**
     * @return the URLVocabularioGeneral
     */
    public String getURLVocabularioGeneral() {
        return URLVocabularioGeneral;
    }

    /**
     * @return the carpetaBase
     */
    public String getCarpetaBase() {
        return carpetaBase;
    }

    /**
     * @param carpetaBase the carpetaBase to set
     */
    public void setCarpetaBase(String URLBase) {
        this.carpetaBase = URLBase + "\\";
        
        this.carpetaResources = carpetaBase + "resources\\";
        
        this.carpetaExportaciones = getCarpetaResources() + "exportaciones\\";
        this.carpetaDeDocumentos = getCarpetaResources() + "documentos\\";
        
        this.carpetaTemporales = getCarpetaExportaciones() + "temporales\\";
        this.carpetaAlmacenamientoPosteos = getCarpetaExportaciones() + "posteos\\";
        
        this.carpetaMapeadoresTemporales = getCarpetaTemporales() + "mapeadores\\";
        this.carpetaVocabulariosTemporales = getCarpetaTemporales() + "vocabularios\\";
        this.carpetaPosteosTemporales = getCarpetaTemporales() + "posteos\\";
        
        URLmapeador = getCarpetaExportaciones() + nombreMapeador;
        URLVocabularioGeneral = getCarpetaExportaciones() + nombreVocabularioGeneral;
    }

    /**
     * @return the carpetaMapeadoresTemporales
     */
    public String getCarpetaMapeadoresTemporales() {
        return carpetaMapeadoresTemporales;
    }

    /**
     * @return the carpetaVocabulariosTemporales
     */
    public String getCarpetaVocabulariosTemporales() {
        return carpetaVocabulariosTemporales;
    }

    /**
     * @return the carpetaPosteosTemporales
     */
    public String getCarpetaPosteosTemporales() {
        return carpetaPosteosTemporales;
    }

    /**
     * @return the carpetaAlmacenamientoPosteos
     */
    public String getCarpetaAlmacenamientoPosteos() {
        return carpetaAlmacenamientoPosteos;
    }

    /**
     * @return the carpetaDeDocumentos
     */
    public String getCarpetaDeDocumentos() {
        return carpetaDeDocumentos;
    }

    /**
     * @return the carpetaResources
     */
    public String getCarpetaResources() {
        return carpetaResources;
    }

    /**
     * @return the carpetaExportaciones
     */
    public String getCarpetaExportaciones() {
        return carpetaExportaciones;
    }

    /**
     * @return the carpetaTemporales
     */
    public String getCarpetaTemporales() {
        return carpetaTemporales;
    }
}
