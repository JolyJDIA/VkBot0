package jolyjdia.eblan.objects;

import jolyjdia.eblan.utils.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class User {
    private final int userId;
    private final int peerId;

    public User(int userId, int peerId) {
        this.userId = userId;
        this.peerId = peerId;
    }

    /*public void sendMessage(String msg) {
        MessageChannel.sendMessage(msg, peerId);
    }*/
    public void sendMessage(@NotNull Object msg) {
        MessageChannel.sendMessage(msg.toString(), peerId);
    }

    public int getUserId() {
        return userId;
    }

    public int getPeerId() {
        return peerId;
    }
}
