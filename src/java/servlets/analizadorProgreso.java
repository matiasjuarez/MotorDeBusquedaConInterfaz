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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Matías
 */
@WebServlet(name = "analizadorProgreso", urlPatterns = {"/analizadorProgreso"})
public class analizadorProgreso extends HttpServlet {

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
       
       Configuracion configuracion = Configuracion.getInstance();
       
       float progreso;
       
       String analisis = request.getParameter("analisisCompleto");
       if(analisis.equalsIgnoreCase("false")){
            progreso = calcularProgresoAnalisis(configuracion);
       }
       else{
           progreso = calcularProgresoArmado(configuracion);
       }
       
       
        
        
        String jsonString = obtenerJSONdelProgreso(progreso);
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
    private float calcularProgresoArmado(Configuracion configuracion){
        try {
            int caracteresValidos = ManejadorDeCadenas.caracteresValidos.length();
            
            String urlPosteos = configuracion.getCarpetaAlmacenamientoPosteos();
            Directorio directorio = new Directorio(urlPosteos);
            directorio.armarEstructuraDelDirectorio();
            
            int cantidadPosteos = directorio.getDocumentos().size();
            
            return cantidadPosteos/(float)caracteresValidos;
        } catch (IOException ex) {
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    private String obtenerJSONdelProgreso(float progreso){
        try {
            JSONObject json = new JSONObject();
            json.put("progreso", String.format("%.2f", progreso));
            String jsonString = json.toString();
            return jsonString;
        } catch (JSONException ex) {
            String mensaje = "Algo salio mal al intentar crear el objeto json del progreso";
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, mensaje, ex);
            return null;
        }
    }
    
    private float calcularProgresoAnalisis(Configuracion configuracion){
        int cantidadDocumentos = contarCantidadDeDocumentosAnalizar(configuracion);
        int documentosPorLote = Configuracion.cantidadDeDocumentosPorLote;

        int maximaCantidadLotes = cantidadDocumentos/documentosPorLote;

        int actualCantidadLotes = contarCantidadLotesAnalizados(configuracion);
        
        return actualCantidadLotes/(float)maximaCantidadLotes;
    }
    
    private int contarCantidadLotesAnalizados(Configuracion configuracion){
        try {
            String URLVocabulariosTemporales = configuracion.getCarpetaVocabulariosTemporales();
            Directorio directorio = new Directorio(URLVocabulariosTemporales);
            directorio.armarEstructuraDelDirectorio();
            return directorio.getDocumentos().size();
        } catch (IOException ex) {
            String mensaje = "Algo salio mal mientras se intentaba contar la cantidad de vocabularios temporales";
            Logger.getLogger(analizadorProgreso.class.getName()).log(Level.SEVERE, mensaje, ex);
            return -1;
        }
    }
    
    private int contarCantidadDeDocumentosAnalizar(Configuracion configuracion){
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
