package com.roblebob.ud801_popular_movies;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsRVAdapter extends RecyclerView .Adapter<DetailsRVAdapter.DetailsRVViewholder> {

    private static final String ARROW_right = Html.fromHtml("&blacktriangleright;").toString();

    private List< String> mTrailerUrlStringList;
    private List< String> mTrailerNameStringList;
    private List< String> mReviewAuthorStringList;
    private List< String> mReviewContentStringList;
    public void setTrailerUrls(     String trailerUrls)     { if (trailerUrls != null)      mTrailerUrlStringList     = splitter( trailerUrls);   notifyDataSetChanged(); }
    public void setTrailerNames(    String trailerNames)    { if (trailerNames != null)     mTrailerNameStringList    = splitter( trailerNames);  notifyDataSetChanged(); }
    public void setReviewAuthors(   String reviewAuthors)   { if (reviewAuthors != null)    mReviewAuthorStringList   = splitter( reviewAuthors); notifyDataSetChanged(); }
    public void setReviewUrls(      String reviewUrls)      { if (reviewUrls != null)       mReviewContentStringList  = splitter( reviewUrls);    notifyDataSetChanged(); }

    public List< String> splitter(String listString) {
        return new ArrayList< String>(Arrays.asList( listString .substring(1, listString.length()-1) .split(",")));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    @Override
    public DetailsRVViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from( parent .getContext())
                .inflate( R.layout.details_recycler_view_single_item, parent, false);
        DetailsRVAdapter.DetailsRVViewholder detailsRVViewHolder = new DetailsRVAdapter.DetailsRVViewholder( view);

        return detailsRVViewHolder;
    }





    private boolean isTrailer(int position) {
        return ( 0 <= position  &&  position < mTrailerUrlStringList.size()  &&
                mTrailerUrlStringList.size() == mTrailerNameStringList.size()); }
    private boolean isReview( int position) {
        return ( mTrailerUrlStringList.size() <= position  &&  position < getItemCount()  &&
                mTrailerUrlStringList.size() == mTrailerNameStringList.size()); }

    @Override
    public void onBindViewHolder(@NonNull DetailsRVViewholder holder, int position) {


        final int i = position +    ( (isReview( position))  ?  - getMovieTrailerItemCount()        : 0) ;

        Log.e(this.getClass().getSimpleName() + "::onBindViewHolder() \t",
                " [pos]:" + position +
                        " --- [isTrailer]:" + isTrailer( position) + " [#]:" + getMovieTrailerItemCount() +
                        " --- [isReview]:" + isReview( position) + " [#]:" + getMovieReviewItemCount() +
                        " ---[i]:" + i) ;
        holder .textView .setText(  ( (isTrailer( position)) ?  mTrailerNameStringList .get( i)     : mReviewAuthorStringList .get( i)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( isTrailer( position)) {


                } else if ( isReview( position)) {


                }
            }
        });


    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int getMovieTrailerItemCount() {
        if ( mTrailerUrlStringList != null && mTrailerNameStringList != null)
            if ( mTrailerUrlStringList.size() == mTrailerNameStringList.size())
                return mTrailerUrlStringList.size();
        return 0;
    }
    private int getMovieReviewItemCount() {
        if ( mReviewContentStringList != null && mReviewAuthorStringList != null)
            if ( mReviewContentStringList.size() == mReviewAuthorStringList.size())
                return mReviewContentStringList.size();
        return 0;
    }

    @Override public int getItemCount() { return getMovieTrailerItemCount() + getMovieReviewItemCount(); }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public interface ItemClickListener { void onItemClickListener( int pos); }

    class DetailsRVViewholder extends RecyclerView.ViewHolder  {

        TextView textView;


        public DetailsRVViewholder(View itemView) {
            super( itemView);
            textView = ( TextView) itemView.findViewById( R.id.details_trailers_recycler_view_single_item_NAME_textview);
        }
    }
}