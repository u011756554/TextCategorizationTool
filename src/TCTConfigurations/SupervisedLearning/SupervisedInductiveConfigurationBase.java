//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SupervisedLearning;

import TCTConfigurations.ConfigurationBase;
import TCTParameters.SupervisedLearning.Parameters_RidgeRegression;
import TCTParameters.SemiSupervisedLearning.Parameters_KNN;
import TCTParameters.SupervisedLearning.Parameters_MLP;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.SupervisedLearning.Parameters_J48;
import TCTParameters.SemiSupervisedLearning.Parameters_KAG;
import TCTParameters.SupervisedLearning.Parameters_LogisticRegression;
import TCTParameters.SupervisedLearning.Parameters_RIPPER;
import TCTParameters.SemiSupervisedLearning.Parameters_Rocchio;
import TCTParameters.SupervisedLearning.Parameters_SMO;
import TCTParameters.SemiSupervisedLearning.Parameters_TGM;
import java.io.Serializable;

public class SupervisedInductiveConfigurationBase extends ConfigurationBase implements Serializable{
    
    
    private boolean NB;
    private boolean MNB;
    private boolean J48;
    private boolean SMO;
    private boolean KNN;
    private boolean MLP;
    private boolean IMBHN;
    private boolean IMBHN2;
    private boolean TGM;
    private boolean Rocchio;
    private boolean RIPPER;
    private boolean BLR;
    private boolean KAG;
    private boolean LLSF;
    private boolean IRR;
           
    private Parameters_KNN parametersKNN;
    private Parameters_MLP parametersMLP;
    private Parameters_SMO parametersSMO;
    private Parameters_IMHN parametersIMBHN;
    private Parameters_IMHN parametersIMBHN2;
    private Parameters_TGM parametersTGM;
    private Parameters_Rocchio parametersRocchio;
    private Parameters_RIPPER parametersRIPPER;
    private Parameters_LogisticRegression parametersBLR;
    private Parameters_J48 parametersJ48;
    private Parameters_KAG parametersKAG;
    private Parameters_RidgeRegression parametersRidge;
    
    public SupervisedInductiveConfigurationBase(){
       setNB(false);
        setMNB(false);
        setJ48(false);
        setSMO(false);
        setKNN(false);
        setMLP(false);
        setIMBHN(false);
        setIMBHN2(false);
        setRocchio(false);
        setRIPPER(false);
        setBLR(false);
        setKAOG(false);
        setLLSF(false);
        setIRR(true);
        
        setParametersKNN(new Parameters_KNN());
        setParametersMLP(new Parameters_MLP());
        setParametersSMO(new Parameters_SMO());
        setParametersIMBHN(new Parameters_IMHN());
        setParametersIMBHN2(new Parameters_IMHN());
        setParametersTGM(new Parameters_TGM());
        setParametersRocchio(new Parameters_Rocchio());
        setParametersRIPPER(new Parameters_RIPPER());
        setParametersBLR(new Parameters_LogisticRegression());
        setParametersJ48(new Parameters_J48());
        setParametersKAG(new Parameters_KAG());
        setParametersRidgeRegression(new Parameters_RidgeRegression());
        
        setNumFolds(10);
        setNumReps(1);
        setNumThreads(10);
    }
    
    //Getters & Setters
    public boolean isIRR(){
        return IRR;
    }
    
    public boolean isLLSF(){
        return LLSF;
    }
    
    public boolean isKAG(){
        return KAG;
    }
    
    public boolean isBLR(){
        return BLR;
    }
    
    public boolean isRIPPER(){
        return RIPPER;
    }
    
    public boolean isTGM(){
        return TGM;
    }
    
    public boolean isJ48() {
        return J48;
    }

    public boolean isKNN() {
        return KNN;
    }

    public boolean isMLP() {
        return MLP;
    }

    public boolean isMNB() {
        return MNB;
    }

    public boolean isNB() {
        return NB;
    }

    public boolean isIMBHN() {
        return IMBHN;
    }
    
    public boolean isIMBHN2() {
        return IMBHN2;
    }
    
    public boolean isSMO() {
        return SMO;
    }
    
    public boolean isRocchio(){
        return Rocchio;
    }

    public Parameters_RidgeRegression getParametersRidgeRegression(){
        return parametersRidge;
    }
    
    public Parameters_KAG getParametersKAG(){
        return parametersKAG;
    }
    
    public Parameters_J48 getParametersJ48(){
        return parametersJ48;
    }
    
    public Parameters_LogisticRegression getParametersBLR(){
        return parametersBLR;
    }
    
    public Parameters_RIPPER getParametersRIPPER(){
        return parametersRIPPER;
    }
    
    public Parameters_TGM getParametersTGM(){
        return parametersTGM;
    }
    
    public Parameters_KNN getParametersKNN() {
        return parametersKNN;
    }

    public Parameters_MLP getParametersMLP() {
        return parametersMLP;
    }

    public Parameters_IMHN getParametersIMBHN() {
        return parametersIMBHN;
    }
    
    public Parameters_IMHN getParametersIMBHN2() {
        return parametersIMBHN2;
    }
    
    public Parameters_SMO getParametersSMO() {
        return parametersSMO;
    }
    
    public Parameters_Rocchio getParametersRocchio(){
        return parametersRocchio;
    }
    
    public void setIRR(boolean IRR){
        this.IRR = IRR;
    }
    
    public void setLLSF(boolean LLSF){
        this.LLSF = LLSF;
    }
    
    public void setKAOG(boolean KAG){
        this.KAG = KAG;
    }
    
    public void setBLR(boolean BLR){
        this.BLR = BLR;
    }
    
    public void setRIPPER(boolean RIPPER){
        this.RIPPER = RIPPER;
    }
    
    public void setTGM(boolean TGM){
        this.TGM = TGM;
    }
    
    public void setJ48(boolean J48) {
        this.J48 = J48;
    }

    public void setKNN(boolean KNN) {
        this.KNN = KNN;
    }

    public void setMLP(boolean MLP) {
        this.MLP = MLP;
    }

    public void setMNB(boolean MNB) {
        this.MNB = MNB;
    }

    public void setNB(boolean NB) {
        this.NB = NB;
    }

    public void setIMBHN(boolean IMBHN) {
        this.IMBHN = IMBHN;
    }
    
    public void setIMBHN2(boolean IMBHN2) {
        this.IMBHN2 = IMBHN2;
    }

    public void setSMO(boolean SMO) {
        this.SMO = SMO;
    }
    
    public void setRocchio(boolean Rocchio){
        this.Rocchio = Rocchio;
    }
    
    public void setParametersRidgeRegression(Parameters_RidgeRegression parametersRidgeRegression){
        this.parametersRidge = parametersRidgeRegression;
    }
    
    public void setParametersKAG(Parameters_KAG parametersKAG){
        this.parametersKAG = parametersKAG;
    }
    
    public void setParametersJ48(Parameters_J48 parametersJ48){
        this.parametersJ48 = parametersJ48;
    }
    
    public void setParametersBLR(Parameters_LogisticRegression parametersBLR){
        this.parametersBLR = parametersBLR;
    }
    
    public void setParametersRIPPER(Parameters_RIPPER parametersRIPPER){
        this.parametersRIPPER = parametersRIPPER;
    }
    
    public void setParametersTGM(Parameters_TGM parametersTGM){
        this.parametersTGM = parametersTGM;
    }
    
    public void setParametersKNN(Parameters_KNN parametersKNN) {
        this.parametersKNN = parametersKNN;
    }

    public void setParametersMLP(Parameters_MLP parametersMLP) {
        this.parametersMLP = parametersMLP;
    }

    public void setParametersIMBHN(Parameters_IMHN parametersIMBHN) {
        this.parametersIMBHN = parametersIMBHN;
    }
    
    public void setParametersIMBHN2(Parameters_IMHN parametersIMBHN2) {
        this.parametersIMBHN2 = parametersIMBHN2;
    }

    public void setParametersSMO(Parameters_SMO parametersSMO) {
        this.parametersSMO = parametersSMO;
    }
    
    public void setParametersRocchio(Parameters_Rocchio parametersRocchio){
        this.parametersRocchio = parametersRocchio;
    }
    
}
