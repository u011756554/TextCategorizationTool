//*****************************************************************************
// Author: Rafael Geraldeli Rossi
// E-mail: rgr.rossi at gmail com
// Last-Modified: January 29, 2015
// Description: This class implements function to geneate Exp and Mutual Knn
//              Networks considering Euclidean or Cosine as proximity measures.
//*****************************************************************************  

package TCTNetworkGeneration;

import TCTStructures.NeighborHash;
import java.util.Random;
import weka.core.Instances;

public class DocumentNetworkGeneration {
    
    public static double[][] GenerateExpNetworkCosine(NeighborHash[] adjancecyListDocTerm, double sigma){
        int numInstPerClass = adjancecyListDocTerm.length;
        double[][] w = new double[numInstPerClass][numInstPerClass];
        
        for(int inst1=0;inst1<numInstPerClass;inst1++){
            for(int inst2=inst1+1;inst2<numInstPerClass;inst2++){
                double dist = Proximity.computeCosineExp(adjancecyListDocTerm[inst1], adjancecyListDocTerm[inst2],sigma);
                w[inst1][inst2] = dist;
                w[inst2][inst1] = dist;
            }
        }
        return w;
    }
    
    public static double[][] GenerateExpNetworkCosine(Instances dataTrain, Instances dataTest, double sigma){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        
        double[][] w = new double[numTrain + numTest][ numTrain + numTest];
        
        for(int inst1=0;inst1<numTrain;inst1++){
            for(int inst2=inst1+1;inst2<numTrain;inst2++){
                double dist = Proximity.computeCosineExp(dataTrain.instance(inst1), dataTrain.instance(inst2),sigma);
                w[inst1][inst2] = dist;
                w[inst2][inst1] = dist;
            }
            for(int inst2=0;inst2<numTest;inst2++){
                double dist = Proximity.computeCosineExp(dataTrain.instance(inst1), dataTest.instance(inst2),sigma);
                w[inst1][numTrain + inst2] = dist;
                w[numTrain + inst2][inst1] = dist;
            }
        }
        
        for(int inst1=0;inst1<numTest;inst1++){
            for(int inst2=inst1;inst2<numTest;inst2++){
                double dist = Proximity.computeCosineExp(dataTest.instance(inst1), dataTest.instance(inst2),sigma);
                w[numTrain + inst1][numTrain + inst2] = dist;  
                w[numTrain + inst2][numTrain + inst1] = dist;      
            }
        }
        return w;
    }
    
    public static double[][] GenerateGaussianNetworkEuclidean(Instances dataTrain, Instances dataTest, double sigma){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        
        double[][] w = new double[numTrain + numTest][ numTrain + numTest];
        
        for(int inst1=0;inst1<numTrain;inst1++){
            for(int inst2=inst1+1;inst2<numTrain;inst2++){
                double dist = Proximity.computeEuclideanExp(dataTrain.instance(inst1), dataTrain.instance(inst2),sigma);
                w[inst1][inst2] = dist;
                w[inst2][inst1] = dist;
            }
            for(int inst2=0;inst2<numTest;inst2++){
                double dist = Proximity.computeEuclideanExp(dataTrain.instance(inst1), dataTest.instance(inst2),sigma);
                w[inst1][numTrain + inst2] = dist;
                w[numTrain + inst2][inst1] = dist;
            }
        }
        
        for(int inst1=0;inst1<numTest;inst1++){
            for(int inst2=inst1;inst2<numTest;inst2++){
                double dist = Proximity.computeEuclideanExp(dataTest.instance(inst1), dataTest.instance(inst2),sigma);
                w[numTrain + inst1][numTrain + inst2] = dist;  
                w[numTrain + inst2][numTrain + inst1] = dist;      
            }
        }
        
        return w;
    }
    
    public static double[][] GenerateMutualKnnNetworkCosine(NeighborHash[] adjancecyListDocTerm, int k){
        int numInstPerClass = adjancecyListDocTerm.length;
        double[][] w = new double[numInstPerClass][numInstPerClass];
        for(int inst1=0;inst1<numInstPerClass;inst1++){
            for(int inst2=inst1+1;inst2<numInstPerClass;inst2++){
                double dist = Proximity.computeCosine(adjancecyListDocTerm[inst1], adjancecyListDocTerm[inst2]);
                w[inst1][inst2] = dist;
                w[inst2][inst1] = dist;
            }
        }    
        
        int[][] kNeighbors = new int[numInstPerClass][k];
        double[][] kProximities = new double[numInstPerClass][k];
        for(int inst1=0;inst1<numInstPerClass;inst1++){
            for(int pos=0;pos<k;pos++){
                int ind = -1;
                double maior = Double.MIN_VALUE;
                for(int inst2=0;inst2<numInstPerClass;inst2++){
                    if(w[inst1][inst2] > maior){
                        ind = inst2;
                        maior = w[inst1][inst2];
                    }
                }
                kNeighbors[inst1][pos] = ind;
                kProximities[inst1][pos] = maior;
                if(ind != -1){
                    w[inst1][ind] = -1;
                }
            }
        }
        
        w = null;
        double[][] w2 = new double[numInstPerClass][numInstPerClass];
        double sigma = 0;
        for(int inst1=0;inst1<numInstPerClass;inst1++){
            sigma += 1 - kProximities[inst1][k-1];
        }
        sigma = (double)sigma / (double)(3 * numInstPerClass);
        for(int inst1=0;inst1<numInstPerClass;inst1++){
            for(int pos=0;pos<k;pos++){
                if(kNeighbors[inst1][pos] != -1){
                    w2[inst1][kNeighbors[inst1][pos]] = Math.exp(((-1) * Math.pow((1 - kProximities[inst1][pos]), 2)) / (2 * Math.pow(sigma, 2)));
                }
            }
        }
        
        w = new double[numInstPerClass][numInstPerClass];
        for(int inst1=0;inst1<numInstPerClass;inst1++){
            boolean flag = false;
            for(int inst2=0;inst2<numInstPerClass;inst2++){
                double value = w2[inst1][inst2] * w2[inst2][inst1];
                if(value > 0){
                    flag = true;
                    w[inst1][inst2] = w2[inst1][inst2];
                    w[inst2][inst1] = w2[inst2][inst1];
                }
            }
            if(flag == false){ //We connect a vertice with the most similar vertice in case of not having mutual neighbors
                if(kNeighbors[inst1][0] != -1){
                    w[inst1][kNeighbors[inst1][0]] = Math.exp(((-1) * Math.pow((1 - kProximities[inst1][0]), 2)) / (2 * Math.pow(sigma, 2)));
                }else{
                    int randomNumber = (int)(new Random().nextDouble() * numInstPerClass);
                    w[inst1][randomNumber] = Math.exp(((-1) * Math.pow((1 - 0.01), 2)) / (2 * Math.pow(sigma, 2)));
                }
                
            }
        }
        
        
        return w;
    }
    
    public static double[][] GenerateMutualKnnNetworkCosine(Instances dataTrain, Instances dataTest, int k){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        
        double[][] w = new double[numTrain + numTest][ numTrain + numTest];
        for(int inst1=0;inst1<numTrain;inst1++){
            for(int inst2=inst1+1;inst2<numTrain;inst2++){
                double dist = Proximity.computeCosine(dataTrain.instance(inst1), dataTrain.instance(inst2));
                w[inst1][inst2] = dist;
                w[inst2][inst1] = dist;
            }
            for(int inst2=0;inst2<numTest;inst2++){
                double dist = Proximity.computeCosine(dataTrain.instance(inst1), dataTest.instance(inst2));
                w[inst1][numTrain + inst2] = dist;
                w[numTrain + inst2][inst1] = dist;
            }
        }
        
        for(int inst1=0;inst1<numTest;inst1++){
            for(int inst2=inst1;inst2<numTest;inst2++){
                double dist = Proximity.computeCosine(dataTest.instance(inst1), dataTest.instance(inst2));
                w[numTrain + inst1][numTrain + inst2] = dist;  
                w[numTrain + inst2][numTrain + inst1] = dist;      
            }
        }
        
        int[][] kNeighbors = new int[numTrain + numTest][k];
        double[][] kProximities = new double[numTrain + numTest][k];
        for(int inst1=0;inst1<(numTrain+numTest);inst1++){
            for(int pos=0;pos<k;pos++){
                int ind = -1;
                double maior = Double.MIN_VALUE;
                for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                    if(w[inst1][inst2] > maior){
                        ind = inst2;
                        maior = w[inst1][inst2];
                    }
                }
                kNeighbors[inst1][pos] = ind;
                kProximities[inst1][pos] = maior;
                if(ind != -1){
                    w[inst1][ind] = -1;
                }
            }
        }
        
        w = null;
        double[][] w2 = new double[numTrain + numTest][numTrain + numTest];
        double sigma = 0;
        for(int inst1=0;inst1<(numTrain + numTest);inst1++){
            sigma += 1 - kProximities[inst1][k-1];
        }
        sigma = (double)sigma / (double)(3 * (numTrain + numTest));
        for(int inst1=0;inst1<(numTrain + numTest);inst1++){
            for(int pos=0;pos<k;pos++){
                if(kNeighbors[inst1][pos] != -1){
                    w2[inst1][kNeighbors[inst1][pos]] = Math.exp(((-1) * Math.pow((1 - kProximities[inst1][pos]), 2)) / (2 * Math.pow(sigma, 2)));
                }
            }
        }
        
        w = new double[numTrain + numTest][numTrain + numTest];
        for(int inst1=0;inst1<(numTrain + numTest);inst1++){
            boolean flag = false;
            for(int inst2=0;inst2<(numTrain + numTest);inst2++){
                double value = w2[inst1][inst2] * w2[inst2][inst1];
                if(value > 0){
                    flag = true;
                    w[inst1][inst2] = w2[inst1][inst2];
                    w[inst2][inst1] = w2[inst2][inst1];
                }
            }
            if(flag == false){ //We connect a vertice with the most similar vertice in case of not having mutual neighbors
                if(kNeighbors[inst1][0] != -1){
                    w[inst1][kNeighbors[inst1][0]] = Math.exp(((-1) * Math.pow((1 - kProximities[inst1][0]), 2)) / (2 * Math.pow(sigma, 2)));
                }else{
                    int randomNumber = (int)(new Random().nextDouble() * (numTrain+numTest));
                    w[inst1][randomNumber] = Math.exp(((-1) * Math.pow((1 - 0.01), 2)) / (2 * Math.pow(sigma, 2)));
                }
                
            }
        }
        return w;
    }
    
    public static double[][] GenerateMutualKnnNetworkEuclidean(Instances dataTrain, Instances dataTest, int k){
        int numTrain = dataTrain.numInstances();
        int numTest = dataTest.numInstances();
        
        double[][] w = new double[numTrain + numTest][ numTrain + numTest];
        for(int inst1=0;inst1<numTrain;inst1++){
            for(int inst2=inst1+1;inst2<numTrain;inst2++){
                double dist = Proximity.calcDistEuclidiana(dataTrain.instance(inst1), dataTrain.instance(inst2));
                w[inst1][inst2] = dist;
                w[inst2][inst1] = dist;
            }
            for(int inst2=0;inst2<numTest;inst2++){
                double dist = Proximity.calcDistEuclidiana(dataTrain.instance(inst1), dataTest.instance(inst2));
                w[inst1][numTrain + inst2] = dist;
                w[numTrain + inst2][inst1] = dist;
            }
        }
        
        for(int inst1=0;inst1<numTest;inst1++){
            for(int inst2=inst1;inst2<numTest;inst2++){
                double dist = Proximity.calcDistEuclidiana(dataTest.instance(inst1), dataTest.instance(inst2));
                w[numTrain + inst1][numTrain + inst2] = dist;  
                w[numTrain + inst2][numTrain + inst1] = dist;      
            }
        }
        
        for(int inst1=0;inst1<(numTrain+numTest);inst1++){
            w[inst1][inst1] = Double.MAX_VALUE;
        }
        
        int[][] kNeighbors = new int[numTrain + numTest][k];
        double[][] kProximities = new double[numTrain + numTest][k];
        for(int inst1=0;inst1<(numTrain+numTest);inst1++){
            for(int pos=0;pos<k;pos++){
                int ind = -1;
                double min = Double.MAX_VALUE;
                for(int inst2=0;inst2<(numTrain+numTest);inst2++){
                    if(w[inst1][inst2] < min){
                        ind = inst2;
                        min = w[inst1][inst2];
                    }
                }
                kNeighbors[inst1][pos] = ind;
                kProximities[inst1][pos] = min;
                if(ind != -1){
                    w[inst1][ind] = Double.MAX_VALUE;
                }
            }
        }
        
        w = null;
        double[][] w2 = new double[numTrain + numTest][numTrain + numTest];
        double sigma = 0;
        for(int inst1=0;inst1<(numTrain + numTest);inst1++){
            //sigma += 1 - kProximities[inst1][k-1];
            sigma += kProximities[inst1][k-1];
        }
        //sigma = (double)sigma / (double)(3 * (numTrain + numTest));
        sigma = (double)sigma / (double)(3 * k);
        for(int inst1=0;inst1<(numTrain + numTest);inst1++){
            for(int pos=0;pos<k;pos++){
                w2[inst1][kNeighbors[inst1][pos]] = kProximities[inst1][pos];
            }
        }
        
        w = new double[numTrain + numTest][numTrain + numTest];
        for(int inst1=0;inst1<(numTrain + numTest);inst1++){
            boolean flag = false;
            for(int inst2=0;inst2<(numTrain + numTest);inst2++){
                double value = w2[inst1][inst2] * w2[inst2][inst1];
                if(value > 0){
                    flag = true;
                    w[inst1][inst2] = w2[inst1][inst2];
                    w[inst2][inst1] = w2[inst2][inst1];
                }
            }
            if(flag == false){
                if(kNeighbors[inst1][0] != -1){
                    w[inst1][kNeighbors[inst1][0]] = Math.exp(((-1) * Math.pow(((-1) * kProximities[inst1][0]), 2)) / (2 * Math.pow(sigma, 2)));
                }else{
                    int randomNumber = (int)(new Random().nextDouble() * (numTrain+numTest));
                    w[inst1][randomNumber] = Math.exp(((-1) * Math.pow((1 - 0.01), 2)) / (2 * Math.pow(sigma, 2)));
                }
                
            }
        }
        return w;
    }
}
