package TCTStructures;

import java.util.HashMap;
import java.util.Set;

public class NeighborHash {
    private HashMap<Integer,Double> neighbors;
    
    public NeighborHash(){
        neighbors = new HashMap<Integer,Double>();
    }
    
    public void AddNeighbor(Integer index, Double value){
        neighbors.put(index, value);
    }
    
    public Double getNeighbor(Integer index){
        return neighbors.get(index);
    }
    
    public HashMap<Integer,Double> getNeighbors(){
        return neighbors;
    }
    
    public String toString(){
        StringBuffer temp = new StringBuffer();
        Set<Integer> chaves = neighbors.keySet();    
        for(Integer chave : chaves){
            temp.append(chave + "-" + neighbors.get(chave) + ",");
        }
        return temp.toString();
    }
}
