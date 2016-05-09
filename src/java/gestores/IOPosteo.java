/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import configuracion.ConfiguracionFabricasStax;
import configuracion.Configuracion;
import entidades.documentos.Documento;
import entidades.posteo.Posteo;
import entidades.posteo.ElementoListaDePosteo;
import entidades.posteo.ListaDePosteo;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Matías
 */
public abstract class IOPosteo {    

    public static void exportarPosteo(Posteo posteo, String URL, boolean ordenar){
        try {
            //exportarSAX(posteo, URL);
            exportarPosteoStax(posteo, URL, ordenar);
        } catch (FileNotFoundException ex) {
            String mensaje = "No se encontro el archivo en la url especificada: " + URL;
            Logger.getLogger(IOPosteo.class.getName()).log(Level.SEVERE, mensaje, ex);
        } catch (Exception ex) {
            Logger.getLogger(IOPosteo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void exportarSAX(Posteo posteo, String URL) throws IOException{
        Element elementoRaiz = obtenerEstructuraDeExportacionDeListas(posteo);
  
            Document archivoDeSalida = new Document(elementoRaiz);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(archivoDeSalida, new FileWriter(URL));

    }
    
    private static Element 
        obtenerEstructuraDeExportacionDeListas(Posteo posteo){
        
        ArrayList<ListaDePosteo> listas = posteo.getListasDePosteo();
        
        Element posteoXML = new Element(Configuracion.marcaElementoRootPosteo);
        posteoXML.addContent(new Element(Configuracion.marcaLetraPosteo).setText(Character.toString(posteo.getLetra())));
        
        for(ListaDePosteo lista: listas){
            Element listaXML = new Element(Configuracion.marcaListaPosteo);
            listaXML.addContent(new Element(Configuracion.marcaPalabraPosteo).setText(lista.getPalabra()));
            
            ArrayList<ElementoListaDePosteo> elementos = lista.getElementosDePosteo();
            
            for(ElementoListaDePosteo elemento: elementos){
                Element elementoXML = new Element(Configuracion.marcaElementoPosteo);

                elementoXML.addContent(new Element(Configuracion.marcaURLDocumentoPosteo).setText(elemento.getURLDocumento()));
                elementoXML.addContent(new Element(Configuracion.marcaFrecuenciaPosteo).setText(Integer.toString(elemento.getFrecuencia())));

                listaXML.addContent(elementoXML);
            }
            
            posteoXML.addContent(listaXML);
        }
        
        return posteoXML;
    }
        
    public static void exportarPosteoStax(Posteo posteo, String URLExportacion, boolean ordenarElementosPorFrecuencia) throws XMLStreamException, FileNotFoundException{
        
        ArrayList<ListaDePosteo> listas = posteo.getListasDePosteo();
        
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        
        XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(URLExportacion)));
        
        // DOCUMENTO
        xmlw.writeStartDocument();
        xmlw.writeCharacters("\n");
        xmlw.writeCharacters("\n");
        
        //<POSTEO>
        xmlw.writeStartElement(Configuracion.marcaElementoRootPosteo);
        xmlw.writeCharacters("\n");
        
        //<LETRA>
        xmlw.writeCharacters("\t");
        xmlw.writeStartElement(Configuracion.marcaLetraPosteo);
        xmlw.writeCharacters(Character.toString(posteo.getLetra()));
        xmlw.writeEndElement();
        xmlw.writeCharacters("\n");
        
        
        for(ListaDePosteo lista: listas){
            //<LISTA>
            
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaListaPosteo);
            xmlw.writeCharacters("\n");
            
            //<PALABRA>
            
            xmlw.writeCharacters("\t");
            xmlw.writeStartElement(Configuracion.marcaPalabraPosteo);
            xmlw.writeCharacters(lista.getPalabra());
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
            
            ArrayList<ElementoListaDePosteo> elementos;
            if(ordenarElementosPorFrecuencia){
                elementos = lista.getElementosDePosteoOrdenadosPorFrecuencia();
            }
            else{
                elementos = lista.getElementosDePosteo();
            }
            
            for(ElementoListaDePosteo elemento: elementos){
                //<ELEMENTO>
                
                xmlw.writeCharacters("\t");
                xmlw.writeCharacters("\t");
                xmlw.writeStartElement(Configuracion.marcaElementoPosteo);
                xmlw.writeCharacters("\n");
                
                //<URL>
                
                xmlw.writeCharacters("\t");
                xmlw.writeCharacters("\t");
                xmlw.writeCharacters("\t");
                xmlw.writeStartElement(Configuracion.marcaURLDocumentoPosteo);
                xmlw.writeCharacters(elemento.getURLDocumento());
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                
                //<FRECUENCIA>
                
                xmlw.writeCharacters("\t");
                xmlw.writeCharacters("\t");
                xmlw.writeCharacters("\t");
                xmlw.writeStartElement(Configuracion.marcaFrecuenciaPosteo);
                xmlw.writeCharacters(Integer.toString(elemento.getFrecuencia()));
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
                
                
                xmlw.writeCharacters("\t");
                xmlw.writeCharacters("\t");
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
            }
            
            
            xmlw.writeCharacters("\t");
            xmlw.writeEndElement();
            xmlw.writeCharacters("\n");
        }
        
        xmlw.writeEndElement();
        xmlw.writeCharacters("\n");
        
        xmlw.writeEndDocument();
        
        xmlw.close();
    }
    
        
   /* public static ArrayList<ListaDePosteo> importarListasDePosteoSAX(String URLBusqueda, char letra) throws IOException, JDOMException{
        
        Directorio directorio = new Directorio(URLBusqueda);
        directorio.obtenerEstructuraDelDirectorio();
        
        ArrayList<Documento> documentos = directorio.getDocumentos();
        
        for (Documento documento: documentos) {
            if (documento.getNombreDocumento().startsWith(Character.toString(letra))){
                SAXBuilder builder = new SAXBuilder();
        
                File file = new File(documento.getURL());

                Document document = builder.build(file);

                Element posteoXML = document.getRootElement();

                return obtenerListasDePosteo(posteoXML);
            }
        }
        
        return new ArrayList<ListaDePosteo>();
    }*/
    
    /*private static ArrayList<ListaDePosteo> obtenerListasDePosteo(Element posteoXML){
        
        MapeadorDeURLs mapeador = MapeadorDeURLs.getInstance();
        
        ArrayList<ListaDePosteo> posteoListas = new ArrayList<>();
        
        List<Element> posteoListasXML = posteoXML.getChildren(Configuracion.marcaListaPosteo);
                
        for(Element posteoListaXML: posteoListasXML){
            ListaDePosteo posteoLista = 
                    new ListaDePosteo(posteoListaXML.getChildText(Configuracion.marcaPalabraPosteo));
           
            
            ArrayList<ElementoListaDePosteo> posteoElementos = obtenerElementosPosteoDesdeElementoListaXML(posteoListaXML);
            for(ElementoListaDePosteo posteoElemento: posteoElementos){
                int urlMapDelElemento = Integer.parseInt(posteoElemento.getURLDocumento());
                posteoElemento.setURLDocumento(mapeador.getURLUnmapping(urlMapDelElemento));
                
                posteoLista.insertarNuevoElemento(posteoElemento);
            }
            
            posteoListas.add(posteoLista);
        }
        
        return posteoListas;
    }*/
    
    /*private static ArrayList<ElementoListaDePosteo> obtenerElementosPosteoDesdeElementoListaXML(Element listaXML){
        
        ArrayList<ElementoListaDePosteo> posteoElementos = new ArrayList<>();
        
        List<Element> posteoElementosXML = listaXML.getChildren(Configuracion.marcaElementoPosteo);
        
        for(Element posteoElementoXML: posteoElementosXML){
            ElementoListaDePosteo posteoElemento = new ElementoListaDePosteo();
            int frecuencia = Integer.parseInt(
                    posteoElementoXML.getChildText(Configuracion.marcaFrecuenciaPosteo));

            String URL = posteoElementoXML.getChildText(Configuracion.marcaURLDocumentoPosteo);

            posteoElemento.setFrecuencia(frecuencia);
            posteoElemento.setURLDocumento(URL);
            
            posteoElementos.add(posteoElemento);
        }
        
        return posteoElementos;
    }*/
    
 // Si quitamos el mapeo, tarda 2600. Si lo dejamos tarda 1800. Que onda?????
    public static ArrayList<ListaDePosteo> importarListasDePosteoStax(
            String URLBusqueda, ArrayList<String> palabrasDeseadas, 
            int cantidadEntradasDeListaDePosteoPermitidas) 
            throws IOException, JDOMException{       
        
        HashMap mapaPalabrasDeseadas = null;
        boolean filtrarPorPalabra = false;
        boolean palabraAceptada = false;
        
        boolean filtrarPorCantidadDeEntradas = false;
        if(cantidadEntradasDeListaDePosteoPermitidas > 0){
            filtrarPorCantidadDeEntradas = true;
        }
        // Se usa para controlar en conjunto con la cantidadEntradasDeListaDePosteo
        // si debemos o no agregar la proxima palabra
        boolean agregarOtraEntrada = true;
        int cantidadEntradasTomadas = 0;
        
        if(palabrasDeseadas != null){
            mapaPalabrasDeseadas = new HashMap(palabrasDeseadas.size());
            for(String palabra: palabrasDeseadas){
                mapaPalabrasDeseadas.put(palabra, null);
            }
            
            filtrarPorPalabra = true;
        }
        
        
        Documento documento = new Documento(URLBusqueda);
        
        ArrayList<ListaDePosteo> listasDePosteo = new ArrayList<>();
        ListaDePosteo listaEnTrabajo = null;
        ElementoListaDePosteo elementoEnTrabajo = null;
        
        try {
            String URL = documento.getURL();
            File file = new File(URL);
            if(!file.exists()){
                throw new IOException("No se encuentra el archivo para las listas de posteo. URL: " + URL);
            }

            XMLInputFactory xmlif = ConfiguracionFabricasStax.getInputFactory();

            XMLStreamReader xmlr = xmlif.createXMLStreamReader(URL, new FileInputStream(file));

            while(xmlr.hasNext()){
                xmlr.next();
                if(xmlr.isStartElement()){
                    
                    if(!filtrarPorCantidadDeEntradas || filtrarPorCantidadDeEntradas && agregarOtraEntrada){
                        String xmlrName = xmlr.getName().toString();

                        if(xmlrName.equals(Configuracion.marcaListaPosteo)){
                            listaEnTrabajo = new ListaDePosteo();
                        }
                        else if(xmlrName.equals(Configuracion.marcaPalabraPosteo)){
                            xmlr.next();
                            if(filtrarPorPalabra){
                                String palabra = xmlr.getText();
                                if(mapaPalabrasDeseadas.containsKey(palabra)){
                                    listaEnTrabajo.setPalabra(palabra);
                                    palabraAceptada = true;
                                }
                                else{
                                    palabraAceptada = false;
                                }
                            }
                            else{
                                listaEnTrabajo.setPalabra(xmlr.getText());
                                palabraAceptada = true;
                            }
                        }

                        if(palabraAceptada){
                            if(xmlrName.equals(Configuracion.marcaElementoPosteo)){
                                xmlr.next();
                                elementoEnTrabajo = new ElementoListaDePosteo();
                            }
                            else if(xmlrName.equals(Configuracion.marcaURLDocumentoPosteo)){
                                xmlr.next();
                                elementoEnTrabajo.setURLDocumento(xmlr.getText());
                            }
                            else if(xmlrName.equals(Configuracion.marcaFrecuenciaPosteo)){
                                xmlr.next();
                                int f = Integer.parseInt(xmlr.getText());
                                elementoEnTrabajo.setFrecuencia(f);
                            }
                        }
                    }
                    else{
                        palabraAceptada = false;
                    }
                }
                else if(xmlr.isEndElement()){
                    String endElement = xmlr.getName().toString();
                    
                    if(palabraAceptada){
                        if(endElement.equals(Configuracion.marcaElementoPosteo)){
                            listaEnTrabajo.insertarNuevoElemento(elementoEnTrabajo);
                            cantidadEntradasTomadas++;
                            
                            if(cantidadEntradasTomadas >= cantidadEntradasDeListaDePosteoPermitidas){
                                agregarOtraEntrada = false;
                                listasDePosteo.add(listaEnTrabajo);
                            }
                        }
                        
                        else if(endElement.equals(Configuracion.marcaListaPosteo)){
                            listasDePosteo.add(listaEnTrabajo);
                            cantidadEntradasTomadas = 0;
                            agregarOtraEntrada = true;
                            // Nos fijamos si ya tomamos una cantidad de palabras 
                            // (O sea de lista de posteos) igual a la cantidad de palabras
                            // distintas que enviamos como parametro. Si es asi, paramos la lectura
                            if(mapaPalabrasDeseadas != null && 
                                    listasDePosteo.size() >= mapaPalabrasDeseadas.size()){
                                break;
                            }
                        }
                    }
                    else{
                        // Cada vez que llegamos al final de una lista de posteo,
                        // reiniciamos la cantidad de entradas que hemos tomado que representa
                        // la cantidad de entradas tomadas de una lista de posteo en particular
                        // y ponemos la variable agregarOtraEntrada en true para indicar que
                        // vamos a seguir tomando entradas para la proxima lista de posteo con la que trabajemos
                        if(endElement.equals(Configuracion.marcaListaPosteo) && !agregarOtraEntrada){
                            cantidadEntradasTomadas = 0;
                            agregarOtraEntrada = true;
                            
                            if(mapaPalabrasDeseadas != null && 
                                    listasDePosteo.size() >= mapaPalabrasDeseadas.size()){
                                break;
                            }
                        }
                    }

                }
            }
            
            xmlr.close();
            } catch (XMLStreamException ex) {
                listasDePosteo = null;
                System.out.println("El archivo del que intenta leer las listas de posteo no tiene el formato adecuado. URL: " + URLBusqueda);

            }
            return listasDePosteo; 
    }
}
