//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of LLGC (Learning with Local and 
//              Global Consistency) algorithm considering adjacency lists to 
//              store relation weights among terms.
// References: - D. Zhou, O. Bousquet, T. N. Lal, J. Weston, B. Schölkopf, 
//               Learning with local and global consistency, in: Proceedings of 
//               the Advances in Neural Information Processing Systems, Vol. 16,
//               2004, pp. 321–328.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import weka.core.Instances;

public class LLGC_DocDoc_ID_AdjList_Transductive extends LLGC_DocDoc_ID{
    
    // Constructor
    public LLGC_DocDoc_ID_AdjList_Transductive(){
        super();
        setAlpha(0);
    }
    
    // Constructor
    public LLGC_DocDoc_ID_AdjList_Transductive(int maxNumberInterations, double alpha){
        setMaxNumIterations(maxNumberInterations);
        setAlpha(alpha);
    }
    
    // Function to perform transductive learning
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
            d = new double[numTrain + numTest];
            listaAdjS = new Neighbor[numTrain + numTest];
            for(int inst=0;inst<(numTrain + numTest);inst++){
                listaAdjS[inst] = new Neighbor();
            }
           
            for(int inst1=0;inst1<numTrain;inst1++){
                double grau = 0;
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                ArrayList<IndexValue> neighbors = adjListDocs[ind1].getNeighbors();
                for(int inst2=0;inst2<neighbors.size();inst2++){
                    grau += neighbors.get(inst2).value;
                }
                d[ind1] = Math.sqrt(grau);
            }
            
            for(int inst1=0;inst1<numTest;inst1++){
                double grau = 0;
                int ind1 = (int)dataTest.instance(inst1).value(0);
                ArrayList<IndexValue> neighbors = adjListDocs[ind1].getNeighbors();
                for(int inst2=0;inst2<neighbors.size();inst2++){
                    grau += neighbors.get(inst2).value;
                }
                d[ind1] = Math.sqrt(grau);
            }
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                ArrayList<IndexValue> neighbors = adjListDocs[ind1].getNeighbors();
                for(int inst2=0;inst2<neighbors.size();inst2++){
                    int ind2 = neighbors.get(inst2).index;
                    double value = neighbors.get(inst2).value;
                    if(!((d[ind1] == 0)  || (d[ind2] == 0))){
                        value = value / (d[ind1] * d[ind2]);
                        IndexValue indVal = new IndexValue();
                        indVal.index = ind2;
                        indVal.value = value;
                        listaAdjS[ind1].AddNeighbor(indVal);
                    }
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                ArrayList<IndexValue> neighbors = adjListDocs[ind1].getNeighbors();
                for(int inst2=0;inst2<neighbors.size();inst2++){
                    int ind2 = neighbors.get(inst2).index;
                    double value = neighbors.get(inst2).value;
                    if(!((d[ind1] == 0)  || (d[ind2] == 0))){
                        value = value / (d[ind1] * d[ind2]);
                        IndexValue indVal = new IndexValue();
                        indVal.index = ind2;
                        indVal.value = value;
                        listaAdjS[ind1].AddNeighbor(indVal);
                    }
                }
            }
            adjListDocs = null;
            d=null;
        }
        setUse(1);
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // Propagating labels from documents to documents
            for(int inst1=0;inst1<numTrain;inst1++){
                int id1 = (int)dataTrain.instance(inst1).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    double fTemp1=0;
                    ArrayList<IndexValue> neighbors = listaAdjS[id1].getNeighbors();
                    for(int inst2=0;inst2<neighbors.size();inst2++){
                        fTemp1 += alpha * neighbors.get(inst2).value * f[neighbors.get(inst2).index][classe];
                        
                    }
                    fTemp[id1][classe] = fTemp1  + (1 - alpha)*y[id1][classe];
                }    
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int id1 = (int)dataTest.instance(inst1).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    double fTemp1=0;
                    ArrayList<IndexValue> neighbors = listaAdjS[id1].getNeighbors();
                    for(int inst2=0;inst2<neighbors.size();inst2++){
                        fTemp1 += alpha * neighbors.get(inst2).value * f[neighbors.get(inst2).index][classe];
                    }
                    fTemp[id1][classe] = fTemp1  + (1 - alpha)*y[id1][classe];
                }    
            }
            
            double acmDif = 0;
            for(int inst=0;inst<(numTrain+numTest);inst++){
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
            
            // Analysis of stopping criteria
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
                
            numIt++;
            setNumIterations(numIt);
        }
        
        //Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            int indTeste = (int)dataTest.instance(inst).value(0);
            int ind= -1;
            double maior = -30000.0;
            for(int classe=0;classe<numClasses;classe++){
                    if(f[indTeste][classe] > maior){
                        ind = classe;
                        maior = f[indTeste][classe];
                    }
            }
            for(int classe=0;classe<numClasses;classe++){
                fUnlabeledDocs[inst][0] = 0.0;
            }
            if(ind == -1){
                fUnlabeledDocs[inst][0] = 1;
            }else{
                fUnlabeledDocs[inst][ind] = 1;
            }
        }
        
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    
}
