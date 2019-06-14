//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_RIPPER implements Serializable{
    private ArrayList<Integer> numOpts = new ArrayList<Integer>();
    private ArrayList<Integer> numFolds = new ArrayList<Integer>();
    
    public Parameters_RIPPER(){
        addOpt(2);
        addOpt(10);
        addFold(3);
        addFold(10);
    }
    
    public void addOpt(Integer opt){
        numOpts.add(opt);
    }
    
    public void addFold(Integer fold){
        numFolds.add(fold);
    }
    
    public Integer getOpt(int pos){
        return numOpts.get(pos);
    }
    
    public Integer getFold(int pos){
        return numFolds.get(pos);
    }
    
    public ArrayList<Integer> getOpts(){
        return numOpts;
    }
    
    public ArrayList<Integer> getFolds(){
        return numFolds;
    }
    
    public void SetOpts(ArrayList<Integer> opts){
        this.numOpts = opts;
    }
    
    public void SetFolds(ArrayList<Integer> folds){
        this.numFolds = folds;
    }
}
