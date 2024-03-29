package com.roblebob.ud801_popular_movies;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainRVAdapter extends RecyclerView.Adapter< MainRVAdapter.MainRVViewHolder> {
    private final static String TAG =   MainRVAdapter.class .getSimpleName();
    private List<Main> mainList = new ArrayList<>();
    private ItemClickListener mItemClickListener;
    private String order;

    public void submitOrder(@NonNull String order) {
        if (getItemCount() > 0) {
            this.order = order;
            switch (order) {

                case "popular":
                    Collections.sort(mainList, ((Comparator<Main>) (Main m1, Main m2) ->
                            (Double.compare(m1.getFavorite(), m2.getFavorite()) != 0) ? Double.compare(m1.getFavorite(), m2.getFavorite()) :
                                    (Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) != 0) ? Double.compare(m1.getPopularVAL(), m2.getPopularVAL()) :
                                            (Double.compare(m1.getVoteCNT(), m2.getVoteCNT()) != 0) ? Double.compare(m1.getVoteCNT(), m2.getVoteCNT()) :
                                                    Double.compare(m1.getVoteAVG(), m2.getVoteAVG())
                    ).reversed());
                    break;

                case "top_rated":
                    Collections.sort(mainList, ((Comparator<Main>) (Main m1, Main m2) ->
                            (Double.compare(m1.getFavorite(), m2.getFavorite()) != 0) ? Double.compare(m1.getFavorite(), m2.getFavorite()) :
                                    (Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) != 0) ? Double.compare(m1.getVoteAVG(), m2.getVoteAVG()) :
                                            (Double.compare(m1.getVoteCNT(), m2.getVoteCNT()) != 0) ? Double.compare(m1.getVoteCNT(), m2.getVoteCNT()) :
                                                    Double.compare(m1.getPopularVAL(), m2.getPopularVAL())
                    ).reversed());
                    break;
            }
            submitList( mainList);
            //Log.e(TAG + "::submitOrder()\t",  order +  "---> "  +   mainList .parallelStream() .map(  (movie) -> movie.getMovieID())  .collect(  Collectors.toList())  .toString());
        }
    }

    public void submitList( List< Main> mainList) { this.mainList = new ArrayList<>(mainList); notifyDataSetChanged(); }



    MainRVAdapter(ItemClickListener itemClickListener) { mItemClickListener = itemClickListener; }

    @NonNull @Override public MainRVViewHolder  onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater .from( parent .getContext()) .inflate( R.layout.main_rv_single_item, parent, false);
        return new MainRVViewHolder( view);
    }

    @Override public void  onBindViewHolder( @NonNull MainRVViewHolder holder, int position) {
        Main main = this.mainList .get( position);
        holder.bindTo( main);
        //Log.e(TAG + "::onBindViewHolder()\t\t", "\t\tPOS:" + position + "\t\t" + main.getMovieID());
    }

    @Override public int  getItemCount() { return  mainList.size(); }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////
    public interface  ItemClickListener  { void onItemClickListener( int position, int movieID); }
    //
    public class  MainRVViewHolder  extends RecyclerView .ViewHolder  implements View .OnClickListener  {

        ImageView posterIv;
        TextView  rankingTv;

        public MainRVViewHolder( @NonNull View itemView) {
            super( itemView);
            posterIv  = itemView .findViewById( R.id.main_rv_single_item_IV);
            rankingTv = itemView .findViewById( R.id.main_rv_single_item_TV);
            itemView .setOnClickListener( this);
        }

        @Override public void onClick( View v) {
            mItemClickListener .onItemClickListener( getLayoutPosition(), mainList.get( getLayoutPosition()).getMovieID());
        }

        public void bindTo( Main main) {

            Picasso .get() .load( main.getPosterURL()) .into(posterIv);

            rankingTv.setText( String .valueOf( getLayoutPosition() + 1));
            if (main.isDetailed())  rankingTv.setBackgroundColor( itemView.getContext().getColor( R.color.colorYellow));
            else                    rankingTv.setBackgroundColor( itemView.getContext().getColor( R.color.colorWhite));
        }
    }
}

