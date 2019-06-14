/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author rafael
 */
public class Results2 implements Serializable{
    
    
    private boolean[][] complete;
    private double[][] accuracies;
    private double[][] microPrecisions;
    private double[][] microRecalls;
    private double[][] macroPrecisions;
    private double[][] macroRecalls;
    
    private double[][][] precision;
    private double[][][] recall;
    private double[][][] f1;
    
    
    private long[][] buildingTimes;
    private long[][] classificationTimes;
    private int[][] numIterations;
    
    String type;
    
    File output;
    
    ArrayList<String> classes;
    
    private int numReps;
    private int numFolds;
    
    public Results2(File output, int numLines, int numColumns, String type, ArrayList<String> classes){
        
        this.output = output;
        this.classes = classes;
        this.type = type;
        
        numReps = numLines;
        numFolds = numColumns;
        
        accuracies = new double[numLines][numColumns];
        microPrecisions = new double[numLines][numColumns];
        microRecalls = new double[numLines][numColumns];
        macroPrecisions = new double[numLines][numColumns];
        macroRecalls = new double[numLines][numColumns];
        
        f1 = new double[classes.size()][numLines][numColumns];
        precision = new double[classes.size()][numLines][numColumns];
        recall = new double[classes.size()][numLines][numColumns];
        
        buildingTimes = new long[numLines][numColumns];
        classificationTimes = new long[numLines][numColumns];
        numIterations = new int[numLines][numColumns];
        complete = new boolean[numLines][numColumns];
        
        for(int lin=0;lin<numLines;lin++){
            for(int col=0;col<numColumns;col++){
                accuracies[lin][col] = -1;
                microPrecisions[lin][col] = -1;
                microRecalls[lin][col] = -1;
                macroPrecisions[lin][col] = -1;
                macroRecalls[lin][col] = -1;
                buildingTimes[lin][col] = -1;
                classificationTimes[lin][col] = -1;
                numIterations[lin][col] = 0;
                complete[lin][col] = false;
            }
        }
        
        for(int classe=0;classe<classes.size();classe++){
            for(int lin=0;lin<numLines;lin++){
                for(int col=0;col<numColumns;col++){
                    precision[classe][lin][col] = -1;
                    recall[classe][lin][col] = -1;
                    f1[classe][lin][col] = -1;
                }
            }
        }
        
    }    
        
    public void setType(String type){
        this.type = type;
    }
    
    public synchronized void setComplete(int lin, int col, boolean value){
        complete[lin][col] = value;
        
        boolean all = true;
        for(int rep=0; rep<numReps; rep++){
            for(int fold=0;fold<numFolds;fold++){
                if(complete[rep][fold] == false){
                    all = false;
                }
            }
        }
        
        if(all == true){
            saveResults();
        }else{
            saveObject();
        }
    }
    
    
    public void setF1(int classe, int lin, int col, double value){
        f1[classe][lin][col] = value;
    }
    
    public void setPrecision(int classe, int lin, int col, double value){
        precision[classe][lin][col] = value;
    }
    
    public void setRecall(int classe, int lin, int col, double value){
        recall[classe][lin][col] = value;
    }
    
    public void setAccuracies(int lin, int col, double value){
        accuracies[lin][col] = value;
    }    
    
    public void setMicroPrecision(int lin, int col, double value){
        microPrecisions[lin][col] = value;
    }
    
    public void setMicroRecall(int lin, int col, double value){
        microRecalls[lin][col] = value;
    }
    
    public void setMacroPrecision(int lin, int col, double value){
        macroPrecisions[lin][col] = value;
    }
    
    public void setMacroRecall(int lin, int col, double value){
        macroRecalls[lin][col] = value;
    }
    
    public void setBuildingTime(int lin, int col, long value){
        buildingTimes[lin][col] = value;
    }
    
    public void setClassificationTime(int lin, int col, long value){
        classificationTimes[lin][col] = value;
    }
    
    public void setNumIterations(int lin, int col, int value){
        numIterations[lin][col] = value;
    }
    
    public String getType(){
        return type;
    }
    
    public double getPrecision(int classe, int lin, int col){
        return precision[classe][lin][col];
    }
    
    public double getRecall(int classe, int lin, int col){
        return recall[classe][lin][col];
    }
    
    public double getF1(int classe, int lin, int col){
        return f1[classe][lin][col];
    }
    
    public double getAccuracies(int lin, int col){
        return accuracies[lin][col];
    }
    
    public double getMicroPrecision(int lin, int col){
        return microPrecisions[lin][col];
    }
    
    public double getMicroRecall(int lin, int col){
        return microRecalls[lin][col];
    }
    
    public double getMacroPrecision(int lin, int col){
        return macroPrecisions[lin][col];
    }
    
    public double getMacroRecall(int lin, int col){
        return macroRecalls[lin][col];
    }
    
    public long getBuildingTime(int lin, int col){
        return buildingTimes[lin][col];
    }
    
    public long getClassificationTime(int lin, int col){
        return classificationTimes[lin][col];
    }
    
    public int getNumIterations(int lin, int col){
        return numIterations[lin][col];
    }
    
    public boolean getComplete(int lin, int col){
        return complete[lin][col];
    }
    
    public synchronized void computeEvaluationMeasures(Integer[][] confusionMatrix, int numClasses, int rep, int fold){
        
        int tp=0;
        int total=0;
        for(int i=0;i<numClasses;i++){
            for(int j=0;j<numClasses;j++){
                if(i==j){
                    tp += confusionMatrix[i][j];
                }
                total += confusionMatrix[i][j];
            }
        }
        double accuracy = ((double)tp/(double)total) * 100;
        this.setAccuracies(rep, fold, accuracy);
        
        double microPrecTotNum = 0, microPrecTotDen = 0, microRevTotNum = 0, microRevTotDen = 0, macroPrecTot = 0, macroRevTot = 0;
        for(int classe=0;classe<numClasses;classe++){
            
            int TPi = confusionMatrix[classe][classe];
            int FPi = 0;
            for(int k=0;k<numClasses;k++){
                if(k!=classe){
                    FPi += confusionMatrix[classe][k];
                }
            }
            microPrecTotNum += TPi;
            microPrecTotDen += (TPi + FPi);
            if((TPi + FPi)==0){
                macroPrecTot += 0;
            }else{
                macroPrecTot += (double)TPi/(double)(TPi + FPi);
            }

            
            int FNi=0;
            for(int k=0;k<numClasses;k++){
                if(k!=classe){
                    FNi += confusionMatrix[k][classe];
                }
            }
            microRevTotNum += TPi;
            microRevTotDen += TPi + FNi;
            if((TPi + FNi)==0){
                macroRevTot += 0;
            }else{
                macroRevTot += (double)TPi/(double)(TPi + FNi);
            }
            
            double precision = 0;
            if((TPi+FPi) > 0){
                precision = (double)TPi/(double)(TPi+FPi);
            }        
            setPrecision(classe,rep,fold,precision);
            
            double recall = 0;
            if((TPi+FNi) > 0){
                recall = (double)TPi/(double)(TPi+FNi);
            }
            setRecall(classe,rep,fold,recall);
            
            double f1 = 0;
            if((precision + recall) > 0){
                f1 = (2*precision*recall)/(precision + recall);
            }
            setF1(classe,rep,fold,f1);
            
        }
        double microPrecision = (double)microPrecTotNum/(double)microPrecTotDen;
        this.setMicroPrecision(rep, fold, microPrecision);
        double microRecall = (double)microRevTotNum/(double)microRevTotDen;
        this.setMicroRecall(rep, fold, microRecall);
        double macroPrecision = (double) macroPrecTot / (double) numClasses;
        this.setMacroPrecision(rep, fold, macroPrecision);
        double macroRecall = (double)macroRevTot/(double)numClasses;
        this.setMacroRecall(rep, fold, macroRecall);
        
        System.out.println(output.getAbsolutePath() + "-------------------------------------------------------------------------------");
        if(this.getType().equals("Transductive")){
            System.out.println("\nRepetition: " + (rep + 1) + " --------------------------------------------------------------------");
        }else if(this.getType().equals("InductiveSupervised")){
            System.out.println("\nRepetition: " + (rep + 1) + " --------------------------------------------------------------------");
            System.out.println("Fold: " + (fold + 1) + " --------------------------------------------------------------------");
        }else{
            System.out.println("\nFold: " + (fold + 1) + " --------------------------------------------------------------------");
            System.out.println("Repetition: " + (rep + 1) + " --------------------------------------------------------------------");
        }
        
        //Printing Confusion Matrix
        for(int i1=0;i1<numClasses;i1++){
            for(int i2=0;i2<numClasses;i2++){
                System.out.print(confusionMatrix[i1][i2] + "\t");
            }
            System.out.println();
        }
        
    }
    
    public void saveObject(){
        try{
            ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(output.getAbsolutePath() + ".tmp"));
            objOutput.writeObject(this);
            objOutput.close();
        }catch(Exception e){
            System.err.println("Error when saving the results");
        } 
    }
    
    public void saveResults(){
        
        File outputTemp = new File(output.getAbsolutePath() + ".tmp");
        if(outputTemp.exists()){
            outputTemp.delete();
        }
        
        double acmAccuracy = 0;
        double acmMicroPrecision = 0;
        double acmMicroRecall = 0;
        double acmMacroPrecision = 0;
        double acmMacroRecall = 0;
        double acmBuildingTime = 0;
        double acmClassificationTime = 0;
        int acmIterations = 0;
        double acmSD = 0;
        
        try{
            FileWriter outputResults = new FileWriter(output.getAbsolutePath() + ".txt");
            
            //Saving results per fold
            if(this.getType().equals("InductiveSupervised")){
                for(int rep=0;rep<numReps;rep++){
                   outputResults.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                   for(int fold=0;fold<numFolds;fold++){
                        outputResults.write("Fold: " + (fold + 1) + " --------------------------------------------------------------------\n");
                        outputResults.write("Accuracy (%): " + this.getAccuracies(rep, fold) + "\n");
                        acmAccuracy += this.getAccuracies(rep, fold);
                        outputResults.write("Error (%): " + (100 - this.getAccuracies(rep, fold)) + "\n");
                        for(int classe=0;classe<classes.size();classe++){
                            outputResults.write("Precision-" + classes.get(classe) + ": " + this.getPrecision(classe,rep, fold) + "\n");
                            outputResults.write("Recall-" + classes.get(classe) + ": " + this.getRecall(classe,rep, fold) + "\n");
                            outputResults.write("F1-" + classes.get(classe) + ": " + this.getF1(classe,rep, fold) + "\n");
                        }
                        outputResults.write("Micro-Precision: " + this.getMicroPrecision(rep, fold) + "\n");
                        acmMicroPrecision += this.getMicroPrecision(rep, fold);
                        outputResults.write("Micro-Recall: " + this.getMicroRecall(rep, fold) + "\n");
                        acmMicroRecall += this.getMicroRecall(rep, fold);
                        outputResults.write("Macro-Precision: " + this.getMacroPrecision(rep, fold) + "\n");
                        acmMacroPrecision += this.getMacroPrecision(rep, fold);
                        outputResults.write("Macro-Recall: " + this.getMacroRecall(rep, fold) + "\n");
                        acmMacroRecall += this.getMacroRecall(rep, fold);
                        outputResults.write("Model Building Time (s): " + ((double)this.getBuildingTime(rep, fold) / (double)1000) + "\n");
                        acmBuildingTime += this.getBuildingTime(rep, fold);
                        outputResults.write("Classification Time (s): " + ((double)this.getClassificationTime(rep, fold) / (double)1000) + "\n");
                        acmClassificationTime += this.getClassificationTime(rep, fold);
                        outputResults.write("Number of Iterations: " + this.getNumIterations(rep, fold) + "\n");
                        acmIterations += this.getNumIterations(rep, fold);
                   }
                }   
            }else if(this.getType().equals("Transductive")){
                for(int rep=0;rep<numReps;rep++){
                    outputResults.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                    outputResults.write("Accuracy (%): " + this.getAccuracies(rep, 0) + "\n");
                    acmAccuracy += this.getAccuracies(rep, 0);
                    outputResults.write("Error (%): " + (100 - this.getAccuracies(rep, 0)) + "\n");
                    for(int classe=0;classe<classes.size();classe++){
                        outputResults.write("Precision-" + classes.get(classe) + ": " + this.getPrecision(classe,rep, 0) + "\n");
                        outputResults.write("Recall-" + classes.get(classe) + ": " + this.getRecall(classe,rep, 0) + "\n");
                        outputResults.write("F1-" + classes.get(classe) + ": " + this.getF1(classe,rep, 0) + "\n");
                    }
                    outputResults.write("Micro-Precision: " + this.getMicroPrecision(rep, 0) + "\n");
                    acmMicroPrecision += this.getMicroPrecision(rep, 0);
                    outputResults.write("Micro-Recall: " + this.getMicroRecall(rep, 0) + "\n");
                    acmMicroRecall += this.getMicroRecall(rep, 0);
                    outputResults.write("Macro-Precision: " + this.getMacroPrecision(rep, 0) + "\n");
                    acmMacroPrecision += this.getMacroPrecision(rep, 0);
                    outputResults.write("Macro-Recall: " + this.getMacroRecall(rep, 0) + "\n");
                    acmMacroRecall += this.getMacroRecall(rep, 0);
                    outputResults.write("Model Building Time (s): " + ((double)this.getBuildingTime(rep, 0) / (double)1000) + "\n");
                    acmBuildingTime += this.getBuildingTime(rep, 0);
                    outputResults.write("Classification Time (s): " + ((double)this.getClassificationTime(rep, 0) / (double)1000) + "\n");
                    acmClassificationTime += this.getClassificationTime(rep, 0);
                    outputResults.write("Number of Iterations: " + this.getNumIterations(rep, 0) + "\n");
                    acmIterations += this.getNumIterations(rep, 0);
                }
            }else{
                 for(int fold=0;fold<numFolds;fold++){
                    outputResults.write("Fold: " + (fold + 1) + " --------------------------------------------------------------------\n");
                    for(int rep=0;rep<numReps;rep++){
                        outputResults.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                        outputResults.write("Accuracy (%): " + this.getAccuracies(rep, fold) + "\n");
                        acmAccuracy += this.getAccuracies(rep, fold);
                        outputResults.write("Error (%): " + (100 - this.getAccuracies(rep, fold)) + "\n");
                        outputResults.write("Micro-Precision: " + this.getMicroPrecision(rep, fold) + "\n");
                        acmMicroPrecision += this.getMicroPrecision(rep, fold);
                        outputResults.write("Micro-Recall: " + this.getMicroRecall(rep, fold) + "\n");
                        acmMicroRecall += this.getMicroRecall(rep, fold);
                        outputResults.write("Macro-Precision: " + this.getMacroPrecision(rep, fold) + "\n");
                        acmMacroPrecision += this.getMacroPrecision(rep, fold);
                        outputResults.write("Macro-Recall: " + this.getMacroRecall(rep, fold) + "\n");
                        acmMacroRecall += this.getMacroRecall(rep, fold);
                        outputResults.write("Model Building Time (s): " + ((double)this.getBuildingTime(rep, fold) / (double)1000) + "\n");
                        acmBuildingTime += this.getBuildingTime(rep, fold);
                        outputResults.write("Classification Time (s): " + ((double)this.getClassificationTime(rep, fold) / (double)1000) + "\n");
                        acmClassificationTime += this.getClassificationTime(rep, fold);
                        outputResults.write("Number of Iterations: " + this.getNumIterations(rep, fold) + "\n");
                        acmIterations += this.getNumIterations(rep, fold);
                   }
                }
            }
            
            //Saving the average of the results
            outputResults.write("\n-------------------------------------\n");
            int totalExecutions = numReps * numFolds;
            double averageAccuracy = (double)acmAccuracy / (double)totalExecutions;
            double averageMicroPrecision = (double)acmMicroPrecision / (double)totalExecutions;
            double averageMicroRecall = (double)acmMicroRecall / (double)totalExecutions;
            double averageMacroPrecision = (double)acmMacroPrecision / (double)totalExecutions;
            double averageMacroRecall = (double)acmMacroRecall / (double)totalExecutions;
            double averageBuildingTime = ((double)acmBuildingTime / (double)1000) / (double)totalExecutions;
            double averageClassificationTime = ((double)acmClassificationTime / (double)1000) / (double)totalExecutions;
            double averageIterations = (double)acmIterations / (double)totalExecutions;
            
            for(int rep=0;rep<numReps;rep++){
                for(int fold=0; fold<numFolds;fold++){
                    acmSD += Math.pow((this.getAccuracies(rep, fold) - averageAccuracy), 2);
                }
            }
            acmSD = (double)acmSD / (double)totalExecutions;
            double standardDeviation = Math.sqrt(acmSD);
            
            outputResults.write("Average Accuracy (%): " + averageAccuracy + "\n");
            outputResults.write("Average Micro-Precision: " + averageMicroPrecision + "\n");
            outputResults.write("Average Micro-Recall: " + averageMicroRecall + "\n");
            outputResults.write("Average Macro-Precision: " + averageMacroPrecision + "\n");
            outputResults.write("Average Macro-Recall: " + averageMacroRecall + "\n");
            outputResults.write("Standard Deviation Accuracy: " + standardDeviation +"\n");
            outputResults.write("Average Model Building Time (s): " + averageBuildingTime + "\n");
            outputResults.write("Average Classification Time (s): " + averageClassificationTime + "\n");
            outputResults.write("Average Number of Iterations (s): " + averageIterations+ "\n");
            
            
            outputResults.close();
        }catch(Exception e){
            System.err.println("Error when saving the results");
            e.printStackTrace();
            System.exit(0);
        }
        
    }
    
}