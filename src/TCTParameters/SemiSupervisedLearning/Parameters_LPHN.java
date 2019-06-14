//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_LPHN implements Serializable{
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_LPHN(){
       setMaxNumIterations(1000);
    }
    
    public Parameters_LPHN(int maxNumberInterations){
        setMaxNumIterations(maxNumberInterations);
    }
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
}