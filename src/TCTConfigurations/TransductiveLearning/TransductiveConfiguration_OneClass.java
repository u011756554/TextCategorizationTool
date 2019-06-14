/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations.TransductiveLearning;

import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class TransductiveConfiguration_OneClass extends TransductiveConfiguration_Base implements Serializable{
    
    private boolean selfTraining;
    private boolean tcbhnDirect;
    
    private TransductiveConfiguration_SelfTraining parametersSelfTrainig;
    private Parameters_IMHN parametersTCBHNDirect;
    
    public TransductiveConfiguration_OneClass(){
        setSelfTraining(false);
        setParametersSelfTrainig(new TransductiveConfiguration_SelfTraining());
        setParametersTCBHNDirect(new Parameters_IMHN());
        getParametersTCBHNDirect().addTaxaerrorCorrectionRate(0.1);
        getParametersTCBHNDirect().addTaxaerrorCorrectionRate(0.5);
        getParametersTCBHNDirect().addTaxaerrorCorrectionRate(0.001);
        getParametersTCBHNDirect().addTaxaerrorCorrectionRate(0.005);
        getParametersTCBHNDirect().addTaxaerrorCorrectionRate(0.0001);
        getParametersTCBHNDirect().addTaxaerrorCorrectionRate(0.0005);
        
        
        ArrayList<Double> numLabeledInstancesPerClass = new ArrayList<Double>();
        numLabeledInstancesPerClass.add(1.0);
        numLabeledInstancesPerClass.add(5.0);
        numLabeledInstancesPerClass.add(10.0);
        numLabeledInstancesPerClass.add(20.0);
        numLabeledInstancesPerClass.add(30.0);
        super.getParametersNumLabeledInstancesPerClass().setNumLabeledInstancesPerClass(numLabeledInstancesPerClass);
        
        
    }

    public TransductiveConfiguration_SelfTraining getParametersSelfTrainig() {
        return parametersSelfTrainig;
    }

    public void setParametersSelfTrainig(TransductiveConfiguration_SelfTraining parametersSelfTrainig) {
        this.parametersSelfTrainig = parametersSelfTrainig;
    }

    public boolean isSelfTraining() {
        return selfTraining;
    }

    public void setSelfTraining(boolean selfTraining) {
        this.selfTraining = selfTraining;
    }

    public boolean isTcbhnDirect() {
        return tcbhnDirect;
    }

    public void setTcbhnDirect(boolean tcbhnDirect) {
        this.tcbhnDirect = tcbhnDirect;
    }

    public Parameters_IMHN getParametersTCBHNDirect() {
        return parametersTCBHNDirect;
    }

    public void setParametersTCBHNDirect(Parameters_IMHN parametersTCBHNDirect) {
        this.parametersTCBHNDirect = parametersTCBHNDirect;
    }
    
    
}
