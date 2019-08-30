/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.templates;

import helper.XMLManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class TPL_SourceVersion_DB {

    protected Document addInfo(Document doc) {
        try {
            String source_name = XMLManager.getXpathElementValue(doc, "/message/source/user_source");

            System.out.println(this.getClass().getName());
            Properties prop = new Properties();

            InputStream inputStream
                    = new FileInputStream(
                            new File(System.getProperty("user.dir") + File.separator + "target"
                                    + File.separator + "input_source_desc"
                                    + File.separator + source_name + ".properties"));

            prop.load(inputStream);
            String type = prop.getProperty("type").trim().replace("\"", "");
            String ip = prop.getProperty("ip").trim().replace("\"", "");
            String username = prop.getProperty("username").trim().replace("\"", "");
            String password = prop.getProperty("password").trim().replace("\"", "");
            
            Element el_type = doc.createElement("type");
            el_type.setTextContent(type);
            Element el_ip = doc.createElement("ip");
            el_ip.setTextContent(ip);
            Element el_username = doc.createElement("username");
            el_username.setTextContent(username);
            Element el_password = doc.createElement("password");
            el_password.setTextContent(password);
            
            doc = XMLManager.addNodeToElementNodeInXML(doc, "source", el_type);
            doc = XMLManager.addNodeToElementNodeInXML(doc, "source", el_ip);
            doc = XMLManager.addNodeToElementNodeInXML(doc, "source", el_username);
            doc = XMLManager.addNodeToElementNodeInXML(doc, "source", el_password);
        } catch (Exception ex) {
            System.err.println("ERROR:"+ex);
        }
        return doc;
    }
}
