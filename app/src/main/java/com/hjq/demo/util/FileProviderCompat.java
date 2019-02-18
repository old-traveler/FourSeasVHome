package com.hjq.demo.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileProviderCompat {


  public static String getRealPathFromURI(Context context, Uri contentURI) {
    String result;
    Cursor cursor = context.getContentResolver().query(contentURI,
        new String[]{ MediaStore.Images.ImageColumns.DATA},//
        null, null, null);
    if (cursor == null) result = contentURI.getPath();
    else {
      cursor.moveToFirst();
      int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      result = cursor.getString(index);
      cursor.close();
    }
    return result;
  }


}
