//*********************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This is an implementation of Term Graph Model algorithm (Wang
//              et al., 2005).
// References: - W. Wang, D. B. Do, and X. Lin. Term graph model for text  
//               classification. In Proceeding of the International Conference 
//               on Advanced Data Mining and Applications, pages 19–30. Springer, 
//               2005.
//             - R. Mihalcea and P. Tarau. TextRank: Bringing order into texts.
//               In Proceeding of the Conference on Empirical Methods in Natural 
//               Language Processing, pages 404-411, 2004.
//*********************************************************************************

package TCTAlgorithms.InductiveSupervised;

import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import TCTStructures.NeighborHash;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import weka.core.Instance;
import weka.core.Instances;

public class TGM_InductiveSupervised extends InductiveSupervisedClassifier{
    
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    
    private double minSup; // Minimum support value to generate term-term relations 
    private double dampingFactor; // Damping factor used in PageRank algorithms. 
    private double minimumAverageDifference; // Minimum difference of PageRank scores into two consecutive iterations. This is used as stopping criterion. 
    private int numMaxIterations; // Maximum number of iterations. This is used as stopping criterion.
    
    private Neighbor[] rankingsFinal; // Stores the ranking of terms for each class 
       
    @Override
    //Function to perform inductive supervised learning
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        double[][] scores = new double[numClasses][numTerms]; 
        
        rankingsFinal = new Neighbor[numClasses];
        
        for(int classe=0;classe<numClasses;classe++){
            rankingsFinal[classe] = new Neighbor();
        }
        
        // Computing PageRank Scores for each class
        for(int classe=0;classe<numClasses;classe++){
            
            Neighbor rankings = new Neighbor();
            
            NeighborHash[] listTermClass = new NeighborHash[numTerms]; // List of terms for each class
            NeighborHash[] listaSupportTermClass = new NeighborHash[numTerms]; // Support value of term for each class
            for(int term=0;term<numTerms;term++){
                listTermClass[term] = new NeighborHash(); 
                listaSupportTermClass[term] = new NeighborHash();
            }
            
            double[] degreeTerm = new double[numTerms]; 
            int numInstClasse = 0; // Number of instances for each class
            
            for(int inst=0;inst<numTrain;inst++){
                int classInst = (int)dataTrain.instance(inst).classValue();
                if(classe == classInst){    
                    numInstClasse++;
                    for(int term=0;term<numTerms;term++){
                        if(dataTrain.instance(inst).value(term) > 0){
                            listTermClass[term].AddNeighbor(inst, dataTrain.instance(inst).value(term));
                        }
                    }    
                }
            }
            
            int numTermsNetwork=0;
        
            // Generating a network with term-term relations
            for(int term1=0;term1<numTerms;term1++){
                Set<Integer> docsTerm1 = listTermClass[term1].getNeighbors().keySet();
                for(int term2=(term1+1);term2<numTerms;term2++){
                    double count = 0;
                    for(Integer indTerm1 : docsTerm1){
                        if(listTermClass[term2].getNeighbors().containsKey(indTerm1)){
                            count++;
                        }
                    }
                    double sup = (double)count / (double)numInstClasse;
                    if(sup > minSup){
                        listaSupportTermClass[term1].AddNeighbor(term2, sup);
                        degreeTerm[term1] = degreeTerm[term1] + sup;
                        listaSupportTermClass[term2].AddNeighbor(term1, sup);
                        degreeTerm[term2] = degreeTerm[term2] + sup;
                    }
                }
                if(degreeTerm[term1]>0){
                    numTermsNetwork++;
                }
            }
            
            // Assigning scores to terms though PageRank
            // We used the version of PageRank for weighted graphs presented by (Mihalcea & Tarau, 2004)
            boolean exit = false;
            int numItPageRank = 0;
            double scoreIni = (1 - dampingFactor) / numTermsNetwork;
            while(exit == false){
                double averageDifference = 0;
                for(int term1=0;term1<numTerms;term1++){
                    Set<Integer> docsTerm1 = listaSupportTermClass[term1].getNeighbors().keySet();
                    if(docsTerm1.size() == 0){
                        continue;
                    }
                    double scoreTerm = scoreIni;
                    for(Integer indTerm1 : docsTerm1){
                        scoreTerm +=  dampingFactor * listaSupportTermClass[term1].getNeighbor(indTerm1) * scores[classe][indTerm1] / degreeTerm[indTerm1];
                    }
                    averageDifference = Math.abs(scoreTerm - scores[classe][term1]);
                    scores[classe][term1] = scoreTerm;
                }
                
                averageDifference = averageDifference / (double)numTermsNetwork;
                numItPageRank++;
                if((numItPageRank > numMaxIterations) || (averageDifference < this.getAvgMinDifference())){
                    exit = true;
                }
            }
            
            // Ranking terms according to PageRanks scores    
            exit = false;
            while(exit == false){
                int ind = -1;
                double highest = -300000000;
                for(int term2=0;term2<numTerms;term2++){
                    if(scores[classe][term2] > highest){
                        ind = term2;
                        highest = scores[classe][term2];
                    }
                }
                if((highest > 0) && (ind != -1)){
                    scores[classe][ind] = 0;
                    IndexValue indVal = new IndexValue();
                    indVal.index = ind;
                    indVal.value = highest;
                    rankings.AddNeighbor(indVal);
                }else{
                    break;
                }
            }
            ArrayList<IndexValue> neighbors = rankings.getNeighbors();
            for(int term1=0;term1<neighbors.size();term1++){
                double valueTerm1 = neighbors.get(term1).value;
                int counter = 0;
                int accumulator = 0;
                for(int term2=term1+1;term2<neighbors.size();term2++){
                    double valueTerm2 = neighbors.get(term2).value;
                    if(valueTerm1 == valueTerm2){
                        counter++;
                        accumulator += counter;
                    }else{
                        break;
                    }
                }
                if(counter == 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = neighbors.get(term1).index;
                    indVal.value = term1;
                    rankingsFinal[classe].AddNeighbor(indVal);
                }else{
                    double rank = (term1 + accumulator)/(double)counter;
                    for(int term2=0;term2<counter;term2++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = neighbors.get(term1 + 1 + term2).index;
                        indVal.value = rank;
                        rankingsFinal[classe].AddNeighbor(indVal);
                    }
                    term1 += counter;
                    term1 --;
                }
            }
            
        }
        
    }
    
    @Override
    // Function to classify a new instance (hard classification)
    // Terms of a new document are ranked besed on their frequency
    // The correlation consists of the sum of the squared difference between the ranking of term A in a class X and the ranking of term A in a new document
    // We empirically verified that the intersection function provide better way to compute correlation between a class prototype and the terms of a new document. 
    public double classifyInstance(Instance instance){
        HashMap<Integer,Double> instancia = new HashMap<Integer,Double>();
        for(int term=0;term<numTerms;term++){
            if(instance.value(term) > 0){
                instancia.put(term, instance.value(term));
            }
        }
        
        double[] classification = new double[numClasses];
        for(int classe=0;classe<numClasses;classe++){
            ArrayList<IndexValue> neighbors = rankingsFinal[classe].getNeighbors();
            int counter = 0;
            ArrayList<IndexValue> rankingClasseTemp = new ArrayList<IndexValue>();
            ArrayList<IndexValue> termFreq = new ArrayList<IndexValue>();
            for(int term=0;term<neighbors.size();term++){
                int indTermClasse = neighbors.get(term).index;
                double rankTermInst = -1;
                if(instancia.containsKey(indTermClasse)){
                    IndexValue indValClasse = new IndexValue();
                    indValClasse.index = neighbors.get(term).index;
                    indValClasse.value = neighbors.get(term).value;
                    rankingClasseTemp.add(indValClasse);
                    IndexValue indValInst = new IndexValue();
                    indValInst.index = neighbors.get(term).index;
                    indValInst.value = instancia.get(indTermClasse);
                    termFreq.add(indValInst);
                    counter++;
                }else{
                    continue;
                }
            }

            double numerator = 0;
            double denominator = 0;
            
            ArrayList<IndexValue> rankingClasseTemp2 = new ArrayList<IndexValue>();
            boolean exit = false;
            counter = 0;
            while(exit == false){
                int index = -1;
                double highest = -30000000;
                for(int term=0;term<rankingClasseTemp.size();term++){
                    if(rankingClasseTemp.get(term).value > highest){
                        highest = rankingClasseTemp.get(term).value;
                        index = term;
                    }
                }
                if(index!=-1){
                    IndexValue indVal = new IndexValue();
                    indVal.index = rankingClasseTemp.get(index).index;
                    indVal.value = highest;
                    rankingClasseTemp2.add(indVal);
                    rankingClasseTemp.remove(index);
                    counter++;
                }
                
                if(rankingClasseTemp.size() == 0){
                    exit = true;
                }
            }
            
            ArrayList<IndexValue> rankingClasse = new ArrayList<IndexValue>();
            for(int term1=0;term1<rankingClasseTemp2.size();term1++){
                double valueTerm1 = rankingClasseTemp2.get(term1).value;
                int counter2 = 0;
                int accumulator2 = 0;
                for(int term2=term1+1;term2<rankingClasseTemp2.size();term2++){
                    double valueTerm2 = rankingClasseTemp2.get(term2).value;
                    if(valueTerm1 == valueTerm2){
                        counter2++;
                        accumulator2 += counter2;
                    }else{
                        break;
                    }
                }
                if(counter2 == 0){
                    IndexValue indVal = new IndexValue();
                    indVal.index = rankingClasseTemp2.get(term1).index;
                    indVal.value = (double)term1;
                    rankingClasse.add(indVal);
                }else{
                    counter2++;
                    double rank = term1 + (accumulator2/(double)counter2);
                    for(int term2=0;term2<counter2;term2++){
                        IndexValue indVal = new IndexValue();
                        indVal.index = rankingClasseTemp2.get(term1 + term2).index;
                        indVal.value = rank;
                        rankingClasse.add(indVal);
                    }
                    term1 += counter2 - 1;
                }
            }

            ArrayList<IndexValue> rankingInstTemp = new ArrayList<IndexValue>();
            exit = false;
            counter = 0;
            while(exit == false){
                int index = -1;
                double highest = -300000000;
                for(int term=0;term<termFreq.size();term++){
                    if(termFreq.get(term).value > highest){
                        highest = termFreq.get(term).value;
                        index = term;
                    }
                }
                if(index!=-1){
                    IndexValue indVal = new IndexValue();
                    indVal.index = termFreq.get(index).index;
                    indVal.value = highest;
                    rankingInstTemp.add(indVal);
                    termFreq.remove(index);
                    counter++;
                }
                
                if(termFreq.size() == 0){
                    exit = true;
                }
            }
            
            HashMap<Integer,Double> rankingInst = new HashMap<Integer,Double>();
            for(int term1=0;term1<rankingInstTemp.size();term1++){
                double valueTerm1 = rankingInstTemp.get(term1).value;
                int counter2 = 0;
                int accumulator2 = 0;
                for(int term2=term1+1;term2<rankingInstTemp.size();term2++){
                    double valueTerm2 = rankingInstTemp.get(term2).value;
                    if(valueTerm1 == valueTerm2){
                        counter2++;
                        accumulator2 += counter2;
                    }else{
                        break;
                    }
                }
                if(counter2 == 0){
                    rankingInst.put(rankingInstTemp.get(term1).index, (double)term1);
                }else{
                    counter++;
                    double rank = term1 + (accumulator2/(double)counter2);
                    for(int term2=0;term2<counter2;term2++){
                        rankingInst.put(rankingInstTemp.get(term1 + term2).index, rank);
                    }
                    term1 += counter2 - 1;
                }
            }

            for(int pos=0;pos<rankingClasse.size();pos++){
                int indexTerm = rankingClasse.get(pos).index;
                double rankingTermClasse = rankingClasse.get(pos).value;
                if(!rankingInst.containsKey(indexTerm)){
                    continue;
                }
                double rankTermInst = rankingInst.get(indexTerm);
                numerator += Math.pow((rankingTermClasse - rankTermInst), 2);
            }
                

            if(numerator!=0){
                double correlation = ((6* numerator) / ((Math.pow(numTerms, 2) - 1) * numTerms));
                classification[classe] = correlation;
            }else{
                classification[classe] = 0;
            }
       }

        double valueClasse=0;
        double highest = -300000000;
        for(int classe=0;classe<numClasses;classe++){
            if(classification[classe] > highest){
                highest = classification[classe];
                valueClasse = classe;
            }
            
        }
        return valueClasse;
    }
    
    
    
    public void setDifMediaMinima(double minimumAverageDifference){
        this.minimumAverageDifference = minimumAverageDifference;
    }
    
    public void setMinSup(double threshold){
        this.minSup = threshold;
    }
    
    public void setDampingFactor(double factor){
        this.dampingFactor = factor;
    }
    
    public void setMaxNumIterations(int numMaxIterations){
        this.numMaxIterations = numMaxIterations;
    }
    
    public double getAvgMinDifference(){
        return this.minimumAverageDifference;
    }
}
