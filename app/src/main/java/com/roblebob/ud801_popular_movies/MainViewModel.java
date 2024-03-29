package com.roblebob.ud801_popular_movies;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final AppStateRepository appStateRepo;
    private final MainRepository     mainRepo;
    private final DetailRepository   detailRepo;
    public  final LiveData< String>      orderLive;
    public  final LiveData< String>      apiKeyLive;
    public  final LiveData< String>      lastPositionLive;
    public  final LiveData< List< Main>> mainListByDatabaseLive;
    public  final LiveData< Integer>     movieCountLive;
    public  final LiveData< Integer>     detailedMovieCountLive;

    public MainViewModel( @NonNull final AppDatabase appDatabase) {
        this.appStateRepo =  new AppStateRepository( appDatabase);
        this.mainRepo     =  new MainRepository(     appDatabase);
        this.detailRepo   =  new DetailRepository(   appDatabase);
        this.orderLive              =  appStateRepo .getOrderLive();
        this.apiKeyLive             =  appStateRepo .getApiKeyLive();
        this.lastPositionLive       =  appStateRepo .getLastPosition();
        this.mainListByDatabaseLive =  mainRepo     .getListLive();
        this.movieCountLive         =  mainRepo     .getMovieCount();
        this.detailedMovieCountLive =  detailRepo   .getMovieCountLive();
     }

    public void start(    String apiKey) { mainRepo .integrate(apiKey); }
    public void setOrder( String order)  { appStateRepo.setOrder(order); }
    public void setApiKey(String apiKey) { appStateRepo.setApiKey(apiKey); }
    public void setLastPosition(String position) { appStateRepo.setLastPosition(position); }
}
