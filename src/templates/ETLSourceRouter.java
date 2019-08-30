/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ETLSourceRouter extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        System.out.println("Configuring GEN1_router"+this.getClass().getName());
        
        from("file:target/gen1_router?noop=false").
            choice().
            when(xpath("/message/command = 'yes2'"))
            .to("file:target/cmd_router_gen2")
            .otherwise()
            .when(xpath("/message/command = 'yes3'"))
            .to("file:target/cmd_router_gen3");//ADD RULE
            //.when(xpath("/message/source/type = 'XXX'"))
            //.to("file:target/other");
            //otherwise().to("file:target/messages/others");
    }
    
}
