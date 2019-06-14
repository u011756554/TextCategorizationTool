//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.Parameters_GNetMine_DocTerm_TermTerm;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SemiSupervisedLearning.Parameters_LPHN;
import TCTParameters.Parameters_TermNetwork;
import java.io.Serializable;
import java.util.ArrayList;

public class TransductiveConfiguration_DocTermAndTermTermRelations extends TransductiveConfiguration_Base implements Serializable {
    
    private String arffEntrada;
    private String arqProb;
    private String dirSaida;
    
    private boolean IMHN;
    private boolean LPHN;
    private boolean GNetMine;
    
    private Parameters_IMHN parametersIMHN;
    private Parameters_LPHN parametersLPHN;
    private Parameters_GNetMine_DocTerm_TermTerm parametersGNetMine;
    
    private boolean SupportNetwork = false;
    private boolean MutualInformationNetwork = false;
    private boolean KappaNetwork = false;
    private boolean ShapiroNetwork = false;
    private boolean YulesQNetwork = false;
    
    private Parameters_TermNetwork parametersSupportNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersMutualInformationNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersKappaNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersShapiroNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersYulesQNetwork = new Parameters_TermNetwork();
    
    public TransductiveConfiguration_DocTermAndTermTermRelations(){
        super();
        setArff("");
        setArqProb("");
        setDirSaida("");
        
        setIMHN(false);
        setLPHN(false);
        setGNetMine(false);
        
        setParameters_IMHN(new Parameters_IMHN());
        setParameters_LPHN(new Parameters_LPHN());
        setParameters_GNetMine(new Parameters_GNetMine_DocTerm_TermTerm());
        
    }
    
    //Getters & Setters
    public Parameters_IMHN getParameters_IMHN(){
        return parametersIMHN;
    }
    
    public Parameters_LPHN getParameters_LPHN(){
        return parametersLPHN;
    }
    
    public Parameters_GNetMine_DocTerm_TermTerm getParameters_GNetMine(){
        return parametersGNetMine;
    }
    
    public void setParameters_IMHN(Parameters_IMHN parametersIMHN){
        this.parametersIMHN = parametersIMHN;
    }
    
    public void setParameters_LPHN(Parameters_LPHN parametersLPHN){
        this.parametersLPHN = parametersLPHN;
    }
    
    public void setParameters_GNetMine(Parameters_GNetMine_DocTerm_TermTerm parametersGNetMine){
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
    
    public String getArff() {
        return arffEntrada;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    public String getArqProb(){
        return arqProb;
    }
    
    public void setArff(String arffEntrada) {
        this.arffEntrada = arffEntrada;
    }
    
    public void setArqProb(String arqProb){
        this.arqProb = arqProb;
    }

    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
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
