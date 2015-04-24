/*******************************************************************************
 * @author Steve Grasso
 *
 * represents a single observation, containing all features and the target
 *
 */

public class Observation {
	private float[] _features;
	private float _target;

	public Observation(float target, float[] features) {
		_target = target;
		_features = features;
	}

	public float[] getFeatures() {
		return _features;
	}

	public float getTarget() {
		return _target;
	}
}