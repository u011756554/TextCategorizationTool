//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ragero
 */
public class Parameters_LLGC implements Serializable{
    
    private ArrayList<Double> alphas = new ArrayList<Double>();
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_LLGC(){
        addAlpha(0.1);
        addAlpha(0.3);
        addAlpha(0.5);
        addAlpha(0.7);
        addAlpha(0.9);
        
        setMaxNumIterations(1000);
    }
    
    public Parameters_LLGC(int maxNumberInterations, ArrayList<Double> alphas){
        setMaxNumIterations(maxNumberInterations);
        setAlphas(alphas);
    }
    
    public void addAlpha(double alpha){
        alphas.add(alpha);
    }
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public double getAlpha(int pos){
        return alphas.get(pos);
    }
    
    public ArrayList<Double> getAlphas(){
        return alphas;
    }
    
    public void setAlphas(ArrayList<Double> alphas){
        this.alphas = alphas;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
}
