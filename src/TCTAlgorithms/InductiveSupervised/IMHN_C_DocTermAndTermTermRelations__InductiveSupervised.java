//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: April 1, 2015
// Description: This is an implementation of IMHN (Inductive Model based on 
//              Heterogeneous Network) which induces a classification
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
//*****************************************************************************

package TCTAlgorithms.InductiveSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.io.FileWriter;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class IMHN_C_DocTermAndTermTermRelations__InductiveSupervised extends InductiveSupervisedClassifier {
    
    private double[][] fDocs; // Class information of documents
    private double[][] fTerms; // Class information of terms
    private double[][] yDoc; // Real class information (labels) of labeled documents
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private double errorCorrectionRate; // Error correction rate
    private double minError; // Minimum mean squared error
    private int maxNumberGlobalIterations; // Maximum number of global iterations 
    private int maxNumberLocalIterations; // Maximum number of local iterations
    
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    // Constructor
    public IMHN_C_DocTermAndTermTermRelations__InductiveSupervised(){
        super();
        setmaxNumberGlobalIterations(10);
        setmaxNumberLocalIterations(100);
        setMinError(0.01);
        setErrorCorrectionRate(0.1);
    }
    
    // Constructor
    public IMHN_C_DocTermAndTermTermRelations__InductiveSupervised(int maxNumberLocalIterations, int maxNumberGlobalIterations, double minError, double errorCorrectionRate, String weight){
        setmaxNumberLocalIterations(maxNumberLocalIterations);
        setmaxNumberGlobalIterations(maxNumberGlobalIterations);
        setMinError(minError);
        setErrorCorrectionRate(errorCorrectionRate);
    }
    
    
    @Override
    //Function to perform inductive supervised learning
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        yDoc = getRealClass(dataTrain);
        fTerms = new double[numTerms][numClasses];
        fDocs = new double [numTrain][numClasses];
        
        //Adjacency lists to speed up learning
        Neighbor[] adjancecyListDocTerm = new Neighbor[numTrain];
        Neighbor[] adjacencyListPDocTerm = new Neighbor[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            adjancecyListDocTerm[inst] = new Neighbor();
            adjacencyListPDocTerm[inst] = new Neighbor();
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
        
        // Computing the degree of the documents
        double[] dDoc = new double[numTrain];
        for(int inst=0;inst<(numTrain);inst++){
            double degree = 0;
            ArrayList<IndexValue> neighbors = adjancecyListDocTerm[inst].getNeighbors();
            for(int term=0;term<neighbors.size();term++){
                degree += neighbors.get(term).value;
            }
            dDoc[inst] = degree;
        }
        
        // Normalizing document-term relations. In this way, the weights of the document-term and term-term relations are in the same range of values
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
        
        
        int numIterationsTotal = 0;
        int numIterationsGlobal = 0;
        boolean exit = false;
        
        // Learning Algorithm
        while(exit == false){
            //Optimizing class information of terms considering labeled documents
            boolean exit1 = false;
            int numIterationsIntern = 0;
            double averageErrorDoc = 0;
            while(exit1 == false){
                averageErrorDoc = 0;
                for(int inst=0;inst<numTrain;inst++){
                    ArrayList<IndexValue> neighbors = adjacencyListPDocTerm[inst].getNeighbors();
                    double[] estimatedClasses = classifyInstance2(neighbors);
                    estimatedClasses = getHighest(estimatedClasses);
                    for(int classe=0;classe<numClasses;classe++){
                        double error = (yDoc[inst][classe] - estimatedClasses[classe]);
                        averageErrorDoc += (error * error)/(double)2;
                        for(int termo=0;termo<neighbors.size();termo++){
                            double currentWeight = fTerms[neighbors.get(termo).index][classe];
                            double newWeight = currentWeight + (errorCorrectionRate * neighbors.get(termo).value * error);
                            fTerms[neighbors.get(termo).index][classe] = newWeight;
                        }
                    }
                }
                numIterationsIntern++;
                numIterationsTotal++;
                averageErrorDoc = (double)averageErrorDoc/(double)numTrain;

                if((averageErrorDoc < getMinError()) || numIterationsIntern > getMaxNumberLocalIterations()){
                    exit1 = true;
                } 
            }
            
            numIterationsIntern = 0;
            double averageErrorTerm = 0;
            //Minimizing the difference among the class information of terms   
            boolean exit2 = false;
            while(exit2 == false){
                averageErrorTerm = 0;
                for(int termo1=0;termo1<numTerms;termo1++){
                    ArrayList<IndexValue> neighbors = adjacencyListTerms[termo1].getNeighbors();
                    for(int classe=0;classe<numClasses;classe++){
                        for(int termo2=0;termo2<neighbors.size();termo2++){
                            try{
                                double error = (fTerms[neighbors.get(termo2).index][classe] - fTerms[termo1][classe]);
                                averageErrorTerm += (error * error)/(double)2;
                                fTerms[termo1][classe] = fTerms[termo1][classe] + (errorCorrectionRate *  neighbors.get(termo2).value * neighbors.get(termo2).value * error);
                            }catch(Exception e){ }
                        }
                    }
                }
                numIterationsIntern++;
                numIterationsTotal++;
                averageErrorTerm = (double)averageErrorTerm/(double)numTerms;

                if((averageErrorTerm < getMinError()) || numIterationsIntern > getMaxNumberLocalIterations()){
                    exit2 = true;
                }
            }
            
            numIterationsGlobal++;
            double averageErrorTotal = averageErrorDoc + averageErrorTerm;
            if((averageErrorTotal < getMinError()) || numIterationsGlobal > getMaxNumberGlobalIterations()){
                exit = true;
            } 
            
        }
        
        this.setNumIterations(numIterationsTotal);
        //this.printWeightTerms("depois.csv",dataTrain.attribute(dataTrain.numAttributes()-1),dataTrain,fTerms);
    }
    
    @Override
    // Function to classify an instance through the propagation of class information of terms (hard classification)
    public double classifyInstance(Instance instance){
        
        double[] classes = new double[numClasses];
        for(int j=0;j<numClasses;j++){
            double acmClassenumTest = 0;
            for(int k=0;k<numTerms; k++){
                if(instance.value(k) >0){
                    acmClassenumTest += (instance.value(k) * fTerms[k][j]);
                }
            }
            classes[j] = acmClassenumTest;
        }

        // Labeling a document according to the high class information value
        double highest = -3000000000.0;
        int index = -1;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] > highest){
                highest = classes[classe];
                index = classe;
            }
        }
        return index;
    }
    
    // Function to return class information values of a document
    public double[] classifyInstanceReal(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        for(int classe=0;classe<numClasses;classe++){
            double acm = 0; 
            for(int term=0;term<neighbors.size();term++){
                    acm += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acm;
        }

        return classes;
    }
    
    // Function to return class confidences of the classification of an instance (soft classification)
    public double[] classifyInstance2(ArrayList<IndexValue> neighbors){
        
        double[] classes = new double[numClasses];
        double total = 0;
        for(int term=0;term<neighbors.size();term++){
            total += neighbors.get(term).value;
        }
        if(total == 0){
            return classes;
        }
        
        for(int classe=0;classe<numClasses;classe++){
            double acm = 0; 
            for(int term=0;term<neighbors.size();term++){ 
                    acm += neighbors.get(term).value * fTerms[neighbors.get(term).index][classe];
            }
            classes[classe] = acm;
        }
        
        double min = Double.MAX_VALUE;
        for(int classe=0;classe<numClasses;classe++){
            if(classes[classe] < min){
                min = classes[classe];
            }
        }
        if(min < 0){
            for(int classe=0;classe<numClasses;classe++){
                double value = classes[classe];
                classes[classe] = value + Math.abs(min);
            }
        }
        total = 0;
        for(int classe=0;classe<numClasses;classe++){
            total += classes[classe];
        }
        for(int classe=0;classe<numClasses;classe++){
            if(total == 0){
                classes[classe] = 0;
            }else{
                double value = classes[classe];
                classes[classe] = value / total;
            }
            
        }
        return classes;
    }
    
    // Function to return the value 1 in the position of the highest value of a vector and 0 for other positions
    public double[] getHighest(double[] weights){
        double highest = -300000;
        double index = -1;
        for(int ind=0;ind<weights.length;ind++){
            if(weights[ind] > highest){
                highest = weights[ind];
                index = ind;
            }
        }
        for(int ind=0;ind<weights.length;ind++){
            if(ind == index){
                weights[ind] = 1;
            }else{
                weights[ind] = 0;
            }
        }
        return weights;
    }
    
    @Override
    // Function to return class confidences of the classification of an instance (soft classification)
    public double[] distributionForInstance(Instance instance){
        double[] distribution = new double[numClasses];
        for(int j=0;j<numClasses;j++){
            double acm = 0; 
            for(int k=0;k<numTerms; k++){ 
                if(instance.value(k) >0){
                    acm += (instance.value(k) * fTerms[k][j]);
                }
            }
            distribution[j] = acm;
        }
        return distribution;
    }
    
    // Returns de Y matrix
    private static double[][] getRealClass(Instances dataTrain){
        double[][] yDoc = new double[dataTrain.numInstances()][dataTrain.numClasses()];
        for(int i=0;i<dataTrain.numInstances();i++){
            Instance inst = dataTrain.instance(i);
            int pos = (int)inst.classValue();
            yDoc[i][pos] = 1;
        }
        return yDoc;
    }
    
    
    //Sets e Gets
    public void setMinError(double minError){
        this.minError = minError;
    }
    
    public void setErrorCorrectionRate(double errorCorrectionRate){
        this.errorCorrectionRate = errorCorrectionRate;
    }
    
    public double getMinError(){
        return this.minError;
    }
    
    public double getErrorCorrectionRate(){
        return this.errorCorrectionRate;
    }
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
    }
    
    public Integer getMaxNumberGlobalIterations(){
        return maxNumberGlobalIterations;
    }
    
    public Integer getMaxNumberLocalIterations(){
        return maxNumberLocalIterations;
    }
    
    public void setmaxNumberGlobalIterations(int maxNumberGlobalIterations){
        this.maxNumberGlobalIterations = maxNumberGlobalIterations;
    }
    
    public void setmaxNumberLocalIterations(int maxNumberLocalIterations){
        this.maxNumberLocalIterations = maxNumberLocalIterations;
    }
    
    public void printWeightTerms(String fileName, Attribute classAtt, Instances data, double[][] f){
        try{
            FileWriter outputFile = new FileWriter(fileName);
            outputFile.write("Term;");
            
            numClasses = classAtt.numValues();
            for(int classe=0;classe<numClasses;classe++){
                outputFile.write(classAtt.value(classe) + ";");
            }
            outputFile.write("\n");
            
            for(int term=0;term<numTerms;term++){
                outputFile.write(data.attribute(term).name() + ";");
                for(int classe=0;classe<numClasses;classe++){
                    outputFile.write(f[term][classe] + ";");
                }
                outputFile.write("\n");
            }
            outputFile.close();
        }catch(Exception e){
            System.err.println("Error when saving class information of terms for classes.");
            e.printStackTrace();
            System.exit(0);
        }
    }
}

