//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTPreprocessing;

/*
 * Remove caracteres nao permitidos de uma variavel string que representa um texto.
 * Os unicos caracteres permitidos sao: abcdefghijklmnopqrstuvwxyz.!_?
 */

/*
 * Remove caracteres nao permitidos de uma variavel string que representa um texto.
 * Os unicos caracteres permitidos sao: abcdefghijklmnopqrstuvwxyz.!_?
 */
public class Cleaner {

    public Cleaner(){

    }

    /** Recebe uma String qualquer e retorna uma String apenas com caracteres permitidos  */
    public String clean(String str){
        String allowed="aáâàãâbcçdeéêfghiíjklmnoóôõpqrstuúüvwxyz_";
        //String allowed="aáâàãâbcçdeéêfghiíjklmnoóôõpqrstuúüvwxyz_[]12345678-";
        StringBuffer new_str= new StringBuffer("");
        str=str.toLowerCase();

        //for(int i=0; i < 100; i++) str=str.replace("\n\n", "\n");
        //str=str.replace("\r", "");
        //str=str.replace("\n", ".");

        for(int i=0; i < str.length(); i++){
            String ch = String.valueOf(str.charAt(i));
            if(allowed.contains(ch)){ /* char allowed? */
                new_str.append(ch);
            }else{
                new_str.append(" ");}
        }
        String new_str2 = new_str.toString();
        boolean exit = false;
        int size1 =0, size2 =0;
        while(exit == false){
            size1 = new_str2.length();
            new_str2=new_str2.replace("  ", " ");
            size2 = new_str2.length();
            if(size1 == size2){
                exit = true;
            }
        }
        
        return new_str2.trim();
    }

}
