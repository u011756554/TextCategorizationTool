//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

import java.io.Serializable;

public class LambdasGNetMine implements Serializable{
    public double lambdaDocDoc;
    public double lambdaDocTerm;
    public double lambdaTermTerm;
    
    public LambdasGNetMine(){ }
    
    public LambdasGNetMine(double lambdaDocDoc, double lambdaDocTerm, double lambdaTermTerm){
        this.lambdaDocDoc = lambdaDocDoc;
        this.lambdaDocTerm = lambdaDocTerm;
        this.lambdaTermTerm = lambdaTermTerm;
    }
}
