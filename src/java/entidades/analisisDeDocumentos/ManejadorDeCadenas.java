/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.analisisDeDocumentos;

import java.util.ArrayList;

/**
 *
 * @author Matías
 */
public abstract class ManejadorDeCadenas {
   
    public static final String separadores = " ,.:;_-*=~#&$+%?!()[]{}'/\\\"\n";
    public static final String caracteresValidos = "abcdefghijklmnñopqrstuvwxyz";
    public enum MetodoDeSeparacionDeCadenas{
        CARACTERES_VALIDOS, SEPARADORES;
    }
    
    public static ArrayList<String> recuperarPalabras(String texto, MetodoDeSeparacionDeCadenas metodo){
        
        if(metodo == MetodoDeSeparacionDeCadenas.CARACTERES_VALIDOS){
            return metodoDeCaracteresValidos(texto);
        }
        else{
            return metodoDeSeparadores(texto);
        }
        
    }
    
    private static ArrayList<String> metodoDeCaracteresValidos(String texto){
        int indices[];
        
        int inicio = -1;
        
        ArrayList<String> palabras = new ArrayList<>();

        while(true){
            indices = encontrarInicioFinProximaPalabra(texto, inicio + 1);
            
            if(indices[0] == -1){
                break;
            }
            
            inicio = indices[1];

            palabras.add(texto.substring(indices[0], indices[1]).toLowerCase());
        }
        
        return palabras;
    }
    
    private static ArrayList<String> metodoDeSeparadores(String texto){
        int inicio = -1;
        int fin;
        int length = texto.length();
        String cadenaSeleccionada;
        
        ArrayList<String> palabras = new ArrayList<>();

        while(inicio < length){
            fin = encontrarPosicionProximoSeparador(texto, inicio + 1, separadores);

            if(fin == -1){
                fin = texto.length();
            }

            cadenaSeleccionada = texto.substring(inicio + 1, fin);
            cadenaSeleccionada = cadenaSeleccionada.trim();

            if(cadenaSeleccionada.length() != 0){
                palabras.add(cadenaSeleccionada.toLowerCase());
            }

            inicio = fin;
        }
        
        return palabras;
    }
    
    // Devuelve los indices inicio y fin para recuperar la palabra siguiente con el metodo substring
    private static int[] encontrarInicioFinProximaPalabra(String cadena, int indiceInicial){
        int[] indices = new int[2];
        indices[0] = -1;
        indices[1] = -1;
        
        int length = cadena.length();
        boolean inicioDePalabraSeteado = false;
        
        char caracterAnalizado;
        for(int i = indiceInicial; i < length; i++){
            caracterAnalizado = cadena.charAt(i);
            
            if(esCaracterValido(caracterAnalizado)){
                if(!inicioDePalabraSeteado){
                    inicioDePalabraSeteado = true;
                    indices[0] = i;
                }
            }
            else{
                if(inicioDePalabraSeteado){
                    indices[1] = i;
                    return indices;
                }
            }
        }
        
        
        if(indices[1] == -1){
            if(inicioDePalabraSeteado){
                indices[1] = length;
            }
        }
        
        return indices;
    }
    
    private static boolean esCaracterValido(char caracter){
        int caracteresValidosLength = caracteresValidos.length();
        
        caracter = Character.toLowerCase(caracter);
        
        for(int j = 0; j < caracteresValidosLength; j++){
            if(caracter == caracteresValidos.charAt(j)){
                return true;
            }
        }
        return false;
    }
    
    private static int encontrarPosicionProximoSeparador(String cadena, int indiceInicial, String separadores){
        
        int length = cadena.length();
        int lengthSeparadores = separadores.length();
        
        for(int i = indiceInicial; i < length; i++){
            for(int j = 0; j < lengthSeparadores; j++){
                if(cadena.charAt(i) == separadores.charAt(j)){
                    return i;
                }
            }
        }
        
        return -1;
    }
    
}
