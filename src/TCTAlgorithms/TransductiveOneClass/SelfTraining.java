/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.TransductiveOneClass;

import TCTAlgorithms.InductiveSupervisedOneClass.OneClassSupervisedClassifier;
import TCTAlgorithms.Transductive.TCHN_DocTerm_Transductive;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTStructures.IndexValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class SelfTraining extends TransductiveLearnerOneClass{
    
    private OneClassSupervisedClassifier supervisedClassifier;
    private TransductiveLearner transductiveClassifier;
    
    private int numIterations;
    private int numAddedLabeledSet;

    public SelfTraining(int numIterations, int numAddedLabeledSet) {
        this.numIterations = numIterations;
        this.numAddedLabeledSet = numAddedLabeledSet;
    }
    
    @Override
    public void buildClassifier(Instances dataPositive, Instances dataUnlabeled) {
       
        Instances newDataTrain = new Instances(dataPositive,0);
        Instances newDataUnlabeled = new Instances(dataUnlabeled,0);
        
        
        fUnlabeledDocs = new double[dataUnlabeled.numInstances()][2];
        
        int numIt = 1;
        
        HashSet<Integer> hashUnlabeled = new HashSet<Integer>();
        for(int docUnl = 0; docUnl < dataUnlabeled.numInstances(); docUnl++){
            hashUnlabeled.add(docUnl);
        }
        
        //Building new dataTrain
        for(int doc=0;doc<dataPositive.numInstances();doc++){
            newDataTrain.add((Instance)dataPositive.instance(doc).copy());
            newDataTrain.instance(newDataTrain.numInstances()-1).setClassValue(0.0);
        }
        
        //Getting the most conffident positive examples
        while(numIt <= numIterations){
            
            try{
                supervisedClassifier.buildClassifier(newDataTrain);
                
                //Building a hashset to keep the ids of unlabeled data;
                ArrayList<IndexValue> scores = new ArrayList<IndexValue>();
                for(int idDoc : hashUnlabeled){
                    
                    double score = supervisedClassifier.getScore(dataUnlabeled.instance(idDoc));
                    scores.add(new IndexValue(idDoc,score));
                }
                
                Collections.sort(scores);
                
                for(int i=0;i<this.numAddedLabeledSet;i++){
                    int idDoc = scores.get(i).getIndex();
                    double score = scores.get(i).getValue();
                    newDataTrain.add((Instance)dataUnlabeled.instance(idDoc).copy());
                    newDataTrain.instance(newDataTrain.numInstances()-1).setClassValue(0.0);
                    hashUnlabeled.remove(idDoc);
                    fUnlabeledDocs[idDoc][0] = 1;
                    fUnlabeledDocs[idDoc][1] = 0;
                }
                
                
            }catch(Exception e){
                System.err.println("Erro ao construir um modelo de classificação");
                e.printStackTrace();
                System.exit(numIt);
            }
            
            numIt++;
        }
        
        //Getting the most confiddent negative examples
        try{
            supervisedClassifier.buildClassifier(newDataTrain);
            ArrayList<IndexValue> scores = new ArrayList<IndexValue>();
            for(int idDoc : hashUnlabeled){
                double score = supervisedClassifier.classifyInstance(dataUnlabeled.instance(idDoc));
                scores.add(new IndexValue(idDoc,score));
            }
            
            
            int numSizeNewDataTrain =     newDataTrain.numInstances();
            for(int i=0; (i<numSizeNewDataTrain) && (scores.size()>0);i++){
            //for(int i=0; (i<this.numAddedLabeledSet*numIterations) || (scores.size()==0);i++){
                int idDoc = scores.get((scores.size()-1)).getIndex();
                newDataTrain.add((Instance)dataUnlabeled.instance(idDoc).copy());
                newDataTrain.instance(newDataTrain.numInstances()-1).setClassValue(1.0);
                hashUnlabeled.remove(idDoc);
                scores.remove(scores.size()-1);
                fUnlabeledDocs[idDoc][0] = 0;
                fUnlabeledDocs[idDoc][1] = 1;
            }
            
        }catch(Exception e){
            System.err.println("Erro ao construir um modelo de classificação");
            e.printStackTrace();
            System.exit(numIt);
        }
        
        //Building the unlabeled set
        ArrayList<Integer> unlabeledIds = new ArrayList<Integer>();
        for(int idDoc : hashUnlabeled){
            newDataUnlabeled.add((Instance)dataUnlabeled.instance(idDoc).copy());
            unlabeledIds.add(idDoc);
        }

        //Calling a Transductive Classifier
        transductiveClassifier.buildClassifier(newDataTrain, newDataUnlabeled);
        
        for(int doc = 0;doc<unlabeledIds.size();doc++){
            int idDoc = unlabeledIds.get(doc);
            fUnlabeledDocs[idDoc][0] =  transductiveClassifier.fUnlabeledDocs[doc][0];
            fUnlabeledDocs[idDoc][1] =  transductiveClassifier.fUnlabeledDocs[doc][1];
        }
        
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public int getNumAddedLabeledSet() {
        return numAddedLabeledSet;
    }

    public void setNumAddedLabeledSet(int numAddedLabeledSet) {
        this.numAddedLabeledSet = numAddedLabeledSet;
    }

    public OneClassSupervisedClassifier getSupervisedClassifier() {
        return supervisedClassifier;
    }

    public void setSupervisedClassifier(OneClassSupervisedClassifier supervisedClassifier) {
        this.supervisedClassifier = supervisedClassifier;
    }

    public TransductiveLearner getTransductiveClassifier() {
        return transductiveClassifier;
    }

    public void setTransductiveClassifier(TransductiveLearner transductiveClassifier) {
        this.transductiveClassifier = transductiveClassifier;
    }
    
}
