/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestores;

import entidades.documentos.Documento;
import entidades.analisisDeDocumentos.ManejadorDeCadenas;
import entidades.documentos.Palabra;
import entidades.documentos.Vocabulario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Matías
 */
public abstract class IODocumentos {
    
    public static Documento leerDocumento(String URL) throws FileNotFoundException, IOException{
        Documento documento = new Documento();
        
        File file = new File(URL);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        StringBuilder textoDelDocumento = new StringBuilder();
        
        String linea = reader.readLine();
        while(linea != null){
            textoDelDocumento.append(linea);
            textoDelDocumento.append("\n");
            linea = reader.readLine();
        }
        
        reader.close();
        
        documento.setTextoDelDocumento(textoDelDocumento.toString());
        documento.setURL(URL);
        
        return documento;
    }
    
    public static Vocabulario armarVocabularioParaDocumento(
            String URL, ManejadorDeCadenas.MetodoDeSeparacionDeCadenas metodoDeSeparacion) throws IOException{
        
        File file = new File(URL);
        
        if(!file.exists()){
            throw new IOException("El archivo especificado por la url no existe: " + URL);
        }
        
        Vocabulario vocabulario = new Vocabulario();
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String linea = reader.readLine();
        while(linea != null){
            vocabulario.introducirPalabras(
                    ManejadorDeCadenas.recuperarPalabras(
                            linea, metodoDeSeparacion));

            linea = reader.readLine();
        }
        
        reader.close();
        
        return vocabulario;
    }
    
    public static String leerTextoDelDocumento(Documento documento) throws FileNotFoundException, IOException{        
        File file = new File(documento.getURL());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        StringBuilder textoDelDocumento = new StringBuilder();
        
        String linea = reader.readLine();
        while(linea != null){
            textoDelDocumento.append(linea);
            textoDelDocumento.append("\n");
            linea = reader.readLine();
        }
        
        reader.close();
        
        return textoDelDocumento.toString();
    }
    
    /*
    Crea un archivo xml en el que se almacenara el vocabulario del documento. 
    @param
    documento es el documento cuyo vocabulario se guardara
    @param
    URL es la URL de la carpeta en la que se almacenara el vocabulario
    @return
    true si el vocabulario se exporto con exito, false en caso contrario
    */
    public static boolean exportarDocumento(Documento documento, String URL){
        try{            
            Element documentoXML = new Element("documento");
            documentoXML.addContent(new Element("URL").setText(documento.getURL()));
            documentoXML.addContent(new Element("nombre").setText(documento.getNombreDocumento()));
            documentoXML.addContent(new Element("carpeta").setText(documento.getURLDirectorioPadre()));
            
            for(Palabra palabra: documento.getVocabularioArrayList()){
                Element xmlPalabra = new Element("palabra");
                xmlPalabra.addContent(new Element("nombre").setText(palabra.getPalabra()));
                xmlPalabra.addContent(new Element("frecuencia").setText(Integer.toString(palabra.getFrecuencia())));
                
                documentoXML.addContent(xmlPalabra);
            }
            
            URL += "\\" + documento.getNombreDocumento() + ".xml";
            
            Document archivoDeSalida = new Document(documentoXML);
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(archivoDeSalida, new FileWriter(URL));
            
            return true;
        }
        catch(IOException e){
            return false;
        }
    }
}
