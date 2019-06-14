//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This algorithm performs transduction through term networks.
//              The classification process is the same performed in 
//              (Rossi et al., 2015), except the algorithm GFHF (Zhu et al., 
//              2013) is used instead of LLGC (Zhou, et al., 2014) algorithm to
//              propagate labels among terms
// References: - R. G. Rossi, S. O. Rezende, A. A. Lopes, Term Network Approach 
//               for Transductive Classification, in: International Conference 
//               on Intelligent Text Processing and Computational Linguistics, 
//               2015 (in press).
//             - X. Zhu, Z. Ghahramani, J. Lafferty, Semi-supervised learning 
//               using gaussian fields and harmonic functions, in: Proceedings 
//               of the International Conference on Machine Learning, AAAI 
//               Press, 2003, pp. 912–919.
//             - D. Zhou, O. Bousquet, T. N. Lal, J. Weston, B. Schölkopf, 
//               Learning with local and global consistency, in: Proceedings of 
//               the Advances in Neural Information Processing Systems, Vol. 16,
//               2004, pp. 321–328.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import java.util.ArrayList;
import java.util.HashMap;
import weka.core.Instance;
import weka.core.Instances;

public class GFHF_TermTerm_Transductive extends TransductiveLearner{
    private double[][] f; // Class information of terms
    private double[][] fTemp; // Class information of terms
    private double[][] y; // Real class information (labels) of labeled documents 
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int maxNumberInterations; // Maximum number of iterations
    
    private Neighbor[] adjacencyListTerms; // Adjancecy list containing term-term relations
    
    //Matrices of the algorithms
    private double[][] p;
    private boolean[] labeled;
    
    public GFHF_TermTerm_Transductive(){
        super();
    }
        
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        labeled = new boolean[numTerms];
        
        f = getClassInformation(dataTrain); // Assigning initial class information of terms
        y = getClassInformation(dataTrain);
        fTemp = new double[numTerms][numClasses];
        fUnlabeledDocs = new double[numTest][numClasses];
        
        if(getUse() == 0){
            p = new double[numTerms][numTerms];

            double[] d = new double[numTerms]; // degree o terms
            for(int atr1=0;atr1<numTerms;atr1++){
                double grau = 0;
                ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                for(int atr2=0;atr2<neighbors.size();atr2++){
                    grau += neighbors.get(atr2).value;
                }
                d[atr1] = grau;
            }
            for(int atr1=0;atr1<numTerms;atr1++){
                ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                for(int atr2=0;atr2<neighbors.size();atr2++){
                    if((d[atr1]==0) || (neighbors.get(atr2).value==0)){
                        p[atr1][neighbors.get(atr2).index] = 0;
                    }else{
                        p[atr1][neighbors.get(atr2).index] = neighbors.get(atr2).value/d[atr1];
                    }
                }
            }
        }
        setUse(1);
        
        setNumIterations(0);
        double previousDiff = 0;
        int countDiff = 0;
        boolean exit = false;
        
        while(exit == false){
            //1st step: f = pf
            for(int atr1=0;atr1<numTerms;atr1++){
                for(int classe=0;classe<numClasses;classe++){
                    double value = 0;
                    ArrayList<IndexValue> neighbors = adjacencyListTerms[atr1].getNeighbors();
                    for(int atr2=0;atr2<neighbors.size();atr2++){
                        value += p[atr1][neighbors.get(atr2).index] * f[neighbors.get(atr2).index][classe];
                    }
                    fTemp[atr1][classe] = value;
                }
            }
            
            double acmDif = 0;
            for(int term=0;term<numTerms;term++){
                for(int classe=0;classe<numClasses;classe++){
                    acmDif += Math.abs(fTemp[term][classe] - f[term][classe]);
                    f[term][classe] = fTemp[term][classe];
                }    
            }
            
            int numIt = getNumiterations();
            if(acmDif == previousDiff){
                countDiff++;
                if(countDiff>=100){
                    exit = true;
                }
            }else{
                countDiff = 0;
                previousDiff = acmDif;
            }

            // Analysis of stopping criteria
            if((acmDif == 0)||(getMaxNumberIterations() == numIt)){
                exit = true;
            }
            
            numIt++;
            setNumIterations(numIt);
            
            // 2nd step: f_{L} = y_{L}
            for(int term=0;term<numTrain;term++){
                for(int classe=0;classe<numClasses;classe++){
                    if(labeled[term] == true){
                        f[term][classe] = y[term][classe];
                    }
                    
                }
            }
        }
        
        //Class mass normalization
        HashMap<Integer,Double> sumClassInformationDocs = new HashMap<Integer,Double>();
        for(int classe=0;classe<numClasses;classe++){
            double acmProbClasse = 0;
            for(int term=0;term<numTerms;term++){
                acmProbClasse += f[term][classe];
            }
            sumClassInformationDocs.put(classe, acmProbClasse);
        }
        for(int classe=0;classe<numClasses;classe++){
            double somaClasse = sumClassInformationDocs.get(classe);
            for(int term=0;term<numTerms;term++){
                double value = f[term][classe];
                value = value / somaClasse;
                f[term][classe] = value;
            }
        }
        
        
        //Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            double sum = 0;
            for(int classe=0;classe<numClasses;classe++){
                double acmClasse = 0;
                for(int term=0;term<numTerms;term++){
                    acmClasse += dataTest.instance(inst).value(term) * f[term][classe];
                }
                fUnlabeledDocs[inst][classe] = acmClasse;
                sum += acmClasse;
            }
            for(int classe=0;classe<numClasses;classe++){
                double value = fUnlabeledDocs[inst][classe];
                value = value / sum;
                fUnlabeledDocs[inst][classe] = value;
            }
        }
        
        for(int inst=0;inst<numTest;inst++){
            double[] classeTemp = new double[numClasses];
            for(int classe=0;classe<numClasses;classe++){
                classeTemp[classe] = (double)fUnlabeledDocs[inst][classe];
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
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setAdjacencyListTerms(Neighbor[] adjacencyListTerms){
        this.adjacencyListTerms = adjacencyListTerms;
    }
    
    public double[][] getClassInformation(Instances dataTrain){
        double[][] vetor = new double[numTerms][numClasses];
        for(int term=0;term<numTerms;term++){
            boolean flag = false;
            for(int classe=0;classe<numClasses;classe++){
                double numerator = 0;
                double denominator = 0;
                for(int inst=0;inst<numTrain;inst++){
                    Instance instance = dataTrain.instance(inst);
                    int infClasse = (int)instance.classValue();
                    denominator += instance.value(term);
                    if(infClasse == classe){
                        numerator += instance.value(term);
                    }
                }
                if(denominator == 0){
                    vetor[term][classe] = 0;
                }else{
                    vetor[term][classe] = numerator/denominator;
                }
                if(numerator > 0){
                    flag = true;
                }
            }
            labeled[term] = flag;
        }
        
        return vetor;
    }
    
}
