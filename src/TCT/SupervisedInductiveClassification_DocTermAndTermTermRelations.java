//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description:
// Description: Class to perform Inctive Supervised Lerning and Evaluation.
//              The classification models are generated considering Documen-
//              term and Term-Term relations.  
//*****************************************************************************

package TCT;


import TCTNetworkGeneration.TermNetworkGeneration;
import TCTAlgorithms.InductiveSupervised.IMHN_R_DocTermAndTermTermRelations_InductiveSupervised;
import TCTAlgorithms.InductiveSupervised.IMHN_C_DocTermAndTermTermRelations__InductiveSupervised;
import TCTConfigurations.SupervisedLearning.SupervisedInductiveConfiguration_DocTermAndTermTermRelations;
import TCTStructures.Neighbor;
import TCTParameters.SemiSupervisedLearning.Parameters_IMHN;
import TCTParameters.Parameters_TermNetwork;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class SupervisedInductiveClassification_DocTermAndTermTermRelations {
    
    /*Function to read the data (document-term matrix and term probabilities) and build networks with Document-term and Term-Term relations. */
    public static  void learning(SupervisedInductiveConfiguration_DocTermAndTermTermRelations configuration){
        
        File fileArff = new File(configuration.getArff());
        File fileProb = new File(configuration.getArqProb());
        File dirSaida = new File(configuration.getDirSaida());
        
        int numTerms = 0;
        
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
        Instances dataOriginal = null;
        int numClasses = 0;
        
        try{
            long begin = System.currentTimeMillis();
            ConverterUtils.DataSource trainSource = new ConverterUtils.DataSource(fileArff.getAbsolutePath().toString()); //Carregando arquivo de Dados
            dataOriginal = trainSource.getDataSet();

            long end = System.currentTimeMillis();
            System.out.println("Tempo de carregamento: " + ((end - begin) / (double)1000));

            Attribute classAtt = null;
            numTerms = dataOriginal.numAttributes()-1;
            classAtt = dataOriginal.attribute(numTerms); //Setting the last feature as class
            dataOriginal.setClass(classAtt);
            numClasses = classAtt.numValues();

            for(int j=0;j<numClasses;j++){
                System.out.println(j + ": " + classAtt.value(j));
            }
            
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
        
        StringBuilder outputFile = new StringBuilder();
        outputFile.append(configuration.getDirSaida());
        outputFile.append("/");
        outputFile.append(dataOriginal.relationName());
        outputFile.append("_InductiveSupervised_DTandTT_");
        
        try{
            
            if(configuration.isSupportNetwork()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append("Support");
                outputFilePar.append("_");
                Parameters_TermNetwork parametersSupportNetwork = configuration.getParametersSupportNetwork();
                if(parametersSupportNetwork.getThresholdNetwork()){
                    for(int lim=0;lim<parametersSupportNetwork.getThresholds().size();lim++){
                        double threshold = parametersSupportNetwork.getThreshold(lim);
                        //System.out.println("Generating term-term relations: Support - Threshold " + threshold);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkThreshold(fileProb,numTerms,threshold,parametersSupportNetwork.getRelative());
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
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
                        int k = parametersSupportNetwork.getK(topK);
                        //System.out.println("Generating term-term relations: Support - TopK " + k);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateSupportNetworkTopK(fileProb,numTerms,k);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
                        double threshold = parametersMutualInformationNetwork.getThreshold(lim);
                        //System.out.println("Generating term-term relations:  Mutual Information - Threshold " + threshold);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkThreshold(fileProb,numTerms,threshold,parametersMutualInformationNetwork.getRelative());
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" +threshold + "_",dataOriginal,numClasses);
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
                        int k = parametersMutualInformationNetwork.getK(topK);
                        //System.out.println("Generating term-term relations: Mutual Information - topK " + k);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateMutualInformationNetworkTopK(fileProb,numTerms,k);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
                        double threshold = parametersKappaNetwork.getThreshold(lim);
                        //System.out.println("Generating term-term relations:  Kappa - Threshold " + threshold);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkThreshold(fileProb,numTerms,threshold,parametersKappaNetwork.getRelative());
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" +threshold + "_",dataOriginal,numClasses);
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
                        int k = parametersKappaNetwork.getK(topK);
                        //System.out.println("Generating term-term relations: Kappa - TopK " + k);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateKappaNetworkTopK(fileProb,numTerms,k);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
                        double threshold = parametersShapiroNetwork.getThreshold(lim);
                        //System.out.println("Generating term-term relations: Shapiro - Threshold " + threshold);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkThreshold(fileProb,numTerms,threshold,parametersShapiroNetwork.getRelative());
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
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
                        int k = parametersShapiroNetwork.getK(topK);
                        //System.out.println("Generating term-term relations: Shapiro - TopK " + k);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateShapiroNetworkTopK(fileProb,numTerms,k);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms, outputFile.toString(), outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
                        double threshold = parametersYulesQNetwork.getThreshold(lim);
                        //System.out.println("Generating term-term relations: Yule\'s Q - Threshold " + threshold);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkThreshold(fileProb,numTerms,threshold,parametersYulesQNetwork.getRelative());
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_Threshold_" + threshold + "_",dataOriginal,numClasses);
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
                        int k = parametersYulesQNetwork.getK(topK);
                        //System.out.println("Generating term-term relations: Yule\'s Q - TopK " + k);
                        Neighbor[] adjacencyListTerms = TermNetworkGeneration.GenerateYulesQNetworkTopK(fileProb,numTerms,k);
                        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
                        learning(configuration,adjacencyListTerms,outputFile.toString(),outputFilePar.toString() + "_TopK_" + k + "_",dataOriginal,numClasses);
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
    
    //Function to set the parameters of supervised inductive learning algorithms
    public static void learning(SupervisedInductiveConfiguration_DocTermAndTermTermRelations configuration, Neighbor[] adjacencyListTerms, String output, String output2, Instances dataOriginal,int numClasses){
        
         //Gerando as networks
        ExecutorService threads = Executors.newFixedThreadPool(configuration.getNumThreads());
        
        try{
            if(configuration.isIMHN()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(output);
                outputFilePar.append("IMHN_DD_TT_");
                outputFilePar.append(output2);
                Parameters_IMHN parametersIMHN = configuration.getParameters_IMHN();
                for(int error=0;error<parametersIMHN.getErrors().size();error++){
                    for(int apr=0;apr<parametersIMHN.getErrorCorrectionRates().size();apr++){
                        //System.out.println("Classification Algorithm: IMHN");
                        //System.err.println("Error Mínimo: " + parametersIMHN.getError(error));
                        //System.out.println("Learning Rate: " + parametersIMHN.getErrorCorrectionRate(apr));
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString() + "_");
                        outputFilePar2.append(parametersIMHN.getErrorCorrectionRate(apr) + "_");
                        outputFilePar2.append(parametersIMHN.getError(error) + "_");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];                    
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            for(int fold=0;fold<configuration.getNumFolds();fold++){
                                IMHN_C_DocTermAndTermTermRelations__InductiveSupervised classifIMHN = new IMHN_C_DocTermAndTermTermRelations__InductiveSupervised();
                                classifIMHN.setAdjacencyListTerms(adjacencyListTerms);
                                classifIMHN.setErrorCorrectionRate(parametersIMHN.getErrorCorrectionRate(apr));
                                classifIMHN.setMinError(parametersIMHN.getError(error));
                                classifIMHN.setmaxNumberLocalIterations(parametersIMHN.getMaxNumberIterationsLocal());
                                classifIMHN.setmaxNumberGlobalIterations(parametersIMHN.getMaxNumberIterationsGlobal());
                                classifiers[rep][fold] = classifIMHN;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);    
                    }
                }    
            }
            if(configuration.isIMHN2()){
                StringBuilder outputFilePar = new StringBuilder();
                outputFilePar.append(output);
                outputFilePar.append("IMHN2_DD_TT_");
                outputFilePar.append(output2);
                Parameters_IMHN parametersIMHN2 = configuration.getParameters_IMHN2();
                for(int error=0;error<parametersIMHN2.getErrors().size();error++){
                    for(int apr=0;apr<parametersIMHN2.getErrorCorrectionRates().size();apr++){
                        //System.out.println("Classification Algorithm: IMHN2");
                        //System.err.println("Error Mínimo: " + parametersIMHN2.getError(error));
                        //System.out.println("Learning Rate: " + parametersIMHN2.getErrorCorrectionRate(apr));
                        StringBuilder outputFilePar2 = new StringBuilder();
                        outputFilePar2.append(outputFilePar.toString() + "_");
                        outputFilePar2.append(parametersIMHN2.getErrorCorrectionRate(apr) + "_");
                        outputFilePar2.append(parametersIMHN2.getError(error) + "_");
                        Classifier[][] classifiers = new Classifier[configuration.getNumReps()][configuration.getNumFolds()];                    
                        for(int rep=0;rep<configuration.getNumReps();rep++){
                            for(int fold=0;fold<configuration.getNumFolds();fold++){
                                IMHN_R_DocTermAndTermTermRelations_InductiveSupervised classifIMHN = new IMHN_R_DocTermAndTermTermRelations_InductiveSupervised();
                                classifIMHN.setAdjacencyListTerms(adjacencyListTerms);
                                classifIMHN.setErrorCorrectionRate(parametersIMHN2.getErrorCorrectionRate(apr));
                                classifIMHN.setMinError(parametersIMHN2.getError(error));
                                classifIMHN.setmaxNumberLocalIterations(parametersIMHN2.getMaxNumberIterationsLocal());
                                classifIMHN.setmaxNumberGlobalIterations(parametersIMHN2.getMaxNumberIterationsGlobal());
                                classifiers[rep][fold] = classifIMHN;
                            }
                        }
                        learning(configuration, threads, classifiers, dataOriginal, outputFilePar2.toString(), numClasses);    
                    }
                }    
            }
            
            
            
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
    
    //Function to run and evaluate supervised learning
    public static void learning(final SupervisedInductiveConfiguration_DocTermAndTermTermRelations configuration, final ExecutorService threads, final Classifier[][] classifiers, Instances dataOriginal, String outputFile, final int numClasses){
        
        try{
            
            final Results results;
            
            final File output = new File(outputFile + configuration.getNumReps() + "_" + configuration.getNumFolds());
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
                results = new Results(output, configuration.getNumReps(), configuration.getNumFolds(), "InductiveSupervised");
            }
            
            //System.out.println("Output: " + output.getAbsolutePath());
            
            for(int rep=0;rep<configuration.getNumReps();rep++){
                
                final int numRep = rep;
                
                final Instances data = new Instances(dataOriginal);
                data.setClass(dataOriginal.attribute(dataOriginal.numAttributes()-1));
                data.randomize(new Random(rep));
                
                for(int fold=0; fold<configuration.getNumFolds();fold++){
                    
                    if(results.getComplete(rep, fold) == true){
                        continue;
                    }
                    
                    final int numFold = fold;
                    
                    
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Integer[][] confusionMatrix = new Integer[numClasses][numClasses]; //matriz de confusão
                            for(int class1=0;class1<numClasses;class1++){//inicializando a matriz
                                for(int class2=0;class2<numClasses;class2++){
                                    confusionMatrix[class1][class2] = 0;
                                }
                            }
                            
                            
                            Instances dataTrain = data.trainCV(configuration.getNumFolds(), numFold);
                            Instances dataTest = data.testCV(configuration.getNumFolds(),numFold);

                            long begin = System.currentTimeMillis();
                            try{
                                classifiers[numRep][numFold].buildClassifier(dataTrain);
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
                            
                            long end  = System.currentTimeMillis();
                            results.setBuildingTime(numRep, numFold, end - begin);

                            begin = System.currentTimeMillis();
                            Evaluations.SupervisedInductiveEvaluation(classifiers[numRep][numFold], dataTest, confusionMatrix);
                            end = System.currentTimeMillis();
                            
                            results.setClassificationTime(numRep, numFold, end - begin);

                            
                            results.computeEvaluationMeasures(confusionMatrix, numClasses, numRep, numFold);
                            
                            results.setComplete(numRep, numFold, true);
  
                        }
                    });
                    
                    threads.execute(thread);
                }    
            }
            
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
    

}
