package album.pagenetsoft.com.gallery;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

/**
 * Created by Sergey on 13.02.2018.
 *
 */

public interface MediaView extends MvpView {
    void setArrayList( List<MediaEntity> arrayList );
    void addedItem( int id );
    void addedBlock( int id, int count );
    void updatedBlock( int id, int count );
}
