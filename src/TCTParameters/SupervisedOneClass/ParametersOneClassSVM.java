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
public class ParametersOneClassSVM implements Serializable{

    ArrayList<Double> nus;
    ArrayList<Double> gammas;
    
    boolean linearKernel;
    boolean polynomialKernel;
    boolean rbfKernel;
    
    public ParametersOneClassSVM(){
        
        //Initial types of kernels
        setLinearKernel(true);
        setPolynomialKernel(true);
        setRbfKernel(true);
        
        //Initial values of gamma
        setGammas(new ArrayList<Double>());
        addGamma(0.0000001);
        addGamma(0.000001);
        addGamma(0.00001);
        addGamma(0.0001);
        addGamma(0.001);
        addGamma(0.01);
        addGamma(0.1);
        addGamma(1.0);
        
        //Initial values of nu
        setNus(new ArrayList<Double>());
        addNu(0.05);
        addNu(0.1);
        addNu(0.15);
        addNu(0.2);
        addNu(0.25);
        addNu(0.3);
        addNu(0.35);
        addNu(0.4);
        addNu(0.45);
        addNu(0.5);
        addNu(0.55);
        addNu(0.6);
        addNu(0.65);
        addNu(0.7);
        addNu(0.75);
        addNu(0.8);
        addNu(0.85);
        addNu(0.9);
        addNu(0.95);
        addNu(1.0);
    }

    public double getNu(int ind){
        return nus.get(ind);
    }
    
    public double getGamma(int ind){
        return gammas.get(ind);
    }
    
    public void addNu(double nu){
        nus.add(nu);
    }
    
    public void addGamma(double gamma){
        gammas.add(gamma);
    }
    
    public ArrayList<Double> getNus() {
        return nus;
    }

    public void setNus(ArrayList<Double> nus) {
        this.nus = nus;
    }

    public ArrayList<Double> getGammas() {
        return gammas;
    }

    public void setGammas(ArrayList<Double> gammas) {
        this.gammas = gammas;
    }

    public boolean isLinearKernel() {
        return linearKernel;
    }

    public void setLinearKernel(boolean linearKernel) {
        this.linearKernel = linearKernel;
    }

    public boolean isPolynomialKernel() {
        return polynomialKernel;
    }

    public void setPolynomialKernel(boolean polynomialKernel) {
        this.polynomialKernel = polynomialKernel;
    }

    public boolean isRbfKernel() {
        return rbfKernel;
    }

    public void setRbfKernel(boolean rbfKernel) {
        this.rbfKernel = rbfKernel;
    }
    
    
    
}
