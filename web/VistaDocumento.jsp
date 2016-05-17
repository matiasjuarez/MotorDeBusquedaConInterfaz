<%-- 
    Document   : VistaDocumento
    Created on : 17/05/2016, 01:30:40
    Author     : Matías
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="css/vistaDocumento.css" />
        <title>Vista documentos</title>
    </head>
    <body>
        <div class="container" id="divContainer">
            <div class="row">
                <div class="col-lg-2"></div>
                <div class="col-lg-8" id="divContenedorTexto">
                    <span class="textoGordo textoBlanco">URL: ${URL}</span>
                    <br/>
                    
                    <h1 class="textoBlanco">Texto del documento</h1>
                    <p class="textoBlanco textoGordo">${textoDocumento}</p>
                </div>
                <div class="col-lg-2"></div>
            </div>
        </div>
        
    </body>
</html>
