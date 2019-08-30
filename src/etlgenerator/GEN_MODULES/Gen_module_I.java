/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES;

import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public interface Gen_module_I {
    
    public List<RoutesBuilder> initializeThisModule () throws Exception;
}
