/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper.source_code_generator;

/**
 * Class throws error when something is wrong during compilation 
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class CompilationException extends Exception{

    public CompilationException(String message) {
        super("Greška java source code koji se generira!"+message);
    }


}
