/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.documentos;

import entidades.analisisDeDocumentos.ManejadorDeCadenas;
import gestores.IODocumentos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Un documento es justamente un documento que nos interesa incluir en nuestra
 * base de datos documental
 * @author Matías
 */
public class Documento {
    private String URL;
    private int urlMapping;
    private File file;
    private Vocabulario vocabulario;
    private String textoDelDocumento;
    private float puntajeFrenteAConsulta;
    private String nombre;
    
    public void adicionarPuntaje(float puntajeAdicional){
        this.puntajeFrenteAConsulta = puntajeFrenteAConsulta + puntajeAdicional;
    }
    /**
     * @return the puntajeFrenteAConsulta
     */
    public float getPuntajeFrenteAConsulta() {
        return puntajeFrenteAConsulta;
    }

    /**
     * @param puntajeFrenteAConsulta the puntajeFrenteAConsulta to set
     */
    public void setPuntajeFrenteAConsulta(float puntajeFrenteAConsulta) {
        this.puntajeFrenteAConsulta = puntajeFrenteAConsulta;
    }

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }
    
    public String getAbsoluteURL() throws IOException{
        return file.getCanonicalPath();
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        file = new File(URL);
        this.URL = URL;
    }
    
    public String getNombreDocumento(){
        return file.getName();
    }
    
    public String getURLDirectorioPadre() throws IOException{
        return file.getParentFile().getCanonicalPath();
    }

    /**
     * @param vocabulario the vocabulario to set
     */
    public void setVocabulario(Vocabulario vocabulario) {
        this.vocabulario = vocabulario;
    }
    
    public Vocabulario getVocabulario(){
        return this.vocabulario;
    }
    
    public ArrayList<Palabra> getVocabularioArrayList(){
        return this.vocabulario.getPalabras();
    }
    /**
     * @return the textoDelDocumento
     */
    public String getTextoDelDocumento() {
        return textoDelDocumento;
    }

    /**
     * @param textoDelDocumento the textoDelDocumento to set
     */
    public void setTextoDelDocumento(String textoDelDocumento) {
        this.textoDelDocumento = textoDelDocumento;
    }
    
    public Documento(String URL){
        this();
        this.setURL(URL);
    }
    
    public Documento(){
        this.vocabulario = new Vocabulario();
    }
    
    public void armarVocabulario(){
        try {
            setVocabulario(
                    IODocumentos.armarVocabularioParaDocumento(
                            URL, ManejadorDeCadenas.MetodoDeSeparacionDeCadenas.CARACTERES_VALIDOS));
        } catch (IOException ex) {
            Logger.getLogger(Documento.class.getName()).log(Level.SEVERE, "No se pudo armar el vocabulario del documento con URL: " + URL, ex);
        }
    }
    
    public void leerTextoDesdeArchivo() throws IOException{
        String texto = IODocumentos.leerTextoDelDocumento(this);
        this.setTextoDelDocumento(texto);
    }
    
    public void limpiarDocumento(){
        this.textoDelDocumento = null;
        this.vocabulario.limpiarPalabras();
    }

    /**
     * @return the urlMapping
     */
    public int getUrlMapping() {
        return urlMapping;
    }

    /**
     * @param urlMapoing the urlMapping to set
     */
    public void setUrlMapping(int urlMapoing) {
        this.urlMapping = urlMapoing;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return getNombreDocumento();
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
