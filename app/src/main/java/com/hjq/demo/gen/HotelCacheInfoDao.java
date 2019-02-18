package com.hjq.demo.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hjq.demo.bean.HotelCacheInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HOTEL_CACHE_INFO".
*/
public class HotelCacheInfoDao extends AbstractDao<HotelCacheInfo, String> {

    public static final String TABLENAME = "HOTEL_CACHE_INFO";

    /**
     * Properties of entity HotelCacheInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ObjectId = new Property(0, String.class, "objectId", true, "OBJECT_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Score = new Property(2, float.class, "score", false, "SCORE");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property City = new Property(4, String.class, "city", false, "CITY");
        public final static Property Image = new Property(5, String.class, "image", false, "IMAGE");
        public final static Property Latlon = new Property(6, String.class, "latlon", false, "LATLON");
        public final static Property Type = new Property(7, String.class, "type", false, "TYPE");
        public final static Property Contact = new Property(8, String.class, "contact", false, "CONTACT");
        public final static Property Price = new Property(9, String.class, "price", false, "PRICE");
        public final static Property Desc = new Property(10, String.class, "desc", false, "DESC");
    }


    public HotelCacheInfoDao(DaoConfig config) {
        super(config);
    }
    
    public HotelCacheInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HOTEL_CACHE_INFO\" (" + //
                "\"OBJECT_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: objectId
                "\"NAME\" TEXT," + // 1: name
                "\"SCORE\" REAL NOT NULL ," + // 2: score
                "\"ADDRESS\" TEXT," + // 3: address
                "\"CITY\" TEXT," + // 4: city
                "\"IMAGE\" TEXT," + // 5: image
                "\"LATLON\" TEXT," + // 6: latlon
                "\"TYPE\" TEXT," + // 7: type
                "\"CONTACT\" TEXT," + // 8: contact
                "\"PRICE\" TEXT," + // 9: price
                "\"DESC\" TEXT);"); // 10: desc
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HOTEL_CACHE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HotelCacheInfo entity) {
        stmt.clearBindings();
 
        String objectId = entity.getObjectId();
        if (objectId != null) {
            stmt.bindString(1, objectId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindDouble(3, entity.getScore());
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(5, city);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(6, image);
        }
 
        String latlon = entity.getLatlon();
        if (latlon != null) {
            stmt.bindString(7, latlon);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
 
        String contact = entity.getContact();
        if (contact != null) {
            stmt.bindString(9, contact);
        }
 
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(10, price);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(11, desc);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HotelCacheInfo entity) {
        stmt.clearBindings();
 
        String objectId = entity.getObjectId();
        if (objectId != null) {
            stmt.bindString(1, objectId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindDouble(3, entity.getScore());
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(5, city);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(6, image);
        }
 
        String latlon = entity.getLatlon();
        if (latlon != null) {
            stmt.bindString(7, latlon);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(8, type);
        }
 
        String contact = entity.getContact();
        if (contact != null) {
            stmt.bindString(9, contact);
        }
 
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(10, price);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(11, desc);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public HotelCacheInfo readEntity(Cursor cursor, int offset) {
        HotelCacheInfo entity = new HotelCacheInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // objectId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.getFloat(offset + 2), // score
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // city
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // image
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // latlon
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // type
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // contact
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // price
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // desc
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HotelCacheInfo entity, int offset) {
        entity.setObjectId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setScore(cursor.getFloat(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCity(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setImage(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLatlon(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setType(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setContact(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPrice(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setDesc(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final String updateKeyAfterInsert(HotelCacheInfo entity, long rowId) {
        return entity.getObjectId();
    }
    
    @Override
    public String getKey(HotelCacheInfo entity) {
        if(entity != null) {
            return entity.getObjectId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HotelCacheInfo entity) {
        return entity.getObjectId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}