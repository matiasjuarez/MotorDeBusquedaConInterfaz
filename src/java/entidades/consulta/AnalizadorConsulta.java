/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.consulta;

import configuracion.Configuracion;
import entidades.mapeador.MapeadorDeURLs;
import entidades.vocabularioGeneral.VocabularioGeneral;
import entidades.vocabularioGeneral.EntradaVocabularioGeneral;
import entidades.vocabularioGeneral.EntradaVocabularioGeneralComparadorCantidadDocumentos;
import entidades.posteo.Posteo;
import entidades.posteo.ListaDePosteo;
import entidades.posteo.ElementoListaDePosteo;
import entidades.documentos.Documento;
import entidades.documentos.DocumentoComparadorPuntaje;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Matías
 */
public class AnalizadorConsulta {
    private static final Configuracion configuracion = Configuracion.getInstance();
    
    private final VocabularioGeneral vocabulario;
    private final MapeadorDeURLs mapeador;
    
    public AnalizadorConsulta(VocabularioGeneral vocabulario, MapeadorDeURLs mapeador){
        this.vocabulario = vocabulario;
        this.mapeador = mapeador;
    }

    // El parametro palabras, son las palabras que queremos buscar en el vocabulario
    private ArrayList<EntradaVocabularioGeneral> obtenerEntradasDelVocabulario(ArrayList<String> palabras){
        
        ArrayList<EntradaVocabularioGeneral> entradas = new ArrayList<>();
        
        for(String palabra: palabras){
            EntradaVocabularioGeneral entrada = vocabulario.obtenerEntrada(palabra);
            if (entrada != null) {
               entradas.add(entrada);
            }
        }
        
        return entradas;
    }
            
    private ArrayList<Posteo> 
        buscarListasDePosteoPorCadaTerminoDeLaConsulta(ArrayList<String> terminos){        
        // Estos son los posteos que contienen las palabras que necesitamos
        ArrayList<ArrayList<String>> listaDePalabrasPorLetraInicial = 
                separarTerminosPorLetraInicial(terminos);
        
        ArrayList<Posteo> posteosNecesarios = cargarPosteosNecesarios(listaDePalabrasPorLetraInicial);
        
        return posteosNecesarios;
    }
        
    private ArrayList<ArrayList<String>> separarTerminosPorLetraInicial(ArrayList<String> terminos){
        HashMap palabrasPorLetra = new HashMap(10);
        
        ArrayList<String> listaDePalabras;
        
        for(String termino: terminos){
            char letra = termino.charAt(0);
            listaDePalabras = (ArrayList<String>) palabrasPorLetra.get(letra);
            
            if(listaDePalabras == null){
                listaDePalabras = new ArrayList<>();
                palabrasPorLetra.put(letra, listaDePalabras);
            }
            
            listaDePalabras.add(termino);
        }
        
        Iterator iterator = palabrasPorLetra.entrySet().iterator();
        
        ArrayList<ArrayList<String>> listasPorLetraInicial = new ArrayList<>();
        while (iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)iterator.next();
    
            ArrayList<String> listaPorLetra = (ArrayList<String>) pair.getValue();
            
            listasPorLetraInicial.add(listaPorLetra);
        }
        
        return listasPorLetraInicial;
    }
    
    // Cada posteo va a contener unicamente las listas que necesita de acuerdo a las palabras
    // de la consulta.
    private ArrayList<Posteo> cargarPosteosNecesarios(ArrayList<ArrayList<String>> listasDePalabras){
        ArrayList<Posteo> posteos = new ArrayList<>();

        String carpetaPosteos = configuracion.getCarpetaAlmacenamientoPosteos();
        StringBuilder URLArchivoExterno;
        
        for(ArrayList<String> listaDePalabras: listasDePalabras) {
            char letra = listaDePalabras.get(0).charAt(0);
            URLArchivoExterno = new StringBuilder(carpetaPosteos);
            URLArchivoExterno.append(letra);
            URLArchivoExterno.append(".xml");
            
            Posteo posteo = new Posteo(letra);
            posteo.importarPosteo(
                    URLArchivoExterno.toString(), listaDePalabras, 
                    Configuracion.documentosAConsiderarPorListaDePosteo);
            
            posteos.add(posteo);
        }
        
        return posteos;
    }    
    
    private ArrayList<EntradaVocabularioGeneral> buscarEntradasEnVocabulario(ArrayList<String> terminos, VocabularioGeneral vocabulario) {       
        ArrayList<EntradaVocabularioGeneral> entradas = new ArrayList<>();
        
        for(String termino: terminos) {
            EntradaVocabularioGeneral entrada = vocabulario.obtenerEntrada(termino);
            if(entrada != null)
            {
                entradas.add(entrada);
            }
            
        }
        
        return entradas;
    }
    
     private ArrayList<Documento> obtenerDocumentosValoradosSegunConsulta(
                ArrayList<Posteo> posteos, 
                ArrayList<EntradaVocabularioGeneral> entradasDeVocabulario, 
                int cantidadDocumentos){
        
        HashMap documentos = new HashMap(100);
        
        for(Posteo posteo: posteos){
            
            ArrayList<ListaDePosteo> listasDePosteo = posteo.getListasDePosteo();
            
            for(ListaDePosteo listaDePosteo: listasDePosteo){
                procesarListaDePosteoParaCalcularPuntajeDeDocumentos(listaDePosteo, 
                        documentos, entradasDeVocabulario, cantidadDocumentos);
            }
        }
        
        // Se sacan los documentos del hashMap y los devolvemos en forma de arrayList
        ArrayList<Documento> documentosAnalizados = new ArrayList<>();
        
        Iterator iterator = documentos.entrySet().iterator();
        
        while(iterator.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)iterator.next();
            
            documentosAnalizados.add((Documento) pair.getValue());
        }
        
        return documentosAnalizados;
    }
    
    // Cada lista de posteo tiene un conjunto de entradas donde cada entrada tiene una referencia
    // URL a un documento y la cantidad de veces que esa palabra aparecio en dicho documento
     private void procesarListaDePosteoParaCalcularPuntajeDeDocumentos(
             ListaDePosteo listaDePosteo,
             HashMap documentos,
             ArrayList<EntradaVocabularioGeneral> entradasDeVocabulario,
             int cantidadDeDocumentos){
         
            Documento documentoEnTrabajo;
            ArrayList<ElementoListaDePosteo> elementosDePosteo = 
                    listaDePosteo.getElementosDePosteo();

            String palabraDeLaLista = listaDePosteo.getPalabra();

            for(ElementoListaDePosteo elementoPosteo: elementosDePosteo) {
                int URLmap = Integer.parseInt(elementoPosteo.getURLDocumento());
                String URL = mapeador.getURLUnmapping(URLmap);

                documentoEnTrabajo = (Documento) documentos.get(URL);
                // Vemos si existe un documento con la URL especificada para no devolverle algo
                // que no existe a nuestro querido usuario
                if(documentoEnTrabajo == null){
                    File file = new File(URL);
                    if(file.exists()){
                        documentoEnTrabajo = new Documento(URL);
                        documentos.put(URL, documentoEnTrabajo);
                    }
                }

                if(documentoEnTrabajo != null){
                    adicionarPuntajeADocumentoCorrespondienteAElementoDePosteo(
                            documentoEnTrabajo, entradasDeVocabulario, 
                            palabraDeLaLista, elementoPosteo, cantidadDeDocumentos);
                }
            }
                
     }
     
     // Cada elemento de una lista de posteo va a tener una referencia a una URL de un documento.
     // Los elementos de la lista son los mas relevantes para la consulta
    private void adicionarPuntajeADocumentoCorrespondienteAElementoDePosteo(Documento documento, 
            ArrayList<EntradaVocabularioGeneral> entradasDeVocabulario,
            String palabraDeLaLista, ElementoListaDePosteo elementoPosteo,
            int cantidadDocumentos){
        
        double puntajeAdicional = 0;

        for(EntradaVocabularioGeneral entrada: entradasDeVocabulario){
            if(entrada.getPalabra().equals(palabraDeLaLista)){
                int frecuenciaPosteo = elementoPosteo.getFrecuencia();
                float presenteEnDocumentos = entrada.getDocumentosEnQueAparece();

                puntajeAdicional = (frecuenciaPosteo * 
                        Math.log(cantidadDocumentos/presenteEnDocumentos));
            }
            documento.adicionarPuntaje((float)puntajeAdicional);
        }
    }
    
    // Las listas de posteo deben estar ordenadas en orden decreciente de nr(cantidad 
    // de documentos en que aparece el termino)
    public ArrayList<Documento> analizarConsulta(Consulta consulta){
        ArrayList<Posteo> posteos = 
                buscarListasDePosteoPorCadaTerminoDeLaConsulta(consulta.getPalabrasDeLaConsulta());
        
        ArrayList<EntradaVocabularioGeneral> entradasPorTermino = 
                buscarEntradasEnVocabulario(consulta.getPalabrasDeLaConsulta(), vocabulario);
        
        ArrayList<Documento> documentosRelevantes = 
                obtenerDocumentosValoradosSegunConsulta(posteos, entradasPorTermino, mapeador.getCantidadURLsMapeadas());

        Collections.sort(documentosRelevantes, new DocumentoComparadorPuntaje());
        
        ArrayList<Documento> documentosDevolver = new ArrayList<>();
        int length = documentosRelevantes.size();
        int maximosDocumentos = Configuracion.documentosADevolverAnteConsulta;
        
        for(int i = 0; i < length && i < maximosDocumentos; i++){
            documentosDevolver.add(documentosRelevantes.get(i));
        }
        return documentosDevolver;
    }
}