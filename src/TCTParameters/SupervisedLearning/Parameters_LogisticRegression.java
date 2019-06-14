//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_LogisticRegression implements Serializable{
    
    private double tolerance;
    private int maxIterations;
    private int numFolds;
    private ArrayList<Integer> priors;
    
    public Parameters_LogisticRegression(){
        setPriors(new ArrayList<Integer>());
        addPrior(1);
        addPrior(2);
        
        setMaxIterations(1000);
        setNumFolds(10);
        setTolerance(0.0005);
    }
    
    public void addPrior(int prior){
        priors.add(prior);
    }
    
    public Integer getPrior(int pos){
        return this.priors.get(pos);
    }
    
    public ArrayList<Integer> getPriors(){
        return this.priors;
    }
    
    public Double getTolerance(){
        return this.tolerance;
    }
    
    public Integer getMaxIterations(){
        return this.maxIterations;
    }
    
    public Integer getNumFolds(){
        return this.numFolds;
    }
    
    public void setTolerance(double tolerance){
        this.tolerance = tolerance;
    }
    
    public void setMaxIterations(int maxIterations){
        this.maxIterations = maxIterations;
    }
    
    public void setNumFolds(int numFolds){
        this.numFolds = numFolds;
    }
    
    public void setPriors(ArrayList<Integer> priors){
        this.priors = priors;
    }
}
