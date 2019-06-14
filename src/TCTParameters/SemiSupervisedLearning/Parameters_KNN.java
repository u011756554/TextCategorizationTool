//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ragero
 */
public class Parameters_KNN implements Serializable{
    private ArrayList<Integer> neighbors = new ArrayList<Integer>();
    private boolean weighted;
    private boolean unweighted;
    private boolean cosine;
    private boolean euclidean;
    
    public Parameters_KNN(){
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
        setWeighted(true);
        setUnweighted(true);
        setCosine(true);
        setEuclidean(false);
    }
    
    public Parameters_KNN(ArrayList<Integer> neighbors, boolean comPeso, boolean semPeso){
        setNeighbors(neighbors);
        setWeighted(comPeso);
        setUnweighted(semPeso);
    }

    public boolean isWeighted() {
        return weighted;
    }

    public boolean isUnweighted() {
        return unweighted;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }
    
    public boolean getEuclidean(){
        return euclidean;
    }
    
    public boolean getCosine(){
        return cosine;
    }
    
    public int getNeighbor(int pos){
        return neighbors.get(pos);
    }

    public void setWeighted(boolean comPeso) {
        this.weighted = comPeso;
    }

    public void setUnweighted(boolean semPeso) {
        this.unweighted = semPeso;
    }

    public void setNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors = neighbors;
    }
    
    public void setEuclidean(boolean euclidean){
        this.euclidean = euclidean;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    public void addNeighbors(int numVizinhos){
        neighbors.add(numVizinhos);
    }
}
