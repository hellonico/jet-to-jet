import static java.util.Collections.singletonList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.datamodel.TimestampedEntry;

import boofcv.abst.scene.ImageClassifier.Score;

/**
 * Print maximum scores
 */
public class LogSink extends AbstractProcessor {

	static PrintWriter out;
	static {
		FileWriter fw;
		try {
			fw = new FileWriter("scores.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void init(Context context) throws Exception {
		
	}

	@Override
	protected boolean tryProcess(int ordinal, Object item) {
		TimestampedEntry<String, Entry<SerializableBufferedImage, Entry<String, Double>>> timestampedEntry = (TimestampedEntry<String, Entry<SerializableBufferedImage, Entry<String, Double>>>) item;
		Entry<SerializableBufferedImage, Entry<String, Double>> imageEntry = timestampedEntry.getValue();
		Entry<String, Double> category = imageEntry.getValue();
		Score score = new Score();
		score.set(category.getValue(), 0);
		out.printf("%s %s %s %s \n", timestampedEntry.getKey(), timestampedEntry.getTimestamp(),
						singletonList(score), singletonList(category.getKey()));
		out.flush();
		return true;
	}

	@Override
	public boolean isCooperative() {
		return false;
	}

}
