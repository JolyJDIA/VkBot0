package jolyjdia.eblan.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.photos.Photo;
import jolyjdia.eblan.Bot;
import jolyjdia.eblan.command.CommandExecutors;
import jolyjdia.eblan.command.CommandLabel;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class ExtraCommands extends CommandExecutors {
    private final Bot bot;
    public ExtraCommands(Bot bot) {
        this.bot = bot;
    }
    @CommandLabel(alias = "l")
    public void likes() {
        try {
            int ownerId = Integer.parseInt(getArgs()[1]);
            final Iterator<Photo> iterator = Bot.getVkApiClient().photos().get(Bot.ZAVR)
                    .ownerId(ownerId)
                    .albumId(getArgs()[2])
                    .execute()
                    .getItems()
                    .iterator();
            Bukkit.getScheduler().runTaskTimerAsynchronously(bot, () -> {
                if (iterator.hasNext()) {
                    try {
                        int photo = iterator.next().getId();
                        Bot.getVkApiClient().likes().add(Bot.ZAVR, Type.PHOTO, photo)
                                .ownerId(ownerId)
                                .execute();
                        Bot.getVkApiClient().likes().add(Bot.VALERA, Type.PHOTO, photo)
                                .ownerId(ownerId)
                                .execute();
                    } catch (ApiException | ClientException e) {
                        e.printStackTrace();
                    }
                    System.out.println("+ like");
                }
            }, 15, 15);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
    int i = 0;
    @CommandLabel(alias = "c")
    public void comments() {
        try {
            int ownerId = Integer.parseInt(getArgs()[1]);
            final Iterator<Photo> iterator = Bot.getVkApiClient().photos().get(Bot.ZAVR)
                    .ownerId(ownerId)
                    .albumId(getArgs()[2])
                    .execute()
                    .getItems()
                    .iterator();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (iterator.hasNext()) {
                        try {
                            int photo = iterator.next().getId();
                            String rand = ThreadLocalRandom.current().ints(256, 0, 122)
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString().repeat(5);
                            Bot.getVkApiClient().photos().createComment(Bot.ZAVR, photo)
                                    .ownerId(ownerId)
                                    .message(rand)
                                    .execute();
                            Bot.getVkApiClient().photos().createComment(Bot.VALERA, photo)
                                    .ownerId(ownerId)
                                    .message(rand);
                            System.out.println("+ comment");
                        } catch (ApiException | ClientException e) {
                            e.printStackTrace();
                        }
                        ++i;
                        if (i == 5) {
                            cancel();
                        }
                    }
                }
            }.runTaskTimerAsynchronously(bot, 0, 11);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
