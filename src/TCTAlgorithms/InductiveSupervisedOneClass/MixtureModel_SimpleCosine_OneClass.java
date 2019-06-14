/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTNetworkGeneration.Proximity;
import TCTStructures.NeighborHash;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class MixtureModel_SimpleCosine_OneClass extends OneClassSupervisedClassifier{

    private NeighborHash[] centers;
    private double[] stdDevs;
    private double[] probDists;
    
    private NeighborHash[] adjacencyListDocTerm;
    private int numTerms;
    private int numDocs;
    
    private double[][] docDist;
    private int numComponents;
    private int numMaxIterations;
    private double minDiff;
    
    
    public MixtureModel_SimpleCosine_OneClass() {
        super();
        numComponents = 10;
        numMaxIterations = 10;
        minDiff = 0.0001;
    }

    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {
        
        this.numTerms = dataTrain.numAttributes() - 1;
        this.numDocs = dataTrain.numInstances();
        
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
        
        
        stdDevs = new double[this.numComponents];
        
        /*ArrayList<Integer>[] docsPerDist = new ArrayList[this.numComponents];
        for(int dist=0;dist<this.numComponents;dist++){
            docsPerDist[dist] = new ArrayList<Integer>();
        }*/
        
        centers = new NeighborHash[this.numComponents];
        
        Random random = new Random(0);
        //Criando os centroides na moeda
        /*
        for(int doc=0;doc<numDocs;doc++){
            docsPerDist[random.nextInt(this.numComponents)].add(doc);
        }*/
        
        docDist = new double[numDocs][this.numComponents];
        
        for(int doc=0;doc<this.numDocs;doc++){
            double sum = 0;
            for(int dist=0;dist<numComponents;dist++){
                docDist[doc][dist] = random.nextDouble();
                sum+= docDist[doc][dist];
            }
            
            for(int dist=0;dist<numComponents;dist++){
                docDist[doc][dist] = docDist[doc][dist]/sum;
            }
        }
        
        
        probDists = new double[this.numComponents];
        for(int dist=0;dist<this.numComponents;dist++){
            probDists[dist] = 1 / (double)this.numComponents;
        }
        
        /*computeCenters(docsPerDist);
        computeStdDevs(docsPerDist);*/
        computeCenters();
        //computeDists(docDist);
        computeProbDists(docDist);
        computeStdDevs();
        //computeProbDists(docDist);
        
        
        int numIt = 0;
        boolean sair = false;
        
        while(sair == false){

            double acmDiff = 0;
            
            //Maximization
            
            for(int doc=0;doc<numDocs;doc++){
                double sum = 0;
                for(int dist=0;dist<numComponents;dist++){
                    double prob = computeProbDist(doc, dist);
                    sum += prob;
                    docDist[doc][dist] = prob;
                }
                
                for(int dist=0;dist<numComponents;dist++){
                    docDist[doc][dist] = docDist[doc][dist] / sum;
                }
                
                //Compute de diferences here
                
            }
            
            //Expectation

            computeCenters();
            computeStdDevs();
            
            
            if(numIt == numMaxIterations){
                sair = true;
            }
            
            /*if(numIt == numMaxIterations || acmDiff < this.minDiff){
                sair = true;
            }*/
            
            numIt++;
        }
        
        
        System.err.println("NumIt: " + numIt);
    }
    
    public void computeProbDists(double[][] docDist){
        double sum = 0;
        for(int dist=0;dist<this.numComponents;dist++){
            double amcProb = 0;
            for(int doc=0;doc<this.numDocs;doc++){
                amcProb += docDist[doc][dist];
            }
            probDists[dist] = amcProb / numDocs;
            sum += probDists[dist];
            
            
        }
        for(int dist=0;dist<this.numComponents;dist++){
            probDists[dist] = probDists[dist]/sum;
        }
        
    }
    
    public void computeDists(double[][] docDist){
        for(int dist=0;dist<this.numComponents;dist++){
            for(int doc=0;doc<this.numDocs;doc++){
                docDist[doc][dist] = computeProbDist(doc,dist);
            }
        }
    }
    
    public double computeProbDist(int idDoc, int dist){
        double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * stdDevs[dist]));
        double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(centers[dist],adjacencyListDocTerm[idDoc]) / stdDevs[dist]), 2);
        

        double score = formula1 * Math.exp(formula2);
        if(Double.isInfinite(score) || Double.isNaN(score)){
             score = 0;
        }
        
        return score;
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
        
        double score = 0;
        double maxScore = -1;
        for(int dist=0;dist<this.numComponents;dist++){
        
            double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * stdDevs[dist]));
            double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(centers[dist],newInstance) / stdDevs[dist]), 2);

            double prob = formula1 * Math.exp(formula2);
            
            if(Double.isInfinite(prob) || Double.isNaN(prob)){
                 prob = 0;
            }
            
            
            score = prob * probDists[dist];
            
            if(score > maxScore){
                maxScore = score;
            }
            
        }
        
        //System.out.println("Classe do Exemplo: " + test.classValue());
        System.out.println("Score: " + maxScore);
        
        return maxScore;
        
    }
    
    /*@Override
    public double getScore(Instance test) {
        NeighborHash newInstance = new NeighborHash();
        
        for(int term=0;term<numTerms;term++){
            double value = test.value(term);
            if((value > 0)){
                newInstance.AddNeighbor(term,value); 
            }    
        }
        
        double score = 0;
        for(int dist=0;dist<this.numComponents;dist++){
        
            double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * stdDevs[dist]));
            double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(centers[dist],newInstance) / stdDevs[dist]), 2);

            double prob = formula1 * Math.exp(formula2);
            
            if(Double.isInfinite(prob) || Double.isNaN(prob)){
                 prob = 0;
            }
            
            
            score += prob * probDists[dist];
            
        }
        
        //System.out.println("Classe do Exemplo: " + test.classValue());
        System.out.println("Score: " + score);
        
        return score;
        
    }*/

    public void computeCenters(){
        
        // Recalculando os centroides;
        for(int dist=0;dist<this.numComponents;dist++){
            // Frequencia total dos termos dos documentos de um determinado tópico
            
            double[] avgTerms = new double[numTerms];
            double totalDist = 0;
            for(int doc=0;doc<this.numDocs;doc++){
                
                HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[doc].getNeighbors();
                Object[] keys = neighbors.keySet().toArray();
                for(int term=0;term<keys.length;term++){
                    int idTerm = (Integer)keys[term];
                    if(neighbors.get(idTerm) > 0){
                        avgTerms[idTerm] += neighbors.get(idTerm) * docDist[doc][dist];
                        //avgTerms[idTerm] += neighbors.get(idTerm);
                        //clusterNumDocs++;
                    }
                    
                    
                    
                }
                totalDist += docDist[doc][dist];
            }

            //Tirando a média das frequências dos termos por tópico;
            for(int term=0;term<numTerms;term++){
                    //avgTerms[term] = (double)avgTerms[term] / (double)clusterNumDocs;
                    avgTerms[term] = (double)avgTerms[term] / (double)totalDist;
            }

            //Criando o novo centróide do tópico
            centers[dist] = new NeighborHash();
            
            for(int term=0;term<numTerms;term++){
                if(avgTerms[term] > 0){
                    centers[dist].AddNeighbor(term,avgTerms[term]);
                }
            }    
                
            
            /*if(centroids[trial][topic].getNeighbors().size() < numTerms){
                System.out.println("Aqui kcta 3");
            }*/

        }
        
    }
    
    public void computeStdDevs(){
        //Computing standard deviation per distribution
        double sum = 0;
        for(int dist=0;dist<this.numComponents;dist++){    
            double stdDev = 0;
            for(int doc=0;doc<this.numDocs;doc++){
                stdDev += Math.pow(1 - Proximity.computeCosine(centers[dist],adjacencyListDocTerm[doc]),2);
            }
            stdDev = Math.sqrt(stdDev / this.numDocs);    
            stdDevs[dist] = stdDev * probDists[dist];
            sum +=  probDists[dist];
        }
        
        for(int dist=0;dist<this.numComponents;dist++){    
            stdDevs[dist] = stdDevs[dist] / sum;
        }
        
        
        
    }
    
    
    public void computeCenters(ArrayList<Integer>[] docsPerDist){
        
        // Recalculando os centroides;
        for(int dist=0;dist<this.numComponents;dist++){
            // Frequencia total dos termos dos documentos de um determinado tópico
            double[] avgTerms = new double[numTerms];
            for(int doc=0;doc<docsPerDist[dist].size();doc++){
                int idDoc = docsPerDist[dist].get(doc);
                HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                Object[] keys = neighbors.keySet().toArray();
                for(int term=0;term<keys.length;term++){
                    int idTerm = (Integer)keys[term];
                    avgTerms[idTerm] += neighbors.get(idTerm);
                }
            }

            //Tirando a média das frequências dos termos por tópico;
            for(int term=0;term<numTerms;term++){
                if(docsPerDist[dist].size() == 0){
                    avgTerms[term] = 0;
                }else{
                    avgTerms[term] = (double)avgTerms[term] / (double)docsPerDist[dist].size();
                }
            }

            //Criando o novo centróide do tópico
            centers[dist] = new NeighborHash();
            
            for(int term=0;term<numTerms;term++){
                if(avgTerms[term] > 0){
                    centers[dist].AddNeighbor(term,avgTerms[term]);
                }
            }    
                
            
            /*if(centroids[trial][topic].getNeighbors().size() < numTerms){
                System.out.println("Aqui kcta 3");
            }*/

        }
        
    }
    
    public void computeStdDevs(ArrayList<Integer>[] docsPerDist){
        //Computing standard deviation per distribution
        for(int dist=0;dist<this.numComponents;dist++){    
            double stdDev = 0;
            for(int doc=0;doc<docsPerDist[dist].size();doc++){
                int idDoc = docsPerDist[dist].get(doc);
                //double prox = Math.pow(1 - Proximity.computeCosine(adjacencyListDocTerm[idDoc],centers[dist]),2);
                stdDev += Math.pow(1 - Proximity.computeCosine(centers[dist],adjacencyListDocTerm[idDoc]),2);
            }
            stdDev = Math.sqrt(stdDev / docsPerDist[dist].size());    
            stdDevs[dist] = stdDev;
        }
        
    }
    
    
    public NeighborHash[] getCenters() {
        return centers;
    }

    public void setCenters(NeighborHash[] centers) {
        this.centers = centers;
    }

    public double[] getStdDevs() {
        return stdDevs;
    }

    public void setStdDevs(double[] stdDevs) {
        this.stdDevs = stdDevs;
    }

    public NeighborHash[] getAdjacencyListDocTerm() {
        return adjacencyListDocTerm;
    }

    public void setAdjacencyListDocTerm(NeighborHash[] adjacencyListDocTerm) {
        this.adjacencyListDocTerm = adjacencyListDocTerm;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }

    public int getNumComponents() {
        return numComponents;
    }

    public void setNumComponents(int numComponents) {
        this.numComponents = numComponents;
    }
    
    
    
    
    
}
