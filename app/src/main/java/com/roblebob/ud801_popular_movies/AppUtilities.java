package com.roblebob.ud801_popular_movies;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AppUtilities {
    /* *********************************************************************************************
     * This method returns the entire result from the HTTP response.
     *
     * @param   urlString The URL to fetch the HTTP response from (as a String and NOT as URL).
     * @return  The contents of the HTTP response.
     * @throws  IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)  return scanner.next();
            else           return null;
        }
        finally { urlConnection.disconnect(); }
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final List< String> ORDER = new ArrayList<>( Arrays.asList( "popular", "top_rated"));
    public int getDetailedItemCountValidated( List<Main> mainList) { return  mainList.parallelStream() .filter(movie -> movie.isDetailed())  .mapToInt(movie -> 1)  .sum(); }
}