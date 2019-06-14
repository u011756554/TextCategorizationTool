//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class TCHN_DocDoc_DocTerm_Transductive extends TransductiveLearner {

    private double[][] fDocs; // Class information of documents
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberGlobalIterations;
    private int maxNumberLocalIterations;
    private double errorCorrectionRate;
    private double minError;
    private boolean usarNaoRotulados;
    private Neighbor[] adjacencyListPDocTerm;
    private Neighbor[] adjacencyListPTermDoc;
    private Neighbor[] listaAdjDocDoc;
    
    
    private double[][] matSim;
    
    public TCHN_DocDoc_DocTerm_Transductive(){
        super();
        setmaxNumberGlobalIterations(100);
        setmaxNumberLocalIterations(100);
        setErrorCorrectionRate(0.1);
    }
    
    public TCHN_DocDoc_DocTerm_Transductive(int maxNumberGlobalIterations, int maxNumberLocalIterations, double errorCorrectionRate){
        setmaxNumberGlobalIterations(maxNumberGlobalIterations);
        setmaxNumberLocalIterations(maxNumberLocalIterations);
        setErrorCorrectionRate(errorCorrectionRate);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation(dataTrain,dataTest);
        yDoc = getClassInformation(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = getClassInformation(dataTrain,dataTest);
        fUnlabeledDocs = new double[numTest][numClasses];
        
        
        
        if(getUse()== 0){
            Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
            Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
            adjacencyListPTermDoc = new Neighbor[numTerms];
            listaAdjDocDoc = new Neighbor[numTrain + numTest];
            //Inicializando as listas
            for(int inst=0;inst<(numTrain + numTest);inst++){
                adjancecyListDocTerm[inst] = new Neighbor();
                adjacencyListPDocTerm[inst] = new Neighbor();
                listaAdjDocDoc[inst] = new Neighbor();
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


            //Calculando a matriz P

            //Calculando grau dos documento considerando termos
            double[] dDocTerm = new double[numTrain + numTest];
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                double grau = dDocTerm[ind1];
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDocTerm[ind1] = grau;
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                double grau = dDocTerm[ind1];
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind1].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDocTerm[ind1] = grau;
            }
            
            /*//Calculando grau entre os documentos
            double[] dDocDoc = new double[numTrain + numTest];
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                double grau = 0;
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
                dDocDoc[ind1] = grau;
            }
            
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                double grau = 0;
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
                dDocDoc[ind1] = grau;
            }*/
            
            
            /*double[] dTerm = new double[numTerms];
            for(int atr1=0;atr1<numTerms;atr1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTermDoc[atr1].getNeighbors();
                for(int inst=0;inst<neighbors.size();inst++){
                    grau += neighbors.get(inst).value;
                }
                dTerm[atr1] = grau;
            }*/

            //Calculando pDocAtr
            //System.out.println("Pow1");
            for(int inst=0;inst<numTrain;inst++){
                int ind = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind].getNeighbors();
                if(dDocTerm[ind] != 0){
                    for(int term=0;term<neighbors.size();term++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDocTerm[ind];
                        adjacencyListPDocTerm[ind].AddNeighbor(indVal);
                        IndexValue indVal2 = new IndexValue();
                        indVal2.index = ind;
                        indVal2.value = (double)neighbors.get(term).value / (double)dDocTerm[ind];
                        adjacencyListPTermDoc[neighbors.get(term).index].AddNeighbor(indVal2);
                    }
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int ind = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[ind].getNeighbors();
                if(dDocTerm[ind] != 0){
                    for(int term=0;term<neighbors.size();term++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDocTerm[ind];
                        adjacencyListPDocTerm[ind].AddNeighbor(indVal);
                        IndexValue indVal2 = new IndexValue();
                        indVal2.index = ind;
                        indVal2.value = (double)neighbors.get(term).value / (double)dDocTerm[ind];
                        adjacencyListPTermDoc[neighbors.get(term).index].AddNeighbor(indVal2);
                    }
                }
            }

            //System.out.println("Matriz P Calculada --------------------------------");
            dDocTerm = null;
            //dTerm = null;
            adjancecyListDocTerm = null;
            adjacencyListTermDoc = null;
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    double weight;
                    if(ind1>ind2){
                        weight = matSim[ind1][ind2];
                    }else{
                        weight = matSim[ind2][ind1];
                    }
                    /*if((weight==0) || (dDocDoc[ind1]==0)){
                        continue;
                    }*/
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    //indVal.value = weight / dDocDoc[ind1];
                    indVal.value = weight;
                    listaAdjDocDoc[ind1].AddNeighbor(indVal);
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    double weight;
                    if(ind1>ind2){
                        weight = matSim[ind1][ind2];
                    }else{
                        weight = matSim[ind2][ind1];
                    }
                    /*if((weight==0) || (dDocDoc[ind1]==0)){
                        continue;
                    }*/
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    //indVal.value = weight / dDocDoc[ind1];
                    indVal.value = weight;
                    listaAdjDocDoc[ind1].AddNeighbor(indVal);
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    double weight;
                    if(ind1>ind2){
                        weight = matSim[ind1][ind2];
                    }else{
                        weight = matSim[ind2][ind1];
                    }
                    /*if((weight==0) || (dDocDoc[ind1]==0)){
                        continue;
                    }*/
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    //indVal.value = weight / dDocDoc[ind1];
                    indVal.value = weight;
                    listaAdjDocDoc[ind1].AddNeighbor(indVal);
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    double weight;
                    if(ind1>ind2){
                        weight = matSim[ind1][ind2];
                    }else{
                        weight = matSim[ind2][ind1];
                    }
                    /*if((weight==0) || (dDocDoc[ind1]==0)){
                        continue;
                    }*/
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind2;
                    //indVal.value = weight / dDocDoc[ind1];
                    indVal.value = weight;
                    listaAdjDocDoc[ind1].AddNeighbor(indVal);
                }
            }
            //dDocDoc = null;            
        }
        
        setUse(1);
        /******************************************************************************************
         * 
         */
        int numIt = 0;
        boolean exit = false;
        int numIterationsTotal = 0;
        int numIterationsGlobal = 0;
        while(exit == false){
            
            boolean exit2 = false;
            int numIterationsInternas = 0;
            //System.out.println("Definicao de weights utilizando documentos rotulados");
            while(exit2 == false){
                double meanError = 0;
                for(int inst=0;inst<numTrain;inst++){
                    int ind = (int)dataTrain.instance(inst).value(0);
                    ArrayList<IndexValue> neighborsAtr = adjacencyListPDocTerm[ind].getNeighbors();
                    double[] estimatedClasses = classifyInstance2(neighborsAtr);

                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = yDoc[ind][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighborsAtr.size();term++){
                                double currentWeight = fTerms[neighborsAtr.get(term).index][classe];
                                double newWeight = currentWeight + (errorCorrectionRate * neighborsAtr.get(term).value * error);
                                fTerms[neighborsAtr.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                numIterationsTotal++;
                meanError = (double)meanError/(double)numTrain;
                //System.out.println(numIterationsInternas + " - " + meanError);
                if((getMaxNumberLocalIterations() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }
            
            //Imprimindo a matriz F
            //this.printWeightTerms("antes.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
            
            //System.out.println("Definindo os rótulos para os documentos de teste");
            for(int inst=0;inst<numTest;inst++){
                int ind = (int)dataTest.instance(inst).value(0);
                fDocsTemp[ind] = classifyInstance2(adjacencyListPDocTerm[ind].getNeighbors());
            }
            
            //Analisando condição de parada para encerrar o algoritmo
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
                }    
            }
            
            numIterationsInternas = 0;
            double averageErrorDoc = 0;
            //System.out.println("Minimizando diferença entre documentos");    
            exit2 = false;
            while(exit2 == false){
                averageErrorDoc = 0;
                for(int inst1=0;inst1<numTest;inst1++){
                    int ind = (int)dataTest.instance(inst1).value(0);
                    ArrayList<IndexValue> neighborsDoc = listaAdjDocDoc[ind].getNeighbors();
                    for(int classe=0;classe<numClasses;classe++){
                        for(int inst2=0;inst2<neighborsDoc.size();inst2++){
                            try{
                                double error = (fDocsTemp[neighborsDoc.get(inst2).index][classe] - fDocsTemp[ind][classe]);
                                averageErrorDoc += (error*error)/(double)2;
                                fDocsTemp[ind][classe] = fDocsTemp[ind][classe] + (errorCorrectionRate *  neighborsDoc.get(inst2).value * neighborsDoc.get(inst2).value * error);
                            }catch(Exception e){ }
                        }    
                    }
                    
                }
                numIterationsInternas++;
                numIterationsTotal++;
                averageErrorDoc = (double)averageErrorDoc/(double)numTerms;
                //System.out.println(numIterationsInternas + " - Error Medio Doc: " + averageErrorDoc);
                if((averageErrorDoc < getMinError()) || numIterationsInternas > getMaxNumberLocalIterations()){
                    exit2 = true;
                }
            }
            
            //Padronizando o fDocs dos documentos não rotulados
            for(int inst=0;inst<numTest;inst++){
                int ind = (int)dataTest.instance(inst).value(0);
                double min = Double.MAX_VALUE;
                for(int classe=0;classe<numClasses;classe++){
                    if(fDocsTemp[ind][classe] < min){
                        min = fDocsTemp[ind][classe];
                    }
                }
                if(min < 0){
                    for(int classe=0;classe<numClasses;classe++){
                        double value = fDocsTemp[ind][classe];
                        value += Math.abs(min);
                        fDocsTemp[ind][classe] = value;
                    }
                }
                double sum = 0;
                for(int classe=0;classe<numClasses;classe++){
                    sum += fDocsTemp[ind][classe];
                }
                for(int classe=0;classe<numClasses;classe++){
                    double value = fDocsTemp[ind][classe] / sum;
                    fDocs[ind][classe] = value;
                    fDocsTemp[ind][classe] = value;
                }
            }
            
            //System.out.println("Definicao de weights utilizando documentos nao rotulados");
            exit2 = false;
            numIterationsInternas = 0;
            while(exit2 == false){
                double meanError = 0;
                for(int inst=0;inst<numTest;inst++){
                    int ind = (int)dataTest.instance(inst).value(0);
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[ind].getNeighbors();
                    double[] estimatedClasses = classifyInstanceReal(neighbors);
                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = fDocs[ind][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * neighbors.get(term).value * error);
                            fTerms[neighbors.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                numIterationsTotal++;
                meanError = (double)meanError/(double)numTest;
                //System.out.println(numIterationsInternas + " - " + meanError);
                if((getMaxNumberLocalIterations() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }
            
            
            
           
            //System.out.println("==================================");
            //System.out.println(numIterationsGlobal + " - Diferenca: " + acmDif);
            //System.out.println("==================================");
            
            numIterationsGlobal++;
            if((acmDif == 0)||(getMaxNumberGlobalIterations() == numIterationsGlobal)){
                exit = true;
            }
        }
        /******************************************************************************************
         * 
         */
        
        setNumIterations(numIterationsTotal);
        //Imprimindo a matriz F
        //this.printWeightTerms("depois.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
        
        //System.out.println("Definindo os rótulos para os documentos de teste");
        for(int inst=0;inst<numTest;inst++){
            int ind  = (int)dataTest.instance(inst).value(0);
            fDocs[ind] = classifyInstance(adjacencyListPDocTerm[ind].getNeighbors());;
        }
        
        //System.out.println("Atribuindo as classes as instancias de teste");
        for(int inst=0;inst<numTest;inst++){
            int indInst  = (int)dataTest.instance(inst).value(0);
            int ind = -1;
            double maior = Double.MIN_VALUE;
            //System.out.print(inst + ": ");
            for(int classe=0;classe<numClasses;classe++){
                //System.out.print(fDocs[inst + numTrain][classe] + "; ");
                if(fDocs[indInst][classe] > maior){
                    ind = classe;
                    maior = fDocs[inst + numTrain][classe];
                }
            }
            //System.out.println();
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
    
    public double[] classifyInstance(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        //System.out.println("Total: " + total);
        for(int classe=0;classe<numClasses;classe++){
            double acmPesoClasse = 0; //Acumulador para o numerator da equação considerando os documentos de treino
            for(int term=0;term<neighbors.size();term++){ // Para todos os documentos de teste que possuam o atributo i
                    acmPesoClasse += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmPesoClasse;
        }

        double maior = Double.MIN_VALUE;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] > maior){
                maior = classes[classe];
                index = classe;
            }
        }
        for(int classe=0;classe<numClasses;classe++){
            classes[classe] = 0;
        }
        if(index == -1){
            return classes;
        }
        classes[index] = 1;
        return classes;
    }
    
    public double[] classifyInstanceReal(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        //System.out.println("Total: " + total);
        for(int classe=0;classe<numClasses;classe++){
            double acmPesoClasse = 0; //Acumulador para o numerator da equação considerando os documentos de treino
            for(int term=0;term<neighbors.size();term++){ // Para todos os documentos de teste que possuam o atributo i
                    acmPesoClasse += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmPesoClasse;
        }

        return classes;
    }
    
    public double[] classifyInstance2(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        //System.out.println("Total: " + total);
        for(int classe=0;classe<numClasses;classe++){
            double acmPesoClasse = 0; //Acumulador para o numerator da equação considerando os documentos de treino
            for(int term=0;term<neighbors.size();term++){ // Para todos os documentos de teste que possuam o atributo i
                    acmPesoClasse += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acmPesoClasse;
        }
        
        double min = Double.MAX_VALUE;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] < min){
                min = classes[classe];
            }
        }
        if(min < 0){
            for(int classe=0;classe<numClasses;classe++){
                double value = classes[classe];
                classes[classe] = value + Math.abs(min);
            }
        }
        total = 0;
        for(int classe=0;classe<numClasses;classe++){
            total += classes[classe];
        }
        for(int classe=0;classe<numClasses;classe++){
            if(total == 0){
                classes[classe] = 0;
            }else{
                double value = classes[classe];
                classes[classe] = value / total;
            }
            
        }
        return classes;
    }
    
    
    public Integer getMaxNumberGlobalIterations(){
        return maxNumberGlobalIterations;
    }
    
    public Integer getMaxNumberLocalIterations(){
        return maxNumberLocalIterations;
    }
    
    public double getErrorCorrectionRate(){
        return errorCorrectionRate;
    }
    
    public double getMinError(){
        return minError;
    }
    
    public boolean getUsarNaoRotulados(){
        return usarNaoRotulados;
    }
    
    public void setmaxNumberGlobalIterations(int maxNumberGlobalIterations){
        this.maxNumberGlobalIterations = maxNumberGlobalIterations;
    }
    
    public void setmaxNumberLocalIterations(int maxNumberLocalIterations){
        this.maxNumberLocalIterations = maxNumberLocalIterations;
    }
    
    public void setErrorCorrectionRate(double errorCorrectionRate){
        this.errorCorrectionRate = errorCorrectionRate;
    }
    
    public void setMinError(double minError){
        this.minError = minError;
    }
    
    public void setUsarNaoRotulados(boolean usarNaoRotulados){
        this.usarNaoRotulados = usarNaoRotulados;
    }
    
    public void printWeightTerms(String fileName, Attribute classAtt, Instances data, double[][] f){
        try{
            FileWriter outputFile = new FileWriter(fileName);
            outputFile.write("Termo;");
            
            numClasses = classAtt.numValues();
            for(int classe=0;classe<numClasses;classe++){
                outputFile.write(classAtt.value(classe) + ";");
            }
            outputFile.write("\n");
            
            for(int term=0;term<numTerms;term++){
                outputFile.write(data.attribute(term).name() + ";");
                for(int classe=0;classe<numClasses;classe++){
                    outputFile.write(f[term][classe] + ";");
                }
                outputFile.write("\n");
            }
            outputFile.close();
        }catch(Exception e){
            System.err.println("Error when saving class information of terms for classes.");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public void setMatSim(double[][] matSim){
        this.matSim = matSim;
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
