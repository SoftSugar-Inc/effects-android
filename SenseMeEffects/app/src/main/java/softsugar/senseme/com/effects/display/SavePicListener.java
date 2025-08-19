package softsugar.senseme.com.effects.display;

import java.nio.ByteBuffer;

public interface SavePicListener {
    void onSuccess(ByteBuffer tmpBuffer, int width, int height);
}
