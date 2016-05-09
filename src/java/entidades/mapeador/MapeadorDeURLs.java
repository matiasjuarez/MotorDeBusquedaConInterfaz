/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mapeador;

import entidades.vocabularioGeneral.VocabularioGeneral;
import gestores.IOMapeadorURLs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;

/**
 * Esta clase mantiene un listado de urls que ya han sido analizadas y les asigna un nombre mas corto
 * de modo que las listas de posteo ocupen menos espacio en disco
 * @author Matías
 */
public class MapeadorDeURLs {
    
    private HashMap URLsMapping; // De URL a int
    private HashMap URLsUnmapping; // De int a URL
    private static int nextMappingNumber = 0;
    
    public MapeadorDeURLs(){
        this(1000);
    }
    
    public MapeadorDeURLs(int capacidadInicial){
        URLsMapping = new HashMap(capacidadInicial);
        URLsUnmapping = new HashMap(capacidadInicial);
        
    }
    
    private static int getNextMappingNumber(){
        int number = nextMappingNumber;
        nextMappingNumber++;
        return number;
    }
    
    public void agregarURL(String url){
        if(!URLsMapping.containsKey(url)){
            int key = getNextMappingNumber();
            URLsMapping.put(url, key);
            URLsUnmapping.put(key, url);
        }
    }
    
    public void agregarURL(String url, int mapping){
        if(!URLsMapping.containsKey(url)){
            URLsMapping.put(url, mapping);
            URLsUnmapping.put(mapping, url);
        }
    }
    
    public void agregarMapeo(Mapeo mapeo){
        agregarURL(mapeo.getURL(), mapeo.getMap());
    }
    
    public void quitarURL(String url){
        if(URLsMapping.containsKey(url)){
            URLsUnmapping.remove((int)URLsMapping.get(url));
            URLsMapping.remove(url);
        }
    }
    
    public int getURLMapping(String URL){
        if(!URLsMapping.containsKey(URL)){
            return -1;
        }
        return (int) URLsMapping.get(URL);
    }
    
    public String getURLUnmapping(int map){
        if(!URLsUnmapping.containsKey(map)){
            return null;
        }
        
        return (String) URLsUnmapping.get(map);
    }
    
    public ArrayList<Mapeo> getMapeos(){
        Iterator iterator = URLsMapping.entrySet().iterator();
        
        ArrayList<Mapeo> elementos = new ArrayList<>();
        while (iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)iterator.next();
    
            Mapeo elemento = new Mapeo();
            elemento.setURL((String) pair.getKey());
            elemento.setMap((int) pair.getValue());
            
            elementos.add(elemento);
        }
        
        return elementos;
    }
    
    public ArrayList<Mapeo> getMapeosOrdenadosPorCodigoMapping(){
        ArrayList<Mapeo> mapeos = getMapeos();
        Collections.sort(mapeos);
        return mapeos;
    }
    
    public void limpiarMapeos(){
        URLsMapping = new HashMap(1000);
        URLsUnmapping = new HashMap(1000);
    }
    
    public boolean existeURL(String URL) {
        return URLsMapping.containsKey(URL);
    }
    
    public int getCantidadURLsMapeadas(){
        return URLsMapping.size();
    }
    
    public boolean importarMapeador(String URL){
        try {
            ArrayList<Mapeo> mapeosImportados = 
                    IOMapeadorURLs.importarMapeadorStax(URL);
            //importarMapeadorSAX(Configuracion.URLmapeador);
            if (mapeosImportados == null) {
                return false;
            }
            
            for(Mapeo mapeo: mapeosImportados) {
                agregarMapeo(mapeo);
            }
            
            return true;
        } catch (IOException ex) {
            String mensaje = "No se pudo importar el mapeador desde la URL: " + URL;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
            return false;
        }
    }
    
    public void exportarMapeador(String URL){
        try {
            IOMapeadorURLs.exportarMapeador(this, URL);
        } catch (XMLStreamException ex) {
            String mensaje = "El documento de importacion de vocabulario especificado por la url no tiene el formato correcto. " + URL;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
        } catch (FileNotFoundException ex) {
            String mensaje = "No se pudo localizar el destino de exportacion del vocabulario especificado por la URL: " + URL;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
        }
    }
    
    public void unirMapeador(MapeadorDeURLs otroMapeador){
        ArrayList<Mapeo> mapeosOtro = otroMapeador.getMapeos();
        
        for(Mapeo mapeoOtro: mapeosOtro){
            agregarMapeo(mapeoOtro);
        }
    }
}
