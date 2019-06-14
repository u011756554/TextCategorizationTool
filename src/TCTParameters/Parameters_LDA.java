/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTParameters;

import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class Parameters_LDA {

    private double alpha;
    private double beta;
    private int numIterations;
    private int numTopicWords;

    public Parameters_LDA(){
        setAlpha(1.0);
        setBeta(0.1);
        setNumIterations(10000);
        setNumTopicWords(10);
    }
    
    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public int getNumTopicWords() {
        return numTopicWords;
    }

    public void setNumTopicWords(int numTopicWords) {
        this.numTopicWords = numTopicWords;
    }
    
}
