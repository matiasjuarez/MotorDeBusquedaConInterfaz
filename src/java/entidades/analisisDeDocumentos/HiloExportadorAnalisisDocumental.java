/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.analisisDeDocumentos;

import entidades.analisisDeDocumentos.AnalisisDocumental;

/**
 *
 * @author Matías
 */
public class HiloExportadorAnalisisDocumental implements Runnable{
    
    private AnalisisDocumental analisis;
    private String URLVocabulario;
    private String URLMapeador;
    private String URLPosteo;
    private String identificadorLote;
    
    public HiloExportadorAnalisisDocumental(AnalisisDocumental analisis, String URLVocabulario, String URLMapeador, String URLPosteo, String identificadorLote){
        this.analisis = analisis;
        this.URLMapeador = URLMapeador;
        this.URLPosteo = URLPosteo;
        this.URLVocabulario = URLVocabulario;
        this.identificadorLote = identificadorLote;
    }
    
    @Override
    public void run() {
        analisis.exportarVocabulario(URLVocabulario);
        analisis.exportarPosteo(URLPosteo, identificadorLote);
        analisis.exportarMapeador(URLMapeador);
        analisis.limpiarTodo();
    }
    
}
