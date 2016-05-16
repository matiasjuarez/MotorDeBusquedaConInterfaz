<%-- 
    Document   : index
    Created on : 06/05/2016, 01:18:23
    Author     : Matías
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="utilidadesJSP.Index" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <%@include file="estilosScripts.jsp" %>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="css/index.css" />
        
        <script src="/MotorDeBusqueda/javascript/jquery.min.js"></script>
        <script src="/MotorDeBusqueda/javascript/bootstrap.min.js"></script>
        <script src="/MotorDeBusqueda/javascript/snowstorm.js"></script>
        <script src="javascript/index.js"></script>
        

    </head>
    <body>
        <audio id="musicaOscuridad" class="oculto">
            <source src="resources/audio/audioOscuridad.mp3" type="audio/mpeg"/>
        </audio>
        <audio id="viento" class="oculto" autoplay="autoplay" loop="loop">
            <source src="resources/audio/wind.mp3" type="audio/mpeg"/>
        </audio>
        
        <div id="divOscuridad" class="oscuridad oculto"></div>
        
        <div class="container" id="divContainer">
            
            <%-- Titulo --%>
            <div class="row" id="divBotones">
                <div class="col-xs-4 parpadeante">
                    
                    <div class="row">
                        <div class="col-xs-12">
                            <p class="text-center text-creepy text-size30">
                                ¿QUE DESEA HACER?
                            </p>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-xs-6">
                            <%boolean sePuedeHacerConsulta = Index.sePuedeRealizarConsulta();
                            
                            if(sePuedeHacerConsulta){%>
                                <button type="button" id="botonConsulta" class="form-control textoBoton  text-creepy">
                                    CONSULTAR
                                </button>
                            <%}
                            else{                             
                            %>
                            
                                <button type="button" id="botonConsultaNotReady" class="form-control disabled botonDeshabilitado">
                                    <span class="textoBoton  text-creepy">CONSULTAR</span>
                                </button>
                            
                            <%}%>
                        </div>
                        <div class="col-xs-6">
                            <button type="button" id="botonRearmar" class="form-control">
                                <span class="textoBoton text-creepy">REARMAR</span>
                            </button>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
    </body>
</html>
