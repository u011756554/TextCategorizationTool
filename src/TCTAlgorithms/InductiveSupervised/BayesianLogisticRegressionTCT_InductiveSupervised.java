//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of Bayesian Logistic Regression for
//              multiclass classification. We used one-vs-all strategy to 
//              perform multiclass classification. We considered the 
//              weka.classifiers.bayes.BayesianLogisticRegression to induce a
//              classification model for each class. 
//*****************************************************************************

package TCTAlgorithms.InductiveSupervised;

import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesianLogisticRegression;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class BayesianLogisticRegressionTCT_InductiveSupervised extends Classifier{
    
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    
    private int maxNumberInterations; // Maximum number of iterations
    private int numFolds; // Number of folds for CV-based hyperparameter selection
    private double tolerance; // Tolerance criteria for the stopping criterion.
    
    private int prior; // 1 = Gaussian, 2 = Laplacian
    
    ArrayList<BayesianLogisticRegression> classifieres;
    
    @Override
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes();
        
        // Creating a single classifier per class
        classifieres = new ArrayList<BayesianLogisticRegression>();
        for(int classe=0;classe<numClasses;classe++){
            BayesianLogisticRegression classifBLR = new BayesianLogisticRegression();
            classifBLR.HyperparameterSelection = 1;
            classifBLR.setNormalizeData(true);
            classifBLR.PriorClass = this.getPrior();
            classifBLR.setMaxIterations(this.getMaxNumberIterations());
            classifBLR.setTolerance(this.getTolerance());
            classifBLR.setNumFolds(this.getNumFolds());
            classifieres.add(classifBLR);
        }

        // Generating a binary dataset for each class: positive label (1) for the current class and negative label (0) for others
        for(int classe=0;classe<numClasses;classe++){
            Instances dataClasse = new Instances(dataTrain,0);
            FastVector my_nominal_values = new FastVector(2);
            my_nominal_values.addElement("negative");
            my_nominal_values.addElement("positive");
            Attribute featureNewClass = new Attribute("att_class", my_nominal_values);
            dataClasse.insertAttributeAt(featureNewClass, numTerms);
            
            Attribute classAtt = dataClasse.attribute(dataClasse.numAttributes()-1); //Setting the last feature as class
            dataClasse.setClass(classAtt);

            dataClasse.deleteAttributeAt(numTerms - 1);
            
            for(int inst=0;inst<numTrain;inst++){
                Instance instance = dataTrain.instance(inst);
                Instance instanceClasse = new Instance(numTerms); 
                instanceClasse.setDataset(dataClasse);
                for(int term=0;term<numTerms-1;term++){
                    instanceClasse.setValue(term, instance.value(term));
                }
                int classeInst = (int)instance.classValue();
                if(classeInst != classe){
                    instanceClasse.setClassValue(0);
                }else{
                    instanceClasse.setClassValue(1);
                }
                dataClasse.add(instanceClasse);
            }
            
            //Inducing a classification model for each class
            BayesianLogisticRegression classifBLR = classifieres.get(classe);
            try{
                classifBLR.buildClassifier(dataClasse);
            }catch(Exception e){
                System.err.println("Error when building a classification model");
            }    
        }
    }
    
    @Override
    // Function to classify a new instance (hard classification)
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
    
    @Override
    //Obtaining the class confidences for each class
    //The class confidences correspod to the classification conficence generated for each classifier
    public double[] distributionForInstance(Instance instance){
        double[][] confidences = new double[numClasses][2];
        
        for(int classe=0;classe<numClasses;classe++){
            BayesianLogisticRegression classifBLR = classifieres.get(classe);
            double[] confsClasse = new double[2];
            try{
                confsClasse = classifBLR.distributionForInstance(instance);
            }catch(Exception e){
                System.err.println("Error when classifying a test instance");
                e.printStackTrace();
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
    
    public void setNumFolds(int numFolds){
        this.numFolds = numFolds;
        
    }
    
    public void setTolerance(double tolerance){
        this.tolerance = tolerance;
    }
    
    public void setPrior(int prior){
        this.prior = prior;
    }
    
    public int getMaxNumberIterations(){
        return this.maxNumberInterations;
    }
    
    public int getNumFolds(){
        return this.numFolds;
    }
    
    public double getTolerance(){
        return this.tolerance;
    }
    
    public int getPrior(){
        return this.prior;
    }

    @Override
    public Capabilities getCapabilities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
