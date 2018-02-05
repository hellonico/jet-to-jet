import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static com.hazelcast.jet.core.processor.Processors.accumulateByFrameP;
import java.util.Map.Entry;

import com.hazelcast.jet.core.Processor;
import com.hazelcast.jet.core.TimestampKind;
import com.hazelcast.jet.core.WindowDefinition;
import com.hazelcast.jet.datamodel.TimestampedEntry;
import com.hazelcast.jet.function.DistributedSupplier;

public class Helper {
	public static DistributedSupplier myCounting(WindowDefinition tumbling) {
		DistributedSupplier<Processor> accumulateByFrameP = accumulateByFrameP(
				(TimestampedEntry<String, String> input) -> "COUNT",
				TimestampedEntry::getTimestamp,
        TimestampKind.EVENT,
        tumbling,
        counting());
		return accumulateByFrameP;
	}
}
