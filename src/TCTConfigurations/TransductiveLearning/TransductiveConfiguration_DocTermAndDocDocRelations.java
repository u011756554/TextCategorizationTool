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
import TCTParameters.Parameters_DocumentNetwork_Exp;
import TCTParameters.Parameters_DocumentNetwork_Knn;
import java.io.Serializable;
import java.util.ArrayList;

public class TransductiveConfiguration_DocTermAndDocDocRelations extends TransductiveConfiguration_Base implements Serializable {
    
    private String arffEntrada;
    private String arqDist;
    private String dirSaida;
    
    private boolean IMHN;
    private boolean LPHN;
    private boolean GNetMine;
    
    private Parameters_IMHN parametersIMHN;
    private Parameters_LPHN parametersLPHN;
    private Parameters_GNetMine_DocTerm_TermTerm parametersGNetMine;
    
    private boolean networkKnn = false;
    private boolean networkExp = false;
    
    private boolean cosine = false;
    
    private Parameters_DocumentNetwork_Knn parametersNetworkKnn;
    private Parameters_DocumentNetwork_Exp parametersNetworkExp;
        
    public TransductiveConfiguration_DocTermAndDocDocRelations(){
        super();
        
        setArff("");
        setArqDist("");
        setDirSaida("");
        
        setIMHN(false);
        setLPHN(false);
        setGNetMine(false);
        
        
        setParameters_IMHN(new Parameters_IMHN());
        setParameters_LPHN(new Parameters_LPHN());
        setParameters_GNetMine(new Parameters_GNetMine_DocTerm_TermTerm());
        
        setParametersNetworkKnn(new Parameters_DocumentNetwork_Knn());
        setParametersNetworkExp(new Parameters_DocumentNetwork_Exp());
    }
    
    //Getters & Setters
    public boolean getCosine(){
        return cosine;
    }
    
    public Parameters_IMHN getParameters_IMHN(){
        return parametersIMHN;
    }
    
    public Parameters_LPHN getParameters_LPHN(){
        return parametersLPHN;
    }
    
    public Parameters_GNetMine_DocTerm_TermTerm getParameters_GNetMine(){
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
    
    public void setArff(String arffEntrada) {
        this.arffEntrada = arffEntrada;
    }
    
    public void setArqDist(String arqDist){
        this.arqDist = arqDist;
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
    
}
