import com.hazelcast.jet.core.CloseableProcessorSupplier;
import com.hazelcast.jet.core.Processor;
import com.hazelcast.jet.function.DistributedSupplier;

public class OneSupplier extends CloseableProcessorSupplier {

	public OneSupplier(Class target) {
		super(new DistributedSupplier() {
			public Processor get() {
				try {
					return (Processor) target.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
}