package album.pagenetsoft.com.gallery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Sergey on 13.02.2018.
 */

public class MediaModel {

    @Getter
    @Setter
    List<MediaEntity> listMedia = Collections.synchronizedList( new ArrayList<MediaEntity>(2048));

    public MediaModel() {
        //
    }


}
