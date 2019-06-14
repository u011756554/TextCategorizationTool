//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTConfigurations.ConfigurationBase;
import TCTParameters.Parameters_NumLabeledDocs;
import java.io.Serializable;

public class TransductiveConfiguration_Base extends ConfigurationBase implements Serializable{
    
    private String dirEntrada;
    private String dirSaida;
    private boolean Porcentage;
    
    Parameters_NumLabeledDocs parametersNumLabeledInstancesPerClass = new Parameters_NumLabeledDocs();
    
    TransductiveConfiguration_Base(){
        setPorcentage(false);
        setNumReps(10);
        setNumThreads(10);
        setDirEntrada("");
        setDirSaida("");
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

    public void setDirEntrada(String dirEntrada) {
        this.dirEntrada = dirEntrada;
    }

    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
    }

    public void setParametersNumLabeledInstancesPerClass(Parameters_NumLabeledDocs parametersNumLabeledInstancesPerClass) {
        this.parametersNumLabeledInstancesPerClass = parametersNumLabeledInstancesPerClass;
    }

    
    public String getDirEntrada() {
        return dirEntrada;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    
}
