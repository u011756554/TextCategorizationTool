//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of GNetMine algorithm (Ji et al., 2010)
//              to perform transductive classification of texts represented in a 
//              bipartite network (document-term relations).
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

public class GNetMine_DocTerm_Transductive extends TransductiveLearner{
    
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
    
    //Constructor
    public GNetMine_DocTerm_Transductive(){
        setMaxNumIterations(1000);
        setAlphaDoc(0.9);
    }
    
    //Constructor
    public GNetMine_DocTerm_Transductive(int numIterations, double alphaDoc){
        setNumIterations(numIterations);
        setAlphaDoc(alphaDoc);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        
        double[] dDoc = new double[numTrain + numTest]; // Degree of documents
        double[] dTerm = new double[numTerms]; // Degree of terms
        
        //Adjacency lists to speed up classification
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
        for(int inst1=0;inst1<numTrain;inst1++){
            double valueD = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst1].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                valueD += neighbors.get(term).value;
            }
            dDoc[inst1] = Math.sqrt(valueD);
        }
        for(int inst1=0;inst1<numTest;inst1++){
            double valueD = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst1 + numTrain].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                valueD += neighbors.get(term).value;
            }
            dDoc[inst1 + numTrain] = Math.sqrt(valueD);
        }
        for(int atr1=0;atr1<numTerms;atr1++){
            double valueD = 0;
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
            for(int inst1=0;inst1<neighbors.size();inst1++){
                valueD += neighbors.get(inst1).value;
            }
            dTerm[atr1] = Math.sqrt(valueD);
        }
        
        Neighbor[] adjacencyListSDocTerm = new Neighbor[numTrain + numTest];
        for(int inst=0;inst<(numTrain + numTest);inst++){
            adjacencyListSDocTerm[inst] = new Neighbor();
        }
        Neighbor[] adjacencyListSTermDoc = new Neighbor[numTerms];
        for(int term=0;term<numTerms;term++){
            adjacencyListSTermDoc[term] = new Neighbor();
        }
        for(int inst=0;inst<(numTrain + numTest);inst++){
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                if(!((dDoc[inst] == 0)  || (dTerm[neighbors.get(term).index] == 0))){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(term).index;
                    indVal.value = neighbors.get(term).value / (dDoc[inst] * dTerm[neighbors.get(term).index]);
                    adjacencyListSDocTerm[inst].AddNeighbor(indVal);
                    IndexValue indVal2 = new IndexValue();
                    indVal2.index = inst;
                    indVal2.value = neighbors.get(term).value / (dDoc[inst] * dTerm[neighbors.get(term).index]);
                    adjacencyListSTermDoc[neighbors.get(term).index].AddNeighbor(indVal2);
                }
            }
        }
        
        boolean exit = false;
        while(exit == false){
            //Propagating labels from documents to terms
            for(int atr1=0;atr1<numTerms;atr1++){
                ArrayList<IndexValue> neighbors = adjacencyListSTermDoc[atr1].getNeighbors();
                for(int class1=0;class1<numClasses;class1++){
                    double value = 0;
                    for(int inst1=0;inst1<neighbors.size();inst1++){
                        value += neighbors.get(inst1).value * fDocs[neighbors.get(inst1).index][class1];
                    }
                    fTerms[atr1][class1] = value;
                }
            }

            //Propagating labels from terms to documents
            for(int inst1=0;inst1<(numTrain + numTest);inst1++){
                ArrayList<IndexValue> neighbors = adjacencyListSDocTerm[inst1].getNeighbors();
                for(int class1=0;class1<numClasses;class1++){
                    double value = 0;
                    for(int atr1=0;atr1<neighbors.size();atr1++){
                        value += (double)(neighbors.get(atr1).value * fTerms[neighbors.get(atr1).index][class1]);
                    }
                    value += (alphaDoc * yDoc[inst1][class1]);
                    value = (double)value / (double)((1 + alphaDoc)); 
                    fDocsTemp[inst1][class1] = value;
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
            int ind = -1;
            double maior = Double.MIN_VALUE;
            //System.out.print(inst + ": ");
            for(int classe=0;classe<numClasses;classe++){
                if(fDocs[inst + numTrain][classe] > maior){
                    ind = classe;
                    maior = fDocs[inst + numTrain][classe];
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
    
    public void setAlphaDoc(double alpha){
        this.alphaDoc = alpha;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
}
