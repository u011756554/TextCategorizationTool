//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of GNetMine algorithm (Ji et al., 2010)
//              to perform transductive classification of texts represented in a 
//              heterogeneous network composed by document-term and term-term 
//              relations. Document-term matrix need to have ID as fist feature.
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
import weka.core.Instances;

public class GNetMine_DocTerm_TermTerm_Transductive_ID extends TransductiveLearner{
    
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
    private double lambdaTermTerm; //Weight of term-term relations
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    //Adjacency lists of the classification algorithm
    private Neighbor[] adjacencyListSDocTerm = new Neighbor[numTrain + numTest];
    private Neighbor[] adjacencyListSTermDoc = new Neighbor[numTerms];
    private Neighbor[] adjacencyListSTermTerm = new Neighbor[numTerms];
    
    // Constructor
    public GNetMine_DocTerm_TermTerm_Transductive_ID(){
        setMaxNumIterations(1000);
        setAlphaDoc(0.9);
        setLambdaDocTermo(0.5);
    }
    
    // Constructor
    public GNetMine_DocTerm_TermTerm_Transductive_ID(int numIterations, double alphaDoc, double lambdaDocTerm){
        setNumIterations(numIterations);
        setAlphaDoc(alphaDoc);
        setLambdaDocTermo(lambdaDocTerm);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation_ID(dataTrain,dataTest);
        yDoc = getClassInformation_ID(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        
        
        
        if(getUse()==0){
            double[] dDocTerm = new double[numTrain + numTest]; // Degree of documents considering document-term relations
            double[] dTermDoc = new double[numTerms]; // Degree of terms considering document-term relations
            double[] dTermTerm = new double[numTerms]; // Degree of terms considering term-term relations
            adjacencyListSDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListSTermDoc = new Neighbor[numTerms];
            adjacencyListSTermTerm = new Neighbor[numTerms];
            
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
                dDocTerm[indInst] = Math.sqrt(grau);
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDocTerm[indInst] = Math.sqrt(grau);
            }

            //Normalizing documentiterm relations to make maximum and minimum relation weights equal to term-term relations
            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(dDocTerm[indInst] != 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDocTerm[indInst];
                        adjacencyListPDocTerm[indInst].AddNeighbor(indVal);
                        IndexValue indVal2 = new IndexValue();
                        indVal2.index = indInst;
                        indVal2.value = indVal.value;
                        adjacencyListPTermDoc[neighbors.get(term).index].AddNeighbor(indVal2);
                    }
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(dDocTerm[indInst] != 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDocTerm[indInst];
                        adjacencyListPDocTerm[indInst].AddNeighbor(indVal);
                        IndexValue indVal2 = new IndexValue();
                        indVal2.index = indInst;
                        indVal2.value = indVal.value;
                        adjacencyListPTermDoc[neighbors.get(term).index].AddNeighbor(indVal2);
                    }
                }
            }
            
            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                if(grau > 0){
                    dDocTerm[indInst] = Math.sqrt(grau);
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                if(grau > 0){
                    dDocTerm[indInst] = Math.sqrt(grau);
                }
            }
            
            for(int term=0;term<numTerms-1;term++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListPTermDoc[term].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    grau += neighbors.get(inst).value;
                }
                if(grau>0){
                    dTermDoc[term] = Math.sqrt(grau);
                }

            }
            for(int atr1=0;atr1<numTerms-1;atr1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                for(int atr2=0;atr2<neighbors.size();atr2++){
                    grau += neighbors.get(atr2).value;
                }
                if(grau > 0){
                    dTermTerm[atr1] = Math.sqrt(grau);
                }
            }

            for(int inst=0;inst<(numTrain + numTest);inst++){
                adjacencyListSDocTerm[inst] = new Neighbor();
            }
            
            for(int term=0;term<numTerms;term++){
                adjacencyListSTermDoc[term] = new Neighbor();
                adjacencyListSTermTerm[term] = new Neighbor();
            }
            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(!((dDocTerm[indInst] == 0)  || (dTermDoc[neighbors.get(term).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = neighbors.get(term).value / (dDocTerm[indInst] * dTermDoc[neighbors.get(term).index]);
                        adjacencyListSDocTerm[indInst].AddNeighbor(indVal);
                    }
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(!((dDocTerm[indInst] == 0)  || (dTermDoc[neighbors.get(term).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = neighbors.get(term).value / (dDocTerm[indInst] * dTermDoc[neighbors.get(term).index]);
                        adjacencyListSDocTerm[indInst].AddNeighbor(indVal);
                    }
                }
            }
            
            for(int term=0;term<numTerms-1;term++){
                ArrayList<IndexValue> neighbors = adjacencyListPTermDoc[term].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    if(!((dTermDoc[term] == 0) || (dDocTerm[neighbors.get(inst).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(inst).index;
                        indVal.value = neighbors.get(inst).value / (dTermDoc[term] * dDocTerm[neighbors.get(inst).index]);
                        adjacencyListSTermDoc[term].AddNeighbor(indVal);
                    }
                }
            }
            
            for(int atr1=0;atr1<numTerms-1;atr1++){
                ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                for(int atr2=0;atr2<neighbors.size();atr2++){
                    if(!((dTermTerm[atr1] == 0)  || (dTermTerm[neighbors.get(atr2).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(atr2).index;
                        indVal.value = neighbors.get(atr2).value / (dTermTerm[atr1] * dTermTerm[neighbors.get(atr2).index]);
                        adjacencyListSTermTerm[atr1].AddNeighbor(indVal);
                    }
                }
            }
            dDocTerm=null;
            dTermDoc=null;
            dTermTerm=null;
            adjancecyListDocTerm = null;
            adjacencyListPDocTerm = null;
            adjacencyListTermDoc = null;
            adjacencyListPTermDoc = null;
        }
        
        setUse(1);
        
        boolean exit = false;
        while(exit == false){
            for(int atr1=0;atr1<numTerms-1;atr1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value1 = 0;
                    //Propagating labels from documents to terms
                    for(int inst1=0;inst1<adjacencyListSTermDoc[atr1].getNeighbors().size();inst1++){
                        value1 += adjacencyListSTermDoc[atr1].getNeighbors().get(inst1).value * fDocs[adjacencyListSTermDoc[atr1].getNeighbors().get(inst1).index][classe];
                    }
                    value1 = value1 * lambdaDocTerm;
                    double value2 = 0;
                    //Propagatin labels from terms to terms
                    for(int atr2=0;atr2<adjacencyListSTermTerm[atr1].getNeighbors().size();atr2++){
                        value2 += adjacencyListSTermTerm[atr1].getNeighbors().get(atr2).value * fTerms[adjacencyListSTermTerm[atr1].getNeighbors().get(atr2).index][classe];
                    }
                    value2 = 2 * value2 * lambdaTermTerm;
                    fTerms[atr1][classe] = (value1 + value2)/(lambdaDocTerm + (2*lambdaTermTerm));
                }
            }

            // Propagating labels from terms to documents
            for(int inst1=0;inst1<numTrain;inst1++){
                int indInst = (int)dataTrain.instance(inst1).value(0);
                ArrayList<IndexValue> neighbors = adjacencyListSDocTerm[indInst].getNeighbors();
                for(int class1=0;class1<numClasses;class1++){
                    double value = 0;
                    for(int atr1=0;atr1<neighbors.size();atr1++){
                        value += (double)(neighbors.get(atr1).value * fTerms[neighbors.get(atr1).index][class1]);
                    }
                    value = value * lambdaDocTerm;
                    value += (alphaDoc * yDoc[indInst][class1]);
                    value = (double)value / (double)((lambdaDocTerm + alphaDoc)); 
                    fDocsTemp[indInst][class1] = value;
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int indInst = (int)dataTest.instance(inst1).value(0);
                ArrayList<IndexValue> neighbors = adjacencyListSDocTerm[indInst].getNeighbors();
                for(int class1=0;class1<numClasses;class1++){
                    double value = 0;
                    for(int atr1=0;atr1<neighbors.size();atr1++){
                        value += (double)(neighbors.get(atr1).value * fTerms[neighbors.get(atr1).index][class1]);
                    }
                    value = value * lambdaDocTerm;
                    value += (alphaDoc * yDoc[indInst][class1]);
                    value = (double)value / (double)((lambdaDocTerm + alphaDoc)); 
                    fDocsTemp[indInst][class1] = value;
                }
            }

            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
                }    
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
    
    public double getLambdaTermoTermo(){
        return lambdaTermTerm;
    }
    
    public void setAlphaDoc(double alpha){
        this.alphaDoc = alpha;
    }
    
    public void setLambdaDocTermo(double lambdaDocTerm){
        this.lambdaDocTerm = lambdaDocTerm;
        setLambdaTermoTermo(1 - lambdaDocTerm);
    }
    
    public void setLambdaTermoTermo(double lambdaTermTerm){
        this.lambdaTermTerm = lambdaTermTerm;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
    }
    
}
