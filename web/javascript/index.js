/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var MotorDeBusqueda = {},
    etapaAnterior = -1,
    intervaloPeticionProgreso;
    

$(document).ready(function(){
    MotorDeBusqueda.snowControlObject = new MotorDeBusqueda.snowControl(MotorDeBusqueda.snowStorm);
    MotorDeBusqueda.snowControlObject.inicializarNevada();
    
    agregarEventos();
});

function agregarEventos(){
    
    $("#botonRearmar").click(function(){
        animarOscuridad(); 
        comenzarAnalisisDeDocumentos();
        console.log("botonRearmar");
    });
}

function comenzarAnalisisDeDocumentos(){
    $.ajax({
        type: "POST",
        url: "/MotorDeBusqueda/armarEstructurasParaElMotor",
        data: {},
        context: this
      });
}

function animarOscuridad(){
    var divOscuridad = document.createElement("div");
    $("body").append(divOscuridad);
    $(divOscuridad).addClass("oscuridad");
    $(divOscuridad).addClass("oscuridadAparece");
    
    var musica = document.getElementById("musicaOscuridad");
    musica.play();
    
    var interval = setInterval(function(){
        var opacidad = $(divOscuridad).css("opacity");
        console.log(opacidad);
        if(opacidad == 1){
            limpiarElementosPaginaPrincipal();
            ponerVideoNevadaDeFondo();
            crearBarrarProgreso();
            obtenerProgreso();
            
            $("#videoNieve").removeClass("oculto");
            $(divOscuridad).removeClass("oscuridadAparece");
            $(divOscuridad).addClass("oscuridadDesaparece");
            clearInterval(interval);
        }
    }, 2000);
    
}


function obtenerProgreso(){
    analisisCompleto = false;
    
    intervaloPeticionProgreso = setInterval(function(){
        $.ajax({
            type: "POST",
            url: "/MotorDeBusqueda/analizadorProgreso",
            data: { analisisCompleto: analisisCompleto},
            context: this,
            success: mostrarProgreso
          });
    }, 1000);
    
}

function crearBarrarProgreso(){
     var divProgress = document.createElement("div"),
        divBar = document.createElement("div"),
        divRow = document.createElement("div");

        $(divRow).addClass("row");
        $(divRow).attr("id", "divContenedorBarraProgreso");
        $(divRow).css({"position":"relative", "left": "0px"});
        
        $(divProgress).addClass("progress col-xs-12");
        $(divProgress).css({"width": (100 + "%"), "padding-right": 0 + "px", "padding-left": 0 + "px"});
        
        
        $(divBar).addClass("progress-bar progress-bar-striped progress-bar-success active");
        $(divBar).attr("role", "progressbar");
        $(divBar).attr("id", "barraProgreso");
        $(divBar).css("width", 0 + "%");
        $(divBar).html(0);
        
        
        $(divProgress).append(divBar);
        $(divRow).append(divProgress);
        
        $("#divContainer").append(divRow);
        
        centrarBarraDeProgreso();
}

function centrarBarraDeProgreso(){
    var barra = $("#divContenedorBarraProgreso"),
        height = $(window).height();

        $(barra).css("top", (height / 2) + "px");
}

function mostrarProgreso(data){
    
    var dataProgreso = data.progreso,
        etapa = parseInt(data.etapa),
            progreso,
            mensaje,
            barraProgreso = $("#barraProgreso");
    
    if(data.etapa == -1){
        debugger;
    }
        //sinComas = dataProgreso.replace(",", ".");

        //progreso = parseFloat(sinComas);
        progreso = dataProgreso * 100;
    
        if(etapa == 0){
            $(barraProgreso).addClass("progress-bar-success");
            $(barraProgreso).removeClass("progress-bar-danger");
        }
        else if (etapa == 1){
            $(barraProgreso).addClass("progress-bar-danger");
            $(barraProgreso).removeClass("progress-bar-success");
        }        
    
    
    if(etapa == -1){
        progreso = 100;
        clearInterval(intervaloPeticionProgreso);
    }
    
    $(barraProgreso).css("width", (progreso + "%"));
    
    if(etapa == 0){
        mensaje = "Analizado: " + progreso.toFixed(2) + "%";
    }
    else if(etapa == 1 || etapa == -1){
        mensaje = "Construido: " + progreso.toFixed(2) + "%";
    }
    
    $(barraProgreso).html(mensaje);
        
    console.log(mensaje);
}

function limpiarElementosPaginaPrincipal(){
    $("body").css("background-image", "none");
    $("#divContainer").html("");
    MotorDeBusqueda.snowControlObject.pararNieve();
    MotorDeBusqueda.snowControlObject.limpiarNieve();
}

function ponerVideoNevadaDeFondo(){
   var divVideo = document.createElement("div"),
        video = document.createElement("video"),
        source = document.createElement("source");

        $(source).attr("src", "/MotorDeBusqueda/resources/video/fallingSnow.mp4");
        $(source).attr("type", "video/mp4");
        
        $(video).attr("autoplay", "autoplay");
        $(video).attr("loop", "loop");
        $(video).append(source);
        
           
        $(divVideo).addClass("divVideoContainer");
        $(divVideo).css("z-index", -1);
        $(divVideo).append(video);
        
        $("#divContainer").append(divVideo);
}

MotorDeBusqueda.snowControl = function(snowFunction){
        this.snowFunction = snowFunction;
        this.snowObject = null;
        
        this.maximosCopos = 300;
        this.coposActivos = this.maximosCopos/2;
        this.vMaxX = 3;
        this.vMaxY = 2; 
    };

    MotorDeBusqueda.snowControl.prototype = {
        limpiarNieve: function(){
            this.snowObject.stop();
        },
        
        recolocarNieve: function(){
            this.snowObject.show();
        },
        
        pararNieve: function(){
            this.snowObject.freeze();
        },
        
        reanudarNieve: function(){
            this.snowObject.resume();
            
        },
        
        inicializarNevada: function(){
            if(this.snowObject == null){
                this.snowObject = new this.snowFunction(window, document);
                this.snowObject.flakesMaxActive = this.maximosCopos;
                this.snowObject.flakesMax = this.coposActivos;
                this.snowObject.snowColor = "#888";
            }
        }
    };