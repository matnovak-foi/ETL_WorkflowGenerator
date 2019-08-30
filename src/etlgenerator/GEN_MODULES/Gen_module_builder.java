/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES;

import etlgenerator.GEN_MODULES.GEN1.Gen1;
import etlgenerator.GEN_MODULES.GEN2.Gen2;
import etlgenerator.GEN_MODULES.GEN3.Gen3;
import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Gen_module_builder {

    int gen1=0;
    int gen2=0;
    int gen3=0;
 

    public List<RoutesBuilder> buildGEN1module() throws Exception {
        return (Gen1.getInstance()).initializeThisModule();
       
    }

    public List<RoutesBuilder> buildGEN2modules(CamelContext context) throws Exception {
        Gen2 gen2 = Gen2.getInstance();
        context.addRoutes(gen2.createGen2routersClasses());
        return gen2.initializeThisModule();
    }

    public List<RoutesBuilder> buildGEN3module() throws Exception {
        return (Gen3.getInstance()).initializeThisModule();
    }

}
