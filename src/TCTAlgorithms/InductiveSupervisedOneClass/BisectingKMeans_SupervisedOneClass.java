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
public class BisectingKMeans_SupervisedOneClass extends PrototypeBasedClusteringClassifier {
    
    private ArrayList<NeighborHash> centroids;
    
    private NeighborHash[] adjacencyListDocTerm;
    private NeighborHash[] adjacencyListTermDoc;
    
    private double[][] docGroup;
    
    private int numDocs;
    private int numTerms;
    
    private boolean cohesionSplitting;
    
    private int numDivisions; // For bisecting -> numDivisions = 2
    
    public BisectingKMeans_SupervisedOneClass(){
        super();
        this.setK(5);
        this.setNumDivisions(2);
        this.setNumMaxIterations(100);
        this.setChangeRate(0);
        this.setCosine(false);
        this.setCohesionSplitting(true);
    }

    @Override
    public void buildClassifier(Instances dataTrain) {
        
        setNumTerms(dataTrain.numAttributes() - 1);
        setNumDocs(dataTrain.numInstances());
        
        // Criando a estrutura em hash para calcular as similaridades
        adjacencyListDocTerm = new NeighborHash[getNumDocs()];
        for(int doc=0;doc<getNumDocs();doc++){
            adjacencyListDocTerm[doc] = new NeighborHash();
        }
        
        adjacencyListTermDoc = new NeighborHash[getNumTerms()];
        for(int term=0;term<getNumTerms();term++){
            adjacencyListTermDoc[term] = new NeighborHash();
        }
        
        // Normalizando
        double[] totalWeightDoc = new double[getNumDocs()];
        for(int doc=0;doc<getNumDocs();doc++){
            for(int term=1;term<getNumTerms();term++){
                if(dataTrain.instance(doc).value(term) > 0){
                    totalWeightDoc[doc] += dataTrain.instance(doc).value(term);
                }    
            }
        }
        
        for(int doc=0;doc<getNumDocs();doc++){
            for(int term=1;term<getNumTerms();term++){
                double value = dataTrain.instance(doc).value(term);
                if(value > 0){
                    //value = value / totalWeightDoc[doc]; // caso queira normalizar (se não é só comentar)
                    adjacencyListDocTerm[doc].AddNeighbor(term,value); 
                    adjacencyListTermDoc[term].AddNeighbor(doc,value);
                    /*if(maxValueTerms[term] < value){ // Armazenando o range de valores dos termos para a inicialização dos centroides
                        maxValueTerms[term] = value;
                    }*/
                }    
            }
        }

        // Definindo todos os elementos como pertencentes à um único grupo
        centroids = new ArrayList<NeighborHash>();
        centroids.add(new NeighborHash());
        for(int term=1;term<getNumTerms();term++){
            double value = Math.random();
            centroids.get(0).AddNeighbor(term,value);
        }
        ArrayList<ArrayList<Integer>> docsPerGroup = new ArrayList<ArrayList<Integer>>();
        docsPerGroup.add(new ArrayList<Integer>());
        for(int doc=0;doc<getNumDocs();doc++){
            docsPerGroup.get(0).add(doc);
        }
        
        docGroup = new double[getNumDocs()][this.getK()];
        
        boolean sair1 = false;
        while(sair1 == false){
            
            //System.out.println("Current Number of Groups: " + centroids.size());
            
            //System.out.println("Computing group to be divide..");
            int idGroupDivision = findIDGroupDivision(docsPerGroup);
            //System.out.println("Group to be divided: " + idGroupDivision);
            
            //System.out.println("Computing centroids...");
            
            /*NeighborHash bestCentroid1 = new NeighborHash();
            NeighborHash bestCentroid2 = new NeighborHash();
            ArrayList<Integer> bestDocsPerGroup1 = new ArrayList<Integer>();
            ArrayList<Integer> bestDocsPerGroup2 = new ArrayList<Integer>();*/
            
            ArrayList<NeighborHash> bestCentroids = new ArrayList<NeighborHash>();
            ArrayList<ArrayList<Integer>> bestDocsPerGroups = new ArrayList<ArrayList<Integer>>();
             
            ArrayList<NeighborHash> centroidsTemp = new ArrayList<NeighborHash>();
            ArrayList<ArrayList<Integer>> docsPerGroupTemp = new ArrayList<ArrayList<Integer>>();
            
            double previousCohesion = -1;
            
            for(int trial=0;trial<this.getNumTrials();trial++){
                
                
                
                for(int c=0;c<this.getNumDivisions();c++){
                    centroidsTemp.add(new NeighborHash());
                }

                for(int topic=0;topic<this.getNumDivisions();topic++){
                    docsPerGroupTemp.add(new ArrayList<Integer>());
                }

                Random random = new Random(trial);
                for(int doc=0;doc<docsPerGroup.get(idGroupDivision).size();doc++){
                    int idDoc = docsPerGroup.get(idGroupDivision).get(doc);
                    int idGroup = random.nextInt(this.getNumDivisions());
                    docsPerGroupTemp.get(idGroup).add(idDoc);
                }    
                // Substituir isso pela média incremental
                for(int topic=0;topic<this.getNumDivisions();topic++){
                    // Frequencia total dos termos dos documentos de um determinado tópico
                    double[] avgTerms = new double[getNumTerms()];
                    for(int doc=0;doc<docsPerGroupTemp.get(topic).size();doc++){
                        int idDoc = docsPerGroupTemp.get(topic).get(doc);
                        HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                        Object[] keys = neighbors.keySet().toArray();
                        for(int term=0;term<keys.length;term++){
                            int idTerm = (Integer)keys[term];
                            avgTerms[idTerm] += neighbors.get(idTerm);
                        }
                    }

                    //Tirando a média das frequências dos termos por tópico;
                    for(int term=0;term<getNumTerms();term++){
                        if(docsPerGroupTemp.get(topic).size() == 0){
                            avgTerms[term] = 0;
                        }else{
                            avgTerms[term] = (double)avgTerms[term] / (double)docsPerGroupTemp.get(topic).size();
                        }
                    }

                    //Criando o novo centróide do tópico
                    NeighborHash newCentroid =  new NeighborHash();
                    for(int term=0;term<getNumTerms();term++){
                        if(avgTerms[term] > 0){
                            newCentroid.AddNeighbor(term,avgTerms[term]);
                        }
                    }
                    centroidsTemp.set(topic, newCentroid);
                }

                //System.out.println("Centroids computed...");

                double acmDiff = 0;

                int numIt = 0;
                boolean sair2 = false;

                double[][] tempDocGroup = new double[getNumDocs()][this.getNumDivisions()];

                while(sair2 == false){
                    //System.out.println("Iteration: " + (numIt + 1));
                    //System.out.println("Calculando a distância entre os objetos e os centroides...");
                    //Zerando o DocsPerGroup a cada Iteração
                    for(int topic=0;topic<this.getNumDivisions();topic++){
                        docsPerGroupTemp.set(topic, new ArrayList<Integer>());
                    }

                    for(int doc=0;doc<docsPerGroup.get(idGroupDivision).size();doc++){
                        int idDoc = docsPerGroup.get(idGroupDivision).get(doc);

                        double maxValue = -1;
                        int idMaxValue = -1;
                        double[] tempGroup = new double[this.getNumDivisions()];
                        for(int topic=0;topic<this.getNumDivisions();topic++){
                            double prox = 0;
                            if(this.isCosine()){
                                prox = Proximity.computeCosine(adjacencyListDocTerm[idDoc],centroidsTemp.get(topic));
                            }else if(this.isPearson()){
                                prox = Proximity.computePearson(adjacencyListDocTerm[idDoc],centroidsTemp.get(topic));
                            }else{
                                prox = Proximity.computeEuclidean(adjacencyListDocTerm[idDoc],centroidsTemp.get(topic));
                            }
                            if(prox > maxValue){
                                maxValue = prox;
                                idMaxValue = topic;
                            }
                        }

                        tempGroup[idMaxValue] = 1;
                        docsPerGroupTemp.get(idMaxValue).add(idDoc);
                        for(int topic=0;topic<this.getNumDivisions();topic++){
                            if(Math.abs(tempGroup[topic] - tempDocGroup[idDoc][topic]) == 1){
                                acmDiff++;
                            }
                            tempDocGroup[idDoc][topic] = tempGroup[topic];
                        }

                    }

                    acmDiff = (acmDiff / (double)getNumDocs()) * 100;

                    //System.out.println("- % of changes: " + acmDiff);

                    //System.out.println("Recalculando os centroides...");
                     // Recalculando os centroides;
                    for(int topic=0;topic<this.getNumDivisions();topic++){
                        // Frequencia total dos termos dos documentos de um determinado tópico
                        double[] avgTerms = new double[getNumTerms()];
                        for(int doc=0;doc<docsPerGroupTemp.get(topic).size();doc++){
                            int idDoc = docsPerGroupTemp.get(topic).get(doc);
                            HashMap<Integer,Double> neighbors =  adjacencyListDocTerm[idDoc].getNeighbors();
                            Object[] keys = neighbors.keySet().toArray();
                            for(int term=0;term<keys.length;term++){
                                int idTerm = (Integer)keys[term];
                                avgTerms[idTerm] += neighbors.get(idTerm);
                            }
                        }

                        //Tirando a média das frequências dos termos por tópico;
                        for(int term=0;term<getNumTerms();term++){
                            if(docsPerGroupTemp.get(topic).size() == 0){
                                avgTerms[term] = 0;
                            }else{
                                avgTerms[term] = (double)avgTerms[term] / (double)docsPerGroupTemp.get(topic).size();
                            }
                        }

                        //Criando o novo centróide do tópico
                        NeighborHash newCentroid =  new NeighborHash();
                        for(int term=0;term<getNumTerms();term++){
                            if(avgTerms[term] > 0){
                                newCentroid.AddNeighbor(term,avgTerms[term]);
                            }
                        }
                        centroidsTemp.set(topic, newCentroid);
                    }

                    //System.out.println("Calculando a coesão...");
                    // Calculando a cohesão -- Não interfere no processo de agrupamento -- pode ser comentada
                    double cohesion = 0;
                    for(int topic=0;topic<this.getNumDivisions();topic++){
                        for(int doc=0;doc<docsPerGroupTemp.get(topic).size();doc++){
                            int idDoc = docsPerGroupTemp.get(topic).get(doc);
                            double prox = 0;
                            if(this.isCosine()){
                                prox = Proximity.computeCosine(adjacencyListDocTerm[idDoc],centroidsTemp.get(topic));
                            }else if(this.isPearson()){
                                prox = Proximity.computePearson(adjacencyListDocTerm[idDoc],centroidsTemp.get(topic));
                            }else{
                                prox = Proximity.computeEuclidean(adjacencyListDocTerm[idDoc],centroidsTemp.get(topic));
                            }
                            cohesion += prox;
                        }
                    }
                    
                    //System.out.println("- Total Cohesion: " + cohesion);
                    
                    if(cohesion > previousCohesion){
                        bestCentroids = new ArrayList<NeighborHash>();
                        bestDocsPerGroups = new ArrayList<ArrayList<Integer>>();
                        for(int c=0;c<this.getNumDivisions();c++){
                            bestCentroids.add(centroidsTemp.get(c));
                            bestDocsPerGroups.add(docsPerGroupTemp.get(c));
                        }
                        
                        previousCohesion = cohesion;
                        
                    }
                    

                    if(acmDiff <= getConvergence()){
                        sair2 = true;
                    }

                    if(numIt >= getNumMaxIterations()){
                        sair2 = true;
                    }

                    numIt++;
                    acmDiff = 0;
                }    
                
            }
            
            for(int c=0;c<this.getNumDivisions();c++){
                if(bestCentroids.size() == 0 || bestDocsPerGroups.size() == 0){
                    System.out.println("");
                }
                centroids.add(bestCentroids.get(c));
                docsPerGroup.add(bestDocsPerGroups.get(c));
                
            }
            
            if(centroids.size() > getK()){
                sair1 = true;
            }
            
            //Tem que remover o docsPerGroup antigo depois
            docsPerGroup.remove(idGroupDivision);
            centroids.remove(idGroupDivision);
        }
        
        
        
    }
    
    public int findIDGroupDivision(ArrayList<ArrayList<Integer>> docsPerGroup){
        int idGroup = -1;
        int currentNumGroups = docsPerGroup.size();
        
        
        if(this.isCohesionSplitting()){
            if(this.isCosine() || this.isPearson()){
                double minCohesion = Double.MAX_VALUE;
                for(int topic=0;topic<currentNumGroups;topic++){
                    double cohesion = 0;
                    for(int doc=0;doc<docsPerGroup.get(topic).size();doc++){
                        int idDoc = docsPerGroup.get(topic).get(doc);
                        double prox = 0;
                        if(this.isCosine()){
                            prox = Proximity.computeCosine(adjacencyListDocTerm[idDoc],centroids.get(topic));
                        }else{
                            prox = Proximity.computePearson(adjacencyListDocTerm[idDoc],centroids.get(topic));
                        }
                        cohesion += prox;
                    }
                    //SSE = SSE / (double)docsPerGroup.get(topic).size();
                    cohesion = cohesion / (double)docsPerGroup.get(topic).size();
                    if(cohesion < minCohesion){
                        minCohesion = cohesion;
                        idGroup = topic;
                    }
                }
            }else{
                double maxSSE = -1;
                for(int topic=0;topic<currentNumGroups;topic++){
                    double SSE = 0;
                    for(int doc=0;doc<docsPerGroup.get(topic).size();doc++){
                        int idDoc = docsPerGroup.get(topic).get(doc);
                        double dist = Proximity.computeEuclidean(adjacencyListDocTerm[idDoc],centroids.get(topic));
                        SSE += dist * dist;
                    }
                    SSE = SSE / (double)docsPerGroup.get(topic).size();
                    if(SSE > maxSSE){
                        maxSSE = SSE;
                        idGroup = topic;
                    }
                }    
            }
        }else{
            int maxDocsGroup = Integer.MIN_VALUE * -1;
            for(int topic=0;topic<currentNumGroups;topic++){
                int numDocsGroup = docsPerGroup.get(topic).size();
                if(numDocsGroup > maxDocsGroup){
                    maxDocsGroup = numDocsGroup;
                    idGroup = topic;
                }
            }
        }
        
        return idGroup;
    }
    
    
    @Override
    public double getScore(Instance test) {
        
        NeighborHash newInstance = new NeighborHash();
        NeighborHash testInstance = new NeighborHash();
        
        double totalWeight = 0;
        
        for(int term=0;term<getNumTerms();term++){
            totalWeight += test.value(term);
        }
        
        for(int term=0;term<getNumTerms();term++){
            double value = test.value(term);
            if((value > 0)){
                newInstance.AddNeighbor(term,(value / totalWeight)); 
            }    
        }
        
        //Parei aqui
        double maxValue = -1;
        for(int topic=0;topic<this.getK();topic++){
            double prox = 0;
            if(this.isCosine()){
                prox = Proximity.computeCosine(newInstance,centroids.get(topic));
            }else if(this.isPearson()){
                prox = Proximity.computePearson(newInstance,centroids.get(topic));
            }else{
                prox = Proximity.computeEuclidean(newInstance,centroids.get(topic));
            }
            
            
            //double prox = Proximity.ComputePearson(newInstance,bestCentroids[topic]);
            if(prox > maxValue){
                maxValue = prox;
            }
        }
        
        return maxValue;
        
    }

    public int getNumDivisions() {
        return numDivisions;
    }

    public void setNumDivisions(int numDivisions) {
        this.numDivisions = numDivisions;
    }

    
    
    public ArrayList<NeighborHash> getCentroids() {
        return centroids;
    }

    public NeighborHash[] getAdjacencyListDocTerm() {
        return adjacencyListDocTerm;
    }

    public NeighborHash[] getAdjacencyListTermDoc() {
        return adjacencyListTermDoc;
    }

    public double[][] getDocGroup() {
        return docGroup;
    }

    public int getNumDocs() {
        return numDocs;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setCentroids(ArrayList<NeighborHash> centroids) {
        this.centroids = centroids;
    }

    public void setAdjacencyListDocTerm(NeighborHash[] adjacencyListDocTerm) {
        this.adjacencyListDocTerm = adjacencyListDocTerm;
    }

    public void setAdjacencyListTermDoc(NeighborHash[] adjacencyListTermDoc) {
        this.adjacencyListTermDoc = adjacencyListTermDoc;
    }

    public void setDocGroup(double[][] docGroup) {
        this.docGroup = docGroup;
    }

    public void setNumDocs(int numDocs) {
        this.numDocs = numDocs;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }

    public boolean isCohesionSplitting() {
        return cohesionSplitting;
    }

    public void setCohesionSplitting(boolean cohesionCriteria) {
        this.cohesionSplitting = cohesionCriteria;
    }
    
    
    
}
