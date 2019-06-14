//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_MLP implements Serializable{
    private ArrayList<Double> taxaserrorCorrectionRate; //Taxas de Apŕendizado
    private ArrayList<Double> constantesMomentum; //Constante momeuntum
    private ArrayList<Integer> numerosNeuronios; //Número de neurônios
    
    private Integer numMaxEpocas;
    
    public Parameters_MLP(){
        
        taxaserrorCorrectionRate = new ArrayList<Double>();
        constantesMomentum = new ArrayList<Double>();
        numerosNeuronios = new ArrayList<Integer>();
        
        addTaxaerrorCorrectionRate(0.15);
        addTaxaerrorCorrectionRate(0.15);
        addTaxaerrorCorrectionRate(0.30);
        addTaxaerrorCorrectionRate(0.45);
        addTaxaerrorCorrectionRate(0.60);
        
        addConstanteMomentum(0.0);
        addConstanteMomentum(0.30);
        addConstanteMomentum(0.80);
        
        addNumeroNeuronios(8);
        addNumeroNeuronios(32);
        addNumeroNeuronios(64);
        
        setMaxNumIterations(10000);
    }
    
    public Parameters_MLP(ArrayList<Double> tas, ArrayList<Double> tms, ArrayList<Integer> nns, Integer numMaxEpocas){
        setTaxaserrorCorrectionRate(taxaserrorCorrectionRate);
        setConstantesMomentum(constantesMomentum);
        setNumerosNeuronios(numerosNeuronios);
        setMaxNumIterations(numMaxEpocas);
    }

    public ArrayList<Double> getConstantesMomentum() {
        return constantesMomentum;
    }
    
    public double getConstanteMomentum(int pos){
        return constantesMomentum.get(pos);
    }

    public Integer getMaxNumberIterations() {
        return numMaxEpocas;
    }

    public ArrayList<Integer> getNumerosNeuronios() {
        return numerosNeuronios;
    }
    
    public Integer getNumeroNeuronios(int pos){
        return numerosNeuronios.get(pos);
    }

    public ArrayList<Double> getErrorCorrectionRates() {
        return taxaserrorCorrectionRate;
    }
    
    public double getErrorCorrectionRate(int pos){
        return taxaserrorCorrectionRate.get(pos);
    }
    
    public void setConstantesMomentum(ArrayList<Double> constantesMomentum) {
        this.constantesMomentum = constantesMomentum;
    }

    public void setMaxNumIterations(Integer numMaxEpocas) {
        this.numMaxEpocas = numMaxEpocas;
    }

    public void setNumerosNeuronios(ArrayList<Integer> numerosNeuronios) {
        this.numerosNeuronios = numerosNeuronios;
    }

    public void setTaxaserrorCorrectionRate(ArrayList<Double> taxaserrorCorrectionRate) {
        this.taxaserrorCorrectionRate = taxaserrorCorrectionRate;
    }

    public void addTaxaerrorCorrectionRate(double taxa){
        this.taxaserrorCorrectionRate.add(taxa);
    }
    
    public void addConstanteMomentum(double constante){
        this.constantesMomentum.add(constante);
    }
    
    public void addNumeroNeuronios(int numNeuronios){
        this.numerosNeuronios.add(numNeuronios);
    }
}
