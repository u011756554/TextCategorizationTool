//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This class contains the variables and functions shared 
//              by different implementations of GFHF (Gaussian Field and 
//              Harmonic Function) algorithm (Zhu et al., 2003). 
//              (This class is not complete yet)
// References: - X. Zhu, Z. Ghahramani, J. Lafferty, Semi-supervised learning 
//               using gaussian fields and harmonic functions, in: Proceedings 
//               of the International Conference on Machine Learning, AAAI 
//               Press, 2003, pp. 912–919.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTNetworkGeneration.DocumentNetworkGeneration;
import TCTStructures.NeighborHash;
import java.util.HashMap;
import weka.core.Instances;

public class GFHF_DocDoc_Transductive extends TransductiveLearner{
    
    private double[][] f; // Class information of documents
    private double[][] fTemp; // Class information of documents
    private double[][] y; // Real class information (labels) of labeled documents 
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double sigma; // bandwidth of a gaussin function (Exp network)
    private int k; // number of neighors (Mutual Knn network)
    
    // Variables to set the type of network
    private boolean gaussian;
    private boolean mutualKnn;
    private boolean cosine;
    
    //Matrices of the algorithm
    private double[][] matSim;
    private double[][] p;
    
    // Constructor
    public GFHF_DocDoc_Transductive(){
        super();
        setUse(0);
        setSigma(0.15);
    }
        
    // Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        f = getClassInformation(dataTrain,dataTest);
        y = getClassInformation(dataTrain,dataTest);
        fTemp = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        
        if(getUse() == 0){
            
            matSim = new double[numTrain + numTest][numTrain + numTest];
            p = new double[numTrain + numTest][numTrain + numTest];
            
            //Creating adjacency lists to speed up computation of similarities among documents
            if(getCosine()){
                NeighborHash[] adjancecyListDocTerm = new NeighborHash[numTrain + numTest];
                NeighborHash[] adjacencyListTermDoc = new NeighborHash[numTerms];
                for(int inst=0;inst<(numTrain + numTest);inst++){
                    adjancecyListDocTerm[inst] = new NeighborHash();
                }
                for(int term=0;term<numTerms;term++){
                    adjacencyListTermDoc[term] = new NeighborHash();
                }

                for(int inst=0;inst<numTrain;inst++){
                    for(int term=0;term<numTerms;term++){
                        if(dataTrain.instance(inst).value(term) > 0){
                            adjancecyListDocTerm[inst].AddNeighbor(term,dataTrain.instance(inst).value(term));
                            adjacencyListTermDoc[term].AddNeighbor(inst,dataTrain.instance(inst).value(term));
                        }    
                    }
                }
                for(int inst=0;inst<numTest;inst++){
                    for(int term=0;term<numTerms;term++){
                        if(dataTest.instance(inst).value(term) > 0){
                            adjancecyListDocTerm[inst + numTrain].AddNeighbor(term,dataTest.instance(inst).value(term));
                            adjacencyListTermDoc[term].AddNeighbor(numTrain + inst,dataTest.instance(inst).value(term));
                        }    
                    }
                }
                if(getGaussian() == true){
                    matSim = DocumentNetworkGeneration.GenerateExpNetworkCosine(adjancecyListDocTerm, sigma);
                }else{
                    if(getMutualKnn() == true){
                        matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkCosine(adjancecyListDocTerm, k);
                    }
                }
                adjancecyListDocTerm = null;
                adjacencyListTermDoc = null;
            }else{
                if(getGaussian() == true){
                    matSim = DocumentNetworkGeneration.GenerateGaussianNetworkEuclidean(dataTrain, dataTest, sigma);
                }else{
                    if(getMutualKnn() == true){
                        matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkEuclidean(dataTrain, dataTest, k);
                    }
                }    
            }
            
            double[] d = new double[numTrain + numTest];
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                double grau = 0;
                for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                    grau += matSim[inst1][inst2];
                }
                d[inst1] = grau;
            }
            
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                    if((matSim[inst1][inst2] == 0) || (d[inst1] == 0)){
                        p[inst1][inst2] = 0;
                    }else{
                        p[inst1][inst2] = matSim[inst1][inst2] / d[inst1];
                    }
                }
            }
            matSim = null;
            
        }
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // 1st step: f = pf
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                        value += p[inst1][inst2] * f[inst2][classe];
                    }
                    fTemp[inst1][classe] = value;
                }
            }
            
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fTemp[inst][classe] - f[inst][classe]);
                    f[inst][classe] = fTemp[inst][classe];
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

            //Stopping crite verification
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
            
            numIt++;
            setNumIterations(numIt);
            
            //2nd step f_{L} = y_{L}
            for(int inst=0;inst<numTrain;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    f[inst][classe] = y[inst][classe];
                }
            }
        }
        
        //Assigning labels to unlabeled documents considering class mass normalization
        HashMap<Integer,Double> sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
                acmProbClasse += f[inst][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        
        for(int inst=0;inst<numTest;inst++){
            double[] classeTemp = new double[numClasses];
            for(int classe=0;classe<numClasses;classe++){
                classeTemp[classe] = (double)f[numTrain + inst][classe]/(double)sumClassInformationDocs.get(classe);
            }
            int ind = -1;
            double maior = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(classeTemp[classe] > maior){
                    ind = classe;
                    maior = classeTemp[classe];
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
    
    public double getSigma(){
        return sigma;
    }
    
    public boolean getGaussian(){
        return gaussian;
    }
    
    public boolean getMutualKnn(){
        return mutualKnn;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public boolean getCosine(){
        return cosine;
    }
    
    public void setSigma(double sigma){
        this.sigma = sigma;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setGaussian(boolean gaussian){
        this.gaussian = gaussian;
    }
    
    public void setMutualKnn(boolean mutualKnn){
        this.mutualKnn = mutualKnn;
    }
    
    public void setK(int k){
        this.k = k;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
}
