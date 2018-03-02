package album.pagenetsoft.com.gallery;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Sergey on 02.03.2018.
 */
public class MediaEntityTest {

    @Test
    public void compare() {

        MediaEntity me1 = new MediaEntity(1, "2", 3, "4", 1);
        MediaEntity me2 = new MediaEntity(1, "2", 3, "4", 2);
        MediaEntity me3 = new MediaEntity(1, "3", 3, "4", 3);

        assertEquals(me1,me2);
        assertNotEquals(me1,me3);
    }

}