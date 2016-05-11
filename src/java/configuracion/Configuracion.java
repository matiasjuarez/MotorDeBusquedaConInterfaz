/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Matías
 */
public final class Configuracion {
    
    private static Configuracion configuracion;
    
    private boolean USAR_MAPEO;
    private int cantidadDeDocumentosPorLote = 10;
    private int documentosAConsiderarPorListaDePosteo = 2;
    private int documentosADevolverAnteConsulta = 5;
    
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
        try {
            this.cargarConfiguracion();
            
            File carpetaBaseFile = new File(this.carpetaBase);
            if(!carpetaBaseFile.exists()){
                GestorEstructuraDeCarpetas gestor = new GestorEstructuraDeCarpetas();
                try {
                    gestor.construirEstructuraDeCarpetasParaElMotor();
                } catch (IOException ex) {
                    Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (ExcepcionDeCreacionDeEstructuraDeMotor ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Configuracion getInstance(){
        if(configuracion == null){
            configuracion = new Configuracion();
            configuracion.setCarpetaBase("C:\\Users\\Matías\\Desktop\\miCarpetaDePrueba\\motorDeBusqueda");
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
    private void setCarpetaBase(String URLBase) {
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
    
    private void cargarConfiguracion() throws ExcepcionDeCreacionDeEstructuraDeMotor, FileNotFoundException{
        try {
            XMLInputFactory xmlif = ConfiguracionFabricasStax.getInputFactory();
            
            InputStream input = Configuracion.class.getResourceAsStream("configuracion.xml");
            
            BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            
            XMLStreamReader xmlr = 
                    xmlif.createXMLStreamReader(reader);
                   
            String urlBase = "";
            String enableMapping = "";
            String batchSize = "";
            String documentsPerList = "";
            String documentsPerQuery = "";
            
            while(xmlr.hasNext()){
                xmlr.next();
                if(xmlr.isStartElement()){
                    String xmlrName = xmlr.getName().toString();
                    
                    if(xmlrName.equals("urlBase")){
                        xmlr.next();
                        urlBase = xmlr.getText();
                    }
                    else if(xmlrName.equals("enableMapping")){
                        xmlr.next();
                        enableMapping = xmlr.getText();
                    }
                    else if(xmlrName.equals("batchSize")){
                        xmlr.next();
                        batchSize = xmlr.getText();
                    }
                    else if(xmlrName.equals("documentsPerList")){
                        xmlr.next();
                        documentsPerList = xmlr.getText();
                    }
                    else if(xmlrName.equals("documentsPerQuery")){
                        xmlr.next();
                        documentsPerQuery = xmlr.getText();
                    }
                }
                
            }
            xmlr.close();
            
            this.setCarpetaBase(urlBase);
            USAR_MAPEO = Boolean.parseBoolean(enableMapping);
            cantidadDeDocumentosPorLote = Integer.parseInt(batchSize);
            documentosAConsiderarPorListaDePosteo = Integer.parseInt(documentsPerList);
            documentosADevolverAnteConsulta = Integer.parseInt(documentsPerQuery);
            
        } catch (XMLStreamException ex) {
            String mensaje = "El archivo de configuracion no tiene un formato correcto";
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the USAR_MAPEO
     */
    public boolean isUSAR_MAPEO() {
        return USAR_MAPEO;
    }

    /**
     * @return the cantidadDeDocumentosPorLote
     */
    public int getCantidadDeDocumentosPorLote() {
        return cantidadDeDocumentosPorLote;
    }
    
    /**
     * @return the documentosAConsiderarPorListaDePosteo
     */
    public int getDocumentosAConsiderarPorListaDePosteo() {
        return documentosAConsiderarPorListaDePosteo;
    }

    /**
     * @return the documentosADevolverAnteConsulta
     */
    public int getDocumentosADevolverAnteConsulta() {
        return documentosADevolverAnteConsulta;
    }
}
