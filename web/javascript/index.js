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
    
    setWindVolume(0.3);
    
    agregarEventos();
    
    agregarBotonPausarAudio();
});

function agregarBotonPausarAudio(){
    var pauseButton = new PauseButton(),
        domElement = pauseButton.getDomElement();
    
    $("body").append(domElement);
}

function setWindVolume(volume){
    var wind = $("#viento"),
        audio = wind[0];
    audio.volume = volume;
}


function agregarEventos(){
    $(window).on("resize", function(){
        centrarBarraDeProgreso();
    });
    
    $("#botonRearmar").click(function(){
        animarOscuridad(); 
        comenzarAnalisisDeDocumentos();
        console.log("botonRearmar");
    });
    
    $("#botonConsultaNotReady").click(function(){
        $("#botonRearmar").addClass("blinkButton");
    });
    
    $("#botonConsulta").click(function(){
        debugger;
        animarFinConstruccionMotor();
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
    $("#divOscuridad").addClass("oscuridadAparece");
    $("#divOscuridad").removeClass("oculto");
    
    var musica = document.getElementById("musicaOscuridad");
    musica.play();
    
    var interval = setInterval(function(){
        var opacidad = $(divOscuridad).css("opacity");
        console.log(opacidad);
        if(opacidad == 1){
            animarFinOscuridad();
            clearInterval(interval);
        }
    }, 1000);
    
}

function animarFinOscuridad(){
    limpiarElementosPaginaPrincipal();
    ponerVideoNevadaDeFondo();
    crearBarrarProgreso();
    obtenerProgreso();

    $("#videoNieve").removeClass("oculto");
    $("#divOscuridad").removeClass("oscuridadAparece");
    $("#divOscuridad").addClass("oscuridadDesaparece");
    
    var interval = setInterval(function(){
        var opacidad = $("#divOscuridad").css("opacity");
        if(opacidad == 0){
            clearInterval(interval);
            $("#divOscuridad").addClass("oculto");
        }
    }, 1000);
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
        divRow = document.createElement("div"),
        spanTexto = document.createElement("span");

        $(divRow).addClass("row");
        $(divRow).attr("id", "divContenedorBarraProgreso");
        $(divRow).css({"position":"relative", "left": "0px"});
        
        $(divProgress).addClass("progress col-xs-12");
        $(divProgress).css({"width": (100 + "%"), "padding-right": 0 + "px", "padding-left": 0 + "px"});
        
        
        $(divBar).addClass("progress-bar progress-bar-striped progress-bar-success active");
        $(divBar).css({"font-size": "0px"});
        $(divBar).attr("aria-valuemax", 100);
        $(divBar).attr("role", "progressbar");
        $(divBar).attr("id", "barraProgreso");
        $(divBar).css("width", 0 + "%");
        $(divBar).html(0);
        
        $(spanTexto).attr("id", "textoBarraProgreso");
        $(spanTexto).addClass("text-size20 text-creepy");
        $(spanTexto).css({color: "black"});
        
        $(divBar).append(spanTexto);
        
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
            barraProgreso = $("#barraProgreso"),
            spanTextoBarraProgreso = $("#textoBarraProgreso");
    
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
        animarFinConstruccionMotor();
    }
    
    $(barraProgreso).css("width", (progreso + "%"));
    
    if(etapa == 0){
        mensaje = "ANALIZADO: " + progreso.toFixed(2) + "%";
    }
    else if(etapa == 1 || etapa == -1){
        mensaje = "CONSTRUIDO: " + progreso.toFixed(2) + "%";
    }
    
    $(spanTextoBarraProgreso).html(mensaje);
        
    console.log(mensaje);
}

function animarFinConstruccionMotor(){
    $("#divOscuridad").removeClass("oscuridadDesaparece");
    $("#divOscuridad").addClass("oscuridadApareceRapido");
    $("#divOscuridad").removeClass("oculto");
    
    disminuirVolumenGradualmente(3);
    var interval = setInterval(function(){
        var opacidad = $("#divOscuridad").css("opacity");
        console.log(opacidad);
        if(opacidad == 1){
            clearInterval(interval);
            window.location = "/MotorDeBusqueda/PaginaConsulta.jsp";
        }
    }, 1000);
}

function disminuirVolumenGradualmente(tiempo){
    debugger;
    var disminucionesPorSegundo = 10,
        audioViento = $("#viento")[0],
        musicaOscuridad = $("#musicaOscuridad")[0],
        disminucionViento = (audioViento.volume/tiempo)/disminucionesPorSegundo,
        disminucionMusicaOscuridad = (musicaOscuridad.volume/tiempo)/disminucionesPorSegundo,
        vientoVolume,
        musicaOscuridadVolume;

    setInterval(function(){
        debugger;
        vientoVolume = audioViento.volume;
        vientoVolume -= disminucionViento;
        if(vientoVolume < 0){
            vientoVolume = 0;
        }
        audioViento.volume = vientoVolume;
        
        
        musicaOscuridadVolume = musicaOscuridad.volume;
        musicaOscuridadVolume -= disminucionMusicaOscuridad;
        if(musicaOscuridadVolume < 0){
            musicaOscuridadVolume = 0;
        }
        musicaOscuridad.volume = musicaOscuridadVolume;
        console.log(musicaOscuridadVolume);
    }, 1000/disminucionesPorSegundo);
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