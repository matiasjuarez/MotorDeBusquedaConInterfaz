/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    
    redimensionarContenedorResultados();
    agregarEventos();
    
    prepararAnimacionDeFondo();
    
    quitarDivOscuridad();
    
    modificarVolumenFuego();
    
    agregarBotonPausarAudio();
});

function agregarBotonPausarAudio(){
    
    var pauseButton = new PauseButton(),
        domElement = pauseButton.getDomElement();
    
    $("body").append(domElement);
}

function modificarVolumenFuego(){
    var sonidoFuego = $("#soundFire")[0];
        sonidoFuego.volume = 0.8;
}

function quitarDivOscuridad(){
    var div = $("#divOscuridad"),
    opacity;
    
    var intervalo = setInterval(function(){
        opacity = $(div).css("opacity");
        if(opacity == 0){
            $(div).addClass("oculto");
            clearInterval(intervalo);
        }
    }, 100);
}

function redimensionarContenedorResultados(){
    var divResultados = $("#divResultados"),
        divTop = $(divResultados).offset().top,
        altoPagina = $(window).height();

    $(divResultados).height((altoPagina - divTop - 10) + "px");
}

function redimensionarContenedorTextoDocumento(){
    var divTexto = $(".divContenedorTextoDocumento"),
        altoPagina = $(window).height(),
        anchoPagina = $(window).width();
        
        $(divTexto).width((anchoPagina/3) + "px");
        var left = (anchoPagina) / 2 - $(divTexto).width() / 2;
        
    $(divTexto).height((altoPagina - 10) + "px");
    $(divTexto).css({"left": (left + "px")});
}

function prepararAnimacionDeFondo(){
    $("#divVideoBackground2").css({"opacity": 0});
    
    comenzarLoopVideo();
    
    sincronizarComienzosParadasDeVideos();
   /* 
    addEndEventToVideos("backgroundVideo1", "backgroundVideo2", "divVideo1", "divVideo2");
    addEndEventToVideos("backgroundVideo2", "backgroundVideo1", "divVideo2", "divVideo1");*/
}

function comenzarLoopVideo(){
    var video = document.getElementById("backgroundVideo1");
    video.currentTime = 1;
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
        redimensionarContenedorTextoDocumento();
    });
    
    $("#inputConsulta").on("keydown", function(event){
        if(event.which === 13){
            event.preventDefault();
            
            $.ajax({
                type: "POST",
                url: "/MotorDeBusqueda/analizarConsulta",
                data: { consulta: $(this).val()},
                context: this,
                success: mostrarDocumentosDevueltos
              });
            
            /*$.post("/MotorDeBusqueda/analizarConsulta", { consulta: $(this).val()})
                    .done(function (data) {
                        mostrarDocumentosDevueltos(data);         
            });*/
        };
            
        
    });
    
    $("body").on("click", ".linkCierre", function(){
        $(this).parent().parent().remove();
    });
    
    agregarEventoSobreLinksDocumentos();
}

function descargarDocumento(data){
    
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
        
        $(divRow).addClass("row");
        $(divRow).addClass("filaResultados");
        
        $(divNombre).addClass("col-xs-4 encabezadoResultado text-creepy");
        $(divNombre).append(spanNombre);
        $(spanNombre).html("DOCUMENTO");

        $(divURL).addClass("col-xs-4 encabezadoResultado text-creepy");
        $(divURL).append(spanURL);
        $(spanURL).html("CLICK PARA VER");

        $(divPuntaje).addClass("col-xs-4 encabezadoResultado text-creepy");
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
        
        $(divRow).addClass("row");
        $(divRow).addClass("filaResultados");
        
        $(divNombre).addClass("col-xs-4");
        $(divNombre).addClass("resultado");
        $(divNombre).append(spanNombre);
        $(spanNombre).html(nombre);
        
        $(divURL).addClass("col-xs-4");
        $(divURL).addClass("resultado");
        $(divURL).addClass("divLinkDocumento");
        $(divURL).append(linkURL);
        $(linkURL).attr("href", URL);
        $(linkURL).addClass("linkDocumento");
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

function agregarEventoSobreLinksDocumentos(){
    
    $("body").on("click", ".linkDocumento", function(event){
       event.preventDefault();
       var link = $(this).attr('href');
       
       
       $.post("/MotorDeBusqueda/lectorDocumento", { "URL": link})
                    .done(function (data) {
                        crearDivParaMostrarTextoDocumento(data);         
            });
            /*
        $.ajax({
                type: "POST",
                url: "/MotorDeBusqueda/descargarArchivo",
                data: { url: link},
                context: this,
                success: descargarDocumento
              });*/
    });
}

function crearDivParaMostrarTextoDocumento(texto){
    var divTexto = document.createElement("div"),
        parrafo = document.createElement("p"),
        parrafoCierre = document.createElement("p"),
        linkCierre = document.createElement("a"),
        width = $(window).width(),
        height = $(window).height(),
        widthElement = width / 3,
        left = widthElement,
        posicionActualTexto = 0,
        caracteresPorCiclo = 20,
        lengthTexto = texto.length,
        interval;
    
    $(divTexto).addClass("divContenedorTextoDocumento");
    $(divTexto).css({"left": left});
    $(divTexto).css({"height": height * 0.9 + "px"});
    $(divTexto).css({"width": widthElement + "px"});
    
    $(parrafoCierre).css({"text-align": "right"});
    $(linkCierre).append("X");
    $(linkCierre).attr("href", "#");
    $(linkCierre).addClass("linkCierre");
    $(parrafoCierre).append(linkCierre);
    $(divTexto).append(parrafoCierre);
    
    $(divTexto).append(parrafo);
    $(parrafo).text(texto);
    
    $("body").append(divTexto);
    
    /*interval = setInterval(function(){
        
        if(posicionActualTexto >= lengthTexto){
            posicionActualTexto = lengthTexto - 1;
            clearInterval(interval);
        }
        
        $(parrafo).text(texto.substring(posicionActualTexto, caracteresPorCiclo));
       
        posicionActualTexto += caracteresPorCiclo;
    }, 1000/60);*/
}
