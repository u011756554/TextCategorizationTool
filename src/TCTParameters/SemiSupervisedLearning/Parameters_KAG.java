//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_KAG implements Serializable{
    private ArrayList<Integer> neighbors = new ArrayList<Integer>();
    private boolean KAG;
    private boolean KAOG;
    
    public Parameters_KAG(){
        addNeighbors(1);
        addNeighbors(3);
        addNeighbors(5);
        addNeighbors(7);
        addNeighbors(9);
        addNeighbors(11);
        addNeighbors(13);
        addNeighbors(15);
        addNeighbors(17);
        addNeighbors(19);
        addNeighbors(21);
        addNeighbors(25);
        addNeighbors(29);
        addNeighbors(35);
        addNeighbors(41);
        addNeighbors(49);
        addNeighbors(57);
        addNeighbors(73);
        addNeighbors(89);
        
        setKAG(true);
        setKAOG(true);
    }
    
    public void setKAG(boolean KAG){
        this.KAG = KAG;
    }
    
    public void setKAOG(boolean KAOG){
        this.KAOG = KAOG;
    }
    
    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }
    
    public int getNeighbor(int pos){
        return neighbors.get(pos);
    }
    
    public void setNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors = neighbors;
    }
    
    public void addNeighbors(int numVizinhos){
        neighbors.add(numVizinhos);
    }
    
    public boolean getKAG(){
        return KAG;
    }
    
    public boolean getKAOG(){
        return KAOG;
    }
}
