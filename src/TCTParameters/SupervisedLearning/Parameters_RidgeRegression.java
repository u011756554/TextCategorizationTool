//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_RidgeRegression implements Serializable{
    ArrayList<Double> ridges;
    
    public Parameters_RidgeRegression(){
        setRidges(new ArrayList<Double>());
        addRidge(10);
        addRidge(100);
        addRidge(1000);
    }
    
    public void addRidge(double ridge){
        this.ridges.add(ridge);
    }
    
    public void setRidges(ArrayList<Double> ridges){
        this.ridges = ridges;
    }
    
    public double getRidge(int pos){
        return this.ridges.get(pos);
    }
    
    public ArrayList<Double> getRidges(){
        return this.ridges;
    }
    
}
