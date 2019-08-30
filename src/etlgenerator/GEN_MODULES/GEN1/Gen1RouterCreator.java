/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN1;

import helper.file.FileManager;
import helper.XMLManager;
import java.io.File;
import java.io.IOException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Gen1RouterCreator implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();

        String izlaz = "";
        try {
            Document doc = XMLManager.crateDocumentForXML(in.getBody(String.class));
            String sources = XMLManager.getXpathElementValue(doc, "/message/sources");
            System.out.println("Found new sources: "+sources);
            if(sources.length()>0){
                int count = (sources.length() - sources.replaceAll("source", "").length())/6;
                System.out.println("'"+count+"'");
                boolean into_gen3 = false;
                String old_source = XMLManager.getXpathElementValue(doc, "/message/sources/source1/user_source");
                for(int i=2;i<=count;i++)
                {
                    String source = XMLManager.getXpathElementValue(doc, "/message/sources/source"+i+"/user_source");
                    if(!source.equalsIgnoreCase(old_source))
                    {
                        into_gen3=true;
                        break;
                    }
                }
                Element el = doc.createElement("command");
                if(into_gen3) el.setTextContent("yes3");
                else el.setTextContent("yes2");
                doc = XMLManager.addNodeToElementNodeInXML(doc, "message", el);
            }
            else{
                String source = XMLManager.getXpathElementValue(doc, "/message/source/user_source");
                System.out.println("Found new source: "+source);
                addRule(source);
            }
   
            izlaz = XMLManager.crateStringFromXMLDocument(doc);
            //System.out.println(izlaz);
        } catch (Exception ex) {
            System.err.println(ex);
        }

        in.setBody(izlaz);
    }

    private void addRule(String source_type) {
        String to = System.getProperty("user.dir") + File.separator + "target" + File.separator + "gen1";
        File to_dir = new File(to + File.separator + source_type);
        System.out.println("If exist: "+to_dir);
        if (!to_dir.exists()) {
            to_dir.mkdir();
            
            String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "etlgenerator";
            System.out.println("Add rule to: " + path);
            String file_content = "";
            try {
                file_content = FileManager.readFileContentToString(path, "ETLSourceRouter.java");
            } catch (IOException ex) {
                System.err.println(ex);
            }

            if (file_content.indexOf(source_type) == -1) {
                file_content = file_content.replace(";//ADD RULE",
                        "\n.otherwise().when(xpath(\"/message/source/user_source = '" + source_type + "'\"))\n"
                        + ".to(\"file:target/gen1/" + source_type + "\");//ADD RULE");
                FileManager.createFileFromString(file_content, path, "ETLSourceRouter.java");
            }
        }
    }
}
