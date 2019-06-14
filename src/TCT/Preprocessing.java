//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 26, 2015
// Description: Steps to generate a document-term matrix from a text collections
//*****************************************************************************

package TCT;

import TCTPreprocessing.CharsetRecognition;
import TCTStructures.TermFreq;
import TCTPreprocessing.Cleaner;
import TCTPreprocessing.StemmerEn;
import TCTPreprocessing.StopWords;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import ptstemmer.Stemmer;

public class Preprocessing {
    
    /* Function to extract features from a textual document
       file - path of textual document
       lang [port|engl] - laguage a textual document (only portugues and english
       remStopWords - remove stopwords
       stemming - replace the word by their stem
       stemTerm - mapping from stemmed temrs to original words
       terms - list of generated terms
       termDF - document frequency of terms
       sw - instance of the StopWords class
       cln - instance of the Cleaner class
       stemPt - instance of the Stemmer class
       stemEn - instance of the StemmerEn class
    */ 
    
    public static ArrayList<TermFreq> FeatureGeneration(File file, String lang, boolean remStopWords, boolean stemming, HashMap<String,String> stemTerm, ArrayList<String> terms, HashMap<String,Integer> termDF, StopWords sw, Cleaner cln, Stemmer stemPt, StemmerEn stemEn){
        ArrayList<TermFreq> atributos = new ArrayList<TermFreq>();

        StringBuffer txt = new StringBuffer();

        try{
            if (!file.exists()) {
                System.out.println("File not found: " +file.getAbsolutePath());
            }
            String charset = CharsetRecognition.Recognize(file);
            //String charset = "UTF-16";
            BufferedReader txtFile = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            
            String line;
            while( (line = txtFile.readLine()) != null ) { txt.append(line + " "); } // Leitura do fileuivo texto e armazenamento na variável txt
            txtFile.close();
        }catch(Exception e){
            System.err.println("Error when reading the file " + file.getAbsolutePath() + ".");
            e.printStackTrace();
            System.exit(0);
        }

        String cleanText = cln.clean(txt.toString()); //Text cleaning
        if(remStopWords==true){
            cleanText = sw.removeStopWords(cleanText); //Stopwords removal
        }

        ArrayList<String> words = new ArrayList<String>();
        HashMap<String, Integer> hashTermFreq = new HashMap<String, Integer>();
        String[] allWords = cleanText.split(" "); //Stores the words of a text in a vector
        if(stemming == true){
            if(lang.equals("port")){ //Word stemming
                for(int i = 0;i<allWords.length;i++){
                    String key = allWords[i];
                    String stem;
                    if(stemTerm.containsKey(key)){
                        stem = stemTerm.get(key);
                    }else{
                        stem = new String(stemPt.wordStemming(key));
                        stemTerm.put(key, stem);
                    }
                    if(hashTermFreq.containsKey(stem)){
                        Integer freq = hashTermFreq.get(stem);
                        hashTermFreq.put(stem, freq + 1);
                    }else{
                        if(stem.length()>1){
                            hashTermFreq.put(stem, 1);
                            if(!terms.contains(stem)){
                                terms.add(stem);
                            }
                            if(!words.contains(stem)){
                                words.add(stem);
                                if(termDF.containsKey(stem)){
                                    int value = termDF.get(stem);
                                    value++;
                                    termDF.put(stem, value);
                                }else{
                                    termDF.put(stem, 1);
                                }
                            }
                        }
                    }
                    
                }
            }else{
                for(int i = 0;i<allWords.length;i++){
                    String key = allWords[i];
                    key = key.trim();
                    String stem;
                    if(stemTerm.containsKey(key)){
                        stem = stemTerm.get(key);
                    }else{
                        stem = new String(StemmerEn.get(key));
                        stemTerm.put(key, stem);
                    }

                    if(hashTermFreq.containsKey(stem)){
                        Integer freq = hashTermFreq.get(stem);
                        hashTermFreq.put(stem, freq + 1);
                    }else{
                        if(stem.length()>1){
                            hashTermFreq.put(stem, 1);
                            if(!terms.contains(stem)){
                                terms.add(stem);
                            }
                            if(!words.contains(stem)){
                                words.add(stem);
                                if(termDF.containsKey(stem)){
                                    int value = termDF.get(stem);
                                    value++;
                                    termDF.put(stem, value);
                                }else{
                                    termDF.put(stem, 1);
                                }
                            }
                        }
                        
                    }
                }
            }
        }else{
                for(int i = 0;i<allWords.length;i++){
                    String key = allWords[i];
                    key = key.trim();
                    if(!stemTerm.containsKey(key)){
                        stemTerm.put(key, key);
                    }
                    if(hashTermFreq.containsKey(key)){
                        Integer freq = hashTermFreq.get(key);
                        hashTermFreq.put(key, freq + 1);
                    }else{
                        if(key.length()>1){
                            hashTermFreq.put(key, 1);
                            if(!terms.contains(key)){
                                terms.add(key);
                            }
                            if(!words.contains(key)){
                                words.add(key);
                                if(termDF.containsKey(key)){
                                    int value = termDF.get(key);
                                    value++;
                                    termDF.put(key, value);
                                }else{
                                    termDF.put(key, 1);
                                }
                            }
                        }
                    }
                }
        }

        Set<String> termList =  hashTermFreq.keySet();
        Object[] termArray = termList.toArray();
        for(int i=0;i<termArray.length;i++){
            String key = termArray[i].toString();
            atributos.add(new TermFreq(key,hashTermFreq.get(key)));
        }
        return atributos;
    }


}
