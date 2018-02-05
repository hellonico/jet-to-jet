import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import com.hazelcast.jet.Traverser;
import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.datamodel.TimestampedEntry;

import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.webcamcapture.UtilWebcamCapture;

/**
 * A source that emits the frames captured from webcam stream.
 * Also creates an GUI to show current captures.
 */
public class WebcamSource extends AbstractProcessor implements Closeable {

    private Traverser<TimestampedEntry> traverser;
    private Webcam webcam;
    private ImagePanel gui;
    private long lastPoll;
    private long intervalMillis = 500;

    @Override
    protected void init(Context context) throws Exception {
        super.init(context);
        webcam = UtilWebcamCapture.openDefault(320, 240);
        gui = new ImagePanel();
        gui.setPreferredSize(webcam.getViewSize());
        ShowImages.showWindow(gui, "Webcam Input", true);
    }

    @Override
    public boolean complete() {
        if (traverser == null) {
            long now = System.currentTimeMillis();
            if (now > lastPoll + intervalMillis) {
                lastPoll = now;
                BufferedImage image = webcam.getImage();
                gui.setImageRepaint(image);
                traverser = Traverser.over(new TimestampedEntry<>(now, new SerializableBufferedImage(image), null));
            } else {
                return false;
            }
        }
        if (emitFromTraverser(traverser)) {
            traverser = null;
        }
        return false;
    }

    @Override
    public boolean isCooperative() {
        return false;
    }

    @Override
    public void close() throws IOException {
        webcam.close();
    }


}
