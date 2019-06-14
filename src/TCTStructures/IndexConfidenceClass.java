//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

public class IndexConfidenceClass {
    
    private int instance;
    private double confidence;
    private int classe;
    
    public IndexConfidenceClass(){
        instance = 0;
        confidence = 0;
        classe = 0;
    }
    
    public IndexConfidenceClass(int instance, double confidence, int classe){
        setInstance(instance);
        setConfidence(confidence);
        setClass(classe);
    }
    
    //Setters & Getters
    public int getInstancia(){
        return instance;
    }
    
    public double getConfianca(){
        return confidence;
    }
    
    public int getClasse(){
        return classe;
    }
    
    public void setInstance(int instance){
        this.instance = instance;
    }
    
    public void setConfidence(double confidence){
        this.confidence = confidence;
    }
    
    public void setClass(int classe){
        this.classe = classe;
    }
    
}
