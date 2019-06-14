/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTPreprocessing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


public class CharsetRecognition {
    
    public static String Recognize(File file){
        String[] charsetsToBeTested = {"UTF-8", "ISO-8859-1", "UTF-16", "US-ASCII", "windows-1253", "ISO-8859-7"};
        
        Charset charset = null;
        
        for (String charsetName : charsetsToBeTested) {
            charset = detectCharset(file, Charset.forName(charsetName));
            if (charset != null) {
                break;
            }
        }
        
        if (charset != null) {
            return charset.name();
        }else{
            return "UTF-8";
        }
        
    }
    
    
    private static Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));
 
            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();
 
            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }
 
            input.close();
 
            if (identified) {
                return charset;
            } else {
                return null;
            }
 
        } catch (Exception e) {
            return null;
        }
    }
    
    
    private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
    
    
}
