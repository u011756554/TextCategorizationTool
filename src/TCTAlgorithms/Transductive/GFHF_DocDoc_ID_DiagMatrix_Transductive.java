//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This class contains the variables and functions shared 
//              by different implementations of GFHF (Gaussian Field and 
//              Harmonic Function) algorithm (Zhu et al., 2003). The graph is 
//              represented by diagonal matrix.
// References: - X. Zhu, Z. Ghahramani, J. Lafferty, Semi-supervised learning 
//               using gaussian fields and harmonic functions, in: Proceedings 
//               of the International Conference on Machine Learning, AAAI 
//               Press, 2003, pp. 912–919.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import java.util.HashMap;
import weka.core.Instances;

public class GFHF_DocDoc_ID_DiagMatrix_Transductive extends GFHF_DocDoc_ID{
    
    //Constructor
    public GFHF_DocDoc_ID_DiagMatrix_Transductive(){
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
            
            p = new double[numTrain + numTest][numTrain + numTest];
                        
            double[] d = new double[numTrain + numTest];
            
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
                d[ind1] = grau;
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
                d[ind1] = grau;
            }
            
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                    
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }    
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                    
                }
            }
            
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int inst2=0;inst2<numTrain;inst2++){
                    int ind2 = (int)dataTrain.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                    
                }
                for(int inst2=0;inst2<numTest;inst2++){
                    int ind2 = (int)dataTest.instance(inst2).value(0);
                    if(ind1 > ind2){
                        if((matSim[ind1][ind2] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind1][ind2] / d[ind1];
                        }    
                    }else{
                        if((matSim[ind2][ind1] == 0) || (d[ind1] == 0)){
                            p[ind1][ind2] = 0;
                        }else{
                            p[ind1][ind2] = matSim[ind2][ind1] / d[ind1];
                        }
                    }
                }
            }
            matSim = null;
        }
        setUse(1);
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            //1st step: f = pf
            /*for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<numTrain;inst2++){
                        int ind2 = (int)dataTrain.instance(inst2).value(0);
                        if(ind1 > ind2){
                            value += p[ind1][ind2] * f[ind2][classe];
                        }else{
                            value += p[ind2][ind1] * f[ind2][classe];
                        }    
                    }
                    fTemp[ind1][classe] = value;
                }
            }
            for(int inst1=0;inst1<numTrain;inst1++){
                int ind1 = (int)dataTrain.instance(inst1).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<numTest;inst2++){
                        int ind2 = (int)dataTest.instance(inst2).value(0);
                        if(ind1 > ind2){
                            value += p[ind1][ind2] * f[ind2][classe];
                        }else{
                            value += p[ind2][ind1] * f[ind2][classe];
                        }    
                    }
                    fTemp[ind1][classe] = value;
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<numTrain;inst2++){
                        int ind2 = (int)dataTrain.instance(inst2).value(0);
                        if(ind1 > ind2){
                            value += p[ind1][ind2] * f[ind2][classe];
                        }else{
                            value += p[ind2][ind1] * f[ind2][classe];
                        }    
                    }
                    fTemp[ind1][classe] = value;
                }
            }
            for(int inst1=0;inst1<numTest;inst1++){
                int ind1 = (int)dataTest.instance(inst1).value(0);
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<numTest;inst2++){
                        int ind2 = (int)dataTest.instance(inst2).value(0);
                        if(ind1 > ind2){
                            value += p[ind1][ind2] * f[ind2][classe];
                        }else{
                            value += p[ind2][ind1] * f[ind2][classe];
                        }    
                    }
                    fTemp[ind1][classe] = value;
                }
            }*/
            
            
            for(int inst1=0;inst1<(numTrain+numTest);inst1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                            value += p[inst1][inst2] * f[inst2][classe];
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
            
            //2nd step:  f_{L} = y_{L}
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
