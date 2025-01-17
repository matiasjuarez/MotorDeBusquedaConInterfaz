/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import configuracion.Configuracion;
import configuracion.GestorEstructuraDeCarpetas;
import entidades.analisisDeDocumentos.AnalizadorDocumentos;
import entidades.documentos.Directorio;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Matías
 */
@WebServlet(name = "armarEstructurasParaElMotor", urlPatterns = {"/armarEstructurasParaElMotor"})
public class armarEstructurasParaElMotor extends HttpServlet {
    
    private static boolean analisisEnCurso = false;
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
        response.setContentType("text/html;charset=UTF-8");
        ServletContext sc = request.getServletContext();
        
        HttpSession session = request.getSession();
        
        if(!analisisEnCurso){
            session.setAttribute("vocabulario", null);
            session.setAttribute("mapeador", null);
            
            analisisEnCurso = true;
            
            Configuracion configuracion = Configuracion.getInstance();
            
            limpiarArchivos(true, true);

            AnalizadorDocumentos.
                    procesarDocumentosDeDirectorio(configuracion.getCarpetaDeDocumentos(), session);

            analisisEnCurso = false;
            
            analizadorProgreso.limpiarCantidadDocumentosAnalizar();
            
            limpiarArchivos(true, false);
        }
        
    }
    
    private static void limpiarArchivos(boolean temporales, boolean trabajo){
        System.gc();
            if(temporales)
            GestorEstructuraDeCarpetas.limpiarArchivosTemporales();
            
            if(trabajo)
            GestorEstructuraDeCarpetas.limpiarArchivosDeTrabajoDelMotor();
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

}
