//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SemiSupervisedLearning;

import TCTConfigurations.ConfigurationBase;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SupervisedLearning.Parameters_J48;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import TCTParameters.SupervisedLearning.Parameters_MLP;
import TCTParameters.Parameters_NumLabeledDocs;
import TCTParameters.SupervisedLearning.Parameters_SMO;
import java.io.Serializable;
import java.util.ArrayList;

public class SemiSupervisedInductiveConfiguration_OnlyLabeledExamples extends ConfigurationBase implements Serializable{
    
    private boolean Porcentage;
    
    private String dirEntrada;
    private String dirSaida;
    
    private boolean NB;
    private boolean MNB;
    private boolean J48;
    private boolean SMO;
    private boolean KNN;
    private boolean MLP;
    private boolean IMBHN;
    private boolean IMBHN2;
    
    private Parameters_J48 parametersJ48;
    private Parameters_KNN parametersKNN;
    private Parameters_MLP parametersMLP;
    private Parameters_SMO parametersSMO;
    private Parameters_IMHN parametersIMBHN;
    private Parameters_IMHN parametersIMBHN2;
    
    Parameters_NumLabeledDocs parametersNumLabeledInstancesPerClass = new Parameters_NumLabeledDocs();
    
    
    public SemiSupervisedInductiveConfiguration_OnlyLabeledExamples(){
        
        parametersJ48 = new Parameters_J48();
        parametersMLP = new Parameters_MLP();
        parametersSMO = new Parameters_SMO();
        parametersKNN = new Parameters_KNN();
        parametersKNN.setNeighbors(new ArrayList<Integer>());
        parametersKNN.addNeighbors(7);
        parametersKNN.addNeighbors(17);
        parametersKNN.addNeighbors(37);
        parametersKNN.addNeighbors(57);
        parametersKNN.setUnweighted(false);
        parametersKNN.setWeighted(true);
        
        parametersIMBHN = new Parameters_IMHN();
        parametersIMBHN2 = new Parameters_IMHN();
        
        setDirEntrada("");
        setDirSaida("");
        
        setNumReps(10);
        setNumFolds(10);
        setNumThreads(10);
        
        setPorcentage(false);
        
    }
    
    public boolean isPorcentage() {
        return Porcentage;
    }
    
    public void setPorcentage(boolean Porcentage) {
        this.Porcentage = Porcentage;
    }
    
    public boolean isJ48() {
        return J48;
    }

    public boolean isKNN() {
        return KNN;
    }

    public boolean isMLP() {
        return MLP;
    }

    public boolean isMNB() {
        return MNB;
    }

    public boolean isNB() {
        return NB;
    }

    public boolean isIMBHN() {
        return IMBHN;
    }
    
    public boolean isIMBHN2() {
        return IMBHN2;
    }
    
    public boolean isSMO() {
        return SMO;
    }

    public String getDirEntrada() {
        return dirEntrada;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    public Parameters_J48 getParametersJ48(){
        return parametersJ48;
    }
    
    public Parameters_KNN getParametersKNN() {
        return parametersKNN;
    }

    public Parameters_MLP getParametersMLP() {
        return parametersMLP;
    }

    public Parameters_IMHN getParametersIMBHN() {
        return parametersIMBHN;
    }
    
    public Parameters_IMHN getParametersIMBHN2() {
        return parametersIMBHN2;
    }
    
    public Parameters_SMO getParametersSMO() {
        return parametersSMO;
    }

    public void setJ48(boolean J48) {
        this.J48 = J48;
    }

    public void setKNN(boolean KNN) {
        this.KNN = KNN;
    }

    public void setMLP(boolean MLP) {
        this.MLP = MLP;
    }

    public void setMNB(boolean MNB) {
        this.MNB = MNB;
    }

    public void setNB(boolean NB) {
        this.NB = NB;
    }

    public void setIMBHN(boolean IMBHN) {
        this.IMBHN = IMBHN;
    }
    
    public void setIMBHN2(boolean IMBHN2) {
        this.IMBHN2 = IMBHN2;
    }

    public void setSMO(boolean SMO) {
        this.SMO = SMO;
    }

    public void setDirEntrada(String dirEntrada) {
        this.dirEntrada = dirEntrada;
    }

    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
    }
    
    public void setParametersJ48(Parameters_J48 parametersJ48){
        this.parametersJ48 = parametersJ48;
    }
    
    public void setParametersKNN(Parameters_KNN parametersKNN) {
        this.parametersKNN = parametersKNN;
    }

    public void setParametersMLP(Parameters_MLP parametersMLP) {
        this.parametersMLP = parametersMLP;
    }

    public void setParametersIMBHN(Parameters_IMHN parametersIMBHN) {
        this.parametersIMBHN = parametersIMBHN;
    }
    
    public void setParametersIMBHN2(Parameters_IMHN parametersIMBHN2) {
        this.parametersIMBHN2 = parametersIMBHN2;
    }

    public void setParametersSMO(Parameters_SMO parametersSMO) {
        this.parametersSMO = parametersSMO;
    }
    
    public Parameters_NumLabeledDocs getParametersNumLabeledInstancesPerClass(){
        return this.parametersNumLabeledInstancesPerClass;
    }

}
