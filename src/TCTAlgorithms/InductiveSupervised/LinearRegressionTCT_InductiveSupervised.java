//*********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of the regularized version of Linear 
//              Least Squares Fit (LLSF) algorithm (Zhang & Oles, 2001; Li & Yang, 
//              2003) for multiclass classification. We used one-vs-all strategy to 
//              perform multiclass classification. We considered the 
//              weka.classifiers.functions.LinearRegression to induce a 
//              classification model for each class. 
// References:  - T. Zhang and F. J. Oles. Text categorization based on 
//              regularized linear classification methods. Inf. Retr., 4(1):5–31, 
//              April 2001.
//              - F. Li and Y. Yang. A loss function analysis for
//              classification methods in text categorization. In Proceeding 
//              of the International Conference on Machine Learning, pages 472–479.
//              AAAI Press, 2003.
//*********************************************************************************

package TCTAlgorithms.InductiveSupervised;

import java.util.ArrayList;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class LinearRegressionTCT_InductiveSupervised extends InductiveSupervisedClassifier{
    
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    
    private double ridge; // Ridge parameter    
    
    ArrayList<LinearRegression> classifieres;
    
    @Override
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes();
        
        // Creating a single classifier per class
        classifieres = new ArrayList<LinearRegression>();
        for(int classe=0;classe<numClasses;classe++){
            LinearRegression classifLR = new LinearRegression();
            classifLR.setRidge(this.getRidge());
            classifLR.setAttributeSelectionMethod(new SelectedTag(LinearRegression.SELECTION_NONE, LinearRegression.TAGS_SELECTION));
            classifieres.add(classifLR);
        }
        
        // Generating a binary dataset for each class: positive label (1) for the current class and negative label (0) for others
        for(int classe=0;classe<numClasses;classe++){
            Instances dataClasse = new Instances(dataTrain,0);
            Attribute featureNewClass = new Attribute("att_class");
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
            LinearRegression classifLMS = classifieres.get(classe);
            try{
                dataClasse.deleteWithMissingClass();
                classifLMS.buildClassifier(dataClasse);
            }catch(Exception e){
                System.err.println("Error when building a classification model.");
                e.printStackTrace();
                //System.exit(0);
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
        int countClasses = 0;
        for(int classe=0;classe<numClasses;classe++){
            LinearRegression classifLMS = classifieres.get(classe);
            double[] confsClasse = new double[2];
            try{
                double valueClasse = classifLMS.classifyInstance(instance);
                if(valueClasse > 0){
                    confsClasse[1] = valueClasse;
                }
            }catch(Exception e){
                System.err.println("Error when classifying a test instance");
                e.printStackTrace();
                System.exit(0);
            }
            confidences[classe][0] = confsClasse[0];
            confidences[classe][1] = confsClasse[1];
            if(confsClasse[1] > 0){
                countClasses++;
            }
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
    
    public void setRidge(double ridge){
        this.ridge = ridge;
    }
    
    public double getRidge(){
        return this.ridge;
    }
}

