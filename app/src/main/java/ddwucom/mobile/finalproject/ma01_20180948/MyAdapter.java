package ddwucom.mobile.finalproject.ma01_20180948;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerViewHolders>{

    final static String TAG = "MyAdapter";

    private ArrayList<Movie> mMovieList;
    private LayoutInflater mInflate;
    private Context mContext;

    //constructor
    public MyAdapter(Context context, ArrayList<Movie> itemList) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mMovieList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.list_movie, parent, false);
        RecyclerViewHolders viewHolder = new RecyclerViewHolders(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, final int position) {
        //포스터만 출력
        String url = "https://image.tmdb.org/t/p/w500" + mMovieList.get(position).getPoster_path();
        Log.d(TAG, "URL: " + url);
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(holder.imageView);

        //각 아이템 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("title", mMovieList.get(position).getTitle());
                intent.putExtra("original_title", mMovieList.get(position).getOriginal_title());
                intent.putExtra("overview", mMovieList.get(position).getOverview());
                intent.putExtra("release_date", mMovieList.get(position).getRelease_date());
                intent.putExtra("poster_path", mMovieList.get(position).getPoster_path());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mMovieList.size();
    }


    public static class RecyclerViewHolders extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public RecyclerViewHolders(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }

}