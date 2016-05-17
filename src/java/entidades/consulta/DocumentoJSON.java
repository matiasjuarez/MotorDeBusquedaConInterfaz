/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.consulta;

import entidades.documentos.Documento;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Esta clase transforma los datos de un documento en formato json de modo
 * que pueda ser tratado con facilidad por javascript
 * @author Matías
 */
public class DocumentoJSON {
    private ArrayList<Documento> documentos = new ArrayList<>();
    
    public void addDocumento(Documento documento){
        documentos.add(documento);
    }
    
    public void setDocumentos(ArrayList<Documento> documentos){
        this.documentos = documentos;
    }
    
    private JSONObject crearDocumentosJSON(Documento documento){
        try {
            JSONObject object = new JSONObject();
            object.put("nombre", documento.getNombre());
            try {
                object.put("URL", documento.getAbsoluteURL());
            } catch (IOException ex) {
                object.put("URL", "No tiene URL valida");
                Logger.getLogger(DocumentoJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
            object.put("puntaje", Math.round(documento.getPuntajeFrenteAConsulta()));
            
            return object;
        } catch (JSONException ex) {
            
            Logger.getLogger(DocumentoJSON.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public JSONArray obtenerDocumentosJSON(){
        JSONArray array = new JSONArray();
        
        if(documentos != null){
            for(Documento documento: documentos){
                array.put(crearDocumentosJSON(documento));
            }
        }
        else{
            try {
                JSONObject json = new JSONObject();
                json.put("nombre", "ninguno");
                json.put("URL", "");
                json.put("puntaje", Integer.toString(-1));
                array.put(json);
            } catch (JSONException ex) {
                Logger.getLogger(DocumentoJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return array;
        /*ArrayList<JSONObject> listaJSON = new ArrayList<>();
        
        for(Documento documento: documentos){
            listaJSON.add(crearDocumentosJSON(documento));
        }
        
        return listaJSON;*/
    }
}
