//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of LLGC (Learning with Local and 
//              Global Consistency) algorithm considering diagonal matrix to 
//              store relation weights among terms.
// References: - D. Zhou, O. Bousquet, T. N. Lal, J. Weston, B. Schölkopf, 
//               Learning with local and global consistency, in: Proceedings of 
//               the Advances in Neural Information Processing Systems, Vol. 16,
//               2004, pp. 321–328.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import weka.core.Instances;

public class LLGC_DocDoc_ID_DiagMatrix_Transductive extends LLGC_DocDoc_ID{
    
    // Constructor
    public LLGC_DocDoc_ID_DiagMatrix_Transductive(){
        super();
        setAlpha(0);
        setUse(0);
    }
    
    // Constructor
    public LLGC_DocDoc_ID_DiagMatrix_Transductive(int maxNumberInterations, double alpha){
        setMaxNumIterations(maxNumberInterations);
        setAlpha(alpha);
        setUse(0);
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
            s = new double[numTrain + numTest][];
            for(int inst=0;inst<(numTrain+numTest);inst++){
                s[inst] = new double[inst + 1];
            }
            
            
            for(int inst1=0;inst1<numTrain;inst1++){
                double grau = 0;
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                    
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                    
                }
                d[ind1] = Math.sqrt(grau);
            }
            for(int inst1=0;inst1<numTest;inst1++){
                double grau = 0;
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        grau += matSim[ind1][ind2];
                    }else{
                        grau += matSim[ind2][ind1];
                    }
                    
                }
                d[ind1] = Math.sqrt(grau);
            }
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind1][ind2] = 0;
                        }else{
                            s[ind1][ind2] = matSim[ind1][ind2] / (d[ind1] * d[ind2]);
                        }
                    }else{
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind2][ind1] = 0;
                        }else{
                            s[ind2][ind1] = matSim[ind2][ind1] / (d[ind1] * d[ind2]);
                        }
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind1][ind2] = 0;
                        }else{
                            s[ind1][ind2] = matSim[ind1][ind2] / (d[ind1] * d[ind2]);
                        }
                    }else{
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind2][ind1] = 0;
                        }else{
                            s[ind2][ind1] = matSim[ind2][ind1] / (d[ind1] * d[ind2]);
                        }
                    }
                }
            }
            
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind1][ind2] = 0;
                        }else{
                            s[ind1][ind2] = matSim[ind1][ind2] / (d[ind1] * d[ind2]);
                        }
                    }else{
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind2][ind1] = 0;
                        }else{
                            s[ind2][ind1] = matSim[ind2][ind1] / (d[ind1] * d[ind2]);
                        }
                    }
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind1][ind2] = 0;
                        }else{
                            s[ind1][ind2] = matSim[ind1][ind2] / (d[ind1] * d[ind2]);
                        }
                    }else{
                        if((d[ind1] == 0)  || (d[ind2] == 0)){
                            s[ind2][ind1] = 0;
                        }else{
                            s[ind2][ind1] = matSim[ind2][ind1] / (d[ind1] * d[ind2]);
                        }
                    }
                }
            }
            d=null;
            matSim=null;
            
        }
        setUse(1);
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            // Propagating labels from documents to documents
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int classe=0;classe<numClasses;classe++){
                    double fTemp1=0;
                    for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                        if(inst1 > inst2){
                            fTemp1 += alpha * s[inst1][inst2] * f[inst2][classe];
                        }else{
                            fTemp1 += alpha * s[inst2][inst1] * f[inst2][classe];
                        }
                    }
                    fTemp[inst1][classe] = fTemp1  + (1 - alpha)*y[inst1][classe];
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

        }
        
        // Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            int indTeste = (int)dataTest.instance(inst).value(0);
            int ind = -1;
            double maior = Double.MIN_VALUE;
            for(int classe=0;classe<numClasses;classe++){
                if(f[indTeste][classe] > maior){
                    ind = classe;
                    maior = f[indTeste][classe];
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
