/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN2;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ETLGen2Routersource_3 extends RouteBuilder{

    @Override
    public void configure() throws Exception {
         System.out.println("Configuring GEN2_router"+this.getClass().getName());
         
         from("file:target/gen2/gen2_source_3?noop=false").
            choice()
            //ADD RULE
            .otherwise()
            .to("file:target/gen3/input");     
    }
    
}
