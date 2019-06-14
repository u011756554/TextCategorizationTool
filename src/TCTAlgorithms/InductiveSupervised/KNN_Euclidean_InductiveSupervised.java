//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: March 12, 2015
// Description: This is an implementation of IMBHN (Inductive Model based on 
//              Bipartite Heterogeneous Network) algorithm (Rossi et al., 2014;
//              Rossi et al., 2012).
// References: - R. G. Rossi, A. A., T. de Paulo Faleiros, S. O. 
//               Rezende, Inductive model generation for text classiﬁcation 
//               using a bipartite heterogeneous network, Journal of Computer 
//               Science and Technology 3 (29) (2014) 361–375.
//             - R. G. Rossi, de Thiago de Paulo Faleiros, A. de Andrade Lopes, 
//               S. O. Rezende, Inductive model generation for text 
//               categorization using a bipartite heterogeneous network, in: 
//               Proceedings of the International Conference on Data Mining, 
//               IEEE, 2012, pp. 1086–1091.
//******************************************************************************

package TCTAlgorithms.InductiveSupervised;

import static TCTNetworkGeneration.Proximity.computeCosine;
import TCTStructures.IndexValue;
import TCTStructures.NeighborHash;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;


public class KNN_Euclidean_InductiveSupervised extends InductiveSupervisedClassifier {
    
    private int k;
    private boolean weightedVote;
    
    private double[][] yDocs; // Class information of terms
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    
    NeighborHash[] adjancecyListDocTerm; //Hash structure to store da datase and speed up cosine similarity computation
    
    Instances trainData;
    
    // Constructor
    public KNN_Euclidean_InductiveSupervised(){
        this.setK(7);
        this.setWeightedVote(true);
    }
    
    // Constructor
    public KNN_Euclidean_InductiveSupervised(int k, boolean weightedVote){
        this.setK(k);
        this.setWeightedVote(weightedVote);
    }
    
    
    @Override
    //Function to perform inductive supervised learning
    public void buildClassifier(Instances dataTrain){
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        yDocs = new double[numTrain][numClasses];
                
        trainData = dataTrain;

        for(int inst=0;inst<numTrain;inst++){
            int indClass = (int)dataTrain.instance(inst).classValue();
            yDocs[inst][indClass] = 1;
        }
    }
    
    
    @Override
    // Function to classify an instance through the propagation of class information of terms (hard classification)
    public double classifyInstance(Instance instance){
        double[] confidences = distributionForInstance(instance);
        
        confidences = distributionForInstance(instance);
        
        double maxClass = -1;
        int idMaxClass = -1;
        
        for(int classe=0;classe<numClasses;classe++){
            if(confidences[classe] > maxClass){
                maxClass = confidences[classe];
                idMaxClass = classe;
            }
        }
        
        return idMaxClass;
    }
    
    
    @Override
    // Function to return class confidences of the classification of an instance (soft classification)
    public double[] distributionForInstance(Instance test){
        double[] confidences = new double[numClasses];
        
        //Computing the similarity among the new instance and the whole dataset
        //long beginSim = System.currentTimeMillis();
        IndexValue[] dists = new IndexValue[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            double euclidean = computeEuclidean(trainData.instance(inst), test);
            dists[inst] = new IndexValue(inst,euclidean);
        }
        //long endSim = System.currentTimeMillis();
        //System.out.println("Cálculo das Similaridades: " + ((endSim - beginSim)/(double)1000));
        
        //long beginOrd = System.currentTimeMillis();
        Arrays.sort(dists, new Comparator() {
        public int compare( Object obj1, Object obj2 ) {
                IndexValue indVal1 = (IndexValue)obj1;
                IndexValue indVal2 = (IndexValue)obj2;
                if(indVal1.value < indVal2.value){
                    return -1;
                }else{
                    if(indVal1.value > indVal2.value){
                        return 1;
                    }else{
                        return 0;
                    }         
                }
        }});
        //long endOrd = System.currentTimeMillis();
        //System.out.println("Tempo Ordenação:" + ((endOrd - beginOrd)/(double)1000));
        
        if(getK() > numTrain){
            setK(numTrain);
        }
        if(getWeightedVote()){
            for(int classe=0;classe<numClasses;classe++){
                double score = 0;
                for(int indK=0;indK<getK();indK++){
                    score += (1/dists[indK].value) * yDocs[dists[indK].index][classe];
                }
                confidences[classe] = score;
            }    
        }else{
            for(int classe=0;classe<numClasses;classe++){
                double score = 0;
                for(int indK=0;indK<getK();indK++){
                    score +=  yDocs[dists[indK].index][classe];
                }
                confidences[classe] = score;
            }
        }

        //System.out.println("Fim da Classificação");
        
        return confidences;
    }
    
    double computeEuclidean(Instance train, Instance test){
        
        double euclidean = 0;
        for(int term=0;term<numTerms;term++){
            euclidean += Math.pow(train.value(term) - test.value(term), 2);
        }
        
        euclidean = Math.sqrt(euclidean);
        
        return euclidean;
        
    }
    
    IndexValue[] computeDists(NeighborHash newInstance){
        IndexValue[] dists = new IndexValue[numTrain];
        
        for(int inst=0;inst<numTrain;inst++){
            double dist=0;
            double numerator = 0;
            double den1 = 0;
            double den2 = 0;

            Set<Integer> atributos1 = adjancecyListDocTerm[inst].getNeighbors().keySet();
            for(Integer atr1 : atributos1){
                den1 += Math.pow(adjancecyListDocTerm[inst].getNeighbor(atr1),2);
            }
            if(den1 == 0){
                dists[inst] = new IndexValue(inst,0.0);
            }else{
                Set<Integer> atributos2 = newInstance.getNeighbors().keySet();
                for(Integer atr2 : atributos2){
                    den2 += Math.pow(newInstance.getNeighbor(atr2),2);
                }
                if(den2 == 0){
                    dists[inst] = new IndexValue(inst,0.0);
                }else{
                    for(Integer atr1 : atributos1){
                        if(newInstance.getNeighbors().containsKey(atr1)){
                            numerator += adjancecyListDocTerm[inst].getNeighbor(atr1) * newInstance.getNeighbor(atr1);
                        }
                    }
                    dist = (numerator / ((Math.sqrt(den1) * Math.sqrt(den2))));

                    double cosine = computeCosine(adjancecyListDocTerm[inst], newInstance);
                    dists[inst] = new IndexValue(inst,cosine);
                }
            }

            
        }
        
        return dists;
    } 
    
    
    // Function to return the Y matrix
    private double[][] getYDoc(Instances dataTrain){
        double[][] yDoc = new double[numTrain][numClasses];
        for(int i=0;i<numTrain;i++){
            Instance inst = dataTrain.instance(i);
            int pos = (int)inst.classValue();
            yDoc[i][pos] = 1;
            
        }
        return yDoc;
    }
    
    public int getK(){
        return this.k;
    }
    
    public void setK(int k){
        this.k = k;
    }
    
    public boolean getWeightedVote(){
        return this.weightedVote;
    }
    
    public void setWeightedVote(boolean weightedVote){
        this.weightedVote = weightedVote;
    }

    @Override
    public Capabilities getCapabilities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

