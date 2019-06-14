//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import java.io.Serializable;
import java.util.ArrayList;

public class Parameters_TagBased implements Serializable{
    //ArrayList<Double> alphas = new ArrayList<Double>();
    private ArrayList<Double> betas = new ArrayList<Double>();
    private ArrayList<Double> gammas = new ArrayList<Double>();
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_TagBased(){
        //addAlpha(0.1);
        //addAlpha(1);
        //addAlpha(10);
        //addAlpha(100);
        //addAlpha(1000);
        
        addBeta(0.1);
        addBeta(1.0);
        addBeta(10.0);
        addBeta(100.0);
        addBeta(1000.0);
        
        addGamma(0.1);
        addGamma(1.0);
        addGamma(10.0);
        addGamma(100.0);
        addGamma(1000.0);
                
        setMaxNumIterations(1000);
    }
    
    public Parameters_TagBased(ArrayList<Double> betas, ArrayList<Double> gammas){
        //setAlphas(alphas);
        setBetas(betas);
        setGammas(gammas);
    }
    
    //public void addAlpha(double alpha){
    //    alphas.add(alpha);
    //}
    
    public void addBeta(double beta){
        betas.add(beta);
    }
    
    public void addGamma(double gamma){
        gammas.add(gamma);
    }
    
    //public void setAlphas(ArrayList<Double> alphas){
    //    this.alphas = alphas;
    //}
    
    public void setBetas(ArrayList<Double> betas){
        this.betas = betas;
    }

    public void setGammas(ArrayList<Double> gammas){
        this.gammas = gammas;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public Integer getMaxNumberIterations(){
        return this.maxNumberInterations;
    }
    
    //public ArrayList<Double> getAlphas(){
    //    return alphas;
    //}
    
    //public Double getAlpha(int pos){
    //    return alphas.get(pos);
    //}
    
    public Double getBeta(int pos){
        return betas.get(pos);
    }
    
    public Double getGamma(int pos){
        return gammas.get(pos);
    }
    
    public ArrayList<Double> getBetas(){
        return betas;
    }
    
    public ArrayList<Double> getGammas(){
        return gammas;
    }
}
