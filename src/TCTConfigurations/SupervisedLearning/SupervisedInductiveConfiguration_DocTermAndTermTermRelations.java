//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SupervisedLearning;

import TCTConfigurations.ConfigurationBase;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.Parameters_TermNetwork;
import java.io.Serializable;

public class SupervisedInductiveConfiguration_DocTermAndTermTermRelations extends ConfigurationBase implements Serializable {
    
    private String arffEntrada;
    private String arqProb;
    private String dirSaida;
    
    private boolean IMHN;
    private boolean IMHN2;
    
    private Parameters_IMHN parametersIMHN;
    private Parameters_IMHN parametersIMHN2;
    
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
    
    public SupervisedInductiveConfiguration_DocTermAndTermTermRelations(){
        setArff("");
        setArqProb("");
        setDirSaida("");
        
        setIMHN(false);
        setNumReps(1);
        setNumFolds(10);
        setNumThreads(10);
        
        setParameters_IMHN(new Parameters_IMHN());
        setParameters_IMHN2(new Parameters_IMHN());
    }
    
    public Parameters_IMHN getParameters_IMHN(){
        return parametersIMHN;
    }
    
    public Parameters_IMHN getParameters_IMHN2(){
        return parametersIMHN2;
    }
    
    public void setParameters_IMHN(Parameters_IMHN parametersIMHN){
        this.parametersIMHN = parametersIMHN;
    }
    
    public void setParameters_IMHN2(Parameters_IMHN parametersIMHN2){
        this.parametersIMHN2 = parametersIMHN2;
    }
    
    public boolean isIMHN(){
        return IMHN;
    }
    
    public boolean isIMHN2(){
        return IMHN2;
    }
    
    public void setIMHN(boolean IMBHN){
        this.IMHN = IMBHN;
    }
    
    public void setIMHN2(boolean IMBHN2){
        this.IMHN2 = IMBHN2;
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
