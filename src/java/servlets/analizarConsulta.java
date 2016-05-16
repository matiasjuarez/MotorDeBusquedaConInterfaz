/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import configuracion.Configuracion;
import entidades.consulta.AnalizadorConsulta;
import entidades.consulta.Consulta;
import entidades.consulta.DocumentoJSON;
import entidades.documentos.Documento;
import entidades.mapeador.MapeadorDeURLs;
import entidades.vocabularioGeneral.VocabularioGeneral;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jboss.weld.context.http.HttpRequestContext;
import org.json.JSONArray;

/**
 *
 * @author Matías
 */
@WebServlet(name = "analizarConsulta", urlPatterns = {"/analizarConsulta"})
public class analizarConsulta extends HttpServlet {

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
        
        /*response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        if(session.getAttribute("inicializado") == null){
            inicializarVocabularioMapeo(request);
        }
        
        ArrayList<Documento> documentosRelevantes = obtenerDocumentosRelevantes(request);
        JSONArray documentosJson = obtenerObjetosJSON(documentosRelevantes);
        
        String jsonString = documentosJson.toString();
        PrintWriter pw = response.getWriter(); 
        pw.print(jsonString);
        pw.close();*/
        /*request.setAttribute("documentos", documentosRelevantes);
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/PaginaConsulta.jsp");
        dispatcher.forward(request, response);*/
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
        
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        if(session.getAttribute("inicializado") == null){
            inicializarVocabularioMapeo(request);
        }
        
        ArrayList<Documento> documentosRelevantes = obtenerDocumentosRelevantes(request);

        request.setAttribute("documentos", documentosRelevantes);
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/PaginaConsulta.jsp");
        dispatcher.forward(request, response);
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
        
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        if(session.getAttribute("vocabulario") == null || session.getAttribute("mapeador") == null){
            inicializarVocabularioMapeo(request);
        }
        
        ArrayList<Documento> documentosRelevantes = obtenerDocumentosRelevantes(request);
        JSONArray documentosJson = obtenerObjetosJSON(documentosRelevantes);
        
        String jsonString = documentosJson.toString();
        PrintWriter pw = response.getWriter(); 
        pw.print(jsonString);
        pw.close();
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
    private static JSONArray obtenerObjetosJSON(ArrayList<Documento> documentos){
        DocumentoJSON dj = new DocumentoJSON();
        dj.setDocumentos(documentos);
        
        return dj.obtenerDocumentosJSON();
    }
    
    private static ArrayList<Documento> obtenerDocumentosRelevantes(HttpServletRequest request){
        
        if(request.getParameter("consulta") == null){
            return null;
        }
        
        String consultaTexto = request.getParameter("consulta");

        Consulta consulta = new Consulta(consultaTexto);
        
        AnalizadorConsulta analizador = prepararAnalizadorConsulta(request);
        
        ArrayList<Documento> documentosRelevantes = analizador.analizarConsulta(consulta);
        for(Documento documento: documentosRelevantes){
            documento.setPuntajeFrenteAConsulta(Math.round(documento.getPuntajeFrenteAConsulta()));
        }
        
        return documentosRelevantes;
    }
    
    private static AnalizadorConsulta prepararAnalizadorConsulta(HttpServletRequest request){
        HttpSession session = request.getSession();
        
        VocabularioGeneral vocabulario = (VocabularioGeneral) session.getAttribute("vocabulario");
        MapeadorDeURLs mapeador = (MapeadorDeURLs) session.getAttribute("mapeador");
        
        AnalizadorConsulta analizador = new AnalizadorConsulta(vocabulario, mapeador);
        
        return analizador;
    }
    
    private static void inicializarVocabularioMapeo(HttpServletRequest request){
        HttpSession session = request.getSession();

        Configuracion configuracion = Configuracion.getInstance();
        
        VocabularioGeneral vocabulario = (VocabularioGeneral) session.getAttribute("vocabulario");
        MapeadorDeURLs mapeador = (MapeadorDeURLs)session.getAttribute("mapeador");

        if(vocabulario == null){
            vocabulario = new VocabularioGeneral();
            vocabulario.importarVocabulario(configuracion.getURLVocabularioGeneral());
            session.setAttribute("vocabulario", vocabulario);
        }

        if(mapeador == null){
            mapeador = new MapeadorDeURLs();
            mapeador.importarMapeador(configuracion.getURLmapeador());
            session.setAttribute("mapeador", mapeador);
        }

        session.setAttribute("inicializado", true);
        
    }
}
