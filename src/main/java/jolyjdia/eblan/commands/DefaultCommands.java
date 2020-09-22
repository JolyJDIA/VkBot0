package jolyjdia.eblan.commands;

import com.sun.management.OperatingSystemMXBean;
import jolyjdia.eblan.command.CommandExecutors;
import jolyjdia.eblan.command.CommandLabel;
import jolyjdia.eblan.command.CommandManager;
import jolyjdia.eblan.utils.MathUtils;
import org.bukkit.Bukkit;

import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class DefaultCommands extends CommandExecutors {
    private static final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    @CommandLabel(alias = "help", desc = "помощь по командам")
    public void help() {
        StringBuilder builder = new StringBuilder();
        for(CommandManager.Command cmd : CommandManager.getRegisteredCommands()) {
            if (cmd.getDescription() == null || cmd.getDescription().isEmpty()) {
                continue;
            }
            builder.append('/').append(cmd.getAlias()[0]);
            if (cmd.getUsage() != null && !cmd.getUsage().isEmpty()) {
                builder.append(' ').append(cmd.getUsage());
            }
            builder.append(" - ").append(cmd.getDescription()).append('\n');
        }
        getSender().sendMessage(builder);
        builder.setLength(0);
    }
    @CommandLabel(alias = {"pl", "plugins"}, desc = "плагины на сервере")
    public void plugins() {
        getSender().sendMessage(Arrays.toString(Bukkit.getServer().getPluginManager().getPlugins()));
    }
    @CommandLabel(alias = {"lag", "system"}, desc = "информация системы")
    public void lag() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        StringBuilder sb = new StringBuilder("Java version: ").append(Runtime.version())
                .append("\nПамять:")
                .append("\n Max: ").append(runtime.maxMemory() / 1048576L).append(" МB")
                .append("\n Total: ").append(totalMemory / 1048576L).append(" МB")
                .append("\n Use: ").append((totalMemory - freeMemory) / 1048576L).append(" МB")
                .append(" (").append(freeMemory / 1048576L).append(" MB свободно)")
                .append("\nПроцессы:")
                .append("\n Ядер: ").append(runtime.availableProcessors())
                .append("\n Использовано сервером: ").append(Math.round(osBean.getProcessCpuLoad()*100)).append('%')
                .append("\n Использовано системой: ").append(Math.round(osBean.getSystemCpuLoad()*100)).append('%');
        getSender().sendMessage(sb);
        sb.setLength(0);
    }
    @CommandLabel(alias = "roll", usage = "<Мин> <Макс>", desc = "случайное число в заданном диапазоне")
    public void roll() {
        if(getArgs().length == 1) {
            getSender().sendMessage(MathUtils.randomInt(100));
        } else if(getArgs().length >= 2 && getArgs().length < 4) {
            try {
                int start = Integer.parseInt(getArgs()[1]);
                if (getArgs().length == 2) {
                    getSender().sendMessage(MathUtils.randomInt(start));
                } else {
                    int end = Integer.parseInt(getArgs()[2]);
                    if(end < start) {
                        getSender().sendMessage("Максимальное число должно быть больше минимального");
                        return;
                    }
                    getSender().sendMessage(MathUtils.randomInt(start, end));
                }
            } catch (NumberFormatException e) {
                getSender().sendMessage("Это не число!");
            }
        }
    }
    @CommandLabel(alias = "flip", desc = "орел или решка")
    public void flip() {
        getSender().sendMessage(MathUtils.randomBoolean() ? "Орел" : "Решка");
    }
}
