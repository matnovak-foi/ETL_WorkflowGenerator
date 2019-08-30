/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN3;

import etlgenerator.GEN_MODULES.GEN1.Gen1;
import etlgenerator.GEN_MODULES.Gen_module_I;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.RoutesBuilder;
import templates.ETLGen3Router;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Gen3 implements Gen_module_I {

     private static Gen3 gen3 = null;
    
    protected Gen3(){};
    
    public static Gen3 getInstance() {
      if(gen3 == null) {
         gen3 = new Gen3();
      }
      return gen3;
   }
    
    @Override
    public List<RoutesBuilder> initializeThisModule() throws Exception {
        List<RoutesBuilder> routers = new ArrayList<>();
        routers.add(new ETLGen3Router());
        return routers;
    }
    
}
