package com.neteru.hermod.classes.adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neteru.hermod.R;
import com.neteru.hermod.classes.Timing;
import com.neteru.hermod.classes.databases.DbManager;
import com.neteru.hermod.classes.models.Article;
import com.neteru.hermod.classes.models.News;

import java.util.List;

import static com.neteru.hermod.classes.AppUtilities.share;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private int rowLayout;
    private DbManager dbManager;
    private List<News> newsList;
    private List<Article> articles;
    private NewsAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView author;
        TextView date;
        ImageView img;
        FrameLayout read;
        FrameLayout save;
        FrameLayout share;
        TextView readTxt;
        TextView saveTxt;
        TextView shareTxt;

        MyViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.news_title);
            description = view.findViewById(R.id.news_description);
            author = view.findViewById(R.id.news_author);
            date = view.findViewById(R.id.news_date);
            img = view.findViewById(R.id.news_img);
            read = view.findViewById(R.id.news_read);
            save = view.findViewById(R.id.news_save);
            share = view.findViewById(R.id.news_share);
            readTxt = view.findViewById(R.id.news_read_txt);
            saveTxt = view.findViewById(R.id.news_save_txt);
            shareTxt = view.findViewById(R.id.news_share_txt);

        }

    }

    public NewsAdapter(List<News> news, int rowLayout, Context context, NewsAdapterListener listener) {
        this.newsList = news;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = listener;

        dbManager = new DbManager(context);
    }

    public NewsAdapter(List<Article> articles, Context context, int rowLayout, NewsAdapterListener listener) {
        this.articles = articles;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = listener;

        dbManager = new DbManager(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position){

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position){

        final String id, sourceName, url, title, imgUrl, description, author, publishedAt;
        if (newsList != null){

            final News news = newsList.get(position);
            id = news.getSource().getId();
            sourceName = news.getSource().getName();
            url = news.getUrl();
            title = news.getTitle();
            imgUrl = news.getUrlToImage();
            description = news.getDescription();
            author = news.getAuthor();
            publishedAt = news.getPublishedAt();

        }else {

            final Article article = articles.get(position);
            id = article.getId();
            sourceName = article.getSource();
            url = article.getUrl();
            title = article.getTitle();
            imgUrl = article.getUrlToImage();
            description = article.getDescription();
            author = article.getAuthor();
            publishedAt = article.getPublishedAt();

        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewsSelected(sourceName, url);
            }
        });

        holder.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewsSelected(sourceName, url);
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewsPictureSelected(title, imgUrl);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Article> articlesByTitle = dbManager.db_getArticleByTitle(title);
                if (articlesByTitle != null && !articlesByTitle.isEmpty()){

                    dbManager.db_removeArticle(new Article(id, sourceName, author, title, description, url, imgUrl, publishedAt, false));
                    setTextViewDrawable(holder.saveTxt, R.mipmap.ic_turned_in_not_black_24dp);
                }else {

                    dbManager.db_insertArticle(new Article(id, sourceName, author, title, description, url, imgUrl, publishedAt, true));
                    setTextViewDrawable(holder.saveTxt, R.mipmap.ic_turned_in_black_24dp);
                }

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    share(context, url, title, description, imgUrl);

                } else {

                    new AlertDialog.Builder(context)
                            .setIcon(R.mipmap.direct_news_launcher)
                            .setMessage(R.string.allow_the_app_str)
                            .setNegativeButton(R.string.cancel_but_str, null)
                            .setPositiveButton(R.string.open_settings_str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                    intent.setData(uri);
                                    context.startActivity(intent);
                                }
                            })
                            .show();
                }

            }
        });

        setTextViewDrawable(holder.readTxt, R.mipmap.ic_visibility_black_24dp);

        setTextViewDrawable(holder.shareTxt, R.mipmap.ic_share_black_24dp);

        holder.title.setText(title);

        holder.description.setText(description);

        if (author != null) {
            holder.author.setText(context.getString(R.string.written_by_str, author));
        }
        if (publishedAt != null) {
            holder.date.setText(Timing.getInstance(context, publishedAt).getTimePeriod());
        }
        if (imgUrl != null) {

            holder.img.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(R.color.ghostwhite)
                    .error(R.drawable.no_image_1)
                    .into(holder.img);

        } else {
            holder.img.setVisibility(View.GONE);
        }

        List<Article> articlesByTitle = dbManager.db_getArticleByTitle(title);
        if (articlesByTitle != null && !articlesByTitle.isEmpty()){
            setTextViewDrawable(holder.saveTxt, R.mipmap.ic_turned_in_black_24dp);
        }else {
            setTextViewDrawable(holder.saveTxt, R.mipmap.ic_turned_in_not_black_24dp);
        }
    }

    private void setTextViewDrawable(TextView holder, int res){

        Drawable drawable = ContextCompat.getDrawable(context, res);

        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.colorAccent));
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            holder.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }

    }

    @Override
    public int getItemCount(){
        if (newsList != null) {
            return newsList.size();
        }else {
            return articles.size();
        }
    }

    public interface NewsAdapterListener{
        void onNewsSelected(String source, String url);
        void onNewsPictureSelected(String title, String img);
    }
}
