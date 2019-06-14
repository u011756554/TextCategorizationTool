//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

import java.io.Serializable;

public class AlphaBeta implements Serializable{
    
    private double alpha;
    private double beta;
    
    public AlphaBeta(){
        this.alpha = 0;
        this.beta = 0;
    }    
    
    public AlphaBeta(double alpha, double beta){
        setAlpha(alpha);
        this.beta = beta;
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setBeta(double beta){
        this.beta = beta;
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public double getBeta(){
        return beta;
    }
}
