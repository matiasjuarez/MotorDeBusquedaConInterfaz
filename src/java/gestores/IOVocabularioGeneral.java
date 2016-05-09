/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import configuracion.ConfiguracionFabricasStax;
import configuracion.Configuracion;
import entidades.vocabularioGeneral.EntradaVocabularioGeneral;
import entidades.vocabularioGeneral.VocabularioGeneral;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Matías
 */
public abstract class IOVocabularioGeneral {
    // Estas propiedades estaticas de la clase se utilizan para poder cambiar a gusto
    // las marcas que se utilizaran en el documento xml tanto para lectura como escritura
    
    public static void exportarVocabularioGeneral(VocabularioGeneral vocabulario, String URL) throws XMLStreamException, FileNotFoundException{

            exportarVocabularioStax(vocabulario, URL);
            //exportarVocabularioSAX(vocabulario, URL);

    }
    
    private static void exportarVocabularioStax(VocabularioGeneral vocabulario, String URLExportacion) throws XMLStreamException, FileNotFoundException{
        ArrayList<EntradaVocabularioGeneral> entradas = 
                vocabulario.getEntradasDelVocabularioOrdenadasPorCantidadDocumentos();
        
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        
        XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(URLExportacion)));
        
        // DOCUMENTO
        xmlw.writeStartDocument();
        xmlw.writeCharacters("\n");
        xmlw.writeCharacters("\n");
        
        //<VOCABULARIO>
        xmlw.writeStartElement(Configuracion.marcaElementoRootVocabulario);
        xmlw.writeCharacters("\n");
        
        for(EntradaVocabularioGeneral entrada: entradas){
            //<ENTRADA>
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaEntradaVocabulario);
            xmlw.writeCharacters("\n");
            
            //<PALABRA>
            xmlw.writeCharacters("\t");
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaPalabraVocabulario);
            xmlw.writeCharacters(entrada.getPalabra());
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            
            //<FRECUENCIA>
            xmlw.writeCharacters("\t");
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaFrecuenciaVocabulario);
            xmlw.writeCharacters(Integer.toString(entrada.getFrecuencia()));
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            
            //<FRECUENCIA MAXIMA>
            xmlw.writeCharacters("\t");
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaFrecuenciaMaximaVocabulario);
            xmlw.writeCharacters(Integer.toString(entrada.getFrecuenciaMaxima()));
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            
            //<CANTIDAD DOCUMENTOS>
            xmlw.writeCharacters("\t");
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaCantidadDocumentosVocabulario);
            xmlw.writeCharacters(Integer.toString(entrada.getDocumentosEnQueAparece()));
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            
            xmlw.writeCharacters("\t");
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
        }
        
        xmlw.writeEndElement();
        xmlw.writeCharacters("\n");
        
        xmlw.writeEndDocument();
        
        xmlw.close();
    }
    
    private static void exportarVocabularioSAX(VocabularioGeneral vocabulario, String URL) throws IOException{
          
            Element documentoXML = new Element(Configuracion.marcaElementoRootVocabulario);
            
            ArrayList<EntradaVocabularioGeneral> entradas = vocabulario.getEntradasDelVocabulario();
            Collections.sort(entradas);
            
            for(EntradaVocabularioGeneral entrada: entradas){
                Element entradaXML = new Element(Configuracion.marcaEntradaVocabulario);
                entradaXML.addContent(new Element(Configuracion.marcaPalabraVocabulario).
                        setText(entrada.getPalabra()));
                entradaXML.addContent(new Element(Configuracion.marcaFrecuenciaVocabulario).
                        setText(Integer.toString(entrada.getFrecuencia())));
                 entradaXML.addContent(new Element(Configuracion.marcaFrecuenciaMaximaVocabulario).
                        setText(Integer.toString(entrada.getFrecuenciaMaxima())));
                entradaXML.addContent(new Element(Configuracion.marcaCantidadDocumentosVocabulario).
                        setText(Integer.toString(entrada.getDocumentosEnQueAparece())));
                
                documentoXML.addContent(entradaXML);
            }
            
            /*if(nombreDelArchivo == null || nombreDelArchivo.equals("")){
                nombreDelArchivo = "vocabularioGeneral.xml";
            }*/
            
            Document archivoDeSalida = new Document(documentoXML);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(archivoDeSalida, new FileWriter(URL));
            
    }
    
    public static ArrayList<EntradaVocabularioGeneral> importarEntradasDeVocabulario(String URL) throws IOException{
        return importarEntradasDeVocabularioStax(URL);
    }
    
    private static ArrayList<EntradaVocabularioGeneral> importarEntradasDeVocabularioSAX(String URL){
        ArrayList<EntradaVocabularioGeneral> entradas = new ArrayList<>();
        
        SAXBuilder builder = new SAXBuilder();
        
        File file = new File(URL);
        
        try{
            Document document = builder.build(file);
            
            Element rootNode = document.getRootElement();
            
            List<Element> lista = rootNode.getChildren(Configuracion.marcaEntradaVocabulario);
            
            for(Element elemento: lista){
                
                EntradaVocabularioGeneral entrada = new EntradaVocabularioGeneral();
                entrada.setDocumentosEnQueAparece(Integer.parseInt(elemento.getChildText(Configuracion.marcaCantidadDocumentosVocabulario)));
                entrada.setFrecuencia(Integer.parseInt(elemento.getChildText(Configuracion.marcaFrecuenciaVocabulario)));
                entrada.setFrecuenciaMaxima(Integer.parseInt(elemento.getChildText(Configuracion.marcaFrecuenciaMaximaVocabulario)));
                entrada.setPalabra((elemento.getChildText(Configuracion.marcaPalabraVocabulario)));
                
                entradas.add(entrada);
            }
        }
        catch(Exception e){
            System.out.println("Error al intentar leer el vocabulario desde la URL: " + URL);
            e.printStackTrace();
            return new ArrayList<>();
        }
        
        return entradas;
    }
    
    private static ArrayList<EntradaVocabularioGeneral> importarEntradasDeVocabularioStax(String URL) throws IOException{
        try{
            ArrayList<EntradaVocabularioGeneral> entradas = new ArrayList<>();
            
            File file = new File(URL);
            if(!file.exists()){
                throw new IOException("No se encuentra el archivo para importar el vocabulario. URL: " + URL);
            }
            
            XMLInputFactory xmlif = ConfiguracionFabricasStax.getInputFactory();
            
            //XMLStreamReader xmlr = xmlif.createXMLStreamReader(URL, new BufferedInputStream(new FileInputStream(file)));
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(URL, new FileInputStream(file));
            
            EntradaVocabularioGeneral entradaEnTrabajo = null;
            String entradaVocabulario = Configuracion.marcaEntradaVocabulario;
            String palabra = Configuracion.marcaPalabraVocabulario;
            String frecuencia = Configuracion.marcaFrecuenciaVocabulario;
            String frecuenciaM = Configuracion.marcaFrecuenciaMaximaVocabulario;
            String documentos = Configuracion.marcaCantidadDocumentosVocabulario;
            
            while(xmlr.hasNext()){
                xmlr.next();
                if(xmlr.isStartElement()){
                    String xmlrName = xmlr.getName().toString();
                    
                    if(xmlrName.equals(entradaVocabulario)){
                       entradaEnTrabajo = new EntradaVocabularioGeneral();
                    }
                    else if(xmlrName.equals(palabra)){
                        xmlr.next();
                        entradaEnTrabajo.setPalabra(xmlr.getText());
                    }
                    else if(xmlrName.equals(frecuencia)){
                        xmlr.next();
                        int f = Integer.parseInt(xmlr.getText());
                        entradaEnTrabajo.setFrecuencia(f);
                    }
                    else if(xmlrName.equals(frecuenciaM)){
                        xmlr.next();
                        int fm = Integer.parseInt(xmlr.getText());
                        entradaEnTrabajo.setFrecuenciaMaxima(fm);
                    }
                    else if(xmlrName.equals(documentos)){
                        xmlr.next();
                        int docs = Integer.parseInt(xmlr.getText());
                        entradaEnTrabajo.setDocumentosEnQueAparece(docs);
                    }
                }
                else if(xmlr.isEndElement()){
                    String endElement = xmlr.getName().toString();
                    if(endElement.equals(entradaVocabulario)){
                        entradas.add(entradaEnTrabajo);
                        entradaEnTrabajo = null;
                    }
                }
            }
            
            xmlr.close();
            
            return entradas;
        }
        catch(XMLStreamException ex){
            String mensaje = "El archivo del que se intenta leer no tiene el formato correcto: " + URL;
            Logger.getLogger(IOVocabularioGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
