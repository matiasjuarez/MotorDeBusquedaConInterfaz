/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.vocabularioGeneral;

import java.util.Comparator;

/**
 *
 * @author Matías
 */
public final class EntradaVocabularioGeneralComparadorCantidadDocumentos implements Comparator<EntradaVocabularioGeneral>{

    @Override
    public int compare(EntradaVocabularioGeneral o1, EntradaVocabularioGeneral o2) {
        return o2.getDocumentosEnQueAparece() - o1.getDocumentosEnQueAparece();
    }
    
}
