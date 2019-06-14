/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations.SupervisedLearning;

import java.io.Serializable;

/**
 *
 * @author rafael
 */
public class SupervisedInductiveConfiguration_TrainTest extends SupervisedInductiveConfigurationBase implements Serializable{
    
    private String dataTrain;
    private String dataTest;
    private String dirResults;

    public SupervisedInductiveConfiguration_TrainTest() {
        super();
        setDataTrain("");
        setDataTest("");
        setDirResults("");
    }

    
    public String getDataTrain() {
        return dataTrain;
    }

    public void setDataTrain(String dataTrain) {
        this.dataTrain = dataTrain;
    }

    public String getDataTest() {
        return dataTest;
    }

    public void setDataTest(String dataTest) {
        this.dataTest = dataTest;
    }

    public String getDirResults() {
        return dirResults;
    }

    public void setDirResults(String dirResults) {
        this.dirResults = dirResults;
    }
    
    
    
}
