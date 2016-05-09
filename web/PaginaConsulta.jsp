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
        <title>JSP Page</title>
        
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="css/paginaConsulta.css" />
        
        <script src="/MotorDeBusqueda/javascript/jquery.min.js"></script>
        <script src="javascript/paginaConsulta.js"></script>
        
    </head>
    <body>
        
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
                <div class="col-xs-12">
                    <h1 id="tituloConsulta" class="text-center well transparente">
                        CONSULTA
                    </h1>
                </div>
            </div>
            
            
            <%-- Input --%>
            <div class="row">
                <div class="col-xs-3">
                </div>
                <div class="col-xs-6">
                    <input type="text" id="inputConsulta" class="form-control" />
                </div>
                <div class="col-xs-3">
                </div>
            </div>
            
            
            <%-- Resultados --%>
            <div class="row">
                <div class="col-xs-1">
                    
                </div>
                
                <div id="divResultados" class="col-xs-10 transparente">
                    <c:forEach items="${documentos}" var="d">
                        <div class="row">
                            <div class="col-xs-4">
                                <span>Nombre: ${d.nombre}</span>
                            </div>
                            
                            <div class="col-xs-4">
                                <a href="${d.URL}">LINK</a>
                            </div>
                            
                            <div class="col-xs-4">
                                <span>Puntaje: ${d.puntajeFrenteAConsulta}</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <div class="col-xs-1">
                    
                </div>
            </div>
    </body>
</html>
