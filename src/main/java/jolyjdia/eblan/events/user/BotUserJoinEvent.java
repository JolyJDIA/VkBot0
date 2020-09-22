package jolyjdia.eblan.events.user;

public class BotUserJoinEvent extends UserEvent {

    public BotUserJoinEvent(int peerId, int userId) {
        super(peerId, userId);
    }
}
