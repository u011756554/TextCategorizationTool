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

package TCTAlgorithms.InductiveSemiSupervised;

import TCTNetworkGeneration.DocumentNetworkGeneration;
import static TCTNetworkGeneration.Proximity.computeCosine;
import static TCTNetworkGeneration.Proximity.computeCosineExp;
import static TCTNetworkGeneration.Proximity.computeEuclideanExp;
import static TCTNetworkGeneration.Proximity.computeVoteEuclidean;
import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import TCTStructures.NeighborHash;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;


public class LLGC_DocDoc_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
    private double[][] f; // Class information of documents
    private double[][] fTemp; // Class information of documents 
    private double[][] y; // Real class information (labels) of labeled documents // Real class information (labels) of labeled documents
    private int numLabeled; // Number of labeled documents
    private int numUnlabeled; // Number of unlabeled documents
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
    
    NeighborHash[] adjancecyListDocTerm;
    
    private double[][] matSim; // matrix to store relation weights among documents
    
    // Matrices of the algorithm
    private double[] d;
    private double[][] s;
    
    Neighbor[] listaAdjS;
    
    public LLGC_DocDoc_InductiveSemiSupervised(){
        super();
        setAlpha(0);
        setUse(0);
        setSigma(0.15);
        
    }
    
    public LLGC_DocDoc_InductiveSemiSupervised(int maxNumberInterations, double alpha, double sigma){
        setMaxNumIterations(maxNumberInterations);
        setAlpha(alpha);
        setUse(0);
    }
    
    public void buildClassifier(Instances dataLabeled, Instances dataUnlabeled){
        this.numLabeled = dataLabeled.numInstances();
        this.numUnlabeled = dataUnlabeled.numInstances();
        this.numClasses = dataLabeled.numClasses();
        this.numTerms = dataLabeled.numAttributes() - 1;
        
        //f = getClassInformationIni();
        f = getClassInformation(dataLabeled,dataUnlabeled);
        y = getClassInformation(dataLabeled,dataUnlabeled);
        fTemp = new double[numLabeled + numUnlabeled][numClasses];

        matSim = new double[numLabeled + numUnlabeled][numLabeled + numUnlabeled];
        d = new double[numLabeled + numUnlabeled];
        

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
        
        //System.out.println("Gerando a rede...");
        if(getCosine()){
            
            if(getGaussian() == true){
                matSim = DocumentNetworkGeneration.GenerateExpNetworkCosine(adjancecyListDocTerm, sigma);
            }else{
                if(getMutualKnn() == true){
                    //w = GerarNetworks.gerarNetworkKnnAssimetrica(dataLabeled, dataTest, k);
                    matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkCosine(adjancecyListDocTerm, k);
                }
            }
            adjacencyListTermDoc = null;
        }else{
            if(getGaussian() == true){
                matSim = DocumentNetworkGeneration.GenerateGaussianNetworkEuclidean(dataLabeled, dataUnlabeled, sigma);
            }else{
                if(getMutualKnn() == true){
                    //w = GerarNetworks.gerarNetworkKnnAssimetrica(dataLabeled, dataTest, k);
                    matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkEuclidean(dataLabeled, dataUnlabeled, k);
                }
            }    
        }
        
        //System.out.println("Calculando Matrizes");
        for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
            double valueD = 0;
            for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                valueD += matSim[inst1][inst2];
            }
            d[inst1] = valueD;
        }
        
        if(mutualKnn == true){
            System.out.println("Using Adjacency List!");
            listaAdjS = new Neighbor[numLabeled + numUnlabeled];
            for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
                listaAdjS[inst] = new Neighbor();
            }
            for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                    if((d[inst1] != 0)  && (d[inst2] != 0)){
                        double s = matSim[inst1][inst2] / (Math.sqrt(d[inst1]) * Math.sqrt(d[inst2]));
                        IndexValue indVal = new IndexValue();
                        indVal.index = inst2;
                        indVal.value = s;
                        listaAdjS[inst1].AddNeighbor(indVal);
                    }
                }
            }
            
        }else{
            s = new double[numLabeled + numUnlabeled][numLabeled + numUnlabeled];
            for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                    if((d[inst1] == 0)  || (d[inst2] == 0)){
                        s[inst1][inst2] = 0;
                    }else{
                        s[inst1][inst2] = matSim[inst1][inst2] / (Math.sqrt(d[inst1]) * Math.sqrt(d[inst2]));
                    }
                }
            }
        }
        d=null;
        matSim=null;
        
        //System.out.println("Algoritmo...");
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        if(mutualKnn == true){
            while(exit == false){
                // Propagating labels from documents to documents
                for(int inst1=0;inst1<numLabeled;inst1++){
                    for(int classe=0;classe<numClasses;classe++){
                        double fTemp1=0;
                        ArrayList<IndexValue> neighbors = listaAdjS[inst1].getNeighbors();
                        for(int inst2=0;inst2<neighbors.size();inst2++){
                            fTemp1 += alpha * neighbors.get(inst2).value * f[neighbors.get(inst2).index][classe];

                        }
                        fTemp[inst1][classe] = fTemp1  + (1 - alpha)*y[inst1][classe];
                    }    
                }
                for(int inst1=0;inst1<numUnlabeled;inst1++){
                    for(int classe=0;classe<numClasses;classe++){
                        double fTemp1=0;
                        ArrayList<IndexValue> neighbors = listaAdjS[inst1].getNeighbors();
                        for(int inst2=0;inst2<neighbors.size();inst2++){
                            fTemp1 += alpha * neighbors.get(inst2).value * f[neighbors.get(inst2).index][classe];
                        }
                        fTemp[inst1][classe] = fTemp1  + (1 - alpha)*y[inst1][classe];
                    }    
                }

                double acmDif = 0;
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
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
        }else{
            while(exit == false){
                // Propagatin labels among documents
                for(int inst1=0;inst1<(numLabeled+numUnlabeled);inst1++){
                    for(int classe=0;classe<numClasses;classe++){
                        double fTemp1=0;
                        for(int inst2=0;inst2<(numLabeled+numUnlabeled);inst2++){
                            fTemp1 += alpha * s[inst1][inst2] * f[inst2][classe];
                        }
                        fTemp[inst1][classe] = fTemp1  + (1 - alpha)*y[inst1][classe];
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

                // Analysis of stopping criteria
                //System.out.println(numIt + "- Acmdif: " + acmDif);
                if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                    exit = true;
                }

                numIt++;
                setNumIterations(numIt);
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
