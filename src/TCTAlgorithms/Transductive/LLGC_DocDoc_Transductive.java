//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of LLGC (Learning with Local and 
//              Global Consistency) algorithm.
// References: - D. Zhou, O. Bousquet, T. N. Lal, J. Weston, B. Schölkopf, 
//               Learning with local and global consistency, in: Proceedings of 
//               the Advances in Neural Information Processing Systems, Vol. 16,
//               2004, pp. 321–328.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTNetworkGeneration.DocumentNetworkGeneration;
import TCTStructures.NeighborHash;
import weka.core.Instances;


public class LLGC_DocDoc_Transductive extends TransductiveLearner{
    
    private double[][] f; // Class information of documents
    private double[][] fTemp; // Class information of documents 
    private double[][] y; // Real class information (labels) of labeled documents // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double alpha; // Parameter of the algorithm
    
    
    // Variables to set the type of network
    private boolean gaussian;
    private boolean mutualKnn;
    private boolean cosine;
    private double sigma;
    private int k;
    
    private double[][] matSim; // matrix to store relation weights among documents
    
    // Matrices of the algorithm
    private double[] d;
    private double[][] s;
    
    public LLGC_DocDoc_Transductive(){
        super();
        setAlpha(0);
        setUse(0);
        setSigma(0.15);
        
    }
    
    public LLGC_DocDoc_Transductive(int maxNumberInterations, double alpha, double sigma){
        setMaxNumIterations(maxNumberInterations);
        setAlpha(alpha);
        setUse(0);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        //f = getClassInformationIni();
        f = getClassInformation(dataTrain,dataTest);
        y = getClassInformation(dataTrain,dataTest);
        fTemp = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];

        matSim = new double[numTrain + numTest][numTrain + numTest];
        d = new double[numTrain + numTest];
        s = new double[numTrain + numTest][numTrain + numTest];

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
                    //w = GerarNetworks.gerarNetworkKnnAssimetrica(dataTrain, dataTest, k);
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
                    //w = GerarNetworks.gerarNetworkKnnAssimetrica(dataTrain, dataTest, k);
                    matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkEuclidean(dataTrain, dataTest, k);
                }
            }    
        }

        for(int inst1=0;inst1<(numTrain+numTest);inst1++){
            double valueD = 0;
            for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                valueD += matSim[inst1][inst2];
            }
            d[inst1] = valueD;
        }

        for(int inst1=0;inst1<(numTrain+numTest);inst1++){
            for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                if((d[inst1] == 0)  || (d[inst2] == 0)){
                    s[inst1][inst2] = 0;
                }else{
                    s[inst1][inst2] = matSim[inst1][inst2] / (Math.sqrt(d[inst1]) * Math.sqrt(d[inst2]));
                }
            }
        }
        d=null;
        matSim=null;
        

        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // Propagatin labels among documents
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int classe=0;classe<numClasses;classe++){
                    double fTemp1=0;
                    for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                        fTemp1 += alpha * s[inst1][inst2] * f[inst2][classe];
                    }
                    fTemp[inst1][classe] = fTemp1  + (1 - alpha)*y[inst1][classe];
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

            // Analysis of stopping criteria
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
                
            numIt++;
            setNumIterations(numIt);

        }
        
        // Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            int ind = -1;
            double maior = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(f[inst + numTrain][classe] > maior){
                    ind = classe;
                    maior = f[inst + numTrain][classe];
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
    
    public double getSigma(){
        return sigma;
    }
    
    public int getK(){
        return k;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public boolean getGaussian(){
        return gaussian;
    }
    
    public boolean getMutualKnn(){
        return mutualKnn;
    }
    
    public boolean getCosine(){
        return cosine;
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setSigma(double sigma){
        this.sigma = sigma;
    }
    
    public void setK(int k){
        this.k = k;
    }
    
    public void setGaussian(boolean gaussian){
        this.gaussian = gaussian;
    }
    
    public void setMutualKnn(boolean mutualKnn){
        this.mutualKnn = mutualKnn;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
}
