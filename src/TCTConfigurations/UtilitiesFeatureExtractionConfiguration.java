/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations;

import TCTParameters.Parameters_LDA;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
public class UtilitiesFeatureExtractionConfiguration {
    
    //Directories
    private String dirArffs;
    private String dirOutput;
    
    //Algorithms
    private boolean LDA;
    
    //Options
    private boolean considerLabels;
    private boolean disconsiderLabels;
    private ArrayList<Integer> numTopics;

    //Parameters
    private Parameters_LDA parametersLDA;

    public UtilitiesFeatureExtractionConfiguration(){
        
        setLDA(false);
        setParametersLDA(new Parameters_LDA());
        setDirArffs("");
        setDirOutput("");
        setConsiderLabels(true);
        setConsiderLabels(false);
        setNumTopics(new ArrayList<Integer>());
        addNumTopics(50);
        addNumTopics(100);
        addNumTopics(150);
        addNumTopics(200);
        
    }
    
    public ArrayList<Integer> getNumTopics() {
        return numTopics;
    }

    public void setNumTopics(ArrayList<Integer> numTopics) {
        this.numTopics = numTopics;
    }
    
    public void addNumTopics(int numTopics){
        this.numTopics.add(numTopics);
    }
    
    public boolean isConsiderLabels() {
        return considerLabels;
    }

    public void setConsiderLabels(boolean considerLabels) {
        this.considerLabels = considerLabels;
    }

    public boolean isDisconsiderLabels() {
        return disconsiderLabels;
    }

    public void setDisconsiderLabels(boolean disconsiderLabels) {
        this.disconsiderLabels = disconsiderLabels;
    }
    
    public boolean isLDA() {
        return LDA;
    }

    public void setLDA(boolean LDA) {
        this.LDA = LDA;
    }

    public Parameters_LDA getParametersLDA() {
        return parametersLDA;
    }

    public void setParametersLDA(Parameters_LDA parametersLDA) {
        this.parametersLDA = parametersLDA;
    }
    
    public String getDirArffs() {
        return dirArffs;
    }

    public void setDirArffs(String dirArffs) {
        this.dirArffs = dirArffs;
    }

    public String getDirOutput() {
        return dirOutput;
    }

    public void setDirOutput(String dirOutput) {
        this.dirOutput = dirOutput;
    }
}
