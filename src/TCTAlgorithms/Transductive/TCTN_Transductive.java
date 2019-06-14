//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of TCTN (Transductive Classification 
//              through Term Networks) [Rossi et. al, 2015].
// References: - R. G. Rossi, S. O. Rezende, A. A. Lopes, Term Network Approach 
//               for Transductive Classification, in: International Conference 
//               on Intelligent Text Processing and Computational Linguistics, 
//               2015 (in press).
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class TCTN_Transductive extends TransductiveLearner{
    
    private double[][] f; // Class information of terms
    private double[][] fTemp; // Class information of terms
    private double[][] y; // Real class information (labels) of labeled documents 
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double alpha; // Parameter of the algorithms
    
    // Matrices used in the algorithm
    private double[] d;
    private double[][] s;
    
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    public TCTN_Transductive(){
        super();
        setAlpha(0);
       
    }
    
    public TCTN_Transductive(int maxNumberInterations, double alpha, double sigma){
        setMaxNumIterations(maxNumberInterations);
        setAlpha(alpha);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        f = getClassInformation(dataTrain); // Assigning initial class information to terms
        y = getClassInformation(dataTrain);
        fTemp = new double[numTerms][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        
        if(getUse() == 0){
            d = new double[numTerms];
            s = new double[numTerms][];
            for(int term=0;term<(numTerms);term++){
                s[term] = new double[term + 1];
            }
            
            for(int atr1=0;atr1<numTerms;atr1++){
                double valueD = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                for(int atr2=0;atr2<neighbors.size();atr2++){
                    valueD += neighbors.get(atr2).value;
                }
                d[atr1] = Math.sqrt(valueD);
            }
            
            for(int atr1=0;atr1<numTerms;atr1++){
                ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                for(int atr2=0;atr2<neighbors.size();atr2++){
                    IndexValue indVal = neighbors.get(atr2);
                    if(atr1>indVal.index){
                        if((d[atr1] == 0)  || (d[indVal.index] == 0)){
                            s[atr1][indVal.index] = 0;
                        }else{

                            s[atr1][indVal.index] = indVal.value / (d[atr1] * d[indVal.index]);
                        }
                    }else{
                        if((d[atr1] == 0)  || (d[indVal.index] == 0)){
                            s[indVal.index][atr1] = 0;
                        }else{

                            s[indVal.index][atr1] = indVal.value / (d[atr1] * d[indVal.index]);
                        }
                    }

                }
            }
            d=null;
        }
        setUse(1);
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // Propagating labels among terms
            for(int atr1=0;atr1<numTerms;atr1++){
                for(int classe=0;classe<numClasses;classe++){
                    double fTemp1=0;
                    ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                    for(int atr2=0;atr2<neighbors.size();atr2++){
                        if(atr1>neighbors.get(atr2).index){
                            fTemp1 += alpha * s[atr1][neighbors.get(atr2).index] * f[neighbors.get(atr2).index][classe];
                        }else{
                            fTemp1 += alpha * s[neighbors.get(atr2).index][atr1] * f[neighbors.get(atr2).index][classe];
                        }
                    }
                    fTemp[atr1][classe] = fTemp1  + (1 - alpha)*y[atr1][classe];
                }    
            }

            double acmDif = 0;
            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fTemp[term][classe] - f[term][classe]);
                    f[term][classe] = fTemp[term][classe];
                }    
            }
            if(acmDif == previousDiff){
                countDiff++;
                if(countDiff>=100){
                    exit = true;
                }
            }else{
                countDiff = 0;
                previousDiff = acmDif;
            }
            int numIt = getNumiterations();
            
            // Analysis of stopping criteria
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
                
            numIt++;
            setNumIterations(numIt);

        }
        
        // Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            for(int classe=0;classe<numClasses;classe++){
                double acmClasse = 0;
                for(int term=0;term<numTerms;term++){
                    acmClasse += dataTest.instance(inst).value(term) * f[term][classe];
                }
                fUnlabeledDocs[inst][classe] = acmClasse;
            }
        }
        
        for(int inst=0;inst<numTest;inst++){
            int ind = -1;
            double maior = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(fUnlabeledDocs[inst][classe] > maior){
                    ind = classe;
                    maior = fUnlabeledDocs[inst][classe];
                }
            }
            for(int classe=0;classe<numClasses;classe++){
                fUnlabeledDocs[inst][classe] = 0;
            }
            if(ind == -1){
                fUnlabeledDocs[inst][0] = 1;
            }else{
                fUnlabeledDocs[inst][ind] = 1;
            }
        }
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public double[][] getClassInformation(Instances dataTrain){
        double[][] vetor = new double[numTerms][numClasses];
        for(int term=0;term<numTerms;term++){
            for(int classe=0;classe<numClasses;classe++){
                double numerator = 0;
                double denominator = 0;
                for(int inst=0;inst<numTrain;inst++){
                    Instance instance = dataTrain.instance(inst);
                    int infClasse = (int)instance.classValue();
                    denominator += instance.value(term);
                    if(infClasse == classe){
                        numerator += instance.value(term);
                    }
                }
                if(denominator == 0){
                    vetor[term][classe] = 0;
                }else{
                    vetor[term][classe] = numerator/denominator;
                }
            }
        }
        return vetor;
    }
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
    }
    
    
    
    public void printWeightTerms(String fileName, Attribute classAtt, Instances data, double[][] f){
        try{
            FileWriter outputFile = new FileWriter(fileName);
            outputFile.write("Termo;");
            
            numClasses = classAtt.numValues();
            for(int classe=0;classe<numClasses;classe++){
                outputFile.write(classAtt.value(classe) + ";");
            }
            outputFile.write("\n");
            
            for(int term=0;term<numTerms;term++){
                outputFile.write(data.attribute(term).name() + ";");
                for(int classe=0;classe<numClasses;classe++){
                    outputFile.write(f[term][classe] + ";");
                }
                outputFile.write("\n");
            }
            outputFile.close();
        }catch(Exception e){
            System.err.println("Error when saving class information of terms for classes.");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
