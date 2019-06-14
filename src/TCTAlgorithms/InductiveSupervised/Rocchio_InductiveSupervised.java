//*******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This in an implementation of the Rocchio algorithm (Rocchi, 1971)
//              considering the formulation presented in (Aggarwal & Zhai, 2012).   
// References: - J. J. Rocchio. Relevance feedback in information retrieval. In 
//               G. Salton, editor, The Smart retrieval system - experiments in 
//               automatic document processing, pages 313–323. Englewood Cliffs, 
//               NJ: Prentice-Hall, 1971.
//             - C. C. Aggarwal and C. Zhai, editors. Mining Text Data. Springer, 
//               2012.
//*******************************************************************************

package TCTAlgorithms.InductiveSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class Rocchio_InductiveSupervised extends InductiveSupervisedClassifier{
    
    private double beta; // weight given to the average vector of documents of a class X when computing the prototype of this class. Beta = [0, 1] 
    private double gamma; // weight given to the average vector of documents of all classes except class X when computing the prototype of the class X. Gamma is equal to (1 - beta). 
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private double[][] prototypes; // Prototypes for each class of a text collection. Each line correspond to a class and each column corresponds to the weight of a term for a class.
    
    // Constructor
    public Rocchio_InductiveSupervised(){
        super();
        setBeta(1);
        setGamma(0);
    }
    
    @Override
    //Function to perform inductive supervised learning
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
        }
        for(int inst=0;inst<numTrain;inst++){
            for(int term=0;term<numTerms;term++){
                if(dataTrain.instance(inst).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataTrain.instance(inst).value(term);
                    adjancecyListDocTerm[inst].AddNeighbor(indVal);
                }    
            }
        }
        
        //Creating the prototypes for each class
        prototypes = new double[numClasses][numTerms];
        double[][] prototypesTemp = new double[numClasses][numTerms];
        int[] contClasses = new int[numClasses];
        
        for(int inst=0;inst<numTrain;inst++){
            int classeInst = (int)dataTrain.instance(inst).classValue();
            contClasses[classeInst] = contClasses[classeInst] + 1;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                prototypesTemp[classeInst][neighbors.get(term).index] = prototypesTemp[classeInst][neighbors.get(term).index] + neighbors.get(term).value;
            }
        }
        
        for(int class1=0;class1<numClasses;class1++){
            for(int term=0;term<numTerms;term++){
                double numTermo1 = prototypesTemp[class1][term];
                double denTermo1 = contClasses[class1];
                double numTermo2 = 0;
                double denTermo2 = 0;
                for(int class2=0;class2<numClasses;class2++){
                    if(class1 == class2){
                        continue;
                    }
                    numTermo2 += prototypesTemp[class2][term];
                    denTermo2 += contClasses[class2];
                }
                prototypes[class1][term] = (beta * (numTermo1/denTermo1)) - (gamma * (numTermo2/denTermo2));
            }
        }
    }
    
    @Override
    // Function to classify a new instance (hard classification)
    public double classifyInstance(Instance instance){
        double[] scores = new double[numClasses];
        double numerator = 0;
        double den1 = 0;
        double den2 = 0;
        
        int numTermsDoc = 0;
        for(int term=0;term<numTerms;term++){
            if(instance.value(term)>0){
                numTermsDoc++;
            }
        }
        
        for(int classe=0;classe<numClasses;classe++){
            for(int term=0;term<numTerms;term++){
                double freqAtr = instance.value(term);
                numerator += freqAtr * prototypes[classe][term];
                den1 += freqAtr * freqAtr;
                den2 += prototypes[classe][term] * prototypes[classe][term];
            }
            if((den1 == 0) || (den2 == 0)){
                scores[classe] = 0;
            }else{
                scores[classe] = numerator / (Math.sqrt(den1) * Math.sqrt(den2));
            }
            
        }
        
        double maior = -300000;
        double index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(scores[classe] > maior){
                maior = scores[classe];
                index = classe;
            }
        }
        
        /*if(index == -1){
            System.out.println("Aqui!!!!");
        }*/
        
        return index;
    }
    
    @Override
    // Function to return class confidences of the classification of a new instance (soft classification)
    public double[] distributionForInstance(Instance instance){
        double[] scores = new double[numClasses];
        double numerator = 0;
        double den1 = 0;
        double den2 = 0;
        
        int numTermsDoc = 0;
        for(int term=0;term<numTerms;term++){
            if(instance.value(term)>0){
                numTermsDoc++;
            }
        }
        
        for(int classe=0;classe<numClasses;classe++){
            for(int term=0;term<numTerms;term++){
                double freqAtr = instance.value(term);
                numerator += freqAtr * prototypes[classe][term];
                den1 += freqAtr * freqAtr;
                den2 += prototypes[classe][term] * prototypes[classe][term];
            }
            scores[classe] = numerator / (Math.sqrt(den1) * Math.sqrt(den2));
        }
        
        double min = Double.MAX_VALUE;
        for(int classe=0;classe<numClasses;classe++){
            if(scores[classe] < min){
                min = scores[classe];
            }
        }
        
        if(min < 0){
            min = min * (-1);
            for(int classe=0;classe<numClasses;classe++){
                scores[classe] = scores[classe] + min;
            }
        }
        
        double sum=0;
        for(int classe=0;classe<numClasses;classe++){
            sum += scores[classe];
        }
        for(int classe=0;classe<numClasses;classe++){
            scores[classe] = scores[classe]/sum;
        }
        
        return scores;
    }
    
    public void setBeta(double beta){
        this.beta = beta;
        setGamma(1 - beta);
    }
    
    public void setGamma(double gamma){
        this.gamma = gamma;
    }
    
    public Double getBeta(){
        return beta;
    }
    
    public Double getGamma(){
        return gamma;
    }
}
