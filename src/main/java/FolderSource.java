import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.hazelcast.jet.Traverser;
import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.datamodel.TimestampedEntry;

import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;

/**
 * A source that emits the frames taken from a folder
 * Also creates an GUI to show current captures.
 */
public class FolderSource extends AbstractProcessor implements Closeable {

    private Traverser<TimestampedEntry> traverser;
    private File base = new File("/Users/niko/Desktop/images"); 
    private ImagePanel gui;
    private long lastPoll;
    private long intervalMillis = 500;

    @Override
    protected void init(Context context) throws Exception {
        super.init(context);
        gui = new ImagePanel();
        base.mkdirs();
        ShowImages.showWindow(gui, "Folder Input", true);
    }

    @Override
    public boolean complete() {
        if (traverser == null) {
            long now = System.currentTimeMillis();
            if (now > lastPoll + intervalMillis) {
                lastPoll = now;
                File[] files = 
                		base.listFiles(pathname -> pathname.getAbsolutePath().endsWith(".jpg"));
                if(files.length==0)
                	  return false;
                File img = files[0];
                BufferedImage bi;
				try {
					bi = ImageIO.read(img);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
                gui.setImageRepaint(bi);
                traverser = Traverser.over(new TimestampedEntry<>(now, new SerializableBufferedImage(bi), null));
                img.delete();
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

    }

}
