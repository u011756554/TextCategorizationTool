//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of LPHN (Label Propagation based on 
//              Heterogeneous Network), wich is an generalization of LPBHN 
//              algorithm (Rossi et al., 2012) to perform transductive
//              classification in heterogeneous networks. This algorithms 
//              considers text collections represented in a heterogeneous 
//              network composed by document-document and document-term 
//              relations. 
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

public class LPHN_DocTerm_TermTerm_Transductive_ID extends TransductiveLearner{
    
    private double[][] fDocs; // Class information of documents
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
   
    // Adjacency lists of the algorithm
    private Neighbor[] adjacencyListPDocTerm;
    private Neighbor[] adjacencyListPTermDoc;
    private Neighbor[] adjacencyListPTermTerm;
    
    
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    
    public LPHN_DocTerm_TermTerm_Transductive_ID(){
        super();
    }
        
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        
        fDocs = getClassInformation_ID(dataTrain,dataTest);
        yDoc = getClassInformation_ID(dataTrain,dataTest);
        fDocsTemp = getClassInformation_ID(dataTrain,dataTest);;
        fUnlabeledDocs = new double[numTest][numClasses];
        fTerms = new double[numTerms][numClasses];
       
        if(getUse()==0){
            //----------Criando a lista de adjacências -------------------------//
            Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
            adjacencyListPTermDoc = new Neighbor[numTerms];
            adjacencyListPTermTerm = new Neighbor[numTerms];

            //Inicializando as listas
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
                int indInst = (int)dataTrain.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    int indAtr = term-1;
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
                int ind1 = (int)dataTest.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    if(dataTest.instance(inst).value(term) > 0){
                        int indAtr = term-1;
                        IndexValue indVal = new IndexValue();
                        indVal.index = indAtr;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjancecyListDocTerm[ind1].AddNeighbor(indVal);
                        indVal = new IndexValue();
                        indVal.index = ind1;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjacencyListTermDoc[indAtr].AddNeighbor(indVal);
                    }    
                }
            }

            //Calculando grau dos documento
            
            for(int inst=0;inst<numTrain;inst++){
                int ind1 = (int)dataTrain.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                for(int term=0;term<neighbors.size();term++){
                    neighbors.get(term).value = neighbors.get(term).value / grau;
                }
            }
            
            for(int inst=0;inst<numTest;inst++){
                int ind1 = (int)dataTest.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                for(int term=0;term<neighbors.size();term++){
                    neighbors.get(term).value = neighbors.get(term).value / grau;
                }
            }
            
            double[] dDoc = new double[numTrain + numTest];
            for(int inst=0;inst<numTrain;inst++){
                int ind1 = (int)dataTrain.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDoc[ind1] = grau;
            }
            
            for(int inst=0;inst<numTest;inst++){
                int ind1 = (int)dataTest.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDoc[ind1] = grau;
            }

            
            
            for(int atr1=0;atr1<numTerms-1;atr1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    grau += neighbors.get(inst).value;
                }
                for(int inst=0;inst<neighbors.size();inst++){
                    neighbors.get(inst).value = neighbors.get(inst).value / grau;
                }
            }
            
            double[] dTerm = new double[numTerms];
            for(int atr1=0;atr1<numTerms-1;atr1++){
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
            
            //Calculando pDocAtr
            //System.out.println("Pow1");
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

            //Calculando pAtrDoc
            //System.out.println("Pow2");
            for(int term=0;term<numTerms-1;term++){
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

            //Calculando PTermos
            for(int atr1=0;atr1<numTerms-1;atr1++){
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

            //System.out.println("Matriz P Calculada --------------------------------");
            dDoc = null;
            dTerm = null;
            adjancecyListDocTerm = null;
            adjacencyListTermDoc = null;    
        }
        
        setUse(1);
        
        //Processo de convergência iterativa
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            //1º Passo do algoritmo f = pf
            //Propagando as informações de classes dos documentos para os termos
            for(int inst=0;inst<numTrain;inst++){
                int ind = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[ind].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int term=0;term<neighbors.size();term++){
                        double value = fTerms[neighbors.get(term).index][classe];
                        value += neighbors.get(term).value * fDocs[ind][classe];
                        fTerms[neighbors.get(term).index][classe] = value;
                    }
                }
            }
            
            for(int inst=0;inst<numTest;inst++){
                int ind = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[ind].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int term=0;term<neighbors.size();term++){
                        double value = fTerms[neighbors.get(term).index][classe];
                        value += neighbors.get(term).value * fDocs[ind][classe];
                        fTerms[neighbors.get(term).index][classe] = value;
                    }
                }
            }
            
            //Propagando as informações de classes entre os termos
            for(int atr1=0;atr1<numTerms-1;atr1++){
                ArrayList<IndexValue> neighbors = adjacencyListPTermTerm[atr1].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int atr2=0;atr2<neighbors.size();atr2++){
                        fTerms[atr1][classe] += neighbors.get(atr2).value * fTerms[neighbors.get(atr2).index][classe];
                    }
                }
            }
                
            //Propagando a informação de classe dos termos para os documentos
            for(int term=0;term<numTerms-1;term++){
                ArrayList<IndexValue> neighbors = adjacencyListPTermDoc[term].getNeighbors();
                for(int classe=0;classe<numClasses;classe++){
                    for(int inst=0;inst<neighbors.size();inst++){
                        double value = fDocsTemp[neighbors.get(inst).index][classe];
                        value += neighbors.get(inst).value * fTerms[term][classe];
                        fDocsTemp[neighbors.get(inst).index][classe] = value;
                    }
                }
            }
            
            
            //Normalizando a fDocsTem pra ver o que que vira
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
            
            //Atribuindo a matriz fTemp a matriz F e calculando as diferenças
            
            double acmDif = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe] ;
                }
                //System.out.println("Parada5");
            }
            
            //System.out.println(getNumiterations() + " - Diferenca: " + acmDif);
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
            
            //2º Passo do algoritmo f_{L} = y_{L}
            //2º Passo do algoritmo f_{L} = y_{L}
            for(int inst=0;inst<numTrain;inst++){
                int ind = (int)dataTrain.instance(inst).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    fDocs[ind][classe] = yDoc[ind][classe];
                }
            }
            for(int inst=0;inst<(numTrain+numTest);inst++){
                for(int classe=0;classe<numClasses;classe++){
                    fDocsTemp[inst][classe] = 0;
                }
            }
            
            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    fTerms[term][classe] = 0;
                }
            }
        }
        
        //Atribuindo as classes as instancias de teste
        /*for(int inst=0;inst<numTest;inst++){
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
            
        }*/
        
        //Atribuindo as classes as instancias de teste
        HashMap<Integer,Double> sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
                acmProbClasse += fDocs[inst][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        //System.out.println("");
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
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
    }
    
}

