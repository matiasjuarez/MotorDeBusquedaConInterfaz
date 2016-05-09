/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import configuracion.ConfiguracionFabricasStax;
import configuracion.Configuracion;
import entidades.vocabularioGeneral.EntradaVocabularioGeneral;
import entidades.mapeador.MapeadorDeURLs;
import entidades.mapeador.Mapeo;
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
public abstract class IOMapeadorURLs {
    
    public static void exportarMapeador(MapeadorDeURLs mapeador, String URL) throws XMLStreamException, FileNotFoundException{

            //exportarMapeadorSAX(mapeador, URL);
            exportarMapeadorStax(mapeador, URL);

    }
    
    public static void exportarMapeadorStax(MapeadorDeURLs mapeador, String URLExportacion) throws XMLStreamException, FileNotFoundException{
        ArrayList<Mapeo> mapeos = 
                mapeador.getMapeosOrdenadosPorCodigoMapping();
        
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        
        XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(URLExportacion)));
        
        // DOCUMENTO
        xmlw.writeStartDocument();
        xmlw.writeCharacters("\n");
        xmlw.writeCharacters("\n");
        
        //<MAPEADOR>
        xmlw.writeStartElement(Configuracion.marcaElementoRootMapeador);
        xmlw.writeCharacters("\n");
        
        for(Mapeo mapeo: mapeos){
            //<MAPEO>
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaMapeoMapeador);
            xmlw.writeCharacters("\n");
            
            //<URL>
            xmlw.writeCharacters("\t");
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaURLMapeador);
            xmlw.writeCharacters(mapeo.getURL());
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            
            //<MAPPING>
            xmlw.writeCharacters("\t");
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaMappingMapeador);
            xmlw.writeCharacters(Integer.toString(mapeo.getMap()));
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
    
    public static void exportarMapeadorSAX(MapeadorDeURLs mapeador, String URL) throws IOException{
           
        ArrayList<Mapeo> mapeos = mapeador.getMapeosOrdenadosPorCodigoMapping();
        
        Element documentoXML = new Element(Configuracion.marcaElementoRootMapeador);
        
        
        for(Mapeo elemento: mapeos){
            Element mapeo = new Element(Configuracion.marcaMapeoMapeador);
            mapeo.addContent(new Element(Configuracion.marcaURLMapeador).setText(elemento.getURL()));
            mapeo.addContent(new Element(Configuracion.marcaMappingMapeador).setText(Integer.toString(elemento.getMap())));

            documentoXML.addContent(mapeo);
        }

        Document archivoDeSalida = new Document(documentoXML);

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(archivoDeSalida, new FileWriter(URL));

    }
    
    // Si no puede leer, deja el mapeador tal como estaba. Si puede leer, borra
    // los datos guardados y los reemplaza con los datos del documento especificado por la URL
    public static MapeadorDeURLs importarMapeadorSAX(String URL){
        MapeadorDeURLs mapeador = new MapeadorDeURLs();
        
        
        SAXBuilder builder = new SAXBuilder();
        
        File file = new File(URL);
        
        try{
            Document document = builder.build(file);
            
            Element rootNode = document.getRootElement();
            
            List<Element> lista = rootNode.getChildren(Configuracion.marcaMapeoMapeador);
            
            for(Element elemento: lista){
                String URLMapeada = elemento.getChildText(Configuracion.marcaURLMapeador);
                int map = Integer.parseInt(elemento.getChildText(Configuracion.marcaMappingMapeador));
                
                mapeador.agregarURL(URLMapeada, map);
            }
        }
        catch(Exception e){
            System.out.println("Error al intentar leer el mapeador desde la URL: " + URL);
            e.printStackTrace();
            return null;
        }
        
        return mapeador;
    }
    
    
    public static ArrayList<Mapeo> importarMapeadorStax(String URL) throws IOException{
        try {
            ArrayList<Mapeo> mapeos = new ArrayList<>();
            File file = new File(URL);
            if(!file.exists()){
                throw new IOException("No se pudo leer el mapeador desde la URL: " + URL);
            }
            
            XMLInputFactory xmlif = ConfiguracionFabricasStax.getInputFactory();
            
            XMLStreamReader xmlr = xmlif.createXMLStreamReader(URL, new FileInputStream(file));
            
            String mapeo = Configuracion.marcaMapeoMapeador;
            String URLMapeada = Configuracion.marcaURLMapeador;
            String mapping = Configuracion.marcaMappingMapeador;
            
            Mapeo mapeoEnTrabajo = new Mapeo();
            
            while(xmlr.hasNext()){
                xmlr.next();
                if(xmlr.isStartElement()){
                    String xmlrName = xmlr.getName().toString();
                    
                    if(xmlrName.equals(mapeo)){
                        mapeoEnTrabajo = new Mapeo();
                    }
                    else if(xmlrName.equals(URLMapeada)){
                        xmlr.next();
                        mapeoEnTrabajo.setURL(xmlr.getText());
                    }
                    else if(xmlrName.equals(mapping)){
                        xmlr.next();
                        int m = Integer.parseInt(xmlr.getText());
                        mapeoEnTrabajo.setMap(m);
                    }
                }
                else if(xmlr.isEndElement()){
                    String endElement = xmlr.getName().toString();
                    if(endElement.equals(mapeo)){
                        mapeos.add(mapeoEnTrabajo);
                    }
                }
            }
            xmlr.close();
            return mapeos;
        } catch (XMLStreamException ex) {
            String mensaje = "El archivo del que se intenta leer no tiene el formato correcto: " + URL;
            Logger.getLogger(IOVocabularioGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
