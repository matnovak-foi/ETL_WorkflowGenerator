/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator;

import helper.XMLManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Agregator extends RouteBuilder {

    public int completionSize;
    
    @Override
    public void configure() throws Exception {
        System.out.println("CONFIGURATE AGGREGATOR");
        from("file:target/gen3/output?noop=false")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchng) throws Exception {
                        //System.out.println("AG: "+exchng.getIn().getHeader("agregate"));
                        exchng.getIn().setHeader("agregate", "agregate");
                    }
                })
                .aggregate(header("agregate"), new AggregationStrategy() {
                    @Override
                    public Exchange aggregate(Exchange exchng, Exchange exchng1) {
                        //System.out.println("TU SAM");
                        try {
                            if (exchng == null) {

                                //System.out.println("PRAZNO");
                                Document doc_final = XMLManager.crateDocumentForXML("\n<final_message>\n\n\n</final_message>");
                                String in2 = exchng1.getIn().getBody(String.class);
                                Document doc2 = XMLManager.crateDocumentForXML(in2);
                                Node msg2 = XMLManager.getElementNodeFromXML(doc2, "root_message");
                                if (msg2 == null) {
                                    msg2 = XMLManager.getElementNodeFromXML(doc2, "message");
                                }
                                doc_final = XMLManager.addNodeToElementNodeInXML(doc_final, "final_message", doc_final.adoptNode(msg2.cloneNode(true)));
                                exchng1.getIn().setBody(XMLManager.crateStringFromXMLDocument(doc_final));

                            } else {
                                String in1 = exchng.getIn().getBody(String.class);
                                Document doc_final = XMLManager.crateDocumentForXML(in1);
                                String in2 = exchng1.getIn().getBody(String.class);
                                Document doc2 = XMLManager.crateDocumentForXML(in2);
                                Node msg2 = XMLManager.getElementNodeFromXML(doc2, "root_message");
                                if (msg2 == null) {
                                    msg2 = XMLManager.getElementNodeFromXML(doc2, "message");
                                }
                                doc_final = XMLManager.addNodeToElementNodeInXML(doc_final, "final_message", doc_final.adoptNode(msg2.cloneNode(true)));
                                exchng1.getIn().setBody(XMLManager.crateStringFromXMLDocument(doc_final));
                            }
                        } catch (Exception ex) {
                            System.err.println(ex);
                        }
                        return exchng1;
                    }
                })
                //.xpath("/agregate")
                .completionSize(completionSize)
                .to("file:target/output");
    }

}
