package com.example.makabaka.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.InnerHolder> {

    private List<Album> mData=new ArrayList<>();
    private OnAlbumItemClickListener mItemClickListener=null;
    private OnAlbumItemLongClickListener mLongClickListener=null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //装载View
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    int clickPosition=(int)v.getTag();
                    mItemClickListener.onItemClick(clickPosition,mData.get(clickPosition));
                }
            }
        });
        holder.setData(mData.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    int clickPosition=(int)v.getTag();
                    mLongClickListener.onItemLongClick(mData.get(clickPosition));
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData!=null){
            return mData.size();//显示的个数
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if(mData!=null){
            mData.clear();
            mData.addAll(albumList);
        }
        notifyDataSetChanged();//更新UI
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            ImageView albumCover=itemView.findViewById(R.id.album_cover);//封面
            TextView albumTitle=itemView.findViewById(R.id.album_title);//标题
            TextView albumDescription=itemView.findViewById(R.id.album_description);//描述
            TextView albumPlayCount=itemView.findViewById(R.id.album_play_count);//播放量
            TextView albumContentSize=itemView.findViewById(R.id.album_content_size);//集数

            albumTitle.setText(album.getAlbumTitle());
            albumDescription.setText(album.getAlbumIntro());
            albumPlayCount.setText(album.getPlayCount()+"");
            albumContentSize.setText(album.getIncludeTrackCount()+"");
            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCover);
        }
    }

    public void setAlbumItemClickListener(OnAlbumItemClickListener listener){
        this.mItemClickListener=listener;
    }

    public interface OnAlbumItemClickListener {
        void onItemClick(int position, Album album);
    }

    public void setOnAlbumItemLongClickListener(OnAlbumItemLongClickListener listener){
        this.mLongClickListener=listener;
    }

    public interface OnAlbumItemLongClickListener{
        void onItemLongClick(Album album);
    }



}
