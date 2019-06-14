/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations.SupervisedLearning;

import TCTParameters.Parameters_NumLabeledDocs;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples extends SupervisedInductiveConfiguraton_OneClass implements Serializable{
    
    private boolean porcentage;
    Parameters_NumLabeledDocs parametersNumLabeledInstancesPerClass = new Parameters_NumLabeledDocs();
    
    public SupervisedInductiveConfiguraton_OneClass_FewLabeledExamples(){
        super();
        
        setPorcentage(false);
        setParametersNumLabeledInstancesPerClass(new Parameters_NumLabeledDocs());
        
        ArrayList<Double> numLabeledInstancesPerClass = new ArrayList<Double>();
        numLabeledInstancesPerClass.add(1.0);
        numLabeledInstancesPerClass.add(5.0);
        numLabeledInstancesPerClass.add(10.0);
        numLabeledInstancesPerClass.add(20.0);
        numLabeledInstancesPerClass.add(30.0);
        getParametersNumLabeledInstancesPerClass().setNumLabeledInstancesPerClass(numLabeledInstancesPerClass);
        
    }

    public boolean isPorcentage() {
        return porcentage;
    }

    public void setPorcentage(boolean porcentage) {
        this.porcentage = porcentage;
    }

    public Parameters_NumLabeledDocs getParametersNumLabeledInstancesPerClass() {
        return parametersNumLabeledInstancesPerClass;
    }

    public void setParametersNumLabeledInstancesPerClass(Parameters_NumLabeledDocs parametersNumLabeledInstancesPerClass) {
        this.parametersNumLabeledInstancesPerClass = parametersNumLabeledInstancesPerClass;
    }
    
}
