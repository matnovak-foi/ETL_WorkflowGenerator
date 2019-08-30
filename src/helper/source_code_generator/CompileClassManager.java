/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper.source_code_generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Class has only static methods to compile and generate java source code based
 * on templates or given string
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class CompileClassManager {

    /**
     * Excepts the template file and compiles the java source code that is ready
     * for usage
     *
     * @param file - template file for compile
     * @return String - full class name that needs to be used for Class.forName
     * @throws CompilationException
     */
    public static String compileTemplateClass(File file) throws CompilationException {
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));
        boolean ok = jc.getTask(null, fileManager, diagnostics, null, null, compilationUnits1).call();

        try {
            fileManager.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        printDiagnostics(diagnostics, !ok);

        System.out.println("APath: " + file.getAbsolutePath());
        System.out.println("Name: " + file.getName());
        System.out.println("Path: " + file.getPath());
        System.out.println("Parent: " + file.getParent());
        String file_name = file.getName().substring(0, file.getName().lastIndexOf("."));
        String file_class_name = file_name + ".class";
        System.out.println("Final file name: " + file_class_name);

        String build_dir = System.getProperty("user.dir") + File.separator + "build" + File.separator + "classes";
        String file_parent_dir_from_src = file.getParent().substring(file.getParent().indexOf("src") + 4);
        build_dir += File.separator + file_parent_dir_from_src;
        System.out.println("Build dir" + build_dir);

        moveCompiledClass(file.getParent(), file_class_name, build_dir);

        String full_class_name = file_parent_dir_from_src + File.separator + file_name;

        return full_class_name.replace(File.separator, ".");
    }
    
    /**
     * Compiles java source code from string and creates .class file that is
     * ready to use
     *
     * @param fullClassName - class name with package like
     * package_name.class_name
     * @param code - java source code that needs to be compiled
     * @throws CompilationException
     */
    public static void compileClassFromString(String fullClassName, String code) throws CompilationException {
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        JavaFileObject file = new JavaSourceFromString(className, code);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        boolean ok = jc.getTask(null, null, diagnostics, null, null, compilationUnits).call();

        printDiagnostics(diagnostics, !ok);

        String parrent_dir = System.getProperty("user.dir");
        String file_class_name = className + ".class";
        System.out.println("Final file name: " + file_class_name);

        String build_dir = System.getProperty("user.dir") + File.separator + "build" + File.separator + "classes";
        String class_package = fullClassName.substring(0, fullClassName.lastIndexOf("."));
        build_dir += File.separator + class_package.replace(".", File.separator);
        System.out.println("Build dir" + build_dir);

        moveCompiledClass(parrent_dir, file_class_name, build_dir);
    }

    /**
     * Prints out errors that exist in the new compiled java source If got exit
     * throws error
     *
     * @param diagnostics - diagnostic from compilation
     * @param exit - if the compilation failed
     * @throws CompilationException
     */
    private static void printDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics, boolean exit) throws CompilationException {
        String greska = "";
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            greska += diagnostic.getKind() + " (code: " + diagnostic.getCode() + ") on line " + diagnostic.getLineNumber() + " in " + diagnostic.getSource();
            greska += "\n   " + diagnostic.getMessage(null) + "\n";
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }

        if (exit) {
            throw new CompilationException("\n " + greska + " \n");
        }
    }

    /**
     * Class that is compiled needs to be moved into build directory so that the
     * rest of the program can use this new classes
     *
     * @param from_path - path to file that needs to be copied
     * @param file_name - file name with .class extension
     * @param to_path - path where this file is copied to usually some build
     * folder
     */
    private static void moveCompiledClass(String from_path, String file_name, String to_path) {
        File dir = new File(to_path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String from = from_path + File.separator + file_name;
        String to = to_path + File.separator + file_name;
        try {
            Files.move(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
