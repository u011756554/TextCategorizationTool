//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_Rocchio implements Serializable{
    
    ArrayList<Double> betas;
    
    public Parameters_Rocchio(){
        setBetas(new ArrayList<Double>());
        addBeta(0.2);
        addBeta(0.4);
        addBeta(0.6);
        addBeta(0.8);
        addBeta(1);
    }
    
    public void addBeta(double beta){
        betas.add(beta);
    }
    
    public void setBetas(ArrayList<Double> betas){
        this.betas = betas;
    }
    
    public Double getBeta(int pos){
        return betas.get(pos);
    }
    
    public ArrayList<Double> getBetas(){
        return betas;
    }
}
