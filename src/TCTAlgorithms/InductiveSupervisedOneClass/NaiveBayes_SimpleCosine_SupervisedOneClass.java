/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTNetworkGeneration.Proximity;
import TCTStructures.NeighborHash;
import java.util.HashMap;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class NaiveBayes_SimpleCosine_SupervisedOneClass extends OneClassSupervisedClassifier{

    NeighborHash center;
    double stdDev;
    
    private NeighborHash[] adjacencyListDocTerm;
    int numTerms;
    
    
    public NaiveBayes_SimpleCosine_SupervisedOneClass() {
        super();
        stdDev = 0;
                
    }

    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {
        
        this.numTerms = dataTrain.numAttributes() - 1;
        int numDocs = dataTrain.numInstances();
        
        // Creating a hash structure to store an adjacent list
        adjacencyListDocTerm = new NeighborHash[numDocs];
        for(int doc=0;doc<numDocs;doc++){
            adjacencyListDocTerm[doc] = new NeighborHash();
        }
        
        for(int doc=0;doc<numDocs;doc++){
            for(int term=0;term<numTerms;term++){
                double value = dataTrain.instance(doc).value(term);
                if(value > 0){
                    adjacencyListDocTerm[doc].AddNeighbor(term,value); 
                }
            }
        }
        
        
        double[] avgTerms = new double[numTerms];
        for(int doc=0;doc<numDocs;doc++){
            HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[doc].getNeighbors();
            Object[] keys = neighbors.keySet().toArray();
            for(int term=0;term<keys.length;term++){
                int idTerm = (Integer)keys[term];
                avgTerms[idTerm] += neighbors.get(idTerm);
            }
        }

        //Average of each term
        for(int term=0;term<numTerms;term++){
                avgTerms[term] = (double)avgTerms[term] / (double)numDocs;
        }

        //Creating the center
        center = new NeighborHash();
        for(int term=0;term<numTerms;term++){
            if(avgTerms[term] > 0){
                center.AddNeighbor(term,avgTerms[term]);
            }
        }    
        
        //Computing standard deviation
        for(int doc=0;doc<numDocs;doc++){
            stdDev += Math.pow(1 - Proximity.computeCosine(center,adjacencyListDocTerm[doc]),2);
        }
        stdDev = Math.sqrt(stdDev / numDocs);
        
    }
    
     @Override
    public double getScore(Instance test) {
        
        NeighborHash newInstance = new NeighborHash();
        
        for(int term=0;term<numTerms;term++){
            double value = test.value(term);
            if((value > 0)){
                newInstance.AddNeighbor(term,value); 
            }    
        }
        
        double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * stdDev));
        double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(center,newInstance) / stdDev), 2);
        

        double score = formula1 * Math.exp(formula2);
        if(Double.isInfinite(score) || Double.isNaN(score)){
             score = 0;
        }
        
        
        System.out.println("Classe do Exemplo: " + test.classValue());
        System.out.println("Score: " + score);
        
        return score;
    }
    
    
    
    
}
