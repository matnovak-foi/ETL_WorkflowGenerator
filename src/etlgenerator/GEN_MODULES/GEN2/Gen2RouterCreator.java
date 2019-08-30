/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN2;

import helper.file.FileManager;
import helper.XMLManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Gen2RouterCreator extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        System.out.println("Configure Gen2RouterCreator");

        from("file:target/cmd_router_gen2?noop=false")
                .process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message in = exchange.getIn();

                        String izlaz = "";
                        try {
                            Document doc = XMLManager.crateDocumentForXML(in.getBody(String.class));
                            /*
                            for(String key : in.getHeaders().keySet())
                            {
                                System.out.println("Ključevi: "+key);
                            }
                            */
                            String xml_msg_name = (String) in.getHeaders().get("camelfilenameonly");
                            String sources = XMLManager.getXpathElementValue(doc, "/message/sources");
                            System.out.println("GEN2 Found new sources: " + sources);
                            if (sources.length() > 0) {
                                int count = (sources.length() - sources.replaceAll("source", "").length()) / 6;
                                System.out.println("GEN2 '" + count + "'");
                                String source = XMLManager.getXpathElementValue(doc, "/message/sources/source1/user_source");
                                String dest_name = XMLManager.getXpathElementValue(doc, "/message/destination/name");
                                String dest_att = XMLManager.getXpathElementValue(doc, "/message/destination/attribute");
                                String trans_name = XMLManager.getXpathElementValue(doc, "/message/transformation/type");
                                List<String> attributes = new ArrayList<String>();
                                for (int i = 1; i <= count; i++) {
                                    String attribute = XMLManager.getXpathElementValue(doc, "/message/sources/source" + i + "/attribute");
                                    attributes.add(attribute);
                                }
                                addRule(source, dest_att, attributes);
                                addAgregatorAndCE(dest_name, source, dest_att, trans_name, attributes, xml_msg_name);
                            }

                            izlaz = XMLManager.crateStringFromXMLDocument(doc);
                            //System.out.println(izlaz);
                        } catch (Exception ex) {
                            System.err.println(ex);
                        }

                        in.setBody(izlaz);
                    }

                    private void addAgregatorAndCE(String dest_name, String source, String dest_att, String trans_name, List<String> attributes, String xml_msg_name) {
                        
                        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2";
                        String name = "ETLGen2AgregatorAndCE" + source + "_" + dest_att.replace(" ", "_") + ".java";
                        
                        String in_path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "templates";
                        String in_name = "ETLGen2AgregatorAndCE.java";
                        //FileManager.copyFile(, , path, name);

                        System.out.println("Add rule to: " + path);
                        String file_content = "";
                        try {
                            file_content = FileManager.readFileContentToString(in_path, in_name);
                        } catch (IOException ex) {
                            System.err.println(ex);
                        }

                        file_content = file_content.replace("2)//ADD QUEUE SIZE",attributes.size()+")");
                        file_content = file_content.replace("ADD TRANS NAME",trans_name);
                        file_content = file_content.replace("ADD ATT NAME",dest_att);
                        file_content = file_content.replace("ADD DEST NAME",dest_name);
                        file_content = file_content.replace("ADD FROM",source+dest_att);
                        file_content = file_content.replace("ADD XML FILE NAME",xml_msg_name.replace(".xml", "_"+source+"_gen2.xml"));
                        file_content = file_content.replace("package templates;", "package etlgenerator.GEN_MODULES.GEN2;");
                        file_content = file_content.replace("class ETLGen2AgregatorAndCE", "class ETLGen2AgregatorAndCE" + source + "_" + dest_att.replace(" ", "_") );
        
                        FileManager.createFileFromString(file_content, path, name);
                    }

                    private void addRule(String source, String dest, List<String> attributes) {
                        String to = System.getProperty("user.dir") + File.separator + "target" + File.separator + "gen2";
                        File to_dir = new File(to + File.separator + source+dest);
                        System.out.println("GEN2 If exist: " + to_dir);
                        if (!to_dir.exists()) {
                            to_dir.mkdir();

                            String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2";
                            System.out.println("Add rule to: " + path);
                            String file_content = "";
                            try {
                                file_content = FileManager.readFileContentToString(path, "ETLGen2Router" + source + ".java");
                            } catch (IOException ex) {
                                System.err.println(ex);
                            }

                            for (Iterator<String> iterator = attributes.iterator(); iterator.hasNext();) {
                                String next = iterator.next();
                                System.out.println("GEN2 next: " + next);

                                //if (file_content.indexOf(next) == -1) {
                                //   System.out.println("GEN2 next: "+next);
                                file_content = file_content.replace("//ADD RULE",
                                        "\n.when(xpath(\"/message/source/attribute = '" + next + "'\"))\n"
                                        + ".to(\"file:target/gen2/" + source+dest + "\")\n.otherwise()\n//ADD RULE");
                                FileManager.createFileFromString(file_content, path, "ETLGen2Router" + source + ".java");
                                //}
                            }
                        }
                    }
                });

    }

}
