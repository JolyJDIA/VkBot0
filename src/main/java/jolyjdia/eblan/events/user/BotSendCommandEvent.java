package jolyjdia.eblan.events.user;

import jolyjdia.eblan.events.JEvent;
import jolyjdia.eblan.objects.User;
import org.bukkit.event.Cancellable;

public class BotSendCommandEvent extends JEvent implements Cancellable {
    private final String[] args;
    private final User user;

    public BotSendCommandEvent(User user, String[] args) {
        this.user = user;
        this.args = args;
    }
    public final User getUser() {
        return user;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
