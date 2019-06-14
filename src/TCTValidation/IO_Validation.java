//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTValidation;

import java.io.File;
import javax.swing.JOptionPane;

public class IO_Validation {
    public static boolean ValidaDir(String tipo, String diretorio){ //Validar Diretorios
        File dir = new File(diretorio);
        if(!dir.exists()){
            JOptionPane.showMessageDialog(null, "O diretório de " + tipo + " não existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!dir.isDirectory()){
            JOptionPane.showMessageDialog(null, "O diretório de " + tipo + " não é válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
