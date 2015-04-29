import java.util.ArrayList;




public class Main {

	public static void main(String[] args) {

		DataModule dm = new DataModule();
		dm.initTrainSet();
		dm.initTestSet();
		

		
		
		
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
