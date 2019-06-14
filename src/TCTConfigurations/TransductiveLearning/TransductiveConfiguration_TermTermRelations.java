//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.Parameters_TermNetwork;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ragero
 */
public class TransductiveConfiguration_TermTermRelations extends TransductiveConfiguration_Base implements Serializable{
    private String arffEntrada;
    private String arqProb;
    private String dirSaida;
    
    private boolean LLGC;
    private boolean GFHF;
    
    private Parameters_LLGC parametersLLGC = new Parameters_LLGC();
    private Parameters_GFHF parametersGFHF = new Parameters_GFHF();
    
    private boolean SupportNetwork = false;
    private boolean MutualInformationNetwork = false;
    private boolean KappaNetwork = false;
    private boolean ShapiroNetwork = false;
    private boolean YulesQNetwork = false;
    
    private int maxNumberInterations; // Maximum number of iterations
    
    private Parameters_TermNetwork parametersSupportNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersMutualInformationNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersKappaNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersShapiroNetwork = new Parameters_TermNetwork();
    private Parameters_TermNetwork parametersYulesQNetwork = new Parameters_TermNetwork();
    
    public TransductiveConfiguration_TermTermRelations(){
        super();
        setPorcentage(true);
        setArff("");
        setArqProb("");
        setDirSaida("");
        setMaxNumIterations(1000);
    }
    
    //Getters & Setters
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
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
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
    
    public boolean isLLGC(){
        return LLGC;
    }
    
    public boolean isGFHF(){
        return GFHF;
    }
    
    public int getMaxNumberIterations(){
        return maxNumberInterations;
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
    
    public Parameters_LLGC getParametersLLGC(){
        return parametersLLGC;
    }
    
    public Parameters_GFHF getParametersGFHF(){
        return parametersGFHF;
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
    
    public void setLLGC(boolean LLGC){
        this.LLGC = LLGC;
    }
    
    public void setGFHF(boolean GFHF){
        this.GFHF = GFHF;
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
