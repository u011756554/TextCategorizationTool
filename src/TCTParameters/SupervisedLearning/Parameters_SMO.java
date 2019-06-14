//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SupervisedLearning;


import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_SMO implements Serializable{
    private ArrayList<Double> valueesC = new ArrayList<Double>();
    private boolean polyKernel;
    private boolean rbfKernel;
    private boolean linearKernel;
    private boolean minKernel;
    
    public Parameters_SMO(){
        valueesC.add(0.00001d);
        valueesC.add(0.0001d);
        valueesC.add(0.001d);
        valueesC.add(0.01d);
        valueesC.add(0d);
        valueesC.add(0.1d);
        valueesC.add(1d);
        valueesC.add(10d);
        valueesC.add(100d);
        valueesC.add(1000d);
        valueesC.add(10000d);
        valueesC.add(100000d);
        
        polyKernel = true;
        rbfKernel = true;
        linearKernel = true;
    }
    
    public Parameters_SMO(ArrayList<Double> cs, boolean polyKernel, boolean rbfKernel, boolean linearKernel, boolean minKernel){
        this.valueesC = cs;
        this.polyKernel= polyKernel;
        this.rbfKernel = rbfKernel;
        this.linearKernel = linearKernel;
        this.minKernel = minKernel;
    }

    public boolean isMinKernel(){
        return minKernel;
    }
    
    public boolean isLinearKernel() {
        return linearKernel;
    }

    public boolean isPolyKernel() {
        return polyKernel;
    }

    public boolean isRbfKernel() {
        return rbfKernel;
    }

    public ArrayList<Double> getvalueesC() {
        return valueesC;
    }
    
    public Double getvalueC(int pos) {
        return valueesC.get(pos);
    }
    
    public void setMinKernel(boolean minKernel){
        this.minKernel = minKernel;
    }
    
    public void setLinearKernel(boolean linearKernel) {
        this.linearKernel = linearKernel;
    }

    public void setPolyKernel(boolean polyKernel) {
        this.polyKernel = polyKernel;
    }

    public void setRbfKernel(boolean rbfKernel) {
        this.rbfKernel = rbfKernel;
    }

    public void setValueesC(ArrayList<Double> valueesC) {
        this.valueesC = valueesC;
    }
    
    public void addvalueC(double c){
        valueesC.add(c);
    }
}
