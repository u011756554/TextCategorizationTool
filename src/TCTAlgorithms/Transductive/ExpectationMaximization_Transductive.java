//**********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 28, 2015
// Description: This is an implementation of Expectation Maximization Algorithm
//              (Nigam et al., 2000)to perform transductive classification of texts.   
// References: - K. Nigam, A. K. McCallum, S. Thrun, T. Mitchell, Text classification 
//               from labeled and unlabeled documents using em, Machine Learning
//               39 (2/3) (2000) 103–134.
//**********************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class ExpectationMaximization_Transductive extends TransductiveLearner{
    
    private double[][] fDocs; // Class information of documents 
    private double[][] fDocsTemp; // Class information of documents 
    private double[][] fTerms; // Class information of terms 
    private double[][] yDoc; // Real class information (labels) of labeled documents 
    private int numTrain; // Number of labeled documents 
    private int numTest; // Number of unlabeled documents 
    private int numClasses; // Number of classes 
    private int numTerms; // Number of terms 
    private int maxNumberInterations; // Maximum number of iterations 
    private double minLogLikelihood; // Minimum Log-Likelihood value
    private double weightUnlabeled; // Weight of unbaled documents to set the class information of terms
    private int numCompClass; // Number of components for each class
    
    //Constructor
    public ExpectationMaximization_Transductive(){
        super();
        setMaxNumIterations(1000);
        setMinLogLikelihood(0.01);
        setNumCompClass(1);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses*numCompClass];
        fDocsTemp = getClassInformation(dataTrain,dataTest);
        fUnlabeledDocs = new double[numTest][numClasses];
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
        Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
        for(int inst=0;inst<(numTrain + numTest);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
        }
        for(int term=0;term<numTerms;term++){
            adjacencyListTermDoc[term] = new Neighbor();
        }
        
        for(int inst=0;inst<numTrain;inst++){
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
        for(int inst=0;inst<numTest;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTest.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTest.instance(inst).value(term);
                    adjancecyListDocTerm[inst + numTrain].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = numTrain + inst;
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
            for(int inst=0;inst<numTrain;inst++){
                numerator += yDoc[inst][classe];
            }
            priors[classe] = (double)numerator / (double)(numClasses + numTrain);
        }
        
        //Expectation Maximization
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        int numIterations = 0;
        while(exit == false){
            //M-Step
            for(int inst=0;inst<numTrain;inst++){
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
            //Normalizing FDocTemp
            for(int inst=0;inst<numTrain;inst++){
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
            
            //Test Documents
            for(int inst=0;inst<(numTest);inst++){
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[numTrain + inst].getNeighbors();
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
                        fDocsTemp[numTrain + inst][classe] = (priors[classe] * numerator) / denominator;
                    }else{
                        fDocsTemp[numTrain + inst][classe] = 0;
                    }
                }
            }
            
            for(int classe=0;classe<(numClasses * numCompClass);classe++){
                double denominator = 0;
                for(int term=0;term<numTerms;term++){
                    ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                    for(int inst=0;inst<neighbors.size();inst++){
                        if(neighbors.get(inst).index >= numTrain){
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
                        if(neighbors.get(inst).index >= numTrain){
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
                for(int inst=0;inst<(numTrain + numTest);inst++){
                    if(inst >= numTrain){
                        numerator += weightUnlabeled * fDocsTemp[inst][classe];
                    }else{
                        numerator += fDocsTemp[inst][classe];
                    }
                    
                }
                priors[classe] = (double)numerator / (double)(numTerms + numTrain + weightUnlabeled * numTest);
            }
            
            numIterations++;
            
            double acmDif = 0;
            for(int inst=0;inst<numTrain;inst++){
                for(int classe=0;classe<(numClasses * numCompClass);classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                }    
            }
            for(int inst=numTrain;inst<numTrain+numTest;inst++){
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
        
        //Assigning labels to unlabeled documents
        //System.out.println("Atribuindo as classes as instancias de teste");
        for(int inst=0;inst<numTest;inst++){
            int ind = -1;
            double maior = -300000;
            double[] classTemp = new double[numClasses];
            
            for(int class1=0;class1<numClasses;class1++){
                double acm = 0;
                for(int class2=0;class2<numCompClass;class2++){
                    acm += fDocs[inst + numTrain][(class1 * numCompClass) + class2];
                }
                classTemp[class1] = acm;
            }
            
            for(int classe=0;classe<numClasses;classe++){
                if(classTemp[classe] > maior){
                    ind = classe;
                    maior = classTemp[classe];
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
    
    public double getWeightUnlabeled(){
        return weightUnlabeled;
    }
    
    public void setWeightUnlabeled(double weightUnlabeled){
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
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        int numClasses = dataTrain.numClasses();
        double[][] dist = new double[numTrain + numTest][numClasses * numCompClass];
        int[] countClasses = new int[numClasses];
        for(int inst=0;inst<numTrain;inst++){
            Instance instance = dataTrain.instance(inst);
            int pos = (int)instance.classValue();
            dist[inst][(pos * numCompClass) + countClasses[pos]] = 1;
            countClasses[pos] = countClasses[pos] + 1;
            if(countClasses[pos] == numCompClass){
               countClasses[pos] = 0;
            }
        }
        double value = 0;
        for(int inst=0;inst<numTest;inst++){
            for(int classe=0;classe<(numClasses * numCompClass);classe++){
                dist[inst + numTrain][classe] = value;
            }
        }
        return dist;
    }
    
}
