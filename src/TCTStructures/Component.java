//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

import java.util.ArrayList;

public class Component {
    
    private ArrayList<Integer> vertices;
    private double purity;
    private int classe;
    private int k;
    private int indexComp;
    
    public Component(){
        setVertices(new ArrayList<Integer>());
        setPurity(0);
        setClass(0);
        setK(0);
        setindexComp(0);
    }
    
    public void addVertice(Integer vertice){
        vertices.add(vertice);
    }
    
    public void setK(int k){
        this.k = k;
    }
    
    public void setVertices(ArrayList<Integer> vertices){
        this.vertices = vertices;
    }
    
    public void setPurity(double purity){
        this.purity = purity;
    }
    
    public void setClass(int classe){
        this.classe = classe;
    }
    
    public void setindexComp(int indexComp){
        this.indexComp = indexComp;
    }
    
    public ArrayList<Integer> getVertices(){
        return this.vertices;
    }
    
    public Integer getVertice(int pos){
        return vertices.get(pos);
    }
    
    public double getPurity(){
        return purity;
    }
    
    public int getClasse(){
        return classe;
    }
    
    public Integer getK(){
        return k;
    }
    
    public int getindexComp(){
        return indexComp;
    }
}
