package album.pagenetsoft.com.gallery;


import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Sergey on 13.02.2018.
 *
 */

@Slf4j
public class MediaObserver extends ContentObserver {

    private MediaPresenter presenter;

    public MediaObserver(Handler handler, MediaPresenter presenter) {
        super(handler);
        this.presenter = presenter;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        log.debug("onChange self={}",selfChange);
        presenter.reloadGalleryAsync2();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        log.debug("onChange self={} Uri={}",selfChange, uri==null? "null" : uri.toString() );
    }
}
