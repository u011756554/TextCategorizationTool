//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is and implementation of Transductive Support Vector
//              Machine presented by Joachims, 1999. We opt to use a multiclass
//              classifier since we vefified it obatins better results.
// References: - T. Joachims, Transductive inference for text classification
//               using support vector machines, in: Proceedings of International
//               Conference on Machine Learning, 1999, pp. 200–209.
//******************************************************************************

package TCTAlgorithms.Transductive;

import TCTStructures.IndexConfidence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instance;
import weka.core.Instances;

public class TSVM_Balanced_Transductive extends TransductiveLearner{
    
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private double c; // Paramenter of the algorithm // Paramenter of the algorithm
    private int maxNumberInterations; // Maximum number of iterations
    
    public TSVM_Balanced_Transductive(){
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
        
        //Creating a SVM classifier with linear Kernel
        SMO classifSVM = new SMO();
        PolyKernel polyKernel = new PolyKernel();
        polyKernel.setExponent(1);
        classifSVM.setKernel(polyKernel);
        classifSVM.setC(c);
        int[] numInstClasses = new int[numClasses];
        
        for(int inst=0;inst<numTrain;inst++){
            int classeInst = (int)dataTrain.instance(inst).classValue();
            numInstClasses[classeInst] = numInstClasses[classeInst] + 1;
        }
        
        double sum = 0;
        for(int classe=0;classe<numClasses;classe++){
            sum += numInstClasses[classe];
        }
        for(int classe=0;classe<numClasses;classe++){
            numInstClasses[classe] = (int)((numInstClasses[classe]/sum) * numTest);
        }
        
        try{
            classifSVM.buildClassifier(dataTrain);
        }catch(Exception e){
            System.err.println("Error when building a classification model");
            e.printStackTrace();
            System.exit(0);
        }
        ArrayList<IndexConfidence> confidences = new ArrayList<IndexConfidence>();
        try{
            for(int inst=0;inst<numTest;inst++){
                confidences.add(new IndexConfidence(inst, classifSVM.distributionForInstance(dataTestCopy.instance(inst))));
            }    
        }catch(Exception e){
            System.err.println("Error when classifying a test instance");
            e.printStackTrace();
            System.exit(0);
        }
       
        double[] numInstClassesTemp = new double[numClasses];
        boolean control = false;
        boolean exit = false;
        
        while(exit == false){
            int numConfsPrevious = 0;
            for(int classe=0;classe<numClasses;classe++){
                for(int inst=0;inst<confidences.size();inst++){
                    confidences.get(inst).setClass(classe);
                }
                Object[] sortedConficences =  confidences.toArray();
                Arrays.sort( sortedConficences, new Comparator() {
                public int compare( Object obj1, Object obj2 ) {
                        IndexConfidence indConf1 = (IndexConfidence)obj1;
                        IndexConfidence indConf2 = (IndexConfidence)obj2;
                        int classeOrd = indConf1.getClasse();
                        if(indConf1.getConfiancas()[classeOrd] > indConf2.getConfiancas()[classeOrd]){
                            return -1;
                        }else{
                            if(indConf1.getConfiancas()[classeOrd] < indConf2.getConfiancas()[classeOrd]){
                                return 1;
                            }else{
                                return 0;
                            }         
                        }
                }});
                ArrayList<Integer> removedIndexes = new ArrayList<Integer>();
                numConfsPrevious = confidences.size();
                for(int inst=0;inst<sortedConficences.length;inst++){
                    if(numInstClassesTemp[classe] == numInstClasses[classe]){
                        continue;
                    }
                    IndexConfidence indConfs = (IndexConfidence)sortedConficences[inst];
                    double[] confs = indConfs.getConfiancas();
                    if(control == false){
                        double maior = -30000;
                        int indMaior = -1;
                        for(int class2=0;class2<numClasses;class2++){
                            if(confs[class2] > maior){
                                maior = confs[class2];
                                indMaior = class2;
                            }
                        }
                        if(indMaior == classe){
                            dataTestCopy.instance(indConfs.getindex()).setClassValue((double)classe);
                            removedIndexes.add(indConfs.getindex());
                            numInstClassesTemp[classe] = numInstClassesTemp[classe] + 1; 
                        }
                    }else{
                        dataTestCopy.instance(indConfs.getindex()).setClassValue((double)classe);
                        removedIndexes.add(indConfs.getindex());
                        numInstClassesTemp[classe] = numInstClassesTemp[classe] + 1; 
                    }
                }
                for(int inst=0;inst<confidences.size();inst++){
                    int ind = removedIndexes.indexOf(confidences.get(inst).getindex());
                    if(ind != -1){
                        removedIndexes.remove(ind);
                        confidences.remove(inst);
                        inst--;
                    }
                }
            }
            control = true;
            if(confidences.size() == 0){
                exit = true;
            }
            
            if(numConfsPrevious == confidences.size()){
                exit = true;
                for(int inst=0;inst<confidences.size();inst++){
                    Instance instance = dataTestCopy.instance(confidences.get(inst).getindex());
                    try{
                        double valueClasse = classifSVM.classifyInstance(instance);
                        instance.setClassValue(valueClasse);
                    }catch(Exception e){
                        System.err.println("Error when classifying a test instance");
                        e.printStackTrace();
                        System.exit(0);
                    }
                    
                }
            }
            
        }
        
        double cMinus = 10e-5;
        double cPlus = 10e-5*numTest/(Math.abs(numTest - numTrain));
        
        Instances dataFull = new Instances(dataTrain);
        for(int inst=0;inst<numTest;inst++){
            Instance instance = dataTestCopy.instance(inst);
            dataFull.add(instance);
        }
        
        int numIt = 0;
        boolean changed = true;
        exit = false;
        while(exit == false){
            classifSVM.setC((cMinus + cPlus)/2);
            try{
                classifSVM.buildClassifier(dataFull);
            }catch(Exception e){
                System.err.println("Error when building a classification model");
                e.printStackTrace();
                System.exit(0);
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
                    System.exit(0);
                }
            }
            
            changed = false;
            for(int item1=0;item1<indErrors.size();item1++){
                Instance instance1 = dataFull.instance(indErrors.get(item1));
                for(int item2=item1;item2<indErrors.size();item2++){
                    Instance instance2 = dataFull.instance(indErrors.get(item2));
                    if(instance1.classValue() != instance2.classValue()){
                        double classTemp = instance1.classValue();
                        instance1.setClassValue(instance2.classValue());
                        instance2.setClassValue(classTemp);    
                        indErrors.remove(item2);
                        changed = true;
                    }
                    
                }
            }
            cMinus = Math.min(2*cMinus, c);
            cPlus = Math.min(2*cPlus, c);
            numIt++;
            // Analysis of stopping criteria
            if((cMinus < c) || (cPlus < c) || (numIt == maxNumberInterations) || (changed == false)){
                exit = true;
            }
            
        }
        
        //Assigning label to unlabeled documents
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
