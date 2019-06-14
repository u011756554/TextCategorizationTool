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

package TCTAlgorithms.InductiveSemiSupervised;

import TCTNetworkGeneration.DocumentNetworkGeneration;
import static TCTNetworkGeneration.Proximity.computeCosine;
import static TCTNetworkGeneration.Proximity.computeCosineExp;
import static TCTNetworkGeneration.Proximity.computeVoteEuclidean;
import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import TCTStructures.NeighborHash;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instance;
import weka.core.Instances;

public class GFHF_DocDoc_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
    private double[][] f; // Class information of documents
    private double[][] fTemp; // Class information of documents
    private double[][] y; // Real class information (labels) of labeled documents 
    private int numLabeled; // Number of labeled documents
    private int numUnlabeled; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double sigma; // bandwidth of a gaussin function (Exp network)
    private int k; // number of neighors (Mutual Knn network)
    
    // Variables to set the type of network
    private boolean gaussian;
    private boolean mutualKnn;
    private boolean cosine;
    
    NeighborHash[] adjancecyListDocTerm;
    
    HashMap<Integer,Double> sumClassInformationDocs;
    
    //Matrices of the algorithm
    private double[][] matSim;
    private double[][] p;
    Neighbor[] adjacencyListP;
    
    // Constructor
    public GFHF_DocDoc_InductiveSemiSupervised(){
        super();
        setUse(0);
        setSigma(0.15);
    }
        
    // Function to perform transductive learning
    public void buildClassifier(Instances dataLabeled, Instances dataUnlabeled){
        this.numLabeled = dataLabeled.numInstances();
        this.numUnlabeled = dataUnlabeled.numInstances();
        this.numClasses = dataLabeled.numClasses();
        this.numTerms = dataLabeled.numAttributes() - 1;
        
        f = getClassInformation(dataLabeled,dataUnlabeled);
        y = getClassInformation(dataLabeled,dataUnlabeled);
        fTemp = new double[numLabeled + numUnlabeled][numClasses];
        
        matSim = new double[numLabeled + numUnlabeled][numLabeled + numUnlabeled];
        p = new double[numLabeled + numUnlabeled][numLabeled + numUnlabeled];

        //Creating adjacency lists to speed up computation of similarities among documents
        if(getCosine()){
            adjancecyListDocTerm = new NeighborHash[numLabeled + numUnlabeled];
            NeighborHash[] adjacencyListTermDoc = new NeighborHash[numTerms];
            for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
                adjancecyListDocTerm[inst] = new NeighborHash();
            }
            for(int term=0;term<numTerms;term++){
                adjacencyListTermDoc[term] = new NeighborHash();
            }

            for(int inst=0;inst<numLabeled;inst++){
                for(int term=0;term<numTerms;term++){
                    if(dataLabeled.instance(inst).value(term) > 0){
                        adjancecyListDocTerm[inst].AddNeighbor(term,dataLabeled.instance(inst).value(term));
                        adjacencyListTermDoc[term].AddNeighbor(inst,dataLabeled.instance(inst).value(term));
                    }    
                }
            }
            for(int inst=0;inst<numUnlabeled;inst++){
                for(int term=0;term<numTerms;term++){
                    if(dataUnlabeled.instance(inst).value(term) > 0){
                        adjancecyListDocTerm[inst + numLabeled].AddNeighbor(term,dataUnlabeled.instance(inst).value(term));
                        adjacencyListTermDoc[term].AddNeighbor(numLabeled + inst,dataUnlabeled.instance(inst).value(term));
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
            adjacencyListTermDoc = null;
        }else{
            if(getGaussian() == true){
                matSim = DocumentNetworkGeneration.GenerateGaussianNetworkEuclidean(dataLabeled, dataUnlabeled, sigma);
            }else{
                if(getMutualKnn() == true){
                    matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkEuclidean(dataLabeled, dataUnlabeled, k);
                }
            }    
        }

        double[] d = new double[numLabeled + numUnlabeled];
        for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
            double grau = 0;
            for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                grau += matSim[inst1][inst2];
            }
            d[inst1] = grau;
        }

        if(mutualKnn == true){
            adjacencyListP = new Neighbor[numLabeled+numUnlabeled];
            for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                adjacencyListP[inst] = new Neighbor();
            }
            for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                    if((matSim[inst1][inst2] != 0) && (d[inst1] != 0)){
                        double p = matSim[inst1][inst2] / d[inst1];
                        IndexValue indVal = new IndexValue();
                        indVal.index = inst2;
                        indVal.value = p;
                        adjacencyListP[inst1].AddNeighbor(indVal);
                    }
                }
            } 
        }else{
            for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                    if((matSim[inst1][inst2] == 0) || (d[inst1] == 0)){
                        p[inst1][inst2] = 0;
                    }else{
                        p[inst1][inst2] = matSim[inst1][inst2] / d[inst1];
                    }
                }
            }    
        }
        matSim = null;
        
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        
        if(mutualKnn == true){
            System.out.println("Using Adjacency List!");
            while(exit == false){
                // Step 1: f = pf
                // Propagating labels among documents
                for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                    for(int classe=0;classe<numClasses;classe++){
                        double value = 0;
                        ArrayList<IndexValue> neighbors = adjacencyListP[inst1].getNeighbors();
                        for(int inst2=0;inst2<neighbors.size();inst2++){
                            value += neighbors.get(inst2).value * f[neighbors.get(inst2).index][classe];
                        }
                        fTemp[inst1][classe] = value;
                    }
                }

                double acmDif = 0;
                for(int inst=0;inst<numLabeled+numUnlabeled;inst++){
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

                //Analysis of stopping criteria
                if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                    exit = true;
                }

                numIt++;
                setNumIterations(numIt);

                //Step 2: f_{L} = y_{L}
                for(int inst=0;inst<numLabeled;inst++){
                    for(int classe=0;classe<numClasses;classe++){
                        f[inst][classe] = y[inst][classe];
                    }
                }
            }    
        }else{
            while(exit == false){
                // 1st step: f = pf
                for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                    for(int classe=0;classe<numClasses;classe++){
                        double value = 0;
                        for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                            value += p[inst1][inst2] * f[inst2][classe];
                        }
                        fTemp[inst1][classe] = value;
                    }
                }

                double acmDif = 0;
                for(int inst=0;inst<numLabeled+numUnlabeled;inst++){
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
                for(int inst=0;inst<numLabeled;inst++){
                    for(int classe=0;classe<numClasses;classe++){
                        f[inst][classe] = y[inst][classe];
                    }
                }
            }    
        }
        
        
        // Acrescentei a partir DAQUI ==================================
        // =============================================================
        //Assigning labels to unlabeled documents considering class mass normalization
        sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                acmProbClasse += f[inst][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        
        for(int inst=0;inst<numUnlabeled;inst++){
            double[] classeTemp = new double[numClasses];
            for(int classe=0;classe<numClasses;classe++){
                classeTemp[classe] = (double)f[numLabeled + inst][classe]/(double)sumClassInformationDocs.get(classe);
            }
        }
        
    }
    
    public double classifyInstance(Instance instance){
        double[] distribution = distributionForInstance(instance);
        double maior = Double.MIN_VALUE;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(distribution[classe] > maior){
                maior = distribution[classe];
                index = classe;
            }
        }
        return index;
    }
    
    public double[] distributionForInstance(Instance instance){
        double[] distribution = new double[numClasses];
        double[] dists = new double[numLabeled + numUnlabeled];
        
        NeighborHash adjancecyListTest = new NeighborHash();
        for(int term=0;term<numTerms;term++){
            if(instance.value(term) > 0){
                adjancecyListTest.AddNeighbor(term,instance.value(term));
            }    
        }
        
        double[] numerador = new double[numClasses];
        double denominator = 0;
        if(mutualKnn){
            if(cosine == true){
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    dists[inst] = computeCosine(adjancecyListTest,adjancecyListDocTerm[inst]);
                }
            }else{
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    dists[inst] = computeVoteEuclidean(adjancecyListTest,adjancecyListDocTerm[inst],0.5);
                }
            }
            
            //Pegando os valores máximos
            for(int topK=0;topK<k;topK++){
                int ind=-1;
                double maiorTopK= -30000;
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    if(dists[inst] > maiorTopK){
                        maiorTopK = dists[inst];
                        ind = inst;
                    }
                }
                if(ind != -1){
                    for(int classe=0;classe<numClasses;classe++){
                        numerador[classe] = numerador[classe] + (maiorTopK * f[ind][classe]);
                    }
                    denominator = denominator + maiorTopK;
                    dists[ind] = -30000;
                }
            }
        }else{
            if(cosine == true){
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    double score = computeCosineExp(adjancecyListTest,adjancecyListDocTerm[inst],sigma);
                    for(int classe=0;classe<numClasses;classe++){
                        numerador[classe] = numerador[classe] + (score * f[inst][classe]);
                    }
                    denominator = denominator + score;
                }
            }else{
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    double score = computeVoteEuclidean(adjancecyListTest,adjancecyListDocTerm[inst],0.5);
                    for(int classe=0;classe<numClasses;classe++){
                        numerador[classe] = numerador[classe] + (score * f[inst][classe]);
                    }
                    denominator = denominator + score;
                }
            }
            
        }
        
        for(int classe=0;classe<numClasses;classe++){
            distribution[classe] = numerador[classe] / denominator;
        }
        
        return distribution;
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
