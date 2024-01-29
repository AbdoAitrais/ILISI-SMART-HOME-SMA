package ma.ilisi.smarthome.models;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.io.File;

public class AirQualityPredictionModel {

    private Instances dataset;
    private NaiveBayes model;

    public AirQualityPredictionModel() throws Exception {
        loadDataset();
        prepareDataSet();
        initializeModel();
        trainModel();
    }



    private void loadDataset() {
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("src/main/resources/datasets/air_quality_dataset.csv"));
            dataset = loader.getDataSet();

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
    private void prepareDataSet() throws Exception {
        ReplaceMissingValues replaceFilter = new ReplaceMissingValues();
        replaceFilter.setInputFormat(dataset);
        dataset = Filter.useFilter(dataset, replaceFilter);
        Normalize normalizeFilter = new Normalize();
        normalizeFilter.setInputFormat(dataset);
        dataset = Filter.useFilter(dataset, normalizeFilter);
    }

    private void initializeModel() {
        model = new NaiveBayes();
    }

    private void trainModel() throws Exception {
        model.buildClassifier(dataset);
    }
    public String predict(double newPM25, double newPM10, double newNO, double newNO2, double newNOx,
                          double newNH3, double newCO, double newSO2, double newO3, double newBenzene,
                          double newToluene, double newXylene) throws Exception {

        // Créer une nouvelle instance avec le bon nombre d'attributs
        Instance newInstance = new DenseInstance(dataset.numAttributes());

        // Définir les valeurs brutes sans normalisation
        newInstance.setValue(dataset.attribute("PM2.5"), newPM25);
        newInstance.setValue(dataset.attribute("PM10"), newPM10);
        newInstance.setValue(dataset.attribute("NO"), newNO);
        newInstance.setValue(dataset.attribute("NO2"), newNO2);
        newInstance.setValue(dataset.attribute("NOx"), newNOx);
        newInstance.setValue(dataset.attribute("NH3"), newNH3);
        newInstance.setValue(dataset.attribute("CO"), newCO);
        newInstance.setValue(dataset.attribute("SO2"), newSO2);
        newInstance.setValue(dataset.attribute("O3"), newO3);
        newInstance.setValue(dataset.attribute("Benzene"), newBenzene);
        newInstance.setValue(dataset.attribute("Toluene"), newToluene);
        newInstance.setValue(dataset.attribute("Xylene"), newXylene);

        // Créer un objet Instances avec la nouvelle instance
        Instances singleInstanceDataset = new Instances(dataset, 0);
        singleInstanceDataset.add(newInstance);

        // Appliquer la normalisation à l'objet Instances
        Normalize normalizeFilter = new Normalize();
        normalizeFilter.setInputFormat(singleInstanceDataset);
        singleInstanceDataset = Filter.useFilter(singleInstanceDataset, normalizeFilter);

        // Classer l'instance normalisée
        double predictedClassIndex = model.classifyInstance(singleInstanceDataset.firstInstance());

        // Obtenir l'attribut de classe
        Attribute classAttribute = dataset.classAttribute();
/*
*       newInstance.setDataset(dataset);

        double predictedClassIndex = model.classifyInstance(newInstance);

        Attribute classAttribute = dataset.classAttribute();

        return classAttribute.value((int) predictedClassIndex);
*
* */
        // Retourner la valeur de classe prédite
        return classAttribute.value((int) predictedClassIndex);
    }


    public static void main(String[] args) {
        try {

            AirQualityPredictionModel predictor = new AirQualityPredictionModel();
            String prediction = predictor.predict(1553.45, 5866.54, 233.3, 2133.6, 1366.09, 142.27, 066.41, 855.19, 29555.38, 1444.28, 5444.64, 0444.92);
            System.out.println("Predicted Class: " + prediction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

