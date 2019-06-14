//*******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: May 1, 2015
// Description: This is an implementation of LPHN (Label Propagation based on 
//              Heterogeneous Network), wich is an generalization of LPBHN 
//              algorithm (Rossi et al., 2012) to perform transductive
//              classification in heterogeneous networks. This algorithms 
//              considers text collections represented in a heterogeneous 
//              network composed by document-document and document-term (This 
//              class is not complete yet)
//              relations. Document-term matrix must have an ID as first feature.
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

public class LPHN_DocDoc_DocTerm_Transductive extends TransductiveLearner{
    
    private double[][] fDocs; // Class information of documents
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double[][] matSim; // matrix to store relatino weights of document-document relations
    
    //Ajdacency lists of the classification algorithm
    
    private Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numTest];    
    private Neighbor[] adjacencyListPTermDoc = new Neighbor[numTerms];
    private Neighbor[] adjacencyListPDocDoc = new Neighbor[numTrain + numTest];
    
    //Constructor
    public LPHN_DocDoc_DocTerm_Transductive(){
        super();
    }
        
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        
        fDocs = getClassInformation_ID(dataTrain,dataTest);
        yDoc = getClassInformation_ID(dataTrain,dataTest);
        fDocsTemp = getClassInformation_ID(dataTrain,dataTest);
        double[][] fDocsTemp2 = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        fTerms = new double[numTerms][numClasses];

        if(getUse()== 0){
            Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
            adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListTermDoc = new Neighbor[numTerms];
            adjacencyListPTermDoc = new Neighbor[numTerms];
            adjacencyListPDocDoc = new Neighbor[numTrain + numTest];
            
            //Adjacency lists to speed up learning
            for(int inst=0;inst<(numTrain + numTest);inst++){
                adjancecyListDocTerm[inst] = new Neighbor();
                adjacencyListPDocTerm[inst] = new Neighbor();
                adjacencyListPDocDoc[inst] = new Neighbor();
            }
            for(int term=0;term<numTerms;term++){
                adjacencyListTermDoc[term] = new Neighbor();
                adjacencyListPTermDoc[term] = new Neighbor();
            }

            for(int inst=0;inst<numTrain;inst++){
                int ind1 = (int)dataTrain.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    if(dataTrain.instance(inst).value(term) > 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = term;
                        indVal.value = dataTrain.instance(inst).value(term);
                        adjancecyListDocTerm[ind1].AddNeighbor(indVal);
                        indVal = new IndexValue();
                        indVal.index = ind1;
                        indVal.value = dataTrain.instance(inst).value(term);
                        adjacencyListTermDoc[term].AddNeighbor(indVal);
                    }    
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int ind1 = (int)dataTest.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    if(dataTest.instance(inst).value(term) > 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = term;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjancecyListDocTerm[ind1].AddNeighbor(indVal);
                        indVal = new IndexValue();
                        indVal.index = ind1;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjacencyListTermDoc[term].AddNeighbor(indVal);
                    }    
                }
            }

            
            
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                for(int term=0;term<neighbors.size();term++){
                    neighbors.get(term).value = neighbors.get(term).value / grau;
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                for(int term=0;term<neighbors.size();term++){
                    neighbors.get(term).value = neighbors.get(term).value / grau;
                }
            }
            
            for(int atr1=0;atr1<numTerms;atr1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    grau += neighbors.get(inst).value;
                }
                for(int inst=0;inst<neighbors.size();inst++){
                    neighbors.get(inst).value = neighbors.get(inst).value / grau;
                }
            }
            
            
            
            
            
            
            
            
            
            double[] dDoc = new double[numTrain + numTest]; // degree of documents
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                double grau = dDoc[ind1];
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1>ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1>ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                }
                dDoc[ind1] = grau;
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                double grau = dDoc[ind1];
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1>ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1>ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                }
                dDoc[ind1] = grau;
            }

            double[] dTerm = new double[numTerms]; //degree of terms
            for(int atr1=0;atr1<numTerms;atr1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    grau += neighbors.get(inst).value;
                }
                dTerm[atr1] = grau;
            }

            //Normalizing relation weights
            for(int inst=0;inst<numTrain;inst++){
                int ind = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind].getNeighbors();
                if(dDoc[ind] != 0){
                    for(int term=0;term<neighbors.size();term++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDoc[ind];
                        adjacencyListPDocTerm[ind].AddNeighbor(indVal);
                    }
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int ind = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind].getNeighbors();
                if(dDoc[ind] != 0){
                    for(int term=0;term<neighbors.size();term++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDoc[ind];
                        adjacencyListPDocTerm[ind].AddNeighbor(indVal);
                    }
                }
            }

            for(int term=1;term<numTerms;term++){
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                if((double)dTerm[term] != 0){
                    for(int inst=0;inst<neighbors.size(); inst++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(inst).index;
                        indVal.value = (double)neighbors.get(inst).value / (double)dTerm[term];
                        adjacencyListPTermDoc[term].AddNeighbor(indVal);
                    }
                }
            }

            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                if(dDoc[ind1] == 0){
                    continue;
                }
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    double value = 0;
                    if(ind1 > ind2){
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / dDoc[ind1];
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / dDoc[ind1];
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListPDocDoc[ind1].AddNeighbor(indVal);                        
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    double value = 0;
                    if(ind1 > ind2){
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / dDoc[ind1];
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / dDoc[ind1];
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListPDocDoc[ind1].AddNeighbor(indVal);   
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                if(dDoc[ind1] == 0){
                    continue;
                }
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);

                    double value = 0;
                    if(ind1 > ind2){
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / dDoc[ind1];
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / dDoc[ind1];
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListPDocDoc[ind1].AddNeighbor(indVal);   
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    double value = 0;
                    if(ind1 > ind2){
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / dDoc[ind1];
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / dDoc[ind1];
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListPDocDoc[ind1].AddNeighbor(indVal);   
                }
            }

            dDoc = null;
            dTerm = null;
            adjancecyListDocTerm = null;
            adjacencyListTermDoc = null;
        }
        setUse(1);
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // 1st step: f = pf
            // Propagating class information from documents to terms
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
            
            //System.out.println("Parada1");
            
            //Propagating class information from terms to documents
            for(int term=0;term<numTerms;term++){
                ArrayList<IndexValue> neighborsAtr = adjacencyListPTermDoc[term].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int inst=0;inst<neighborsAtr.size();inst++){
                        double value = fDocsTemp[neighborsAtr.get(inst).index][classe];
                        value += neighborsAtr.get(inst).value * fTerms[term][classe];
                        fDocsTemp[neighborsAtr.get(inst).index][classe] = value;
                    }
                }
            }
            
            //Propagatin class information from documents to documents
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                ArrayList<IndexValue> neighborsDoc = adjacencyListPDocDoc[inst1].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<neighborsDoc.size();inst2++){
                            value += neighborsDoc.get(inst2).value * fDocs[neighborsDoc.get(inst2).index][classe];
                    }
                    fDocsTemp2[inst1][classe] = value;
                }
            }
            
            
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int classe=0;classe<numClasses;classe++){
                    fDocsTemp[inst1][classe] += fDocsTemp2[inst1][classe];
                }
            }
            
            //Normalizing fDocTemp
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
                    fDocs[inst][classe] = fDocsTemp[inst][classe] ;
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
                int ind = (int)dataTrain.instance(inst).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    fDocs[ind][classe] = yDoc[ind][classe];
                }
            }
            
            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    fTerms[term][classe] = 0;
                }
            }
        }
        
        //Assingning labels to unlabaled documents through class mass normalizatino
        HashMap<Integer,Double> sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
                acmProbClasse += fDocs[inst][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        for(int inst=0;inst<numTest;inst++){
            int indTeste = (int)dataTest.instance(inst).value(0);
            double[] classeTemp = new double[numClasses];
            for(int classe=0;classe<numClasses;classe++){
                classeTemp[classe] = (double)fDocs[indTeste][classe]/(double)sumClassInformationDocs.get(classe);
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
    
    public void setMatSim(double[][] matSim){
        this.matSim = matSim;
    }
    
}

