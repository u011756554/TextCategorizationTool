//******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of IMBHN (Inductive Model based on 
//              Bipartite Heterogeneous Network) which induces a classification
//              model considering document-term and term-term relations. We 
//              used the class(...) funcion as in (Rossi et al., 2014;
//              Rossi et al., 2012) to induce the relevance score of terms.
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

public class IMBHN_R_InductiveSupervised extends InductiveSupervisedClassifier {
    
    private double[][] fDocs; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberIterations; // Maximum number of iterations
    private double errorCorrectionRate; // Error correction error
    private double minError; // Minimum Mean Squared Error
    
    // Constructor
    public IMBHN_R_InductiveSupervised(){
        super();
        setMaxNumIterations(1000);
        setMinError(0.01);
        setErrorCorrectionRate(0.1);
    }
    
    // Constructor
    public IMBHN_R_InductiveSupervised(int maxNumberIterations, double minError, double errorCorrectionRate, String weight){
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
        
        yDoc = getRealClass(dataTrain);
        fTerms = new double[numTerms][numClasses];
        fDocs = new double [numTrain][numClasses];
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain];
        for(int inst=0;inst<numTrain;inst++){
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
        
        //Normalizing doc-term relations
        double[] dDoc = new double[numTrain];
        for(int inst=0;inst<(numTrain);inst++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                grau += neighbors.get(term).value;
            }
            dDoc[inst] = grau;
        }
        for(int inst=0;inst<numTrain;inst++){
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
        while(exit == false){
            double meanError = 0;
            //Optimizing class information of terms considering labeled documents
            for(int inst=0;inst<numTrain;inst++){
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                double[] estimatedClasses = classifyInstance2(neighbors);
                for(int classe=0;classe<numClasses;classe++){
                    double error = yDoc[inst][classe] - estimatedClasses[classe];
                    meanError += (error * error)/(double)2;
                     for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[inst].getNeighbor(term).value * error);
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
        
        this.setNumIterations(numIt);
        //System.out.println("NumIt: " + numIt);
        //this.printWeightTerms("depois.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
        //System.exit(0);
        
    }

    @Override
    // Function to classify an instance through the propagation of class information of terms (hard classification)
    public double classifyInstance(Instance instance){
        
        double[] classes = new double[numClasses];
        for(int j=0;j<numClasses;j++){
            double acm = 0; 
            for(int k=0;k<numTerms; k++){ 
                if(instance.value(k) >0){
                    acm += (instance.value(k) * fTerms[k][j]);
                }
            }
            classes[j] = acm;
        }

        double max = Double.MIN_VALUE * -1;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] > max){
                max = classes[classe];
                index = classe;
            }
        }
        return index;
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
            double acmPesoClasse = 0; 
            for(int term=0;term<neighbors.size();term++){ 
                    acmPesoClasse += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmPesoClasse;
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
    
    //Returns the value 1 for the possition corresponding to the class and 0 otherwise.
    private static double[][] getRealClass(Instances dataTrain){
        double[][] distProbTrain = new double[dataTrain.numInstances()][dataTrain.numClasses()];
        for(int i=0;i<dataTrain.numInstances();i++){
            Instance inst = dataTrain.instance(i);
            int pos = (int)inst.classValue();
            distProbTrain[i][pos] = 1;
        }
        return distProbTrain;
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
            System.err.println("Error when saving information class of terms for classes.");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
}

