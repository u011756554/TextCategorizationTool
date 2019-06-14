//*******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: February 28, 2015
// Description: This is an implementation of GNetMine algorithm (Ji et al., 2010)
//              to induce a classification model (fTerms) considering a text 
//              collection represented in a bipartite network (document-term
//              relations).
// References: - M. Ji, Y. Sun, M. Danilevsky, J. Han, J. Gao, Graph regularized
//               transductive classification on heterogeneous information
//               networks, in: Proceedings of the European Conference on Machine
//               Learning and Knowledge Discovery in Databases, Springer, 2010, 
//               pp. 570–586.
//*******************************************************************************

package TCTAlgorithms.InductiveSemiSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class GNetMine_DocTermRelations_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
    private double[][] fDocs; // Class information of documents // Class information of documents
    private double[][] fDocsTemp; // Class information of documents // Class information of documents
    private double[][] fTerms; // Class information of terms // Class information of terms (parameters of the model)
    private double[][] yDoc; // Real class information (labels) of labeled documents // Real class information (labels) of labeled documents
    private int numLabeled; // Number of labeled documents
    private int numUnlabeled; // Number of unlabeled documents
    private int numClasses; // Number of classes // Number of classes
    private int numTerms; // Number of terms // Number of terms
    private int maxNumberInterations; // Maximum number of iterations // Maximum number of iterations
    private double alphaDoc; //Parameter of the algorithms //Parameter of the algorithms
    
    //Adjacency lists of the classification algorithms
    private Neighbor[] adjacencyListSDocTerm;
    private Neighbor[] adjacencyListSTermDoc;
    
    // Constructor 
    public GNetMine_DocTermRelations_InductiveSemiSupervised(){
        setMaxNumIterations(1000);
        setAlphaDoc(0.9);
    }
    
    //Constructor
    public GNetMine_DocTermRelations_InductiveSemiSupervised(int numIterations, double alphaDoc){
        setNumIterations(numIterations);
        setAlphaDoc(alphaDoc);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numLabeled = dataTrain.numInstances();
        this.numUnlabeled = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = new double[numLabeled + numUnlabeled][numClasses];
        
        double[] dDoc = new double[numLabeled + numUnlabeled]; // Degree of documents
        double[] dTerm = new double[numTerms]; // Degree of terms
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numLabeled + numUnlabeled];
        Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
        for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
        }
        for(int term=0;term<numTerms;term++){
            adjacencyListTermDoc[term] = new Neighbor();
        }
        
        for(int inst=0;inst<numLabeled;inst++){
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
        for(int inst=0;inst<numUnlabeled;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTest.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTest.instance(inst).value(term);
                    adjancecyListDocTerm[inst + numLabeled].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = numLabeled + inst;
                    indVal.value = dataTest.instance(inst).value(term);
                    adjacencyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        
        for(int inst1=0;inst1<numLabeled;inst1++){
            double valueD = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst1].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                valueD += neighbors.get(term).value;
            }
            dDoc[inst1] = Math.sqrt(valueD);
        }
        for(int inst1=0;inst1<numUnlabeled;inst1++){
            double valueD = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst1 + numLabeled].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                valueD += neighbors.get(term).value;
            }
            dDoc[inst1 + numLabeled] = Math.sqrt(valueD);
        }
        for(int atr1=0;atr1<numTerms;atr1++){
            double valueD = 0;
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
            for(int inst1=0;inst1<neighbors.size();inst1++){
                valueD += neighbors.get(inst1).value;
            }
            dTerm[atr1] = Math.sqrt(valueD);
        }
        
        adjacencyListSDocTerm = new Neighbor[numLabeled + numUnlabeled];
        for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
            adjacencyListSDocTerm[inst] = new Neighbor();
        }
        adjacencyListSTermDoc = new Neighbor[numTerms];
        for(int term=0;term<numTerms;term++){
            adjacencyListSTermDoc[term] = new Neighbor();
        }
        for(int inst=0;inst<(numLabeled + numUnlabeled);inst++){
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
        
        double previousDiff = 0;
        int countDiff = 0;
        setNumIterations(0);
        
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
            for(int inst1=0;inst1<(numLabeled + numUnlabeled);inst1++){
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
            for(int inst=0;inst<numLabeled+numUnlabeled;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
                }    
            }

            //Analysis of stopping criteria            
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
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }

            numIt++;
            setNumIterations(numIt);
        }
    }
    
    //Function to classify unlabeled instances
    public double classifyInstance(Instance instance){
        double valueClasse = -1;
        double[] distClasses = distributionForInstance(instance);
        double maior = -3000000;
        for(int classe=0;classe<numClasses;classe++){
            if(distClasses[classe] > maior){
                 maior = distClasses[classe];
                 valueClasse = classe;
            }
        }
        return valueClasse;
    }
    
    //Function to return the classification confidences for each class
    public double[] distributionForInstance(Instance instance){
        double[] distClasses = new double[numClasses];
        for(int classe=0;classe<numClasses;classe++){
            double score = 0;
            for(int term=0;term<numTerms;term++){
               score += instance.value(term) * fTerms[term][classe];
            } 
            distClasses[classe] = score;
        }
        
        double sum = 0;
        for(int classe=0;classe<numClasses;classe++){
            sum += distClasses[classe];
        }
        if(sum != 0){
            for(int classe=0;classe<numClasses;classe++){
                distClasses[classe] = distClasses[classe] / sum;
            }    
        }
        return distClasses;
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
