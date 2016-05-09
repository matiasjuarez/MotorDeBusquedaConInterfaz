
package entidades.vocabularioGeneral;

import entidades.mapeador.MapeadorDeURLs;
import entidades.documentos.Vocabulario;
import entidades.documentos.Palabra;
import entidades.documentos.Documento;
import gestores.IOMapeadorURLs;
import gestores.IOVocabularioGeneral;
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
 * Esta clase representa el vocabulario que se utiliza en el modelo vectorial.
 * Se arma en base a todos los vocabularios de los documentos de la base de datos documental.
 * @author Matías
 */
public class VocabularioGeneral {
    
    private HashMap entradas;
    private MapeadorDeURLs mapeador;
    
    public VocabularioGeneral(){
        entradas = new HashMap(10000);
        mapeador = new MapeadorDeURLs();
    }
    
    /*
    Toma el vocabulario de algun documento y lo añade al vocabulario general.
    Si el documento ya ha sido agregado al vocabulario, va a existir en el mapeo.
    Si existe en el mapeo, entonces el documento no se va a agregar de nuevo y el
    metodo devolvera false
    */
    public void anadirDocumentoAlVocabulario(Documento documento){
        
        Vocabulario vocabulario = documento.getVocabulario();
        
        for(Palabra palabra: vocabulario.getPalabras()){
            EntradaVocabularioGeneral nuevaEntrada = new EntradaVocabularioGeneral();
            nuevaEntrada.setDocumentosEnQueAparece(1);
            nuevaEntrada.setFrecuencia(palabra.getFrecuencia());
            nuevaEntrada.setFrecuenciaMaxima(palabra.getFrecuencia());
            nuevaEntrada.setPalabra(palabra.getPalabra());
            
            agregarEntrada(nuevaEntrada);
        }
    }
    
    public void limpiarVocabulario(){
        this.entradas = new HashMap(10000);
    }
    
    public ArrayList<EntradaVocabularioGeneral> getEntradasDelVocabulario(){
        ArrayList<EntradaVocabularioGeneral> entradasVocabulario = new ArrayList<>();
        
        Iterator it = entradas.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
    
            EntradaVocabularioGeneral entrada = (EntradaVocabularioGeneral)pair.getValue();
            
            entradasVocabulario.add(entrada);
        }
        
        return entradasVocabulario;
    }
    
    public ArrayList<EntradaVocabularioGeneral> getEntradasDelVocabularioOrdenadasPorCantidadDocumentos(){
        ArrayList<EntradaVocabularioGeneral> entradasVocabularioGeneral = getEntradasDelVocabulario();
        Collections.sort(entradasVocabularioGeneral, new EntradaVocabularioGeneralComparadorCantidadDocumentos());
        return entradasVocabularioGeneral;
    }
    
    public int getCantidadDeEntradas(){
        return entradas.size();
    }
    
    public EntradaVocabularioGeneral obtenerEntrada(String palabra){
        return (EntradaVocabularioGeneral)entradas.get(palabra);
    }
    
    // Falta controlar que la entrada no provenga de un documento ya procesado
    private void agregarEntrada(EntradaVocabularioGeneral entradaNueva){
        
        String palabra = entradaNueva.getPalabra();
        
        EntradaVocabularioGeneral entradaEnVocabulario = 
                (EntradaVocabularioGeneral)entradas.get(palabra);
            
            if(entradaEnVocabulario == null){
                entradas.put(palabra, entradaNueva);
            }
            else{
                entradaEnVocabulario.
                        setDocumentosEnQueAparece(
                                entradaEnVocabulario.getDocumentosEnQueAparece() + 
                                        entradaNueva.getDocumentosEnQueAparece());
                
                entradaEnVocabulario.setFrecuencia(
                        entradaEnVocabulario.getFrecuencia() + entradaNueva.getFrecuencia());
                
                if(entradaEnVocabulario.getFrecuenciaMaxima() < entradaNueva.getFrecuencia()){
                    entradaEnVocabulario.setFrecuenciaMaxima(entradaNueva.getFrecuencia());
                }
            }
    }
    
    public boolean importarVocabulario(String URL){
        try {
            ArrayList<EntradaVocabularioGeneral> entradasImportadas =
                    IOVocabularioGeneral.importarEntradasDeVocabulario(URL);
            
            if (entradasImportadas == null) {
                return false;
            }
            
            for(EntradaVocabularioGeneral entrada: entradasImportadas) {
                agregarEntrada(entrada);
            }
            
            return true;
        } catch (IOException ex) {
            String mensaje = "No se pudo importar el vocabulario desde la URL: " + URL;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
            return false;
        }
    }
    
    public void exportarVocabulario(String URL){
        try {
            IOVocabularioGeneral.exportarVocabularioGeneral(this, URL);
        } catch (XMLStreamException ex) {
            String mensaje = "El documento de importacion de vocabulario especificado por la url no tiene el formato correcto. " + URL;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
        } catch (FileNotFoundException ex) {
            String mensaje = "No se pudo localizar el destino de exportacion del vocabulario especificado por la URL: " + URL;
            Logger.getLogger(VocabularioGeneral.class.getName()).log(Level.SEVERE, mensaje, ex);
        }
    }
    
    public void unirVocabulario(VocabularioGeneral otroVocabulario){
        ArrayList<EntradaVocabularioGeneral> entradasOtro = otroVocabulario.getEntradasDelVocabulario();
        
        for(EntradaVocabularioGeneral entradaOtro: entradasOtro){
            agregarEntrada(entradaOtro);
        }
    }
}
