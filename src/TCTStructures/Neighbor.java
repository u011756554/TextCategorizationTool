//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

import java.util.ArrayList;

public class Neighbor {
    private ArrayList<IndexValue> neighbors;
    
    public Neighbor(){
        neighbors = new ArrayList<IndexValue>();
    }
    
    public void AddNeighbor(IndexValue indVal){
        neighbors.add(indVal);
    }
    
    public IndexValue getNeighbor(int index){
        return neighbors.get(index);
    }
    
    public ArrayList<IndexValue> getNeighbors(){
        return neighbors;
    }
}
