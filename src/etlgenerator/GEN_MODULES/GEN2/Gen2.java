/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.GEN_MODULES.GEN2;

import etlgenerator.GEN_MODULES.GEN1.Gen1;
import etlgenerator.GEN_MODULES.Gen_module_I;
import helper.file.FileManager;
import helper.fileFilters.DirectoryFilter;
import helper.fileFilters.FileExtensionFilter;
import helper.source_code_generator.CompilationException;
import helper.source_code_generator.CompileClassManager;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.RoutesBuilder;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Gen2 implements Gen_module_I {

    private static Gen2 gen2 = null;

    protected Gen2() {
    }

    ;
    
    public static Gen2 getInstance() {
        if (gen2 == null) {
            gen2 = new Gen2();
        }
        return gen2;
    }

    @Override
    public List<RoutesBuilder> initializeThisModule() throws Exception {
        List<RoutesBuilder> routesBuilders = buildRouters();
        return routesBuilders;
    }

    private List<RoutesBuilder> buildRouters() throws CompilationException, InterruptedException {
        List<RoutesBuilder> routers = new ArrayList<RoutesBuilder>();

        String current_dir = System.getProperty("user.dir");
        File gen1_dirs = new File(current_dir + File.separator + "target" + File.separator + "gen1");
        File[] gen1_source_dirs = gen1_dirs.listFiles(new DirectoryFilter());

        System.out.println("Čekam da završi generiranje routera!");
        Thread.sleep(10000); //treba čekati da ovaj zavšri s kreiranje ETLGen2Routers
        File dir_target_cmd_gen2 = new File(current_dir + File.separator + "target" + File.separator + "cmd_router_gen2");
        String[] ext = {"xml"};
        FileExtensionFilter filter = new FileExtensionFilter();
        filter.setExtension(ext);
        File[] files = dir_target_cmd_gen2.listFiles(filter);
        int wait = 0;
        while(files.length != 0) {
            files = dir_target_cmd_gen2.listFiles(filter);
            Thread.sleep(1000);
            System.err.println("Wait for gen2: " + wait);
            wait++;
        }

        for (File gen1_source_dir : gen1_source_dirs) {
            String name = gen1_source_dir.getName();

            File file = new File(current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2" + File.separator + "ETLGen2Router" + name + ".java");
            System.out.println("GEN2 COMPILE: " + file.getAbsolutePath());

            int wait2 = 0;
            if (!file.exists()) {
                Thread.sleep(1000);
                System.err.println("Wait for " + file.getName() + ": " + wait2);
                wait++;
            }

            //File[] files1 = {file};
            String fullClassName = CompileClassManager.compileTemplateClass(file);
            System.out.println("GEN2 CREATE:" + fullClassName);
            RoutesBuilder etlgen2router = null;

            try {
                Class klasa = Class.forName(fullClassName);
                etlgen2router = (RoutesBuilder) klasa.newInstance();
                //Method metoda = klasa.getMethod("method", null);
                //metoda.invoke(instanca, null);
            } catch (Exception ex) {
                System.err.println(ex);
            }
            routers.add(etlgen2router);
            //break;
        }

        //Thread.sleep(5000);
        File gen2_dir = new File(current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2");
        String[] ext2 = {"java"};
        FileExtensionFilter filter2 = new FileExtensionFilter();
        filter2.setExtension(ext2);
        File[] files2 = gen2_dir.listFiles(filter2);
        for (File file : files2) {
            if (file.isDirectory()) {
                continue;
            }
            if (file.getName().contains("ETLGen2AgregatorAndCE")) {
                String fullClassName = CompileClassManager.compileTemplateClass(file);
                System.out.println("GEN2 CREATE:" + fullClassName);
                RoutesBuilder etlgen2agg = null;

                try {
                    Class klasa = Class.forName(fullClassName);
                    etlgen2agg = (RoutesBuilder) klasa.newInstance();
                    //Method metoda = klasa.getMethod("method", null);
                    //metoda.invoke(instanca, null);
                } catch (Exception ex) {
                    System.err.println(ex);
                }
                routers.add(etlgen2agg);
            }
        }

        System.out.println("KRAJ");
        return routers;
    }

    public RoutesBuilder createGen2routersClasses() throws Exception {

        String current_dir = System.getProperty("user.dir");
        File gen1_dirs = new File(current_dir + File.separator + "target" + File.separator + "gen1");
        File[] gen1_source_dirs = gen1_dirs.listFiles(new DirectoryFilter());

        for (File gen1_source_dir : gen1_source_dirs) {
            String name = gen1_source_dir.getName();
            createRouter(name);
        }

        return new Gen2RouterCreator();
    }

    private void createRouter(String router_source_name) throws CompilationException {
        String current_dir = System.getProperty("user.dir");

        FileManager.copyFile(
                current_dir + File.separator + "src" + File.separator + "templates", "ETLGen2Router.java", current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2", "ETLGen2Router" + router_source_name + ".java");

        String file_content = "";
        try {
            file_content = FileManager.readFileContentToString(current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2", "ETLGen2Router" + router_source_name + ".java");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        file_content = file_content.replace("package templates;", "package etlgenerator.GEN_MODULES.GEN2;");
        file_content = file_content.replace("gen2_source_X", "gen2_" + router_source_name);
        file_content = file_content.replace("class ETLGen2Router", "class ETLGen2Router" + router_source_name);
        FileManager.createFileFromString(file_content, current_dir + File.separator + "src" + File.separator + "etlgenerator" + File.separator + "GEN_MODULES" + File.separator + "GEN2", "ETLGen2Router" + router_source_name + ".java");

    }

}
