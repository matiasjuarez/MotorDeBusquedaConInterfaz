/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.analisisDeDocumentos;

import entidades.mapeador.MapeadorDeURLs;
import entidades.vocabularioGeneral.VocabularioGeneral;
import entidades.posteo.Posteo;
import entidades.documentos.Documento;
import java.util.ArrayList;

/**
 *
 * @author Matías
 */
public class AnalisisDocumental {
    private final VocabularioGeneral vocabulario;
    private final MapeadorDeURLs mapeador;
    private final Posteo posteo;
    private ArrayList<Documento> documentos;
    
    public AnalisisDocumental(){
        this.documentos = new ArrayList<>();
        this.vocabulario = new VocabularioGeneral();
        this.mapeador = new MapeadorDeURLs();
        this.posteo = new Posteo();
    }
    
    public void setDocumentos(ArrayList<Documento> documentos){
        this.documentos = documentos;
    }
    
    public void analizarDocumentos(){
        for(Documento documento: documentos){
            documento.armarVocabulario();
            
            // El vocabulario debe ir antes ya que si no, el documento se agregar al mapeador y nos
            // sale que el documento ya fue analizado
            vocabulario.anadirDocumentoAlVocabulario(documento);
            
            mapeador.agregarURL(documento.getURL());
            documento.setUrlMapping(mapeador.getURLMapping(documento.getURL()));
            
            posteo.procesarDocumento(documento, true);
        }
    }
    
    public void limpiarTodo(){
        vocabulario.limpiarVocabulario();
        mapeador.limpiarMapeos();
        posteo.limpiarProfundamente();
        
        if(documentos != null){
            for(Documento documento: documentos){
            documento.limpiarDocumento();
            documento = null;
            }
        }
        
    }
    
    public void exportarVocabulario(String URL){
        vocabulario.exportarVocabulario(URL);
    }
    
    public void exportarPosteo(String URL, String identificador){
       ArrayList<Posteo> posteosPorLetra = posteo.dividirPosteoPorLetra();
       
       StringBuilder url;
       for(Posteo posteoPorLetra: posteosPorLetra){
           url = new StringBuilder();
           url.append(URL);
           url.append(posteoPorLetra.getLetra());
           url.append("\\");
           url.append(identificador);
           
           posteoPorLetra.exportarPosteo(url.toString(), false);
       }
       
    }
    
    public void exportarMapeador(String URL){
        mapeador.exportarMapeador(URL);
    }
}
