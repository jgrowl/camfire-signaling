package tv.camfire.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webrtc.VideoRenderer;

/**
 * User: jonathan
 * Date: 4/16/13
 * Time: 12:24 PM
 */
public class Renderer extends VideoRenderer {

    Logger logger = LoggerFactory.getLogger(Renderer.class);

    public Renderer() {
        super(new MyCallbacks());
    }

}
