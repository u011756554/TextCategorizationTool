/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTParameters.SemiSupervisedLearning;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class Parameters_NMFE_SSCC implements Serializable{
    
    private ArrayList<Double> omegas;
    private ArrayList<Double> alphas;
    private ArrayList<Double> betas;

    private int numMaxIterations;
    private double minDifference;
    
    public Parameters_NMFE_SSCC(){
        setOmegas(new ArrayList<Double>());
        setAlphas(new ArrayList<Double>());
        setBetas(new ArrayList<Double>());
        
        addAlpha(1);
        addAlpha(5);
        addAlpha(10);
        
        addBeta(1);
        addBeta(5);
        addBeta(10);
        
        addOmega(0.001);
        
        setNumMaxIterations(1000);
        setMinDifference(0.001);
    }

    public void addOmega(double omega){
        omegas.add(omega);
    }
    
    public void addAlpha(double alpha){
        alphas.add(alpha);
    }
    
    public void addBeta(double beta){
        betas.add(beta);
    }
    
    public ArrayList<Double> getOmegas() {
        return omegas;
    }
    
    public double getOmega(int pos){
        return omegas.get(pos);
    }
    
    public double getAlpha(int pos){
        return alphas.get(pos);
    }
    
    public double getBeta(int pos){
        return betas.get(pos);
    }
    
    public void setMinDifference(double minDifference){
        this.minDifference = minDifference;
    }
    
    public void setOmegas(ArrayList<Double> omegas) {
        this.omegas = omegas;
    }

    public ArrayList<Double> getAlphas() {
        return alphas;
    }

    public void setAlphas(ArrayList<Double> alphas) {
        this.alphas = alphas;
    }

    public int getNumMaxIterations(){
        return numMaxIterations;
    }
    
    public double getMinDifference(){
        return minDifference;
    }
    
    public ArrayList<Double> getBetas() {
        return betas;
    }

    public void setBetas(ArrayList<Double> betas) {
        this.betas = betas;
    }
    
    public void setNumMaxIterations(int numMaxIterations){
        this.numMaxIterations = numMaxIterations;
    }
    
}
