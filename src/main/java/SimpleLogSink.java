import com.hazelcast.jet.core.AbstractProcessor;

/**
 * Print maximum scores
 */
public class SimpleLogSink extends AbstractProcessor {

	@Override
	protected void init(Context context) throws Exception {
		
	}

	@Override
	protected boolean tryProcess(int ordinal, Object item) {
		System.out.printf("%s \n", item);
		return true;
	}

	@Override
	public boolean isCooperative() {
		return false;
	}

}
