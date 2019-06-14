//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SemiSupervisedLearning;

import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.Parameters_DocumentNetwork_Knn;
import TCTParameters.SemiSupervisedLearning.Parameters_EM;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.Parameters_GNetMine_DocTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.SemiSupervisedLearning.Parameters_NMFE_SSCC;
import TCTParameters.SemiSupervisedLearning.Parameters_TSVM;
import TCTParameters.SemiSupervisedLearning.Parameters_TagBased;
import java.io.Serializable;

public class SemiSupervisedInductiveConfiguration extends SemiSupervisedInductiveConfigurationBase implements Serializable{
    
    private String dirEntrada;
    private String dirSaida;
    
    private boolean ExpectationMaximization;
    private boolean GNetMine;
    private boolean LPHN;
    private boolean TCHN;
    private boolean TSVM;
    private boolean TB;
    private boolean LLGC;
    private boolean GFHF;
    private boolean NMFE;
    
    private Parameters_EM parametersEM;
    private Parameters_IMHN parametersTCHN;
    private Parameters_TSVM parametersTSVM;
    private Parameters_GNetMine_DocTerm parametersGNetMine;
    private Parameters_LPHN parametersLPHN;
    private Parameters_TagBased parametersTB;
    private Parameters_LLGC parametersLLGC;
    private Parameters_GFHF parametersGFHF;
    private Parameters_NMFE_SSCC parametersNMFE_SSCC;
    
    private Parameters_DocumentNetwork_Knn parametersNetworkKnn = new Parameters_DocumentNetwork_Knn();
    private Parameters_DocumentNetwork_Exp parametersNetworkExp = new Parameters_DocumentNetwork_Exp();
    
    private boolean networkKnn;
    private boolean networkExp;
    
    public SemiSupervisedInductiveConfiguration(){
        setDirEntrada("");
        setDirSaida("");
        setExpectationMaximization(false);
        setGNetMine(false);
        setLPHN(false);
        setTCHN(false);
        setTSVM(false);
        setLLGC(false);
        setGFHF(false);
        setNMFE(false);
        setParametersEM(new Parameters_EM());
        setParametersTCHN(new Parameters_IMHN());
        setParametersTSVM(new Parameters_TSVM());
        setParametersGNetMine(new Parameters_GNetMine_DocTerm());
        setParametersTB(new Parameters_TagBased());
        setParametersLPHN(new Parameters_LPHN());
        setParametersLLGC(new Parameters_LLGC());
        setParametersGFHF(new Parameters_GFHF());
        setParametersNMFE(new Parameters_NMFE_SSCC());
        
        setParametersNetworkKnn(new Parameters_DocumentNetwork_Knn());
        setParametersNetworkExp(new Parameters_DocumentNetwork_Exp());
    }
    
    public String getDirEntrada(){
        return this.dirEntrada;
    }
    
    public String getDirSaida(){
        return this.dirSaida;
    }
    
    public boolean isNMFE(){
        return this.NMFE;
    }
    
    public boolean isTB(){
        return this.TB;
    }
    
    public boolean isExpectationMaximization(){
        return this.ExpectationMaximization;
    }
    
    public boolean isGNetMine(){
        return this.GNetMine;
    }
    
    public boolean isLPHN(){
        return this.LPHN;
    }
    
    public boolean isTCHN(){
        return this.TCHN;
    }
    
    public boolean isTSVM(){
        return this.TSVM;
    }
    
    public boolean isLLGC(){
        return this.LLGC;
    }
    
    public boolean isGFHF(){
        return this.GFHF;
    }
    
    public boolean isNetworkExp(){
        return networkExp;
    }
    
    public boolean isNetworkKnn(){
        return networkKnn;
    }
    
    public void isNetworkExp(boolean networkExp){
        this.networkExp = networkExp;
    }
    
    public void isNetworkKnn(boolean networkKnn){
        this.networkKnn = networkKnn;
    }
    
    public void setParametersNetworkKnn(Parameters_DocumentNetwork_Knn parametersNetworkKnn){
        this.parametersNetworkKnn = parametersNetworkKnn;
    }
    
    public void setParametersNetworkExp(Parameters_DocumentNetwork_Exp parametersNetworkExp){
        this.parametersNetworkExp = parametersNetworkExp; 
    }
   
    public void setParametersNMFE(Parameters_NMFE_SSCC parametersNMFE){
        this.parametersNMFE_SSCC = parametersNMFE;
    }
    
    public void setParametersLLGC(Parameters_LLGC parametersLLGC){
        this.parametersLLGC = parametersLLGC;
    }
    
    public void setParametersGFHF(Parameters_GFHF parametersGFHF){
        this.parametersGFHF = parametersGFHF;
    }
    
    public void setParametersTB(Parameters_TagBased parametersTB){
        this.parametersTB = parametersTB;
    }
    
    public void setParametersLPHN(Parameters_LPHN parametersLPHN){
        this.parametersLPHN = parametersLPHN;
    }
    
    public void setParametersEM(Parameters_EM parametersEM){
        this.parametersEM = parametersEM;
    }
    
    public void setParametersTCHN(Parameters_IMHN parametersTCHN){
        this.parametersTCHN = parametersTCHN;
    }
    
    public void setParametersTSVM(Parameters_TSVM parametersTSVM){
        this.parametersTSVM = parametersTSVM;
    }
    
    public void setParametersGNetMine(Parameters_GNetMine_DocTerm parametersGNetMine){
        this.parametersGNetMine = parametersGNetMine;
    }
    
    public void setExpectationMaximization(boolean ExpectationMaximization){
        this.ExpectationMaximization = ExpectationMaximization;
    }
    
    public void setKnnNetwork(boolean networkKnn){
        this.networkKnn = networkKnn;
    }
    
    public void setExpNetwork(boolean networkExp){
        this.networkExp = networkExp;
    }
    
    public void setNMFE(boolean NMFE){
        this.NMFE = NMFE;
    }
    
    public void setLLGC(boolean LLGC){
        this.LLGC = LLGC;
    }
    
    public void setGFHF(boolean GFHF){
        this.GFHF = GFHF;
    }
    
    public void setTB(boolean TB){
        this.TB = TB;
    }
    
    public void setGNetMine(boolean GNetMine){
        this.GNetMine = GNetMine;
    }
    
    public void setLPHN(boolean LPHN){
        this.LPHN = LPHN;
    }
    
    public void setTCHN(boolean TCHN){
        this.TCHN = TCHN;
    }
    
    public void setTSVM(boolean TSVM){
        this.TSVM = TSVM;
    }
    
    public void setDirEntrada(String dirEntrada){
        this.dirEntrada = dirEntrada;
    }
    
    public void setDirSaida(String dirSaida){
        this.dirSaida = dirSaida;
    }
    
    public Parameters_NMFE_SSCC getParametersNMFE(){
        return this.parametersNMFE_SSCC;
    }
    
    public Parameters_DocumentNetwork_Exp getParametersNetworkExp(){
        return this.parametersNetworkExp;
    }
    
    public Parameters_DocumentNetwork_Knn getParametersNetworkKnn(){
        return this.parametersNetworkKnn;
    }
    
    public Parameters_LLGC getParametersLLGC(){
        return this.parametersLLGC;
    }
    
    public Parameters_GFHF getParametersGFHF(){
        return this.parametersGFHF;
    }
    
    public Parameters_TagBased getParametersTB(){
        return this.parametersTB;
    }
    
    public Parameters_LPHN getParametersLPHN(){
        return this.parametersLPHN;
    }
    
    public Parameters_EM getParametersEM(){
        return this.parametersEM;
    }
    
    public Parameters_IMHN getParametersTCHN(){
        return this.parametersTCHN;
    }
    
    public Parameters_TSVM getParametersTSVM(){
        return this.parametersTSVM;
    }
    
    public Parameters_GNetMine_DocTerm getParametersGNetMine(){
        return this.parametersGNetMine;
    }
}
