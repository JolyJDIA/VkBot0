package jolyjdia.eblan.events.messages;

import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.eblan.events.JEvent;
import jolyjdia.eblan.objects.User;

public class MessageEvent extends JEvent {
    private final Message msg;
    private final User user;

    public MessageEvent(User user, Message msg) {
        this.msg = msg;
        this.user = user;
    }

    public final User getUser() {
        return user;
    }

    public final Message getMessage() {
        return msg;
    }
}