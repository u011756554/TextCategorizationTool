//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_DocumentNetwork_Exp implements Serializable {
    
    private ArrayList<Double> sigmas;
    
    private boolean cosine;
    
    public Parameters_DocumentNetwork_Exp(){
        sigmas = new ArrayList<Double>();
        sigmas.add(0.05);
        sigmas.add(0.2);
        sigmas.add(0.35);
        sigmas.add(0.5);
    }
    
    public boolean isCosseno(){
        return this.cosine;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    public void addSigma(Double valueSigma){
        sigmas.add(valueSigma);
    }
    
    public Double getSigma(int pos){
        return sigmas.get(pos);
    }
    
    public ArrayList<Double> getSigmas(){
        return sigmas;
    }
    
    public void setSigma(ArrayList<Double> sigmas){
        this.sigmas = sigmas;
    }
    
}