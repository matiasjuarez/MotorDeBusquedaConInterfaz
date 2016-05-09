/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.documentos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Representa un directorio que puede contener documentos que nos interesa
 * que sean tenidos en cuenta en nuestro vocabulario
 * @author Matías
 */
public class Directorio {
    private String URL;
    private File file;
    
    private ArrayList<Directorio> subdirectorios;
    private ArrayList<Documento> documentos;
    
    public Directorio(String URL){
        this();
        this.URL = URL;
        this.file = new File(URL);
    }
    
    public Directorio(){
        subdirectorios = new ArrayList<>();
        documentos = new ArrayList<>();
    }
    
    public String getNombre(){
        return file.getName();
    }
    
    public void armarEstructuraDelDirectorio() throws IOException{
        File file = new File(URL);
        
        if(file.isDirectory()){
            for(File archivo: file.listFiles()){
                if(archivo.isFile()){
                    Documento documento = new Documento(archivo.getCanonicalPath());
                    documentos.add(documento);
                }
                else if(archivo.isDirectory()){
                    Directorio directorio = new Directorio(archivo.getCanonicalPath());
                    subdirectorios.add(directorio);
                }
            }
        }
    }
    
    public ArrayList<Documento> getDocumentos(){
        return this.documentos;
    }
    
    public ArrayList<Directorio> getSubdirectorios(){
        return this.subdirectorios;
    }
    
    public void eliminarDocumentosDelDirectorio(){
        try {
            armarEstructuraDelDirectorio();
            
            ArrayList<Documento> documentos = getDocumentos();
            
            for(Documento documento: documentos){
                File file = new File(documento.getAbsoluteURL());
                file.delete();
            }
        } catch (IOException ex) {
            Logger.getLogger(Directorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}