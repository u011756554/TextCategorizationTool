//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_TGM implements Serializable{
    
    private boolean unionSet;
    private boolean biggerSet;
    private boolean smallerSet;
    private boolean intersection;
    
    private int maxNumberInterations; // Maximum number of iterations
    private double difMediaMinima;
    private double dampingFactor;
    
    ArrayList<Double> minSups = new ArrayList<Double>();
    
    public Parameters_TGM(){
        //setUnionSet(true);
        //setBiggerSet(true);
        //setSmallerSet(true);
        setIntersection(true);
        
        addMinSup(0.0);
        addMinSup(0.15);
        addMinSup(0.30);
        addMinSup(0.45);
        
        setMaxNumIterations(1000);
        setDifMediaMinima(0.01);
        setDampingFactor(0.85);
    }
    
    public void addMinSup(double threshold){
        this.minSups.add(threshold);
    }
    
    public void setDampingFactor(double factor){
        this.dampingFactor = factor;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setDifMediaMinima(double difMediaMinima){
        this.difMediaMinima = difMediaMinima;
    }
    
    public void setMinSups(ArrayList<Double> minSups){
        this.minSups = minSups;
    }
    
    public void setUnionSet(boolean unionSet){
        this.unionSet = unionSet;
    }
    
    public void setBiggerSet(boolean biggerSet){
        this.biggerSet = biggerSet;
    }
    
    public void setSmallerSet(boolean smallerSet){
        this.smallerSet = smallerSet;
    }
    
    public void setIntersection(boolean intersection){
        this.intersection = intersection;
    }
    
    public Integer getMaxNumberIterations(){
        return this.maxNumberInterations;
    }
    
    public Double getAvgMinDifference(){
        return this.difMediaMinima;
    }
    
    public boolean getUnionSet(){
        return this.unionSet;
    }
    
    public boolean getBiggerSet(){
        return this.biggerSet;
    }
    
    public boolean getSmallerSet(){
        return this.smallerSet;
    }
    
    public boolean getIntersection(){
        return this.intersection;
    }
    
    public ArrayList<Double> getMinSups(){
        return this.minSups;
    }
    
    public double getMinSup(int pos){
        return this.minSups.get(pos);
    }
    
    public Double getDampingFactor(){
        return this.dampingFactor;
    }
}
