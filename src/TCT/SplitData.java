/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCT;

import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_Base;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_Multiview;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_OneClass;
import java.util.ArrayList;
import java.util.Random;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author rafael
 */
public class SplitData {
    
    /*Function to split text collection into train (labeled) and test (unlabeled) sets. Test data are used as unlabeled data in transductive learning and for evaluation. 
      numInstPerClass instances for each class are selected as labeled examples.*/
    public static void splitTrainTestTransductive(TransductiveConfiguration_Base configuration, Instances data, Instances dataTrain, Instances dataTest, double numInstPerClass){
        int numClasses = data.numClasses();
        int[] totalInstClass = new int[numClasses];
        int[] instPerClass = new int[numClasses];
        int[] instChosenByClass = new int[numClasses];
        for(int classe=0;classe<numClasses;classe++){
            totalInstClass[classe] = 0;
            instPerClass[classe] = 0;
            instChosenByClass[classe] = 0;
        }
        
        if(configuration.isPorcentage() == true){
            for(int inst=0;inst<data.numInstances();inst++){
                Instance instance = data.instance(inst);
                int classe = (int)instance.classValue();
                int value = totalInstClass[classe];
                value++;
                totalInstClass[classe] = value;
            }    
        }
        
        if(configuration.isPorcentage() == false){
            for(int classe=0;classe<numClasses;classe++){
                instPerClass[classe] = (int)numInstPerClass;
            }    
        }else{
            for(int classe=0;classe<numClasses;classe++){
                double value = totalInstClass[classe] * ((double)numInstPerClass/(double)100);
                if(value < 1){
                    value = 1;
                }
                instPerClass[classe] = (int)value;
            }
        }
        
        for(int inst=0;inst<data.numInstances();inst++){
            Instance instance = data.instance(inst);
            int classe = (int)instance.classValue();
            int value = instChosenByClass[classe];
            value++;
            if(value > instPerClass[classe]){
                dataTest.add(instance);
            }else{
                dataTrain.add(instance);
                instChosenByClass[classe] = value;
            }
        }
    }
    
    public static void splitViewsTransductive(TransductiveConfiguration_Multiview configuration, Instances data, ArrayList<Instances> trainViews, ArrayList<Instances> testViews, double numInstPerClass, int indRep, int numViews){
        
        int numTerms = data.numAttributes() - 2;
        int numTermsPerView = numTerms / numViews;
       
        int numClasses = data.numClasses();
        int[] totalInstClass = new int[numClasses];
        int[] instPerClass = new int[numClasses];
        int[] instChosenByClass = new int[numClasses];
        
        if(configuration.isPorcentage() == true){
            for(int inst=0;inst<data.numInstances();inst++){
                Instance instance = data.instance(inst);
                int classe = (int)instance.classValue();
                int value = totalInstClass[classe];
                value++;
                totalInstClass[classe] = value;
            }    
        }
        
        if(configuration.isPorcentage() == false){
            for(int classe=0;classe<numClasses;classe++){
                instPerClass[classe] = (int)numInstPerClass;
            }    
        }else{
            for(int classe=0;classe<numClasses;classe++){
                double value = totalInstClass[classe] * ((double)numInstPerClass/(double)100);
                if(value < 1){
                    value = 1;
                }
                instPerClass[classe] = (int)value;
            }
        }
        
        for(int inst=0;inst<data.numInstances();inst++){
            Instance instance = data.instance(inst);
            int classe = (int)instance.classValue();
            int value = instChosenByClass[classe];
            value++;
            if(value > instPerClass[classe]){
                for(int vis=0;vis<numViews;vis++){
                    testViews.get(vis).add(instance);
                }
            }else{
                for(int vis=0;vis<numViews;vis++){
                    trainViews.get(vis).add(instance);
                }
                instChosenByClass[classe] = value;
            }
        }
        
        Random random = new Random(indRep);
        for(int term=0;term<numTerms;term++){
            int randomAtrView = random.nextInt(numViews);
            boolean exit = false;
            while(exit == true){
                int numTermsCurrentView = trainViews.get(randomAtrView).numAttributes() - 1;
                if(numTermsCurrentView == numTermsPerView){
                    randomAtrView++;
                    if(randomAtrView >= (numViews - 1)){
                        randomAtrView = 0;
                    }
                }else{
                    exit = true;
                }
            }
            String nameAtr = data.attribute(term).name();
            for(int vis=0;vis<numViews;vis++){
                if(randomAtrView == vis){
                    break;
                }
                Attribute att = trainViews.get(vis).attribute(nameAtr);
                int index = att.index();
                trainViews.get(vis).deleteAttributeAt(index);
                testViews.get(vis).deleteAttributeAt(index);
            }
        }
        
    }
    
    public static boolean splitTrainTestSupervisedOneClass(Instances dataOriginal, Instances dataTrain, Instances dataTest, int classe, int fold) {

        Instances target = new Instances(dataOriginal, 0);
        Instances targetTest = new Instances(dataOriginal, 0);

        for (int inst = 0; inst < dataOriginal.numInstances(); inst++) {

            if (dataOriginal.instance(inst).classValue() == classe) {
                target.add(dataOriginal.instance(inst));
            } else {
                dataTest.add(dataOriginal.instance(inst));
            }
        }

        if(target.numInstances() <= 2){
            return false;
        }
        
        Instances dataTrainTarget = null;
        if(target.numInstances() >= 10){
            targetTest = target.testCV(10, fold);
            dataTrainTarget = target.trainCV(10, fold);
        }else{ 
            targetTest = target.testCV(target.numInstances(), fold % target.numInstances());
            dataTrainTarget = target.trainCV(target.numInstances(), fold % target.numInstances());
        } 

        for (int inst = 0; inst < dataTrainTarget.numInstances(); inst++) {
            dataTrain.add(dataTrainTarget.instance(inst));
        }

        for (int inst = 0; inst < targetTest.numInstances(); inst++) {
            dataTest.add(targetTest.instance(inst));
        }
        
        return true;
    }
    
    public static boolean splitTrainTestTransductiveOneClass(boolean porcentage, Instances data, Instances dataTrain, Instances dataTest, double numInstPerClass, int idClasse){
        int numClasses = data.numClasses();
        int totalInstClass = 0;
        int instPerClass = 0;
        int instChosenByClass = 0;
        
        
        if(porcentage == true){
            for(int inst=0;inst<data.numInstances();inst++){
                Instance instance = data.instance(inst);
                int classe = (int)instance.classValue();
                if(classe == idClasse){
                    int value = totalInstClass;
                    value++;
                    totalInstClass = value;    
                }
            }    
        }
        
        if(porcentage == false){
            for(int classe=0;classe<numClasses;classe++){
                instPerClass = (int)numInstPerClass;
            }    
        }else{
            for(int classe=0;classe<numClasses;classe++){
                double value = totalInstClass * ((double)numInstPerClass/(double)100);
                if(value < 1){
                    value = 1;
                }
                instPerClass = (int)value;
            }
        }
        
        for(int inst=0;inst<data.numInstances();inst++){
            Instance instance = data.instance(inst);
            int classe = (int)instance.classValue();
            if(classe == idClasse){
                int value = instChosenByClass;
                value++;
                if(value > instPerClass){
                    dataTest.add(instance);
                }else{
                    dataTrain.add(instance);
                    instChosenByClass = value;
                }    
            }else{
                dataTest.add(instance);
            }
            
        }
        
        if(instChosenByClass <= instPerClass){
            return false;
        }
        
        return true;
    }
    
    
    
}
