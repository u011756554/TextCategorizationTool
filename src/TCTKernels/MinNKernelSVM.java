//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: 
//*****************************************************************************  

package TCTKernels;

import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.Capabilities.Capability;

import java.util.Enumeration;
import java.util.Vector;

public class MinNKernelSVM extends weka.classifiers.functions.supportVector.CachedKernel {

  public MinNKernelSVM() {
    super();
  }


  public MinNKernelSVM(Instances data, int cacheSize) throws Exception {

    super();

    setCacheSize(cacheSize);

    buildKernel(data);
  }

  public String globalInfo() {
    return
        "The additive min kernel";
  }

  /**
   * Returns an enumeration describing the available options.
   *
   * @return 		an enumeration of all the available options.
   */
  public Enumeration listOptions() {
    Vector		result;
    Enumeration		en;

    result = new Vector();

    en = super.listOptions();
    while (en.hasMoreElements())
      result.addElement(en.nextElement());

    return result.elements();
  }

  /**
   * Parses a given list of options. <p/>
   *
   */
  public void setOptions(String[] options) throws Exception {

    super.setOptions(options);
  }

  /**
   * Gets the current settings of the Kernel.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  public String[] getOptions() {
    int       i;
    Vector    result;
    String[]  options;

    result = new Vector();
    options = super.getOptions();
    for (i = 0; i < options.length; i++)
      result.add(options[i]);

    return (String[]) result.toArray(new String[result.size()]);
  }

  /**
   *
   * @param id1   	the index of instance 1
   * @param id2		the index of instance 2
   * @param inst1	the instance 1 object
   * @return 		the dot product
   * @throws Exception 	if something goes wrong
   */
  protected double evaluate(int id1, int id2, Instance inst1)
    throws Exception {

    double result;
    if (id1 == id2) {
      result = addMin(inst1, inst1);
    } else {
      result = addMin(inst1, m_data.instance(id2));
    }

    return result;
  }

  protected final double addMin(Instance inst1, Instance inst2)
    throws Exception {

    double num = 0;
    double den1 = 0;
    double den2 = 0;

    int n1 = inst1.numValues();
    int n2 = inst2.numValues();
    int classIndex = m_data.classIndex();
    for (int p1 = 0, p2 = 0; p1 < n1 && p2 < n2;) {
      int ind1 = inst1.index(p1);
      int ind2 = inst2.index(p2);
      if (ind1 == ind2) {
	 	if (ind1 != classIndex) {
	 		double v1 = inst1.valueSparse(p1);
	  		double v2 = inst2.valueSparse(p2);
	  		num += ((v1<v2) ? v1 : v2);
	  		den1 += v1;
	  		den2 += v2;
		}
		p1++;
		p2++;
      } else if (ind1 > ind2) {
		double v2 = inst2.valueSparse(p2);
		den2 += v2;
		p2++;
      } else {
		double v1 = inst1.valueSparse(p1);
		den1 += v1;
		p1++;
      }
    }

    return (num/ (Math.sqrt(den1)*Math.sqrt(den2)) );
  }

  /**
   * Returns the Capabilities of this kernel.
   *
   * @return            the capabilities of this object
   * @see               Capabilities
   */
  public Capabilities getCapabilities() {
    Capabilities result = super.getCapabilities();
    result.disableAll();

    result.enable(Capability.NUMERIC_ATTRIBUTES);
    result.enableAllClasses();

    return result;
  }

  /**
   * returns a string representation for the Kernel
   *
   * @return 		a string representaiton of the kernel
   */
  public String toString() {
    String	result = "Additive Min Kernel";

    return result;
  }

}
