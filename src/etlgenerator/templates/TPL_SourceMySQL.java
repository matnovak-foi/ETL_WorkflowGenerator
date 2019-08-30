/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.w3c.dom.Document;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class TPL_SourceMySQL extends TPL_SourceVersion_DB implements I_TPL_Content_Enricher{
    @Override
    public Document addInfo(Document doc){
        System.out.println(this.getClass().getName());
        
        doc = super.addInfo(doc);
        return doc;
    }
}
