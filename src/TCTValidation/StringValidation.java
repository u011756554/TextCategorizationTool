//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTValidation;

import javax.swing.JOptionPane;

public class StringValidation {
    public static boolean StringVazia(String tipo, String string){
        if(string.isEmpty()){
            JOptionPane.showMessageDialog(null, "O " + tipo + " não é válido", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }
}
