//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

public class AlphaBetaGamma {
    
    private double alpha;
    private double beta;
    private double gamma;
    
    public AlphaBetaGamma(){
        setAlpha(0);
        setBeta(0);
        setGamma(0);
    }    
    
    public AlphaBetaGamma(double alpha, double beta, double gamma){
        setAlpha(alpha);
        setBeta(beta);
        setGamma(gamma);
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setBeta(double beta){
        this.beta = beta;
    }
    
    public void setGamma(double gamma){
        this.gamma = gamma;
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public double getBeta(){
        return beta;
    }
    
    public double getGamma(){
        return gamma;
    }
}
