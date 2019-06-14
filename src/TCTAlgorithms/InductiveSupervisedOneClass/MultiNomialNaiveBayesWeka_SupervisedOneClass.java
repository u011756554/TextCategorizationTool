/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class MultiNomialNaiveBayesWeka_SupervisedOneClass extends OneClassSupervisedClassifier{
    NaiveBayesMultinomial classifier;
    
    @Override
    public void buildClassifier(Instances dataTrain) throws Exception{
        classifier = new NaiveBayesMultinomial();
        
        classifier.buildClassifier(dataTrain);
        
    }
    
    @Override
    public double classifyInstance(Instance test){
        double score = getScore(test);
        //System.out.println("Classe: " + test.classValue());
        //System.out.println("Score: " + score);
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
