/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN2;

import etlgenerator.templates.CE_TPL_Manager;
import etlgenerator.templates.I_TPL_Content_Enricher;
import etlgenerator.templates.TPL_Destination;
import helper.XMLManager;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ETLGen2AgregatorAndCEsource_2_Name_Surname extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:target/gen2/source_2Name_Surname?noop=false")
                .multicast().to("file:target/gen3/input")
                .aggregate(new AggregationStrategy() {

                    @Override
                    public Exchange aggregate(Exchange exchng, Exchange exchng1) {
           
                        try {
                            if (exchng == null) {
                                //System.out.println("PRAZNO");
                                Document doc_final = XMLManager.crateDocumentForXML("\n<root_message>\n\t<destination>\n\t</destination>\n</root_message>");
                                String in2 = exchng1.getIn().getBody(String.class);
                                Document doc2 = XMLManager.crateDocumentForXML(in2);
                                Node msg2 = XMLManager.getElementNodeFromXML(doc2, "message");

                                Element dest_name = doc_final.createElement("name");
                                dest_name.setTextContent("employees");
                                doc_final = XMLManager.addNodeToElementNodeInXML(doc_final, "destination", dest_name);
                                Element att_name = doc_final.createElement("attribute");
                                att_name.setTextContent("Name_Surname");
                                doc_final = XMLManager.addNodeToElementNodeInXML(doc_final, "destination", att_name);

                                doc_final = XMLManager.addNodeToElementNodeInXML(doc_final, "root_message", doc_final.adoptNode(msg2.cloneNode(true)));
                                doc_final = (new TPL_Destination()).addInfo(doc_final);

                                String trans_name = "merge";
                                System.out.println("'" + trans_name + "'");
                                I_TPL_Content_Enricher trans = CE_TPL_Manager.findCorrectTransformation(trans_name);
                                if (trans != null) {
                                    doc_final = trans.addInfo(doc_final);
                                }
                                exchng1.getIn().setBody(XMLManager.crateStringFromXMLDocument(doc_final));

                            } else {
                                //System.out.println("PUNO");
                                String in1 = exchng.getIn().getBody(String.class);
                                Document doc_final = XMLManager.crateDocumentForXML(in1);

                                String in2 = exchng1.getIn().getBody(String.class);
                                Document doc2 = XMLManager.crateDocumentForXML(in2);
                                //System.out.println("Aggregator: " + in1 + in2);

                                Node msg2 = XMLManager.getElementNodeFromXML(doc2, "message");
                                doc_final = XMLManager.addNodeToElementNodeInXML(doc_final, "root_message", doc_final.adoptNode(msg2.cloneNode(true)));
                                exchng1.getIn().setBody(XMLManager.crateStringFromXMLDocument(doc_final));
                            }
                        } catch (Exception ex) {
                            System.err.println(ex);
                        }
                        return exchng1;
                    }
                })
                .xpath("/message/source/user_source")
                .completionSize(2)
                .to("file:target/gen3/input?fileName=message-12_source_2_gen2.xml");
    }

}
