//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: May 1, 2015
// Description: This class contains the variables and functions shared 
//              by different implementations of GFHF (Gaussian Field and 
//              Harmonic Function) algorithm (Zhu et al., 2003).
// References: - X. Zhu, Z. Ghahramani, J. Lafferty, Semi-supervised learning 
//               using gaussian fields and harmonic functions, in: Proceedings 
//               of the International Conference on Machine Learning, AAAI 
//               Press, 2003, pp. 912–919.
//*****************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.Neighbor;
import weka.core.Instances;

public class GFHF_DocDoc_ID extends TransductiveLearner{
    
    protected double[][] f; // Class information of documents 
    protected double[][] fTemp; // Class information of documents
    protected double[][] y; // Real class information (labels) of labeled documents 
    protected int numTrain; // Number of labeled documents
    protected int numTest; // Number of unlabeled documents
    protected int numClasses; // Number of Classes
    protected int numTerms; // Number of Terms
    protected int maxNumberInterations; // Maximum number of iterations
    
    //Matrices and adjacency lists of the algorithm
    protected double[][] matSim; 
    protected double[][] p;
    protected Neighbor[] adjListDocs;
    protected Neighbor[] adjacencyListP;
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){}
    
    public void setAdjListDocs(Neighbor[] adjListDocs){
        this.adjListDocs = adjListDocs;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
      public void setMatSim(double[][] matSim){
        this.matSim = matSim;
    }
      
}
