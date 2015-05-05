
import java.util.ArrayList;

import la.matrix.Matrix;
import ml.options.Options;
import ml.regression.LASSO;
import ml.regression.Regression;


/**
 */
public class LassoRegression {

	public static void main(String[] args) throws Exception {

		
		// Create regression model from training data
		DataModule trainDm = new DataModule();
		trainDm.initTrainSet();

		DataModule Test_dm = new DataModule();
		Test_dm.initTestSet();
		
		ArrayList<float[]> featuresFloat = trainDm.getTrainSetFeatures();
		ArrayList<Float> targets = trainDm.getTrainSetTargets();
		
		double[][] data = new double[featuresFloat.size()][featuresFloat.get(0).length];
		double[][] depVars = new double[targets.size()][0];
		
		/*
		for (int i = 0; i < featuresFloat.size(); i++) {
			data[i] = convertFloatsToDoubles(featuresFloat.get(i));
			double[] temp = new double[1]; 
			temp[0] = targets.get(i);
			depVars[i] = temp;
			
		}
		*/
		
		for (int i = 0; i < featuresFloat.size(); i++) {
			double[] temp = new double[1]; 
			temp[0] = targets.get(i);
			depVars[i] = temp;
			for (int j=0; j<featuresFloat.get(i).length; j++) {
				data[i][j] = featuresFloat.get(i)[j];
			}
			
		}
			
		Options options = new Options();
		options.maxIter = 70;
		options.lambda = 0.0366;
		//options.lambda = 0.02;
		options.verbose = !true;
		options.calc_OV = !true;
		options.epsilon = 1e-5;
		Regression LASSO = new LASSO(options);
		LASSO.feedData(data);
		LASSO.feedDependentVariables(depVars);

		LASSO.train();
		// Use model to predict targets
		
		
		ArrayList<float[]> testFeaturesFloat = Test_dm.getTestSetFeatures();
		ArrayList<Float> testTargets = Test_dm.getTestSetTargets();
		
		/*
		for (int i = 0; i < testFeaturesFloat.size(); i++) {
			testData[i] = convertFloatsToDoubles(featuresFloat.get(i));
			
		}
		*/
		
		double[][] testData = new double[testFeaturesFloat.size()][testFeaturesFloat.get(0).length];
		for (int i = 0; i < testFeaturesFloat.size(); i++) {
			for (int j=0; j<testFeaturesFloat.get(i).length; j++) {
				testData[i][j] = testFeaturesFloat.get(i)[j];
			}
			
		}
				
		Matrix Yt = LASSO.predict(testData);
		
		double[] originalTargets = new double[500];
		double[] predictedTargets = new double[500];
		
		double meanR2 = 0;
		
		for (int i=0; i<testTargets.size(); i++) {
			System.out.println("Observation " + (i+1) + " target " + testTargets.get(i).toString() + " prediction " + String.valueOf(Yt.getEntry(i, 0)));
			originalTargets[i] = testTargets.get(i);
			predictedTargets[i] = Yt.getEntry(i, 0);
			double r2 = r_square(predictedTargets,originalTargets);
			System.out.println("R2 = " + r2);
			meanR2 += r2;
		}
		
		meanR2 = meanR2/testTargets.size();
		System.out.println("Mean R2: "+meanR2);
		
	}
	
	public static double r_square(double[] estimates,
			double[] actuals) {
		double SSE = 0;
		double SST = 0;
		double y_bar = 0;
		for (int i = 0; i < 500; i++) {
			y_bar += actuals[i];
		}
		y_bar = y_bar / 500;
		// SSE
		for (int i = 0; i < 500; i++) {
			SSE = SSE + Math.pow(actuals[i] - estimates[i], 2);
			SST = SST + Math.pow(actuals[i] - y_bar, 2);
		}
		// System.out.println("SSE =" +SSE + " SST =" + SST);
		return (double) 1.0 - (SSE / SST);
	}
	
	public static double[] convertFloatsToDoubles(float[] input)
	{
	    if (input == null)
	    {
	        return null; // Or throw an exception - your choice
	    }
	    double[] output = new double[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        output[i] = input[i];
	    }
	    return output;
	}
}