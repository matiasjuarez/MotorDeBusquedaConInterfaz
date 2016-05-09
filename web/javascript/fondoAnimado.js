/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    MotorDeBusqueda.FondoAnimado = function(snowFunction){
        this.snowFunction = snowFunction;
        this.snowObject = null;
        
        this.maximosCopos = 208;
        this.coposActivos = this.maximosCopos/2;
        this.vMaxX = 3;
        this.vMaxY = 2; 
    };

    MotorDeBusqueda.FondoAnimado.prototype = {
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
});


