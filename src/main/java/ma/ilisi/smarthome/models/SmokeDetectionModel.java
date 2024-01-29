package ma.ilisi.smarthome.models;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class SmokeDetectionModel {

    private Instances dataset;  // Weka Instances object
    private J48 model;  // Weka J48 Decision Tree model

    public SmokeDetectionModel() throws Exception {
        loadDataset();
        initializeModel();
        trainModel();
        evaluateModel();
    }

    private void loadDataset() {
        try {
            String DATASET_PATH = "src/main/resources/datasets/smoke_detection_iot.arff";
            dataset = DataSource.read(DATASET_PATH);

            // Debugging statements
            System.out.println("Number of Instances: " + dataset.numInstances());
            System.out.println("Number of Attributes: " + dataset.numAttributes());

            // Assuming the target variable is the last attribute
            dataset.setClassIndex(dataset.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or throw it again depending on your requirements
        }
    }

    private void initializeModel() {
        model = new J48();
    }

    private void trainModel() throws Exception {
        model.buildClassifier(dataset);
    }

    private void evaluateModel() throws Exception {
        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(model, dataset, 10, new Debug.Random(1));
        System.out.println("=== Evaluation ===");
        System.out.println(eval.toSummaryString());
    }

    public static void main(String[] args) {
        try {
            // Example usage
            SmokeDetectionModel smokeDetection = new SmokeDetectionModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
