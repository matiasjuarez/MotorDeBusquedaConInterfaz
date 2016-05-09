/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import javax.xml.stream.XMLInputFactory;

/**
 *
 * @author Matías
 */
public final class ConfiguracionFabricasStax {
    private ConfiguracionFabricasStax(){};
    
    public static XMLInputFactory getInputFactory(){
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
            xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);
            xmlif.setProperty(XMLInputFactory.IS_COALESCING , Boolean.FALSE);
            
        return xmlif;
    }
}
