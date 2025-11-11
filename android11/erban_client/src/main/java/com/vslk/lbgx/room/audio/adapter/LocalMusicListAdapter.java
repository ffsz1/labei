package com.vslk.lbgx.room.audio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.ViewHolder>
        implements View.OnClickListener {
    private Context context;
    private List<LocalMusicInfo> localMusicInfos;

    public LocalMusicListAdapter(Context context) {
        this.context = context;
    }

    public void setLocalMusicInfos(List<LocalMusicInfo> localMusicInfos) {
        this.localMusicInfos = localMusicInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_item_local_music, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalMusicInfo localMusicInfo = localMusicInfos.get(position);
        holder.musicName.setText(localMusicInfo.getSongName());
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

        if (localMusicInfo.isInPlayerList()) {
            holder.addBtn.setVisibility(View.GONE);
            holder.okBtn.setVisibility(View.VISIBLE);
        } else {
            holder.addBtn.setVisibility(View.VISIBLE);
            holder.okBtn.setVisibility(View.GONE);
        }
        holder.addBtn.setTag(localMusicInfo);
        holder.addBtn.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (localMusicInfos == null) {
            return 0;
        } else {
            return localMusicInfos.size();
        }
    }

    @Override
    public void onClick(View v) {
        LocalMusicInfo localMusicInfo = (LocalMusicInfo) v.getTag();
        CoreManager.getCore(IPlayerCore.class).addMusicToPlayerList(localMusicInfo);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView addBtn;
        ImageView okBtn;
        TextView musicName;
        TextView artistName;
        TextView duration;

        public ViewHolder(View itemView) {
            super(itemView);
            okBtn = itemView.findViewById(R.id.ok_btn);
            addBtn = (ImageView) itemView.findViewById(R.id.add_btn);
            musicName = (TextView) itemView.findViewById(R.id.music_name);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            duration = (TextView) itemView.findViewById(R.id.duration);
        }


    }
}
