/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author rafael
 */
public abstract class Clustering extends OneClassSupervisedClassifier{
    
    HashSet<Integer>[] solution;

    public void constructSolution(int k){
        this.solution = new HashSet[k];
        for (int i = 0; i < k; i++) {
            solution[i] = new HashSet<Integer>();
        }
    }
    
    public HashSet<Integer>[] getSolution() {
        return solution;
    }

    public void setSolution(HashSet<Integer>[] solution) {
        this.solution = solution;
    }
    
}
