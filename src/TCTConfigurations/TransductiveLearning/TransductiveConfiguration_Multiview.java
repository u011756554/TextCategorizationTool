//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTConfigurations.TransductiveLearning;

import java.io.Serializable;
import java.util.ArrayList;

public class TransductiveConfiguration_Multiview extends TransductiveConfiguration_CoTraining implements Serializable{
    
    private ArrayList<Integer> numberViews = new ArrayList<Integer>();
    
    public TransductiveConfiguration_Multiview(){
        super();
        addnumberViews(3);
        addnumberViews(5);
        addnumberViews(10);
        //addnumberViews(20);
    }
    
    public void addnumberViews(int num){
        numberViews.add(num);
    }
    
    public Integer getNumberView(int pos){
        return numberViews.get(pos);
    }
    
    public ArrayList<Integer> getNumberViews(){
        return numberViews;
    }
    
    public void setnumberViews(ArrayList<Integer> numberViews){
        this.numberViews = numberViews;
    }
}
