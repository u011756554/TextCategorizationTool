//**********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 28, 2015
// Description: This is an implementation of Expectation Maximization algorithm
//              (Nigam et al., 2000) to semi-supervised classification of texts.   
// References: - K. Nigam, A. K. McCallum, S. Thrun, T. Mitchell, Text classification 
//               from labeled and unlabeled documents using em, Machine Learning
//               39 (2/3) (2000) 103–134.
//**********************************************************************************

package TCTAlgorithms.InductiveSemiSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class ExpectationMaximizationMultipleComponents_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
    private double[][] fDocs; // Class information of documents 
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms 
    private double[][] yDoc; // Real class information (labels) of labeled documents 
    private int numLabeled; // Number of labeled documents
    private int numUnlabeled; // Number of unlabeled documents
    private int numClasses; // Number of classes 
    private int numTerms; // Number of terms 
    private int maxNumberInterations; // Maximum number of iterations 
    private double minLogLikelihood; // Minimum Log-Likelihood value
    private double weightUnlabeled; // Weight of unbaled documents to set the class information of terms
    private int numCompClass; // Number of components for each class
    
    //Constructor
    public ExpectationMaximizationMultipleComponents_InductiveSemiSupervised(){
        super();
        setMaxNumIterations(1000);
        setMinLogLikelihood(0.01);
        setNumCompClass(1);
    }
    
    //Function to perform Expectation Maximization
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numLabeled = dataTrain.numInstances();
        this.numUnlabeled = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses*numCompClass];
        fDocsTemp = getClassInformation(dataTrain,dataTest);
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numLabeled + numUnlabeled]; //Stores the terms contained in documents
        Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms]; //Store the document in which the terms occur
        for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
        }
        for(int term=0;term<numTerms;term++){
            adjacencyListTermDoc[term] = new Neighbor();
        }
        
        for(int inst=0;inst<numLabeled;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTrain.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTrain.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = inst;
                    indVal.value = dataTrain.instance(inst).value(term);
                    adjacencyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        for(int inst=0;inst<numUnlabeled;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTest.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTest.instance(inst).value(term);
                    adjancecyListDocTerm[inst + numLabeled].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = numLabeled + inst;
                    indVal.value = dataTest.instance(inst).value(term);
                    adjacencyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        
        //Estimating parameters only with labeled documents
        double[] denominators = new double[numClasses * numCompClass];
        for(int classe=0;classe<(numClasses * numCompClass);classe++){
            double denominator = numTerms;
            for(int term=0;term<numTerms;term++){
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    denominator += neighbors.get(inst).value * yDoc[neighbors.get(inst).index][classe];
                }
            }
            denominators[classe] = denominator;
        }
        
        for(int term=0;term<numTerms;term++){
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
            for(int classe=0;classe<(numClasses* numCompClass);classe++){
                double numerator = 1;
                for(int inst=0;inst<neighbors.size();inst++){
                    numerator += (double)neighbors.get(inst).value * (double)yDoc[neighbors.get(inst).index][classe];
                }
                fTerms[term][classe] = numerator / denominators[classe];
            }
        }
        
        double[] priors = new double[numClasses * numCompClass];
        for(int classe=0;classe<(numClasses * numCompClass);classe++){
            double numerator = 1;
            for(int inst=0;inst<numLabeled;inst++){
                numerator += yDoc[inst][classe];
            }
            priors[classe] = (double)numerator / (double)(numClasses + numLabeled);
        }
        
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        int numIterations = 0;
        while(exit == false){
            //M-Step
            //Labeled Documents
            for(int inst=0;inst<numLabeled;inst++){
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
                double denominator = 0;
                int classeInst = (int)dataTrain.instance(inst).classValue();
                for(int class2=0;class2<numCompClass;class2++){
                    int indexClasse = (classeInst*numCompClass) + class2;
                    double acm = 1;
                    for(int term=0;term<neighbors.size();term++){
                        acm *= fTerms[neighbors.get(term).index][indexClasse];
                    }
                    denominator += priors[indexClasse] * acm;
                }
                    
                double numerator = 1;
                for(int class2=0;class2<numCompClass;class2++){
                    int indexClasse = (classeInst*numCompClass) + class2;
                    for(int term=0;term<neighbors.size();term++){
                        numerator *= fTerms[neighbors.get(term).index][indexClasse];
                    }
                    if(denominator != 0){
                        fDocsTemp[inst][indexClasse] = (priors[indexClasse] * numerator) / denominator;
                    }else{
                        fDocsTemp[inst][indexClasse] = 0;
                    }    
                }
                
            }
            //Normalizing fDocTemp
            for(int inst=0;inst<numLabeled;inst++){
                double sum=0;
                for(int classe=0;classe<numClasses*numCompClass;classe++){
                        sum += fDocsTemp[inst][classe];
                }
                for(int classe=0;classe<numClasses*numCompClass;classe++){
                    if(sum == 0){
                        fDocsTemp[inst][classe] = 0;
                    }else{
                        fDocsTemp[inst][classe] = fDocsTemp[inst][classe] / sum;
                    }
                }
            }
            // Unlabeled documents
            for(int inst=0;inst<(numUnlabeled);inst++){
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[numLabeled + inst].getNeighbors();
                double denominator = 0;
                for(int classe=0;classe<(numClasses * numCompClass);classe++){
                    double acm = 1;
                    for(int term=0;term<neighbors.size();term++){
                        acm *= fTerms[neighbors.get(term).index][classe];
                    }
                    denominator += priors[classe] * acm;
                }
                
                for(int classe=0;classe<(numClasses * numCompClass);classe++){
                    double numerator = 1;
                    for(int term=0;term<neighbors.size();term++){
                        numerator *= fTerms[neighbors.get(term).index][classe];
                    }
                    if(denominator != 0){
                        fDocsTemp[numLabeled + inst][classe] = (priors[classe] * numerator) / denominator;
                    }else{
                        fDocsTemp[numLabeled + inst][classe] = 0;
                    }
                }
            }
            
            for(int classe=0;classe<(numClasses * numCompClass);classe++){
                double denominator = 0;
                for(int term=0;term<numTerms;term++){
                    ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                    for(int inst=0;inst<neighbors.size();inst++){
                        if(neighbors.get(inst).index >= numLabeled){
                            denominator += weightUnlabeled * neighbors.get(inst).value * fDocs[neighbors.get(inst).index][classe];
                        }else{
                            denominator += neighbors.get(inst).value * fDocs[neighbors.get(inst).index][classe];
                        }
                        
                    }
                }
                denominator += numTerms;
                denominators[classe] = denominator;
            }
            
            //E-Step - Computing class information of terms (model parameters)
            for(int term=0;term<numTerms;term++){
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                for(int classe=0;classe<(numClasses * numCompClass);classe++){
                    double numerator = 1;
                    for(int inst=0;inst<neighbors.size();inst++){
                        if(neighbors.get(inst).index >= numLabeled){
                            numerator += weightUnlabeled * (double)neighbors.get(inst).value * (double)fDocs[neighbors.get(inst).index][classe];
                        }else{
                            numerator += (double)neighbors.get(inst).value * (double)fDocs[neighbors.get(inst).index][classe];
                        }
                        
                    }
                    fTerms[term][classe] = numerator / denominators[classe];
                }
            }            
            
            //Class priors considering labeled and unlabeled documents
            for(int classe=0;classe<(numClasses * numCompClass);classe++){
                double numerator = 1;
                for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
                    if(inst >= numLabeled){
                        numerator += weightUnlabeled * fDocsTemp[inst][classe];
                    }else{
                        numerator += fDocsTemp[inst][classe];
                    }
                    
                }
                priors[classe] = (double)numerator / (double)(numTerms + numLabeled + weightUnlabeled * numUnlabeled);
            }
            
            numIterations++;
            
            double acmDif = 0;
            for(int inst=0;inst<numLabeled+numUnlabeled;inst++){
                for(int classe=0;classe<(numClasses * numCompClass);classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
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
            
            //Analysis of stopping criteria
            if((getMaxNumberIterations() == numIterations) || (acmDif <= getMinLogLikelihood())){
                exit = true;
            }
        }
    }
    
    //Function to classify unlabeled instances
    public double classifyInstance(Instance instance){
        double valueClasse = -1;
        double[] distClasses = distributionForInstance(instance);
        double maior = -3000000;
        for(int classe=0;classe<numClasses;classe++){
            if(distClasses[classe] > maior){
                 maior = distClasses[classe];
                 valueClasse = classe;
            }
        }
        return valueClasse;
    }
    
    //Function to return the classification confidences for each class
    public double[] distributionForInstance(Instance instance){
        double[] distClasses = new double[numClasses];
        for(int class1=0;class1<numClasses;class1++){
            double score = 0;
            for(int class2=0;class2<numCompClass;class2++){
                for(int term=0;term<numTerms;term++){
                    score += instance.value(term) *  fTerms[term][(class1 * numCompClass) + class2];
                }
            }
            distClasses[class1] = score;
        }
        
        double sum = 0;
        for(int classe=0;classe<numClasses;classe++){
            sum += distClasses[classe];
        }
        if(sum != 0){
            for(int classe=0;classe<numClasses;classe++){
                distClasses[classe] = distClasses[classe] / sum;
            }    
        }
        
        return distClasses;
    }
    
    public double getWeightUnlabeledDocs(){
        return weightUnlabeled;
    }
    
    public void setWeightUnlabeledDocs(double weightUnlabeled){
        this.weightUnlabeled = weightUnlabeled;
    }
    
    public int getMaxNumberIterations(){
        return this.maxNumberInterations;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public double getMinLogLikelihood(){
        return this.minLogLikelihood;
    }
    
    public void setMinLogLikelihood(double minLogLikelihood){
        this.minLogLikelihood = minLogLikelihood;
    }
    
    public int getNumCompClass(){
        return this.numCompClass;
    }
    
    public void setNumCompClass(int numCompClass){
        this.numCompClass = numCompClass;
    }
    
    //Function to initialize multiple components per class
    public double[][] getClassInformation(Instances dataTrain, Instances dataTest){
        int numLabeled = dataTrain.numInstances();
        int numUnlabeled = dataTest.numInstances();
        int numClasses = dataTrain.numClasses();
        double[][] dist = new double[numLabeled + numUnlabeled][numClasses * numCompClass];
        int[] countClasses = new int[numClasses];
        for(int inst=0;inst<numLabeled;inst++){
            Instance instance = dataTrain.instance(inst);
            int pos = (int)instance.classValue();
            dist[inst][(pos * numCompClass) + countClasses[pos]] = 1;
            countClasses[pos] = countClasses[pos] + 1;
            if(countClasses[pos] == numCompClass){
               countClasses[pos] = 0;
            }
        }
        double value = 0;
        for(int inst=0;inst<numUnlabeled;inst++){
            for(int classe=0;classe<(numClasses * numCompClass);classe++){
                dist[inst + numLabeled][classe] = value;
            }
        }
        return dist;
    }
    
}
