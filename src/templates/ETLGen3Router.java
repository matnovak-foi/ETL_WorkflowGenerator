/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import helper.XMLManager;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ETLGen3Router extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        System.out.println("Configuring GEN3_router"+this.getClass().getName());
        
        from("file:target/gen3/input?noop=false")
           
            .to("file:target/gen3/output"); 
    }
    
}