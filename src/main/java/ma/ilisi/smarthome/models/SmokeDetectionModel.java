package ma.ilisi.smarthome.models;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.Instance;
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
            String DATASET_PATH = "src/main/resources/datasets/smoke_detection_iot_reduced.arff";
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

    public boolean predictSmokeExistence(double temperature, double humidity, double tvoc, double eco2,
                                         double rawH2, double rawEthanol, double pressure) throws Exception {
        // Create a new DenseInstance for prediction
        Instance newInstance = new DenseInstance(7);
        newInstance.setDataset(dataset);

        // Set attribute values for prediction
        newInstance.setValue(dataset.attribute("Temperature[C]"), temperature);
        newInstance.setValue(dataset.attribute("Humidity[%]"), humidity);  // Corrected attribute name
        newInstance.setValue(dataset.attribute("TVOC[ppb]"), tvoc);
        newInstance.setValue(dataset.attribute("eCO2[ppm]"), eco2);
        newInstance.setValue(dataset.attribute("Raw H2"), rawH2);  // Corrected attribute name
        newInstance.setValue(dataset.attribute("Raw Ethanol"), rawEthanol);  // Corrected attribute name
        newInstance.setValue(dataset.attribute("Pressure[hPa]"), pressure);

        // Predict smoke existence
        double prediction = model.classifyInstance(newInstance);

        // Convert the numeric prediction to a boolean (assuming 0 means false and 1 means true)
        return prediction == 1.0;
    }





    public static void main(String[] args) {
        try {
            // Example usage
            SmokeDetectionModel smokeDetection = new SmokeDetectionModel();

            // Example attribute values
            double temperature = 20.0;
            double humidity = 55.0;
            double tvoc = 0.0;
            double eco2 = 400.0;
            double rawH2 = 12306.0;
            double rawEthanol = 18520.0;
            double pressure = 939.735;

            // Predict smoke existence
            boolean smokePrediction = smokeDetection.predictSmokeExistence(temperature, humidity, tvoc, eco2, rawH2, rawEthanol, pressure);

            // Print the result
            System.out.println("Smoke Predicted: " + smokePrediction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
