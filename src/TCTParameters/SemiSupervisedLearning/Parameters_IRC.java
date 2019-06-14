//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTParameters.SemiSupervisedLearning;

import TCTConfigurations.TransductiveLearning.TransductiveConfiguration;
import TCTStructures.AlphaBeta;
import java.io.Serializable;
import java.util.ArrayList;


public class Parameters_IRC implements Serializable{
    
    ArrayList<AlphaBeta> pondTermos = new ArrayList<AlphaBeta>();
    ArrayList<AlphaBeta> pondDocs = new ArrayList<AlphaBeta>();
    
    private int maxNumberInterations; // Maximum number of iterations
    
    public Parameters_IRC(){
        addPondTermos(1,5);
        addPondTermos(2,4);
        addPondTermos(3,3);
        addPondTermos(4,2);
        addPondTermos(5,1);
        
        addPondDocs(1,5);
        addPondDocs(2,4);
        addPondDocs(3,3);
        addPondDocs(4,2);
        addPondDocs(5,1);
        
        setMaxNumIterations(1000);
    }
    
    public Parameters_IRC(ArrayList<AlphaBeta> pondTermos, ArrayList<AlphaBeta> pondDocs){
        this.pondDocs = pondDocs;
        this.pondTermos = pondTermos;
    }
    
    private void addPondTermos(double alpha, double beta){
        AlphaBeta ab = new AlphaBeta(alpha,beta);
        pondTermos.add(ab);
    }
    
    private void addPondDocs(double alpha, double beta){
        AlphaBeta ab = new AlphaBeta(alpha,beta);
        pondDocs.add(ab);
    }
    
    public ArrayList<AlphaBeta> getPondTermos(){
        return pondTermos;
    }
    
    public ArrayList<AlphaBeta> getPondDocs(){
        return pondDocs;
    }
    
    public Integer getMaxNumberIterations(){
        return this.maxNumberInterations;
    }
    
    public void setPondTermos(ArrayList<AlphaBeta> pondTermos){
        this.pondTermos = pondTermos;
    }
    
    public void setPondDocs(ArrayList<AlphaBeta> pondDocs){
        this.pondDocs = pondDocs;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
}
