/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTConfigurations;

import java.io.Serializable;
import javax.activation.DataHandler;

/**
 *
 * @author rafael
 */
public class ConfigurationBase implements Serializable{
    
    private int numReps;
    private int numFolds;
    private int numThreads;
    private Email email;

    public ConfigurationBase(){
        setNumThreads(10);
        setNumFolds(10);
        setNumReps(10);
        email = new Email();
    }

    public void setEmailAdress(String eMailTo){
        email.seteMailTo(eMailTo);
    }
    
    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
    
    public int getNumReps() {
        return numReps;
    }

    public void setNumReps(int numReps) {
        this.numReps = numReps;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }
    
    @Override
    public String toString(){
        String output = "";
        output += "# Repetitions: " + this.numReps + "\n";
        output += "# Folds: " + this.numFolds + "\n";
        output += "# Threads: " + this.numThreads + "\n";
        return output;
    }
    
    
}
