import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.clusterers.*;

public class HomeworkFive {
	public static void main(String[] args) throws Exception{
		DataSource wineSource = new DataSource("wine1.arff");
		Instances wineData = wineSource.getDataSet();
		
		// generate a copy of the data with no class attribute
		Instances classlessData = wineSource.getDataSet();
		classlessData.deleteAttributeAt(0);


		// create a KMeans clusterer
		SimpleKMeans skm = new SimpleKMeans();
		skm.setNumClusters(3);
		skm.buildClusterer(classlessData);

		// evaluate the KMeans clusterer
		ClusterEvaluation skmEvaluation = new ClusterEvaluation();
		skmEvaluation.setClusterer(skm);
		skmEvaluation.evaluateClusterer(classlessData);
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
		double[] classes = wineData.attributeToDoubleArray(0);
		
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
		
		// Create EM Clusterer
		EM em = new EM();
		em.setNumClusters(3);
		em.buildClusterer(classlessData);
		
		// evaluate the EM clusterer
		ClusterEvaluation emEvaluation = new ClusterEvaluation();
		emEvaluation.setClusterer(em);
		emEvaluation.evaluateClusterer(classlessData);
		System.out.println(emEvaluation.clusterResultsToString());
		
		// Compare classes and clusters
		clusters = emEvaluation.getClusterAssignments();
		a = 0;
		b = 0;
		c = 0;
		d = 0;
		
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
		rand = (double) (a + d) / (double) (a + b + c + d);
		System.out.println("Rand: " + rand);
	}
}
