//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This class contais the variables and function shared by the 
//              classes wich implement LLGC (Learning with Local and Global 
//              Consistency) algorithm. 
// References: - D. Zhou, O. Bousquet, T. N. Lal, J. Weston, B. Schölkopf, 
//               Learning with local and global consistency, in: Proceedings of 
//               the Advances in Neural Information Processing Systems, Vol. 16,
//               2004, pp. 321–328.
//*****************************************************************************


package TCTAlgorithms.Transductive;

import TCTStructures.Neighbor;
import weka.core.Instances;

public class LLGC_DocDoc_ID extends TransductiveLearner{
    protected double[][] f; // Class information of documents
    protected double[][] fTemp; // Class information of documents
    protected double[][] y; // Real class information (labels) of labeled documents 
    protected int numTrain; // Number of labeled documents
    protected int numTest; // Number of unlabeled documents
    protected int numClasses; // Number of classes
    protected int numTerms; // Number of terms
    protected int maxNumberInterations; // Maximum number of iterations
    protected double alpha; // Parameter of the algorithms
    
    //Variables to store relation weights among documents
    protected double[][] matSim;
    protected Neighbor[] adjListDocs;

    // Variables of the algorithms
    protected double[] d;
    protected double[][] s;
    protected Neighbor[] listaAdjS;
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){}
    
    public void setAdjListDocs(Neighbor[] adjListDocs){
        this.adjListDocs = adjListDocs;
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
    
    
    public void setMatSim(double[][] matSim){
        this.matSim = matSim;
    }
    
}
