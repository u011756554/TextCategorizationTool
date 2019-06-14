//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import TCTStructures.LambdasGNetMine;
import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_GNetMine_DocDoc_DocTerm_TermTerm implements Serializable{
    private ArrayList<Double> alphasDocs = new ArrayList<Double>();
    private ArrayList<LambdasGNetMine> lambdas = new ArrayList<LambdasGNetMine>();
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_GNetMine_DocDoc_DocTerm_TermTerm(){
        
        addAlphaDoc(0.1);
        //addAlphaDoc(0.3);
        addAlphaDoc(0.5);
        //addAlphaDoc(0.7);
        addAlphaDoc(0.9);
        
        addLambdas(0.33,0.33,0.33);
        addLambdas(0.4,0.4,0.2);
        addLambdas(0.4,0.2,0.4);
        addLambdas(0.2,0.4,0.4);
        addLambdas(0.2,0.2,0.6);
        addLambdas(0.2,0.6,0.2);
        addLambdas(0.6,0.2,0.2);

        setMaxNumIterations(1000);
    }
    
    public void addAlphaDoc(double alpha){
        alphasDocs.add(alpha);
    }
    
    public void addLambdas(double lambdaDocDoc, double lambdaDocTerm, double lambdaTermTerm){
        lambdas.add(new LambdasGNetMine(lambdaDocDoc, lambdaDocTerm, lambdaTermTerm));
    }
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public double getAlphaDoc(int pos){
        return alphasDocs.get(pos);
    }
    
    public ArrayList<Double> getAlphasDocs(){
        return alphasDocs;
    }
    
    public ArrayList<LambdasGNetMine> getLambdas(){
        return lambdas;
    }
    
    public LambdasGNetMine getLambda(int pos){
        return lambdas.get(pos);
    }
    
    public void setAlphasDocs(ArrayList<Double> alphasDocs){
        this.alphasDocs = alphasDocs;
    }
    
    public void setLambdas(ArrayList<LambdasGNetMine> lambdas){
        this.lambdas = lambdas;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
}
