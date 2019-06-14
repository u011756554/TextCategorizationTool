//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of GNetMine algorithm (Ji et al., 2010)
//              to perform transductive classification of texts represented in a 
//              heterogeneous network composed by document-document and 
//              document-term  relations. Document-term matrix need to have ID 
//              as fist feature.
// References: - M. Ji, Y. Sun, M. Danilevsky, J. Han, J. Gao, Graph regularized
//               transductive classification on heterogeneous information
//               networks, in: Proceedings of the European Conference on Machine
//               Learning and Knowledge Discovery in Databases, Springer, 2010, 
//               pp. 570–586.
//*******************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class GNetMine_DocDoc_DocTerm_Transductive extends TransductiveLearner{
    
    private double[][] fDocs; // Class information of documents 
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double alphaDoc; //Parameter of the algorithms
    private double lambdaDocTerm; //Weight of document-term relations
    private double lambdaDocDoc; // Weight of ducument-document relations
    private double[][] w; //matrix to store weights of document-document relations
    
    //Adjacency lists of the classification algorithm
    private Neighbor[] adjacencyListSDocTerm;
    private Neighbor[] adjacencyListSDocDoc;
    private Neighbor[] adjacencyListSTermDoc;
    
    //Constructor
    public GNetMine_DocDoc_DocTerm_Transductive(){
        setMaxNumIterations(1000);
        setAlphaDoc(0.9);
        setLambdaDocTermo(0.5);
    }

    //Constructor
    public GNetMine_DocDoc_DocTerm_Transductive(int numIterations, double alphaDoc, double lambdaDocTerm){
        setNumIterations(numIterations);
        setAlphaDoc(alphaDoc);
        setLambdaDocTermo(lambdaDocTerm);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        numTrain = dataTrain.numInstances();
        numTest = dataTest.numInstances();
        numClasses = dataTrain.numClasses();
        numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
       
        if(getUse()==0){
            adjacencyListSDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListSDocDoc = new Neighbor[numTrain + numTest];
            adjacencyListSTermDoc = new Neighbor[numTerms];
            
            double[] dDocTerm = new double[numTrain + numTest]; // degree of documents considering document-term relations
            double[] dTermDoc = new double[numTerms]; // degree of terms considering document-term relations
            double[] dDocDoc = new double[numTrain + numTest]; // degree of documents considering document-document relations

            //Adjacency lists to speed up classification
            Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
            Neighbor[] adjacencyListPTermDoc = new Neighbor[numTerms];
            
            for(int inst=0;inst<(numTrain + numTest);inst++){
                adjancecyListDocTerm[inst] = new Neighbor();
                adjacencyListPDocTerm[inst] = new Neighbor();
            }
            for(int term=0;term<numTerms;term++){
                adjacencyListTermDoc[term] = new Neighbor();
                adjacencyListPTermDoc[term] = new Neighbor();
            }

            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    int indAtr = term -1;
                    if(dataTrain.instance(inst).value(term) > 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = indAtr;
                        indVal.value = dataTrain.instance(inst).value(term);
                        adjancecyListDocTerm[indInst].AddNeighbor(indVal);
                        indVal = new IndexValue();
                        indVal.index = indInst;
                        indVal.value = dataTrain.instance(inst).value(term);
                        adjacencyListTermDoc[indAtr].AddNeighbor(indVal);
                    }    
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    int indAtr = term -1;
                    if(dataTest.instance(inst).value(term) > 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = indAtr;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjancecyListDocTerm[indInst].AddNeighbor(indVal);
                        indVal = new IndexValue();
                        indVal.index = indInst;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjacencyListTermDoc[indAtr].AddNeighbor(indVal);
                    }    
                }
            }
            
            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDocTerm[indInst] = grau;
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDocTerm[indInst] = grau;
            }
            
            dTermDoc = new double[numTerms];
            for(int term=0;term<numTerms;term++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    grau += neighbors.get(inst).value;
                }
                dTermDoc[term] = grau;
            }

            for(int inst1=0;inst1<numTrain;inst1++){
                double grau = 0;
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += w[ind1][ind2];
                    }else{
                        grau += w[ind2][ind1];
                    }

                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += w[ind1][ind2];
                    }else{
                        grau += w[ind2][ind1];
                    }

                }
                dDocDoc[ind1] = Math.sqrt(grau);
            }
            for(int inst1=0;inst1<numTest;inst1++){
                double grau = 0;
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += w[ind1][ind2];
                    }else{
                        grau += w[ind2][ind1];
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += w[ind1][ind2];
                    }else{
                        grau += w[ind2][ind1];
                    }

                }
                dDocDoc[ind1] = Math.sqrt(grau);
            }
            
            for(int inst=0;inst<(numTrain + numTest);inst++){
                adjacencyListSDocTerm[inst] = new Neighbor();
                adjacencyListSDocDoc[inst] = new Neighbor();
            }
            
            for(int term=0;term<numTerms;term++){
                adjacencyListSTermDoc[term] = new Neighbor();
            }
            
            for(int inst=0;inst<numTrain;inst++){
                int ind1 = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(!((dDocTerm[ind1] == 0)  || (dTermDoc[neighbors.get(term).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = neighbors.get(term).value / (dDocTerm[ind1] * dTermDoc[neighbors.get(term).index]);
                        adjacencyListSDocTerm[ind1].AddNeighbor(indVal);
                    }
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int ind1 = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(!((dDocTerm[ind1] == 0)  || (dTermDoc[neighbors.get(term).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = neighbors.get(term).value / (dDocTerm[ind1] * dTermDoc[neighbors.get(term).index]);
                        adjacencyListSDocTerm[ind1].AddNeighbor(indVal);
                    }
                }
            }

            for(int term=0;term<numTerms;term++){
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    if(!((dTermDoc[term] == 0) || (dDocTerm[neighbors.get(inst).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(inst).index;
                        indVal.value = neighbors.get(inst).value / (dTermDoc[term] * dDocTerm[neighbors.get(inst).index]);
                        adjacencyListSTermDoc[term].AddNeighbor(indVal);
                    }
                }
            }
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                if(dDocDoc[ind1] == 0){
                    continue;
                }
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(dDocDoc[ind2] == 0){
                        continue;
                    }
                    double value=0;
                    if(ind1 > ind2){
                        if(w[ind1][ind2] == 0){
                            continue;
                        }
                        value =  w[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(w[ind2][ind1] == 0){
                            continue;
                        }
                        value = w[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListSDocDoc[ind1].AddNeighbor(indVal);
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(dDocDoc[ind2] == 0){
                        continue;
                    }
                    double value = 0;
                    if(ind1 > ind2){
                        if(w[ind1][ind2] == 0){
                            continue;
                        }
                        value = w[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(w[ind2][ind1] == 0){
                            continue;
                        }
                        value = w[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListSDocDoc[ind1].AddNeighbor(indVal);
                }
            }
            
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                if(dDocDoc[ind1] == 0){
                    continue;
                }
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(dDocDoc[ind2] == 0){
                        continue;
                    }
                    double value = 0;
                    if(ind1 > ind2){
                        if(w[ind1][ind2] == 0){
                            continue;
                        }
                        value = w[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(w[ind2][ind1] == 0){
                            continue;
                        }
                        value = w[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListSDocDoc[ind1].AddNeighbor(indVal);
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(dDocDoc[ind2] == 0){
                        continue;
                    }
                    double value = 0;
                    if(ind1 > ind2){
                        if(w[ind1][ind2] == 0){
                            continue;
                        }
                        value = w[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(w[ind2][ind1] == 0){
                            continue;
                        }
                        value = w[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListSDocDoc[ind1].AddNeighbor(indVal);
                }
            }
            dDocDoc=null;
            dDocTerm=null;
            dTermDoc=null;
            dDocDoc=null;

            adjancecyListDocTerm = null;
            adjacencyListPDocTerm = null;
            adjacencyListTermDoc = null;
            adjacencyListPTermDoc = null;

        }
        
        setUse(1);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        while(exit == false){
            //Propagating labels from documents to terms
            for(int atr1=0;atr1<numTerms;atr1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value1 = 0;
                    for(int inst1=0;inst1<adjacencyListSTermDoc[atr1].getNeighbors().size();inst1++){
                        value1 += adjacencyListSTermDoc[atr1].getNeighbors().get(inst1).value * fDocs[adjacencyListSTermDoc[atr1].getNeighbors().get(inst1).index][classe];
                    }
                    value1 = (value1 * lambdaDocTerm)/(lambdaDocTerm + 2*lambdaDocDoc);
                    fTerms[atr1][classe] = value1; 
                }
            }

            //Propagating labels from terms to documents and from documents to documents
            for(int inst1=0;inst1<(numTrain + numTest);inst1++){
                ArrayList<IndexValue> neighborsAtr = adjacencyListSDocTerm[inst1].getNeighbors();
                ArrayList<IndexValue> neighborsDoc = adjacencyListSDocDoc[inst1].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    double value1 = 0;
                    for(int atr1=0;atr1<neighborsAtr.size();atr1++){
                        value1 += (double)(neighborsAtr.get(atr1).value * fTerms[neighborsAtr.get(atr1).index][classe]);
                    }
                    value1 = value1 * lambdaDocTerm;
                    
                    double value2 = 0;
                    for(int inst2=0;inst2<neighborsDoc.size();inst2++){
                        value2 += alphaDoc * neighborsDoc.get(inst2).value * fDocs[neighborsDoc.get(inst2).index][classe];
                    }
                    
                    value2 = value2 * 2 * lambdaDocDoc;
                    double value = value1 + value2;
                    value += ((1 - alphaDoc) * yDoc[inst1][classe]);
                    value = (double)value / (double)((2 * lambdaDocTerm + 2*lambdaDocDoc * alphaDoc)); 
                    fDocsTemp[inst1][classe] = value;
                }
            }

            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
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
            int numIt = getNumiterations();
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }

            numIt++;
            setNumIterations(numIt);
        }
        
        //Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            int indTeste = (int)dataTest.instance(inst).value(0);
            int ind = -1;
            double maior = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(fDocs[indTeste][classe] > maior){
                    ind = classe;
                    maior = fDocs[indTeste][classe];
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
    
    public Double getAlphaDoc(){
        return alphaDoc;
    }
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public double getLambdaDocTermo(){
        return lambdaDocTerm;
    }
    
    public double getLambdaDocDoc(){
        return lambdaDocDoc;
    }
    
    public void setAlphaDoc(double alpha){
        this.alphaDoc = alpha;
    }
    
    public void setLambdaDocTermo(double lambdaDocTerm){
        this.lambdaDocTerm = lambdaDocTerm;
        setLambdaDocDoc(1 - lambdaDocTerm);
    }
    
    public void setLambdaDocDoc(double lambdaDocDoc){
        this.lambdaDocDoc = lambdaDocDoc;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setMatSim(double[][] w){
        this.w = w;
    }
    
    public double[][] getClassInformation(Instances dataTrain, Instances dataTest){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        int numClasses = dataTrain.numClasses();
        double[][] dist = new double[numTrain + numTest][numClasses];
        for(int inst=0;inst<numTrain;inst++){
            Instance instance = dataTrain.instance(inst);
            int ind = (int)instance.value(0);
            int pos = (int)instance.classValue();
            dist[ind][pos] = 1;
            
        }
        double value = 0;
        for(int inst=0;inst<numTest;inst++){
            Instance instance = dataTest.instance(inst);
            int ind = (int)instance.value(0);
            for(int classe=0;classe<numClasses;classe++){
                dist[ind][classe] = value;
            }
        }
        return dist;
    }
    
    
    
}
