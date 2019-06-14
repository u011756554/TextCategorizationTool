//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: March 12, 2015
// Description: This is an implementation of IMBHN (Inductive Model based on 
//              Bipartite Heterogeneous Network) algorithm (Rossi et al., 2014;
//              Rossi et al., 2012).
// References: - R. G. Rossi, A. A., T. de Paulo Faleiros, S. O. 
//               Rezende, Inductive model generation for text classiﬁcation 
//               using a bipartite heterogeneous network, Journal of Computer 
//               Science and Technology 3 (29) (2014) 361–375.
//             - R. G. Rossi, de Thiago de Paulo Faleiros, A. de Andrade Lopes, 
//               S. O. Rezende, Inductive model generation for text 
//               categorization using a bipartite heterogeneous network, in: 
//               Proceedings of the International Conference on Data Mining, 
//               IEEE, 2012, pp. 1086–1091.
//******************************************************************************

package TCTAlgorithms.InductiveSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class IMBHN_C_InductiveSupervised extends InductiveSupervisedClassifier {
    
    private double[][] fTerms; // Class information of terms
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberIterations; // Maximum number of iteratios
    private double minError; // Minimum mean squared error
    private double errorCorrectionRate; // Error correction rate
    private int iterations; //Number of iteration performed during inductive supervised learning
    
    // Constructor
    public IMBHN_C_InductiveSupervised(){
        setMaxNumIterations(1000);
        setMinError(0.01);
        setErrorCorrectionRate(0.1);
    }
    
    // Constructor
    public IMBHN_C_InductiveSupervised(int maxNumberIterations, double minError, double errorCorrectionRate){
        setMaxNumIterations(maxNumberIterations);
        setMinError(minError);
        setErrorCorrectionRate(errorCorrectionRate);
    }
    
    
    @Override
    //Function to perform inductive supervised learning
    public void buildClassifier(Instances dataTrain){
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fTerms = new double[numTerms][numClasses];
        
        double[][] fDocs = getFTrain(dataTrain);
        
        int numIt = 0; 
        boolean exit = false;
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
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

        // Learning Algorithm
        while(exit == false){
            double meanError = 0;
            //Optimizing class information of terms considering labeled documents
            for(int inst=0;inst<numTrain;inst++){
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
                double[] estimatedClasses = classifyInstanceHard(neighbors);
                for(int classe=0;classe<numClasses;classe++){
                    double error = fDocs[inst][classe] - estimatedClasses[classe];
                    meanError += (error * error)/(double)2;
                     for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * adjancecyListDocTerm[inst].getNeighbor(term).value * error);
                            fTerms[neighbors.get(term).index][classe] = newWeight;
                    }
                }
            }
            numIt++;
            meanError = (double)meanError/(double)numTrain;
            
            // Analysis of stopping criteria
            if((meanError < getMinError()) || numIt > getMaxNumberIterations()){
                exit = true;
            }
        }
        
        iterations = numIt;
        this.setNumIterations(iterations);
        //this.printWeightTerms("depois.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
        
    }
    
    
    @Override
    // Function to classify an instance through the propagation of class information of terms (hard classification)
    public double classifyInstance(Instance instance){
        double[] classes = new double[numClasses];
        for(int j=0;j<numClasses;j++){
            double acmClassenumTest = 0; 
            for(int k=0;k<numTerms; k++){ 
                if(instance.value(k) >0){
                    acmClassenumTest += (instance.value(k) * fTerms[k][j]);
                }
            }
            classes[j] = acmClassenumTest;
        }

        double max = -300000;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] > max){
                max = classes[classe];
                index = classe;
            }
        }
        return index;
    }
    
    // Perform the class(...) function described in Rossi et al., 2014/2012.        
    public double[] classifyInstanceHard(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        for(int classe=0;classe<numClasses;classe++){
            double acm = 0; 
            for(int term=0;term<neighbors.size();term++){ 
                    acm += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acm;
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
        
        if(index != -1){
            classes[index] = 1;
        }else{
            classes[0] = 1;
        }
        
        return classes;
    }
    
    
    @Override
    // Function to return class confidences of the classification of an instance (soft classification)
    public double[] distributionForInstance(Instance instance){
        double[] distribution = new double[numClasses];
        for(int j=0;j<numClasses;j++){
            double acmClassenumTest = 0; 
            for(int k=0;k<numTerms; k++){ 
                if(instance.value(k) >0){
                    acmClassenumTest += (instance.value(k) * fTerms[k][j]);
                }
            }
            distribution[j] = acmClassenumTest;
        }
        
        double min = Double.MAX_VALUE;
        for(int classe=0;classe<numClasses;classe++){
            if(distribution[classe] < min){
                min = distribution[classe];
            }
        }
        if(min < 0){
            for(int classe=0;classe<numClasses;classe++){
                double value = distribution[classe];
                distribution[classe] = value + Math.abs(min);
            }
        }
        double total = 0;
        for(int classe=0;classe<numClasses;classe++){
            total += distribution[classe];
        }
        for(int classe=0;classe<numClasses;classe++){
            if(total == 0){
                distribution[classe] = 0;
            }else{
                double value = distribution[classe];
                distribution[classe] = value / total;
            }
            
        }
        return distribution;
    }
    
    // Function to return the Y matrix
    private double[][] getFTrain(Instances dataTrain){
        double[][] yDoc = new double[numTrain][numClasses];
        for(int i=0;i<numTrain;i++){
            Instance inst = dataTrain.instance(i);
            int pos = (int)inst.classValue();
            yDoc[i][pos] = 1;
            
        }
        return yDoc;
    }
    
    // Function to print the class information of terms
    private void printfTerms(){
        for(int term=0;term<numTerms;term++){
            System.out.print(term + ": ");
            for(int classe=0;classe<numClasses;classe++){
                System.out.print(fTerms[term][classe] + ",");
            }
            System.out.println();
        }
    }
    
    public void setMaxNumIterations(int maxNumberIterations){
        this.maxNumberIterations = maxNumberIterations;
    }
    
    public void setMinError(double minError){
        this.minError = minError;
    }
    
    public void setErrorCorrectionRate(double errorCorrectionRate){
        this.errorCorrectionRate = errorCorrectionRate;
    }
    
    public int getMaxNumberIterations(){
        return this.maxNumberIterations;
    }
    
    public double getMinError(){
        return this.minError;
    }
    
    public double getErrorCorrectionRate(){
        return this.errorCorrectionRate;
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

