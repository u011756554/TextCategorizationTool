//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This class contains the variables and functions shared 
//              by different implementations of GFHF (Gaussian Field and 
//              Harmonic Function) algorithm (Zhu et al., 2003). The graph is 
//              represented by adjacency lists to speed up classification.
// References: - X. Zhu, Z. Ghahramani, J. Lafferty, Semi-supervised learning 
//               using gaussian fields and harmonic functions, in: Proceedings 
//               of the International Conference on Machine Learning, AAAI 
//               Press, 2003, pp. 912–919.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instances;

public class GFHF_DocDoc_ID_AdjList_Transductive extends GFHF_DocDoc_ID{
    
    //Constructor
    public GFHF_DocDoc_ID_AdjList_Transductive(){
        super();
        setUse(0);
    }
    
    //Function to perform transductive classification
    @Override
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        f = getClassInformation_ID(dataTrain,dataTest);
        y = getClassInformation_ID(dataTrain,dataTest);
        fTemp = new double[numTrain + numTest][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        
        if(getUse() == 0){
            adjacencyListP = new Neighbor[numTrain+numTest];
            for(int inst=0;inst<(numTrain+numTest);inst++){
                adjacencyListP[inst] = new Neighbor();
            }
            
            double[] d = new double[numTrain + numTest]; // Degree of documents
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjListDocs[inst1].getNeighbors();
                for(int inst2=0;inst2<neighbors.size();inst2++){
                    grau += neighbors.get(inst2).value;
                }
                d[inst1] = grau;
            }
            
            //Normalizing relation weights
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                if(d[inst1] == 0){ 
                    continue; 
                }
                ArrayList<IndexValue> neighbors = adjListDocs[inst1].getNeighbors();
                for(int inst2=0;inst2<neighbors.size();inst2++){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(inst2).index;
                    indVal.value = neighbors.get(inst2).value / d[inst1];
                    adjacencyListP[inst1].AddNeighbor(indVal);
                }
            }
            adjListDocs = null;
            
        }
        
        setUse(1);
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // Step 1: f = pf
            // Propagating labels among documents
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    ArrayList<IndexValue> neighbors = adjacencyListP[inst1].getNeighbors();
                    for(int inst2=0;inst2<neighbors.size();inst2++){
                        value += neighbors.get(inst2).value * f[neighbors.get(inst2).index][classe];
                    }
                    fTemp[inst1][classe] = value;
                }
            }
            
            double acmDif = 0;
            for(int inst=0;inst<numTrain+numTest;inst++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fTemp[inst][classe] - f[inst][classe]);
                    f[inst][classe] = fTemp[inst][classe];
                }    
            }
            
            if(acmDif == previousDiff){
                countDiff++;
                if(countDiff>=100){
                    exit = true;
                }
            }else{
                countDiff = 0;
                previousDiff = acmDif;
            }
            int numIt = getNumiterations();

            //Analysis of stopping criteria
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
            
            numIt++;
            setNumIterations(numIt);
            
            //Step 2: f_{L} = y_{L}
            for(int inst=0;inst<numTrain;inst++){
                int ind = (int)dataTrain.instance(inst).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    f[ind][classe] = y[ind][classe];
                }
            }
        }
        
       
        //Assigning labels to unlabeled documents considering class mass normalization
        HashMap<Integer,Double> sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
                acmProbClasse += f[inst][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        for(int inst=0;inst<numTest;inst++){
            int indTeste = (int)dataTest.instance(inst).value(0);
            double[] classeTemp = new double[numClasses];
            for(int classe=0;classe<numClasses;classe++){
                classeTemp[classe] = (double)f[indTeste][classe]/(double)sumClassInformationDocs.get(classe);
            }
            int ind = -1;
            double maior = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(classeTemp[classe] > maior){
                    ind = classe;
                    maior = classeTemp[classe];
                }
            }
            for(int classe=0;classe<numClasses;classe++){
                fUnlabeledDocs[inst][classe] = 0;
            }
            if(ind == -1){
                fUnlabeledDocs[inst][0] = 1;
            }else{
                fUnlabeledDocs[inst][ind] = 1;
            }
        }
    }
}
