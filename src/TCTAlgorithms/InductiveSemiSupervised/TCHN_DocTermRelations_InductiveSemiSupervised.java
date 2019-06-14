//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description:
//*****************************************************************************

package TCTAlgorithms.InductiveSemiSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class TCHN_DocTermRelations_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier {

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
    
    
    public TCHN_DocTermRelations_InductiveSemiSupervised(){
        super();
        setmaxNumberGlobalIterations(100);
        setmaxNumberLocalIterations(100);
        setErrorCorrectionRate(0.1);
    }
    
    public TCHN_DocTermRelations_InductiveSemiSupervised(int maxNumberGlobalIterations, int maxNumberLocalIterations, double errorCorrectionRate){
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
        
        //----------Criando a lista de adjacências -------------------------//
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain + numTest];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain + numTest];
        
        //Inicializando as listas
        for(int inst=0;inst<(numTrain+numTest);inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            adjacencyListPDocTerm[inst] = new Neighbor();
        }
        
        for(int inst=0;inst<numTrain;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTrain.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTrain.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
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
                }    
            }
        }
        
        //Calculando grau dos documento
        double[] dDoc = new double[numTrain + numTest];
        for(int inst=0;inst<(numTrain+numTest);inst++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                grau += neighbors.get(term).value;
            }
            dDoc[inst] = grau;
        }
        
        //Calculando pDocAtr
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
        
        /******************************************************************************************
         * 
         */
        int numIt = 0;
        boolean exit = false;
        int numIterationsTotal = 0;
        while(exit == false){
            
            boolean exit2 = false;
            int numIterationsInternas = 0;
            //System.out.println("Definicao de weights utilizando documentos rotulados");
            while(exit2 == false){
                double meanError = 0;
                for(int inst=0;inst<numTrain;inst++){
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                    double[] estimatedClasses = classifyInstance2(neighbors);

                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = yDoc[inst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighbors.size();term++){
                                double currentWeight = fTerms[neighbors.get(term).index][classe];
                                double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[inst].getNeighbor(term).value * error);
                                fTerms[neighbors.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
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
                fDocsTemp[numTrain + inst] = classifyInstance2(adjacencyListPDocTerm[numTrain + inst].getNeighbors());
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
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst + numTrain].getNeighbors();
                    double[] estimatedClasses = classifyInstanceReal(neighbors);
                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = fDocs[numTrain + inst][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int term=0;term<neighbors.size();term++){
                            double currentWeight = fTerms[neighbors.get(term).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[numTrain + inst].getNeighbor(term).value * error);
                            fTerms[neighbors.get(term).index][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                meanError = (double)meanError/(double)numTest;
                //System.out.println(numIterationsInternas + " - " + meanError);
                if((getMaxNumberLocalIterations() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }
            
           
            //System.out.println("--------------------------");
            //System.out.println("Diferenca: " + acmDif);
            //System.out.println("--------------------------");
            
            numIterationsTotal++;
            numIt++;
            if((acmDif == 0)||(getMaxNumberGlobalIterations() == numIterationsTotal)){
                exit = true;
            }
        }
        
        setNumIterations(numIt);
        
    }
    
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
    
    public double[] distributionForInstance(Instance instance){
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<numTerms;term++){
            total += instance.value(term);
        }
        if(total == 0){
            return classes;
        }
        
        //System.out.println("Total: " + total);
        for(int classe=0;classe<numClasses;classe++){
            double acmPesoClasse = 0; //Acumulador para o numerator da equação considerando os documentos de treino
            for(int term=0;term<numTerms;term++){ // Para todos os documentos de teste que possuam o atributo i
                    acmPesoClasse += instance.value(term) * fTerms[term][classe];
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
            min = min * (-1);
            for(int classe=0;classe<numClasses;classe++){
                classes[classe] = classes[classe] + min;
            }    
        }
        
        double sum=0;
        for(int classe=0;classe<numClasses;classe++){
            sum += classes[classe];
        }
        
        for(int classe=0;classe<numClasses;classe++){
            classes[classe] = classes[classe]/sum;
        }
        
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
