/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations.SupervisedLearning;

import TCTConfigurations.ConfigurationBase;
import TCTParameters.SupervisedOneClass.ParametersIMHN;
import TCTParameters.SupervisedOneClass.ParametersKNNDensity;
import TCTParameters.SupervisedOneClass.ParametersMM;
import TCTParameters.SupervisedOneClass.ParametersNaiveBayes;
import TCTParameters.SupervisedOneClass.ParametersOneClassSVM;
import TCTParameters.SupervisedOneClass.ParametersPrototypeBasedClustering;
import java.io.Serializable;

/**
 *
 * @author deni
 */
public class SupervisedInductiveConfiguraton_OneClass extends ConfigurationBase implements Serializable {
    
    private String dirInput;
    private String dirOutput;

    private boolean NB;
    private boolean MNB;
    private boolean KME;
    private boolean KMED;
    private boolean KNNDensity;
    private boolean KNNRelativeDensity;
    private boolean IMBHNR;
    private boolean IMBHNC;
    private boolean SVM;
    private boolean KSNNDensity;
    private boolean BKM;
    private boolean GMM;
        
    private ParametersNaiveBayes parametersNB;
    private ParametersNaiveBayes parametersMNB;
    private ParametersPrototypeBasedClustering parametersKME;
    private ParametersPrototypeBasedClustering parametersKMED;
    private ParametersPrototypeBasedClustering parametersBKM;
    private ParametersKNNDensity parametersKNNDensity;
    private ParametersKNNDensity parametersKNNRelativeDensity;
    private ParametersIMHN parametersIMBHNR;
    private ParametersIMHN parametersIMBHNC;
    private ParametersKNNDensity parametersKSNNDensity;
    private ParametersOneClassSVM parametersSVM;
    private ParametersMM parametersGMM;
        
    public SupervisedInductiveConfiguraton_OneClass(){
        setNumReps(10);
        setNumFolds(10);
        setDirInput("");
        setDirOutput("");
        setNB(false);
        setMNB(false);
        setKME(false);
        setKMED(false);
        setKNNDensity(false);
        setKNNRelativeDensity(false);
        setIMBHNR(false);
        setIMBHNC(false);
        setSVM(false);
        setKSNNDensity(false);
        setBKM(false);
        setGMM(false);
        
        setParametersNB(new ParametersNaiveBayes());
        setParametersMNB(new ParametersNaiveBayes());
        setParametersKMED(new ParametersPrototypeBasedClustering());
        setParametersBKM(new ParametersPrototypeBasedClustering());
        setParametersKNNRelativeDensity(new ParametersKNNDensity());
        setParametersIMBHNR(new ParametersIMHN());
        setParametersIMBHNC(new ParametersIMHN());
        setParametersSVM(new ParametersOneClassSVM());
        setParametersKSNNDensity(new ParametersKNNDensity());
        getParametersKSNNDensity().setKs(parametersKSNNDensity.generateKs(1, 60, 1));
        setParametersKNNDensity(new ParametersKNNDensity());
        setParametersKME(new ParametersPrototypeBasedClustering());
        setParametersGMM(new ParametersMM());

    }

    public boolean isGMM() {
        return GMM;
    }

    public void setGMM(boolean GMM) {
        this.GMM = GMM;
    }

    public ParametersMM getParametersGMM() {
        return parametersGMM;
    }

    public void setParametersGMM(ParametersMM parametersGMM) {
        this.parametersGMM = parametersGMM;
    }

    public ParametersOneClassSVM getParametersSVM() {
        return parametersSVM;
    }

    public void setParametersSVM(ParametersOneClassSVM parametersSVM) {
        this.parametersSVM = parametersSVM;
    }

    public boolean isKSNNDensity() {
        return KSNNDensity;
    }

    public void setKSNNDensity(boolean KSNNDensity) {
        this.KSNNDensity = KSNNDensity;
    }

    public ParametersKNNDensity getParametersKSNNDensity() {
        return parametersKSNNDensity;
    }

    public void setParametersKSNNDensity(ParametersKNNDensity parametersKSNNDensity) {
        this.parametersKSNNDensity = parametersKSNNDensity;
    }

    
    public boolean isSVM() {
        return SVM;
    }

    public void setSVM(boolean SVM) {
        this.SVM = SVM;
    }
    
    public String getDirInput() {
        return dirInput;
    }

    public void setDirInput(String dirInput) {
        this.dirInput = dirInput;
    }

    public String getDirOutput() {
        return dirOutput;
    }

    public void setDirOutput(String dirOutput) {
        this.dirOutput = dirOutput;
    }

    public boolean isNB() {
        return NB;
    }

    public void setNB(boolean NB) {
        this.NB = NB;
    }

    public boolean isMNB() {
        return MNB;
    }

    public void setMNB(boolean MNB) {
        this.MNB = MNB;
    }

    public boolean isKME() {
        return KME;
    }

    public void setKME(boolean KME) {
        this.KME = KME;
    }

    public boolean isKMED() {
        return KMED;
    }

    public void setKMED(boolean KMED) {
        this.KMED = KMED;
    }
    
    public void setBKM(boolean BKM){
        this.BKM = BKM;
    }

    public ParametersPrototypeBasedClustering getParametersBKM() {
        return parametersBKM;
    }

    public void setParametersBKM(ParametersPrototypeBasedClustering parametersBKM) {
        this.parametersBKM = parametersBKM;
    }

    public ParametersNaiveBayes getParametersNB() {
        return parametersNB;
    }

    public void setParametersNB(ParametersNaiveBayes parametersNB) {
        this.parametersNB = parametersNB;
    }

    public ParametersNaiveBayes getParametersMNB() {
        return parametersMNB;
    }

    public void setParametersMNB(ParametersNaiveBayes parametersMNB) {
        this.parametersMNB = parametersMNB;
    }

    public ParametersPrototypeBasedClustering getParametersKME() {
        return parametersKME;
    }

    public void setParametersKME(ParametersPrototypeBasedClustering parametersKME) {
        this.parametersKME = parametersKME;
    }

    public ParametersPrototypeBasedClustering getParametersKMED(){
        return parametersKMED;
    }
    
    public void setParametersKMED(ParametersPrototypeBasedClustering parametersKMedoid) {
        this.parametersKMED = parametersKMedoid;
    }

    public boolean isKNNDensity() {
        return KNNDensity;
    }

    public void setKNNDensity(boolean KNNDensity) {
        this.KNNDensity = KNNDensity;
    }

    public ParametersKNNDensity getParametersKNNDensity() {
        return parametersKNNDensity;
    }

    public void setParametersKNNDensity(ParametersKNNDensity parametersKNNDensity) {
        this.parametersKNNDensity = parametersKNNDensity;
    }

    public boolean isKNNRelativeDensity() {
        return KNNRelativeDensity;
    }

    public boolean isBKM() {
        return BKM;
    }
    
    

    public void setKNNRelativeDensity(boolean KNNRelativeDensity) {
        this.KNNRelativeDensity = KNNRelativeDensity;
    }

    public ParametersKNNDensity getParametersKNNRelativeDensity() {
        return parametersKNNRelativeDensity;
    }

    public void setParametersKNNRelativeDensity(ParametersKNNDensity parametersKNNRelativeDensity) {
        this.parametersKNNRelativeDensity = parametersKNNRelativeDensity;
    }

    public boolean isIMBHNR() {
        return IMBHNR;
    }

    public void setIMBHNR(boolean IMBHNR) {
        this.IMBHNR = IMBHNR;
    }

    public ParametersIMHN getParametersIMBHNR() {
        return parametersIMBHNR;
    }

    public void setParametersIMBHNR(ParametersIMHN parametersIMBHNR) {
        this.parametersIMBHNR = parametersIMBHNR;
    }

    public boolean isIMBHNC() {
        return IMBHNC;
    }

    public void setIMBHNC(boolean IMBHNC) {
        this.IMBHNC = IMBHNC;
    }

    public ParametersIMHN getParametersIMBHNC() {
        return parametersIMBHNC;
    }

    public void setParametersIMBHNC(ParametersIMHN parametersIMBHNC) {
        this.parametersIMBHNC = parametersIMBHNC;
    }
    
    @Override
    public String toString(){
        StringBuffer output = new StringBuffer();
        output.append("- Input Directory: " + this.dirInput + "\n");
        output.append("- Output Directory: " + this.dirOutput + "\n");
        output.append("- NB: " + this.isNB() + "\n");
        output.append("- MNB: " + this.isMNB() + "\n");
        output.append("- KME: " + this.isKME() + "\n");
        output.append("- KMED: " + this.isKMED() + "\n");
        output.append("- BKM: " + this.isBKM() + "\n");
        output.append("- KNNDensity: " + this.isKNNDensity() + "\n");
        output.append("- KNNRelativeDensity: " + this.isKNNRelativeDensity() + "\n");
        output.append("- IMBHNR: " + this.isIMBHNR() + "\n");
        return output.toString();
    }
    
}
