/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function PauseButton(){
    
    this.muted = false;
    
    this.button;
    
    this.originalVolumes = [];
    
    this.pause = function(){
        var audios = document.getElementsByTagName("audio"),
                i = 0,
                length = audios.length;
        
        if(this.muted){
            
            for(i = 0; i < length; i++){
                audios[i].volume = this.originalVolumes[i];
            }
            
            $(this.button).html("MUTE");
        }
        else{
            guardarVolumenesOriginales.call(this, audios);
            for(i = 0; i < length; i++){
                
                audios[i].volume = 0;
            }
           $(this.button).html("UNMUTE");
        }
        
        this.muted = !this.muted;
        
        function guardarVolumenesOriginales(audios){
            
            this.originalVolumes = [];
            
            var i = 0,
                length = audios.length;
        
                for(i = 0; i < length; i++){
                    this.originalVolumes.push(audios[i].volume);
                }
        }
    };
    
    this.getDomElement = function(){
      
        var divContainer = document.createElement("div"),
            button = document.createElement("button"),
            self = this;
    
        $(divContainer).addClass("audioControl");
        
        $(button).html("MUTE");
        $(button).addClass("btn btn-warning");
        
        $(divContainer).append(button);
        
        $(button).on("click", function(){
            self.pause();
        });
        
        this.button = button;
        
        return divContainer;
    };
}

