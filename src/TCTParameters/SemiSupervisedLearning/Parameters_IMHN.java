//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_IMHN implements Serializable{
    private ArrayList<Double> taxaserrorCorrectionRate = new ArrayList<Double>();
    private Integer numMaxEpocasLocal;
    private Integer numMaxEpocasGlobal;
    private Integer numMaxEpocas;
    private ArrayList<Double> errors = new ArrayList<Double>();
    
    public Parameters_IMHN(){
        taxaserrorCorrectionRate = new ArrayList<Double>();
        errors = new ArrayList<Double>();
        
        addTaxaerrorCorrectionRate(0.01);
        addTaxaerrorCorrectionRate(0.05);
        addTaxaerrorCorrectionRate(0.1);
        addTaxaerrorCorrectionRate(0.5);
        
        setNumMaxEpocasLocal(100);
        setNumMaxEpocasGlobal(10);
        setMaxNumIterations(1000);
        
        addError(0.01);
    }
    
    public Parameters_IMHN(ArrayList<Double> tas, Integer numMaxEpocas, Integer numMaxEpocasLocal, Integer numMaxEpocasGlobal, ArrayList<Double> errors){
        setTaxaserrorCorrectionRate(tas);
        setNumMaxEpocasLocal(numMaxEpocasLocal);
        setNumMaxEpocasGlobal(numMaxEpocasGlobal);
        setMaxNumIterations(numMaxEpocas);
        setErrors(errors);
    }

    public ArrayList<Double> getErrors() {
        return errors;
    }
    
    public Double getError(int pos){
        return errors.get(pos);
    }
    
    public Integer getMaxNumberIterations(){
        return numMaxEpocas;
    }
    
    public Integer getMaxNumberIterationsLocal() {
        return numMaxEpocasLocal;
    }
    
    public Integer getMaxNumberIterationsGlobal() {
        return numMaxEpocasGlobal;
    }

    public ArrayList<Double> getErrorCorrectionRates() {
        return taxaserrorCorrectionRate;
    }
    
    public Double getErrorCorrectionRate(int pos){
        return taxaserrorCorrectionRate.get(pos);
    }

    public void setErrors(ArrayList<Double> errors) {
        this.errors = errors;
    }
    
    public void setMaxNumIterations(Integer numMaxEpocas){
        this.numMaxEpocas = numMaxEpocas;
    }
    
    public void setNumMaxEpocasLocal(Integer numMaxEpocasLocal) {
        this.numMaxEpocasLocal = numMaxEpocasLocal;
    }
    
    public void setNumMaxEpocasGlobal(Integer numMaxEpocasGlobal) {
        this.numMaxEpocasGlobal = numMaxEpocasGlobal;
    }

    public void setTaxaserrorCorrectionRate(ArrayList<Double> taxaserrorCorrectionRate) {
        this.taxaserrorCorrectionRate = taxaserrorCorrectionRate;
    }
    
   
    public void addTaxaerrorCorrectionRate(double taxa){
        this.taxaserrorCorrectionRate.add(taxa);
    }
    
    public void addError(double error){
        this.errors.add(error);
    }
    
}
