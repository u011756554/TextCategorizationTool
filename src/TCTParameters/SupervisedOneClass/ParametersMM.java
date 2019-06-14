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
public class ParametersMM extends ParametersOneClass implements Serializable{
    
    ArrayList<Integer> numModels;
    double tolerance;
    int numMaxIterations;
    
    public ParametersMM(){
        setNumModels(generateNumModels(1,30,1));
        numMaxIterations = 100;
        tolerance = 0.000001;
        
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
    }

    public ArrayList<Integer> getNumModels() {
        return numModels;
    }

    public void setNumModels(ArrayList<Integer> numModels) {
        this.numModels = numModels;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public int getNumMaxIterations() {
        return numMaxIterations;
    }

    public void setNumMaxIterations(int numMaxIterations) {
        this.numMaxIterations = numMaxIterations;
    }
    
    public ArrayList<Integer> generateNumModels(int min, int max, int step){
        ArrayList<Integer> models = new ArrayList<Integer>();
        for(int k=min;k<=max;k+=step){
            models.add(k);
        }
        return models;
    }
    
}
