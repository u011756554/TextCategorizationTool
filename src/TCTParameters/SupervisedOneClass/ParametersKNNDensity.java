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
public class ParametersKNNDensity extends ParametersOneClass implements Serializable{
    
    private ArrayList<Integer> ks;
    
    boolean cosine;
    boolean snn;
    
    public ParametersKNNDensity(){
        super();
        
        setKs(generateKs(1,30,1));
        setCosine(true);
        setSnn(false);
        
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

    public int getK(int id){
        return ks.get(id);
    }
    
    public ArrayList<Integer> getKs() {
        return ks;
    }

    public void setKs(ArrayList<Integer> ks) {
        this.ks = ks;
    }

    public boolean isCosine() {
        return cosine;
    }

    public void setCosine(boolean consine) {
        this.cosine = consine;
    }

    public boolean isSnn() {
        return snn;
    }

    public void setSnn(boolean snn) {
        this.snn = snn;
    }
    
    public ArrayList<Integer> generateKs(int minK, int maxK, int stepK){
        ArrayList<Integer> ks = new ArrayList<Integer>();
        for(int k=minK;k<=maxK;k+=stepK){
            ks.add(k);
        }
        return ks;
    }
    
}
