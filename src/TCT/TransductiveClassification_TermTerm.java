//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: Class to perform Transductive Lerning and Evaluation.
// Description: Class to perform Inctive Semi-Supervised Lerning and Evaluation.
//              The algoritms are based on networks composed by term-term
//              relations.
//*****************************************************************************

package TCT;

import TCTNetworkGeneration.TermNetworkGeneration;
import TCTParameters.Parameters_TermNetwork;
import TCTParameters.SemiSupervisedLearning.Parameters_GFHF;
import TCTParameters.SemiSupervisedLearning.Parameters_LLGC;
import TCTAlgorithms.Transductive.TransductiveLearner;
import TCTAlgorithms.Transductive.GFHF_TermTerm_Transductive;
import TCTAlgorithms.Transductive.TCTN_Transductive;
import TCTConfigurations.TransductiveLearning.TransductiveConfiguration_TermTermRelations;
import TCTStructures.Neighbor;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class TransductiveClassification_TermTerm {
    
    /*Function to read the data (document-term matrix and term probabilities) and build networks with term-term and relations. */
    public static void learning(final TransductiveConfiguration_TermTermRelations configuration){
        
        File fileArff = new File(configuration.getArff());
        final File fileProb = new File(configuration.getArqProb());
        File dirSaida = new File(configuration.getDirSaida());
        
        if(!fileArff.getAbsolutePath().endsWith(".arff")){
            System.out.println("Invalid ARF file");
            return;
        }
        System.out.println(fileArff.getAbsolutePath());
        if(!fileProb.getAbsolutePath().endsWith(".prb")){
            System.out.println("Invalid probability file");
            return;
        }
        if(!dirSaida.exists()){
            System.out.println("Output directory does not exist");
            return;
        }
        
        System.out.println(fileArff.getAbsolutePath());
        System.out.println("Loading ARFF file");
        
        try{ 
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(fileArff.getAbsolutePath().toString()); //Carregando arquivo de Dados
            final Instances dataOriginal = trainSource.getDataSet();

            Attribute classAtt = null;
            final int numTerms = dataOriginal.numAttributes()-1;
            classAtt = dataOriginal.attribute(numTerms); //Setting the last feature as class
            dataOriginal.setClass(classAtt);
            final int numClasses = classAtt.numValues();

            for(int j=0;j<numClasses;j++){
                System.out.println(j + ": " + classAtt.value(j));
            }
            
            final StringBuilder outputFile = new StringBuilder();
            outputFile.append(configuration.getDirSaida());
            outputFile.append("/");
            outputFile.append(dataOriginal.relationName());
            outputFile.append("_Transductive_TT_");
            
            //Generating term-term relations
            if(configuration.isSupportNetwork()){
                final StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Support");
                outputFilePar.append("_");
                final Parameters_TermNetwork parametersSupportNetwork = configuration.getParametersSupportNetwork();
                if(parametersSupportNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersSupportNetwork.getThresholds().size();lim++){
                        final double threshold = parametersSupportNetwork.getThreshold(lim);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkThreshold(fileProb,numTerms,threshold,parametersSupportNetwork.getRelative());
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }    
                }
                if(parametersSupportNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersSupportNetwork.getKs().size();topK++){
                        final int k = parametersSupportNetwork.getK(topK);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkTopK(fileProb,numTerms,k);
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }
                }

            }
            if(configuration.isMutualInformationNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("MutualInformation");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersMutualInformationNetwork = configuration.getParametersMutualInformationNetwork();
                if(parametersMutualInformationNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersMutualInformationNetwork.getThresholds().size();lim++){
                        final double threshold = parametersMutualInformationNetwork.getThreshold(lim);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkThreshold(fileProb,numTerms,threshold,parametersMutualInformationNetwork.getRelative());
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" +threshold + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }    
                }
                if(parametersMutualInformationNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersMutualInformationNetwork.getKs().size();topK++){
                        final int k = parametersMutualInformationNetwork.getK(topK);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkTopK(fileProb,numTerms,k);
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }
                }
            }
            if(configuration.isKappaNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Kappa");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersKappaNetwork = configuration.getParametersKappaNetwork();
                if(parametersKappaNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersKappaNetwork.getThresholds().size();lim++){
                        final double threshold = parametersKappaNetwork.getThreshold(lim);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkThreshold(fileProb,numTerms,threshold,parametersKappaNetwork.getRelative());
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" +threshold + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }    
                }
                if(parametersKappaNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersKappaNetwork.getKs().size();topK++){
                        final int k = parametersKappaNetwork.getK(topK);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkTopK(fileProb,numTerms,k);
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }
                }    
            }
            if(configuration.isShapiroNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Shapiro");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersShapiroNetwork = configuration.getParametersShapiroNetwork();
                if(parametersShapiroNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersShapiroNetwork.getThresholds().size();lim++){
                        final double threshold = parametersShapiroNetwork.getThreshold(lim);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkThreshold(fileProb,numTerms,threshold,parametersShapiroNetwork.getRelative());
                        learning(configuration,threads,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }    
                }
                if(parametersShapiroNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersShapiroNetwork.getKs().size();topK++){
                        final int k = parametersShapiroNetwork.getK(topK);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkTopK(fileProb,numTerms,k);
                        learning(configuration,threads, adjacencyListTerms, outputFile.toString(), outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }
                }    
            }
            if(configuration.isYulesQNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Yule's Q");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersYulesQNetwork = configuration.getParametersYulesQNetwork();
                if(parametersYulesQNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersYulesQNetwork.getThresholds().size();lim++){
                        final double threshold = parametersYulesQNetwork.getThreshold(lim);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkThreshold(fileProb,numTerms,threshold,parametersYulesQNetwork.getRelative());
                        learning(configuration,threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }       
                }
                if(parametersYulesQNetwork.getNetworkTopK()){
                    for(int topK=0;topK<parametersYulesQNetwork.getKs().size();topK++){
                        final int k = parametersYulesQNetwork.getK(topK);
                        final ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkTopK(fileProb,numTerms,k);
                        learning(configuration, threads, adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
                        boolean exit = false;
                        threads.shutdown();
                        while(exit == false){
                            if(threads.isTerminated()){
                                exit = true;
                            }else{
                                Thread.sleep(1000);
                            }
                        }
                    }
                }
            }

            System.out.println("Process concluded successfully");
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            
        }catch(OutOfMemoryError e){
            configuration.getEmail().getContent().append("OutOfMemory!!!!");
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }catch(Exception e){
            System.err.println("Error when generating a classifier.");
            configuration.getEmail().getContent().append(e.getMessage());
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    //Function to set the parameters of semi-supervised inductive learning algorithms
    public static void learning(TransductiveConfiguration_TermTermRelations configuration, ExecutorService threads, Neighbor[] adjacencyListTerms, String output, String output2, Instances dataOriginal,int numClasses){
        
        for(int numEx=0;numEx<configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass().size();numEx++){
            double numLabeledInstances = configuration.getParametersNumLabeledInstancesPerClass().getNumLabeledInstancesPerClass(numEx);
            if(configuration.isLLGC()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(numLabeledInstances);
                outputFilePar.append("_");
                if(configuration.isPorcentage() == true){
                    outputFilePar.append("percentage");
                    outputFilePar.append("_");
                }else{
                    outputFilePar.append("real");
                    outputFilePar.append("_");
                }
                outputFilePar.append("LLGC_");
                Parameters_LLGC parametersLLGC = configuration.getParametersLLGC();
                for(int alpha=0;alpha<parametersLLGC.getAlphas().size();alpha++){
                    //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                    //System.out.println("Classification Algorithm: LLGC");
                    //System.out.println("Alpha: " + parametersLLGC.getAlpha(alpha));
                    StringBuilder outputFilePar2 = new StringBuilder();
                    outputFilePar2.append("_");
                    outputFilePar2.append(parametersLLGC.getAlpha(alpha));
                    TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                    for(int rep=0;rep<configuration.getNumReps();rep++){
                        TCTN_Transductive classifLLGC = new TCTN_Transductive();
                        classifLLGC.setMaxNumIterations(parametersLLGC.getMaxNumberIterations());
                        classifLLGC.setAlpha(parametersLLGC.getAlpha(alpha));
                        classifLLGC.setAdjacencyListTerms(adjacencyListTerms);
                        classifLLGC.setUse(0);
                        classifiers[rep] = classifLLGC;
                    }
                    learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar.toString() + output2 + outputFilePar2.toString(), numLabeledInstances, numClasses);    
                }    
            }

            if(configuration.isGFHF()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(numLabeledInstances);
                outputFilePar.append("_");
                if(configuration.isPorcentage() == true){
                    outputFilePar.append("percentage");
                    outputFilePar.append("_");
                }else{
                    outputFilePar.append("real");
                    outputFilePar.append("_");
                }
                outputFilePar.append("GFHF_");
                Parameters_GFHF parametersGFHF = configuration.getParametersGFHF();
                //System.out.println("Number of labeled intances per class: " + numLabeledInstances);
                //System.out.println("Classification Algorithm: GFHF");
                TransductiveLearner[] classifiers = new TransductiveLearner[configuration.getNumReps()];
                for(int rep=0;rep<configuration.getNumReps();rep++){
                    GFHF_TermTerm_Transductive classifGFHF = new GFHF_TermTerm_Transductive();
                    classifGFHF.setMaxNumIterations(parametersGFHF.getMaxNumberIterations());
                    classifGFHF.setAdjacencyListTerms(adjacencyListTerms);
                    classifGFHF.setUse(0);
                    classifiers[rep] = classifGFHF;
                }
                learning(configuration, threads, classifiers, dataOriginal, output + outputFilePar.toString() + output2, numLabeledInstances, numClasses);    
            }
        } 
    }
    
    //Function to run and evaluate transductive learning
    public static void learning(final TransductiveConfiguration_TermTermRelations configuration, final ExecutorService threads, final TransductiveLearner[] classifiers, final Instances dataOriginal, String outputFile, final double numInstPerClass, final int numClasses){
        
        try{
            
            final Results results;
            
            final File output = new File(outputFile);
            final File outputResult = new File(output.getAbsolutePath() + ".txt");
            if(outputResult.exists()){
                return;
            }
            final File outputTemp = new File(output.getAbsolutePath() + ".tmp");
            
            if(outputTemp.exists()){
                ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(output.getAbsolutePath() + ".tmp"));
                results = (Results)objInput.readObject();
                objInput.close();
            }else{
                results = new Results(output, configuration.getNumReps(), 1, "Transductive");
            }
            
            //System.out.println("Output: " + output.getAbsolutePath());
            
            for(int rep=0;rep<configuration.getNumReps();rep++){
                
                if(results.getComplete(rep, 0) == true){
                    continue;
                }
                
                final int numRep = rep;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; 
                        for(int class1=0;class1<numClasses;class1++){
                            for(int class2=0;class2<numClasses;class2++){
                                confusionMatrix[class1][class2] = 0;
                            }
                        }

                        Instances dataTrain = new Instances(dataOriginal,0);
                        Instances dataTest = new Instances(dataOriginal,0);

                        Instances data = new Instances(dataOriginal);
                        data.randomize(new Random(numRep));

                        SplitData.splitTrainTestTransductive(configuration, data, dataTrain, dataTest, numInstPerClass);

                        classifiers[numRep].setNumIterations(0);
                        long begin = System.currentTimeMillis();
                        try{
                            classifiers[numRep].buildClassifier(dataTrain, dataTest);
                        }catch(OutOfMemoryError e){
                            configuration.getEmail().getContent().append("OutOfMemory!!!!");
                            configuration.getEmail().send();
                            e.printStackTrace();
                            System.exit(0);
                        }catch(Exception e){
                            System.err.println("Error when generating a classifier.");
                            configuration.getEmail().getContent().append(e.getMessage());
                            configuration.getEmail().getContent().append(configuration.toString());
                            configuration.getEmail().send();
                            e.printStackTrace();
                            System.exit(0);
                        }
                        
                        long end  = System.currentTimeMillis();
                        results.setBuildingTime(numRep, 0, end - begin);
                        results.setNumIterations(numRep, 0, classifiers[numRep].getNumiterations());
                        begin = System.currentTimeMillis();
                        Evaluations.TransductiveEvaluation(classifiers[numRep], dataTest, confusionMatrix);
                        end = System.currentTimeMillis();
                        results.setClassificationTime(numRep, 0, end - begin);

                        results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, 0);
                        results.setComplete(numRep, 0, true);
                        
                    }
                });
                
                threads.execute(thread);
                
            }   
            
        }catch(OutOfMemoryError e){
            configuration.getEmail().getContent().append("OutOfMemory!!!!");
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }catch(Exception e){
            System.err.println("Error when generating a classifier.");
            configuration.getEmail().getContent().append(e.getMessage());
            configuration.getEmail().getContent().append(configuration.toString());
            configuration.getEmail().send();
            e.printStackTrace();
            System.exit(0);
        }
        
    }
    
}
