//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Implementation of Tag-based Model algorithm (Yin et al., 2009).
// References: - Z. Yin, R. Li, Q. Mei, J. Han, Exploring social tagging graph
//               for web object classification, in: Proceedings of the 
//               International Conference on Knowledge Discovery and Data 
//               Mining, 2009, pp. 957–966.
//*****************************************************************************

package TCTAlgorithms.InductiveSemiSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;

public class TagBased_DocTermRelation_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
    private double[][] fDocs; // Class information of documents 
    private double[][] fDocsTemp; // Class information of documents
    private double[][] fTerms; // Class information of terms 
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numLabeledDocs; // Number of labeled target objects
    private int numUnlabeledDocs; // Number of unlabeled target objects 
    private int numClasses; // Number of classes // Number of classes in the dataset
    private int numTerms; // Number of terms // Number of bridge objects
    private int maxNumIterations; // Maximum number of iterations
    private double alpha, beta, gamma; //Parameters of the Tag-based Model Algorithm
    
    public TagBased_DocTermRelation_InductiveSemiSupervised(){
        setAlpha(0);
        setBeta(1.0);
        setGamma(1.0);
        setMaxNumIterations(1000);
    }
    
    public void buildClassifier(Instances dataLabeled, Instances dataUnlabeled){
        this.numLabeledDocs = dataLabeled.numInstances();
        this.numUnlabeledDocs = dataUnlabeled.numInstances();
        this.numClasses = dataLabeled.numClasses();
        this.numTerms = dataLabeled.numAttributes() - 1;
        
        fDocs = getClassInformation(dataLabeled,dataUnlabeled); //Initial values for fTarget (1 for the position corresponding to the class and 0 otherwise)
        yDoc = getClassInformation(dataLabeled,dataUnlabeled); //Initial values for yTarget (1 for the position corresponding to the class and 0 otherwise)
        fDocsTemp = getClassInformation(dataLabeled,dataUnlabeled); //Initial values for fTarget temp
        fTerms = new double[numTerms][numClasses]; //Initial values for fBridge (All values are 0)
        
        // Creating adjacency lists to speed up the transductive classification
        Neighbor[] adjancecyListDocTerm = new Neighbor[numLabeledDocs + numUnlabeledDocs];
        Neighbor[] adjacencyListTermDoc = new Neighbor[numTerms];
        
        //Initializing the adjacency lists
        for(int doc=0;doc<(numLabeledDocs + numUnlabeledDocs);doc++){
            adjancecyListDocTerm[doc] = new Neighbor();
        }
        for(int term=0;term<numTerms;term++){
            adjacencyListTermDoc[term] = new Neighbor();
        }
        
        //Populating the adjacency lists
        for(int doc=0;doc<numLabeledDocs;doc++){
            for(int term=0;term<numTerms;term++){
                if(dataLabeled.instance(doc).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataLabeled.instance(doc).value(term);
                    adjancecyListDocTerm[doc].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = doc;
                    indVal.value = dataLabeled.instance(doc).value(term);
                    adjacencyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        for(int doc=0;doc<numUnlabeledDocs;doc++){
            for(int term=0;term<numTerms;term++){
                if(dataUnlabeled.instance(doc).value(term) > 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = term;
                    indVal.value = dataUnlabeled.instance(doc).value(term);
                    adjancecyListDocTerm[doc + numLabeledDocs].AddNeighbor(indVal);
                    indVal = new IndexValue();
                    indVal.index = numLabeledDocs + doc;
                    indVal.value = dataUnlabeled.instance(doc).value(term);
                    adjacencyListTermDoc[term].AddNeighbor(indVal);
                }    
            }
        }
        
        //Computing the degree of the target objects
        double[] dDoc = new double[numLabeledDocs + numUnlabeledDocs];
        for(int doc=0;doc<(numLabeledDocs + numUnlabeledDocs);doc++){
            double degree = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[doc].getNeighbors();
            for(int bridge=0;bridge<neighbors.size();bridge++){
                degree += neighbors.get(bridge).value;
            }
            dDoc[doc] = degree;
        }
        //Computing the degree of the bridge objects
        double[] dTerm = new double[numTerms];
        for(int term=0;term<numTerms;term++){
            double degree = 0;
            ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
            for(int inst=0;inst<neighbors.size();inst++){
                degree += neighbors.get(inst).value;
            }
            dTerm[term] = degree;
        }
        
        int numIterations = 0;
        boolean exit = false;
        
        //Iterative Transductive Classification
        while(exit == false){
            
            //Propagating class information from bridge to target objects
            for(int doc=0;doc<numLabeledDocs;doc++){
                for(int classe=0;classe<numClasses;classe++){
                    fDocsTemp[doc][classe] = ((double)beta/(double)(beta + dDoc[doc])) * yDoc[doc][classe];
                    double acmnumTrain = 0;
                    ArrayList<IndexValue> neighbors = adjancecyListDocTerm[doc].getNeighbors();
                    for(int term=0;term<neighbors.size();term++){
                        acmnumTrain += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
                    }
                    fDocsTemp[doc][classe] += (double)acmnumTrain/(double)(beta + dDoc[doc]);
                }
            }
            for(int doc=0;doc<numUnlabeledDocs;doc++){
                for(int classe=0;classe<numClasses;classe++){
                    fDocsTemp[doc + numLabeledDocs][classe] = ((double)gamma/(double)(gamma + dDoc[doc + numLabeledDocs])) * yDoc[doc + numLabeledDocs][classe];
                    double acmnumTest = 0;
                    ArrayList<IndexValue> neighbors = adjancecyListDocTerm[doc + numLabeledDocs].getNeighbors();
                    for(int term=0;term<neighbors.size();term++){
                        acmnumTest += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
                    }
                    fDocsTemp[doc + numLabeledDocs][classe] += (double)acmnumTest/(double)(gamma + dDoc[doc + numLabeledDocs]);
                }
            }
            //Propagating class information from target to bridge objects
            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    ArrayList<IndexValue> neighbors = adjacencyListTermDoc[term].getNeighbors();
                    for(int inst=0;inst<neighbors.size();inst++){
                        value += neighbors.get(inst).value * fDocsTemp[neighbors.get(inst).index][classe];
                    }
                    if(dTerm[term] > 0){
                        fTerms[term][classe] = value / (double)(dTerm[term]);
                    }
                }
            }
            
            //Vector standardization. Sum of each object's weights is equal 1.
            for(int doc=0;doc<(numLabeledDocs + numUnlabeledDocs);doc++){
                double total = 0;
                for(int classe=0;classe<numClasses;classe++){
                    total += fDocsTemp[doc][classe];
                }
                if(total != 0){
                    for(int classe=0;classe<numClasses;classe++){
                        fDocsTemp[doc][classe] = (double)fDocsTemp[doc][classe]/(double)total;
                    }    
                }
                
            }
            for(int term=0;term<numTerms;term++){
                double total = 0;
                for(int classe=0;classe<numClasses;classe++){
                    total += fTerms[term][classe];
                }
                if(total != 0){
                    for(int classe=0;classe<numClasses;classe++){
                        fTerms[term][classe] = (double)fTerms[term][classe]/(double)total;
                    }      
                }
            }
            if(numIterations <=1){
                numIterations++;
                continue;
            }
            //Stopping the iterative process if the diffenece between consecutive iterations is 0 or the maximum number of iterations is reached
            double acmDif = 0;
            for(int doc=0;doc<(numLabeledDocs + numUnlabeledDocs);doc++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fDocsTemp[doc][classe] - fDocs[doc][classe]);
                    fDocs[doc][classe] = fDocsTemp[doc][classe];
                }
            }
            
            if((acmDif == 0)||(getMaxNumIterations() == numIterations)){
                exit = true;
            }
            numIterations++;
        }
        
        
        
        
    }
    
    public double classifyInstance(Instance instance){
        double valueClasse = -1;
        double[] distClasses = distributionForInstance(instance);
        double maior = -3000000;
        for(int classe=0;classe<numClasses;classe++){
            if(distClasses[classe] > maior){
                 maior = distClasses[classe];
                 valueClasse = classe;
            }
        }
        return valueClasse;
    }
    
    public double[] distributionForInstance(Instance instance){
        double[] distClasses = new double[numClasses];
        for(int classe=0;classe<numClasses;classe++){
            double score = 0;
            for(int term=0;term<numTerms;term++){
               score += instance.value(term) * fTerms[term][classe];
            } 
            //distClasses[classe] = score / sumClassInformationDocs.get(classe);
            distClasses[classe] = score;
        }
        
        double sum = 0;
        for(int classe=0;classe<numClasses;classe++){
            sum += distClasses[classe];
        }
        if(sum != 0){
            for(int classe=0;classe<numClasses;classe++){
                distClasses[classe] = distClasses[classe] / sum;
            }    
        }
        
        return distClasses;
    }
    
    private double[][] getClassInformationTrain(Instances dataTrain){
        double[][] fOrigTrain = new double[numUnlabeledDocs][numClasses];
        for(int i=0;i<numUnlabeledDocs;i++){
            Instance inst = dataTrain.instance(i);
            int pos = (int)inst.classValue();
            fOrigTrain[i][pos] = 1;
            
        }
        return fOrigTrain;
    }
    
    private double[][] getfUnlabeledDocs(){
        double[][] fOrigTest = new double[numUnlabeledDocs][numClasses];
        //float prob = (float)1/(float)numClasses;
        float prob = 0;
        for(int inst=0;inst<numUnlabeledDocs;inst++){
            for(int classe=0;classe<numClasses;classe++){
                fOrigTest[inst][classe] = prob;
            }
        }
        return fOrigTest;
    }
    
    private double[][] getDistEstTrain(){
        double[][] fEstTrain = new double[numLabeledDocs][numClasses];
        float prob = (float)1/(float)numClasses;
        for(int inst=0;inst<numLabeledDocs;inst++){
            for(int classe=0;classe<numClasses;classe++){
                fEstTrain[inst][classe] = prob;
            }
        }
        return fEstTrain;
    }
    
    private double[][] getDistEstAtr(){
        double[][] fEstAtr = new double[numLabeledDocs][numClasses];
        float prob = (float)1/(float)numClasses;
        for(int term=0;term<numLabeledDocs;term++){
            for(int classe=0;classe<numClasses;classe++){
                fEstAtr[term][classe] = prob;
            }
        }
        return fEstAtr;
    }
    
    //Sets e Gets
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setBeta(double beta){
        this.beta = beta;
    }
    
    public void setGamma(double gamma){
        this.gamma = gamma;
    }
    
    public void setMaxNumIterations(int maxNumIterations){
        this.maxNumIterations = maxNumIterations;
    }
    
    public int getMaxNumIterations(){
        return this.maxNumIterations;
    }
    
    public double getAlpha(){
        return this.alpha;
    }
    
    public double getBeta(){
        return this.beta;
    }
    
    public double getGamma(){
        return this.gamma;
    }
    
}
