package org.huxizhijian.simplerecipebook.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import org.huxizhijian.simplerecipebook.greendao.entity.SearchHistory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SEARCH_HISTORY".
*/
public class SearchHistoryDao extends AbstractDao<SearchHistory, Long> {

    public static final String TABLENAME = "SEARCH_HISTORY";

    /**
     * Properties of entity SearchHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SearchKey = new Property(1, String.class, "searchKey", false, "SEARCH_KEY");
        public final static Property SaveTime = new Property(2, long.class, "saveTime", false, "SAVE_TIME");
    }


    public SearchHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public SearchHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SEARCH_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SEARCH_KEY\" TEXT," + // 1: searchKey
                "\"SAVE_TIME\" INTEGER NOT NULL );"); // 2: saveTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SEARCH_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SearchHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String searchKey = entity.getSearchKey();
        if (searchKey != null) {
            stmt.bindString(2, searchKey);
        }
        stmt.bindLong(3, entity.getSaveTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SearchHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String searchKey = entity.getSearchKey();
        if (searchKey != null) {
            stmt.bindString(2, searchKey);
        }
        stmt.bindLong(3, entity.getSaveTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SearchHistory readEntity(Cursor cursor, int offset) {
        SearchHistory entity = new SearchHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // searchKey
            cursor.getLong(offset + 2) // saveTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SearchHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSearchKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSaveTime(cursor.getLong(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SearchHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SearchHistory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SearchHistory entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
