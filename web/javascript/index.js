/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var MotorDeBusqueda = {};

$(document).ready(function(){
    MotorDeBusqueda.snowControlObject = new MotorDeBusqueda.snowControl(MotorDeBusqueda.snowStorm);
    MotorDeBusqueda.snowControlObject.inicializarNevada();
    
    agregarEventos();
});

function agregarEventos(){
    
    $("#botonRearmar").click(function(){
        comenzarAnalisisDeDocumentos();
        animarOscuridad(); 
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
            
            $(divOscuridad).removeClass("oscuridadAparece");
            $(divOscuridad).addClass("oscuridadDesaparece");
            clearInterval(interval);
        }
    }, 1000);
}

var analisisCompleto = false,
    intervaloPeticionProgreso;
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
        divBar = document.createElement("div");
        
        $(divProgress).addClass("progress");
        $(divProgress).css({"position": "absolute", "left": "0px"});
        $(divProgress).attr("id", "divContenedorBarraProgreso");
        
        $(divBar).addClass("progress-bar progress-bar-striped progress-bar-success active");
        $(divBar).attr("role", "progressbar");
        $(divBar).attr("id", "barraProgreso");
        $(divBar).css("width", 0 + "%");
        $(divBar).html(0);
        
        
        $(divProgress).append(divBar);
        
        $("#divContainer").append(divProgress);
        
        centrarBarraDeProgreso();
}

function centrarBarraDeProgreso(){
    var barra = $("#divContenedorBarraProgreso"),
        height = $(window).height();

        $(barra).css("top", (height / 2) + px);
}

function mostrarProgreso(data){
    debugger;
    var dataProgreso = data.progreso,
            sinComas,
            progreso,
            barraProgreso = $("#barraProgreso");
    
    sinComas = dataProgreso.replace(",", ".");
    
    progreso = parseFloat(sinComas);
    
    if(progreso >= 1){
        if(analisisCompleto = true){
            clearInterval(intervaloPeticionProgreso);
        }
        progreso = 100;
        $(barraProgreso).removeClass("progress-bar-success");
        $(barraProgreso).addClass("progress-bar-danger");
        analisisCompleto = true;
        
    }
    else{
        progreso = progreso * 100;
    }
    
    $(barraProgreso).css("width", (progreso + "%"));
    $(barraProgreso).html(progreso.toFixed(2));
        
    console.log(progreso);
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
        
        $("body").append(divVideo);
}

MotorDeBusqueda.snowControl = function(snowFunction){
        this.snowFunction = snowFunction;
        this.snowObject = null;
        
        this.maximosCopos = 208;
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
                this.snowObject.snowColor = "#aaa";
            }
        }
    };