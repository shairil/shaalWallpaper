package com.example.shaalwallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaalwallpaper.R;
import com.example.shaalwallpaper.Wallpaper;
import com.example.shaalwallpaper.helper.webScrapping;

import java.io.File;
import java.util.List;

public class WallapaperAdapter extends RecyclerView.Adapter<WallapaperAdapter.WallapaperViewHolder> {
    public List<String> imgUrls = null, titles=null, res=null, paths = null;
    private List<Bitmap> imgs=null;
    private List<Integer> ids=null;
    private List<File> files = null;
    private int count;
    private webScrapping web;
    int n = 2;
    Context context;
    RecyclerView recyclerView;
    //private CardView cardView;

    @NonNull
    @Override
    public WallapaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listwallpaperlayout, parent, false);
        return new WallapaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallapaperViewHolder holder, int position) {
        //Log.d("Wallpaper", "onBindViewHolder: " + imgUrls.get(position));
        if(imgUrls != null) {
            Glide.with(context)
                    .load(imgUrls.get(position))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);
        }
        else{
            Glide.with(context)
                    .load(files.get(position))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);
            //holder.img.setImageBitmap(imgs.get(position));
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgUrls != null)
                    callWallpaperScreenFromMain(imgUrls.get(position), titles.get(position), ids.get(position));
                else{
                    int i = files.get(position).getName().indexOf('(');
                    int j = files.get(position).getName().indexOf(')');
                    int id = Integer.parseInt(files.get(position).getName().substring(i+1, j));
                    String title = files.get(position).getName().substring(0, i);
                    String path = files.get(position).getAbsolutePath();
                    callWallpaperScreenFromCollection(path, title, id);
                }

            }
        });
    }

    private void setImageAnimate(ImageView img) {
    }

    private void callWallpaperScreenFromMain(String imgURL, String title, int id) {
        Intent intent = new Intent(context, Wallpaper.class);

        intent.putExtra("type", 1);
        intent.putExtra("imgURL", imgURL);
        intent.putExtra("title", title);
        intent.putExtra("id", Integer.toString(id));
        context.startActivity(intent);
    }

    private void callWallpaperScreenFromCollection(String path, String title, int id) {
        Intent intent = new Intent(context, Wallpaper.class);

        intent.putExtra("type", 2);
        intent.putExtra("path", path);
        intent.putExtra("title", title);
        intent.putExtra("id", Integer.toString(id));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        //Log.d("TAG", "getItemCount:" + imgUrls.size());
        if(ids == null){
            return files.size();
        }
        return ids.size();
    }

    public WallapaperAdapter(webScrapping web, Context context, RecyclerView recyclerView) {
        this.web = web;
        web.getWallpaper(n);
        this.context = context;
        this.imgUrls = web.getImageURL();
        Log.d("SO rha hu", "WallpaperAdapter: " + imgUrls.size());
        this.titles = web.getTitle();
        this.count = web.getCount();
        this.res = web.getRes();

        this.ids = web.getId();
        this.recyclerView = recyclerView;
        count-=ids.size();
    }

    public WallapaperAdapter(List<String> imgURLs, List<String> titles, List<String> res, List<Integer> ids, Context context) {
        this.context = context;
        this.imgUrls = imgURLs;
        Log.d("SO rha hu", "WallpaperAdapter: " + imgURLs.size());
        this.titles = titles;
        this.ids = ids;
        this.res = res;
    }

    public WallapaperAdapter(List<Bitmap> imgURLs, List<String> titles, List<Integer> ids, Context context, List<String> paths) {
        this.context = context;
        this.imgs = imgURLs;
        Log.d("So rha hu", "WallpaperAdapter: " + imgURLs.size());
        this.titles = titles;
        this.ids = ids;
        this.res = res;
        this.paths = paths;
    }

    public WallapaperAdapter(List<File> files, Context context) {
        this.context = context;
        this.files = files;
    }

    public static class WallapaperViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public CardView cardView;

        public WallapaperViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.Wallpaperlist);
            cardView = (CardView) itemView.findViewById(R.id.idCVWallpaper);
        }
    }

//    public void loadMore(){
//        n++;
//        //Log.d("TAG", "loadMore: " + n);
//        if(count > 0) {
//            web.getWallpaper(n);
//            count-=ids.size();
//            Log.d("TAG", "loadMore: " + count);
//            //int i = ids.size();
////            int j = web.getId().size();
////            count -= j;
//            this.imgUrls.addAll(web.getImageURL());
////            Log.d("TAG", "loadMore: " + imgUrls);
//            this.titles.addAll(web.getTitle());
//            this.res.addAll(web.getRes());
//            this.ids.addAll(web.getId());
//            //notifyItemRangeInserted(i, j);
//            notifyDataSetChanged();
//        }
//
//
//    }
}