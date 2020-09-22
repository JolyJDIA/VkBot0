package jolyjdia.eblan;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jolyjdia.eblan.objects.Chat;
import jolyjdia.eblan.objects.User;

import java.util.concurrent.TimeUnit;

public final class UserManager {

    private UserManager() {}

    private static final LoadingCache<Integer, Chat> chats = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public Chat load(Integer integer) {
                    return new Chat(integer);
                }
            });

    public static Chat getChatByUser(int peerId) {
        return chats.getUnchecked(peerId);
    }
    //Тут добавим права пользователя
    public static User getChatByUser(int userId, int peerId) {
        Chat chat = chats.getUnchecked(peerId);
        return chat.getUsers().getUnchecked(userId);
    }
    public static void deleteUser(int userId, int peerId) {
        Chat chat =  chats.getUnchecked(peerId);
        chat.getUsers().invalidate(userId);
    }
}
