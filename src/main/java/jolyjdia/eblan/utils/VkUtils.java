package jolyjdia.eblan.utils;

import com.vk.api.sdk.objects.Validable;
import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.Wallpost;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class VkUtils {

    private VkUtils() {}

    @NonNls
    public static @NotNull String attachment(String body, int ownerId, int id) {
        return body + ownerId + '_' + id;
    }

    //[idasdasda|]
    public static Optional<Integer> getUserId(@NotNull String s) {
        try {
            return Optional.of(s.charAt(0) == '[' ? Integer.parseInt(s.substring(3).split("\\|")[0]) : Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @NonNls
    public static @NotNull String attachment(@NotNull Validable body) {
        if (Wallpost.class.isAssignableFrom(body.getClass())) {
            Wallpost wallpost = (Wallpost) body;
            return "wall" + wallpost.getOwnerId() + '_' + wallpost.getId();
        } else if (Photo.class.isAssignableFrom(body.getClass())) {
            Photo photo = (Photo) body;
            return "photo" + photo.getOwnerId() + '_' + photo.getId();
        } else if (Audio.class.isAssignableFrom(body.getClass())) {
            Audio audio = (Audio) body;
            return "audio" + audio.getId() + '_' + audio.getId();
        } else if (Video.class.isAssignableFrom(body.getClass())) {
            Video video = (Video) body;
            return "video" + video.getOwnerId() + '_' + video.getId();
        }
        return "";
    }
}