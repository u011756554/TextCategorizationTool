//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_GNetMine_DocTerm_TermTerm implements Serializable{
    
    private ArrayList<Double> alphasDocs = new ArrayList<Double>();
    private ArrayList<Double> lambdasDocTermo = new ArrayList<Double>();
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_GNetMine_DocTerm_TermTerm(){
        addAlphaDoc(0.1);
        //addAlphaDoc(0.3);
        addAlphaDoc(0.5);
        //addAlphaDoc(0.7);
        addAlphaDoc(0.9);
        
        addLambdaDocTermo(0.1);
        //addLambdaDocTermo(0.3);
        addLambdaDocTermo(0.5);
        //addLambdaDocTermo(0.7);
        addLambdaDocTermo(0.9);
        
        setMaxNumIterations(1000);
    }
    
    public Parameters_GNetMine_DocTerm_TermTerm(ArrayList<Double> alphasDocs, ArrayList<Double> lambdasDocTermo, int maxNumberInterations){
        setAlphasDocs(alphasDocs);
        setLambdasDocTermo(lambdasDocTermo);
        setMaxNumIterations(maxNumberInterations);
    }
    
    public void addAlphaDoc(double alpha){
        alphasDocs.add(alpha);
    }
    
    public void addLambdaDocTermo(double lambdaDocTerm){
        lambdasDocTermo.add(lambdaDocTerm);
    }
            
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public double getAlphaDoc(int pos){
        return alphasDocs.get(pos);
    }
    
    public double getLambdaDocTermo(int pos){
        return lambdasDocTermo.get(pos);
    }
    
    public ArrayList<Double> getAlphasDocs(){
        return alphasDocs;
    }
    
    public ArrayList<Double> getLambdasDocTermo(){
        return lambdasDocTermo;
    }
    
    public void setAlphasDocs(ArrayList<Double> alphas){
        this.alphasDocs = alphas;
    }
    
    public void setLambdasDocTermo(ArrayList<Double> lambdasDocTermo){
        this.lambdasDocTermo = lambdasDocTermo;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
}
