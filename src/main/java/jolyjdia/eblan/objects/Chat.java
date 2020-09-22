package jolyjdia.eblan.objects;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import jolyjdia.eblan.Bot;
import jolyjdia.eblan.utils.MessageChannel;

import java.util.concurrent.TimeUnit;

public class Chat {
    private final int peerId;
    private final LoadingCache<Integer, User> container;

    public Chat(int peerId) {
        this.peerId = peerId;
        this.container = CacheBuilder.newBuilder()
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public User load(Integer userId) {
                        return new User(userId, peerId);
                    }
                });
    }

    public final int getPeerId() {
        return peerId;
    }
    public final void editChat(String title) {
        MessageChannel.editChat(title, peerId);
    }
    public final void sendMessage(String msg) {
        MessageChannel.sendMessage(msg, peerId);
    }
    public final void sendAttachments(String attachments) {
        MessageChannel.sendAttachments(attachments, peerId);
    }
    public final void sendMessage(String msg, String attachments) {
        MessageChannel.sendMessage(msg, peerId, attachments);
    }

    public final void sendKeyboard(String msg, Keyboard keyboard) {
        MessageChannel.sendKeyboard(msg, peerId, keyboard);
    }

    public final LoadingCache<Integer, User> getUsers() {
        return container;
    }
    public static boolean isOwner(int peerId, int userId) {
        try {
            return Bot.getVkApiClient().messages().getConversationMembers(Bot.getGroupActor(), peerId)
                    .execute().getItems()
                    .stream()
                    .anyMatch(e -> {
                        if (e.getMemberId() != userId) {
                            return false;
                        }
                        Boolean isOwner = e.getIsOwner();
                        Boolean isAdmin = e.getIsAdmin();
                        return (isOwner != null && isOwner) || (isAdmin != null && isAdmin);
                    });
        } catch (ApiException | ClientException e) {
            return false;
        }
    }
}
