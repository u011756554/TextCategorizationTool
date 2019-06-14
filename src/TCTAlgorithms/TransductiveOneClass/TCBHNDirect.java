/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.TransductiveOneClass;

import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import java.util.Collections;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class TCBHNDirect extends TransductiveLearnerOneClass{
    
    private double[][] fDocs; // Class information of documents
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numUnlabeled; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberGlobalIterations; // Maximum Number of Global Iterations
    private int maxNumberLocalIterations; // Maximum Number of Local Iterations
    private double errorCorrectionRate; // Error Correction Rate
    private double minError; //Minimum mean squared error
     
    public TCBHNDirect(){
        setMaxNumberGlobalIterations(100);
        setMaxNumberLocalIterations(100);
        setErrorCorrectionRate(0.01);        
    }
    
    
     
    @Override
    public void buildClassifier(Instances dataPositive, Instances dataUnlabeled) {
        numTrain = dataPositive.numInstances();
        numUnlabeled = dataUnlabeled.numInstances();
        numClasses = 2;
        numTerms = dataPositive.numAttributes() - 1;
        
        fDocs = getClassInformation(dataPositive,dataUnlabeled);
        yDoc = getClassInformation(dataPositive,dataUnlabeled);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = getClassInformation(dataPositive,dataUnlabeled);
        fUnlabeledDocs = new double[numUnlabeled][numClasses];
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numUnlabeled];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numUnlabeled];
        for(int inst=0;inst<(numTrain+numUnlabeled);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            adjacencyListPDocTerm[inst] = new Neighbor();
        }
        for(int inst=0;inst<numTrain;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataPositive.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataPositive.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
                }    
            }
        }
        for(int inst=0;inst<numUnlabeled;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataUnlabeled.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataUnlabeled.instance(inst).value(term);
                    adjancecyListDocTerm[inst + numTrain].AddNeighbor(indVal);
                }    
            }
        }
        
        //Normalizing doc-term relations
        double[] dDoc = new double[numTrain + numUnlabeled];
        for(int inst=0;inst<(numTrain+numUnlabeled);inst++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                grau += neighbors.get(term).value;
            }
            dDoc[inst] = grau;
        }
        for(int inst=0;inst<(numTrain + numUnlabeled);inst++){
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
        
        //1st Step =========================================================================================================
        //==================================================================================================================
        
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
            for(int inst=0;inst<numUnlabeled;inst++){
                fDocsTemp[numTrain + inst] = classifyInstanceReal(adjacencyListPDocTerm[numTrain + inst].getNeighbors());
            }
            
            //Analysis of stopping criteria
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numUnlabeled;inst++){
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
                for(int inst=0;inst<numUnlabeled;inst++){
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
                meanError = (double)meanError/(double)numUnlabeled;
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
        //setNumIterations(numIt);
        
        //Assingning labels to unlabaled documents
        ArrayList<IndexValue> scores = new ArrayList<IndexValue>();
        for(int inst=0;inst<numUnlabeled;inst++){
            fDocs[numTrain + inst] = classifyInstanceReal(adjancecyListDocTerm[numTrain + inst].getNeighbors());
            scores.add(new IndexValue(numTrain + inst, fDocs[numTrain + inst][0]));
        }
        
        Collections.sort(scores);
        
        fDocs = getClassInformation(dataPositive,dataUnlabeled);
        yDoc = getClassInformation(dataPositive,dataUnlabeled);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = getClassInformation(dataPositive,dataUnlabeled);
        fUnlabeledDocs = new double[numUnlabeled][numClasses];
        
        //Creating ids for Labeled Data
        ArrayList<Integer> idsLabeledData = new ArrayList<Integer>();
        for(int doc=0;doc<dataPositive.numInstances();doc++){
            idsLabeledData.add(doc);
        }
        
        //Creating ids for Unlabeled Data
        ArrayList<Integer> idsUnlabeledData = new ArrayList<Integer>();
        for(int doc=0;doc<dataUnlabeled.numInstances();doc++){
            idsUnlabeledData.add(doc + numTrain);
        }
        
        
        
        for(int i=0; (i<dataPositive.numInstances()) || (scores.size()==0);i++){
            //for(int i=0; (i<this.numAddedLabeledSet*numIterations) || (scores.size()==0);i++){
            int idDoc = scores.get((scores.size()-1)).getIndex();
            idsLabeledData.add(idDoc);
            idsUnlabeledData.remove(idsLabeledData.indexOf(idDoc));
            yDoc[idDoc][0] = 0;
            yDoc[idDoc][1] = 1;
            fDocs[idDoc][0] = 0;
            fDocs[idDoc][1] = 1;
            scores.remove(scores.size()-1);
        }
        
        /*while(scores.get(scores.size() -1).getValue() == 0){
            int idDoc = scores.get((scores.size()-1)).getIndex();
            idsLabeledData.add(idDoc);
            idsUnlabeledData.remove(idsLabeledData.indexOf(idDoc));
            yDoc[idDoc][0] = 0;
            yDoc[idDoc][1] = 1;
            fDocs[idDoc][0] = 0;
            fDocs[idDoc][1] = 1;
            scores.remove(scores.size()-1);
        }*/
        
        // 2nd Step =================================================================================
        // ==========================================================================================
        // Learning Algorithm
        numIt = 0;
        exit = false;
        numIterationsTotal = 0;
        
        
        
        while(exit == false){
            
            boolean exit2 = false;
            int numIterationsInternas = 0;
            //Optimizing class information of terms considering labeled documents
            while(exit2 == false){
                double meanError = 0;
                
                for(int id=0;id<idsLabeledData.size();id++){
                    int inst = idsLabeledData.get(id);
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
            for(int id=0;id<idsUnlabeledData.size();id++){
                int inst = idsUnlabeledData.get(id);
                fDocsTemp[inst] = classifyInstance2(adjacencyListPDocTerm[inst].getNeighbors());
            }
            
            //Analysis of stopping criteria
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numUnlabeled;inst++){
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
                for(int id=0;id<idsUnlabeledData.size();id++){
                    int inst = id;
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                    double[] estimatedClasses = classifyInstanceReal(neighbors);
                    for(int classe=0;classe<numClasses;classe++){
                        double error = fDocs[inst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[inst].getNeighbor(term).value * error);
                            fTerms[neighbors.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                meanError = (double)meanError/(double)numUnlabeled;
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
        
        
        
        //Assingning labels to unlabaled documents
        for(int id=0;id<idsUnlabeledData.size();id++){
            int inst = idsUnlabeledData.get(id);
            fDocs[inst] = classifyInstance(adjancecyListDocTerm[inst].getNeighbors());;
        }
        
        for(int inst=0;inst<numUnlabeled;inst++){
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

    public double[][] getfDocs() {
        return fDocs;
    }

    public void setfDocs(double[][] fDocs) {
        this.fDocs = fDocs;
    }

    public double[][] getfDocsTemp() {
        return fDocsTemp;
    }

    public void setfDocsTemp(double[][] fDocsTemp) {
        this.fDocsTemp = fDocsTemp;
    }

    public double[][] getfTerms() {
        return fTerms;
    }

    public void setfTerms(double[][] fTerms) {
        this.fTerms = fTerms;
    }

    public double[][] getyDoc() {
        return yDoc;
    }

    public void setyDoc(double[][] yDoc) {
        this.yDoc = yDoc;
    }

    public int getNumTrain() {
        return numTrain;
    }

    public void setNumTrain(int numTrain) {
        this.numTrain = numTrain;
    }

    public int getNumUnlabeled() {
        return numUnlabeled;
    }

    public void setNumUnlabeled(int numUnlabeled) {
        this.numUnlabeled = numUnlabeled;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }
    
    
    
    public int getMaxNumberGlobalIterations() {
        return maxNumberGlobalIterations;
    }

    public void setMaxNumberGlobalIterations(int maxNumberGlobalIterations) {
        this.maxNumberGlobalIterations = maxNumberGlobalIterations;
    }

    public int getMaxNumberLocalIterations() {
        return maxNumberLocalIterations;
    }

    public void setMaxNumberLocalIterations(int maxNumberLocalIterations) {
        this.maxNumberLocalIterations = maxNumberLocalIterations;
    }

    public double getErrorCorrectionRate() {
        return errorCorrectionRate;
    }

    public void setErrorCorrectionRate(double errorCorrectionRate) {
        this.errorCorrectionRate = errorCorrectionRate;
    }

    public double getMinError() {
        return minError;
    }

    public void setMinError(double minError) {
        this.minError = minError;
    }
    
    
}
