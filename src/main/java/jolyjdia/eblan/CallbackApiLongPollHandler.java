package jolyjdia.eblan;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.board.TopicComment;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAction;
import com.vk.api.sdk.objects.messages.MessageActionStatus;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.Wallpost;
import jolyjdia.eblan.command.CommandManager;
import jolyjdia.eblan.events.messages.BotNewMessageEvent;
import jolyjdia.eblan.events.user.BotSendCommandEvent;
import jolyjdia.eblan.events.user.BotUserJoinEvent;
import jolyjdia.eblan.events.user.BotUserLeaveEvent;
import jolyjdia.eblan.objects.User;
import jolyjdia.eblan.utils.StringBind;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {
    //  private final CommandExecutor executor = new CommandExecutor(Runtime.getRuntime().availableProcessors());
    private final Plugin plugin;
    CallbackApiLongPollHandler(Plugin plugin, VkApiClient client, GroupActor actor) {
        super(client, actor);
        this.plugin = plugin;
    }

    @Override
    public final void messageNew(Integer groupId, @NotNull Message msg) {
      //  if(msg.getPeerId().equals(msg.getFromId()) || msg.getFromId() < 0) {
        //    return;
        //}
        if(msg.getFromId() < 0) {
            return;
        }
        MessageAction action = msg.getAction();
        if(action != null) {
            MessageActionStatus type = action.getType();
            if(type == MessageActionStatus.CHAT_KICK_USER) {
                UserManager.deleteUser(msg.getPeerId(), msg.getFromId());
                //Удаляю из бд беседу

                BotUserLeaveEvent event = new BotUserLeaveEvent(msg.getPeerId(), msg.getFromId());
                Bukkit.getPluginManager().callEvent(event);
                return;
            } else if(type == MessageActionStatus.CHAT_INVITE_USER || type == MessageActionStatus.CHAT_INVITE_USER_BY_LINK) {
                BotUserJoinEvent event = new BotUserJoinEvent(msg.getPeerId(), msg.getFromId());
                Bukkit.getPluginManager().callEvent(event);
                return;
            }
        }
        @NonNls String text = msg.getText();
        User user = UserManager.getChatByUser(msg.getFromId(), msg.getPeerId());
        if(text.length() > 1 && (text.charAt(0) == '/' || text.charAt(0) == '!')) {
            String[] args = text.substring(1).split(" ");
            long start = System.currentTimeMillis();
            if(CommandManager.getRegisteredCommands().stream().noneMatch(cmd -> {
                if(cmd.equalsCommand(args[0])) {
                    cmd.execute(user, args);
                    BotSendCommandEvent event = new BotSendCommandEvent(user, args);
                    Bukkit.getPluginManager().callEvent(event);
                    return true;
                }
                return false;
            })) { return; }
            @NonNls long end = System.currentTimeMillis() - start;
            StringBind.log("Команда: "+ Arrays.toString(args) +" ("+end+"ms) Чат: " +msg.getPeerId() + '(' +msg.getFromId()+ ')');
            return;
        }
        StringBind.log("Сообщение: \""+ msg.getText()+ "\" Чат: " +msg.getPeerId() + '(' +msg.getFromId()+ ')');
        BotNewMessageEvent event = new BotNewMessageEvent(user, msg);
        Bukkit.getPluginManager().callEvent(event);
        try {
            UserXtrCounters vkUser = Bot.getVkApiClient().users()
                    .get(Bot.getGroupActor())
                    .userIds(String.valueOf(msg.getFromId()))
                    .execute().get(0);
            for(Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("§c§l[VKSYNC]§b "+vkUser.getFirstName()+": §e"+text);
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
    @Override
    public final void messageReply(Integer groupId, @NotNull Message msg) {
    }

    @Override
    public final void messageEdit(Integer groupId, @NotNull Message msg) {

    }
    @Override
    public final void photoNew(Integer groupId, @NotNull Photo photo) {
    }
    @Override
    public final void wallPostNew(Integer groupId, Wallpost wallpost) {

    }
    @Override
    public final void wallRepost(Integer groupId, Wallpost wallpost) {

    }
    @Override
    public final void boardPostNew(Integer groupId, TopicComment comment) {

    }
    @Override
    public final void boardPostEdit(Integer groupId, TopicComment comment) {

    }
    @Override
    public final void boardPostRestore(Integer groupId, TopicComment comment) {

    }
}