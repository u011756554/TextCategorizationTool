/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTParameters.SupervisedOneClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class ParametersPrototypeBasedClustering extends ParametersOneClass implements Serializable{
    
    private ArrayList<Integer> ks;
    
    private int numMaxIterations;
    private double minDiffObjective;
    private double minChangeRate;

    private int numTrials;
    
    private boolean cosine;
    private boolean pearson;
    private boolean euclidean;
    
    private boolean cohesionSplitting;
    
    public ParametersPrototypeBasedClustering(){
        setKs(generateKs(1,30,1));
        
        setMinChangeRate(0);
        
        this.setNumTrials(10);
        
        this.addThreshold(0.05);
        this.addThreshold(0.1);
        this.addThreshold(0.15);
        this.addThreshold(0.2);
        this.addThreshold(0.25);
        this.addThreshold(0.3);
        this.addThreshold(0.35);
        this.addThreshold(0.4);
        this.addThreshold(0.45);
        this.addThreshold(0.5);
        this.addThreshold(0.55);
        this.addThreshold(0.6);
        this.addThreshold(0.65);
        this.addThreshold(0.7);
        this.addThreshold(0.75);
        this.addThreshold(0.8);
        this.addThreshold(0.85);
        this.addThreshold(0.9);
        this.addThreshold(0.95);
        
        setNumMaxIterations(100);
        setMinDiffObjective(0);
        setMinChangeRate(0);
        setCosine(true);
        
        setCohesionSplitting(false);
    }

    public int getNumTrials() {
        return numTrials;
    }

    public void setNumTrials(int numTrials) {
        this.numTrials = numTrials;
    }
    
    public boolean isCosine() {
        return cosine;
    }

    public void setCosine(boolean cosine) {
        this.cosine = cosine;
    }
    
    public void addK(int k){
        ks.add(k);
    }
    
    public int getK(int id){
        return ks.get(id);
    }
    
    public ArrayList<Integer> getKs() {
        return ks;
    }

    public void setKs(ArrayList<Integer> ks) {
        this.ks = ks;
    }

    public int getNumMaxIterations() {
        return numMaxIterations;
    }

    public void setNumMaxIterations(int numMaxIterations) {
        this.numMaxIterations = numMaxIterations;
    }

    public double getMinChangeRate() {
        return minChangeRate;
    }

    public void setMinChangeRate(double changeMinRate) {
        this.minChangeRate = changeMinRate;
    }

    public double getMinDiffObjective() {
        return minDiffObjective;
    }

    public void setMinDiffObjective(double minDiffObjective) {
        this.minDiffObjective = minDiffObjective;
    }

    public ArrayList<Integer> generateKs(int minK, int maxK, int stepK){
        ArrayList<Integer> ks = new ArrayList<Integer>();
        for(int k=minK;k<=maxK;k+=stepK){
            ks.add(k);
        }
        return ks;
    }
    
    public boolean isPearson() {
        return pearson;
    }

    public void setPearson(boolean pearson) {
        this.pearson = pearson;
    }

    public boolean isEuclidean() {
        return euclidean;
    }

    public void setEuclidean(boolean euclidean) {
        this.euclidean = euclidean;
    }

    public boolean isCohesionSplitting() {
        return cohesionSplitting;
    }

    public void setCohesionSplitting(boolean cohesionSplitting) {
        this.cohesionSplitting = cohesionSplitting;
    }
    
    
}
