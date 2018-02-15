package album.pagenetsoft.com.gallery;

import android.graphics.Bitmap;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Sergey on 13.02.2018.
 *
 */

@Data
@EqualsAndHashCode(exclude={"md5", "bitmap", "title", "listId" })
public class MediaEntity {

    private long id;
    private String data;
    private int size;
    private String title;
    private final int listId;

    Bitmap bitmap;
    private String md5;

    public MediaEntity(long id, String data, int size, String title, int listId) {
        this.id = id;
        this.data = data;
        this.size = size;
        this.title = title;
        this.listId = listId;
    }
}
