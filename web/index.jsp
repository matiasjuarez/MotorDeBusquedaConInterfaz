<%-- 
    Document   : index
    Created on : 06/05/2016, 01:18:23
    Author     : Matías
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="css/index.css" />
        
        <script src="/MotorDeBusqueda/javascript/jquery.min.js"></script>
        <script src="/MotorDeBusqueda/javascript/bootstrap.min.js"></script>
        <script src="/MotorDeBusqueda/javascript/snowstorm.js"></script>
        <script src="javascript/index.js"></script>
        

    </head>
    <body>
        <audio id="musicaOscuridad">
            <source src="resources/audio/audioOscuridad.mp3" type="audio/mpeg"/>
        </audio>
        
        <div class="container" id="divContainer">
            
            <%-- Titulo --%>
            <div class="row" id="divBotones">
                <div class="col-xs-4">
                    
                    <div class="row">
                        <div class="col-xs-12">
                            <p class="text-center">
                                ¿QUÉ DESEA HACER?
                            </p>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-xs-6">
                            <button type="button" id="botonConsulta" class="form-control">
                                <span class="textoBoton">CONSULTAR</span>
                            </button>
                        </div>
                        <div class="col-xs-6">
                            <button type="button" id="botonRearmar" class="form-control">
                                <span class="textoBoton">REARMAR</span>
                            </button>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
    </body>
</html>
