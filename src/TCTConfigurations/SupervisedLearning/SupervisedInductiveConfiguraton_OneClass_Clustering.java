/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations.SupervisedLearning;

import TCTParameters.SupervisedOneClass.ParametersClustering;
import TCTParameters.SupervisedOneClass.ParametersPrototypeBasedClustering;

/**
 *
 * @author rafael
 */
public class SupervisedInductiveConfiguraton_OneClass_Clustering extends SupervisedInductiveConfiguraton_OneClass{
    
    private boolean kMeans;
    private boolean singleLink;
    
    private ParametersPrototypeBasedClustering parametersClusteringKME;
    private ParametersClustering parametersClusteringSingleLink;
    
    
    public SupervisedInductiveConfiguraton_OneClass_Clustering(){
        setkMeans(false);
        setSingleLink(false);
        
        setParametersClusteringKME(new ParametersPrototypeBasedClustering());
        setParametersClusteringSingleLink(new ParametersClustering());
    }

    public ParametersPrototypeBasedClustering getParametersClusteringKME() {
        return parametersClusteringKME;
    }

    public void setParametersClusteringKME(ParametersPrototypeBasedClustering parametersClusteringKME) {
        this.parametersClusteringKME = parametersClusteringKME;
    }

    public ParametersClustering getParametersClusteringSingleLink() {
        return parametersClusteringSingleLink;
    }

    public void setParametersClusteringSingleLink(ParametersClustering parametersClusteringSingleLink) {
        this.parametersClusteringSingleLink = parametersClusteringSingleLink;
    }

    
    
    public boolean isSingleLink() {
        return singleLink;
    }

    public void setSingleLink(boolean singleLink) {
        this.singleLink = singleLink;
    }

    public boolean iskMeans() {
        return kMeans;
    }

    public void setkMeans(boolean kMeans) {
        this.kMeans = kMeans;
    }
    
    
}
