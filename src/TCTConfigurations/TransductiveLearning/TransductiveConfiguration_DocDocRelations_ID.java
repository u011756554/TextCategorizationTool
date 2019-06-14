//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.Parameters_DocumentNetwork_Knn;
import java.io.Serializable;

public class TransductiveConfiguration_DocDocRelations_ID extends TransductiveConfiguration_Base implements Serializable {
    
    private String matrizDados;
    private String matrizProx;
    private String dirResultados;
    
    private boolean LLGC;
    private boolean GFHF;
    private boolean TCHN;
    
    private Parameters_LLGC parametersLLGC = new Parameters_LLGC();
    private Parameters_GFHF parametersGFHF = new Parameters_GFHF();
    private Parameters_IMHN parametersTCHN = new Parameters_IMHN();
    
    private Parameters_DocumentNetwork_Knn parametersNetworkKnn = new Parameters_DocumentNetwork_Knn();
    private Parameters_DocumentNetwork_Exp parametersNetworkExp = new Parameters_DocumentNetwork_Exp();
    
    private boolean networkKnn;
    private boolean networkExp;
    
    private boolean adjList;
    
    public TransductiveConfiguration_DocDocRelations_ID(){
        super();
        
        setPorcentage(false);
        setMatrizDados("");
        setMatrizProx("");
        setDirResultados("");
        
        setAdjList(true);
    }
    
    //Getters & Setters
    public void setAdjList(boolean adjList){
        this.adjList = adjList;
    }
    
    public void setParametersNetworkKnn(Parameters_DocumentNetwork_Knn parametersNetworkKnn){
        this.parametersNetworkKnn = parametersNetworkKnn;
    }
    
    public Parameters_DocumentNetwork_Knn getParametersKnnNetwork(){
        return this.parametersNetworkKnn;
    }
    
    public void setParametersNetworkExp(Parameters_DocumentNetwork_Exp parametersNetworkExp){
        this.parametersNetworkExp = parametersNetworkExp;
    }
    
    public Parameters_DocumentNetwork_Exp getParametersExpNetwork(){
        return this.parametersNetworkExp;
    }
    
    public boolean isNetworkExp(){
        return networkExp;
    }
    
    public boolean isNetworkKnn(){
        return networkKnn;
    }
    
    public void setNetworkExp(boolean networkExp){
        this.networkExp = networkExp;
    }
    
    public void setNetworkKnn(boolean networkKnn){
        this.networkKnn = networkKnn;
    }
   
    public boolean isLLGC(){
        return LLGC;
    }
    
    public boolean isGFHF(){
        return GFHF;
    }
    
    public boolean isTCHN(){
        return TCHN;
    }
    
    public String getMatrizDados() {
        return matrizDados;
    }

    public String getDirResultados() {
        return dirResultados;
    }
    
    public String getMatrizProx(){
        return matrizProx;
    }
    
    public boolean getAdjList(){
        return this.adjList;
    }
    
    public Parameters_LLGC getParametersLLGC(){
        return parametersLLGC;
    }
    
    public Parameters_GFHF getParametersGFHF(){
        return parametersGFHF;
    }
    
    public Parameters_IMHN getParametersTCHN(){
        return parametersTCHN;
    }
    
    public void setLLGC(boolean LLGC){
        this.LLGC = LLGC;
    }
    
    public void setGFHF(boolean GFHF){
        this.GFHF = GFHF;
    }
    
    public void setTCHN(boolean TCHN){
        this.TCHN = TCHN;
    }
    
    public void setMatrizDados(String matrizDados) {
        this.matrizDados = matrizDados;
    }
    
    public void setMatrizProx(String matrizProx){
        this.matrizProx = matrizProx;
    }

    public void setDirResultados(String dirResultados) {
        this.dirResultados = dirResultados;
    }
    
}
