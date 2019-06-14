/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervised;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class IterativeRidgeRegression extends Classifier{

    private double[][] fTerms; // Class information of terms
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberIterations; // Maximum number of iteratios
    double lambda;
    double minDifference;

    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fTerms = new double[numTerms][numClasses];
        double[][] rDocs = new double[numTrain][numClasses];
        double[][] yDocs = getYTrain(dataTrain);
        
        int numIt = 0;
        boolean exit = false;
        
        double[] grau = new double[numTrain];
        for(int doc=0;doc<numTrain;doc++){
            for(int atr=0;atr<numTerms;atr++){
                grau[doc] += dataTrain.instance(doc).value(atr);
            }
        }
        
        for(int doc=0;doc<numTrain;doc++){
            for(int atr=0;atr<numTerms;atr++){
                dataTrain.instance(doc).setValue(atr, dataTrain.instance(doc).value(atr)/grau[doc]);
            }
        }
        
        while(exit == false){
            double acmDiff = 0;
            for(int classe=0;classe<numClasses;classe++){
                for(int term=0;term<numTerms;term++){
                    double numerator = 0;
                    double denominator = 0;
                    for(int doc=0;doc<numTrain;doc++){
                        numerator += (rDocs[doc][classe] - 1) * dataTrain.instance(doc).value(term) * yDocs[doc][classe] + (lambda * numTrain * fTerms[term][classe]);
                        //numerator += (rDocs[doc][classe] - 1) * dataTrain.instance(doc).value(term) * yDocs[doc][classe];
                        denominator += dataTrain.instance(doc).value(term) * dataTrain.instance(doc).value(term) + lambda * numTrain;
                        //denominator += dataTrain.instance(doc).value(term) * dataTrain.instance(doc).value(term);
                    }
                    //numerator += (lambda * numTrain * fTerms[term][classe]);
                    //denominator +=  lambda * numTrain;
                    double deltaW =  (-1) * numerator / denominator;
                    for(int doc=0;doc<numTrain;doc++){
                        rDocs[doc][classe] = rDocs[doc][classe] + deltaW + dataTrain.instance(doc).value(term) * yDocs[doc][classe];
                    }
                    double oldValue = fTerms[term][classe];
                    fTerms[term][classe] = oldValue + deltaW;
                    acmDiff += Math.abs(oldValue - fTerms[term][classe]);
                }
            }
            
            System.out.println("Iteration " + numIt + " - " + acmDiff);
            
            numIt++;
            if(numIt == this.getMaxNumberIterations()){
                exit = true;
            }
            if(acmDiff < this.getMinDifference()){
                exit = true;
            }
        }
        
        
    }
    
    @Override
    public double classifyInstance(Instance instance){
    
        double[] confidences = distributionForInstance(instance);
        
        double max = -300000;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(confidences[classe] > max){
                max = confidences[classe];
                index = classe;
            }
        }
        return index;
        
    }
    
    @Override
    public double[] distributionForInstance(Instance instance){
        double[] confidences = new double[numClasses];
        
        for(int classe=0;classe<numClasses;classe++){
            double confidence = 0;
            for(int term=0;term<numTerms;term++){
                confidence += instance.value(term) * fTerms[term][classe];
            }    
        }
        
        return confidences;
    }
    
    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getMinDifference() {
        return minDifference;
    }

    public void setMinDifference(double minDifference) {
        this.minDifference = minDifference;
    }
    
    public double[][] getfTerms() {
        return fTerms;
    }

    public void setfTerms(double[][] fTerms) {
        this.fTerms = fTerms;
    }

    public int getNumTrain() {
        return numTrain;
    }

    public void setNumTrain(int numTrain) {
        this.numTrain = numTrain;
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

    public int getMaxNumberIterations() {
        return maxNumberIterations;
    }

    public void setMaxNumberIterations(int maxNumberIterations) {
        this.maxNumberIterations = maxNumberIterations;
    }
    
    // Function to return the Y matrix
    private double[][] getYTrain(Instances dataTrain){
        double[][] yDoc = new double[numTrain][numClasses];
        for(int i=0;i<numTrain;i++){
            Instance inst = dataTrain.instance(i);
            int pos = (int)inst.classValue();
            //yDoc[i][pos] = 1;
            for(int classe=0;classe<numClasses;classe++){
                if(classe == pos){
                    yDoc[i][classe] = 1;
                }else{
                    yDoc[i][classe] = -1;
                }
            }
        }
        return yDoc;
    }
}
