package com.neteru.hermod.classes.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.neteru.hermod.classes.models.Article;

import java.util.List;

@SuppressWarnings("unused")
public class DbManager extends OrmLiteSqliteOpenHelper {

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "direct_news.db";
    private final static String  TAG = "DB_MANAGER";

    public DbManager(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Article.class);

        }catch (Exception e){
            Log.e(TAG, "Erreur lors de la création des Tables - "+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Article.class, true);
            TableUtils.clearTable(connectionSource, Article.class);

        }catch (Exception e){
            Log.e(TAG, "Erreur lors de la mise à jour des Tables - "+e);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void db_insertArticles(List<Article> Articles){
        try {

            getDao(Article.class).create(Articles);

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de l'insertion des objets dans la Table - "+e);

        }
    }

    public void db_insertArticle(Article Article){
        try {

            getDao(Article.class).create(Article);

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de l'insertion de l'objet dans la Table - "+e);

        }
    }

    public List<Article> db_getAllArticles(){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            QueryBuilder<Article, Integer> qb = dao.queryBuilder();

            qb.orderBy("id", false);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture de toute la Table - "+e);
            return null;
        }
    }

    public List<Article> db_getArticleByCategory(String category){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            QueryBuilder<Article, Integer> qb = dao.queryBuilder();

            qb.orderBy("id", false);
            qb.where().eq("category", category);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture par categorie de la Table - "+e);
            return null;
        }
    }

    public Article db_getArticleById(String id){
        try {

            return getDao(Article.class).queryForEq("id", id).get(0);

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture par titre de la Table - "+e);
            return null;
        }
    }

    public List<Article> db_getArticleByTitle(String title){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            QueryBuilder<Article, Integer> qb = dao.queryBuilder();

            qb.orderBy("publishedAt", false);
            qb.where().eq("title", title);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture par titre de la Table - "+e);
            return null;
        }
    }

    public List<Article> db_getArticleByDescription(String description){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            QueryBuilder<Article, Integer> qb = dao.queryBuilder();

            qb.orderBy("publishedAt", false);
            qb.where().eq("description", description);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture par description de la Table - "+e);
            return null;
        }
    }

    public List<Article> db_getArticleByFav(boolean fav){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            QueryBuilder<Article, Integer> qb = dao.queryBuilder();

            qb.orderBy("id", false);
            qb.where().eq("fav", fav);

            return qb.query();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture si favoris de la Table - "+e);
            return null;
        }
    }

    public void db_updateArticleFav(Article article, boolean fav){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            UpdateBuilder<Article, Integer> ub = dao.updateBuilder();

            ub.updateColumnValue("fav", fav);
            ub.where().eq("id",article.getId())
                    .and()
                    .eq("title",article.getTitle())
                    .and()
                    .eq("description",article.getDescription());
            ub.update();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la mise à jour dans la Table - "+e);

        }
    }

    public void db_removeArticle(Article article){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            DeleteBuilder<Article, Integer> ub = dao.deleteBuilder();

            ub.where().eq("id",article.getId())
                    .and()
                    .eq("title",article.getTitle())
                    .and()
                    .eq("description",article.getDescription());
            ub.delete();

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la suppression dans la Table - "+e);

        }
    }

    public List<Article> db_searchArticleByTitleAndDescription(String q){
        try {

            Dao<Article, Integer> dao = getDao(Article.class);
            QueryBuilder<Article, Integer> qb = dao.queryBuilder();

            qb.where().like("title", "%"+q+"%").or().like("description", "%"+q+"%");
            PreparedQuery<Article> pq = qb.prepare();

            return dao.query(pq);

        }catch (Exception e){

            Log.e(TAG, "Erreur lors de la lecture par titre et description de la Table - "+e);
            return null;
        }
    }

    public void db_clearArticlesTable(){
        try {

            TableUtils.clearTable(getConnectionSource(), Article.class);

        }catch (Exception e){
            Log.e(TAG, "Erreur lors de la purge de la Table - "+e);
        }
    }
}
