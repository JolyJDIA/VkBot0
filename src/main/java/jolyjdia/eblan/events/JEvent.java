package jolyjdia.eblan.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class JEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public JEvent() {
        super(true);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
