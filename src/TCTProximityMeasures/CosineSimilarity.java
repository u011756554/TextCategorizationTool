//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTProximityMeasures;


import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Range;
import weka.core.neighboursearch.PerformanceStats;

public class CosineSimilarity implements DistanceFunction{

	protected Instances m_Data = null;
	protected boolean normalized=false;
	
	@Override
	public double distance(Instance first, Instance second) {
		return distance(first, second, Double.POSITIVE_INFINITY, null);
	}

	@Override
	public double distance(Instance first, Instance second, PerformanceStats stats) throws Exception {
		return distance(first, second, Double.POSITIVE_INFINITY, stats);
	}

	@Override
	public double distance(Instance first, Instance second, double cutOffvalue) {
		return distance(first, second, Double.POSITIVE_INFINITY, null);
	}

	@Override
	public double distance(Instance first, Instance second, double cutOffvalue, PerformanceStats stats) {
		
                
		double distance=0;
                double num1=0;
                double num2=0;
		for(int j=0; j < first.numAttributes()-1; j++){ /* ignora o atributo classe (ultimo) */
			Attribute att = first.attribute(j);
			Attribute att2 = second.attribute(j);
			
			// overlap */
			/*if(att.isNominal() || att.isString()){	
				if(!first.stringvalue(att).equals( second.stringvalue(att) )){ // sao diferentes, entao sum 1
					distance += 1;
				}
			}else{*/
				
				double x = first.value(att);
                                num1 = num1 + (x*x);
				double y = second.value(att2);
				num2 = num2 + (y*y);
                                distance = distance + x*y;
				
				// aplicando modulo
			//}
			
		}		
		num1 = Math.sqrt(num1);
                num2 = Math.sqrt(num2);
                
                //System.out.println("distance antes: " + distance);
                distance= distance/(num1*num2);
                
                //System.out.println("num1: " + num1);
                //System.out.println("num2: " + num2);
                //System.out.println("distance: " + distance);
                
		return (1 - distance);
		
	}

	@Override
	public String getAttributeIndices() {
		return null;
	}

	@Override
	public Instances getInstances() {
		return m_Data;
	}

	@Override
	public boolean getInvertSelection() {
		return false;
	}

	@Override
	public void postProcessDistances(double[] distances) {

	}

	@Override
	public void setAttributeIndices(String value) {

	}

	@Override
	public void setInstances(Instances insts) {
		m_Data = insts;
	}

	@Override
	public void setInvertSelection(boolean value) {

	}

	@Override
	public void update(Instance ins) {

	}

	@Override
	public String[] getOptions() {
		return null;
	}

	@Override
	public Enumeration listOptions() {
		return null;
	}

	@Override
	public void setOptions(String[] options) throws Exception {

	}

	public void normalize(){
		if(normalized) return;
		
		int numAttributes = m_Data.instance(0).numAttributes();
		double[] MAX_VALUE = new double[numAttributes];
		
		for(int i=0; i < m_Data.numInstances(); i++){
			Instance inst = m_Data.instance(i);
			for(int j=0; j < inst.numAttributes(); j++){
				
				if(inst.attribute(j).isNumeric() && !inst.isMissing(j)){
					if(inst.value(j) > MAX_VALUE[j]){
						MAX_VALUE[j] = inst.value(j);
					}
				}
			}
		}
		
		for(int i=0; i < m_Data.numInstances(); i++){
			Instance inst = m_Data.instance(i);
			for(int j=0; j < inst.numAttributes(); j++){
				
				if(inst.attribute(j).isNumeric() && !inst.isMissing(j)){
					inst.setValue(j, inst.value(j)/MAX_VALUE[j]);
				}
			}
		}	
		
		normalized=true;
	}	
	
}