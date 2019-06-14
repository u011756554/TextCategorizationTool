//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class SupervisedInductiveConfiguration extends SupervisedInductiveConfigurationBase implements Serializable{
    
    private String dirEntrada;
    private String dirSaida;
    
    public SupervisedInductiveConfiguration(){
        super();
        
        setDirEntrada("");
        setDirSaida("");
    }
    
    //Getters & Setters
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

    
}
