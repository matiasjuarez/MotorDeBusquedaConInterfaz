/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    
    redimensionarContenedorResultados();
    agregarEventos();
    
    prepararAnimacionDeFondo();
    
});

function redimensionarContenedorResultados(){
    var divResultados = $("#divResultados"),
        divTop = $(divResultados).offset().top,
        altoPagina = $(window).height();

    $(divResultados).height((altoPagina - divTop - 10) + "px");
}

function prepararAnimacionDeFondo(){
    $("#divVideoBackground2").css({"opacity": 0});
    
    comenzarLoopVideo();
    
    sincronizarComienzosParadasDeVideos();
   /* debugger;
    addEndEventToVideos("backgroundVideo1", "backgroundVideo2", "divVideo1", "divVideo2");
    addEndEventToVideos("backgroundVideo2", "backgroundVideo1", "divVideo2", "divVideo1");*/
}

function comenzarLoopVideo(){
    var video = document.getElementById("backgroundVideo1");
    video.currentTime = 7;
    video.play();
}

function sincronizarComienzosParadasDeVideos(){
    var video1 = document.getElementById("backgroundVideo1"),
        video2 = document.getElementById("backgroundVideo2"),
        div1 = $("#divVideoBackground1"),
        div2 = $("#divVideoBackground2"),
        video1Playing = true,
        initialTime = 4,
        timeoutTime = 7000;
        
        establecerTimeOut();
        
    function establecerTimeOut(){
        if(video1Playing){
            setTimeout(function(){
                video1Playing = false;
                
                playStop(video2, div2, video1, div1);
                establecerTimeOut();
            }, timeoutTime);
        }
        else{
            setTimeout(function(){
                video1Playing = true;
                
                playStop(video1, div1, video2, div2);
                establecerTimeOut();
            }, timeoutTime);
        }
    }
    
    function playStop(videoPlay, divPlay, videoStop, divStop){
        videoPlay.currentTime = initialTime;
        videoPlay.play();
        
        $(divStop).removeClass("videoAparece");
        $(divStop).addClass("videoDesvanece");
        
        $(divPlay).removeClass("videoDesvanece");
        $(divPlay).addClass("videoAparece");
    }
}

/*function addEndEventToVideos(videoEndingId, videoStartingId, endingDiv, startingDiv){
    var videoEnding = document.getElementById(videoEndingId),
        videoStarting = document.getElementById(videoStartingId),
        initialTime = 7;

    $(videoEnding).on("ended", function(){
        videoStarting.currentTime = initialTime;
        videoStarting.play();
        
        $("#"+endingDiv).removeClass("videoAparece");
        $("#"+endingDiv).addClass("videoDesvanece");
        
        $("#"+startingDiv).removeClass("videoDesvanece");
        $("#"+startingDiv).addClass("videoAparece");
        
    });
}*/

function agregarEventos(){
    $(window).on("resize", function(){
        redimensionarContenedorResultados(); 
    });
    
    $("#inputConsulta").on("keydown", function(event){
        if(event.which === 13){
            debugger;
            $.post("/MotorDeBusqueda/analizarConsulta", { consulta: $(this).val()})
                    .done(function (data) {
                        mostrarDocumentosDevueltos(data);         
            });
        }
    });
}

function mostrarDocumentosDevueltos(data){
    var i = 0,
        length = data.length;
    
    limpiarResultadosAnteriores();
    agregarEncabezados();
    
    for(i = 0; i < length; i++){
        agregarNuevaFilaParaDocumento(data[i]);
    }
}

function agregarEncabezados(){
    var divRow = document.createElement("div"),
        divNombre = document.createElement("div"),
        divURL = document.createElement("div"),
        divPuntaje = document.createElement("div"),
        spanNombre = document.createElement("span"),
        spanURL = document.createElement("span"),
        spanPuntaje = document.createElement("span");
        debugger;
        $(divRow).addClass("row");
        $(divRow).addClass("filaResultados");
        
        $(divNombre).addClass("col-xs-4");
        $(divNombre).addClass("encabezadoResultado");
        $(divNombre).append(spanNombre);
        $(spanNombre).html("DOCUMENTO");
        
        $(divURL).addClass("col-xs-4");
        $(divURL).addClass("encabezadoResultado");
        $(divURL).append(spanURL);
        $(spanURL).html("CLICK PARA VER");
        
        $(divPuntaje).addClass("col-xs-4");
        $(divPuntaje).addClass("encabezadoResultado");
        $(divPuntaje).append(spanPuntaje);
        $(spanPuntaje).html("PUNTAJE");
        
        $(divRow).append(divNombre);
        $(divRow).append(divURL);
        $(divRow).append(divPuntaje);
        
        $("#divResultados").append(divRow);
}

function limpiarResultadosAnteriores(){
    $("#divResultados").html("");
}

function agregarNuevaFilaParaDocumento(JSONData){
    var nombre = JSONData.nombre,
        URL = JSONData.URL,
        puntaje = JSONData.puntaje,
        divRow = document.createElement("div"),
        divNombre = document.createElement("div"),
        divURL = document.createElement("div"),
        divPuntaje = document.createElement("div"),
        spanNombre = document.createElement("span"),
        linkURL = document.createElement("a"),
        spanPuntaje = document.createElement("span");
        debugger;
        $(divRow).addClass("row");
        $(divRow).addClass("filaResultados");
        
        $(divNombre).addClass("col-xs-4");
        $(divNombre).addClass("resultado");
        $(divNombre).append(spanNombre);
        $(spanNombre).html(nombre);
        
        $(divURL).addClass("col-xs-4");
        $(divURL).addClass("resultado");
        $(divURL).append(linkURL);
        $(linkURL).attr("href", URL);
        $(linkURL).append("LINK");
        
        $(divPuntaje).addClass("col-xs-4");
        $(divPuntaje).addClass("resultado");
        $(divPuntaje).append(spanPuntaje);
        $(spanPuntaje).html(puntaje);
        
        $(divRow).append(divNombre);
        $(divRow).append(divURL);
        $(divRow).append(divPuntaje);
        
        $("#divResultados").append(divRow);
}

