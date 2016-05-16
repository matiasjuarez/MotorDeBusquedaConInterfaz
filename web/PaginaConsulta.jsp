<%-- 
    Document   : PaginaConsulta
    Created on : 08/05/2016, 21:59:48
    Author     : Matías
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CONSULTA</title>
        
        <%@include file="estilosScripts.jsp" %>
        
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="css/paginaConsulta.css" />
        
        <script src="/MotorDeBusqueda/javascript/jquery.min.js"></script>
        <script src="javascript/paginaConsulta.js"></script>
    </head>
    <body>
        <div id="divOscuridad" class="oscuridad oscuridadDesapareceRapido"></div>
        
        <audio autoplay="autoplay" loop="loop" id="soundFire">
            <source src="resources/audio/lavaSound.mp3" type="audio/mpeg"/>
        </audio>
        
        <div id="divBackgroundVideo">
            <div id="divVideoBackground1" class="divVideoContainer">
                <video id="backgroundVideo1" class="backgroundVideo">
                    <source src="resources/video/flareParticles1.mp4" type="video/mp4">
                </video>
            </div>
            
            <div id="divVideoBackground2" class="divVideoContainer">
                <video id="backgroundVideo2" class="backgroundVideo">
                    <source src="resources/video/flareParticles2.mp4" type="video/mp4">
                </video>
            </div>
        </div>
        
        <div class="container" id="divContainer">
            
            <%-- Titulo --%>
            <div class="row">
                <div class="col-xs-1"></div>
                <div class="col-xs-10">
                    <h1 id="tituloConsulta" class="text-center well transparente text-creepy">
                        CONSULTA
                    </h1>
                </div>
                <div class="col-xs-1"></div>
            </div>
            
            
            <%-- Input --%>
            <div class="row">
                <div class="col-xs-3">
                </div>
                <div class="col-xs-6">
                    <form action="/MotorDeBusqueda/analizarConsulta" method="GET" id="form1">
                        <input type="text" id="inputConsulta" name = "consulta" class="form-control text-creepy" />
                    </form>
                    <div class="row">
                        <div class="col-xs-4"></div>
                        <div class="col-xs-4">
                            <button type="submit" form="form1" value="Submit" class="form-control">
                                <span class="textoBoton  text-creepy">ENVIAR GET</span>
                            </button>
                        </div>
                        <div class="col-xs-4"></div>
                    </div>
                </div>
                <div class="col-xs-3">
                </div>
            </div>
            
            
            <%-- Resultados --%>
            <div class="row">
                <div class="col-xs-1">
                    
                </div>
                
                <div id="divResultados" class="col-xs-10 transparente">
                    <div class="row filaResultados">
                        <div class="col-xs-4 encabezadoResultado text-creepy">
                            <span>DOCUMENTO</span>
                        </div>
                        
                        <div class="col-xs-4 encabezadoResultado text-creepy">
                            <span>CLICK PARA VER</span>
                        </div>
                        
                        <div class="col-xs-4 encabezadoResultado text-creepy">
                            <span>PUNTAJE</span>
                        </div>
                    </div>
                    <c:forEach items="${documentos}" var="d">
                        <div class="row filaResultados">
                            <div class="col-xs-4 resultado">
                                <span>${d.nombre}</span>
                            </div>
                            
                            <div class="col-xs-4 resultado">
                                <a href="${d.URL}" class="linkDocumento">LINK</a>
                            </div>
                            
                            <div class="col-xs-4 resultado">
                                <span>${d.puntajeFrenteAConsulta}</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <div class="col-xs-1">
                    
                </div>
            </div>
    </body>
</html>
