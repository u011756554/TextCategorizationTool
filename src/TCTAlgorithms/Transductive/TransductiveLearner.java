//**********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Variables and functions shared by transductive learning algorithms.
//**********************************************************************************

package TCTAlgorithms.Transductive;

import weka.core.Instance;
import weka.core.Instances;

public abstract class TransductiveLearner {
    public double[][] fUnlabeledDocs;
    private int numIterations;
    private int use;
    
    public TransductiveLearner(){
        setNumIterations(100);
        setUse(0);
    }
    
    public void setNumIterations(int numIterations){
        this.numIterations = numIterations;
    }
    
    public int getNumiterations(){
        return this.numIterations;
    }
    
    
    public abstract void buildClassifier(Instances dataTrain, Instances dataTest);
    
    //Set the value 1 in the position corresponding to the class and 0 for others in the class information vector of labeled documents
    public double[][] getClassInformation(Instances dataTrain, Instances dataTest){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        int numClasses = dataTrain.numClasses();
        double[][] dist = new double[numTrain + numTest][numClasses];
        for(int inst=0;inst<numTrain;inst++){
            Instance instance = dataTrain.instance(inst);
            int pos = (int)instance.classValue();
            dist[inst][pos] = 1;
            
        }
        double value = 0;
        for(int inst=0;inst<numTest;inst++){
            for(int classe=0;classe<numClasses;classe++){
                dist[inst + numTrain][classe] = value;
            }
        }
        return dist;
    }
    
    private double[][] getClassInformationIni(Instances dataTrain, Instances dataTest){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        int numClasses = dataTrain.numClasses();
        double[][] dist = new double[numTrain + numTest][numClasses];
        //double value = (double)1/(double)numClasses;
        double value = 0;
        for(int inst=0;inst<(numTrain+numTest);inst++){
            for(int classe=0;classe<numClasses;classe++){
                dist[inst][classe] = value;
            }
        }
        return dist;
    }
    
    public double[][] getClassInformation_ID(Instances dataTrain, Instances dataTest){
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
    
    public int getUse(){
        return use;
    }
    
    public void setUse(int use){
        this.use = use;
    }
}
