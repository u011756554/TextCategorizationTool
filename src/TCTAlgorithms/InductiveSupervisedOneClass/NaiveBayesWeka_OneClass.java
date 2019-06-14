/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTAlgorithms.InductiveSupervisedOneClass.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class NaiveBayesWeka_OneClass extends OneClassSupervisedClassifier{
    
    NaiveBayes classifier;
    
    @Override
    public void buildClassifier(Instances dataTrain) throws Exception{
        classifier = new NaiveBayes();
        
        classifier.buildClassifier(dataTrain);
        
    }
    
    @Override
    public double classifyInstance(Instance test) {
        double score = getScore(test);
        return (score >= this.getThreshold() ? 0 : 1);
    }

    @Override
    public double getScore(Instance test) {
        double[] confidences = null;
        try {
            confidences = classifier.distributionForInstance(test);
        } catch (Exception ex) {
            System.err.println("An error occurred when try to get an score of an instance.");
            ex.printStackTrace();
        }
        return confidences[0];
    }

    
    
}
