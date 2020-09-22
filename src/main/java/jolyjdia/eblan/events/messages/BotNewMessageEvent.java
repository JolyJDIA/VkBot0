package jolyjdia.eblan.events.messages;

import com.vk.api.sdk.objects.messages.Message;
import jolyjdia.eblan.objects.User;

public class BotNewMessageEvent extends MessageEvent {
    public BotNewMessageEvent(User user, Message msg) {
        super(user, msg);
    }
}
