/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.posteo;

import entidades.vocabularioGeneral.VocabularioGeneral;
import entidades.documentos.Palabra;
import entidades.documentos.Documento;
import gestores.IOPosteo;
import java.io.IOException;
import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.JDOMException;
/**
 *
 * @author Matías
 */
public class Posteo {
    private HashMap listasDePosteo;
    private final char letra; // La letra que se almacena en este Posteo en particular. * representa todas
    
    public Posteo(char letra){
        listasDePosteo = new HashMap(2000);
        this.letra = letra;
    }
    
    public Posteo(){
        this('*');
    }

    
    // Este metodo construye la ListaDePosteo en base al vocabulario del documento pasado por parametro
    public void procesarDocumento(Documento documento, boolean usarMapping){
        
        String URLDocumento = documento.getURL();
        ArrayList<Palabra> palabras = documento.getVocabularioArrayList();
        for(Palabra palabra: palabras){
            // listaDeInsercion es la ListaDePosteo que hay en la posicion
            // indicada por la key = palabra.getPalabra()
            ListaDePosteo listaDeInsercion;
            
            // Si no existe una clave para "palabra" entonces, la creamos y agregamos un ListaDePosteo nueva
            listaDeInsercion = (ListaDePosteo) listasDePosteo.get(palabra.getPalabra());
            if(listaDeInsercion == null){
                String word = palabra.getPalabra();
                listaDeInsercion = new ListaDePosteo(word);
                listasDePosteo.put(word, listaDeInsercion);
            }
            
            ElementoListaDePosteo elemento = new ElementoListaDePosteo();
            elemento.setFrecuencia(palabra.getFrecuencia());
            
            if(usarMapping){
                elemento.setURLDocumento(Integer.toString(documento.getUrlMapping()));
            }else{
                elemento.setURLDocumento(documento.getURL());
            }
            
            
            listaDeInsercion.insertarNuevoElemento(elemento);
        }
    }
    
    public ArrayList<ListaDePosteo> getListasDePosteo(){
        ArrayList<ListaDePosteo> listasDePosteoDevolver = new ArrayList<>();
        
        Iterator it = listasDePosteo.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            
            ListaDePosteo lista = (ListaDePosteo)(pair.getValue());
            
            listasDePosteoDevolver.add(lista);
        }
        
        return listasDePosteoDevolver;
    }
    
    public int getCantidadDeListas(){
        return listasDePosteo.size();
    }
    
    public char getLetra(){
        return this.letra;
    }
    
    public ArrayList<Posteo> dividirPosteoPorLetra(){
        HashMap mapaPosteos = new HashMap(30);
        ArrayList<Posteo> posteos = new ArrayList<>();
        
        Iterator iterator;
        String letraInicial;
        
        for(ListaDePosteo lista: getListasDePosteo()){
            letraInicial = lista.getPalabra().substring(0, 1);
            
            Posteo posteo = (Posteo) mapaPosteos.get(letraInicial);
            
            if (posteo == null) {
                posteo = new Posteo(letraInicial.charAt(0));
                mapaPosteos.put(letraInicial, posteo);
            }
            
            posteo.insertarLista(lista);
        }
        
        iterator = mapaPosteos.entrySet().iterator();
        
        while(iterator.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)iterator.next();
            posteos.add((Posteo) pair.getValue());
        }
        
        return posteos;
    }
    
    private void insertarLista(ListaDePosteo lista){
        listasDePosteo.put(lista.getPalabra(), lista);
    }
    
    public void insertarListasDePosteo(ArrayList<ListaDePosteo> listas){
        for(ListaDePosteo lista: listas){
            insertarLista(lista);
        }
    }
    
    public boolean importarPosteo(String URLImportacion){
        try {
            ArrayList<ListaDePosteo> posteoListas =
                    IOPosteo.importarListasDePosteoCompletaStax(URLImportacion);
                    //IOPosteo.importarListasDePosteoSAX(Configuracion.URLAlmacenamientoPosteos, letra);
            
            for(ListaDePosteo lista: posteoListas) {
                insertarLista(lista);
            }
            return true;
        } catch (IOException ex) {
            String mensaje = "No se pudo importar el posteo desde la URL: " + URLImportacion;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
            return false;
        } catch (JDOMException ex) {
             String mensaje = "El documento para este posteo no tiene el formato correcto";
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
            return false;
        }
    }
    
    public boolean importarPosteo(
            String URLImportacion, ArrayList<String> palabrasDeseadas, int cantidadPalabrasPermitidasPorListaDePosteo){
        try {
            ArrayList<ListaDePosteo> posteoListas =
                    IOPosteo.importarListasDePosteoStax(
                            URLImportacion, palabrasDeseadas, cantidadPalabrasPermitidasPorListaDePosteo);
                    //IOPosteo.importarListasDePosteoSAX(Configuracion.URLAlmacenamientoPosteos, letra);
            
            for(ListaDePosteo lista: posteoListas) {
                insertarLista(lista);
            }
            return true;
        } catch (IOException ex) {
            System.out.println("No se pudo leer el archivo de importacion");
            ex.printStackTrace();
            return false;
        } catch (JDOMException ex) {
            System.out.println("El documento para este posteo no tiene el formato correcto");
            ex.printStackTrace();
            return false;
        }
    }
    
    public ListaDePosteo getListaPosteoParaTermino(String termino){
        return (ListaDePosteo) listasDePosteo.get(termino);
    }
    
    public void exportarPosteo(String URLExportacion, boolean ordenar){
        IOPosteo.exportarPosteo(this, URLExportacion, ordenar);
    }
    
    public void limpiarListasDePosteo(){
        
        listasDePosteo.clear();
    }
    
    public void limpiarProfundamente(){
        Iterator it = listasDePosteo.entrySet().iterator();
        
        while(it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)it.next();
            
            ListaDePosteo lista = (ListaDePosteo) pair.getValue();
            lista.limpiarElementos();
        }
        limpiarListasDePosteo();
    }
    
    public void unirPosteo(Posteo otroPosteo){
        ArrayList<ListaDePosteo> listasOtroPosteo = otroPosteo.getListasDePosteo();
        ArrayList<ElementoListaDePosteo> elementosListaOtroPosteo;
        String palabra;
        
        for(ListaDePosteo listaOtroPosteo: listasOtroPosteo){
            palabra = listaOtroPosteo.getPalabra();
            elementosListaOtroPosteo = listaOtroPosteo.getElementosDePosteo();
            
            for(ElementoListaDePosteo elementoListaOtroPosteo: elementosListaOtroPosteo){
                insertarElementoDeListaDePosteo(elementoListaOtroPosteo, palabra);
            }
        }
    }
    
    private void insertarElementoDeListaDePosteo(ElementoListaDePosteo elemento, String palabra){
        
        ListaDePosteo lista = (ListaDePosteo) this.listasDePosteo.get(palabra);
        
        if(lista == null){
            lista = new ListaDePosteo(palabra);
            listasDePosteo.put(palabra, lista);
        }
        lista.insertarNuevoElemento(elemento);
    }
}
