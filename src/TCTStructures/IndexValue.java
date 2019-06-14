//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

public class IndexValue implements Comparable<IndexValue>{
    public int index;
    public double value;
    
    public IndexValue(){
        index = 0;
        value = 0;
    }
    
    public IndexValue(int index, double value){
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(IndexValue other) {
        if(this.value > other.value){
            return -1;
        }else if(this.value < other.value){
            return 1;
        }else{
            return 0;
        }
    }
    
    
}
