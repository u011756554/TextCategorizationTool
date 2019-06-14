//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

import java.util.ArrayList;

public class FeatureList {
    private ArrayList<TermFreq> features;

    public void FeatureList(){
        features = new ArrayList<TermFreq>();
    }
    
    public void setFeatures(ArrayList<TermFreq> features){
        this.features = features;
    }
    
    public ArrayList<TermFreq> getFeatures(){
        return features;
    }
    
    public TermFreq getFeature(int pos){
        return features.get(pos);
    }
}
