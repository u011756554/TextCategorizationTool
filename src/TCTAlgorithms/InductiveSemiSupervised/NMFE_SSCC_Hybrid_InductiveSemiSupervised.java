//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: March 12, 2015
// Description: This is an implementation of NMFE-SSCC (Non-negative matrix 
//              factorization for semi-supervised collective classification
//              (Wu et al., 2015). The classification of new documents is
//              carried out connecting a new document to the most similar
//              documents and propagatin the class information contained in 
//              the matrix v to set the class information of new document.
// References: - Q. Wu, M. Tan, X. Li, H. Min, N. Sun, NMFE-SSCC: non-negative 
//               matrix factorization ensemble for semi-supervised collective 
//               classification, Knowledge-Based Systems 89 (2015) 160–172.
//******************************************************************************

package TCTAlgorithms.InductiveSemiSupervised;

import TCTNetworkGeneration.DocumentNetworkGeneration;
import static TCTNetworkGeneration.Proximity.computeCosine;
import static TCTNetworkGeneration.Proximity.computeCosineExp;
import static TCTNetworkGeneration.Proximity.computeVoteEuclidean;
import TCTStructures.NeighborHash;
import java.util.Random;
import weka.core.Instance;
import weka.core.Instances;
import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class NMFE_SSCC_Hybrid_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
     //matrices of the algorithm
    private double[][] v; // class information of documents
    private double[][] vTemp;
    private double[][] u; // class information of terms
    private double[][] b; 
    private double[][] w; // document-term matrix
    //private double[][] y;
    //private double[][] x;
    
    //variables of the algorithms
    private double alpha;
    private double beta;
    private double omega;

    //general variables
    private int numLabeled;
    private int numUnlabeled;
    private int numClasses;
    private int numTerms;
    private int numMaxIterations;
    private double minDifference;
    
    //variables to set the type of network and to store the network
    private boolean gaussian;
    private boolean cosine;
    private double sigma;
    private int k;
    private double[][] matSim;
    
    //AdjacencyList Used to speed up the classificaion of new documents
    NeighborHash[] adjancecyHashListDocTerm;
    
    
    public NMFE_SSCC_Hybrid_InductiveSemiSupervised(){
        super();
        setAlpha(0);
        setBeta(0);
        setUse(0);
        setOmega(0);
        setNumMaxIterations(1000);
        setMinDifference(0.1);
        
    }
    
    public void buildClassifier(Instances dataLabeled, Instances dataUnlabeled){
        this.numLabeled = dataLabeled.numInstances();
        this.numUnlabeled = dataUnlabeled.numInstances();
        this.numClasses = dataLabeled.numClasses();
        this.numTerms = dataLabeled.numAttributes() - 1;
        
        v = getClassInformation(dataLabeled,dataUnlabeled);
        vTemp = new double[numLabeled+numUnlabeled][numClasses];
        
        //y = getClassInformation(dataTrain,dataTest);
        
        b = new double[numClasses][numClasses];
        
        Random rand = new Random(0);
        for(int class1=0;class1<numClasses;class1++){
            for(int class2=0;class2<numClasses;class2++){
                b[class1][class2] = rand.nextDouble() % 1;
            }
        }
        
        u = new double[numTerms][numClasses];
        //uTemp = new double[numTerms][numClasses];
        //Initializing u with random values
        for(int term=0;term<numTerms;term++){
            for(int classe=0;classe<numClasses;classe++){
                u[term][classe] = rand.nextDouble() % 1;
            }
        }
        
        for(int doc=0;doc<numUnlabeled;doc++){
            for(int classe=0;classe<numClasses;classe++){
                v[numLabeled + doc][classe] = (double)1/(double)numClasses;
            }
        }
        
        w = computeW();
        double[][] wTransp = matrixTranspose(w);
        double[][] matDotProdWY = matrixDotProd(w,v);
        double[][] matDotProdWYTransp = matrixTranspose(matDotProdWY);
        
        //generating matrix x
        /*x = new double[numTrain + numTest][numTerms];
        for(int doc=0;doc<numTrain;doc++){
            for(int term=0;term<numTerms;term++){
                x[doc][term] = dataTrain.instance(doc).value(term);
            }
        }
        for(int doc=0;doc<numTest;doc++){
            for(int term=0;term<numTerms;term++){
                x[doc + numTrain][term] = dataTest.instance(doc).value(term);
            }
        }*/
        
        matSim = new double[numLabeled + numUnlabeled][numLabeled + numUnlabeled];
        
        Neighbor[] adjancecyListDocTerm = new Neighbor[numLabeled + numUnlabeled];
        Neighbor[] adjancecyListTermDoc = new Neighbor[numTerms];
        for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            
        }
        for(int term=0;term<numTerms;term++){
            adjancecyListTermDoc[term] = new Neighbor();
        }
        for(int inst=0;inst<numLabeled;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataLabeled.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataLabeled.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = inst;
                    indVal.value = dataLabeled.instance(inst).value(term);
                    adjancecyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        for(int inst=0;inst<numUnlabeled;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataUnlabeled.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataUnlabeled.instance(inst).value(term);
                    adjancecyListDocTerm[inst + numLabeled].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = inst + numLabeled;
                    indVal.value = dataUnlabeled.instance(inst).value(term);
                    adjancecyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        
        if(getCosine()){
            
            adjancecyHashListDocTerm= new NeighborHash[numLabeled + numUnlabeled];
            for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
                adjancecyHashListDocTerm[inst] = new NeighborHash();
            }

            for(int inst=0;inst<numLabeled;inst++){
                for(int term=0;term<numTerms;term++){
                    if(dataLabeled.instance(inst).value(term) > 0){
                        adjancecyHashListDocTerm[inst].AddNeighbor(term,dataLabeled.instance(inst).value(term));
                    }    
                }
            }
            for(int inst=0;inst<numUnlabeled;inst++){
                for(int term=0;term<numTerms;term++){
                    if(dataUnlabeled.instance(inst).value(term) > 0){
                        adjancecyHashListDocTerm[inst + numLabeled].AddNeighbor(term,dataUnlabeled.instance(inst).value(term));
                    }    
                }
            }
            if(getGaussian() == true){
                matSim = DocumentNetworkGeneration.GenerateExpNetworkCosine(adjancecyHashListDocTerm, sigma);
            }else{
                matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkCosine(adjancecyHashListDocTerm, k);
            }
            
        }else{
            if(getGaussian() == true){
                matSim = DocumentNetworkGeneration.GenerateGaussianNetworkEuclidean(dataLabeled, dataUnlabeled, sigma);
            }else{
                matSim = DocumentNetworkGeneration.GenerateMutualKnnNetworkEuclidean(dataLabeled, dataUnlabeled, k);
            }    
        }
        
        double[] d = new double[numLabeled + numUnlabeled];
        for(int doc1=0;doc1<(numLabeled + numUnlabeled);doc1++){
            double acm=0;
            for(int doc2=0;doc2<(numLabeled + numUnlabeled);doc2++){
                acm+= matSim[doc1][doc2];
            }
            d[doc1] = acm;
        }
        
        //double[][]  matDotXY = matrixDotProd(w,y);       
        //double[][]  matDotXYTransp = matrixTranspose(matDotXY);
        
        boolean sair=false;
        int numIt=0;
        while(sair == false){ 
            
            //Step 1
            double[][] matNumerador = matrixMultiplication(adjancecyListTermDoc, v);
            double[][] matDenominador = matrixMultiplication(u, matrixMultiplication(matrixTranspose(v),v));

            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    if(matDenominador[term][classe] == 0){
                        u[term][classe] = 0;
                    }else{
                        u[term][classe] = u[term][classe] * matNumerador[term][classe]/matDenominador[term][classe];
                    }
                    
                }
            }
            
            //Step 2
            matNumerador = matrixMultiplication(matDotProdWYTransp, v);
            matDenominador = matrixMultiplication(matrixDotProd(wTransp,matrixMultiplication(b,matrixTranspose(v))),v);    
            //Updating the values of matrix B
            for(int class1=0;class1<numClasses;class1++){
                for(int class2=0;class2<numClasses;class2++){
                    if(matDenominador[class1][class2] == 0){
                        b[class1][class2] = 0;
                    }else{
                        b[class1][class2] = b[class1][class2] * matNumerador[class1][class2] / matDenominador[class1][class2];
                    }
                }
            }
            
            //Step3
            double[][] numerator1 = matrixMultiplication(adjancecyListDocTerm, u); 
            double[][] numerator2 = matrixMultiplication(matDotProdWY,b);
            double[][] numerator3 = matrixMultiplication(matSim,v);
            
            double[][] denominator1 = matrixMultiplication(v, matrixMultiplication(matrixTranspose(u),u));
            double[][] denominator2 = matrixMultiplication(matrixDotProd(w, matrixMultiplication(v,matrixTranspose(b))),b);
            
            for(int doc=0;doc<numUnlabeled;doc++){
                for(int classe=0;classe<numClasses;classe++){
                    if((denominator1[doc+numLabeled][classe] + alpha*denominator2[doc+numLabeled][classe] + beta*d[doc+numLabeled]*v[doc+numLabeled][classe]) == 0){
                        vTemp[doc+numLabeled][classe] = 0;
                    }else{
                        vTemp[doc+numLabeled][classe] = v[doc+numLabeled][classe] * ((numerator1[doc+numLabeled][classe] + alpha*numerator2[doc+numLabeled][classe] + beta*numerator3[doc+numLabeled][classe])/(denominator1[doc+numLabeled][classe] + alpha*denominator2[doc+numLabeled][classe] + beta*d[doc+numLabeled]*v[doc+numLabeled][classe]));
                    }
                    
                }
            }
            
            numIt++;
            if(numIt == numMaxIterations){
                sair = true;
                continue;
            }
            //computing the difference between consecutive iterations
            if(numIt == 1){
                continue;
            }
            double acmDiff=0;
            for(int doc=0;doc<numUnlabeled;doc++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDiff = Math.abs(vTemp[doc+numLabeled][classe] - v[doc+numLabeled][classe]);
                    v[doc+numLabeled][classe] = vTemp[doc+numLabeled][classe];
                }
            }
            //System.out.println("Difference: " + acmDiff);
            
            if(acmDiff < minDifference){
                if(numIt > 1){
                    sair = true;
                }
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
        if(getGaussian() == false){
            if(cosine == true){
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    dists[inst] = computeCosine(adjancecyListTest,adjancecyHashListDocTerm[inst]);
                }
            }else{
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    dists[inst] = computeVoteEuclidean(adjancecyListTest,adjancecyHashListDocTerm[inst],0.5);
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
                        numerador[classe] = numerador[classe] + (maiorTopK * v[ind][classe]);
                    }
                    denominator = denominator + maiorTopK;
                    dists[ind] = -30000;
                }
            }
        }else{
            if(cosine == true){
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    double score = computeCosineExp(adjancecyListTest,adjancecyHashListDocTerm[inst],sigma);
                    for(int classe=0;classe<numClasses;classe++){
                        numerador[classe] = numerador[classe] + (score * v[inst][classe]);
                    }
                    denominator = denominator + score;
                }
            }else{
                for(int inst=0;inst<(numLabeled+numUnlabeled);inst++){
                    double score = computeVoteEuclidean(adjancecyListTest,adjancecyHashListDocTerm[inst],0.5);
                    for(int classe=0;classe<numClasses;classe++){
                        numerador[classe] = numerador[classe] + (score * v[inst][classe]);
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
    
    public void setMinDifference(double minDifference){
        this.minDifference = minDifference;
    }
    
    public void setOmega(double omega){
        this.omega = omega;
    }
    
    public void setGaussian(boolean gaussian){
        this.gaussian = gaussian;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    public void setSigma(double sigma){
        this.sigma = sigma;
    }

    public void setK(int k){
        this.k = k;
    }
    
    public void setNumMaxIterations(int numMaxIterations){
        this.numMaxIterations = numMaxIterations;
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setBeta(double beta){
        this.beta = beta;
    }
    
    public double getMinDifference(){
        return minDifference;
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public double getBeta(){
        return beta;
    }
    
    public int getNumMaxIterations(){
        return numMaxIterations;
    }
    
    public boolean getGaussian(){
        return gaussian;
    }
    
    public boolean getCosine(){
        return cosine;
    }
    
    public double getSigma(){
        return sigma;
    }
    
    public int getK(){
        return k;
    }
    
    public double getOmega(){
        return omega;
    }
    
    public double[][] computeW(){
        double[][] w = new double[numLabeled+numUnlabeled][numClasses];
        for(int doc=0;doc<numLabeled;doc++){
            for(int classe=0;classe<numClasses;classe++){
                if(v[doc][classe] == 1){
                    w[doc][classe] = omega;
                }else{
                    w[doc][classe] = 1 - omega;
                }
            }
        }
        return w;
    }
    
   
    public double[][] matrixMultiplication(double mat1[][], double mat2[][]){
        int numLinesMat1 = mat1.length;
        int numColsMat1 = mat1[0].length;
        int numLinesMat2 = mat2.length;
        int numColsMat2 = mat2[0].length;
        
        if(numColsMat1 != numLinesMat2){
            System.err.println("Incorrect Matrix Multiplication");
            System.exit(0);
        }
        
        double[][] matResult = new double[numLinesMat1][numColsMat2];
        
        for(int id1=0;id1<numLinesMat1;id1++){
            for(int id2=0;id2<numColsMat2;id2++){
                double acm = 0;
                for(int id3=0;id3<numColsMat1;id3++){
                    acm += mat1[id1][id3] * mat2[id3][id2];
                }
                matResult[id1][id2] = acm;
            }
        }
        
        return matResult;
    }
    
    public double[][] matrixMultiplication(Neighbor[] adjList, double mat2[][]){
        int numLinesMat1 = adjList.length;
        int numColsMat1;
        
        int numLinesMat2 = mat2.length;
        int numColsMat2 = mat2[0].length;

        double[][] matResult = new double[adjList.length][numColsMat2];
        
        for(int id1=0;id1<numLinesMat1;id1++){
            ArrayList<IndexValue> neighbors = adjList[id1].getNeighbors();
            for(int id2=0;id2<numColsMat2;id2++){
                double acm=0;
                for(int id3=0;id3<neighbors.size();id3++){
                    acm += neighbors.get(id3).value * mat2[neighbors.get(id3).index][id2];
                }
                matResult[id1][id2] = acm;
            }
        }
        
        return matResult;
    }
    
    public double[][] matrixTranspose(double[][] matrix){
        int numLines = matrix.length;
        int numCols = matrix[0].length;
        
        double matT[][] = new double[numCols][numLines];
        
        for(int lin=0;lin<numLines;lin++){
            for(int col=0;col<numCols;col++){
                matT[col][lin] = matrix[lin][col];
            }
        }
        
        return matT;
    }
    
    public double[][] matrixDotProd(double[][] mat1, double[][] mat2){
        int numLinesMat1 = mat1.length;
        int numColsMat1 = mat1[0].length;
        int numLinesMat2 = mat2.length;
        int numColsMat2 = mat2[0].length;
        
        if((numLinesMat1 != numLinesMat2) || (numColsMat1 != numColsMat2)){
            System.err.println("Incorrect Matrix Dimensions");
            System.exit(0);
        }
        
        double[][] matDot = new double[numLinesMat1][numColsMat1];
        
        for(int lin=0;lin<numLinesMat1;lin++){
            for(int col=0;col<numColsMat1;col++){
                matDot[lin][col] = mat1[lin][col] * mat2[lin][col];
            }
        }
        
        return matDot;
    }
    
}
