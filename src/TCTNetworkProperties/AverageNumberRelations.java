/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package TCTNetworkProperties;

import TCTStructures.Neighbor;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author ragero
 */
public class AverageNumberRelations {
    public static double BipartiteNetwork(Instances data){
        double avgDegree = 0;
        
        int numAtrs = data.numAttributes() - 1;
        int numInsts = data.numInstances();
        
        for(int inst=0;inst<numInsts;inst++){
            Instance instance = data.instance(inst);
            for(int atr=1;atr<numAtrs;atr++){
                if(instance.value(atr) > 0){
                    avgDegree++;
                }
            }
        }
        
        avgDegree = (double)avgDegree/(double)numInsts;
        
        return avgDegree;
    }
    
    public static double TermTermNetwork(Neighbor[] adjacencyListTerms){
        double avgDegree = 0;
        
        int listSize = adjacencyListTerms.length;
        for(int atr=0;atr<listSize;atr++){
            avgDegree += adjacencyListTerms[atr].getNeighbors().size();
        }
        
        avgDegree = (double)avgDegree/(double)listSize;
        
        return avgDegree;
    }
    
    public static double DocDocNetwork(Neighbor[] adjacencyListDocs){
        double avgDegree = 0;
        
        int listSize = adjacencyListDocs.length;
        for(int atr=0;atr<listSize;atr++){
            avgDegree += adjacencyListDocs[atr].getNeighbors().size();
        }
        
        avgDegree = (double)avgDegree/(double)listSize;
        
        return avgDegree;
    }
}
