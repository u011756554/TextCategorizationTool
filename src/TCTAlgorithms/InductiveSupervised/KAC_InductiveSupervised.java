//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description:  This is an implementation of the K-Associated Classifier
//               (Bertini et al., 2011). 
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

public class KAC_InductiveSupervised extends Classifier{
    private int numTrain; // Number of labeled documents
    private int numClasses; // Number of classes
    private int numTerms; // Number of terms
    private boolean cosine; // Cosine similarity
    private boolean euclidean; // Euclidean distance 
    private int k; // number of nearest neighbors
    
    private int[] classeVertices;
    private ArrayList<Component> kac;
    
    NeighborHash[] adjancecyListDocTerm;
    
    public KAC_InductiveSupervised(){
        cosine = true;
        euclidean = false;
    }
    
    public KAC_InductiveSupervised(boolean cosine){
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

        Neighbor[] kAssociatedList = new Neighbor[numTrain];
        Neighbor[] kAssociatedListIn = new Neighbor[numTrain];
        Neighbor[] kAssociatedListOut = new Neighbor[numTrain];

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

        ArrayList<Component> components = findComponents(kAssociatedList,kAssociatedListIn,kAssociatedListOut,k);
        kac = components;
        System.out.println();

        
    }
    
    @Override
    // Function to classify a new instance (hard classification)
    public double classifyInstance(Instance instance){
        double valueClasse = -1;
        double[] distClasses = distributionForInstance(instance);
        double maior = -3000000;
        for(int classe=0;classe<numClasses;classe++){
            if(distClasses[classe] > maior){
                 maior = distClasses[classe];
                 valueClasse = classe;
            }
        }
        return valueClasse;
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
        for(int comp=0;comp<kac.size();comp++){
            if(kac.get(comp).getK()>highestK){
                highestK = kac.get(comp).getK();
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
        
        int[] numNeighborsComp = new int[kac.size()];
        for(int k=0;k<highestK;k++){
            int vertice = kNeighbors[k].index;
            for(int comp=0;comp<kac.size();comp++){
                if(kac.get(comp).getVertices().contains(vertice)){
                    numNeighborsComp[comp] = numNeighborsComp[comp] + 1;
                    break;
                }
            }
        }
        
        for(int classe=0;classe<numClasses;classe++){
            for(int comp=0;comp<kac.size();comp++){
                if(kac.get(comp).getClasse() == classe){
                    distClasses[classe] = distClasses[classe] + ((double)numNeighborsComp[comp] * kac.get(comp).getPurity()); 
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
                isInComponent[inst] = true;
                components.add(component);
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
                    components.add(component);
                }
            }
        }
        
        return components;
    }
    
    public void setCosine(boolean cosine){
        this.cosine = cosine;
    }
    
    public void setEuclidean(boolean euclidean){
        this.euclidean = euclidean;
    }
    
    public boolean getCosine(){
        return this.cosine;
    }
    
    public boolean getEuclidean(){
        return this.euclidean;
    }
    
    public void setK(int k){
        this.k = k;
    }
}
