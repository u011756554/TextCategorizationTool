/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTParameters.SupervisedOneClass;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author deni
 */
public class ParametersNaiveBayes extends ParametersOneClass implements Serializable{
        
    public ParametersNaiveBayes(){
        super(); 
        
        /*this.addThreshold(0.05);
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
        this.addThreshold(0.95);*/
        
        for(int expoente=1;expoente<=50;expoente++){
            String mask = "";
            for(int m=1;m<=expoente;m++){
                mask += "0";
            } 
            DecimalFormat df = new DecimalFormat("0." + mask);
            //NumberFormat df = NumberFormat.getNumberInstance();
            
            for(int base=30;base>=1;base--){
                double value = base * (1 / Math.pow(10, expoente));
                double parametro = Double.parseDouble(df.format(value).replace(',', '.'));
                this.addThreshold(parametro);
            }
            
        }
        
    }
    
}
