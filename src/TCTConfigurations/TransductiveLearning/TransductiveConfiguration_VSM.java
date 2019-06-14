//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.SemiSupervisedLearning.Parameters_EM;
import TCTParameters.SemiSupervisedLearning.Parameters_TSVM;
import java.io.Serializable;

public class TransductiveConfiguration_VSM extends TransductiveConfiguration_Base implements Serializable{
    private String dirEntrada;
    private String dirSaida;
    
    private boolean EM;
    private boolean TSVM;
    
    Parameters_EM parametersEM;
    Parameters_TSVM parametersTSVM;
    
    public TransductiveConfiguration_VSM(){
        super();
        setDirEntrada("");
        setDirSaida("");
        
        setParametersEM(new Parameters_EM());
        setParametersTSVM(new Parameters_TSVM());
    }
    
    public boolean isEM(){
        return EM;
    }
    
    public boolean isTSVM(){
        return TSVM;
    }
    
    public void setEM(boolean EM){
        this.EM = EM;
    }
    
    public void setTSVM(boolean TSVM){
        this.TSVM = TSVM;
    }
    
    public String getDirEntrada() {
        return dirEntrada;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    public void setDirEntrada(String dirEntrada) {
        this.dirEntrada = dirEntrada;
    }

    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
    }
    
    public Parameters_EM getParametersEM(){
        return this.parametersEM;
    }
    
    public Parameters_TSVM getParametersTSVM(){
        return this.parametersTSVM;
    }
    
    public void setParametersEM(Parameters_EM parametersEM){
        this.parametersEM = parametersEM;
    }
    
    public void setParametersTSVM(Parameters_TSVM parametersTSVM){
        this.parametersTSVM = parametersTSVM;
    }
}
