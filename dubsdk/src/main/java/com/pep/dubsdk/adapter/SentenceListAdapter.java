package com.pep.dubsdk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pep.dubsdk.R;
import com.pep.dubsdk.model.Sentence;

import java.util.List;

public class SentenceListAdapter extends RecyclerView.Adapter<SentenceListAdapter.VH> {
    private Context mContext;
    private List<Sentence> mData;
    private VideoOperateCallBack operateCallBack;

    public interface VideoOperateCallBack {
        void playVideoPeriod(int position, int fromSeconds, int toSeconds);

        void mixerVideo(int position, String fileName);

        void clipVideo(int position);
    }

    public void setVideoOperateCallBack(VideoOperateCallBack onItemClick) {
        this.operateCallBack = onItemClick;
    }

    public SentenceListAdapter(Context mContext, List<Sentence> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.sentence_list_item, parent, false);
        return new VH(contentView);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final Sentence sentence = mData.get(position);
        if (sentence != null) {
            holder.tvSentenceCN.setText(sentence.sentenceCN);
            holder.tvSentenceEN.setText(sentence.sentenceEN);
            holder.clipVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (operateCallBack != null) {
                        operateCallBack.playVideoPeriod(position, sentence.fromSeconds * 1000, sentence.toSeconds * 1000);
                    }
                }
            });
            holder.mixerVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (operateCallBack != null) {
                        operateCallBack.mixerVideo(position, sentence.sentenceId + "_clip");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        private RelativeLayout llListRoot;
        private TextView tvSentenceEN;
        private TextView tvSentenceCN;
        private Button clipVideo;
        private Button mixerVideo;

        public VH(View itemView) {
            super(itemView);
            llListRoot = itemView.findViewById(R.id.llListRoot);
            tvSentenceEN = itemView.findViewById(R.id.tv_en_sentence);
            tvSentenceCN = itemView.findViewById(R.id.tv_cn_sentence);
            clipVideo = itemView.findViewById(R.id.clipVideo);
            mixerVideo = itemView.findViewById(R.id.deMixer);
        }
    }
}
