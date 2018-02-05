import static java.util.Collections.singletonList;

import java.util.Map.Entry;

import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.datamodel.TimestampedEntry;

import boofcv.abst.scene.ImageClassifier.Score;
import boofcv.gui.ImageClassificationPanel;
import boofcv.gui.image.ShowImages;

/**
 * A GUI which will show the frames with the maximum classification scores.
 */
public class GUISink extends AbstractProcessor {

    private ImageClassificationPanel panel;

    @Override
    protected void init(Context context) throws Exception {
        panel = new ImageClassificationPanel();
        ShowImages.showWindow(panel, "Results", true);
    }

    @Override
    protected boolean tryProcess(int ordinal, Object item) throws Exception {
        TimestampedEntry<String, Entry<SerializableBufferedImage, Entry<String, Double>>> timestampedEntry = (TimestampedEntry<String, Entry<SerializableBufferedImage, Entry<String, Double>>>) item;
        Entry<SerializableBufferedImage, Entry<String, Double>> imageEntry = timestampedEntry.getValue();
        SerializableBufferedImage image = imageEntry.getKey();
        Entry<String, Double> category = imageEntry.getValue();
        Score score = new Score();
        score.set(category.getValue(), 0);
        panel.addImage(image.getImage(), timestampedEntry.getKey() + timestampedEntry.getTimestamp(), singletonList(score), singletonList(category.getKey()));
        return true;
    }

    @Override
    public boolean isCooperative() {
        return false;
    }

}
