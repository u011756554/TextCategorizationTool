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
import weka.core.Attribute;
import weka.core.Instances;

public class TCHN_DocTerm_TermTerm_Transductive_ID extends TransductiveLearner {

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
    
    
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    Neighbor[] adjancecyListDocTerm; 
    Neighbor[] adjacencyListPDocTerm;
    
    public TCHN_DocTerm_TermTerm_Transductive_ID(){
        super();
        setmaxNumberGlobalIterations(100);
        setmaxNumberLocalIterations(100);
        setErrorCorrectionRate(0.1);
    }
    
    public TCHN_DocTerm_TermTerm_Transductive_ID(int maxNumberGlobalIterations, int maxNumberLocalIterations, double errorCorrectionRate){
        setmaxNumberGlobalIterations(maxNumberGlobalIterations);
        setmaxNumberLocalIterations(maxNumberLocalIterations);
        setErrorCorrectionRate(errorCorrectionRate);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fDocs = getClassInformation_ID(dataTrain,dataTest);
        yDoc = getClassInformation_ID(dataTrain,dataTest);
        fTerms = new double[numTerms][numClasses];
        fDocsTemp = getClassInformation_ID(dataTrain,dataTest);
        fUnlabeledDocs = new double[numTest][numClasses];
        
        if(getUse() == 0){
            //----------Criando a lista de adjacências -------------------------//
            adjancecyListDocTerm = new Neighbor[numTrain + numTest];
            adjacencyListPDocTerm = new Neighbor[numTrain + numTest];

            //Inicializando as listas
            for(int inst=0;inst<(numTrain+numTest);inst++){
                adjancecyListDocTerm[inst] = new Neighbor();
                adjacencyListPDocTerm[inst] = new Neighbor();
            }

            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    int indAtr = term - 1;
                    if(dataTrain.instance(inst).value(term) > 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = indAtr;
                        indVal.value = dataTrain.instance(inst).value(term);
                        adjancecyListDocTerm[indInst].AddNeighbor(indVal);
                    }    
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                for(int term=1;term<numTerms;term++){
                    int indAtr = term - 1;
                    if(dataTest.instance(inst).value(term) > 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = indAtr;
                        indVal.value = dataTest.instance(inst).value(term);
                        adjancecyListDocTerm[indInst].AddNeighbor(indVal);
                    }    
                }
            }
            //Calculando grau dos documento
            double[] dDoc = new double[numTrain + numTest];
            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDoc[indInst] = grau;
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    grau += neighbors.get(term).value;
                }
                dDoc[indInst] = grau;
            }

            //Calculando pDocAtr
            for(int inst=0;inst<numTrain;inst++){
                int indInst = (int)dataTrain.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(dDoc[indInst] != 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDoc[indInst];
                        adjacencyListPDocTerm[indInst].AddNeighbor(indVal);
                    }
                }
            }
            for(int inst=0;inst<numTest;inst++){
                int indInst = (int)dataTest.instance(inst).value(0);
                ArrayList<IndexValue> neighbors = adjancecyListDocTerm[indInst].getNeighbors();
                for(int term=0;term<neighbors.size();term++){
                    if(dDoc[indInst] != 0){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term).index;
                        indVal.value = (double)neighbors.get(term).value / (double)dDoc[indInst];
                        adjacencyListPDocTerm[indInst].AddNeighbor(indVal);
                    }
                }
            }
            adjancecyListDocTerm = null;
            dDoc = null;
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
                    int indInst = (int)dataTrain.instance(inst).value(0);
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[indInst].getNeighbors();
                    double[] estimatedClasses = classifyInstance2(neighbors);

                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = yDoc[indInst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighbors.size();term++){
                                double currentWeight = fTerms[neighbors.get(term).index][classe];
                                double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[indInst].getNeighbor(term).value * error);
                                fTerms[neighbors.get(term).index][classe] = newWeight;
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
                int indInst = (int)dataTest.instance(inst).value(0);
                fDocsTemp[indInst] = classifyInstance2(adjacencyListPDocTerm[indInst].getNeighbors());
            }
            
            //Analisando condição de parada para encerrar o algoritmo
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[inst][classe] - fDocs[inst][classe]);
                    fDocs[inst][classe] = fDocsTemp[inst][classe];
                }    
            }
            
            
            //System.out.println("Definicao de weights utilizando documentos nao rotulados");
            exit2 = false;
            numIterationsInternas = 0;
            while(exit2 == false){
                double meanError = 0;
                for(int inst=0;inst<numTest;inst++){
                    int indInst = (int)dataTest.instance(inst).value(0);
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[indInst].getNeighbors();
                    double[] estimatedClasses = classifyInstanceReal(neighbors);
                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = fDocs[indInst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[indInst].getNeighbor(term).value * error);
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
            
            
            numIterationsInternas = 0;
            double averageErrorTerm = 0;
            //System.out.println("Minimizando diferença entre termos");    
            exit2 = false;
            while(exit2 == false){
                averageErrorTerm = 0;
                for(int termo1=0;termo1<numTerms-1;termo1++){
                    ArrayList<IndexValue> neighbors = adjacencyListTerms[termo1].getNeighbors();
                    for(int classe=0;classe<numClasses;classe++){
                        for(int termo2=0;termo2<neighbors.size();termo2++){
                            double error = (fTerms[neighbors.get(termo2).index][classe] - fTerms[termo1][classe]);
                            averageErrorTerm += (error * error)/(double)2;
                            fTerms[termo1][classe] = fTerms[termo1][classe] + (errorCorrectionRate *  neighbors.get(termo2).value * neighbors.get(termo2).value * error);
                        }
                    }
                }
                numIterationsInternas++;
                numIterationsTotal++;
                averageErrorTerm = (double)averageErrorTerm/(double)numTerms;
                //System.out.println(numIterationsInternas + " - Error Medio Termo: " + averageErrorTerm);

                if((averageErrorTerm < getMinError()) || numIterationsInternas > getMaxNumberLocalIterations()){
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
            int indInst = (int)dataTest.instance(inst).value(0);
            fDocs[indInst] = classifyInstance(adjacencyListPDocTerm[indInst].getNeighbors());;
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
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
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
    
}
