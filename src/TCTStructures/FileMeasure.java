//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTStructures;

public class FileMeasure {
    private String fileName;
    private ResultMeasures measures;
    
    public FileMeasure(){
        fileName = new String();
        measures = new ResultMeasures();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ResultMeasures getMeasures() {
        return measures;
    }

    public void setMeasures(ResultMeasures measures) {
        this.measures = measures;
    }
    
    
}
