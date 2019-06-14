//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTIO;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ListFiles {
    
    public static ArrayList<String> listFilesStr(File dirIn){
        
        ArrayList<File> arrayFiles = new ArrayList<File>();
        File[] files = dirIn.listFiles();
        
        for(File f : files){
            arrayFiles.add(f);
        }
        
        for(int i=0;i<arrayFiles.size();i++){
            File file = arrayFiles.get(i);
            if(file.isDirectory()){
                File[] files2 = file.listFiles();
                for(File f : files2){
                    arrayFiles.add(f);
                }
                arrayFiles.remove(i);
                i--;
            }
        }

        ArrayList<String> paths = new ArrayList<String>();
        for(File f : arrayFiles){
            paths.add(f.getAbsolutePath());
        }
        
        Collections.sort(paths);
        return paths;
    }
    
    public static void List(File dirIn, ArrayList<File> filesIn){
        File[] files = dirIn.listFiles();
        for(int i=0;i<files.length;i++){
            if(!files[i].isDirectory()){
                filesIn.add(files[i]);
            }else{
                List(files[i], filesIn);
            }
        }
        
    }

    public static boolean List(File dirIn, File dirOut, ArrayList<File> filesIn, ArrayList<File> filesOut, File dirBase){
        File[] files = dirIn.listFiles();
        for(int i=0;i<files.length;i++){
            //System.out.println("File: " + files[i]);
            if(files[i].isDirectory()){
                File dirNameOut = new File(dirOut.toString() + files[i].toString().substring(dirBase.toString().length(), files[i].toString().length()));
                if(!dirNameOut.exists()){
                    boolean criou = dirNameOut.mkdir();
                    if(criou == false){
                        return false;
                    }
                }
                List(files[i], dirOut, filesIn, filesOut, dirBase);
            }
            if(!files[i].getName().endsWith("txt")){
                continue;
            }
            String fileName = files[i].toString();
            String fileOut = dirOut.toString() + fileName.substring(dirBase.toString().length(), fileName.length());
            filesIn.add(new File(fileName));
            filesOut.add(new File(fileOut));
        }
        Collections.sort(filesIn);
        return true;
    }
}
