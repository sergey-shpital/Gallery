package album.pagenetsoft.com.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Sergey on 14.02.2018.
 *
 */

@Slf4j
public class MediaAdapter extends RecyclerView.Adapter< MediaAdapter.ViewMediaHolder> {

    private List<MediaEntity> entityList;

    public static class ViewMediaHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.textTitle)
        TextView textTitle;

        @BindView(R.id.textSize)
        TextView textSize;

        @BindView(R.id.textHash)
        TextView textHash;

        public ViewMediaHolder( View itemView ) {
            super( itemView );
            ButterKnife.bind(ViewMediaHolder.this, itemView );
        }
    }

    public void setEntityList(List<MediaEntity> entityList) {
        this.entityList = entityList;
        notifyDataSetChanged();
    }

    public void addedItem( int id ) {
        notifyItemInserted(id);
    }

    public void addedBlock( int id, int count ) {
        notifyItemRangeInserted(id, count );
    }

    public void updatedBlock( int id, int count ) {
        notifyItemRangeChanged(id, count);
    }

    @Override
    public MediaAdapter.ViewMediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater
                .from(context)
                .inflate(R.layout.media_item, parent, false);
        return new ViewMediaHolder(v);
    }

    @Override
    public void onBindViewHolder(MediaAdapter.ViewMediaHolder holder, int position) {
        MediaEntity entity = entityList.get( position );
        holder.textTitle.setText( entity.getTitle() );
        holder.textHash.setText( entity.getMd5() );
        holder.textSize.setText( String.format("%.7f Mb", entity.getSize()/1048576.0 ) );
        if(entity.getBitmap()!=null) {
            holder.imageView.setImageBitmap( entity.getBitmap() );
        }
    }

    @Override
    public int getItemCount() {
        return  (entityList!=null) ? entityList.size() : 0;
    }
}
