/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etlgenerator.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class CE_TPL_Manager {
    public static I_TPL_Content_Enricher findCorrectSource(String source_properties){
        String type = "";
        Properties prop = new Properties();
         try {
            InputStream inputStream = 
                new FileInputStream(
                        new File(
                                System.getProperty("user.dir")+File.separator+"target"
                                        +File.separator+"input_source_desc"
                                        +File.separator+source_properties+".properties"));
       
            prop.load(inputStream);
            type = prop.getProperty("type").trim().replace("\"", "");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        System.out.println("CE TPL Manager type is: '"+type+"'");
        if(type.equalsIgnoreCase("MySQL"))
            return new TPL_SourceMySQL();
        else if(type.equalsIgnoreCase("PostgreSQL"))
            return new TPL_SourcePostgreeSQL();
        else if(type.equalsIgnoreCase("FlatFile"))
            return new TPL_SourceFlatFile();
        
        System.out.println("CE TPL Manager type not found for: "+type);
        return null;
    }
    public static I_TPL_Content_Enricher findCorrectTransformation(String transformation_name){
 
        if(transformation_name.equalsIgnoreCase("convertDate"))
            return new TPL_T_DateFormat();
        else if(transformation_name.equalsIgnoreCase("merge"))
            return new TPL_T_Merge();
       
        return null;
    }
}
