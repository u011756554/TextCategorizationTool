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
import java.util.HashSet;
import java.util.Set;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Marcos Gôlo
 */
public class KSNN_SupervisedOneClass extends KNN_BasedSupervisedOneClass {

    HashMap<Integer, HashSet<Integer>> hashIndexNeighbors = new HashMap<Integer, HashSet<Integer>>();

    public void buildClassifier(Instances dataTrain) {
        super.buildClassifier(dataTrain);
        for (int inst = 0; inst < getNumTrain(); inst++) {
            HashSet<Integer> hashIndex = new HashSet<Integer>();
            ArrayList<IndexValue> neighbors = calculaSimilaridade(adjancecyListDocTerm[inst]);
            for (IndexValue x : neighbors) {
                hashIndex.add(x.getIndex());
            }
            hashIndexNeighbors.put(inst, hashIndex);
        }
        
    }

    @Override
    public double classifyInstance(Instance test){
        
        double score = getScore(test);
        
        //System.out.println("Classe: " + test.classValue());
        //System.out.println("Score: " + score);
        
        if(score >getThreshold()){
            return 0;
        }else{
            return 1;
        }
        
    }
    
    private void addMedida(ArrayList<Double> array, int k, double snn){
        if(array.size()==0){
            array.add(snn);
        }else {
            int i=0;
            while(i<array.size() && array.get(i) < snn){
                i++;
            }
            array.add(i,snn);
            if(array.size()==k+1){
                array.remove(0);
            }
        }
    }
     
    

    @Override
    public double getScore(Instance test) {
        
        double score = 0;
        double snn=0;
        
        HashSet<Integer> hashIndex = new HashSet<Integer>();
        NeighborHash nInst = newInstance(test);
        ArrayList<IndexValue> neighbors = calculaSimilaridade(nInst);
        ArrayList<Double> similaritySNN = new ArrayList<Double>();
        
        for (IndexValue x : neighbors) {
            hashIndex.add(x.getIndex());
        } 
        
        
        Set<Integer> keys = hashIndexNeighbors.keySet();
        for(Integer key: keys){
            double qtdNeighbors = 0;
            HashSet<Integer> hashAux = hashIndexNeighbors.get(key);
            for(Integer keyTwo: hashIndex){
                if(hashAux.contains(keyTwo)){
                    qtdNeighbors++;
                }
            }
            if(qtdNeighbors > 0){
                addMedida(similaritySNN,getK(),qtdNeighbors/(double)getK());    
            }
            
        }
        
        for(double x : similaritySNN){
            snn+=x;
        }
        
        score = snn/(double)getK();
        
        return score;
    }
}