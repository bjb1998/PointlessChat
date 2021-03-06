package edu.uw.tcss450.group6App.model;

import static edu.uw.tcss450.group6App.MainActivity.mLocationModel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * ViewModel for data received from weatherbit.io
 */
public class WeatherViewModel extends AndroidViewModel {


    /**
     * API key from weatherbit.io from Robert's UW email.
     */
    private static final String API_KEY = "d76b1605bb534d7ea82ace219ff60b96";

    private final MutableLiveData<JSONObject> mResponse;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }


    public void connectGetDaily() {
        final Location location = mLocationModel.getCurrentLocation();
        String url = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() + "&key=" + API_KEY;

        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public Request<JSONObject> connectGet10Day() {
        final Location location = mLocationModel.getCurrentLocation();
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?" + "lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() + "&key=" + API_KEY;
        Log.d("LAT", "" + location.getLatitude());
        Log.d("LONG", "" + location.getLongitude());

        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
        return request;
    }



    private void handleResult(final JSONObject result) {
        mResponse.setValue(result);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }


}
