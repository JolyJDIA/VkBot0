package jolyjdia.eblan.modules;

import com.google.gson.reflect.TypeToken;
import jolyjdia.eblan.Bot;
import jolyjdia.eblan.command.CommandExecutors;
import jolyjdia.eblan.command.CommandLabel;
import jolyjdia.eblan.command.CommandManager;
import jolyjdia.eblan.events.messages.BotNewMessageEvent;
import jolyjdia.eblan.module.Module;
import jolyjdia.eblan.utils.MathUtils;
import jolyjdia.eblan.utils.StringBind;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Cities extends CommandExecutors implements Module, Listener {
    private Map<Character, List<String>> map;
    private final Bot bot;
    //private char lastLetter;
    private static final int SKIP =
            1 << 'ь' |
            1 << 'ъ' |
            1 << 'ы' ;
    private final Map<Integer, User> users = new HashMap<>();

    public Cities(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onLoad() {
        try (FileInputStream fileInputStream = new FileInputStream(
                new File("C:\\Users\\Администратор\\IdeaProjects\\VkBotMinecraft\\src\\main\\resources\\cities.json")
        ); InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            this.map = Collections.unmodifiableMap(StringBind.fromJson(inputStreamReader, new MapTypeToken().getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CommandManager.registerCommand(this);
        Bukkit.getPluginManager().registerEvents(this, bot);
    }

    @EventHandler
    public void onCities(BotNewMessageEvent e) {
        String word = e.getMessage().getText();
        if(word.length() < 1) {
            return;
        }
        User user = users.computeIfAbsent(e.getUser().getPeerId(), integer -> new User());
        if(user.check(word)) {
            e.getUser().sendMessage(user.randCity());
        }
    }
    @CommandLabel(alias = "города")
    public void addGameToChat() {
        getSender().sendMessage(users.computeIfAbsent(getSender().getPeerId(), integer -> new User()).randCity());
    }

    private static class MapTypeToken extends TypeToken<Map<Character, List<String>>> {}

    private final class User {
        private char lastLetterU;
        private final Map<Character, List<String>> mapPrivate = new HashMap<>(Cities.this.map);

        public User() {
            Set<Character> characters = map.keySet();
            lastLetterU = (char) characters.toArray()[MathUtils.randomInt(characters.size())];
        }

        private String randCity() {
            List<String> cities = mapPrivate.get(lastLetterU);
            String city = cities.get(MathUtils.randomInt(cities.size()));
            for(int i = city.length()-1; i > 0; --i) {
                char c = city.charAt(i);
                if(!Character.isLetter(c) || (SKIP >> (int) c & 1) != 0) {
                    continue;
                }
                lastLetterU = Character.toUpperCase(c);
                cities.remove(city);
                return city;
            }
            return randCity();
        }
        public boolean check(String word) {
            char firstChar = Character.toUpperCase(word.charAt(0));

            if(!Character.isLetter(firstChar) || firstChar != lastLetterU) {
                return false;
            }
            List<String> city;
            if((city = map.get(firstChar)) != null && city.contains(word)) {
                for(int i = word.length()-1; i > 0; --i) {
                    char c = word.charAt(i);
                    if (Character.isLetter(c) && (SKIP >> (int) c & 1) == 0) {
                        lastLetterU = Character.toUpperCase(c);
                        break;
                    }
                }
                mapPrivate.get(firstChar).remove(word);
                return true;
            }
            return false;
        }
    }
}
