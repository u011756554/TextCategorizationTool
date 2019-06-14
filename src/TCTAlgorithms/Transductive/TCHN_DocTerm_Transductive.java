//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: March 12, 2015
// Description:  This is an implementation of TCBHN (Transductive Classification
//               based on Bipartite Heterogeneus Network) algorithm to perform 
//               transductive classification of texts represented in a bipartite 
//               network (document-term relations).
//********************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instances;

public class TCHN_DocTerm_Transductive extends TransductiveLearner {

    private double[][] fDocs; // Class information of documents
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberGlobalIterations; // Maximum Number of Global Iterations
    private int maxNumberLocalIterations; // Maximum Number of Local Iterations
    private double errorCorrectionRate; // Error Correction Rate
    private double minError; //Minimum mean squared error
    
    // Constructor
    public TCHN_DocTerm_Transductive(){
        super();
        setmaxNumberGlobalIterations(100);
        setmaxNumberLocalIterations(100);
        setErrorCorrectionRate(0.1);
    }
    
    // Constructor
    public TCHN_DocTerm_Transductive(int maxNumberGlobalIterations, int maxNumberLocalIterations, double errorCorrectionRate){
        setmaxNumberGlobalIterations(maxNumberGlobalIterations);
        setmaxNumberLocalIterations(maxNumberLocalIterations);
        setErrorCorrectionRate(errorCorrectionRate);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = getClassInformation(dataTrain,dataTest);
        fUnlabeledDocs = new double[numTest][numClasses];
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
        for(int inst=0;inst<(numTrain+numTest);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            adjacencyListPDocTerm[inst] = new Neighbor();
        }
        for(int inst=0;inst<numTrain;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTrain.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTrain.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
                }    
            }
        }
        for(int inst=0;inst<numTest;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTest.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTest.instance(inst).value(term);
                    adjancecyListDocTerm[inst + numTrain].AddNeighbor(indVal);
                }    
            }
        }
        
        //Normalizing doc-term relations
        double[] dDoc = new double[numTrain + numTest];
        for(int inst=0;inst<(numTrain+numTest);inst++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                grau += neighbors.get(term).value;
            }
            dDoc[inst] = grau;
        }
        for(int inst=0;inst<(numTrain + numTest);inst++){
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                if(dDoc[inst] != 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(term).index;
                    indVal.value = (double)neighbors.get(term).value / (double)dDoc[inst];
                    adjacencyListPDocTerm[inst].AddNeighbor(indVal);
                }
            }
        }
        
        // Learning Algorithm
        int numIt = 0;
        boolean exit = false;
        int numIterationsTotal = 0;
        while(exit == false){
            
            boolean exit2 = false;
            int numIterationsInternas = 0;
            //Optimizing class information of terms considering labeled documents
            while(exit2 == false){
                double meanError = 0;
                for(int inst=0;inst<numTrain;inst++){
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                    double[] estimatedClasses = classifyInstance2(neighbors);
                    for(int classe=0;classe<numClasses;classe++){
                        double error = yDoc[inst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighbors.size();term++){
                                double currentWeight = fTerms[neighbors.get(term).index][classe];
                                double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[inst].getNeighbor(term).value * error);
                                fTerms[neighbors.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                meanError = (double)meanError/(double)numTrain;
                // Analysis of stopping criteria 
                if((getMaxNumberLocalIterations() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }
            
            //Propagating class information from terms to unlabeled documents
            for(int inst=0;inst<numTest;inst++){
                fDocsTemp[numTrain + inst] = classifyInstance2(adjacencyListPDocTerm[numTrain + inst].getNeighbors());
            }
            
            //Analysis of stopping criteria
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
                }    
            }
            
            //Optimizing class information of terms considering the class information assigned to unlabeled documents
            exit2 = false;
            numIterationsInternas = 0;
            while(exit2 == false){
                double meanError = 0;
                for(int inst=0;inst<numTest;inst++){
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst + numTrain].getNeighbors();
                    double[] estimatedClasses = classifyInstanceReal(neighbors);
                    for(int classe=0;classe<numClasses;classe++){
                        double error = fDocs[numTrain + inst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[numTrain + inst].getNeighbor(term).value * error);
                            fTerms[neighbors.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                meanError = (double)meanError/(double)numTest;
                //Analysis of stopping criteria
                if((getMaxNumberLocalIterations() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }
            
            //Analysis of stopping criteria
            numIterationsTotal++;
            numIt++;
            if((acmDif == 0)||(getMaxNumberGlobalIterations() == numIterationsTotal)){
                exit = true;
            }
        }
        
        setNumIterations(numIt);
        
        //Assingning labels to unlabaled documents
        for(int inst=0;inst<numTest;inst++){
            fDocs[numTrain + inst] = classifyInstance(adjancecyListDocTerm[numTrain + inst].getNeighbors());;
        }
        for(int inst=0;inst<numTest;inst++){
            int ind = -1;
            double max = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(fDocs[inst + numTrain][classe] > max){
                    ind = classe;
                    max = fDocs[inst + numTrain][classe];
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
    
    // Function to classify an instance through the propagation of class information of terms (hard classification)
    public double[] classifyInstance(ArrayList<IndexValue> neighbors){
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        for(int classe=0;classe<numClasses;classe++){
            double acmClassWeight = 0; 
            for(int term=0;term<neighbors.size();term++){ 
                    acmClassWeight += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmClassWeight;
        }

        double max = Double.MIN_VALUE;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] > max){
                max = classes[classe];
                index = classe;
            }
        }
        for(int classe=0;classe<numClasses;classe++){
            classes[classe] = 0;
        }
        if(index == -1){
            return classes;
        }
        classes[index] = 1;
        return classes;
    }
    
    // Function to classify an instance through the propagation of class information of terms (soft classification)
    public double[] classifyInstanceReal(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        for(int classe=0;classe<numClasses;classe++){
            double acmClassWeight = 0; 
            for(int term=0;term<neighbors.size();term++){ 
                    acmClassWeight += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmClassWeight;
        }

        return classes;
    }
    
    // Function to return class confidences of the classification of an instance (soft classification)
    public double[] classifyInstance2(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        for(int classe=0;classe<numClasses;classe++){
            double acmClassWeight = 0; 
            for(int term=0;term<neighbors.size();term++){ 
                    acmClassWeight += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmClassWeight;
        }
        
        double min = Double.MAX_VALUE;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] < min){
                min = classes[classe];
            }
        }
        if(min < 0){
            for(int classe=0;classe<numClasses;classe++){
                double value = classes[classe];
                classes[classe] = value + Math.abs(min);
            }
        }
        total = 0;
        for(int classe=0;classe<numClasses;classe++){
            total += classes[classe];
        }
        for(int classe=0;classe<numClasses;classe++){
            if(total == 0){
                classes[classe] = 0;
            }else{
                double value = classes[classe];
                classes[classe] = value / total;
            }
            
        }
        return classes;
    }
    
    
    public Integer getMaxNumberGlobalIterations(){
        return maxNumberGlobalIterations;
    }
    
    public Integer getMaxNumberLocalIterations(){
        return maxNumberLocalIterations;
    }
    
    public double getErrorCorrectionRate(){
        return errorCorrectionRate;
    }
    
    public double getMinError(){
        return minError;
    }
    
    public void setmaxNumberGlobalIterations(int maxNumberGlobalIterations){
        this.maxNumberGlobalIterations = maxNumberGlobalIterations;
    }
    
    public void setmaxNumberLocalIterations(int maxNumberLocalIterations){
        this.maxNumberLocalIterations = maxNumberLocalIterations;
    }
    
    public void setErrorCorrectionRate(double errorCorrectionRate){
        this.errorCorrectionRate = errorCorrectionRate;
    }
    
    public void setMinError(double minError){
        this.minError = minError;
    }
    
    public void printWeightTerms(String fileName, Attribute classAtt, Instances data, double[][] f){
        try{
            FileWriter outputFile = new FileWriter(fileName);
            outputFile.write("Term;");
            
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
