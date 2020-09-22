package jolyjdia.eblan.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.StringJoiner;

public final class StringBind {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private StringBind() {}

    public static void log(@NonNls String msg) {
        try {
            System.out.println('[' +Thread.currentThread().getName()+"]: "+msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static @NotNull String bind(int start, @NotNull String[] a) {
        int iMax = a.length;
        if (iMax == 0) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = start; i < iMax; ++i) {
            joiner.add(a[i]);
        }
        return joiner.toString();
    }
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(JsonElement json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }
    public static <T> T fromJson(Reader json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }
}
