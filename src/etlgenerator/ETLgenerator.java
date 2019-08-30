/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator;

import etlgenerator.GEN_MODULES.Gen_module_builder;
import helper.file.FileManager;
import helper.fileFilters.FileExtensionFilter;
import helper.source_code_generator.CompilationException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ETLgenerator {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, CompilationException {
       
     //INIT   
        String current_dir = System.getProperty("user.dir");
        
        FileManager.copyFile(
            current_dir+File.separator+"src"+File.separator+"templates"
            , "ETLSourceRouter.java"
            , current_dir+File.separator+"src"+File.separator+"etlgenerator" 
            , "ETLSourceRouter.java");
        
        String file_content = "";
            try {
                file_content = FileManager.readFileContentToString(current_dir+File.separator+"src"+File.separator+"etlgenerator", "ETLSourceRouter.java");
            } catch (IOException ex) {
                System.err.println(ex);
            }
        file_content = file_content.replace("package templates;", "package etlgenerator;");
        FileManager.createFileFromString(file_content, current_dir+File.separator+"src"+File.separator+"etlgenerator", "ETLSourceRouter.java");
        
         /*
        System.out.println();
        File file = new File(current_dir+"/src/templates/NewClass.java");
        
        File[] files1 = {file};
        String fullClassName = CompileClassManager.compileTemplateClass(file);
        try {
            Class klasa = Class.forName(fullClassName);
            Object instanca = klasa.newInstance();
            Method metoda = klasa.getMethod("method", null);
            metoda.invoke(instanca, null);
        } catch (Exception ex) {
            Logger.getLogger(ETLgenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            StringWriter writer = new StringWriter();
PrintWriter out = new PrintWriter(writer);
    out.println("package generated; ");
    out.println("public class HelloWorld {");
    out.println("  public static void main(String args[]) {");
    out.println("    System.out.println(\"This is in another java file\"+args[0]);");    
    out.println("  }");
    out.println("}");
    out.close();
    
    fullClassName = "generated.HelloWorld";
     CompileClassManager.compileClassFromString(fullClassName, writer.toString());  
    
     try {
            Class klasa = Class.forName(fullClassName);
            Object instanca = klasa.newInstance();
            Method metoda = klasa.getMethod("main", new Class[] { String[].class });
            String[] x = {"1"};
            metoda.invoke(instanca, new Object[] { x });
            metoda.invoke(null, new Object[] { x });
        } catch (Exception ex) {
            Logger.getLogger(ETLgenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
 */
        try {
            CamelContext context = new DefaultCamelContext();
            context.addRoutes(new Spliter());
            //context.addRoutes();
            // start the route and let it do its work
            System.out.println("Starting");
            context.start();
            
            //context.addRoutes(new ETLGen2AgregatorAndCE());
            
            Gen_module_builder gen_module_builder = new Gen_module_builder();
            List<RoutesBuilder> gen1_routes = gen_module_builder.buildGEN1module();
            for (Iterator<RoutesBuilder> iterator = gen1_routes.iterator(); iterator.hasNext();) {
                RoutesBuilder next = iterator.next();
                context.addRoutes(next);
            }
            //context.addRoutes(new ETLGen2Routersource_1());
            
            List<RoutesBuilder> gen2_routes = gen_module_builder.buildGEN2modules(context);
            for (Iterator<RoutesBuilder> iterator = gen2_routes.iterator(); iterator.hasNext();) {
                RoutesBuilder next = iterator.next();
                context.addRoutes(next);
            }
            
             List<RoutesBuilder> gen3_routes = gen_module_builder.buildGEN3module();
             for (Iterator<RoutesBuilder> iterator = gen3_routes.iterator(); iterator.hasNext();) {
                RoutesBuilder next = iterator.next();
                context.addRoutes(next);
            }
           
           // Gen_module_I gen1_module = gen_module_builder.buildGEN1module();
          //  gen1_module.initializeThisModule(context);
 
            
            //context.start();
            //ETLSourceRouter source_router = new ETLSourceRouter();
            /*ne radi nemože čitati iz istog izvora
            source_router.includeRoutes(new RouteBuilder() {

                @Override
                public void configure() throws Exception {
                   from("file:target/gen1_router?noop=true").
                   choice().
                   when(xpath("/message/source/type = 'PostgreSQL'"))
                   .to("file:target/other");
                }
            });*/
            //context.addRoutes(source_router);
            
            // add our route to the CamelContext
            //context.addRoutes(new ETLSourceRouter());

            
            Thread.sleep(5000);
            System.in.read();
            System.out.println("Done");
            // stop the CamelContext
            context.stop();
                    
        } catch (Exception ex) {
            System.out.println(ex);
        }
   
        //CLEANUP
        //obriši target/gen1 dir
        String target_dir = System.getProperty("user.dir") + File.separator + "target";
        FileManager.deleteDirContent(new File(target_dir + File.separator + "gen1"));
        FileManager.deleteDirContent(new File(target_dir + File.separator + "gen2"));
        FileManager.deleteDirContent(new File(target_dir + File.separator + "input"));
        FileManager.deleteDirContent(new File(target_dir + File.separator + "gen1_router"));
        FileManager.deleteDirContent(new File(target_dir + File.separator + "gen3"+File.separator+"output"));
        
        FileManager.copyFile(target_dir+ File.separator + "input_backup", "message.xml", target_dir+ File.separator + "input", "message.xml");
        
        //obriši ETLSourceRouter.java u etlgenerator
        File gen2_dir = new File(current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2");
        
        String[] ext = {"class"};
        FileExtensionFilter filter = new FileExtensionFilter();
        filter.setExtension(ext);
        File[] files = gen2_dir.listFiles(filter);
        for (File file : files) {
            file.delete();
        }
    }
    
    
}
