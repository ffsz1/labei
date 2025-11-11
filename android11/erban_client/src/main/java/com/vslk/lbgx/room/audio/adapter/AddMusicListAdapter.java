package com.vslk.lbgx.room.audio.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.player.IPlayerCore;
import com.tongdaxing.xchat_core.player.bean.LocalMusicInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

/**
 * Created by chenran on 2017/11/1.
 */

public class AddMusicListAdapter extends RecyclerView.Adapter<AddMusicListAdapter.ViewHolder> {
    private Context context;
    private List<LocalMusicInfo> localMusicInfos;

    public AddMusicListAdapter(Context context) {
        this.context = context;
    }

    public void setLocalMusicInfos(List<LocalMusicInfo> localMusicInfos) {
        this.localMusicInfos = localMusicInfos;
    }

    @Override
    public AddMusicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_item_add_music, parent, false);
        return new AddMusicListAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(AddMusicListAdapter.ViewHolder holder, int position) {
        final LocalMusicInfo localMusicInfo = localMusicInfos.get(position);
        LocalMusicInfo current = CoreManager.getCore(IPlayerCore.class).getCurrent();
        holder.musicName.setText(localMusicInfo.getSongName());
        if (current != null && current.getLocalId() == localMusicInfo.getLocalId()) {
            holder.musicName.setTextColor(Color.parseColor("#FED700"));
        } else {
            holder.musicName.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.duration.setText(TimeUtils.getFormatTimeString(localMusicInfo.getDuration(), "min:sec"));
        if (localMusicInfo.getArtistNames() != null && localMusicInfo.getArtistNames().size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < localMusicInfo.getArtistNames().size(); i++) {
                String artistName = localMusicInfo.getArtistNames().get(i);
                sb.append(artistName);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            holder.artistName.setText(sb.toString());
        } else {
            holder.artistName.setText("");
        }
        holder.deleteBtn.setTag(localMusicInfo);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoreManager.getCore(IPlayerCore.class).deleteMusicFromPlayerList(localMusicInfo);
            }
        });
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoreManager.getCore(IPlayerCore.class).play(localMusicInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (localMusicInfos == null) {
            return 0;
        } else {
            return localMusicInfos.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteBtn;
        TextView musicName;
        TextView artistName;
        TextView duration;
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
            deleteBtn = (ImageView) itemView.findViewById(R.id.delete_btn);
            musicName = (TextView) itemView.findViewById(R.id.music_name);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            duration = (TextView) itemView.findViewById(R.id.duration);
        }


    }
}
