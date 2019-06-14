//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is and implementation of Transductive Support Vector
//              Machine presented by Joachims, 1999. In this class we do not
//              keep the same class proportion of labeled documents in the
//              classification of unlabeled documents. (This class is still not
//              complete)
// References: - T. Joachims, Transductive inference for text classification
//               using support vector machines, in: Proceedings of International
//               Conference on Machine Learning, 1999, pp. 200–209.
//******************************************************************************

package TCTAlgorithms.Transductive;

import java.util.ArrayList;
import java.util.Random;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class TSVMBinary_OneAgainstRest_Unbalanced_Transductive extends TransductiveLearner{
    
    private int numTrain; // Number of labeled documents
    private int numTest; // Number of unlabeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private double c; // Paramenter of the algorithm
    private int maxNumberInterations; // Maximum number of iterations
    ArrayList<SMO> classifieres;
    
    public TSVMBinary_OneAgainstRest_Unbalanced_Transductive(){
        super();
        setC(1.0);
        setMaxNumIterations(50);
    }
    
    public void buildClassifier(Instances dataTrain, Instances dataTest){
        this.numTrain = dataTrain.numInstances();
        this.numTest = dataTest.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        fUnlabeledDocs = new double[numTest][numClasses];
        
        classifieres = new ArrayList<SMO>();
        for(int classe=0;classe<numClasses;classe++){
            SMO classifSMO = new SMO();
            classifieres.add(classifSMO);
        }
        
        for(int classe=0;classe<numClasses;classe++){
            
            Instances dataClassTrain = new Instances(dataTrain,0);
            Instances dataClassTest = new Instances(dataTest,0);
            FastVector my_nominal_values = new FastVector(2);
            my_nominal_values.addElement("negativo");
            my_nominal_values.addElement("positivo");
            Attribute featureNewClass = new Attribute("att_class2", my_nominal_values);
            dataClassTrain.insertAttributeAt(featureNewClass, numTerms);
            dataClassTest.insertAttributeAt(featureNewClass, numTerms);
            numTerms = dataClassTrain.numAttributes();
            
            Attribute classAtt = dataClassTrain.attribute(dataClassTrain.numAttributes()-2); //Setting the last feature as class
            dataClassTrain.setClass(classAtt);
            dataClassTest.setClass(classAtt);

            dataClassTrain.deleteAttributeAt(numTerms - 1);
            dataClassTest.deleteAttributeAt(numTerms - 1);
            numTerms = dataClassTrain.numAttributes();
            
            for(int inst=0;inst<numTrain;inst++){
                Instance instance = dataTrain.instance(inst);
                Instance instanceClasse = new Instance(numTerms); 
                instanceClasse.setDataset(dataClassTrain);
                for(int term=0;term<numTerms-1;term++){
                    instanceClasse.setValue(term, instance.value(term));
                }
                int classeInst = (int)instance.classValue();
                if(classeInst != classe){
                    instanceClasse.setClassValue(0);
                }else{
                    instanceClasse.setClassValue(1);
                }
                dataClassTrain.add(instanceClasse);
            }
            
            for(int inst=0;inst<numTest;inst++){
                Instance instance = dataTest.instance(inst);
                Instance instanceClasse = new Instance(numTerms); 
                instanceClasse.setDataset(dataClassTest);
                for(int term=0;term<numTerms-1;term++){
                    instanceClasse.setValue(term, instance.value(term));
                }
                int classeInst = (int)instance.classValue();
                if(classeInst != classe){
                    instanceClasse.setClassValue(0);
                }else{
                    instanceClasse.setClassValue(1);
                }
                dataClassTest.add(instanceClasse);
            }

            SMO classifSVM = classifieres.get(classe);
            try{
                Instances dataTestCopy = new Instances(dataClassTest);
                PolyKernel polyKernel = new PolyKernel();
                polyKernel.setExponent(1);
                classifSVM.setKernel(polyKernel);
                classifSVM.setC(c);
                int[] numInstClasses = new int[2];

                for(int inst=0;inst<numTrain;inst++){
                    int classeInst = (int)dataClassTrain.instance(inst).classValue();
                    numInstClasses[classeInst] = numInstClasses[classeInst] + 1;
                }

                double sum = 0;
                for(int class2=0;class2<2;class2++){
                    sum += numInstClasses[class2];
                }
                for(int class2=0;class2<2;class2++){
                    numInstClasses[class2] = (int)((numInstClasses[class2]/sum) * numTest);
                }
                
                try{
                    classifSVM.buildClassifier(dataClassTrain);
                    for(int inst=0;inst<numTest;inst++){
                        double[] confs = classifSVM.distributionForInstance(dataTestCopy.instance(inst));
                        if(confs[1] >= confs[0]){
                            dataTestCopy.instance(inst).setClassValue(1.0);
                        }else{
                            dataTestCopy.instance(inst).setClassValue(0.0);
                        }
                    }
                }catch(Exception e){
                    System.err.println("Error when building a classification model.");
                    e.printStackTrace();
                    System.exit(0);
                }
                
                boolean exit = false;
                
                //Segunda parte do TSVM
                double cMinus = 10e-5;
                double cPlus = 10e-5*numInstClasses[1]/(Math.abs((numTrain+numTest) - numInstClasses[1]));

                //Conjunto de instancias com dataTrain + dataTest
                Instances dataFull = new Instances(dataClassTrain);
                for(int inst=0;inst<numTest;inst++){
                    Instance instance = dataTestCopy.instance(inst);
                    dataFull.add(instance);
                }

                int numIt = 0;
                
                exit = false;
            
                while(exit == false){
                    classifSVM.setC((cMinus + cPlus)/2);
                    try{
                        classifSVM.buildClassifier(dataFull);
                    }catch(Exception e){
                        System.err.println("Error when building a classification model.");
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
                            System.err.println("Error when classifying a test instance.");
                            e.printStackTrace();
                            System.exit(0);
                        }
                    }

                    int numTrocas = 0;
                    for(int item1=0;item1<indErrors.size();item1++){
                        Instance instance1 = dataFull.instance(indErrors.get(item1));
                        for(int item2=item1;item2<indErrors.size();item2++){
                            Instance instance2 = dataFull.instance(indErrors.get(item2));
                            if(instance1.classValue() != instance2.classValue()){
                                double classTemp = instance1.classValue();
                                instance1.setClassValue(instance2.classValue());
                                instance2.setClassValue(classTemp);    
                                indErrors.remove(item2);
                                numTrocas++;
                            }

                        }
                    }
                    
                    Random rand = new Random();
                    int numTrocasRand = 0;
                    if(numTrocas == 0){
                        for(int item1=0;item1<indErrors.size();item1++){
                            if(rand.nextBoolean()== true){
                                Instance instance1 = dataFull.instance(indErrors.get(item1));
                                if(instance1.classValue() == 0){
                                    instance1.setClassValue(1);
                                }else{
                                    instance1.setClassValue(0);
                                }
                                numTrocasRand++;
                            }
                        }
                    }
                    cMinus = Math.min(2*cMinus, c);
                    cPlus = Math.min(2*cPlus, c);
                    numIt++;
                    
                    if(cMinus >= c){
                        exit = true;
                    }
                    if(cPlus >= c){
                        exit = true;
                    }
                    if(numIt == maxNumberInterations){
                        exit = true;
                    }
                    if(numTrocasRand == 0){
                        exit = true;
                    }
                    
                }
            }catch(Exception e){
                System.err.println("Error when classifying test instances.");
                e.printStackTrace();
                System.exit(0);
            }
        }    
        //Assigning labels to unlabeled documents
        for(int inst=0;inst<numTest;inst++){
            int classe = (int)classifyInstance(dataTest.instance(inst));
            fUnlabeledDocs[inst][classe] = 1;
        }
    }
    
    
    public double classifyInstance(Instance instance){
        
        double[] confidences = distributionForInstance(instance);
        double valueClasse = -1;
        double maior = -30000000;
        for(int classe=0;classe<numClasses;classe++){
            if(confidences[classe] > maior){
                maior = confidences[classe];
                valueClasse = classe;
            }
        }
        return valueClasse;
    }
    
    public double[] distributionForInstance(Instance instance){
        double[][] confidences = new double[numClasses][2];
        for(int classe=0;classe<numClasses;classe++){
            SMO classifSVM = classifieres.get(classe);
            double[] confsClasse = new double[2];
            try{
                double valClasse = classifSVM.classifyInstance(instance);
                if(valClasse > 0){
                    double[] confsSVM = classifSVM.distributionForInstance(instance);
                    confsClasse[1] = confsSVM[1];
                }
                //System.out.println();
            }catch(Exception e){
                System.err.println("Error when classifying a test instance");
                e.printStackTrace();
                System.exit(0);
            }
            confidences[classe][0] = confsClasse[0];
            confidences[classe][1] = confsClasse[1];
        }
        double[] dist = new double[numClasses];
        double sum = 0;
        for(int classe=0;classe<numClasses;classe++){
            sum += confidences[classe][1];
        }
        if(sum!=0){
            for(int classe=0;classe<numClasses;classe++){
                dist[classe] = confidences[classe][1]/sum;
            }    
        }
        
        return dist;
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
