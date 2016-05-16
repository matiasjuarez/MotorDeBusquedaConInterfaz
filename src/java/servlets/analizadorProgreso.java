/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import configuracion.Configuracion;
import entidades.analisisDeDocumentos.ManejadorDeCadenas;
import entidades.documentos.Directorio;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Matías
 */
@WebServlet(name = "analizadorProgreso", urlPatterns = {"/analizadorProgreso"})
public class analizadorProgreso extends HttpServlet {
    


private static final Configuracion configuracion = Configuracion.getInstance();
private static final Directorio carpetaPosteos = 
        new Directorio(configuracion.getCarpetaAlmacenamientoPosteos());
private static final Directorio carpetaVocabularioTemporal = 
        new Directorio(configuracion.getCarpetaVocabulariosTemporales());

private static int cantidadDocumentosAnalizar = -1;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.setContentType("application/json;charset=UTF-8");
       int etapa = determinarEtapa(request.getSession()); // etapa 0 es analisis, 1 construccion, -1 ninguna
       
       float progreso = -1;
       
       if(etapa == 0){
            progreso = calcularProgresoAnalisis();
       }
       else if(etapa == 1){
           progreso = calcularProgresoArmado();
       }

        String jsonString = obtenerJSONdelProgreso(progreso, etapa);
        if(jsonString != null){
            PrintWriter pw = response.getWriter(); 
            pw.print(jsonString);
            pw.close(); 
        }
       
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public static void limpiarCantidadDocumentosAnalizar(){
        cantidadDocumentosAnalizar = -1;
    }
    
    private int determinarEtapa(HttpSession session){
        String etapaActual = (String) session.getAttribute("etapa");
        int etapaDevolver;
        
        if(etapaActual.equalsIgnoreCase("analisis")){
            etapaDevolver = 0;
        }
        else if(etapaActual.equalsIgnoreCase("construccion")){
            etapaDevolver = 1;
        }
        else{
            etapaDevolver = -1;
        }
        return etapaDevolver;
    }
    
    private float calcularProgresoArmado(){
        try {
            int caracteresValidos = ManejadorDeCadenas.caracteresValidos.length();

            carpetaPosteos.armarEstructuraDelDirectorio();
            
            int cantidadPosteos = carpetaPosteos.getDocumentos().size();
            
            float progreso = cantidadPosteos/(float)caracteresValidos;
            
            return progreso;
        } catch (IOException ex) {
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    private String obtenerJSONdelProgreso(float progreso, int etapa){
        try {
            JSONObject json = new JSONObject();
            json.put("progreso", progreso);
            json.put("etapa", etapa);
            String jsonString = json.toString();
            return jsonString;
        } catch (JSONException ex) {
            String mensaje = "Algo salio mal al intentar crear el objeto json del progreso";
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, mensaje, ex);
            return null;
        }
    }
    
    private float calcularProgresoAnalisis(){
        if(cantidadDocumentosAnalizar < 0){
            cantidadDocumentosAnalizar = contarCantidadDeDocumentosAnalizar();
        }
        
        int documentosPorLote = configuracion.getCantidadDeDocumentosPorLote();
        
        Double d = (Math.ceil(cantidadDocumentosAnalizar/(float)documentosPorLote));
        int maximaCantidadLotes = d.intValue();

        int actualCantidadLotes = contarCantidadLotesAnalizados();
        
        float progreso = actualCantidadLotes/(float)maximaCantidadLotes;
        
        return progreso;
    }
    
    private int contarCantidadLotesAnalizados(){
        try {
            carpetaVocabularioTemporal.armarEstructuraDelDirectorio();
            return carpetaVocabularioTemporal.getDocumentos().size();
        } catch (IOException ex) {
            String mensaje = "Algo salio mal mientras se intentaba contar la cantidad de vocabularios temporales";
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, mensaje, ex);
            return -1;
        }
    }
    
    private int contarCantidadDeDocumentosAnalizar(){
        try {
            String URLDocumentos = configuracion.getCarpetaDeDocumentos();
            Directorio directorio = new Directorio(URLDocumentos);
            directorio.armarEstructuraDelDirectorio();
            return directorio.getDocumentos().size();
        } catch (IOException ex) {
            String mensaje = "Algo salio mal mientras se intentaba contar la cantidad de documentos";
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, mensaje, ex);
            return -1;
        }
    }
}
