package com.roblebob.ud801_popular_movies;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainRVAdapter.ItemClickListener  {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainRVAdapter mMainRVAdapter;
    public Context context;
    private AppDatabase appDatabase;
    private MainViewModelFactory mainViewModelFactory;


    @Override protected void onCreate( Bundle savedInstanceState) {
        super .onCreate( savedInstanceState);
        setContentView( R.layout.activity_main);
        context = MainActivity.this;
        appDatabase = AppDatabase.getInstance( this.context);
        mainViewModelFactory = new MainViewModelFactory( this.appDatabase);
        final MainViewModel mainViewModel = new ViewModelProvider(this, this.mainViewModelFactory) .get( MainViewModel.class);

        RecyclerView mMainRV = (RecyclerView) this.findViewById( R.id.activity_main_RV);
        RecyclerView.LayoutManager mRVLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        ((LinearLayoutManager) mRVLayoutManager) .setInitialPrefetchItemCount( 100);
        mMainRV .setLayoutManager( mRVLayoutManager);
        mMainRVAdapter = new MainRVAdapter( this);
        mMainRV .setAdapter( mMainRVAdapter);
        mMainRV .setHasFixedSize( true);

        Toolbar toolbar = findViewById( R.id.activity_main_TOOLBAR);
        if (toolbar != null)  setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar() .setDisplayShowTitleEnabled( false);
        }
        toolbar.setFitsSystemWindows( true);



        mainViewModel .apiKeyLive .observe(this, new Observer< String>() {
            @Override public void onChanged( @Nullable String apiKey) {
                //mainViewModel .apiKeyLive .removeObserver( this);

                //Toast .makeText( context, "apiKey: " + apiKey, Toast.LENGTH_SHORT).show();
                Log.e(TAG + ":::mainViewModel .apiKeyLive\t", "\t" + apiKey);
                populateApiKeyUI( apiKey);
            }
        });

        mainViewModel .orderLive .observe(this, new Observer< String>() {
            @Override public void onChanged( @Nullable  String order) {
                //mainViewModel .orderLive .removeObserver( this);

                //Toast .makeText( context, "order: " + order, Toast.LENGTH_SHORT).show();
                Log.e(TAG + ":::mainViewModel .orderLive\t\t", "\t\t" + order);

                if (order != null) {
                    mMainRVAdapter .submitOrder(  order);

                    switch (order) {
                        case "popular":
                            if ( ((TabItem) findViewById( R.id.activity_main_TAB_popular)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_popular)) .setSelected(true);
                            if ( ((TabItem) findViewById(R.id.activity_main_TAB_top_rated)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_top_rated)) .setSelected(false);
                            Log.e(TAG + ":::mainViewModel .orderLive\t\t", "\t\t the order \"" + order + "\" has been applied !");
                            break;

                        case "top_rated":
                            if ( ((TabItem) findViewById( R.id.activity_main_TAB_popular)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_popular)) .setSelected(false);
                            if ( ((TabItem) findViewById(R.id.activity_main_TAB_top_rated)) != null)
                                ((TabItem) findViewById( R.id.activity_main_TAB_top_rated)) .setSelected(true);

                            Log.e(TAG + ":::mainViewModel .orderLive\t\t", "\t\tthe order \"" + order + "\" has been applied !");
                            break;
                        default:
                            Log.e(TAG + ":::mainViewModel .orderLive\t\t", "\t\tthe new order \"" + order + "\"  could not be applied !");
                    }


                    TabLayout tabLayout = findViewById( R.id.activity_main_TABLAYOUT);
                    //tabLayout .setSelected( true);
                    tabLayout .addOnTabSelectedListener( new TabLayout .OnTabSelectedListener() {
                        @Override public void onTabSelected( TabLayout.Tab tab) { mainViewModel .setOrder( AppUtilities.ORDER.get( tab.getPosition())); }
                        @Override public void onTabUnselected( TabLayout.Tab tab) {}
                        @Override public void onTabReselected( TabLayout.Tab tab) { mainViewModel .setOrder( AppUtilities.ORDER.get( tab.getPosition())); }
                    });



                } else mainViewModel.setOrder("popular");






            }
        });

        mainViewModel .mainListByDatabaseLive .observe(this, new Observer< List< Main>>() {
            @Override public void onChanged( @Nullable  List< Main> mainList) {
                //mainViewModel .mainListByDatabaseLive .removeObserver( this);

                if (mainList != null) {
                    mMainRVAdapter .submitList( mainList);
                    //Toast .makeText( context, "mainList changed", Toast.LENGTH_SHORT).show();
                    Log.e(TAG + ":::mainListByDatabaseLive\t" , "\t" + ((mainList != null)  ? mainList.toString() : "null"));
                }
            }
        });

        mainViewModel .movieCountLive .observe(this, new Observer< Integer>() {
            @Override public void onChanged( @Nullable Integer movieCount) {
                //mainViewModel .movieCountLive .removeObserver( this);

                ((TextView) findViewById( R.id.activitity_main_BASIC_COUNT_tv))  .setText(  String.valueOf(  movieCount));
                //Toast .makeText( context, "movieCount changed: " + movieCount, Toast.LENGTH_SHORT).show();
                Log.e(TAG + ":::mainViewModel .movieCountLive\t" , "\tMovieCount: " + (movieCount + 1));
            }
        });

        mainViewModel .detailedMovieCountLive .observe(this, new Observer< Integer>() {
            @Override public void onChanged( @Nullable Integer detailedMovieCount) {
                //mainViewModel .detailedMovieCountLive .removeObserver( this);

                //Toast .makeText( context, "detailedMovieCount changed: " + , Toast.LENGTH_SHORT).show();
                Log.e(TAG + ":::detailedMovieCountLive\t" , "\t" + detailedMovieCount);
                ((TextView) findViewById( R.id.activitity_main_DETAILED_COUNT_tv))  .setText(  String.valueOf(  detailedMovieCount));
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null)  activeNetwork = cm.getActiveNetworkInfo();
        return  (activeNetwork != null)  &&  (activeNetwork.isConnectedOrConnecting());
    }



    public void populateApiKeyUI( String apiKey) {

        final MainViewModel mainViewModel = new ViewModelProvider(this, this.mainViewModelFactory) .get( MainViewModel.class);

        Log.e(TAG + "::populateApiKeyUI()\t" , "\t<---(apiKey)---\t" + apiKey);

        if (apiKey != null)  {   Toast.makeText(  this.context, "apiKey accepted", Toast.LENGTH_SHORT).show();
            ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)     )   .setVisibility( View.GONE);
            ((TabLayout)         findViewById( R.id.activity_main_TABLAYOUT)         )   .setVisibility( View.VISIBLE);
            ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp))   .setVisibility( View.VISIBLE);
            ((RecyclerView)      findViewById( R.id.activity_main_RV)                )   .setVisibility( View.VISIBLE);
            mainViewModel .start( apiKey);

        } else {                 Toast .makeText( this.context, "apiKey rejected", Toast.LENGTH_SHORT).show();
            ((ConstraintLayout)  findViewById( R.id.activity_main_INITIAL_SETUP)     )   .setVisibility( View.VISIBLE);
            ((TabLayout)         findViewById( R.id.activity_main_TABLAYOUT)         )   .setVisibility( View.GONE);
            ((ConstraintLayout)  findViewById( R.id.activity_main_TOOLBAR_state_disp))   .setVisibility( View.GONE);
            ((RecyclerView)      findViewById( R.id.activity_main_RV)                )   .setVisibility( View.GONE);
            ((TextInputEditText) findViewById( R.id.activity_main_INITIAL_SETUP_textInputLayout_tv)) .setOnEditorActionListener(   (v, actionId, event) -> {
                Log.e(TAG + "::populateApiKeyUI()\t", "----------->    ACTION was called:   " + v.getText().toString());
                mainViewModel.setApiKey(v.getText().toString());
                return false;   //  FALSE -> keyboard display goes into hiding
            });
        }
    }



    @Override public void onItemClickListener( int movieID) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent .putExtra( DetailActivity.INTENT_EXTRA_movieID, movieID);
        startActivity( intent);
    }
}
