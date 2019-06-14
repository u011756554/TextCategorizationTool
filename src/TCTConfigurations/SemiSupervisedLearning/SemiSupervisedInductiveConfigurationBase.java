//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SemiSupervisedLearning;

import TCTConfigurations.ConfigurationBase;
import TCTParameters.Parameters_NumLabeledDocs;
import java.io.Serializable;

public class SemiSupervisedInductiveConfigurationBase extends ConfigurationBase implements Serializable{

    private boolean Porcentage;
    
    Parameters_NumLabeledDocs parametersNumLabeledInstancesPerClass = new Parameters_NumLabeledDocs();
    
    SemiSupervisedInductiveConfigurationBase(){
        setPorcentage(false);
        setNumReps(10);
        setNumFolds(10);
        setNumThreads(10);
    }
    
    //Getters e Setters
    public boolean isPorcentage() {
        return Porcentage;
    }
    
    public void setPorcentage(boolean Porcentage) {
        this.Porcentage = Porcentage;
    }
    
    
    public Parameters_NumLabeledDocs getParametersNumLabeledInstancesPerClass(){
        return this.parametersNumLabeledInstancesPerClass;
    }
}
