/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var MotorDeBusqueda = {};

$(document).ready(function(){
    debugger;
    var fondoAnimado = new MotorDeBusqueda.FondoAnimado(MotorDeBusqueda.snowStorm);
    
        fondoAnimado.inicializarNevada();

        
    $("#morir").click(function(){
        animarOscuridad();
    });
});

function animarOscuridad(){
    var div = $("#sobreTodo");
    $(div).addClass("oscuridad");
    debugger;
    var musica = document.getElementById("musica");
    musica.play();
}