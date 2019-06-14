//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is and implementation of Transductive Support Vector
//              Machine presented by Joachims, 1999. In this class we do not
//              keep the same class proportion of labeled documents in the
//              classification of unlabeled documents. We opt to use
//              a multiclass classifier since we vefified it obatins better
//              results.
// References: - T. Joachims, Transductive inference for text classification
//               using support vector machines, in: Proceedings of International
//               Conference on Machine Learning, 1999, pp. 200–209.
//******************************************************************************

package TCTAlgorithms.InductiveSemiSupervised;

import TCTStructures.IndexConfidence;
import java.util.ArrayList;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instance;
import weka.core.Instances;

public class TSVM_Unbalanced_InductiveSemiSupervised extends InductiveSemiSupervisedClassifier{
    
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private double c; // Paramenter of the algorithm 
    private int maxNumberInterations; // Maximum number of iterations
    private SMO classifSVM; // SVM Classifier
    
    // Constructor
    public TSVM_Unbalanced_InductiveSemiSupervised(){
        super();
        setC(1.0);
        setMaxNumIterations(50);
    }
    
    //Function to perform transductive learning
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        
        Instances dataTestCopy = new Instances(dataTest);
        
        //Creating a SVM classifier with linear Kernel
        classifSVM = new SMO();
        PolyKernel polyKernel = new PolyKernel();
        polyKernel.setExponent(1);
        classifSVM.setKernel(polyKernel);
        
        try{
            classifSVM.buildClassifier(dataTrain);
        }catch(Exception e){
            System.err.println("Error when building a classification model");
            e.printStackTrace();
        }
        
        ArrayList<IndexConfidence> confidences = new ArrayList<IndexConfidence>();
        try{
            for(int inst=0;inst<numTest;inst++){
                confidences.add(new IndexConfidence(inst, classifSVM.distributionForInstance(dataTestCopy.instance(inst))));
            }    
        }catch(Exception e){
            System.err.println("Error when classifying a test instance");
            e.printStackTrace();
        }
       
        
        try{
            for(int inst=0;inst<numTest;inst++){
                Instance instance = dataTestCopy.instance(inst);
                double valueClasse = classifSVM.classifyInstance(instance);
                instance.setClassValue(valueClasse);
            }
        }catch(Exception e){
            System.err.println("Error when classifying a test instance");
            e.printStackTrace();
        }
        
        double cMinus = 10e-5;
        double cPlus = (10e-5*numTrain)/(Math.abs(numTrain - numTest));

        Instances dataFull = new Instances(dataTrain);
        for(int inst=0;inst<numTest;inst++){
            Instance instance = dataTestCopy.instance(inst);
            dataFull.add(instance);
        }
        
        int numIt = 0;
        while((cMinus < c) || (cPlus < c) || (numIt == maxNumberInterations)){
            classifSVM.setC((cMinus + cPlus)/2);
            try{
                classifSVM.buildClassifier(dataFull);
            }catch(Exception e){
                System.err.println("Error when building a classification model");
                e.printStackTrace();
            }
            ArrayList<Integer> indErrors = new ArrayList<Integer>();
            for(int inst=numTrain;inst<(numTrain+numTest);inst++){
                Instance instance = dataFull.instance(inst);
                try{
                    double previousClass = instance.classValue();
                    double predictedClass = classifSVM.classifyInstance(instance);
                    if(previousClass != predictedClass){
                        indErrors.add(inst);
                    }
                }catch(Exception e){
                    System.err.println("Error when classifying a test instance");
                    e.printStackTrace();
                }
            }
            
            for(int item1=0;item1<indErrors.size();item1++){
                Instance instance1 = dataFull.instance(indErrors.get(item1));
                for(int item2=item1;item2<indErrors.size();item2++){
                    Instance instance2 = dataFull.instance(indErrors.get(item2));
                    //Exclange class between instances
                    double classTemp = instance1.classValue();
                    instance1.setClassValue(instance2.classValue());
                    instance2.setClassValue(classTemp);
                }
            }
            cMinus = Math.min(2*cMinus, c);
            cPlus = Math.min(2*cPlus, c);
            numIt++;
        }
    }

    // Function to classify new instances
    public double classifyInstance(Instance instance){
        try{
            return classifSVM.classifyInstance(instance);
        }catch(Exception e){
            System.out.println("TSVM: error when classifying a test instance");
        }
        return -1;
    }
    
    //Function to return the classification confidences for each class
    public double[] distributionForInstance(Instance instance){
        double[] distClasses = new double[numClasses];
        try{
            return classifSVM.distributionForInstance(instance);
        }catch(Exception e){
            System.out.println("TSVM: error when classifying a test instance");
        }
        return distClasses;
    }
    
    public void setMaxNumIterations(int maxNumberInterations){
        this.maxNumberInterations = maxNumberInterations;
    }
    
    public void setC(double c){
        this.c = c;
    }
    
    public Integer maxNumberInterations(){
        return maxNumberInterations;
    }
    
    public Double getC(){
        return c;
    }
}
