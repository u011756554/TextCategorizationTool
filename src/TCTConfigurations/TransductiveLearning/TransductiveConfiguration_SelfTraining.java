//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.Parameters_DocumentNetwork_Knn;
import TCTParameters.Parameters_GNetMine_DocTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_EM;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.SemiSupervisedLearning.Parameters_TSVM;
import TCTParameters.SupervisedOneClass.ParametersIMHN;
import TCTParameters.SupervisedOneClass.ParametersKNNDensity;
import TCTParameters.SupervisedOneClass.ParametersPrototypeBasedClustering;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ragero
 */
public class TransductiveConfiguration_SelfTraining extends TransductiveConfiguration_Base implements Serializable{
    
    
    private boolean MNB;
    private boolean KNN;
    
    private Parameters_KNN parametersKNN;
    
    //supervised learning - one class
    private boolean knnDensity;
    private boolean kMeans;
    private boolean imbhn;
    
    //parameters supervised learning - one class
    private ParametersKNNDensity parametersKNNDensity;
    private ParametersIMHN parametersIMBHNR;
    private ParametersPrototypeBasedClustering parametersKME;
    
    //transductive learning - traditional
    private boolean tcbhn;
    private boolean gnetMine;
    private boolean tsvm;
    private boolean em;
    private boolean llgc;
    private boolean gfhf;
    
    // parameters transductive learning 
    private Parameters_GNetMine_DocTerm parametersGNetMine;
    private Parameters_IMHN parametersTCBHN;
    private Parameters_TSVM parametersTSVM;
    private Parameters_EM parametersEM;
    private Parameters_GFHF parametersGFHF;
    private Parameters_LLGC parametersLLGC;
    
    //Document Network Parameters
    Parameters_DocumentNetwork_Knn parametersKNNNetwork;
    
    //SelfTraining Parameters
    private ArrayList<Integer> numInstPerClassAddTraining;
    private ArrayList<Integer> numIterations;
    
    public TransductiveConfiguration_SelfTraining(){
        super();
        
        setMNB(true);
        setKNN(true);
        
        numInstPerClassAddTraining = new ArrayList<Integer>();
        numInstPerClassAddTraining.add(1);
        numInstPerClassAddTraining.add(5);
        numInstPerClassAddTraining.add(10);
        
        numIterations = new ArrayList<Integer>();
        numIterations.add(5);
        numIterations.add(10);
        numIterations.add(20);
        
        setParametersKNNDensity(new ParametersKNNDensity());
        ArrayList<Integer> numNeighbors = new ArrayList<Integer>();
        numNeighbors.add(1);
        numNeighbors.add(3);
        numNeighbors.add(5);
        numNeighbors.add(7);
        numNeighbors.add(9);
        numNeighbors.add(11);
        numNeighbors.add(13);
        numNeighbors.add(15);
        numNeighbors.add(17);
        numNeighbors.add(19);
        this.getParametersKNNDensity().setKs(numNeighbors);
        
        setParametersIMBHNR(new ParametersIMHN());
        
        setParametersKME(new ParametersPrototypeBasedClustering());
        ArrayList<Integer> numClusters = new ArrayList<Integer>();
        numClusters.add(1);
        numClusters.add(3);
        numClusters.add(5);
        numClusters.add(7);
        numClusters.add(9);
        numClusters.add(11);
        numClusters.add(13);
        numClusters.add(15);
        numClusters.add(17);
        numClusters.add(19);
        this.getParametersKME().setKs(numClusters);
        
        setParametersGNetMine(new Parameters_GNetMine_DocTerm());
        setParametersTCBHN(new Parameters_IMHN());
        setParametersTSVM(new Parameters_TSVM());
        setParametersLLGC(new Parameters_LLGC());
        setParametersGFHF(new Parameters_GFHF());
        
        setParametersEM(new Parameters_EM());
        this.getParametersEM().setMinLogLikelihood(0.01);
        ArrayList<Integer> numCompClasses = new ArrayList<Integer>();
        numCompClasses.add(1);
        numCompClasses.add(5);
        numCompClasses.add(10);
        this.getParametersEM().setNumCompClasses(numCompClasses);
        ArrayList<Double> weightsUnlabeled = new ArrayList<Double>();
        weightsUnlabeled.add(0.25);
        weightsUnlabeled.add(0.5);
        weightsUnlabeled.add(0.75);
        this.getParametersEM().setWeightsUnlabeled(weightsUnlabeled);
        
        setParametersKNN(new Parameters_KNN());

        setParametersKNNNetwork(new Parameters_DocumentNetwork_Knn());
        
        ArrayList<Integer> ks = new ArrayList<Integer>();
        ks.add(7);
        ks.add(17);
        ks.add(37);
        this.getParametersKNNNetwork().setK(ks);
        
        
        ArrayList<Double> cs = new ArrayList<Double>();
        cs.add(0.01);
        cs.add(0.1);
        cs.add(1.0);
        cs.add(10.0);
        this.parametersTSVM.setCs(cs);
        
    }

    public Parameters_DocumentNetwork_Knn getParametersKNNNetwork() {
        return parametersKNNNetwork;
    }

    public void setParametersKNNNetwork(Parameters_DocumentNetwork_Knn parametersKNNNetwork) {
        this.parametersKNNNetwork = parametersKNNNetwork;
    }
    
    public Parameters_GFHF getParametersGFHF() {
        return parametersGFHF;
    }

    public void setParametersGFHF(Parameters_GFHF parametersGFHF) {
        this.parametersGFHF = parametersGFHF;
    }

    public Parameters_LLGC getParametersLLGC() {
        return parametersLLGC;
    }

    public void setParametersLLGC(Parameters_LLGC parametersLLGC) {
        this.parametersLLGC = parametersLLGC;
    }

    public boolean isLlgc() {
        return llgc;
    }

    public void setLlgc(boolean llgc) {
        this.llgc = llgc;
    }

    public boolean isGfhf() {
        return gfhf;
    }

    public void setGfhf(boolean gfhf) {
        this.gfhf = gfhf;
    }

    public boolean isEm() {
        return em;
    }

    public void setEm(boolean em) {
        this.em = em;
    }

    public Parameters_EM getParametersEM() {
        return parametersEM;
    }

    public void setParametersEM(Parameters_EM parametersEM) {
        this.parametersEM = parametersEM;
    }

    
    
    public Parameters_KNN getParametersKNN() {
        return parametersKNN;
    }

    public void setParametersKNN(Parameters_KNN parametersKNN) {
        this.parametersKNN = parametersKNN;
    }

    public ArrayList<Integer> getNumInstPerClassAddTraining() {
        return numInstPerClassAddTraining;
    }

    public void setNumInstPerClassAddTraining(ArrayList<Integer> numInstPerClassAddTraining) {
        this.numInstPerClassAddTraining = numInstPerClassAddTraining;
    }

    public Parameters_TSVM getParametersTSVM() {
        return parametersTSVM;
    }

    public void setParametersTSVM(Parameters_TSVM parametersTSVM) {
        this.parametersTSVM = parametersTSVM;
    }

    public ArrayList<Integer> getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(ArrayList<Integer> numIterations) {
        this.numIterations = numIterations;
    }

    public boolean isMNB() {
        return MNB;
    }
    
    public boolean isKNN(){
        return KNN;
    }
    
    public void setMNB(boolean MNB) {
        this.MNB = MNB;
    }
    
    public void setKNN(boolean KNN){
        this.KNN = KNN;
    }
    
    public Parameters_GNetMine_DocTerm getParametersGNetMine() {
        return parametersGNetMine;
    }

    public void setParametersGNetMine(Parameters_GNetMine_DocTerm parametersGNetMine) {
        this.parametersGNetMine = parametersGNetMine;
    }

    public Parameters_IMHN getParametersTCBHN() {
        return parametersTCBHN;
    }

    public void setParametersTCBHN(Parameters_IMHN parametersTCBHN) {
        this.parametersTCBHN = parametersTCBHN;
    }
    
    public ParametersKNNDensity getParametersKNNDensity() {
        return parametersKNNDensity;
    }

    public void setParametersKNNDensity(ParametersKNNDensity parametersKNNDensity) {
        this.parametersKNNDensity = parametersKNNDensity;
    }

    public ParametersIMHN getParametersIMBHNR() {
        return parametersIMBHNR;
    }

    public void setParametersIMBHNR(ParametersIMHN parametersIMBHNR) {
        this.parametersIMBHNR = parametersIMBHNR;
    }

    public ParametersPrototypeBasedClustering getParametersKME() {
        return parametersKME;
    }

    public void setParametersKME(ParametersPrototypeBasedClustering parametersKME) {
        this.parametersKME = parametersKME;
    }

    public boolean isKnnDensity() {
        return knnDensity;
    }

    public void setKnnDensity(boolean knn) {
        this.knnDensity = knn;
    }

    public boolean iskMeans() {
        return kMeans;
    }

    public void setkMeans(boolean kMeans) {
        this.kMeans = kMeans;
    }

    public boolean isImbhn() {
        return imbhn;
    }

    public void setImbhn(boolean imbhn) {
        this.imbhn = imbhn;
    }

    public boolean isTcbhn() {
        return tcbhn;
    }

    public void setTcbhn(boolean tcbhn) {
        this.tcbhn = tcbhn;
    }

    public boolean isGnetMine() {
        return gnetMine;
    }

    public void setGnetMine(boolean gnetMine) {
        this.gnetMine = gnetMine;
    }

    public boolean isTsvm() {
        return tsvm;
    }

    public void setTsvm(boolean tsvm) {
        this.tsvm = tsvm;
    }
    
    
}
