package jolyjdia.eblan.command;

import jolyjdia.eblan.commands.DefaultCommands;
import jolyjdia.eblan.objects.User;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class CommandManager {
    private static final Set<Command> COMMAND_SET = new HashSet<>();

    static {
        registerCommand(new DefaultCommands());
    }

    private CommandManager() {}

    public static void registerCommand(@NotNull CommandExecutors command) {
        for (Method method : command.getClass().getMethods()) {
            if (!method.isAnnotationPresent(CommandLabel.class)) {
                continue;
            }
            if (method.getParameterTypes().length > 0) {
                continue;
            }
            CommandLabel label = method.getAnnotation(CommandLabel.class);
            Command handler = new Command(() -> {
                try {
                    method.invoke(command);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }, command, label.alias(), label.minArg(), label.maxArg());
            handler.setUsage(label.usage());
            handler.setDescription(label.desc());
            COMMAND_SET.add(handler);
        }
    }

    public static Set<Command> getRegisteredCommands() {
        return COMMAND_SET;
    }

    public static void unregisterAllCommands() {
        COMMAND_SET.clear();
    }
    public static class Command {
        private final String[] alias;
        private final Runnable runnable;
        private final CommandExecutors consumer;
        private final int minArg, maxArg;
        private String description;
        private String usage;

        Command(Runnable runnable,
                CommandExecutors consumer,
                String @NotNull [] alias,
                int minArg,
                int maxArg
        ) {
            this.alias = alias;
            this.runnable = runnable;
            this.consumer = consumer;
            this.minArg = minArg;
            this.maxArg = maxArg;
        }


        public void execute(@NotNull User sender, @NotNull String[] args) {
            if((args.length >= minArg && args.length <= maxArg) || maxArg == -1) {
                consumer.accept(sender, args);
                runnable.run();
            } else {
                if(!usage.isEmpty()) {
                    sender.sendMessage(usage);
                }
            }
        }

        public String getDescription() {
            return description;
        }

        public String getUsage() {
            return usage;
        }

        public String[] getAlias() {
            return alias;
        }

        public int getMaxArg() {
            return maxArg;
        }

        public int getMinArg() {
            return minArg;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public boolean equalsCommand(String s2) {
            if (alias.length == 0) {
                return false;
            }
            for(String a : alias) {
                if(a.equalsIgnoreCase(s2)) {
                    return true;
                }
            }
            return false;
        }
    }
}