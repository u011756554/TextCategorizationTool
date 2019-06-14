//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_GFHF implements Serializable{
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_GFHF(){
        setMaxNumIterations(1000);
    }
    
    public Parameters_GFHF(int maxNumberInterations){
        setMaxNumIterations(maxNumberInterations);
    }
    
    public Integer getMaxNumberIterations(){
        return maxNumberInterations;
    }
   
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
}
