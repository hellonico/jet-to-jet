import java.util.Collections;
import java.util.List;

import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.core.CloseableProcessorSupplier;
import com.hazelcast.jet.core.Processor;
import com.hazelcast.jet.core.ProcessorMetaSupplier;
import com.hazelcast.jet.core.ProcessorSupplier;
import com.hazelcast.jet.core.processor.Processors;
import com.hazelcast.jet.function.DistributedFunction;
import com.hazelcast.jet.function.DistributedSupplier;
import com.hazelcast.nio.Address;
import com.hazelcast.partition.strategy.StringPartitioningStrategy;

public class ManySourceSupplier implements ProcessorMetaSupplier {
	private Address ownerAddress;
	private String partitionKeyString;
	private Class<? extends AbstractProcessor> source;

	public ManySourceSupplier(String partitionKeyString, Class source) {
		this.partitionKeyString = partitionKeyString;
		this.source = source;
	}

	@Override
    public int preferredLocalParallelism() {
        return 1;
    }

    @Override
    public void init(Context context) {
        String partitionKey = StringPartitioningStrategy.getPartitionKey(partitionKeyString);
        ownerAddress = context.jetInstance().getHazelcastInstance().getPartitionService()
                              .getPartition(partitionKey).getOwner().getAddress();
    }

    @Override
    public DistributedFunction<Address, ProcessorSupplier> get(List<Address> addresses) {
        return address -> {
            	return new OneSupplier(source);
        };
    }
}
