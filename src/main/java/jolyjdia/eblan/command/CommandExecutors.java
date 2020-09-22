package jolyjdia.eblan.command;

import jolyjdia.eblan.objects.User;

public class CommandExecutors {
    private User sender;
    private String[] args;

    public void accept(User sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public User getSender() {
        return sender;
    }
}
