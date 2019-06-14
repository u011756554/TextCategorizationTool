//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_TermNetwork implements Serializable{
    
    private ArrayList<Double> thresholds = new ArrayList<Double>();
    private ArrayList<Integer> ks = new ArrayList<Integer>();
    private boolean relative;
    
    private boolean networkThreshold = false;
    private boolean networkTopK = false;
    
    public Parameters_TermNetwork(){
        addThreshold(0);
        addThreshold(0.25);
        addThreshold(0.5);
        addThreshold(0.75);
        
        addK(7);
        addK(17);
        addK(37);
        addK(57);
        
        setNetworkThreshold(true);
        setNetworkTopK(true);
        
        setRelative(false);
    }
    
    public Parameters_TermNetwork(ArrayList<Double> thresholds){
        this.thresholds = thresholds;
    }
    
    public void addK(int k){
        ks.add(k);
    }
    
    public void addThreshold(double threshold){
        thresholds.add(threshold);
    }
    
    public boolean getRelative(){
        return relative;
    }
    
    public double getThreshold(int pos){
        return thresholds.get(pos);
    }
    
    public int getK(int pos){
        return ks.get(pos);
    }
    
    public ArrayList<Double> getThresholds(){
        return thresholds;
    }
    
    public ArrayList<Integer> getKs(){
        return ks;
    }
    
    public boolean getThresholdNetwork(){
        return this.networkThreshold;
    }
    
    public boolean getNetworkTopK(){
        return this.networkTopK;
    }
    
    public void setNetworkThreshold(boolean networkThreshold){
        this.networkThreshold = networkThreshold;
    }
    
    public void setNetworkTopK(boolean networkTopK){
        this.networkTopK = networkTopK; 
    }    
    
    public void setThresholds(ArrayList<Double> thresholds){
        this.thresholds = thresholds;
    }
    
    public void setKs(ArrayList<Integer> ks){
        this.ks = ks;
    }
    
    public void setRelative(boolean relative){
        this.relative = relative;
    }
}
