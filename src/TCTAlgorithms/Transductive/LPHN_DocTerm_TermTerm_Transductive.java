//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of LPHN (Label Propagation based on 
//              Heterogeneous Network), wich is an generalization of LPBHN 
//              algorithm (Rossi et al., 2012) to perform transductive
//              classification in heterogeneous networks. This algorithms 
//              considers text collections represented in a heterogeneous 
//              network composed by term-term and document-term relations. 
// References: - R. G. Rossi, A. A. Lopes, S. O. Rezende, A parameter-free label
//               propagation algorithm using bipartite heterogeneous networks 
//               for text classification, in: Proceedings of the Symposium on 
//               Applied Computing (in press), ACM, 2014, pp. 79–84.
//********************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instances;

public class LPHN_DocTerm_TermTerm_Transductive extends TransductiveLearner{
    
    private double[][] fDocs; // Class information of documents
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    //Constructor
    public LPHN_DocTerm_TermTerm_Transductive(){
        super();
    }
        
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fDocsTemp = getClassInformation(dataTrain,dataTest);;
        fUnlabeledDocs = new double[numTest][numClasses];
        fTerms = new double[numTerms][numClasses];
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
        Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
        Neighbor[] adjacencyListPTermDoc = new Neighbor[numTerms];
        Neighbor[] adjacencyListPTermTerm = new Neighbor[numTerms];

        for(int inst=0;inst<(numTrain + numTest);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            adjacencyListPDocTerm[inst] = new Neighbor();
        }
        for(int term=0;term<numTerms;term++){
            adjacencyListTermDoc[term] = new Neighbor();
            adjacencyListPTermDoc[term] = new Neighbor();
            adjacencyListPTermTerm[term] = new Neighbor();
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

        double[] dDoc = new double[numTrain + numTest]; // degree of documents
        for(int inst=0;inst<(numTrain+numTest);inst++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                grau += neighbors.get(term).value;
            }
            dDoc[inst] = grau;
        }

        double[] dTerm = new double[numTerms]; // degree of terms
        for(int atr1=0;atr1<numTerms;atr1++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
            for(int inst=0;inst<neighbors.size();inst++){
                grau += neighbors.get(inst).value;
            }
            neighbors = adjacencyListTerms[atr1].getNeighbors();
            for(int atr2=0;atr2<neighbors.size();atr2++){
                grau+= neighbors.get(atr2).value;
            }
            dTerm[atr1] = grau;
        }

        //Normalizing relation weights
        for(int inst=0;inst<(numTrain + numTest);inst++){
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                if(dDoc[inst] != 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(term).index;
                    indVal.value = (double)neighbors.get(term).value / (double)dDoc[inst];
                    adjacencyListPDocTerm[inst].AddNeighbor(indVal);
                }
            }
        }

        for(int term=0;term<numTerms;term++){
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
            for(int inst=0;inst<neighbors.size(); inst++){
                if((double)dTerm[term] != 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(inst).index;
                    indVal.value = (double)neighbors.get(inst).value / (double)dTerm[term];
                    adjacencyListPTermDoc[term].AddNeighbor(indVal);
                }
            }
        }

        for(int atr1=0;atr1<numTerms;atr1++){
            ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
            for(int atr2=0;atr2<neighbors.size();atr2++){
                if((double)dTerm[neighbors.get(atr2).index] != 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(atr2).index;
                    indVal.value = (double)neighbors.get(atr2).value / (double)dTerm[atr1];
                    adjacencyListPTermTerm[atr1].AddNeighbor(indVal);
                }
            }
        }

        dDoc = null;
        dTerm = null;
        adjancecyListDocTerm = null;
        adjacencyListTermDoc = null;    
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // 1st step: f = pf
            //Propagating class information from documents to terms
            for(int inst=0;inst<(numTrain + numTest);inst++){
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int term=0;term<neighbors.size();term++){
                        double value = fTerms[neighbors.get(term).index][classe];
                        value += neighbors.get(term).value * fDocs[inst][classe];
                        fTerms[neighbors.get(term).index][classe] = value;
                    }
                }
            }
            //Propagating class information from terms to terms
            for(int atr1=0;atr1<numTerms;atr1++){
                ArrayList<IndexValue> neighbors = adjacencyListPTermTerm[atr1].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int atr2=0;atr2<neighbors.size();atr2++){
                        fTerms[atr1][classe] += neighbors.get(atr2).value * fTerms[neighbors.get(atr2).index][classe];
                    }
                }
            }
            //Propagating class information from terms to documents
            for(int term=0;term<numTerms;term++){
                ArrayList<IndexValue> neighbors = adjacencyListPTermDoc[term].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int inst=0;inst<neighbors.size();inst++){
                        double value = fDocsTemp[neighbors.get(inst).index][classe];
                        value += neighbors.get(inst).value * fTerms[term][classe];
                        fDocsTemp[neighbors.get(inst).index][classe] = value;
                    }
                }
            }

            //Normalizing fDocsTem
            for(int inst=0;inst<(numTrain+numTest);inst++){
                double sum=0;
                for(int classe=0;classe<numClasses;classe++){
                    sum += fDocsTemp[inst][classe];
                }
                for(int classe=0;classe<numClasses;classe++){
                    double value = fDocsTemp[inst][classe];
                    if(sum == 0){
                        value = 0;
                    }else{
                        value = value / sum;
                    }
                    
                    fDocsTemp[inst][classe] = value;
                }
            }
            
            double acmDif = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
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
            
            int numIt = getNumiterations();
            
            //Analysis of stopping criteria
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
            
            numIt++;
            setNumIterations(numIt);
            
            // 2nd step f_{L} = y_{L}
            for(int inst=0;inst<numTrain;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    fDocs[inst][classe] = yDoc[inst][classe];
                    fDocsTemp[inst][classe] = 0;
                }
            }
            for(int inst=0;inst<numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    fDocsTemp[inst + numTrain][classe] = 0;
                }
            }
            
            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    fTerms[term][classe] = 0;
                }
            }
        }
        
        //Assigning labels to unlabeled document through class mass normalization
        HashMap<Integer,Double> sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
                acmProbClasse += fDocs[inst][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        for(int inst=0;inst<numTest;inst++){
            double[] classeTemp = new double[numClasses];
            for(int classe=0;classe<numClasses;classe++){
                classeTemp[classe] = (double)fDocs[numTrain + inst][classe]/(double)sumClassInformationDocs.get(classe);
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
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
    }
    
}

