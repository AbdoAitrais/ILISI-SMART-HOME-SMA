package ma.ilisi.smarthome.models;

import weka.classifiers.functions.Logistic;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class HeatingCoolingPredictionModel {

    private Instances dataset;  // Weka Instances object
    private Logistic model;  // Weka Logistic Regression model

    public HeatingCoolingPredictionModel() throws Exception {
        loadDataset();
        initializeModel();
        trainModel();
    }

    private void loadDataset() {
        try {
            String DATASET_PATH = "src/main/resources/datasets/climat_dataset.arff";
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
        model = new Logistic();
    }

    private void trainModel() throws Exception {
        model.buildClassifier(dataset);
    }

    public String predictHeatingCoolingDecision(double temperature, double humidity, String wind, String outlook) throws Exception {
        // Create a new DenseInstance for prediction
        Instance newInstance = new DenseInstance(4);
        newInstance.setValue(dataset.attribute("temperature"), temperature);
        newInstance.setValue(dataset.attribute("humidity"), humidity);
        newInstance.setValue(dataset.attribute("wind"), wind);
        newInstance.setValue(dataset.attribute("outlook"), outlook);

        // Predict the heating/cooling decision
        double[] predictionDistribution = model.distributionForInstance(newInstance);

        // Convert the predicted distribution to the corresponding class value
        String predictedDecision = dataset.classAttribute().value((int) Math.round(predictionDistribution[0]));

        return predictedDecision;
    }

    public static void main(String[] args) {
        try {
            // Example usage
            HeatingCoolingPredictionModel predictor = new HeatingCoolingPredictionModel();

            // Example
            double newTemperature = 40;  // Degrees Celsius
            double newHumidity = 60;  // Percentage
            String newWind = "low";
            String newOutlook = "sunny";

            String predictedDecision = predictor.predictHeatingCoolingDecision(newTemperature, newHumidity, newWind, newOutlook);

            System.out.println("Predicted Heating/Cooling Decision: " + predictedDecision);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
