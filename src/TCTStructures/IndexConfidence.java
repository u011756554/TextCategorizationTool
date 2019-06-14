//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

public class IndexConfidence {
    int index;
    double[] confidences;
    int classe;
    boolean tag;
    
    public IndexConfidence(int index, double[] confidences){
        this.index = index;
        this.confidences = confidences;
        tag = false;
    }
    
    public void setTag(boolean tag){
        this.tag = tag;
    }
    
    public void setClass(int classe){
        this.classe = classe;
    }
    
    public int getClasse(){
        return classe;
    }
    
    public void setindex(int index){
        this.index = index;
    }
    
    public int getindex(){
        return this.index;
    }
    
    public void setConfiancas(double[] confidences){
        this.confidences = confidences;
    }
    
    public double[] getConfiancas(){
        return confidences;
    }
    
    public boolean getTag(){
        return tag;
    }
}
