//******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of IMBHN (Inductive Model based on 
//              Bipartite Heterogeneous Network) which induces a classification
//              model considering document-term and term-term relations. We 
//              used the class(...) funcion as in (Rossi et al., 2014;
//              Rossi et al., 2012) to induce the relevance score of terms.
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

package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instances;

public class IMBHNR_SupervisedOneClass extends IMBHN_SupervisedOneClass {
    
    
    private int numTrain; // Number of labeled documents
    private int maxNumberIterations; // Maximum number of iterations
    private double errorCorrectionRate; // Error correction error
    private double minError; // Minimum Mean Squared Error
    
    // Constructor
    public IMBHNR_SupervisedOneClass(){
        super();
        setMaxNumIterations(1000);
        setMinError(0.01);
        setErrorCorrectionRate(0.1);
    }
    
    // Constructor
    public IMBHNR_SupervisedOneClass(int maxNumberIterations, double minError, double errorCorrectionRate, String weight){
        setMaxNumIterations(maxNumberIterations);
        setMinError(minError);
        setErrorCorrectionRate(errorCorrectionRate);
    }
    
    
    @Override
    //Function to perform inductive supervised learning
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.setNumTerms(dataTrain.numAttributes() - 1);

        this.setFTerms(new double[this.getNumTerms()]);
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            adjacencyListPDocTerm[inst] = new Neighbor();
        }
        
        for(int inst=0;inst<numTrain;inst++){
            for(int term=0;term<this.getNumTerms();term++){
                if(dataTrain.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTrain.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
                }    
            }
        }
        
        //Normalizing doc-term relations
        double[] dDoc = new double[numTrain];
        for(int inst=0;inst<(numTrain);inst++){
            double grau = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                grau += neighbors.get(term).value;
            }
            dDoc[inst] = grau;
        }
        for(int inst=0;inst<numTrain;inst++){
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                if(dDoc[inst] != 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(term).index;
                    indVal.value = (double)neighbors.get(term).value / (double)dDoc[inst];
                    adjacencyListPDocTerm[inst].AddNeighbor(indVal);
                }
            }
        }
        double[] fTerms = this.getFTerms();
        // Learning Algorithm
        int numIt = 0;
        boolean exit = false;
        while(exit == false){
            double meanError = 0;
            //Optimizing class information of terms considering labeled documents
            for(int inst=0;inst<numTrain;inst++){
                ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                double estimatedClasses = classifyInstance2(neighbors);
                double error = 1 - estimatedClasses;
                meanError += (error * error)/(double)2;
                 for(int term=0;term<neighbors.size();term++){
                        double currentWeight = fTerms[neighbors.get(term).index];
                        double newWeight = currentWeight + (errorCorrectionRate * adjacencyListPDocTerm[inst].getNeighbor(term).value * error);
                        fTerms[neighbors.get(term).index] = newWeight;
                }
                
            }
            numIt++;
            meanError = (double)meanError/(double)numTrain;
            
            // Analysis of stopping criteria
            if((meanError < getMinError()) || numIt > getMaxNumberIterations()){
                exit = true;
            }
        }
        
        //this.setNumIterations(numIt);
        //System.out.println("NumIt: " + numIt);
        //this.printWeightTerms("depois.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
        //System.exit(0);
        
    }

        
    
    
    
    // Function to return class confidences of the classification of an instance (soft classification)
    public double classifyInstance2(ArrayList<IndexValue> neighbors){
        
        
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return 0;
        }
        
        double[] fTerms = this.getFTerms();
        
        double score = 0;
        for(int term=0;term<neighbors.size();term++){ 
            score += neighbors.get(term).value * fTerms[neighbors.get(term).index];
        }
        
        return score;

    }
    
    public void setMaxNumIterations(int maxNumberIterations){
        this.maxNumberIterations = maxNumberIterations;
    }
    
    public void setMinError(double minError){
        this.minError = minError;
    }
    
    public void setErrorCorrectionRate(double errorCorrectionRate){
        this.errorCorrectionRate = errorCorrectionRate;
    }
    
    public int getMaxNumberIterations(){
        return this.maxNumberIterations;
    }
    
    public double getMinError(){
        return this.minError;
    }
    
    public double getErrorCorrectionRate(){
        return this.errorCorrectionRate;
    }
    
    
}

