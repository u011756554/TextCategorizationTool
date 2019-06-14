/*
 * Class with algorithm parameters
 *
 * Deni Dias da Silva Junior
 * @author denidiasjr
 */
package TCTParameters.SupervisedOneClass;

import java.io.Serializable;
import java.util.ArrayList;

public class ParametersOneClass implements Serializable {
    
    private boolean manual;
    private ArrayList<Double> thresholds;

    private boolean automatic;

    public ParametersOneClass(){
        setManual(true);
        setThresholds(new ArrayList<Double>());
        
        setAutomatic(true);
    }
    
    public void addThreshold(double threshold){
        thresholds.add(threshold);
    }
    
    public ArrayList<Double> getThresholds() {
        return thresholds;
    }

    public void setThresholds(ArrayList<Double> thresholds) {
        this.thresholds = thresholds;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public double getThreshold(int id){
        return thresholds.get(id);
    }
    
    public void setAutomatic(boolean automaticThreshold) {
        this.automatic = automaticThreshold;
    }
    
    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }


}
