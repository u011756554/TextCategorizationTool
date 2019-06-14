//******************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description:  This is an implementation of the K-Associated Classifier 
//               K-Associated Optimal Graph (Bertini et al., 2011). 
// References: - J. R. Bertini Jr., L. Zhao, R. Motta, and A. A. Lopes. A 
//               nonparametric classification method based  on k-associated 
//               graphs. Information Sciences, 181(24):5435–5456, December 2011.
//******************************************************************************

package TCTAlgorithms.InductiveSupervised;

import TCTNetworkGeneration.Proximity;
import TCTStructures.Component;
import TCTStructures.IndexValue;
import TCTStructures.Neighbor;
import TCTStructures.NeighborHash;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class KACOG_InductiveSupervised extends Classifier{
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private int numMaxK; // Maximum value of K used as stopping criterion
    private boolean cosine; // Cosine similarty
    private boolean euclidean; // Euclidean distance 
    
    private int[] classeVertices;
    private ArrayList<Component> kaog;
    
    NeighborHash[] adjancecyListDocTerm;
    
    int indexComp;
    
    
    public KACOG_InductiveSupervised(){
        this.numMaxK = 100;
        cosine = true;
        euclidean = false;
    }
    
    public KACOG_InductiveSupervised(int numMaxK, boolean cosine){
        this.numMaxK = numMaxK;
        if(cosine == true){
            this.cosine = true;
            this.euclidean = false;
        }else{
            this.cosine = false;
            this.euclidean = true;
        }
    }
    
    @Override
    public void buildClassifier(Instances dataTrain){
        
        this.numTrain = dataTrain.numInstances();
        this.numClasses = dataTrain.numClasses();
        this.numTerms = dataTrain.numAttributes() - 1;
        
        classeVertices = new int[numTrain];
        
        // Using hashs to speed up the computation of similarities among documents
        adjancecyListDocTerm = new NeighborHash[numTrain];
        if(getCosine()){
            for(int inst=0;inst<numTrain;inst++){
                adjancecyListDocTerm[inst] = new NeighborHash();
            }
            for(int inst=0;inst<numTrain;inst++){
                for(int term=0;term<numTerms;term++){
                    if(dataTrain.instance(inst).value(term) > 0){
                        adjancecyListDocTerm[inst].AddNeighbor(term,dataTrain.instance(inst).value(term));
                    }    
                }
                classeVertices[inst] = (int)dataTrain.instance(inst).classValue();
            }
        }    

        // Creating similarity matrix
        double[][] matProximidade = new double[numTrain][];
        for(int inst=0;inst<numTrain;inst++){
            matProximidade[inst] = new double[inst + 1];
        }
        if(getCosine()){
            for(int inst1=0;inst1<numTrain;inst1++){
                for(int inst2=inst1+1;inst2<numTrain;inst2++){
                    double dist = Proximity.computeCosine(adjancecyListDocTerm[inst1],adjancecyListDocTerm[inst2]);
                    matProximidade[inst2][inst1] = dist;
                }
            }    
        }
        
        indexComp = 0;
        
        boolean exit = false;
        int k=0;
        
        double lastAvgDegree = 0;
        double currentDegree = 0;
        
        Neighbor[] kAssociatedList = new Neighbor[numTrain];
        Neighbor[] kAssociatedListIn = new Neighbor[numTrain];
        Neighbor[] kAssociatedListOut = new Neighbor[numTrain];
        
        for(int inst=0;inst<numTrain;inst++){
            kAssociatedList[inst] = new Neighbor();
            kAssociatedListIn[inst] = new Neighbor();
            kAssociatedListOut[inst] = new Neighbor();
        }
        int numberComponentsPrevious = 0;
        int countComponents = 0;
        
        while(exit == false){
            
            lastAvgDegree = currentDegree;
            
            k = k+1;
            
            
            System.out.print(k + ",");
            int[][] kNeighbors = new int[numTrain][k];
            double[][] kProximities = new double[numTrain][k];
            for(int inst1=0;inst1<numTrain;inst1++){
                for(int pos=0;pos<k;pos++){
                    int ind = -1;
                    int ind1 = -1;
                    int ind2 = -1;
                    double maior = Double.MIN_VALUE;
                    for(int inst2=0;inst2<numTrain;inst2++){
                        if(inst1 == inst2){
                            continue;
                        }
                        ind1 = -1;
                        ind2 = -1;
                        if(inst1 > inst2){
                            ind1 = inst1;
                            ind2 = inst2;
                        }else{
                            ind1 = inst2;
                            ind2 = inst1;
                        }
                        if(matProximidade[ind1][ind2] > maior){
                            ind = inst2;
                            maior = matProximidade[ind1][ind2];
                        }
                    }
                    kNeighbors[inst1][pos] = ind;
                    kProximities[inst1][pos] = maior;
                    if(ind != -1){
                        if(inst1>ind){
                            matProximidade[inst1][ind] = -1;
                        }else{
                            matProximidade[ind][inst1] = -1;
                        }
                    }
                }
            }
            
            kAssociatedList = new Neighbor[numTrain];
            kAssociatedListIn = new Neighbor[numTrain];
            kAssociatedListOut = new Neighbor[numTrain];
            
            for(int inst=0;inst<numTrain;inst++){
                kAssociatedList[inst] = new Neighbor();
                kAssociatedListIn[inst] = new Neighbor();
                kAssociatedListOut[inst] = new Neighbor();
            }
            
            for(int inst=0;inst<numTrain;inst++){
                for(int pos=0;pos<k;pos++){
                    int index = kNeighbors[inst][pos];
                    if(index == -1){
                        continue;
                    }
                    double classeInst1 = dataTrain.instance(inst).classValue();
                    double classeInst2 = dataTrain.instance(index).classValue();
                    if(classeInst1 == classeInst2){
                        IndexValue indVal = new IndexValue();
                        indVal.index = index;
                        indVal.value = kProximities[inst][pos];
                        kAssociatedList[inst].AddNeighbor(indVal);
                        kAssociatedListOut[inst].AddNeighbor(indVal);
                        IndexValue indVal2 = new IndexValue();
                        indVal2.index = inst;
                        indVal2.value = kProximities[inst][pos];
                        kAssociatedListIn[index].AddNeighbor(indVal2);
                    }
                }
            }
            
            ArrayList<Component> components = new ArrayList<Component>();
            components = findComponents(kAssociatedList,kAssociatedListIn,kAssociatedListOut,k);
            
            if(k == 1){
                kaog = components;
                continue;
            }
            
            for(int comp=0;comp<components.size();comp++){
                Component newComponent = components.get(comp);
                double purityNewComponent = newComponent.getPurity();
                boolean bestPurity = true;
                ArrayList<Integer> indicesComponentsRemove = new ArrayList<Integer>();
                ArrayList<Integer> pairedComponents = findSuperComponent(components.get(comp),kaog);
                for(int indComp=0;indComp<pairedComponents.size();indComp++){
                    int indexComp = pairedComponents.get(indComp);
                    if(purityNewComponent < kaog.get(indexComp).getPurity()){
                        bestPurity = false;
                        break;
                    }else{
                        indicesComponentsRemove.add(kaog.get(indexComp).getindexComp());
                    }
                }
                if(bestPurity == true){
                    kaog.add(newComponent);
                    for(int comp2=0;comp2<kaog.size();comp2++){
                        int indexComp = kaog.get(comp2).getindexComp();
                        if(indicesComponentsRemove.contains(indexComp)){
                            kaog.remove(comp2);
                            comp2--;
                        }
                    }
                }
            }

            // Analysis of sttopping criteria
            if(k == numMaxK){
                exit = true;
            }
            if(numberComponentsPrevious == kaog.size()){
                countComponents++;
                if(countComponents == 10){
                    exit = true;
                }
            }else{
                countComponents = 0;
                numberComponentsPrevious = kaog.size();
            }
        }
        System.out.println();
    }
    
    @Override
    // Function to classify a new instance (hard classification)
    public double classifyInstance(Instance instance){
        double indexClass = -1;
        double[] distClasses = distributionForInstance(instance);
        double maior = -3000000;
        for(int classe=0;classe<numClasses;classe++){
            if(distClasses[classe] > maior){
                 maior = distClasses[classe];
                 indexClass = classe;
            }
        }
        return indexClass;
    }
    
    @Override
    // Function to return class confidences of the classification of an instance (soft classification)
    public double[] distributionForInstance(Instance instance){
        double[] distClasses = new double[numClasses];
        
        NeighborHash instanceTest = new NeighborHash();
        for(int term=0;term<numTerms;term++){
            if(instance.value(term) > 0){
                instanceTest.AddNeighbor(term,instance.value(term));
            }    
        }
        
        int highestK = -3000;
        for(int comp=0;comp<kaog.size();comp++){
            if(kaog.get(comp).getK()>highestK){
                highestK = kaog.get(comp).getK();
            }
        }
        
        IndexValue[] similarities = new IndexValue[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            similarities[inst] = new IndexValue();
            similarities[inst].index = inst;
            similarities[inst].value = Proximity.computeCosine(adjancecyListDocTerm[inst],instanceTest);
        }
        
        IndexValue[] kNeighbors = new IndexValue[highestK];
        for(int k=0;k<highestK;k++){
            kNeighbors[k] = new IndexValue();
            double maior = -30000;
            int index = -1;
            for(int inst=0;inst<numTrain;inst++){
                if(similarities[inst].value > maior){
                    maior = similarities[inst].value;
                    index = similarities[inst].index;
                }
            }
            if(index != -1){
                similarities[index].value = -1;
                kNeighbors[k].index = index;
                kNeighbors[k].value = maior;
            }
        }
        
        int[] numNeighborsComp = new int[kaog.size()];
        for(int k=0;k<highestK;k++){
            int vertice = kNeighbors[k].index;
            for(int comp=0;comp<kaog.size();comp++){
                if(kaog.get(comp).getVertices().contains(vertice)){
                    numNeighborsComp[comp] = numNeighborsComp[comp] + 1;
                    break;
                }
            }
        }
        
        for(int classe=0;classe<numClasses;classe++){
            for(int comp=0;comp<kaog.size();comp++){
                if(kaog.get(comp).getClasse() == classe){
                    distClasses[classe] = distClasses[classe] + ((double)numNeighborsComp[comp] * kaog.get(comp).getPurity()); 
                }
            }
        }
        
        double sum = 0;
        for(int classe=0;classe<numClasses;classe++){
            sum += distClasses[classe];
        }
        if(sum != 0){
            for(int classe=0;classe<numClasses;classe++){
                distClasses[classe] = distClasses[classe] / sum;
            }
        }

        return distClasses;
    }
    
    // Extract the components of a K-Associated Graph
    public ArrayList<Component> findComponents(Neighbor[] kAssociatedList, Neighbor[] kAssociatedListIn, Neighbor[] kAssociatedListOut,int k){
        ArrayList<Component> components = new ArrayList<Component>();
        
        boolean[] isInComponent = new boolean[numTrain];
        for(int inst=0;inst<numTrain;inst++){
            if(kAssociatedList[inst].getNeighbors().size() == 0){
                Component component = new Component();
                ArrayList<Integer> vertices = new ArrayList<Integer>();
                vertices.add(inst);
                component.setVertices(vertices);
                component.setPurity(0);
                component.setClass(classeVertices[inst]);
                component.setindexComp(indexComp);
                indexComp++;
                components.add(component);
                isInComponent[inst] = true;
            }else{
                Component component = new Component();
                ArrayList<Integer> queueVertices = new ArrayList<Integer>();
                ArrayList<IndexValue> neighbors = kAssociatedList[inst].getNeighbors();
                for(int neighbor=0;neighbor<neighbors.size();neighbor++){
                    queueVertices.add(neighbors.get(neighbor).index);
                }
                for(int vertice1=0;vertice1<queueVertices.size();vertice1++){
                    int vert = queueVertices.get(vertice1);
                    if(isInComponent[vert] == true){
                        continue;
                    }else{
                        component.addVertice(vert);
                        component.setClass(classeVertices[vert]);
                        component.setK(k);
                        isInComponent[vert] = true;
                        ArrayList<IndexValue> neighbors2 = kAssociatedList[vert].getNeighbors();
                        for(int vertice2=0;vertice2<neighbors2.size();vertice2++){
                            int neighbor = neighbors2.get(vertice2).index;
                            if(isInComponent[neighbor] == false){
                                queueVertices.add(neighbor);
                            }
                        }
                    }
                }
                if(component.getVertices().size()>0){
                    double degreeIn = 0;
                    double degreeOut = 0;
                    int sizeComponent = component.getVertices().size();
                    for(int vert=0;vert<sizeComponent;vert++){
                        degreeIn += kAssociatedListIn[component.getVertice(vert)].getNeighbors().size();
                        degreeOut += kAssociatedListOut[component.getVertice(vert)].getNeighbors().size();
                    }
                    double purity = (double)(degreeIn + degreeOut)/(double)sizeComponent;
                    purity = purity / (double)(2 * k);
                    component.setPurity(purity);
                    component.setK(k);
                    component.setindexComp(indexComp);
                    indexComp++;
                    components.add(component);
                }
            }
        }
        
        return components;
    }
    
    // Function to find the components which became a subset of bigger components
    private ArrayList<Integer> findSuperComponent(Component component, ArrayList<Component> bestComponents){
        ArrayList<Integer> pairedComponents = new ArrayList<Integer>();
        int[] listVerticesComponets = new int[numTrain];
        for(int comp=0;comp<bestComponents.size();comp++){
            Component component2 = bestComponents.get(comp);
            for(int vert=0;vert<component2.getVertices().size();vert++){
                listVerticesComponets[component2.getVertice(vert)] = comp;
            }
        }
        
        for(int vert=0;vert<component.getVertices().size();vert++){
            int vertice = component.getVertice(vert);
            int numComp = listVerticesComponets[vertice];
            if(!pairedComponents.contains(numComp)){
                pairedComponents.add(numComp);
            }
        }
        
        return pairedComponents;
    }
    
    // Function to return the average degree of the network
    private double averageDegreeNetwork(Neighbor[] kAssociatedListIn, Neighbor[] kAssociatedListOut){
        double degree = 0;
        for(int inst=0;inst<numTrain;inst++){
            ArrayList<IndexValue> neighborsIn = kAssociatedListIn[inst].getNeighbors();
            ArrayList<IndexValue> neighborsOut = kAssociatedListOut[inst].getNeighbors();
            degree += neighborsIn.size() + neighborsOut.size();
        }
        double averageDegree = degree/(double)numTrain;
        return averageDegree;
    }
    
    private void setNumMaxK(int numMaxK){
        this.numMaxK = numMaxK;
    }
    
    private void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    private void setEuclidean(boolean euclidean){
        this.euclidean = euclidean;
    }
    
    private int getNumMaxK(){
        return this.numMaxK;
    }
    
    private boolean getCosine(){
        return this.cosine;
    }
    
    private boolean getEuclidean(){
        return this.euclidean;
    }
}
