/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations.TransductiveLearning;

import java.io.Serializable;

/**
 *
 * @author rafael
 */
public class TransductiveConfiguration_TrainTest extends TransductiveConfiguration implements Serializable{
    
    private String dataTrain;
    private String dataTest;
    private String dirResults;

    public TransductiveConfiguration_TrainTest() {
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
