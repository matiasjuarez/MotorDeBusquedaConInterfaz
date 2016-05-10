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
        animarOscuridad(); 
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
            
            $(divOscuridad).removeClass("oscuridadAparece");
            $(divOscuridad).addClass("oscuridadDesaparece");
            clearInterval(interval);
        }
    }, 1000);
}

function limpiarElementosPaginaPrincipal(){
    $("body").css("background-image", "none");
    $("#divBotones").remove();
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