/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.TransductiveOneClass;

import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public abstract class TransductiveLearnerOneClass {
    
    private int interestClass;
    public double[][] fUnlabeledDocs;
    
    
    public TransductiveLearnerOneClass(){
        interestClass = -1;
    }
    
    public abstract void buildClassifier(Instances dataPositive, Instances dataUnlabeled);

    public int getInterestClass() {
        return interestClass;
    }

    public void setInterestClass(int interestClass) {
        this.interestClass = interestClass;
    }

    public double[][] getfUnlabeledDocs() {
        return fUnlabeledDocs;
    }

    public void setfUnlabeledDocs(double[][] fUnlabeledDocs) {
        this.fUnlabeledDocs = fUnlabeledDocs;
    }
    
    public double[][] getClassInformation(Instances dataLabeled, Instances dataUnlabeled){
        int numLabeled = dataLabeled.numInstances();
        int numUnlabeled = dataUnlabeled.numInstances();
        int numClasses = 2;
        double[][] dist = new double[numLabeled + numUnlabeled][numClasses];
        for(int inst=0;inst<numLabeled;inst++){
            Instance instance = dataLabeled.instance(inst);
            int classID = (int)instance.classValue(); 
            int pos = 0;
            if(classID == interestClass){
                dist[inst][pos] = 1;
            }       
        }
        double value = 0;
        for(int inst=0;inst<numUnlabeled;inst++){
            for(int classe=0;classe<numClasses;classe++){
                dist[inst + numLabeled][classe] = value;
            }
        }
        return dist;
    }
    
}
