package com.podorozhniak.kotlinx.practice.view.clrecview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.podorozhniak.kotlinx.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private final List<Match> matchList = new ArrayList<>();
    private final List<Match> matchListCopy = new ArrayList<>();
    private final OnMatchClickListener onMatchClickListener;

    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }

    public MatchAdapter(OnMatchClickListener onMatchClickListener) {
        this.onMatchClickListener = onMatchClickListener;
    }

    public void filter(String text) {
        matchList.clear();
        if (text.isEmpty()) {
            matchList.addAll(matchListCopy);
        } else {
            text = text.toLowerCase();
            for (Match match : matchListCopy) {
                if (match.getResult().toLowerCase().contains(text) || match.getSeason().toLowerCase().contains(text)) {
                    matchList.add(match);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setItems(Collection<Match> matches) {
        matchList.addAll(matches);
        matchListCopy.addAll(matchList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cl_final, parent, false);
            return new MatchViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer,
                    parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind();
        } else if (holder instanceof MatchViewHolder) {
            ((MatchViewHolder) holder).bind(matchList.get(position - 1));
        } else if (holder instanceof FooterViewHolder) {
            Log.d("TAG", "onBindViewHolder: ");

        }
    }

    @Override
    public int getItemCount() {
        return matchList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == matchList.size() + 1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        HeaderViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.text);
        }

        void bind() {
            textView.setText(textView.getContext().getString(R.string.to_be_continued));
        }
    }


    class MatchViewHolder extends RecyclerView.ViewHolder {
        private TextView yearTv;
        private TextView matchTv;

        public MatchViewHolder(View itemView) {
            super(itemView);
            yearTv = itemView.findViewById(R.id.yearTv);
            matchTv = itemView.findViewById(R.id.matchTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Match match = matchList.get(getLayoutPosition());
                    onMatchClickListener.onMatchClick(match);
                }
            });
        }

        public void bind(Match match) {
            yearTv.setText(match.getSeason());
            matchTv.setText(match.getResult());
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View v) {
            super(v);
        }
    }
}
