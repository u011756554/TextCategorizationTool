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

/**
 *
 * @author rafael
 */
public class ResultsOneClass implements Serializable{
    
    private boolean[][][] complete;
    private double[][][] accuracies;
    private double[][][] precisions;
    private double[][][] recalls;
    private double[][][] f1s;
    private long[][][] buildingTimes;
    private long[][][] classificationTimes;
    private int[][][] numIterations;
    
    String type;
    
    File output;
    
    private int numReps;
    private int numFolds;
    private int numClasses;
    
    public ResultsOneClass(File output, int numClasses, int numReps, int numFolds, String type){
        
        this.output = output;
        
        this.type = type;
        
        this.numReps = numReps;
        this.numFolds = numFolds;
        this.numClasses = numClasses;
        
        accuracies = new double[numClasses][numReps][numFolds];
        precisions = new double[numClasses][numReps][numFolds];
        recalls = new double[numClasses][numReps][numFolds];
        f1s = new double[numClasses][numReps][numFolds];
        buildingTimes = new long[numClasses][numReps][numFolds];
        classificationTimes = new long[numClasses][numReps][numFolds];
        numIterations = new int[numClasses][numReps][numFolds];
        complete = new boolean[numClasses][numReps][numFolds];
        
        for(int classe=0;classe<numClasses;classe++){
            for(int rep=0;rep<numReps;rep++){
                for(int col=0;col<numFolds;col++){
                    accuracies[classe][rep][col] = -1;
                    precisions[classe][rep][col] = -1;
                    recalls[classe][rep][col] = -1;
                    f1s[classe][rep][col] = -1;
                    buildingTimes[classe][rep][col] = -1;
                    classificationTimes[classe][rep][col] = -1;
                    numIterations[classe][rep][col] = 0;
                    complete[classe][rep][col] = false;
                }
            }    
        }
        
        
    }    
        
    public void setType(String type){
        this.type = type;
    }
    
    public synchronized void setComplete(int classe, int rep, int fold, boolean value){
        complete[classe][rep][fold] = value;
        
        boolean all = true;
        for(int clas=0;clas<numClasses;clas++){
            for(int repetition=0; repetition<numReps; repetition++){
                for(int folds=0;folds<numFolds;folds++){
                    if(complete[clas][repetition][folds] == false){
                        all = false;
                    }
                }
            }    
        }
        
        
        if(all == true){
            saveResults();
        }else{
            saveObject();
        }
    }
    
    public void setAccuracy(int classe, int rep, int fold, double value){
        accuracies[classe][rep][fold] = value;
    }    
    
    public void setPrecision(int classe, int rep, int fold, double value){
        precisions[classe][rep][fold] = value;
    }
    
    public void setRecall(int classe, int rep, int fold, double value){
        recalls[classe][rep][fold] = value;
    }
    
    public void setF1(int classe, int rep, int fold, double value){
        f1s[classe][rep][fold] = value;
    }
    
    public void setBuildingTime(int classe, int rep, int fold, long value){
        buildingTimes[classe][rep][fold] = value;
    }
    
    public void setClassificationTime(int classe, int rep, int fold, long value){
        classificationTimes[classe][rep][fold] = value;
    }
    
    public void setNumIterations(int classe, int rep, int fold, int value){
        numIterations[classe][rep][fold] = value;
    }
    
    public String getType(){
        return type;
    }
    
    public double getAccuracy(int classe, int rep, int fold){
        return accuracies[classe][rep][fold];
    }
    
    public double getPrecision(int classe, int rep, int fold){
        return precisions[classe][rep][fold];
    }
    
    public double getRecall(int classe, int rep, int fold){
        return recalls[classe][rep][fold];
    }
    
    public double getF1(int classe, int rep, int fold){
        return f1s[classe][rep][fold];
    }
    
    public long getBuildingTime(int classe, int rep, int fold){
        return buildingTimes[classe][rep][fold];
    }
    
    public long getClassificationTime(int classe, int rep, int fold){
        return classificationTimes[classe][rep][fold];
    }
    
    public int getNumIterations(int classe, int rep, int fold){
        return numIterations[classe][rep][fold];
    }
    
    public boolean getComplete(int classe, int rep, int fold){
        return complete[classe][rep][fold];
    }
    
    public synchronized void computeEvaluationMeasures(Integer[][] confusionMatrix, int classe, int rep, int fold){
        
        int tp = confusionMatrix[0][0];
        int tn = confusionMatrix[1][1];
        int fp = confusionMatrix[0][1];
        int fn = confusionMatrix[1][0];
        
        double accuracy = ((double)(tp + tn)/(double)(tp + tn + fp + fn)) * 100;
        this.setAccuracy(classe, rep, fold, accuracy);
        
        double precision = 0;
        if((tp + fp) > 0){
            precision = (double)tp/(double)(tp + fp);
        }
        this.setPrecision(classe, rep, fold, precision);
        
        double recall = 0;
        if((tp + fn) > 0){
            recall = (double)tp/(double)(tp + fn);
        }
        this.setRecall(classe,rep, fold, recall);
        
        double f1 = 0;
        if((precision + recall) > 0){
            f1 = (2 * precision * recall) / (precision + recall);
        }
        this.setF1(classe,rep, fold, f1);
        
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
        for(int i1=0;i1<2;i1++){
            for(int i2=0;i2<2;i2++){
                System.out.print(confusionMatrix[i1][i2] + "\t");
            }
            System.out.println();
        }
        
        this.setComplete(classe, rep, fold, true);
        
    }
    
    
    public synchronized void saveObject(){
        try{
            ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(output.getAbsolutePath() + ".tmp"));
            objOutput.writeObject(this);
            objOutput.close();
        }catch(Exception e){
            System.err.println("Error when saving the results");
        } 
    }
    
    public synchronized void saveResults(){
        
        File outputTemp = new File(output.getAbsolutePath() + ".tmp");
        if(outputTemp.exists()){
            outputTemp.delete();
        }
        
        double acmAccuracy = 0;
        double acmPrecision = 0;
        double acmRecall = 0;
        double acmF1 = 0;
        double acmBuildingTime = 0;
        double acmClassificationTime = 0;
        int acmIterations = 0;
        double acmSD = 0;
        
        try{
            FileWriter outputResults = new FileWriter(output.getAbsolutePath() + ".txt");
            
            //Saving results per fold
            if(this.getType().equals("InductiveSupervised")){
                for(int classe=0;classe<numClasses;classe++){
                    for(int rep=0;rep<numReps;rep++){
                       outputResults.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                       for(int fold=0;fold<numFolds;fold++){
                            outputResults.write("Fold: " + (fold + 1) + " --------------------------------------------------------------------\n");
                            outputResults.write("Accuracy (%): " + this.getAccuracy(classe, rep, fold) + "\n");
                            if(this.getAccuracy(classe, rep, fold) != -1){
                                acmAccuracy += this.getAccuracy(classe, rep, fold);    
                            }
                            outputResults.write("Error (%): " + (100 - this.getAccuracy(classe, rep, fold)) + "\n");
                            outputResults.write("Precision: " + this.getPrecision(classe, rep, fold) + "\n");
                            if(this.getPrecision(classe, rep, fold) != -1){
                                acmPrecision += this.getPrecision(classe, rep, fold);    
                            }
                            outputResults.write("Recall: " + this.getRecall(classe, rep, fold) + "\n");
                            if(this.getRecall(classe, rep, fold) != -1){
                                acmRecall += this.getRecall(classe, rep, fold);    
                            }
                            double f1 = 0;
                            if(this.getRecall(classe, rep, fold) + this.getPrecision(classe, rep, fold) > 0){
                                f1 = (2 * this.getRecall(classe, rep, fold) + this.getPrecision(classe, rep, fold))/(this.getRecall(classe, rep, fold) + this.getPrecision(classe, rep, fold));
                            }
                            outputResults.write("F1: " + this.getF1(classe, rep, fold) + "\n");
                            acmF1 += this.getF1(classe, rep, fold);
                            outputResults.write("Model Building Time (s): " + ((double)this.getBuildingTime(classe, rep, fold) / (double)1000) + "\n");
                            if(this.getBuildingTime(classe, rep, fold) != -1){
                                acmBuildingTime += this.getBuildingTime(classe, rep, fold);    
                            }
                            outputResults.write("Classification Time (s): " + ((double)this.getClassificationTime(classe, rep, fold) / (double)1000) + "\n");
                            if(this.getClassificationTime(classe, rep, fold) != -1){
                                acmClassificationTime += this.getClassificationTime(classe, rep, fold);    
                            }
                            outputResults.write("Number of Iterations: " + this.getNumIterations(classe, rep, fold) + "\n");
                            if(this.getNumIterations(classe, rep, fold) != -1){
                                acmIterations += this.getNumIterations(classe, rep, fold);    
                            }
                       }
                    }  
                }    
            }else if(this.getType().equals("Transductive")){
                for(int classe=0;classe<numClasses;classe++){
                    for(int rep=0;rep<numReps;rep++){
                        outputResults.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                        outputResults.write("Accuracy (%): " + this.getAccuracy(classe, rep, 0) + "\n");
                        acmAccuracy += this.getAccuracy(classe, rep, 0);
                        outputResults.write("Error (%): " + (100 - this.getAccuracy(classe, rep, 0)) + "\n");
                        outputResults.write("Precision: " + this.getPrecision(classe, rep, 0) + "\n");
                        acmPrecision += this.getPrecision(classe, rep, 0);
                        outputResults.write("Recall: " + this.getRecall(classe, rep, 0) + "\n");
                        acmRecall += this.getRecall(classe, rep, 0);
                        outputResults.write("F1: " + this.getF1(classe, rep, 0) + "\n");
                        acmF1 += this.getF1(classe, rep, 0);
                        outputResults.write("Model Building Time (s): " + ((double)this.getBuildingTime(classe, rep, 0) / (double)1000) + "\n");
                        acmBuildingTime += this.getBuildingTime(classe, rep, 0);
                        outputResults.write("Classification Time (s): " + ((double)this.getClassificationTime(classe, rep, 0) / (double)1000) + "\n");
                        acmClassificationTime += this.getClassificationTime(classe, rep, 0);
                        outputResults.write("Number of Iterations: " + this.getNumIterations(classe, rep, 0) + "\n");
                        acmIterations += this.getNumIterations(classe, rep, 0);
                    }
                }    
            }else{
                for(int classe=0;classe<numClasses;classe++){
                    for(int fold=0;fold<numFolds;fold++){
                        outputResults.write("Fold: " + (fold + 1) + " --------------------------------------------------------------------\n");
                        for(int rep=0;rep<numReps;rep++){
                            outputResults.write("Repetition: " + (rep + 1) + " --------------------------------------------------------------------\n");
                            outputResults.write("Accuracy (%): " + this.getAccuracy(classe, rep, fold) + "\n");
                            acmAccuracy += this.getAccuracy(classe, rep, fold);
                            outputResults.write("Error (%): " + (100 - this.getAccuracy(classe, rep, fold)) + "\n");
                            outputResults.write("Precision: " + this.getPrecision(classe, rep, fold) + "\n");
                            acmPrecision += this.getPrecision(classe, rep, fold);
                            outputResults.write("Recall: " + this.getRecall(classe, rep, fold) + "\n");
                            acmRecall += this.getRecall(classe, rep, fold);
                            outputResults.write("F1: " + this.getF1(classe, rep, fold) + "\n");
                            acmF1 += this.getF1(classe, rep, fold);
                            outputResults.write("Model Building Time (s): " + ((double)this.getBuildingTime(classe, rep, fold) / (double)1000) + "\n");
                            acmBuildingTime += this.getBuildingTime(classe, rep, fold);
                            outputResults.write("Classification Time (s): " + ((double)this.getClassificationTime(classe, rep, fold) / (double)1000) + "\n");
                            acmClassificationTime += this.getClassificationTime(classe, rep, fold);
                            outputResults.write("Number of Iterations: " + this.getNumIterations(classe, rep, fold) + "\n");
                            acmIterations += this.getNumIterations(classe, rep, fold);
                       }
                    }
                }    
            }
            
            //Saving the average of the results
            outputResults.write("\n-------------------------------------\n");
            int totalExecutions = numClasses * numReps * numFolds;
            double averageAccuracy = (double)acmAccuracy / (double)totalExecutions;
            double averagePrecision = (double)acmPrecision / (double)totalExecutions;
            double averageRecall = (double)acmRecall / (double)totalExecutions;
            double averageF1 = 0;
            if((averagePrecision + averageRecall) > 0){
                averageF1 = (2 * averagePrecision * averageRecall) / (averagePrecision + averageRecall);
            }
            double averageBuildingTime = ((double)acmBuildingTime / (double)1000) / (double)totalExecutions;
            double averageClassificationTime = ((double)acmClassificationTime / (double)1000) / (double)totalExecutions;
            double averageIterations = (double)acmIterations / (double)totalExecutions;
            
            for(int classe=0;classe<numClasses;classe++){
                for(int rep=0;rep<numReps;rep++){
                    for(int fold=0; fold<numFolds;fold++){
                        acmSD += Math.pow((this.getAccuracy(classe, rep, fold) - averageAccuracy), 2);
                    }
                }    
            }
            
            acmSD = (double)acmSD / (double)totalExecutions;
            double standardDeviation = Math.sqrt(acmSD);
            
            outputResults.write("Average Accuracy (%): " + averageAccuracy + "\n");
            outputResults.write("Average Precision: " + averagePrecision + "\n");
            outputResults.write("Average Recall: " + averageRecall + "\n");
            outputResults.write("Average F1: " + averageF1 + "\n");
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