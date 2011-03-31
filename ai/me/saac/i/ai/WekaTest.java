package me.saac.i.ai;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;

public class WekaTest {
	public static void main(String[] args) {
		Attribute a = new Attribute("a");
		Attribute b = new Attribute("b");
		FastVector fv = new FastVector(3);
		fv.addElement("check");
		fv.addElement("raise");
		fv.addElement("fold");
		
		Attribute c = new Attribute("c", fv);
		
		FastVector attrs = new FastVector(3);
		attrs.addElement(a);
		attrs.addElement(b);
		attrs.addElement(c);
		Instances inss = new Instances("inss", attrs,100);
		Instance ins;
		String cValue;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				ins = new Instance(3);
				ins.setValue(a, i);
				ins.setValue(b, j);
				if(i < 7 && (Math.random() < 0.5)) {
					if(j < 6) {
						cValue = "raise";
					} else {
						cValue = "check";
					}
				} else {
					cValue = "fold";
				}
				ins.setValue(c,cValue);
				inss.add(ins);
			}
		}
		
		inss.setClass(c);
		NaiveBayes nb = new NaiveBayes();
		try {
			nb.buildClassifier(inss);
		} catch(Exception e) {
			
		}
		System.out.println(nb.toString());
		
		Instance test = new Instance(3);
		test.setDataset(inss);
		test.setValue(a, 2);
		test.setValue(b, 8);
		try {
			 System.out.println("class: " + nb.classifyInstance(test));
			 double[] dist = nb.distributionForInstance(test);
			 for(double d : dist) {
				 System.out.println(d);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
