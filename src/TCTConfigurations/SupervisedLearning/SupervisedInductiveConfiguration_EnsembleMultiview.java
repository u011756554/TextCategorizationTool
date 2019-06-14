//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class SupervisedInductiveConfiguration_EnsembleMultiview extends SupervisedInductiveConfigurationBase implements Serializable{
    
    private String arffVisao1;
    private String arffVisao2;
    private String dirSaida;
    
    private boolean validaSomaConfianca;
    private boolean validaSomaPonderadaConfianca;
    private boolean validaMaisConfiante;
    
    private ArrayList<Double> confidences = new ArrayList<Double>();
    
    public SupervisedInductiveConfiguration_EnsembleMultiview(){ 
        super();
        setArffVisao1("");
        setArffVisao2("");
        setDirSaida("");
        
        addConfianca(0.1);
        addConfianca(0.3);
        addConfianca(0.5);
        addConfianca(0.7);
        addConfianca(0.9);
        
        setNumReps(1);
    }
    
    //Getters & Setters
    public void addConfianca(double conf){
        confidences.add(conf);
    }
    
    public void setConfidence(ArrayList<Double> confidences){
        this.confidences = confidences;
    }
    
    public ArrayList<Double> getConfiancas(){
        return this.confidences;
    }
    
    public double getConfianca(int pos){
        return confidences.get(pos);
    }
    
    public boolean isSumOfConfidences(){
        return validaSomaConfianca;
    }
    
    public boolean isSumOfWeightedConfidences(){
        return validaSomaPonderadaConfianca;
    }
    
    public boolean isHighestConfidence(){
        return validaMaisConfiante;
    }
    
    public String getArffVisao1() {
        return arffVisao1;
    }
    
    public String getArffVisao2() {
        return arffVisao2;
    }

    public String getDirSaida() {
        return dirSaida;
    }
    
    public void setValidaSomaConfianca(boolean validaSomaConfianca){
        this.validaSomaConfianca = validaSomaConfianca;
    }
    
    public void setValidaSomaPonderadaConfianca(boolean validaSomaPonderadaConfianca){
        this.validaSomaPonderadaConfianca = validaSomaPonderadaConfianca;
    }
    
    public void setValidaMaisConfiante(boolean validaMaisConfiante){
        this.validaMaisConfiante = validaMaisConfiante;
    }

    public void setArffVisao1(String arffVisao1) {
        this.arffVisao1 = arffVisao1;
    }
    
    public void setArffVisao2(String arffVisao2) {
        this.arffVisao2 = arffVisao2;
    }

    public void setDirSaida(String dirSaida) {
        this.dirSaida = dirSaida;
    }
    
}
