package jolyjdia.eblan;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import jolyjdia.eblan.command.CommandManager;
import jolyjdia.eblan.commands.ExtraCommands;
import jolyjdia.eblan.module.ModuleLoader;
import jolyjdia.eblan.modules.Cities;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Bot extends JavaPlugin {
    public static final UserActor VALERA = new UserActor(0,"");
    public static final UserActor ZAVR = new UserActor(0, "");
    private static final Properties properties = new Properties();
    private static final TransportClient transportClient = new HttpTransportClient();
    private static final VkApiClient vkApiClient = new VkApiClient(transportClient);
    private static final ModuleLoader moduleLoader = new ModuleLoader();
    private static final int groupId;
    private static final String accessToken;
    private static final GroupActor groupActor;
    private Thread leader;

    static {
        try (InputStream inputStream = Bot.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupId = Integer.parseInt(properties.getProperty("groupId"));
        accessToken = properties.getProperty("accessToken");
        groupActor = new GroupActor(groupId, accessToken);
    }

    @Override
    public void onEnable() {
        moduleLoader.registerModule(new Cities(this));
        moduleLoader.enableModule();
        CommandManager.registerCommand(new ExtraCommands(this));
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(this, vkApiClient, groupActor);
        (leader = new Thread(() -> {
            try {
                handler.run();
            } catch (ClientException | ApiException e) {
                e.printStackTrace();
            }
        }, "Bot | Main Thread")).start();
        /**getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                System.out.println("online");
                vkApiClient.account().setOnline(ZAVR).execute();
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        }, 0, 5500);*/
    }
    public static void save() {
        new Thread(() -> {
            int i = 702;
            try {
                final List<Photo> list = vkApiClient.photos().get(VALERA)
                        .ownerId(526616439)
                        .albumId("saved")
                        .count(148)
                        .execute()
                        .getItems();
                for (Photo photo : list) {
                    PhotoSizes photoSizes = photo.getSizes().get(photo.getSizes().size() - 1);
                    BufferedImage image = ImageIO.read(photoSizes.getUrl());
                    if (image == null) {
                        continue;
                    }
                    ImageIO.write(image, "png", new File("D:\\JolyJDIA\\Pictures\\saved\\" + (i++) + ".png"));
                   // Thread.sleep(20);
                }
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDisable() {
        leader.interrupt();
    }

    public static int getGroupId() {
        return groupId;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static GroupActor getGroupActor() {
        return groupActor;
    }

    public static TransportClient getTransportClient() {
        return transportClient;
    }

    public static VkApiClient getVkApiClient() {
        return vkApiClient;
    }

    public static ModuleLoader getModuleLoader() {
        return moduleLoader;
    }

    public static Properties getProperties() {
        return properties;
    }
}
