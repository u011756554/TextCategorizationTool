//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import java.util.ArrayList;
import weka.core.Instances;

public class TCHN_DocDoc_ID_Transductive extends TransductiveLearner{
    private double[][] f;
    private double[][] fTemp;
    private double[][] y;
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    private double errorCorrectionRate;
    private int numMaxiterationsGlobal;
    private int numMaxiterationsLocal;
    private double minError;
    
    private double[][] matSim;
    private double[][] p;
    
   
    public TCHN_DocDoc_ID_Transductive(){
        super();
        setMaxNumIterationsGlobal(10);
        setMaxNumIterationsLocal(100);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        //f = getClassInformationIni();
        f = getClassInformation_ID(dataTrain,dataTest);
        fTemp = getClassInformation_ID(dataTrain,dataTest);
        y = getClassInformation_ID(dataTrain,dataTest);
        
        fUnlabeledDocs = new double[numTest][numClasses];
        
        if(getUse() == 0){
            
            p = new double[numTrain + numTest][numTrain + numTest];
            /*for(int inst=0;inst<(numTrain+numTest);inst++){
                p[inst] = new double[inst + 1];
            }*/
                        
            double[] d = new double[numTrain + numTest];
            
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
                d[ind1] = grau;
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
                d[ind1] = grau;
            }
            //System.out.println("Matriz D Calculada --------------------------------");
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                    
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }    
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                    
                }
            }
            
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                    
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }    
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                }
            }
            
            //System.out.println("Matriz P Calculada --------------------------------");
            matSim = null;
        }
        setUse(1);
        
         int numIt = 0;
        boolean exit = false;
        int numIterationsTotal = 0;
        while(exit == false){
            
            boolean exit2 = false;
            int numIterationsInternas = 0;
            //System.out.println("Definicao de weights utilizando documentos rotulados");
            while(exit2 == false){
                double meanError = 0;
                for(int inst1=0;inst1<numTrain;inst1++){
                    double[] estimatedClasses = classifyInstance2(inst1);

                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = y[inst1][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int inst2=0;inst2<numTest;inst2++){
                            double currentWeight = fTemp[numTrain + inst2][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * p[inst1][inst2] * error);
                            fTemp[numTrain + inst2][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                meanError = (double)meanError/(double)numTrain;
                System.out.println(numIterationsInternas + " - Error medio Int1: " + meanError);
                if((getMaxNumberIterationsLocal() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }
            
            //Analisando condição de parada para encerrar o algoritmo
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fTemp[inst][classe] - f[inst][classe]);
                    f[inst][classe] = fTemp[inst][classe];
                }    
            }
            
            //System.out.println("--------------------------");
            System.out.println("Diferenca: " + acmDif);
            //System.out.println("--------------------------");
            
            numIterationsInternas = 0;
            //System.out.println("Definicao de weights utilizando documentos nao rotulados");
            while(exit2 == false){
                double meanError = 0;
                for(int inst1=0;inst1<numTest;inst1++){
                    double[] estimatedClasses = classifyInstance2(numTrain + inst1);

                    //Calculando o error
                    for(int classe=0;classe<numClasses;classe++){
                        double error = f[numTrain + inst1][classe] - estimatedClasses[classe];
                        meanError += (error * error)/(double)2;
                        //Corrigindo os weights de cada um dos atributos conectados ao documento
                        for(int inst2=0;inst2<numTest;inst2++){
                                double currentWeight = fTemp[numTrain + inst2][classe];
                                double newWeight;
                                if(inst1 > inst2){
                                    newWeight = currentWeight + (errorCorrectionRate * p[inst1][inst2] * error);
                                }else{
                                    newWeight = currentWeight + (errorCorrectionRate * p[inst2][inst1] * error);
                                }
                                fTemp[numTrain + inst2][classe] = newWeight;
                        }
                    }
                }
                numIt++;
                numIterationsInternas++;
                meanError = (double)meanError/(double)numTest;
                System.out.println(numIterationsInternas + " - " + meanError);
                if((getMaxNumberIterationsLocal() == numIterationsInternas) || (meanError < getMinError())){
                    exit2 = true;
                }
            }

            numIterationsTotal++;
            numIt++;
            if((acmDif == 0)||(getMaxNumberIterationsGlobal() == numIterationsTotal)){
                exit = true;
            }
        }
        /******************************************************************************************
         * 
         */
        
        setNumIterations(numIt);
        //Imprimindo a matriz F
        //this.printWeightTerms("depois.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
        
        //System.out.println("Atribuindo as classes as instancias de teste");
        for(int inst=0;inst<numTest;inst++){
            int ind = -1;
            double maior = Double.MIN_VALUE;
            //System.out.print(inst + ": ");
            for(int classe=0;classe<numClasses;classe++){
                //System.out.print(fDocs[inst + numTrain][classe] + "; ");
                if(f[inst + numTrain][classe] > maior){
                    ind = classe;
                    maior = f[inst + numTrain][classe];
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
    
    public double[] classifyInstance2(int inst1){
        
        double[] classes = new double[numClasses];
        
        //System.out.println("Total: " + total);
        for(int classe=0;classe<numClasses;classe++){
            double acmPesoClasse = 0; //Acumulador para o numerator da equação considerando os documentos de treino
            for(int inst2=0;inst2<(numTrain + numTest);inst2++){ // Para todos os documentos de teste que possuam o atributo i
                if(inst1>inst2){
                    
                }else{
                    acmPesoClasse += p[inst1][inst2] * fTemp[inst2][classe];
                }    
            }
            classes[classe] = acmPesoClasse;
        }
        
        /*double min = Double.MAX_VALUE;
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
        double total = 0;
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
            
        }*/
        return classes;
    }
    
    public void setMatSim(double[][] matSim){
        this.matSim = matSim;
    }
    
    public Integer getMaxNumberIterationsLocal(){
        return numMaxiterationsLocal;
    }
    
    public Integer getMaxNumberIterationsGlobal(){
        return numMaxiterationsGlobal;
    }
    
    public double getErrorCorrectionRate(){
        return errorCorrectionRate;
    }
    
    public double getMinError(){
        return minError;
    }
    
    public void setMaxNumIterationsLocal(int numMaxiterationsLocal){
        this.numMaxiterationsLocal = numMaxiterationsLocal;
    }
    
    public void setMaxNumIterationsGlobal(int numMaxiterationsGlobal){
        this.numMaxiterationsGlobal = numMaxiterationsGlobal;
    }
    
    public void setErrorCorrectionRate(double errorCorrectionRate){
        this.errorCorrectionRate = errorCorrectionRate;
    }
    
    public void setMinError(double minError){
        this.minError = minError;
    }
}
