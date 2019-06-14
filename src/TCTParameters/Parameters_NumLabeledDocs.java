//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_NumLabeledDocs implements Serializable{
    
    private ArrayList<Double> NumLabeledInstancesPerClass;
    
    public Parameters_NumLabeledDocs(){
        setNumLabeledInstancesPerClass(new ArrayList<Double>());
        addNumLabeledInstancesPerClass(1.0);
        addNumLabeledInstancesPerClass(10.0);
        addNumLabeledInstancesPerClass(20.0);
        addNumLabeledInstancesPerClass(30.0);
        addNumLabeledInstancesPerClass(40.0);
        addNumLabeledInstancesPerClass(50.0);
    }
    
    public ArrayList<Double> getNumLabeledInstancesPerClass() {
        return NumLabeledInstancesPerClass;
    }
    
    public double getNumLabeledInstancesPerClass(int pos) {
        return NumLabeledInstancesPerClass.get(pos);
    }
    
    public void setNumLabeledInstancesPerClass(ArrayList<Double> NumLabeledInstancesPerClass) {
        this.NumLabeledInstancesPerClass = NumLabeledInstancesPerClass;
    }
    
    public void addNumLabeledInstancesPerClass(double numEx){
        NumLabeledInstancesPerClass.add(numEx);
    }
}
