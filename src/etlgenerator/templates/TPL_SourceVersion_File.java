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
public class TPL_SourceVersion_File {
    protected Document addInfo(Document doc){
        System.out.println(this.getClass().getName());
        return doc;
    }
}
