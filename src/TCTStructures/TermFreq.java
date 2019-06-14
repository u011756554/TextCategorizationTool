//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

public class TermFreq {
    private String term;
    private Integer frequency;
    
    public TermFreq(){
        setTerm("");
        setFrequency(0);
    }
    
    public TermFreq(String term, Integer freq){
        setTerm(term);
        setFrequency(freq);
    }
    
    public void setTerm(String term){
        this.term = term;
    }
    
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }
    
    public String getFeature(){
        return term;
    }
    
    public Integer getFrequency(){
        return frequency;
    }
}
