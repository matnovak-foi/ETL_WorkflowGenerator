/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN1;

import etlgenerator.Agregator;
import etlgenerator.GEN_MODULES.Gen_module_I;
import helper.fileFilters.DirectoryFilter;
import helper.fileFilters.FileExtensionFilter;
import helper.source_code_generator.CompilationException;
import helper.source_code_generator.CompileClassManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Gen1 implements Gen_module_I {
    
    private static Gen1 gen1 = null;
    
    protected Gen1(){};
    
    public static Gen1 getInstance() {
      if(gen1 == null) {
         gen1 = new Gen1();
      }
      return gen1;
   }

    @Override
    public List<RoutesBuilder> initializeThisModule() throws Exception {
        List<RoutesBuilder> routes_builders = new ArrayList<RoutesBuilder>();
        waitToCreate();
        Agregator agregator = new Agregator();
        String target_dir = System.getProperty("user.dir") + File.separator + "target";
        File file = new File(target_dir +File.separator + "gen1_router");
        agregator.completionSize = file.listFiles().length;
        routes_builders.add(agregator);
        //context.addRoutes(new etlgenerator.ETLSourceRouter());
        //System.out.println(context.getStatus());
        //etlsourcerouter.addRoutesToCamelContext(context);
        routes_builders.add(createRouter());
        
        routes_builders.addAll(createContentEnrichers());
        
        return routes_builders;
    }
    
    private List<RoutesBuilder> createContentEnrichers() throws Exception{
        List<RoutesBuilder> routes_builders = new ArrayList<RoutesBuilder>();
        
        String current_dir = System.getProperty("user.dir");
        File gen1_dir = new File(current_dir + File.separator + "target" + File.separator + "gen1");
        File[] gen1_source_dirs = gen1_dir.listFiles(new DirectoryFilter());
        
        for (File gen1_source_dir : gen1_source_dirs) {
            String name = gen1_source_dir.getName();
            File gen2_source_dir = new File(current_dir + File.separator + "target" + File.separator + "gen2" + File.separator + "gen2_" + name);
            gen2_source_dir.mkdir();
            ETLgen1_content_entricher ce = new ETLgen1_content_entricher(name, "gen2_" + name);
            routes_builders.add(ce);
        }
        return routes_builders;
    }

    private RoutesBuilder createRouter() throws CompilationException {
        String current_dir = System.getProperty("user.dir");
        File file = new File(current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "ETLSourceRouter.java");

        File[] files1 = {file};
        String fullClassName = CompileClassManager.compileTemplateClass(file);
        System.out.println(fullClassName);
        RoutesBuilder etlsourcerouter = null;

        try {
            Class klasa = Class.forName(fullClassName);
            etlsourcerouter = (RoutesBuilder) klasa.newInstance();
                //Method metoda = klasa.getMethod("method", null);
            //metoda.invoke(instanca, null);
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return etlsourcerouter;
    }

    private void waitToCreate() throws InterruptedException {
        String current_dir = System.getProperty("user.dir");
        File input_dir = new File(current_dir + File.separator + "target" + File.separator + "input");
        
        int wait = 0;
        String[] ext = {"xml"};
        FileExtensionFilter filter = new FileExtensionFilter();
        filter.setExtension(ext);
        File[] has_files = input_dir.listFiles(filter);
        System.out.println(has_files.length);
        while (has_files != null && has_files.length != 0) {
            Thread.sleep(100);
            wait++;
            has_files = input_dir.listFiles(filter);
            System.out.println("Wait to spliter (in 100ms): " + wait +" length"+ has_files.length);
        }
       
        System.out.println("Wait to spliter (in 100ms): " + wait);
        //Thread.sleep(1000);
    }
}
