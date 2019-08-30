/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN1;

import etlgenerator.templates.CE_TPL_Manager;
import etlgenerator.templates.I_TPL_Content_Enricher;
import etlgenerator.templates.TPL_Destination;
import helper.XMLManager;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.w3c.dom.Document;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ETLgen1_content_entricher extends RouteBuilder {

    private String from;
    private String to;
    
    public ETLgen1_content_entricher(String from, String to) {
        super();
        this.from = from;
        System.out.println("CE from: "+from);
        this.to = to;
        System.out.println("CE to: "+to);
    }

    @Override
    public void configure() throws Exception {
        from("file:target/gen1/"+this.from+"?noop=false")
                .process(new Processor() {
                    
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message in = exchange.getIn();
                        String izlaz = "";
                        try {
                            Document doc = XMLManager.crateDocumentForXML(in.getBody(String.class));
                            doc = (new TPL_Destination()).addInfo(doc);
                            I_TPL_Content_Enricher source_class;
                            String source_name = XMLManager.getXpathElementValue(doc, "/message/source/user_source");
                            source_class = CE_TPL_Manager.findCorrectSource(source_name);
                            doc = source_class.addInfo(doc);
                            String trans_name = XMLManager.getXpathElementValue(doc, "/message/transformation/type");
                            System.out.println("'"+trans_name+"'");
                            I_TPL_Content_Enricher trans = CE_TPL_Manager.findCorrectTransformation(trans_name);
                            if(trans!=null)
                                doc = trans.addInfo(doc);
                            izlaz = XMLManager.crateStringFromXMLDocument(doc);
                        } catch (Exception ex) {
                            System.err.println(ex);
                        }

                        in.setBody(izlaz);
                    }
                })
                .to("file:target/gen2/"+this.to);
    }

}
