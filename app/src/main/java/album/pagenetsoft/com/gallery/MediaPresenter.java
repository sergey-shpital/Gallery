package album.pagenetsoft.com.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Sergey on 13.02.2018.
 *
 */

@Slf4j
public class MediaPresenter extends MvpBasePresenter<MediaView> {

    private final MediaModel model = new MediaModel();

    private final ExecutorService pool ;
    private ContentResolver contentResolver;

    private MediaObserver mediaObserver = new MediaObserver(null, this);

    private final static String[] columns = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.TITLE
    };

    @Getter
    private boolean started = false;

    private AtomicLong lastRowid = new AtomicLong(-1);
    private AtomicBoolean running = new AtomicBoolean(false);

    public MediaPresenter() {
        int processors = Runtime.getRuntime().availableProcessors();
        pool = Executors.newFixedThreadPool(processors + 1 );
    }

    public void initGallery(AppCompatActivity activity) {
        if(started) showNewList();
        else {
            started = true;
             loadGalleryAsync2(activity);
        }
     }

    public void closeGalery()   {
        started = false;
        ifViewAttached( (MediaView view)->view.setArrayList( null ) );
    }

    @Override
    public void attachView(MediaView view) {
        super.attachView(view);
        if(started) {
            showNewList();
        }
    }

    private static void inflateMediaEntity(MediaEntity mediaEntity) {
        Bitmap bmp = MediaUtils.getResizedBitmap(mediaEntity.getData(), 72, 72);
        mediaEntity.setBitmap(bmp);

        String md5 = MediaUtils.getFileChecksum(mediaEntity.getData());
        mediaEntity.setMd5(md5);
    }


    public void reloadGalleryAsync2() {
        if( started && !running.get() ) {
            pool.execute(()->loadGallery( contentResolver) );
        }
    }

    public void loadGalleryAsync2(Context context) {

        if(contentResolver==null) {
            contentResolver = context.getApplicationContext().getContentResolver();
            registerObserver(contentResolver);
        }

        pool.execute(()->loadGallery( contentResolver) );
    }

    public void registerObserver( ContentResolver contentResolver ) {

        contentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mediaObserver );
    }


    public void loadGallery( ContentResolver contentResolver ) {

        running.set(true);

        Cursor curExternal = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                String.format(" %s > %d ",MediaStore.Images.Media._ID, lastRowid.get() ),
                null,
                null
        );

        HashMap<String, Integer> hashMap = new HashMap<>();

        for (String nameCol : columns) {
            hashMap.put(nameCol, curExternal.getColumnIndex(nameCol));
        }

        List<MediaEntity> listMedia = model.getListMedia();

        showNewList();

        int listPos = listMedia.size();
        try {
            while (curExternal.moveToNext()) {
                MediaEntity mediaEntity = new MediaEntity(
                        curExternal.getLong(hashMap.get(columns[0])),
                        curExternal.getString(hashMap.get(columns[1])),
                        curExternal.getInt(hashMap.get(columns[2])),
                        curExternal.getString(hashMap.get(columns[3])),
                        listPos
                );

                listMedia.add(mediaEntity);

                showNewList();

                addToList(listPos++);

                lastRowid.set( mediaEntity.getId() );

                pool.execute(()->{
                    inflateMediaEntity(mediaEntity);
                    updateLast( mediaEntity.getListId() );
                });

            }
            //!
            curExternal.close();

        }catch (Exception e ) {
            log.debug("ERROR:{}",e);
        }

        running.set(false);
    }

    void showNewList() {
        ifViewAttached( (MediaView view)->view.setArrayList( model.getListMedia()) );
    }

    void addToList( int id ) {
        ifViewAttached( (MediaView view)->view.addedItem( id ) );
    }

    void addToList( int id, int count ) {
        ifViewAttached( (MediaView view)->view.addedBlock( id, count ) );
    }


    void updateLast( int id ) {
        ifViewAttached( (MediaView view)->view.updatedBlock( id, 1 ) );
    }

    @Override
    public void destroy() {
        if(contentResolver!=null) {
            contentResolver.unregisterContentObserver(mediaObserver);
            contentResolver = null;
        }
        super.destroy();
    }
}


