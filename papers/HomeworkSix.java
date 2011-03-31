import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.clusterers.*;


public class HomeworkSix {
	public static void main(String[] args) throws Exception{
		
		// Used the Weka GUI to run weka.attributeSelction.PrincipalComponents 
		// on wine1.arff, stored the results in wine15d.arff and wine1_10d.arff
		
		DataSource original = new DataSource("wine1.arff");
		Instances originalData = original.getDataSet();
		DataSource wine5Source = new DataSource("wine15d.arff");
		Instances fiveDData = wine5Source.getDataSet();
		DataSource wine10Source = new DataSource("wine1_10d.arff");
		Instances tenDData = wine10Source.getDataSet();
		
		// generate a copy of the data with no class attribute
		Instances classlessData = wine5Source.getDataSet();
		classlessData.deleteAttributeAt(0);

		System.out.println("\n\n=====\nSimple K Means on 5-d data");
		simpleKMeans(fiveDData, originalData);
		
		System.out.println("\n\n=====\nSimple K Means on 10-d data");
		simpleKMeans(tenDData, originalData);
		
		System.out.println("\n\n=====\nEM on 5-d data");
		EM(fiveDData, originalData);
		
		System.out.println("\n\n=====\nEM on 10-d data");
		EM(tenDData, originalData);

	}
	
	public static void simpleKMeans(Instances data, Instances originalData) throws Exception {
		// create a KMeans clusterer
		SimpleKMeans skm = new SimpleKMeans();
		skm.setNumClusters(3);
		skm.buildClusterer(data);

		// evaluate the KMeans clusterer
		ClusterEvaluation skmEvaluation = new ClusterEvaluation();
		skmEvaluation.setClusterer(skm);
		skmEvaluation.evaluateClusterer(data);
		System.out.println(skmEvaluation.clusterResultsToString());
		
		// to calculate the RAND index, need to know
		// a: number of pairs with same class label and in same cluster
		// b: the number of pairs with the same class label, but in different clusters
		// c: the number of pairs in the same cluster, but with different class labels
		// d: the number of pairs with a different class label, in different clusters
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		
		// get class data as an array of doubles
		double[] classes = originalData.attributeToDoubleArray(0);
		
		// get cluster data as an array of doubles
		double[] clusters = skmEvaluation.getClusterAssignments();
		
		// Compare classes and clusters
		for(int i = 0; i < classes.length; i++) {
			for(int j = i + 1; j < classes.length; j++) {
				if(classes[i] == classes[j]) {
					if(clusters[i] == clusters[j])
						a++;
					else
						b++;
				} else {
					if(clusters[i] == clusters[j])
						c++;
					else
						d++;
				}
			}
		}
		
		System.out.println("a: " + a + " b: " + b + " c: " + c + " d: " + d);
		double rand = (double) (a + d) / (double) (a + b + c + d);
		System.out.println("Rand: " + rand);
	}
	
	public static void EM(Instances data, Instances originalData) throws Exception {
		
		// Create EM Clusterer
		EM em = new EM();
		em.setNumClusters(3);
		em.buildClusterer(data);
		
		// evaluate the EM clusterer
		ClusterEvaluation emEvaluation = new ClusterEvaluation();
		emEvaluation.setClusterer(em);
		emEvaluation.evaluateClusterer(data);
		System.out.println(emEvaluation.clusterResultsToString());
		
		// get class data as an array of doubles
		double[] classes = originalData.attributeToDoubleArray(0);
		
		double[] clusters = emEvaluation.getClusterAssignments();
		// compare classes and clusters
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		
		for(int i = 0; i < classes.length; i++) {
			for(int j = i + 1; j < classes.length; j++) {
				if(classes[i] == classes[j]) {
					if(clusters[i] == clusters[j])
						a++;
					else
						b++;
				} else {
					if(clusters[i] == clusters[j])
						c++;
					else
						d++;
				}
			}
		}
		
		System.out.println("a: " + a + " b: " + b + " c: " + c + " d: " + d);
		double rand = (double) (a + d) / (double) (a + b + c + d);
		System.out.println("Rand: " + rand);
	}
}
