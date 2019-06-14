//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to generate a document-term matrix in ARFF format.
//              (http://www.cs.waikato.ac.nz/ml/weka/arff.html)
//*****************************************************************************

package TCTPreprocessing;

import TCT.Preprocessing;
import TCTStructures.TermFreq;
import TCTStructures.FeatureList;
import TCTIO.ListFiles;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;
import ptstemmer.Stemmer;
import ptstemmer.implementations.OrengoStemmer;

public class TextRepresentation {
    
    //Function to read a text collection and generate a document-term matrix
    public static void Represent(String dirIn, String dirOut, String relationName, boolean dirClass, String language, boolean stemming, boolean removeStopWords, int df, boolean freq, boolean sparse, int numGrams){
        
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<FeatureList> terms = new ArrayList<FeatureList>(); // list constendo os atributos de cada documento da coleção
        HashMap<String,Integer> termDF = new HashMap<String,Integer>(); //armazena as DF dos atributos
        HashMap<String,String> stemPal = new HashMap<String,String>(); // dicionário do tipo palavra - stem
        ArrayList<File> filesIn = new ArrayList<File>();
        StopWords sw = new StopWords(language); //Objeto para remoção das stopwords dos documentos
        Cleaner cln = new Cleaner();
        Stemmer stemPt = new OrengoStemmer(); //Objeto para a radicalização em português
        StemmerEn stemEn = new StemmerEn(); //Objeto para a radicalização em inglês
        System.out.println("Listing Files... Input Diretory: " + dirIn);
        ListFiles.List(new File(dirIn), filesIn); //Vetor para armazenar os documentos textuais
        
        Collections.sort(filesIn);
        
        System.out.println("Extracting Terms...");
        for(int i =0;i<filesIn.size();i++){ // criando vetores contendo os atributos e suas frquências em cada documento da coleção
            System.out.println(filesIn.get(i).getAbsoluteFile());
            FeatureList features = new FeatureList();
            features.setFeatures(Preprocessing.FeatureGeneration(filesIn.get(i), language, removeStopWords, stemming, stemPal, names, termDF, sw, cln, stemPt, stemEn));
            terms.add(i, features);
        }
        
        sw = null;
        cln = null;
        stemPt = null;
        stemEn = null;
        
        if(df>0){
            names = new ArrayList<String>();
            Set<String> featureName = termDF.keySet();
            Iterator it = featureName.iterator();
            while(it.hasNext()){
                String key = (String)it.next();
                if(termDF.get(key) >= df){
                    names.add(key);
                }
            }
        }

        HashMap<String,String> classes = new HashMap<String,String>();
        ArrayList<String> allClasses = new ArrayList<String>();
        if(dirClass){
            classes = GetTextCollectionClasses(filesIn, allClasses);
        }

        int numTerms = 0;
        if(dirClass){
            numTerms = names.size() + 2;
        }else{
            numTerms = names.size() + 1;
        }
        
        System.out.println("Generating an ARFF file");
        
        GenerateARFF(names, relationName, dirClass, allClasses, filesIn.size(), numTerms, dirOut, filesIn, terms, freq, classes, sparse);
        JOptionPane.showMessageDialog(null, "Document-term matrix was generated.", "TCT", JOptionPane.PLAIN_MESSAGE);
    }
    
    //Function to return the labels of text documents. Directories are treated as labels.
    public static HashMap<String,String> GetTextCollectionClasses(ArrayList<File> filesIn, ArrayList<String> allClasses){
        HashMap<String,String> classes = new HashMap<String,String>();
        
        for(int i=0;i<filesIn.size();i++){
            String arquivo = filesIn.get(i).getAbsolutePath();
            arquivo = arquivo.replace("\\", "/");
            arquivo = arquivo.substring(0,arquivo.lastIndexOf("/"));
            arquivo = arquivo.substring(arquivo.lastIndexOf("/") + 1, arquivo.length());
            classes.put(filesIn.get(i).getAbsolutePath(), arquivo);
            if(!allClasses.contains(arquivo)){
                allClasses.add(arquivo);
            }
        }
        return classes;
    }
    
    
    public static boolean GenerateARFF(ArrayList<String> names, String relacao, boolean classe, ArrayList<String> infClasses, int numFiles, int numTerms, String dirOut, ArrayList<File> filesIn, ArrayList<FeatureList> atributos, boolean freq, HashMap<String,String> classes, boolean sparse){
        
        try{
            System.out.println("Saving ARFF file...");
            BufferedWriter outputArff = new BufferedWriter(new FileWriter(dirOut + "/" + relacao + ".arff"));

            //Saving Header
            
            outputArff.write("@RELATION " + relacao);
            outputArff.newLine();
            outputArff.write("");
            outputArff.newLine();

            for(int i=0; i<names.size(); i++){
                outputArff.write("@ATTRIBUTE " + names.get(i) + " REAL\n");
            }

            if(classe){
                outputArff.write("@ATTRIBUTE class_atr {");
                StringBuffer lineClasse = new StringBuffer();
                for(int j=0;j<infClasses.size();j++){
                    lineClasse.append(infClasses.get(j)+",");
                }
                String todasClasses = lineClasse.toString().substring(0,lineClasse.toString().length()-1);
                outputArff.write(todasClasses + "}\n");
            }
            outputArff.flush();
            
            outputArff.write("");
            outputArff.newLine();
            outputArff.write("@DATA");
            outputArff.newLine();

            
            int[] data = new int[numTerms];
            
            for(int i=0;i<numFiles;i++){
                for(int j=0;j<numTerms;j++){
                    data[j] = 0;
                }
                FeatureList atrbs = atributos.get(i);
                for(int j=0;j<atrbs.getFeatures().size();j++){
                    TermFreq item = atrbs.getFeature(j);
                    int pos = names.indexOf(item.getFeature());
                    if(freq){
                        data[pos + 1] = item.getFrequency();
                    }else{
                        if(item.getFrequency() > 0){
                            data[pos + 1] = 1;
                        }else{
                            data[pos + 1] = 0;
                        }
                        
                    }
                    
                }
                StringBuffer line = new StringBuffer();
                if(sparse == true){
                    for(int j=1;j<numTerms-1;j++){
                        if(data[j] > 0){
                            line.append((j-1) + " " + data[j] + ",");
                        }
                    }
                }else{
                    for(int j=1;j<numTerms-1;j++){
                        line.append(data[j] + ",");
                    }
                }
                
                if(sparse == true){
                    if(classe){
                        line = line.append((numTerms - 2) + " " + "\"" + classes.get(filesIn.get(i).getAbsolutePath()) + "\",");
                    }
                }else{
                    if(classe){
                        line = line.append("\"" + classes.get(filesIn.get(i).getAbsolutePath()) + "\",");
                    }    
                }
                
                
                String output = line.substring(0,line.length()-1);
                if(sparse == true){
                    outputArff.write("{" + output + "}" + "\n");
                }else{
                    outputArff.write(output + "\n");
                }
                

            }

            outputArff.flush();
            outputArff.close();

        }

        catch(IOException ex){
            System.err.println("Error when saving document-term matrix.");
            ex.printStackTrace();
            System.exit(0);
        }
        System.out.println("ARFF file was generated");
        return true;
    }
    
}

    