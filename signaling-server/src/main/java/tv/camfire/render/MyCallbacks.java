package tv.camfire.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

/**
 * User: jonathan
 * Date: 6/17/13
 * Time: 1:42 PM
 */
class MyCallbacks implements VideoRenderer.Callbacks {
    Logger logger = LoggerFactory.getLogger(MyCallbacks.class);

//    VideoCapturer capturer = VideoCapturer.createFakeVideoCapturer();


    @Override
    public void setSize(int i, int i2) {
        logger.debug("set size...");
    }

    @Override
    public void renderFrame(VideoRenderer.I420Frame i420Frame) {
        logger.debug("Erryday I be renderin");
//        capturer.fakeSignalVideoFrame(i420Frame);
    }
}
