package jolyjdia.eblan.events.user;

import jolyjdia.eblan.events.JEvent;

public class UserEvent extends JEvent {
    private final int peerId;
    private final int userId;

    public UserEvent(int peerId, int userId) {
        this.peerId = peerId;
        this.userId = userId;
    }

    public final int getPeerId() {
        return peerId;
    }

    public final int getUserId() {
        return userId;
    }
}
