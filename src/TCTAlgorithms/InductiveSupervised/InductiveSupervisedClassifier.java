//*****************************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Variables and functions shared by inductive supervised learning algorithms.
//*****************************************************************************************

package TCTAlgorithms.InductiveSupervised;

import weka.classifiers.Classifier;

public abstract class InductiveSupervisedClassifier extends Classifier{
    private int numIterations;
    private int use;
    
    InductiveSupervisedClassifier(){
        super();
        setNumIterations(0);
    }
    
    public void setNumIterations(int numIterations){
        this.numIterations = numIterations;
    }
    
    public int getNumiterations(){
        return this.numIterations;
    }
    
    public void setUse(int use){
        this.use = use;
    }
    
    public int getUse(){
        return this.use;
    }
   
}
