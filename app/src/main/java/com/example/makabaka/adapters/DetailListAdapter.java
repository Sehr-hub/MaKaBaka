package com.example.makabaka.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter  extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private List<Track> mDetailData=new ArrayList<>();
    private SimpleDateFormat mSimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mMinDurationFormat=new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourDurationFormat=new SimpleDateFormat("hh:mm:ss");
    private ItemClickListener mItemClickListener=null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //绑定控件
        View itemView=holder.itemView;
        TextView orderTv=itemView.findViewById(R.id.order_text); //序号
        TextView titleTv=itemView.findViewById(R.id.detail_item_title);//标题
        TextView playCountTv=itemView.findViewById(R.id.detail_item_play_count);//播放量
        TextView durationTv=itemView.findViewById(R.id.detail_item_duration);//时长
        TextView upDateTv=itemView.findViewById(R.id.detail_item_update_time);//更新日期
        //设置数据
        Track track=mDetailData.get(position);
        orderTv.setText(position+1+"");
        titleTv.setText(track.getTrackTitle());
        playCountTv.setText(track.getPlayCount()+"");
        if(track.getDuration()>60*60){
            durationTv.setText(mHourDurationFormat.format(track.getDuration()*1000));
        }else {
            durationTv.setText(mMinDurationFormat.format(track.getDuration()*1000));
        }
        upDateTv.setText(mSimpleDateFormat.format(track.getUpdatedAt()));
        //设置点击事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    //点击播放时传递列表和位置参数
                    mItemClickListener.onItemClick(mDetailData,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        //清除原有数据
        mDetailData.clear();
        //添加新的数据
        mDetailData.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setItemClickListener(ItemClickListener listener){
        this.mItemClickListener=listener;
    }

    public interface ItemClickListener{
        void onItemClick(List<Track> detailData, int position);
    }

}
