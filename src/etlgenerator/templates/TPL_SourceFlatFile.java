/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.templates;

import org.w3c.dom.Document;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class TPL_SourceFlatFile extends TPL_SourceVersion_File implements I_TPL_Content_Enricher{
    public Document addInfo(Document doc){
        System.out.println(this.getClass().getName());
        doc = super.addInfo(doc);
        return doc;
    }
}
