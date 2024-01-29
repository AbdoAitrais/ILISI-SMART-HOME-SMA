package ma.ilisi.smarthome.models;

import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ShutterPredictionModel {

    private Instances dataset;  // Weka Instances object
    private LinearRegression model;  // Weka Linear Regression model

    public ShutterPredictionModel() throws Exception {
        loadDataset();
        initializeModel();
        trainModel();
    }

    private void loadDataset() {
        try {
            String DATASET_PATH = "src/main/resources/datasets/shutters_dataset.arff";
            DataSource source = new DataSource(DATASET_PATH);
            dataset = source.getDataSet();

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
        model = new LinearRegression();
    }

    private void trainModel() throws Exception {
        model.buildClassifier(dataset);
    }

    public double predictShutterPosition(double newLightIntensity, double newTemperature) throws Exception {
        // Create a new DenseInstance for prediction
        Instance newInstance = new DenseInstance(2);
        newInstance.setValue(dataset.attribute("light_intensity"), newLightIntensity);
        newInstance.setValue(dataset.attribute("temperature"), newTemperature);

        // Predict the shutter position
        return model.classifyInstance(newInstance);
    }


    public static void main(String[] args) {
        try {
            // Example usage
            ShutterPredictionModel predictor = new ShutterPredictionModel();

            // Example 1
            double newLightIntensity1 = 900;  // Lux
            double newTemperature1 = 25;  // Degrees Celsius

            // Example 2
            double newLightIntensity2 = 1100;  // Lux
            double newTemperature2 = 28;  // Degrees Celsius

            // Example 3
            double newLightIntensity3 = 750;  // Lux
            double newTemperature3 = 19;  // Degrees Celsius

            // Example 4
            double newLightIntensity4 = 1300;  // Lux
            double newTemperature4 = 32;  // Degrees Celsius


            double predictedShutterPosition = predictor.predictShutterPosition(newLightIntensity1, newTemperature1);

            System.out.println("Predicted Shutter Position: " + predictedShutterPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
