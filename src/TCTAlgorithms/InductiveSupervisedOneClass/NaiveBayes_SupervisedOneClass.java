package TCTAlgorithms.InductiveSupervisedOneClass;

import TCTAlgorithms.InductiveSupervisedOneClass.*;
import weka.core.Instances;
import weka.core.Instance;

/**
 *
 * @author deni
 */
public class NaiveBayes_SupervisedOneClass extends OneClassSupervisedClassifier {

    private double[] mediaAtributos;
    private double[] desvioAtributos;

    // Construtor
    public NaiveBayes_SupervisedOneClass() {
        super();
    }

    // Constroi o classificador
    @Override
    public void buildClassifier(Instances dataTrain) throws Exception {

        // Inicializacao das variaveis do classificador
        int numAtributos = dataTrain.numAttributes() - 1;
        int numInstancias = dataTrain.numInstances();

        mediaAtributos = new double[numAtributos];
        desvioAtributos = new double[numAtributos];

        /*for (int inst = 0; inst < numInstancias; inst++) {

            for (int atr = 0; atr < numAtributos; atr++) {
                double anterior = dataTrain.instance(inst).value(atr);
                dataTrain.instance(inst).setValue(atr, anterior + 1);
            }
        }*/

        // Percorre as instancias a fim de capturar a norma 
        /*for (int inst = 0; inst < numInstancias; inst++) {

            double norma = 0;

            // Captura a norma dos atributos
            for (int atr = 0; atr < numAtributos; atr++) {
                norma = norma + Math.pow(dataTrain.instance(inst).value(atr), 2);
            }

            norma = Math.sqrt(norma);

            // Define o novo valor do atributo
            for (int atr = 0; atr < numAtributos; atr++) {
                double anterior = dataTrain.instance(inst).value(atr);
                dataTrain.instance(inst).setValue(atr, anterior / norma);
            }
        }*/

        // Percorre os atributos
        for (int inst = 0; inst < numInstancias; inst++) {

            // Verifica se aquela instancia pertence a classe desejada
            System.out.println(dataTrain.instance(inst).value(0));
        }
        
        for (int atr = 0; atr < numAtributos; atr++) {

            double somatorio = 0;

            // Percorre as instancias para calcular a media
            for (int inst = 0; inst < numInstancias; inst++) {

                // Verifica se aquela instancia pertence a classe desejada
                somatorio += dataTrain.instance(inst).value(atr);
            }

            // Calcula a media dos atributos
            double media = somatorio / numInstancias;
            mediaAtributos[atr] = media;
            somatorio = 0;

            // Percorre as instancias para calcular o desvio padrao
            for (int inst = 0; inst < numInstancias; inst++) {

                // Verifica se aquela instancia pertence a classe desejada
                somatorio += Math.pow(dataTrain.instance(inst).value(atr) - media, 2);
            }

            // Calcula o desvio padrao dos atributos
            desvioAtributos[atr] = Math.sqrt(somatorio / numInstancias);
        }
    }

    // Realiza a classificacao para uma nova instancia
    public double classifyInstance(Instance instance) {

        return (getScore(instance) >= this.getThreshold() ? 0 : 1);
    }

    // Calcula a distribuicao normal para determinada instancia
    @Override
    public double getScore(Instance instance) {

        int numAtributos = instance.numAttributes() - 1;

        // Percorre os atributos para calcular a probabilidade
        double acumulaConfianca = 1;

        // Captura a norma dos atributos
        //double norma = 0;
        
        /*for (int atr = 0; atr < numAtributos; atr++) {
            norma = norma + Math.pow(instance.value(atr), 2);
        }

        norma = Math.sqrt(norma);

        // Define o novo valor do atributo
        for (int atr = 0; atr < numAtributos; atr++) {
            double anterior = instance.value(atr);
            instance.setValue(atr, anterior / norma);
        }*/

        for (int atr = 0; atr < numAtributos; atr++) {

            // Calcula a gaussiana para cada novo atributo
            if (desvioAtributos[atr] == 0) {
                continue;
            }

            if (instance.value(atr) == 0) {
                continue;
            }

            double formula1 = 1;
            //double formula1 = 1 / ((Math.sqrt(2 * Math.PI) * desvioAtributos[atr]));
            double formula2 = -0.5 * Math.pow((instance.value(atr) - mediaAtributos[atr]) / desvioAtributos[atr], 2);
            
            
            /*if (formula1 == 0 || formula2 == 0) {
                System.out.println("igual a 0");
            }

            if((formula1 * Math.exp(formula2)) > 1){
                System.out.println("Deu merda!!!!");
            }*/
            
            //acumulaConfianca *= formula1 * Math.pow(Math.E, formula2);
            acumulaConfianca *= formula1 * Math.exp(formula2);
            //System.out.println("Confianca: " + acumulaConfianca);
            if(Double.isInfinite(acumulaConfianca)){
                 System.out.println("Aqui deu infinito");
            }
            if(Double.isNaN(acumulaConfianca)){
                System.out.println("Aqui deu NaN");
            }
        }

        
        
        // Armazena a confianca da classe
        return acumulaConfianca;
    }


    
}
