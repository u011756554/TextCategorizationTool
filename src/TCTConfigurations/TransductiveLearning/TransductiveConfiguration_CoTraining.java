//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import java.io.Serializable;

public class TransductiveConfiguration_CoTraining extends TransductiveConfiguration_SelfTraining implements Serializable{
    
    private int numRandomViews;
    
    public TransductiveConfiguration_CoTraining(){
        super();
        setNumRandomViews(10);
    }
    
    public Integer getNumRandomViews(){
        return this.numRandomViews;
    }
    
    public void setNumRandomViews(int numRandomViews){
        this.numRandomViews = numRandomViews;
    }
}
