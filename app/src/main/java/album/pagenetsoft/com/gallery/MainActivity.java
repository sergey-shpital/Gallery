package album.pagenetsoft.com.gallery;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.extern.slf4j.Slf4j;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@Slf4j
@RuntimePermissions
public class MainActivity
        extends MvpActivity< MediaView, MediaPresenter >
        implements MediaView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.buttonGo)
    Button button;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MediaAdapter mediaAdapter = new MediaAdapter();

    private boolean go = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter( mediaAdapter );

        progressBar.setMax(100);


        if( getPresenter().isStarted() ) {
            setGo(true);
        }

    }

    @NonNull
    @Override
    public MediaPresenter createPresenter() {
        return new MediaPresenter();
    }

    @OnClick(R.id.buttonGo)
    void buttonGoPressed() {
        if(!go) {
            MainActivityPermissionsDispatcher.initWithPermissionCheck(this);
        }else {
            stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MainActivityPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void setGo(boolean stateGo) {
        go = stateGo;
        if(go)
            button.setText("Stop");
        else
            button.setText("Start");
    }

    public boolean isGo() {
        return go;
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void init() {
        setGo(true);
        getPresenter().initGallery(this );
    }
    private void stop() {
        setGo(false);
        getPresenter().closeGalery();
    }

    @Override
    public void setArrayList(List<MediaEntity> arrayList) {
        runOnUiThread(()-> mediaAdapter.setEntityList( arrayList ) );
    }

    @Override
    public void addedItem( int id ) {
        runOnUiThread(()-> mediaAdapter.addedItem( id ) );
    }

    @Override
    public void addedBlock( int id, int count ) {
        runOnUiThread(()-> mediaAdapter.addedBlock(id, count ));
    }

    @Override
    public void updatedBlock( int id, int count ) {
        runOnUiThread( ()-> {
                mediaAdapter.updatedBlock(id, count );

                int max = mediaAdapter.getItemCount();
                if(max >  0) {
                    int percents = Math.min((id+1)*100/max, 100);
                    if( percents == 0 || percents > progressBar.getProgress() ) {
                        progressBar.setProgress(percents);
                    }
                }
            });
    }
}
