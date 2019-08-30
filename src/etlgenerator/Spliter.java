/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator;

import etlgenerator.GEN_MODULES.GEN1.Gen1RouterCreator;
import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Spliter extends RouteBuilder{
    
    @Override
    public void configure() throws Exception {
        
        from("file:target/input?noop=false")
        .split()
        .tokenizeXML("message")
        .streaming() //nije obavezno
        //.setHeader(Exchange.FILE_NAME, simple("${file:name.noext}-${date:now:yyyyMMddHHmmssSSS}.${file:ext}"))
        .process(new Gen1RouterCreator())
        //.setBody(body().append("Tu sam"))
        .to("file:target/gen1_router?fileName=${file:name.noext}-${header.CamelSplitIndex}.${file:ext}");
        
    }
    
}
