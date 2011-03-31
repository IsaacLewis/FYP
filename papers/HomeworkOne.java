import java.io.*;
import java.util.Scanner;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.Id3;

public class HomeworkOne {

	
	public static void naiveBayes(Instances trainingData, Instances testingData) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("hello, world!");
		DataSource testingSource = new DataSource("test2.arff");
		Instances testingData = testingSource.getDataSet();
		DataSource trainingSource = new DataSource("train2.arff");
		Instances trainingData = trainingSource.getDataSet();
		
		if (trainingData.classIndex() == -1) trainingData.setClassIndex(trainingData.numAttributes() - 1);
		if (testingData.classIndex() == -1) testingData.setClassIndex(trainingData.numAttributes() - 1);
		naiveBayes(trainingData, testingData);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose classifier: (I)d3, (J)48 or Naive (B)ayes: ");
		String input = scanner.next();
		Classifier classifier;

		if(input.equals("B")) {
			System.out.println("Using Naive Bayes Classifier");
			classifier = new NaiveBayes();
		} else if (input.equals("I")) {
			System.out.println("Using Id3 Classifier");
			classifier = new Id3();
		} else {
			System.out.println("Using J48 Classifier");
			classifier = new J48();		
		}
		
		classifier.buildClassifier(trainingData);
		Evaluation trainingEvaluation = new Evaluation(trainingData);
		trainingEvaluation.evaluateModel(classifier, trainingData);
		System.out.println(trainingEvaluation.toSummaryString());
		
		Evaluation testingEvaluation = new Evaluation(testingData);
		testingEvaluation.evaluateModel(classifier, testingData);
		System.out.println(testingEvaluation.toSummaryString());
	}

}
