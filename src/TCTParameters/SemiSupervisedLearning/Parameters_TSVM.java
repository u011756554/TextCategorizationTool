//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_TSVM implements Serializable{
    
    private ArrayList<Double> cs;
    private int maxNumberInterations; // Maximum number of iterations
    private boolean balanced;
    private boolean unbalanced;
    
    public Parameters_TSVM(){
        setCs(new ArrayList<Double>());
        addC(0.00001);
        addC(0.0001);
        addC(0.001);
        addC(0.01);
        addC(0.1);
        addC(1.0);
        addC(10.0);
        setBalanced(true);
        setUnbalanced(true);
        
        setMaxNumIterations(50);
    }
    
    public void addC(double c){
        cs.add(c);
    }
    
    public Double getC(int pos){
        return cs.get(pos);
    }
    
    public ArrayList<Double> getCs(){
        return cs;
    }
    
    public void setCs(ArrayList<Double> cs){
        this.cs = cs;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setBalanced(boolean balanced){
        this.balanced = balanced;
    }
    
    public void setUnbalanced(boolean unbalanced){
        this.unbalanced = unbalanced;
    }
    
    public boolean getBalanced(){
        return balanced;
    }
    
    public boolean getUnbalanced(){
        return unbalanced;
    }
}
