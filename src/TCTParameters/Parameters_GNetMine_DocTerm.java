//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_GNetMine_DocTerm implements Serializable{
    
    private ArrayList<Double> alphasDocs = new ArrayList<Double>();
    //private ArrayList<Double> alphasTermos = new ArrayList<Double>();
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_GNetMine_DocTerm(){
        addAlphaDoc(0.1);
        addAlphaDoc(0.3);
        addAlphaDoc(0.5);
        addAlphaDoc(0.7);
        addAlphaDoc(0.9);
        
        //addAlphaTermo(0.1);
        //addAlphaTermo(0.3);
        //addAlphaTermo(0.5);
        //addAlphaTermo(0.7);
        //addAlphaTermo(0.9);
        
        setMaxNumIterations(1000);
    }
    
    public Parameters_GNetMine_DocTerm(ArrayList<Double> alphasDocs, int maxNumberInterations){
        setAlphasDocs(alphasDocs);
        //setAlphasTermos(alphasTermos);
        setMaxNumIterations(maxNumberInterations);
    }
    
    public void addAlphaDoc(double alpha){
        alphasDocs.add(alpha);
    }
    
    /*public void addAlphaTermo(double alpha){
        alphasTermos.add(alpha);
    }*/
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public double getAlphaDoc(int pos){
        return alphasDocs.get(pos);
    }
    
    /*public double getAlphaTermo(int pos){
        return alphasTermos.get(pos);
    }*/
    
    public ArrayList<Double> getAlphasDocs(){
        return alphasDocs;
    }
    
    /*public ArrayList<Double> getAlphasTermos(){
        return alphasTermos;
    }*/
    
    public void setAlphasDocs(ArrayList<Double> alphas){
        this.alphasDocs = alphas;
    }
    
    /*public void setAlphasTermos(ArrayList<Double> alphas){
        this.alphasTermos = alphas;
    }*/
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
}
