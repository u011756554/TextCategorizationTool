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

package TCTAlgorithms.Transductive;

import TCTStructures.IndexConfidence;
import java.util.ArrayList;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instance;
import weka.core.Instances;

public class TSVM_Unbalanced_Transductive extends TransductiveLearner{
    
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private double c; // Paramenter of the algorithm
    private int maxNumberInterations; // Maximum number of iterations
    
    public TSVM_Unbalanced_Transductive(){
        super();
        setC(1.0);
        setMaxNumIterations(50);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        
        fUnlabeledDocs = new double[numTest][numClasses];
        
        Instances dataTestCopy = new Instances(dataTest);
        
        SMO classifSVM = new SMO();
        PolyKernel polyKernel = new PolyKernel();
        polyKernel.setExponent(1);
        classifSVM.setKernel(polyKernel);
        
        try{
            classifSVM.buildClassifier(dataTrain);
        }catch(Exception e){
            System.out.println("Error whe building a classification model");
            e.printStackTrace();
            System.exit(0);
        }
        
        ArrayList<IndexConfidence> confidences = new ArrayList<IndexConfidence>();
        try{
            for(int inst=0;inst<numTest;inst++){
                confidences.add(new IndexConfidence(inst, classifSVM.distributionForInstance(dataTestCopy.instance(inst))));
            }    
        }catch(Exception e){
            System.err.println("Error when classifying a test instance.");
            e.printStackTrace();
            System.exit(0);
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
            System.exit(0);
        }
        
        double cMinus = 10e-5;
        double cPlus = 10e-5*numTest/(Math.abs(numTest - numTrain));

        Instances dataFull = new Instances(dataTrain);
        for(int inst=0;inst<numTest;inst++){
            Instance instance = dataTestCopy.instance(inst);
            dataFull.add(instance);
        }
        
        int numIt = 0;
        while((cMinus < c) && (cPlus < c) && (numIt <= maxNumberInterations)){
            classifSVM.setC((cMinus + cPlus)/2);
            try{
                classifSVM.buildClassifier(dataFull);
            }catch(Exception e){
                System.out.println("Error when building a classification model");
                e.printStackTrace();
                System.exit(0);
            }
            ArrayList<Integer> indErrors = new ArrayList<Integer>();
            //Obtendo as instancias com errors
            for(int inst=numTrain;inst<(numTrain+numTest);inst++){
                Instance instance = dataFull.instance(inst);
                try{
                    double previousClass = instance.classValue();
                    double predictedClass = classifSVM.classifyInstance(instance);
                    if(previousClass != predictedClass){
                        indErrors.add(inst);
                    }
                }catch(Exception e){
                    System.out.println("Error when classifying a test instance.");
                    e.printStackTrace();
                    System.exit(0);
                }
            }
            
            for(int item1=0;item1<indErrors.size();item1++){
                Instance instance1 = dataFull.instance(indErrors.get(item1));
                for(int item2=item1;item2<indErrors.size();item2++){
                    Instance instance2 = dataFull.instance(indErrors.get(item2));
                    if(instance1.classValue() != instance2.classValue()){
                        double classTemp = instance1.classValue();
                        instance1.setClassValue(instance2.classValue());
                        instance2.setClassValue(classTemp);    
                        indErrors.remove(item2);
                    }
                    
                }
            }
            cMinus = Math.min(2*cMinus, c);
            cPlus = Math.min(2*cPlus, c);
            numIt++;
        }
        
        //Assigning labels to unlabeled documents
        for(int inst=numTrain;inst<(numTrain + numTest);inst++){
            int classe = (int)dataFull.instance(inst).classValue();
            fUnlabeledDocs[inst - numTrain][classe] = 1;
        }
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
