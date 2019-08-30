/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.templates;

import helper.XMLManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class TPL_T_DateFormat implements I_TPL_Content_Enricher {

    public Document addInfo(Document doc) {
        System.out.println(this.getClass().getName());
        try {
            String format = XMLManager.getXpathElementValue(doc, "/message/transformation/format");
            if (format.equals("European date")) {
                Element el_type = doc.createElement("format2");
                el_type.setTextContent("dd.mm.YYYY");

                doc = XMLManager.addNodeToElementNodeInXML(doc, "transformation", el_type);
            } else {

            }
        } catch (Exception ex) {
            System.err.println("ERROR:" + ex);
        }

        return doc;
    }
}
