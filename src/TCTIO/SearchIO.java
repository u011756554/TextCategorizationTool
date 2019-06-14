//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************

package TCTIO;

import javax.swing.JFileChooser;

public class SearchIO {

    public static String AbreDir(){
        String diretorio = new String("");
        JFileChooser open = new JFileChooser();
        open.setFileSelectionMode(open.DIRECTORIES_ONLY);
        open.setDialogTitle("Selecione o Diretório");
        open.setDialogType(open.OPEN_DIALOG);
        open.showOpenDialog(null);
        if(!(open.getSelectedFile()==null)){
            diretorio = open.getSelectedFile().toString();
        }
        return diretorio;
    }

    public static String AbreArq(){
        String arquivo = new String("");
        JFileChooser open = new JFileChooser();
        open.setFileSelectionMode(open.FILES_ONLY);
        open.setDialogTitle("Selecione o Arquivo");
        open.setDialogType(open.OPEN_DIALOG);
        open.showOpenDialog(null);
        if(!(open.getSelectedFile()==null)){
            arquivo = open.getSelectedFile().toString();
        }
        return arquivo;
    }
}
