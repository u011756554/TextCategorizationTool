//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_J48 implements Serializable{
    
    private ArrayList<Double> confidences;
    
    public Parameters_J48(){
        setConfidences(new ArrayList<Double>());
        addConfidence(0.25);
        addConfidence(0.20);
        addConfidence(0.15);
    }
    
    public void addConfidence(double confidence){
        confidences.add(confidence);
    }
    
    public void setConfidences(ArrayList<Double> confidences){
        this.confidences = confidences;
    }
    
    public Double getConfidence(int pos){
        return this.confidences.get(pos);
    }
    
    public ArrayList<Double> getConfidences(){
        return this.confidences;
    }
}
