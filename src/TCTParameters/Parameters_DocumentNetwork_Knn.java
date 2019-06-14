//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_DocumentNetwork_Knn implements Serializable{
    
    private ArrayList<Integer> ks;
    private boolean cosine;
    
    public Parameters_DocumentNetwork_Knn(){
        ks = new ArrayList<Integer>();
        ks.add(7);
        ks.add(17);
        ks.add(37);
        ks.add(57);
        
        setCosine(true);
    }
    
    public boolean isCosseno(){
        return this.cosine;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    public void addK(Integer valueK){
        ks.add(valueK);
    }
    
    public Integer getK(int pos){
        return ks.get(pos);
    }
    
    public ArrayList<Integer> getKs(){
        return ks;
    }
    
    public void setK(ArrayList<Integer> ks){
        this.ks = ks;
    }
    
}
