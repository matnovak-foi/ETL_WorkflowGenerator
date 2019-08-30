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
public class ETLGen2Routersource_1 extends RouteBuilder{

    @Override
    public void configure() throws Exception {
         System.out.println("Configuring GEN2_router"+this.getClass().getName());
         
         from("file:target/gen2/gen2_source_1?noop=false").
            choice()
            
.when(xpath("/message/source/attribute = 'Name'"))
.to("file:target/gen2/source_1Name_Surname")
.otherwise()

.when(xpath("/message/source/attribute = 'Surname'"))
.to("file:target/gen2/source_1Name_Surname")
.otherwise()

.when(xpath("/message/source/attribute = 'Street'"))
.to("file:target/gen2/source_1Street_Number_City")
.otherwise()

.when(xpath("/message/source/attribute = 'Number'"))
.to("file:target/gen2/source_1Street_Number_City")
.otherwise()

.when(xpath("/message/source/attribute = 'City'"))
.to("file:target/gen2/source_1Street_Number_City")
.otherwise()
//ADD RULE
            .otherwise()
            .to("file:target/gen3/input");     
    }
    
}
