  
/* ===========================================================
 * EM : a clustering program for the Java(TM) platform
 * ===========================================================
 * This is a Java code that borrows Michael Chen's idea (sth4nth@gmail.com).
 * http://www.mathworks.com/matlabcentral/
 * fileexchange/26184-em-algorithm-for-gaussian-mixture-model
 * 
 * This is a Java program trying to obtain the maximum likelihood estimation 
 * of Gaussian mixture model by expectation maximization (EM) algorithm.
 * It works on data set of arbitrary dimensions. 
 * Several techniques are applied to avoid the float number underflow problems 
 * that often occurs on computing probability of high dimensional data.
 * -----------------
 * EM.java
 * -----------------
 *
 * Original Author:  Sun Bo (National University of Singapore)
 * Contact: sunbocsg@gmail.com
 * Date: July 19,2012
 * 
 * Core Reference: http://www-personal.umich.edu/~gyemin/pubs/tcem_tr.pdf 
 * The first two pages should be enough.
 * 
 * I hope that it will be useful, but it is WITHOUT ANY WARRANTY.
 * 
 * How to use it?
 * 
 * 1. instantiate an EM object.
 * 2. set your tolerance level,default is 1e-10
 * 3. construct your data matrix, see bulidCluster method 
 * for more details
 * 4. bulid clusters
 * 5. if converged, call getLabel to get the final assignment
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTNetworkGeneration.Proximity;
import TCTStructures.NeighborHash;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;
import weka.core.Instance;
import weka.core.Instances;

public class MixtureModel_SimpleCosine_OneClass2 extends OneClassSupervisedClassifier{
	public double tolerance; // tolerance level
	public int numMaxIterations; // maximum iteration
	private boolean converged;
	private double llh, previousllh;
	// This array stores the label of assignment for each data, if converged

	// this has several names: weight or mixtur portion
	private double[] weight;
        private double[] bestWeight;
        

	// membership probability
	private double[][] memberProb;
        private double[] stable;
        
        private NeighborHash[] data;
	private NeighborHash[] mu; // mean or center
        private NeighborHash[] bestMus;
	private double[] sigma;// covariance matrices
        private double[] bestSigmas;// covariance matrices
        
        
        private int numDocs;
        private int numTerms;
        
        private int k;
        private int numTrials;
	/*
	 * constructor
	 */
	public MixtureModel_SimpleCosine_OneClass2() {
		converged = false;
                k=15;
                numTrials = 10;
                tolerance =  1e-10;
                numMaxIterations = 500;
	}

        
        /*
	 * data matrix is an n x d matrix, where d is the dimension of each vector,
	 * n is the number of vectors, k : number of clusters , for example,
	 * {{1,2},{ 3,4}, {5,6}} represents three two-dimensional points
	 */
	@Override
        public void buildClassifier(Instances dataTrain) throws Exception {
                numDocs = dataTrain.numInstances();
                numTerms = dataTrain.numAttributes()-1;
		
		// Creating a hash structure to store an adjacent list
                data = new NeighborHash[numDocs];
                for(int doc=0;doc<numDocs;doc++){
                    data[doc] = new NeighborHash();
                }

                for(int doc=0;doc<numDocs;doc++){
                    for(int term=0;term<numTerms;term++){
                        double value = dataTrain.instance(doc).value(term);
                        if(value > 0){
                            data[doc].AddNeighbor(term,value); 
                        }
                    }
                }
                
                this.weight = new double[k];


                initialization(data);

                int count = 0;
                previousllh = Double.NEGATIVE_INFINITY;
                while (!converged && count < numMaxIterations) {
                        count++;
                        maximization(memberProb);
                        llh = expectation();
                        // relative
                        converged = llh - previousllh < tolerance * Math.abs(llh);
                        previousllh = llh;
                }
                    
	}
        
        
	/*
	 * random initialization, assign k distinct data point to be centers please
	 * see RandomSample class for details
	 */
	private void initialization(NeighborHash[] data) {
		
		mu = new NeighborHash[k];
                
		// idx is an array storing distinct k random values ranging from 1 to n
		Random rand = new Random(0);
                HashSet<Integer> inds = new HashSet<Integer>();
		for (int i = 0; i < k; i++) {
                    int ind = rand.nextInt(data.length);
                    if(!inds.contains(ind)){
                        mu[i] = data[rand.nextInt(data.length)];
                    }else{
                        i--;
                    }
			
		}
		/*
		 * The following part determines the initial assignment. During random
		 * initialization, the covariance matrix sigma is assumed to be identity
		 * matrix, so for each data x, the nomralization constant is identical,
		 * in order to determine the initial assigment, it suffices to take the
		 * minimum of Mahalanobis distance, m distance = (x - mu)'*sigma*(x -
		 * mu) = (x'-mu')(x-mu) = x'x - mu'x-x'mu + mu'mu = mu'mu - 2* mu'x +
		 * x'x since x'x is same for all cluster means, we only need to take the
		 * maximum of(mu'x-1/2*mu'mu), this is fundamental to understand the
		 * code
		 */
		double[][] temp = new double[numDocs][k];
		for (int doc = 0; doc < numDocs; doc++) {
			for (int dist = 0; dist < k; dist++) {
				temp[doc][dist] = Proximity.computeCosine(mu[dist],data[doc]);
			}
		}
		int[] label = new int[numDocs];
		for (int i = 0; i < numDocs; i++) {
			label[i] = max(temp[i]);
		}
		
		memberProb = new double[numDocs][k];
		for (int i = 0; i < numDocs; i++) {
			memberProb[i][label[i]] = 1;
		}
	}

	/*
	 * return the index of the maximum values in an array
	 */
	private static int max(double[] array) {
		int idx = 0;
		double max = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				idx = i;
			}
		}
		return idx;
	}



	private void maximization(double[][] memberProb) {
		double[] temp = new double[k];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < numDocs; j++) {
				temp[i] += memberProb[j][i];
			}
		}
		// update weight or mixture portion
		// update weight or mixture portion
                double sum=0;
                for (int i = 0; i < k; i++) {
                        this.weight[i] = (temp[i] + 1) / (numDocs + k);
                        sum+=this.weight[i];
                }
                for (int i = 0; i < k; i++) {
                        this.weight[i] = this.weight[i]/sum;
                }
                
                
                
                
                
		// update mean or center
		for (int h = 0; h < k; h++) {
			mu[h] = new NeighborHash();
                        
                        double[] avgTerms = new double[numTerms];
                        for(int doc=0;doc<this.numDocs;doc++){
                            HashMap<Integer,Double> neighbors =  data[doc].getNeighbors();
                            Object[] keys = neighbors.keySet().toArray();
                            for(int term=0;term<keys.length;term++){
                                int idTerm = (Integer)keys[term];
                                if(neighbors.get(idTerm) > 0){
                                    avgTerms[idTerm] += neighbors.get(idTerm) * memberProb[doc][h];
                                }
                            }
                        }
                        
                        
                        for(int term=0;term<numTerms;term++){
                            if(avgTerms[term] > 0){
                                mu[h].AddNeighbor(term,avgTerms[term]/temp[h]);
                            }
                        }   
		}
                
                
		// update sigma
		sigma = new double[k];
                
                sum = 0;
                for(int dist=0;dist<this.k;dist++){    
                    double stdDev = 0;
                    for(int doc=0;doc<this.numDocs;doc++){
                        stdDev += Math.pow(1 - Proximity.computeCosine(mu[dist],data[doc]),2)*memberProb[doc][dist];
                    }
                    sigma[dist] = Math.sqrt(stdDev / temp[dist]);    
                    
                }


	}

	// expectation function, return the average loglikelihood of estimation
	private double expectation() {
		double llh = 0;
		double[][] temp = new double[numDocs][k];
		
                for (int h = 0; h < k; h++) {
                    for (int j = 0; j < numDocs; j++) {
			double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * sigma[h]));
                        double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(mu[h],data[j]) / sigma[h]), 2);

                        double score = formula1 * Math.exp(formula2);
                        if(Double.isInfinite(score) || Double.isNaN(score)){
                             score = 0;
                        }
                    }    
		}

		for (int i = 0; i < k; i++) {
			for (int j = 0; j < numDocs; j++) {
				temp[j][i] += Math.log(this.weight[i]);
			}
		}
		// obtain the sum along rows
		double[] T = logsumexp(temp);

		for (int i = 0; i < T.length; i++) {
			llh += T[i];
		}
		llh = llh / (double)numDocs;
		for (int i = 0; i < numDocs; i++) {
			for (int j = 0; j < k; j++) {
				temp[i][j] = temp[i][j] - T[i];
				temp[i][j] = Math.exp(temp[i][j]);
			}
		}
		// update membership probability
		memberProb = temp;
		return llh;
	}

	// Compute log sum while avoiding numerical underflow.
	private double[] logsumexp(double[][] arr) {
		double[] ans = new double[arr.length];
		double[] y = new double[arr.length];
		double[][] temp = new double[arr.length][arr[0].length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = Arrays.copyOf(arr[i], arr[i].length);
		}
		/*
		 * subtract the maximum of each row, at the end add it back
		 */
		for (int i = 0; i < temp.length; i++) {
			y[i] = max(temp[i]);
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] -= y[i];
			}
		}
		for (int i = 0; i < temp.length; i++) {
			double sum = 0;
			for (int j = 0; j < temp[i].length; j++) {
				sum += Math.exp(temp[i][j]);
			}
			ans[i] = y[i] + Math.log(sum);
		}
		Vector<Integer> idx = new Vector<Integer>();
		for (int i = 0; i < y.length; i++) {
			if (Double.isInfinite(y[i]) || Double.isNaN(y[i])) {
				idx.add(i);
			}
		}
		if (idx.size() != 0) {
			for (int i = 0; i < idx.size(); i++) {
				ans[idx.get(i)] = y[idx.get(i)];
			}
		}
		return ans;
	}

	

	// return the membership probability of each data point
	public double[][] getMembershipProbability() {
		return memberProb;
	}

	// return the mixture portion
	public double[] getWeight() {
		return weight;
	}


	// return the convergence status
	public boolean isConverged() {
		return converged;
	}

	
        
        
    /*@Override
    public double getScore(Instance test) {
        NeighborHash newInstance = new NeighborHash();
        
        for(int term=0;term<numTerms;term++){
            double value = test.value(term);
            if((value > 0)){
                newInstance.AddNeighbor(term,value); 
            }    
        }
        
        double score = 0;
        double maxScore = -1;
        for(int dist=0;dist<this.k;dist++){
        
            double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * sigma[dist]));
            double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(mu[dist],newInstance) / sigma[dist]), 2);

            double prob = formula1 * Math.exp(formula2);
            
            if(Double.isInfinite(prob) || Double.isNaN(prob)){
                 prob = 0;
            }
            
            
            //score = prob * probDists[dist];
            score = prob * weight[dist];
            
            if(score > maxScore){
                maxScore = score;
            }
            
        }
        
        //System.out.println("Classe do Exemplo: " + test.classValue());
        System.out.println("Score: " + maxScore);
        
        return maxScore;
        
    }*/
    
    @Override
    public double getScore(Instance test) {
        
        if(weight[0] == 0){
            double[] temp = new double[k];
            for (int i = 0; i < temp.length; i++) {
                    for (int j = 0; j < numDocs; j++) {
                            temp[i] += memberProb[j][i];
                    }
            }
            // update weight or mixture portion
            double sum=0;
            for (int i = 0; i < k; i++) {
                    this.weight[i] = (temp[i] + 1) / (numDocs + k);
                    sum+=this.weight[i];
            }
            for (int i = 0; i < k; i++) {
                    this.weight[i] = this.weight[i]/sum;
            }
        }
        
        
        
        NeighborHash newInstance = new NeighborHash();
        
        for(int term=0;term<numTerms;term++){
            double value = test.value(term);
            if((value > 0)){
                newInstance.AddNeighbor(term,value); 
            }    
        }
        
        double score = 0;
        for(int dist=0;dist<this.k;dist++){
        
            double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * sigma[dist]));
            double formula2 = -0.5 * Math.pow(1 - (Proximity.computeCosine(mu[dist],newInstance) / sigma[dist]), 2);

            double prob = formula1 * Math.exp(formula2);
            
            if(Double.isInfinite(prob) || Double.isNaN(prob)){
                 prob = 0;
            }
            
            score += prob * this.weight[dist];
            
        }
        
        //System.out.println("Classe do Exemplo: " + test.classValue());
        //System.out.println("Score: " + score);
        
        if(score == 0){
            System.out.println("Aqui!");
        }
        
        return score;
        
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public int getNumMaxIterations() {
        return numMaxIterations;
    }

    public void setNumMaxIterations(int numMaxIterations) {
        this.numMaxIterations = numMaxIterations;
    }

    public double getLlh() {
        return llh;
    }

    public void setLlh(double llh) {
        this.llh = llh;
    }

    public double getPreviousllh() {
        return previousllh;
    }

    public void setPreviousllh(double previousllh) {
        this.previousllh = previousllh;
    }

    public double[] getBestWeight() {
        return bestWeight;
    }

    public void setBestWeight(double[] bestWeight) {
        this.bestWeight = bestWeight;
    }

    public double[][] getMemberProb() {
        return memberProb;
    }

    public void setMemberProb(double[][] memberProb) {
        this.memberProb = memberProb;
    }

    public double[] getStable() {
        return stable;
    }

    public void setStable(double[] stable) {
        this.stable = stable;
    }

    public NeighborHash[] getData() {
        return data;
    }

    public void setData(NeighborHash[] data) {
        this.data = data;
    }

    public NeighborHash[] getMu() {
        return mu;
    }

    public void setMu(NeighborHash[] mu) {
        this.mu = mu;
    }

    public NeighborHash[] getBestMus() {
        return bestMus;
    }

    public void setBestMus(NeighborHash[] bestMus) {
        this.bestMus = bestMus;
    }

    public double[] getSigma() {
        return sigma;
    }

    public void setSigma(double[] sigma) {
        this.sigma = sigma;
    }

    public double[] getBestSigmas() {
        return bestSigmas;
    }

    public void setBestSigmas(double[] bestSigmas) {
        this.bestSigmas = bestSigmas;
    }

    public int getNumDocs() {
        return numDocs;
    }

    public void setNumDocs(int numDocs) {
        this.numDocs = numDocs;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getNumTrials() {
        return numTrials;
    }

    public void setNumTrials(int numTrials) {
        this.numTrials = numTrials;
    }
    
    
}

