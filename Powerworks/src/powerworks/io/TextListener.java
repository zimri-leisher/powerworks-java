package powerworks.io;


/**
 * Simply use InputManager.funnelKeys to handle all char input, and InputManager.stopFunneling to stop. No controls will be processed, all will go directly to text
 */
public interface TextListener {
    public void handleChar(char c);
}
