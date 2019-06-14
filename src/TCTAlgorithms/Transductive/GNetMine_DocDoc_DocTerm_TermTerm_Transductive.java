//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This class is not working yet
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class GNetMine_DocDoc_DocTerm_TermTerm_Transductive extends TransductiveLearner{
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
    private double lambdaDocDoc;
    private double lambdaTermTerm; //Weight of term-term relations
    private double[][] matSim;
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    //Adjacency lists of the classification algorithms
    Neighbor[] adjacencyListSDocTerm;
    Neighbor[] adjacencyListSTermDoc;
    Neighbor[] adjacencyListSTermTerm;
    private Neighbor[] adjacencyListSDocDoc;
    
    public GNetMine_DocDoc_DocTerm_TermTerm_Transductive(){
        setMaxNumIterations(1000);
        setAlphaDoc(0.9);
        setLambdaDocDoc(0.33);
        setLambdaDocTermo(0.33);
        setLambdaTermoTermo(0.33);
        
    }
    
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

        if(getUse()==0){
            adjacencyListSDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListSDocDoc = new Neighbor[numTrain + numTest];
            adjacencyListSTermDoc = new Neighbor[numTerms];
            adjacencyListSTermTerm = new Neighbor[numTerms];
            
            double[] dDocTerm = new double[numTrain + numTest]; // Degree of documents considering document-term relations
            double[] dTermDoc = new double[numTerms]; // Degree of terms considering document-term relations
            double[] dTermTerm = new double[numTerms]; //Degree of terms considering term-term relations
            double[] dDocDoc = new double[numTrain + numTest]; // degree of documents considering document-document relations
            
            Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
            Neighbor[] adjacencyListPTermDoc = new Neighbor[numTerms];
            
            //Adjcency lists to speed up classification
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
                if(grau > 0){
                    dDocTerm[indInst] = Math.sqrt(grau);
                }
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
            
            
            for(int inst1=0;inst1<numTrain;inst1++){
                double grau = 0;
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }

                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
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
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }

                }
                dDocDoc[ind1] = Math.sqrt(grau);
            }
            
            //Calculando grau da relação termo-termo
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
                adjacencyListSDocDoc[inst] = new Neighbor();
            }

            for(int term=0;term<numTerms;term++){
                adjacencyListSTermDoc[term] = new Neighbor();
                adjacencyListSTermTerm[term] = new Neighbor();
            }
            //Calculando a matriz S sDocAtr
            for(int inst=0;inst<(numTrain + numTest);inst++){
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(!((dDocTerm[inst] == 0)  || (dTermDoc[neighbors.get(term).index] == 0))){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = neighbors.get(term).value / (dDocTerm[inst] * dTermDoc[neighbors.get(term).index]);
                        adjacencyListSDocTerm[inst].AddNeighbor(indVal);
                    }
                }
            }
            //Calculando a matri S sAtrDoc
            for(int term=0;term<numTerms;term++){
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

            //Calculando a matriz S SAtrAtr
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
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value =  matSim[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
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
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
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
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
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
                        if(matSim[ind1][ind2] == 0){
                            continue;
                        }
                        value = matSim[ind1][ind2] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }else{
                        if(matSim[ind2][ind1] == 0){
                            continue;
                        }
                        value = matSim[ind2][ind1] / (dDocDoc[ind1] * dDocDoc[ind2]);
                    }
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    indVal.value = value;
                    adjacencyListSDocDoc[ind1].AddNeighbor(indVal);
                }
            }
            
            //System.out.println("Matriz S Calculada --------------------------------");
            dDocTerm=null;
            dTermDoc=null;
            dTermTerm=null;
            dDocDoc=null;

            adjancecyListDocTerm = null;
            adjacencyListPDocTerm = null;
            adjacencyListTermDoc = null;
            adjacencyListPTermDoc = null;    
        }
        setUse(1);
        
        //System.out.println("Matriz S Calculada --------------------------------");
        
        boolean exit = false;
        while(exit == false){
            //Calculando o vetor f para os termos
            //System.out.println("Calculando o vetor f para os termos");
            for(int atr1=0;atr1<numTerms-1;atr1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value1 = 0;
                    for(int inst1=0;inst1<adjacencyListSTermDoc[atr1].getNeighbors().size();inst1++){
                        value1 += adjacencyListSTermDoc[atr1].getNeighbors().get(inst1).value * fDocs[adjacencyListSTermDoc[atr1].getNeighbors().get(inst1).index][classe];
                    }
                    value1 = value1 * lambdaDocTerm;
                    double value2 = 0;
                    
                    for(int atr2=0;atr2<adjacencyListSTermTerm[atr1].getNeighbors().size();atr2++){
                        value2 += adjacencyListSTermTerm[atr1].getNeighbors().get(atr2).value * fTerms[adjacencyListSTermTerm[atr1].getNeighbors().get(atr2).index][classe];
                    }
                    value2 = 2 * value2 * lambdaTermTerm;
                    fTerms[atr1][classe] = (value1 + value2)/(lambdaDocTerm + (2*lambdaTermTerm));
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
            
            //Atribuindo os vetores temporários aos vetores de teste
            //System.out.println("Atribuindo os vetores temporários aos vetores de teste");
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
                }    
            }
            int numIt = getNumiterations();
            //System.out.println(numIt + " - Diferenca: " + acmDif);
            
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
    
    public double getLambdaTermoTermo(){
        return lambdaTermTerm;
    }
    
    public void setAlphaDoc(double alpha){
        this.alphaDoc = alpha;
    }
    
    public void setLambdaDocTermo(double lambdaDocTerm){
        this.lambdaDocTerm = lambdaDocTerm;
    }
    
    public void setLambdaDocDoc(double lambdaDocDoc){
        this.lambdaDocDoc = lambdaDocDoc;
    }
    
    public void setLambdaTermoTermo(double lambdaTermTerm){
        this.lambdaTermTerm = lambdaTermTerm;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setMatSim(double[][] matSim){
        this.matSim = matSim;
    }
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
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
        //double value = (double)1/(double)numClasses;
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
