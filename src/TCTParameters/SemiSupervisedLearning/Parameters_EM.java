//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_EM implements Serializable{
    
    private int maxNumberInterations; // Maximum number of iterations
    private double minLogLikelihood;
    private ArrayList<Double> weightsUnlabeled = new ArrayList<Double>();
    private ArrayList<Integer> numCompClasses = new ArrayList<Integer>();
    
    public Parameters_EM(){
        setMaxNumIterations(1000);
        setMinLogLikelihood(0);
        
        addWeightUnlabeled(0.1);
        addWeightUnlabeled(0.3);
        addWeightUnlabeled(0.5);
        addWeightUnlabeled(0.7);
        addWeightUnlabeled(0.9);
        
        addNumCompClasses(1);
        addNumCompClasses(2);
        addNumCompClasses(5);
        addNumCompClasses(10);
    }
    
    public void addNumCompClasses(int numComp){
        numCompClasses.add(numComp);
    }
    
    public Integer getNumCompClasse(int pos){
        return numCompClasses.get(pos);
    }
    
    public ArrayList<Integer> getNumCompClasses(){
        return numCompClasses;
    }
    
    public void addWeightUnlabeled(double weightUnlabeled){
        weightsUnlabeled.add(weightUnlabeled);
    }
    
    public Double getWeightUnlabeledInstances(int pos){
        return weightsUnlabeled.get(pos);
    }
    
    public ArrayList<Double> getWeightsUnlabeled(){
        return weightsUnlabeled;
    }
    
    public Integer getMaxNumberIterations(){
        return this.maxNumberInterations;
    }
    
    public Double getMinLogLikelihood(){
        return this.minLogLikelihood;
    }
    
    public void setNumCompClasses(ArrayList<Integer> numCompClasses){
        this.numCompClasses = numCompClasses;
    }
    
    public void setWeightsUnlabeled(ArrayList<Double> weightsUnlabeled){
        this.weightsUnlabeled = weightsUnlabeled;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setMinLogLikelihood(double minLogLikelihood){
        this.minLogLikelihood = minLogLikelihood;
    }
}
