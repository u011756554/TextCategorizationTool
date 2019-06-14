/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import weka.core.Instance;

/**
 *
 * @author rafael
 */
public abstract class IMBHN_SupervisedOneClass extends OneClassSupervisedClassifier{
    
    private double[] fTerms; // Class information of terms
    private int numTerms; // Number of terms
    
    
    
    @Override
    public double getScore(Instance test){
        double score = 0;
        double total = 0;
        
        for(int term=0;term<numTerms; term++){ 
            total+= test.value(term);
        }
        
        for(int term=0;term<numTerms; term++){ 
            if(test.value(term) >0){
                score += (test.value(term)/total * fTerms[term]);
            }
        }
        
        return score;
    }

    public double[] getFTerms() {
        return fTerms;
    }

    public void setFTerms(double[] fTerms) {
        this.fTerms = fTerms;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }
    
    
}
