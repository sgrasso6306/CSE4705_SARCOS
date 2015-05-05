import java.util.ArrayList;

import edu.uci.lasso.LassoFit;
import edu.uci.lasso.LassoFitGenerator;




public class Main {

	public static void main(String[] args) {

		DataModule dm = new DataModule();
		dm.initTrainSet();
		//dm.initTestSet();

		
		/*
		int numObservations = dm.getTrainSetObs().size();
		System.out.println("num obs: " + numObservations);
		int featuresCount = 21;
		ArrayList<float[]> features = dm.getTrainSetFeatures();
		ArrayList<Float> targets = dm.getTrainSetTargets();
		
		/*
		 * LassoFitGenerator is initialized
		 */
		/*
		LassoFitGenerator fitGenerator = new LassoFitGenerator();
		try {
			fitGenerator.init(featuresCount, numObservations);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < numObservations; i++) {
			fitGenerator.setObservationValues(i, features.get(i));
			fitGenerator.setTarget(i, targets.get(i));
		}
		
		/*
		 * Generate the Lasso fit. The -1 arguments means that
		 * there would be no limit on the maximum number of 
		 * features per model
		 */
		/*
		LassoFit fit = fitGenerator.fit(-1);
		System.out.println("rsquared: ");
		for (int i=0; i<fit.rsquared.length; i++) {
			System.out.println("i: "+i+"  r^2: "+fit.rsquared[i]);
		}
		*/
		
		
		/*
		 * Print the generated fit
		 */
		//System.out.println(fit);
		
		
		
		
		
		
		
		/*
		int i=0;
		for (Observation o : dm.getTrainSetObs()) {
			i++;
			System.out.println("Obs: "+i);
			System.out.println("      Target: "+o.getTarget());
			System.out.print("      Features: ");
			for (int j=0; j<21; j++) {
				System.out.print(o.getFeatures()[j]);
			}
			System.out.println();
		}
		*/
		
		/*
		ArrayList<float[]> features = dm.getTrainSetFeatures();
		ArrayList<Float> targets = dm.getTrainSetTargets();
		for (int i=1; i<=features.size(); i++) {
			System.out.println("Obs: "+i);
			System.out.println("      Target: "+ targets.get(i-1));
			System.out.print("      Features: ");
			for (int j=0; j<21; j++) {
				System.out.print(features.get(i-1)[j]);
			}
			System.out.println();
		}
		*/
		
	}

}
