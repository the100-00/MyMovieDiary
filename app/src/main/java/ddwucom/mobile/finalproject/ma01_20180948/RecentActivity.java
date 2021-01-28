package ddwucom.mobile.finalproject.ma01_20180948;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    ArrayList<Movie> movieList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmovie);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        movieList = new ArrayList<Movie>();

        //Asynctask - OKHttp

        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();

        recyclerView.setLayoutManager(new GridLayoutManager(RecentActivity.this, 2));
        Toast.makeText(this, "최신 영화 정보", Toast.LENGTH_LONG).show();
}

    public class MyAsyncTask extends AsyncTask<String, Void, Movie[]> {
        //로딩중 표시
        ProgressDialog progressDialog = new ProgressDialog(RecentActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중...");
            //show dialog
            progressDialog.show();

            //목록 배열의 내용을 크리어 해 놓는다.
            movieList.clear();

        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/upcoming?api_key=32dd22afb55563fc35064d92148a1cea&language=ko-KR&page=1")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("results");
                Movie[] posts = gson.fromJson(rootObject, Movie[].class);
                return posts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            //ArrayList에 차례대로 집어 넣는다.
            if(result.length > 0){
                for(Movie p : result){
                    movieList.add(p);
                }
            }
            //어답터 설정
            adapter = new MyAdapter(RecentActivity.this, movieList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_search:
                EditText s = findViewById(R.id.etSearch);
                if(!s.getText().toString().equals("")){
                Toast.makeText(RecentActivity.this, "영화를 검색합니다.", Toast.LENGTH_SHORT).show();
                SearchAsyncTask sAsyncTask = new SearchAsyncTask();
                sAsyncTask.execute();}
                else
                    Toast.makeText(RecentActivity.this, "검색할 영화를 입력하세요.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class SearchAsyncTask extends AsyncTask<String, Void, Movie[]> {
        //로딩중 표시
        ProgressDialog progressDialog = new ProgressDialog(RecentActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중...");
            //show dialog
            progressDialog.show();

            //목록 배열의 내용을 크리어 해 놓는다.
            movieList.clear();

        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            EditText movie = findViewById(R.id.etSearch);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/search/movie?api_key=32dd22afb55563fc35064d92148a1cea&query="+movie.getText()+"&language=ko-KR&page=1")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("results");
                Movie[] posts = gson.fromJson(rootObject, Movie[].class);
                return posts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            //ArrayList에 차례대로 집어 넣는다.
            if(result.length > 0){
                for(Movie p : result){
                    movieList.add(p);
                }
            }
            //어답터 설정
            adapter = new MyAdapter(RecentActivity.this, movieList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}