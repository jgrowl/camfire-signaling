package tv.camfire;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * User: jonathan
 * Date: 4/9/13
 * Time: 10:56 AM
 */
public class SdpObserverLatch implements SdpObserver {
    private boolean success = false;
    private SessionDescription sdp = null;
    private String error = null;
    private CountDownLatch latch = new CountDownLatch(1);

    public SdpObserverLatch() {}

    public boolean await() {
        try {
            latch.await(1000, TimeUnit.MILLISECONDS);
            return getSuccess();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getSuccess() {
        return success;
    }

    public SessionDescription getSdp() {
        return sdp;
    }

    public String getError() {
        return error;
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        this.sdp = sessionDescription;
        onSetSuccess();
    }

    @Override
    public void onSetSuccess() {
        success = true;
        latch.countDown();
    }

    @Override
    public void onCreateFailure(String s) {
        onSetFailure(s);
    }

    @Override
    public void onSetFailure(String s) {
        this.error = s;
        latch.countDown();
    }
}
