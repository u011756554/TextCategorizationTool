//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.Parameters_GNetMine_DocTerm_TermTerm;
import TCTParameters.Parameters_GNetMine_DocDoc_DocTerm_TermTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.Parameters_TermNetwork;
import TCTParameters.Parameters_DocumentNetwork_Knn;
import java.io.Serializable;

public class TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations extends TransductiveConfiguration_Base implements Serializable{
    private String arffEntrada;
    private String arqDist;
    private String arqProb;
    private String dirSaida;
    
    private boolean IMHN;
    private boolean LPHN;
    private boolean GNetMine;
    
    private Parameters_IMHN parametersIMHN;
    private Parameters_LPHN parametersLPHN;
    private Parameters_GNetMine_DocDoc_DocTerm_TermTerm parametersGNetMine;

    private boolean SupportNetwork = false;
    private boolean MutualInformationNetwork = false;
    private boolean KappaNetwork = false;
    private boolean ShapiroNetwork = false;
    private boolean YulesQNetwork = false;
    
    private boolean networkKnn = false;
    private boolean networkExp = false;
    
    private boolean cosine = false;
    
    private Parameters_DocumentNetwork_Knn parametersNetworkKnn;
    private Parameters_DocumentNetwork_Exp parametersNetworkExp;
    
    private Parameters_TermNetwork parametersSupportNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersMutualInformationNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersKappaNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersShapiroNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersYulesQNetwork = new Parameters_TermNetwork();
    
    public TransductiveConfiguration_DocDocAndDocTermAndTermTermRelations(){
        super();
        
        setArff("");
        setArqDist("");
        setArqProb("");
        setDirSaida("");
        
        setIMHN(false);
        setLPHN(false);
        setGNetMine(false);
        
        setParameters_IMHN(new Parameters_IMHN());
        setParameters_LPHN(new Parameters_LPHN());
        setParameters_GNetMine(new Parameters_GNetMine_DocDoc_DocTerm_TermTerm());
        
        setParametersNetworkKnn(new Parameters_DocumentNetwork_Knn());
        setParametersNetworkExp(new Parameters_DocumentNetwork_Exp());
        
        setParametersSupportNetwork(new Parameters_TermNetwork());
        setParametersMutualInformationNetwork(new Parameters_TermNetwork());
        setParametersKappaNetwork(new Parameters_TermNetwork());
        setParametersShapiroNetwork(new Parameters_TermNetwork());
        setParametersYulesQNetwork(new Parameters_TermNetwork());
        
    }
    
    public boolean getCosine(){
        return cosine;
    }
    
    public Parameters_IMHN getParameters_IMHN(){
        return parametersIMHN;
    }
    
    public Parameters_LPHN getParameters_LPHN(){
        return parametersLPHN;
    }
    
    public Parameters_GNetMine_DocDoc_DocTerm_TermTerm getParameters_GNetMine(){
        return parametersGNetMine;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    public void setParameters_IMHN(Parameters_IMHN parametersIMHN){
        this.parametersIMHN = parametersIMHN;
    }
    
    public void setParameters_LPHN(Parameters_LPHN parametersLPHN){
        this.parametersLPHN = parametersLPHN;
    }
    
    public void setParameters_GNetMine(Parameters_GNetMine_DocDoc_DocTerm_TermTerm parametersGNetMine){
        this.parametersGNetMine = parametersGNetMine;
    }
    
    public boolean isIMHN(){
        return IMHN;
    }
    
    public boolean isLPHN(){
        return LPHN;
    }
    
    public boolean isGNetMine(){
        return GNetMine;
    }

    public void setIMHN(boolean IMBHN){
        this.IMHN = IMBHN;
    }
    
    public void setLPHN(boolean LPHN){
        this.LPHN = LPHN;
    }
    
    public void setGNetMine(boolean GNetMine){
        this.GNetMine = GNetMine;
    }
    
    public void setNetworkKnn(boolean networkKnn){
        this.networkKnn = networkKnn;
    }
    
    public void setNetworkExp(boolean networkExp){
        this.networkExp = networkExp;
    }
    
    public void setParametersNetworkKnn(Parameters_DocumentNetwork_Knn parametersNetworkKnn){
        this.parametersNetworkKnn = parametersNetworkKnn;
    }
    
    public void setParametersNetworkExp(Parameters_DocumentNetwork_Exp parametersNetworkExp){
        this.parametersNetworkExp = parametersNetworkExp;
    }
    
    public String getArff() {
        return arffEntrada;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    public String getArqDist(){
        return arqDist;
    }
    
    public String getArqProb(){
        return arqProb;
    }
    
    public void setArff(String arffEntrada) {
        this.arffEntrada = arffEntrada;
    }
    
    public void setArqDist(String arqDist){
        this.arqDist = arqDist;
    }

    public void setArqProb(String arqProb){
        this.arqProb = arqProb;
    }
    
    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
    }
    
    public boolean isNetworkKnn(){
        return networkKnn;
    }
    
    public boolean isNetworkExp(){
        return networkExp;
    }
    
    public Parameters_DocumentNetwork_Knn getParametersKnnNetwork(){
        return parametersNetworkKnn;
    }
    
    public Parameters_DocumentNetwork_Exp getParametersExpNetwork(){
        return parametersNetworkExp;
    }
    
    public void setSupportNetwork(boolean SupportNetwork){
        this.SupportNetwork = SupportNetwork;
    }
    
    public void setMutualInformationNetwork(boolean MutualInformationNetwork){
        this.MutualInformationNetwork = MutualInformationNetwork;
    }
    
    public void setKappaNetwork(boolean KappaNetwork){
        this.KappaNetwork = KappaNetwork;
    }
    
    public void setShapiroNetwork(boolean ShapiroNetwork){
        this.ShapiroNetwork = ShapiroNetwork;
    }
    
    public void setYulesQNetwork(boolean YulesQNetwork){
        this.YulesQNetwork = YulesQNetwork;
    }
    
    public void setParametersSupportNetwork(Parameters_TermNetwork parametersSupportNetwork){
        this.parametersSupportNetwork = parametersSupportNetwork;
    }
    
    public void setParametersYulesQNetwork(Parameters_TermNetwork parametersYulesQNetwork){
        this.parametersYulesQNetwork = parametersYulesQNetwork;
    }
    
    public void setParametersMutualInformationNetwork(Parameters_TermNetwork parametersMutualInformationNetwork){
        this.parametersMutualInformationNetwork = parametersMutualInformationNetwork;
    }
    
    public void setParametersKappaNetwork(Parameters_TermNetwork parametersKappaNetwork){
        this.parametersKappaNetwork = parametersKappaNetwork;
    }
    
    public void setParametersShapiroNetwork(Parameters_TermNetwork parametersShapiroNetwork){
        this.parametersShapiroNetwork = parametersShapiroNetwork;
    }
    
    public boolean isSupportNetwork(){
        return SupportNetwork;
    }
    
    public boolean isMutualInformationNetwork(){
        return MutualInformationNetwork;
    }
    
    public boolean isKappaNetwork(){
        return KappaNetwork;
    }
    
    public boolean isShapiroNetwork(){
        return ShapiroNetwork;
    }
    
    public boolean isYulesQNetwork(){
        return YulesQNetwork;
    }
    
    public Parameters_TermNetwork getParametersSupportNetwork(){
        return this.parametersSupportNetwork;
    }
    
    public Parameters_TermNetwork getParametersYulesQNetwork(){
        return this.parametersYulesQNetwork;
    }
    
    public Parameters_TermNetwork getParametersMutualInformationNetwork(){
        return this.parametersMutualInformationNetwork;
    }
    
    public Parameters_TermNetwork getParametersKappaNetwork(){
        return this.parametersKappaNetwork;
    }
    
    public Parameters_TermNetwork getParametersShapiroNetwork(){
        return this.parametersShapiroNetwork;
    }
}
