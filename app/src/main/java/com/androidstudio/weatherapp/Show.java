package com.androidstudio.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Show extends AppCompatActivity {

    private View mProgressView;
    private View mShowData;
    private TextView tvLoad;

    String Sunrise,Sunset,Wind,Pressure,Humidity;

    TextView tvCity,tvUpdated,tvStatus,tvTemp,tvMinTemp,tvMaxTemp;
    LinearLayout layout;

    LayoutInflater inflater;
    ImageView ivSymbol,ivExit;
    TextView tvTitle,tvInfo;
    PopupWindow window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup,null,false);

        ivExit = customView.findViewById(R.id.ivExit);
        ivSymbol = customView.findViewById(R.id.ivSymbol);
        tvTitle = customView.findViewById(R.id.tvTitle);
        tvInfo = customView.findViewById(R.id.tvInfo);

        window = new PopupWindow(customView, 600, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setAnimationStyle(R.style.Animation);

        layout = findViewById(R.id.show);

        mShowData = findViewById(R.id.show_data);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvCity = findViewById(R.id.tvCity);
        tvUpdated = findViewById(R.id.tvUpdated);
        tvStatus = findViewById(R.id.tvStatus);
        tvTemp = findViewById(R.id.tvtemp);
        tvMinTemp = findViewById(R.id.tvMinTemp);
        tvMaxTemp = findViewById(R.id.tvMaxTemp);

        showProgress(true);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request;

        request = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?q=" + getIntent().getStringExtra("city") +
                    "&APPID=befb2f891e142ad6ddd0a90520770b53", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                            JSONObject main = response.getJSONObject("main");
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            JSONObject wind = response.getJSONObject("wind");
                            JSONObject sys = response.getJSONObject("sys");

                            tvCity.setText(response.getString("name") + "," + sys.getString("country"));

                            tvUpdated.setText("Updated at: " + new SimpleDateFormat("dd/MM/yyyy " +
                                    "hh:mm a", Locale.ENGLISH).format(new Date(response.getLong("dt") * 1000)));

                            tvStatus.setText(weather.getString("description").toUpperCase());

                            tvTemp.setText((int) (main.getDouble("temp")-273.15) + "°C");

                            tvMaxTemp.setText("Max.Temp." + (int) (main.getDouble("temp_max")-273.15)+ "°C");

                            tvMinTemp.setText("Min.Temp." + (int) (main.getDouble("temp_min")-273.15) + "°C");

                            Sunrise = (new SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                                    .format(new Date(sys.getLong("sunrise") * 1000)));

                            Sunset = (new SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                                    .format(new Date(sys.getLong("sunset") * 1000)));

                            Wind = ((int)wind.getDouble("speed")+"Km/hr");

                            Pressure = ((int)main.getDouble("pressure")+"mb");

                            Humidity = ((int)main.getDouble("humidity")+"%");
                            showProgress(false);
                        }
                    catch (JSONException e) {
                        e.printStackTrace();
                        showProgress(false);
                        Show.this.finish();
                    }
            }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof NoConnectionError)
                        Toast.makeText(Show.this, "No internet connection", Toast.LENGTH_LONG).show();
                    else if(error instanceof TimeoutError)
                        Toast.makeText(Show.this, "Request timeout due to slow internet connection", Toast.LENGTH_LONG).show();
                    else if(error instanceof ServerError)
                        Toast.makeText(Show.this, "No city found named '"+getIntent().getStringExtra("city")+"'.Try again with different city", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                    Show.this.finish();
                }
            });
        requestQueue.add(request);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mShowData.setVisibility(show ? View.GONE : View.VISIBLE);
            mShowData.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mShowData.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mShowData.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void clicked(View view)
    {
        switch (view.getId()) {
            case R.id.llSunrise:
                if (window.isShowing())
                    window.dismiss();
                ivSymbol.setImageResource(R.drawable.sunrise);
                tvTitle.setText("Sunrise");
                tvInfo.setText(Sunrise);

                window.showAtLocation(layout, Gravity.CENTER, 0, -50);
                break;
            case R.id.llSunset:
                if (window.isShowing())
                    window.dismiss();
                ivSymbol.setImageResource(R.drawable.sunset);
                tvTitle.setText("Sunset");
                tvInfo.setText(Sunset);

                window.showAtLocation(layout, Gravity.CENTER, 0, -50);
                break;
            case R.id.llWind:
                if (window.isShowing())
                    window.dismiss();
                ivSymbol.setImageResource(R.drawable.wind);
                tvTitle.setText("Wind");
                tvInfo.setText(Wind);

                window.showAtLocation(layout, Gravity.CENTER, 0, -50);
                break;
            case R.id.llHumidity:
                if (window.isShowing())
                    window.dismiss();
                ivSymbol.setImageResource(R.drawable.humidity);
                tvTitle.setText("Humidity");
                tvInfo.setText(Humidity);

                window.showAtLocation(layout, Gravity.CENTER, 0, -50);
                break;
            case R.id.llPressure:
                if (window.isShowing())
                    window.dismiss();
                ivSymbol.setImageResource(R.drawable.pressure);
                tvTitle.setText("Pressure");
                tvInfo.setText(Pressure);

                window.showAtLocation(layout, Gravity.CENTER, 0, -50);
                break;
        }

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });

    }
}
