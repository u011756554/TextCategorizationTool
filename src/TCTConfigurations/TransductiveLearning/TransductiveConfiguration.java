//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.SemiSupervisedLearning.Parameters_IRC;
import TCTParameters.Parameters_GNetMine_DocTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_TagBased;
import TCTParameters.Parameters_DocumentNetwork_Knn;
import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_NMFE_SSCC;
import java.io.Serializable;

public class TransductiveConfiguration extends TransductiveConfiguration_Base implements Serializable{
    
    
    private boolean IRC;
    private boolean TabBased;
    private boolean LLGC;
    private boolean GFHF;
    private boolean GNetMine;
    private boolean LPBHN;
    private boolean TCBHN;
    private boolean NMFE;
    
    private Parameters_IRC parametersIRC = new Parameters_IRC();
    private Parameters_TagBased parametersTagBased = new Parameters_TagBased();
    private Parameters_LLGC parametersLLGC = new Parameters_LLGC();
    private Parameters_GFHF parametersGFHF = new Parameters_GFHF();
    private Parameters_LPHN parametersLPBHN = new Parameters_LPHN();
    private Parameters_GNetMine_DocTerm parametersGNetMine = new Parameters_GNetMine_DocTerm();
    private Parameters_IMHN parametersTCBHN = new Parameters_IMHN();
    private Parameters_NMFE_SSCC parametersNMFE = new Parameters_NMFE_SSCC();
    
    private Parameters_DocumentNetwork_Knn parametersNetworkKnn = new Parameters_DocumentNetwork_Knn();
    private Parameters_DocumentNetwork_Exp parametersNetworkExp = new Parameters_DocumentNetwork_Exp();
    
    private boolean networkKnn;
    private boolean networkExp;
    
    public TransductiveConfiguration(){
        super();
    }
    
    //Getters & Setters
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
    
    public boolean isNMFE(){
        return NMFE;
    }
    
    public boolean isIRC() {
        return IRC;
    }
    
    public boolean isTagBased(){
        return TabBased;
    }
    
    public boolean isLLGC(){
        return LLGC;
    }
    
    public boolean isGFHF(){
        return GFHF;
    }
    
    public boolean isLPBHN(){
        return LPBHN;
    }
    
    public boolean isGNetMine(){
        return GNetMine;
    }
    
    public boolean isTCBHN(){
        return TCBHN;
    }
    
    public Parameters_NMFE_SSCC getParametersNMFE(){
        return parametersNMFE;
    }
    
    public Parameters_IRC getParametersIRC() {
        return parametersIRC;
    }
    
    public Parameters_TagBased getParametersTagBased(){
        return parametersTagBased;
    }
    
    public Parameters_LLGC getParametersLLGC(){
        return parametersLLGC;
    }
    
    public Parameters_GFHF getParametersGFHF(){
        return parametersGFHF;
    }
    
    public Parameters_LPHN getParametersLPBHN(){
        return parametersLPBHN;
    }
    
    public Parameters_GNetMine_DocTerm getParametersGNetMine(){
        return parametersGNetMine;
    }
    
    public Parameters_IMHN getParametersTCBHN(){
        return parametersTCBHN;
    }
    
    public void setNMFE(boolean NMFE){
        this.NMFE = NMFE;
    }
    
    public void setIRC(boolean IRC) {
        this.IRC = IRC;
    }
    
    public void setTagBased(boolean TagBased){
        this.TabBased = TagBased;
    }
    
    public void setLLGC(boolean LLGC){
        this.LLGC = LLGC;
    }
    
    public void setGFHF(boolean GFHF){
        this.GFHF = GFHF;
    }
    
    public void setLPBHN(boolean LPBHN){
        this.LPBHN = LPBHN;
    }
    
    public void setGNetMine(boolean GNetMine){
        this.GNetMine = GNetMine;
    }
    
    public void setTCBHN(boolean TCBHN){
        this.TCBHN = TCBHN;
    }
    
 
}
