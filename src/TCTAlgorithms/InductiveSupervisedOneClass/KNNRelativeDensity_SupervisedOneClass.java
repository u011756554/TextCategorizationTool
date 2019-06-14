/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTStructures.IndexValue;
import TCTStructures.NeighborHash;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instance;

/**
 *
 * @author Marcos Gôlo
 */
public class KNNRelativeDensity_SupervisedOneClass extends KNN_BasedSupervisedOneClass{
    
    HashMap<Integer,Double> hashDensity = new HashMap<Integer,Double>();
    
    @Override
    public double classifyInstance(Instance test){
        
        
        double score = getScore(test);
        
        //System.out.println("Classe: " + test.classValue());
        //System.out.println("score: " + score);
        
        if(score>=getThreshold()){
            return 0;
        }else{
            return 1;
        }
        
    }

    @Override
    public double getScore(Instance test) {
        NeighborHash ninst = newInstance(test);
        ArrayList<IndexValue> neighbors = calculaSimilaridade(ninst);
        double densityX= sumDensity(neighbors);
        double sumDensity = 0.0;
        densityX=densityX/getK();
        for(int g=0;g<neighbors.size();g++){
            double densityY=0;
            if(!hashDensity.containsKey(neighbors.get(g).getIndex())){
                ArrayList<IndexValue> neighborsTest = calculaSimilaridade(adjancecyListDocTerm[neighbors.get(g).getIndex()]);
                densityY = sumDensity(neighborsTest);
                hashDensity.put(neighborsTest.get(g).getIndex(), densityY);
            }else{
                 densityY=hashDensity.get(neighbors.get(g).getIndex());
            }
            densityY=densityY/getK();
            sumDensity+=densityY;
        }
        sumDensity=sumDensity/getK();
        double score=densityX/sumDensity;
        
        return score;
    }
    
}
