
import TCTConfigurations.UtilitiesFeatureExtractionConfiguration;
import TCTIO.ListFiles;
import java.io.File;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rafael
 */
public class FeatureExtraction {
    
    public static  void Learning(UtilitiesFeatureExtractionConfiguration configuration){
     
        ArrayList<File> filesIn = new ArrayList<File>();
        ListFiles.List(new File(configuration.getDirArffs()), filesIn); //Vetor para armazenar os documentos textuais
        for(int i =0;i<filesIn.size();i++){
            
            
        }
        
    }
    
}
