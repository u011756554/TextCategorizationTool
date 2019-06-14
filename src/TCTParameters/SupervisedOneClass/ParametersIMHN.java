/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTParameters.SupervisedOneClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class ParametersIMHN extends ParametersOneClass implements Serializable{
    
    private ArrayList<Double> errorCorrectionRates = new ArrayList<Double>();
    private Integer numMaxEpocasLocal;
    private Integer numMaxEpocasGlobal;
    private Integer numMaxEpocas;
    private ArrayList<Double> errors = new ArrayList<Double>();
    
    public ParametersIMHN(){
        super();    
        errorCorrectionRates = new ArrayList<Double>();
        errors = new ArrayList<Double>();
        
        addErrorCorrectionRates(0.01);
        addErrorCorrectionRates(0.05);
        addErrorCorrectionRates(0.1);
        addErrorCorrectionRates(0.5);
        
        setNumMaxEpocasLocal(100);
        setNumMaxEpocasGlobal(10);
        setMaxNumIterations(1000);
        
        addError(0.001);
        addError(0.005);
        addError(0.01);
        addError(0.05);
        
        this.addThreshold(0.05);
        this.addThreshold(0.1);
        this.addThreshold(0.15);
        this.addThreshold(0.2);
        this.addThreshold(0.25);
        this.addThreshold(0.3);
        this.addThreshold(0.35);
        this.addThreshold(0.4);
        this.addThreshold(0.45);
        this.addThreshold(0.5);
        this.addThreshold(0.55);
        this.addThreshold(0.6);
        this.addThreshold(0.65);
        this.addThreshold(0.7);
        this.addThreshold(0.75);
        this.addThreshold(0.8);
        this.addThreshold(0.85);
        this.addThreshold(0.9);
        this.addThreshold(0.95);
        
    }
    
    public ParametersIMHN(ArrayList<Double> tas, Integer numMaxEpocas, Integer numMaxEpocasLocal, Integer numMaxEpocasGlobal, ArrayList<Double> errors){
        super();
        setErrorCorrectionRates(tas);
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
        return errorCorrectionRates;
    }
    
    public Double getErrorCorrectionRate(int pos){
        return errorCorrectionRates.get(pos);
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

    public void setErrorCorrectionRates(ArrayList<Double> taxaserrorCorrectionRate) {
        this.errorCorrectionRates = taxaserrorCorrectionRate;
    }
    
   
    public void addErrorCorrectionRates(double taxa){
        this.errorCorrectionRates.add(taxa);
    }
    
    public void addError(double error){
        this.errors.add(error);
    }
    
    
}
