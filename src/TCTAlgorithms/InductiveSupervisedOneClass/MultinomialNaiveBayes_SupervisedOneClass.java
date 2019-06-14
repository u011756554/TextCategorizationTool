/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTAlgorithms.InductiveSupervisedOneClass;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Instance;

/**
 *
 * @author deni
 */
public class MultinomialNaiveBayes_SupervisedOneClass extends OneClassSupervisedClassifier {

    private double[] probTerms;
    private double threshold;
    

    // Construtor
    public MultinomialNaiveBayes_SupervisedOneClass() {
        super();
    }

    // Constroi o classificador
    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {

        // Inicializacao das variaveis do classificador
        int numAtributos = dataTrain.numAttributes() - 1;
        int numInstancias = dataTrain.numInstances();

        probTerms = new double[numAtributos];

        double somatorioTotal = 0;

        for (int atr = 0; atr < numAtributos; atr++) {

            double somatorioParcial = 0;

            for (int inst = 0; inst < numInstancias; inst++) {

                if (dataTrain.instance(inst).value(atr) > 5) {
                    somatorioParcial += dataTrain.instance(inst).value(atr);
                }

            }

            probTerms[atr] = somatorioParcial;
            somatorioTotal += somatorioParcial;
        }

        for (int i = 0; i < probTerms.length; i++) {
            probTerms[i] = probTerms[i] / somatorioTotal;
        }

    }

    // Realiza a classificacao para uma nova instancia
    public double classifyInstance(Instance test) {
        
        double score = getScore(test); 
        
        //System.out.println("Classe: " + test.classValue());
        //System.out.println("Score: " + score);
        
        return (score >= this.threshold ? 0 : 1);
    }

    @Override
    public double getScore(Instance test) {
        int numAtributos = test.numAttributes() - 1;

        // Percorre os atributos para calcular a probabilidade
        double score = 1;

        for (int atr = 0; atr < numAtributos; atr++) {

            // Verifica se o valor da instancia eh maior que 0
            if (test.value(atr) > 0 && probTerms[atr] > 0) {

                // Calcula a gaussiana para cada novo atributo
                score *= test.value(atr) * probTerms[atr];
            }
        }

        // Armazena a confianca da classe
        return score;
    }
    
   

    public double getThreshold() {
        return this.threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    
}
