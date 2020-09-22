package jolyjdia.eblan.events.user;

public class BotUserLeaveEvent extends UserEvent {

    public BotUserLeaveEvent(int peerId, int userId) {
        super(peerId, userId);
    }
}
