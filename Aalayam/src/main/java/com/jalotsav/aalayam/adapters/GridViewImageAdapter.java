package com.jalotsav.aalayam.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.navgndrwer.patient.FullScreenImageViewActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewImageAdapter  extends BaseAdapter implements AalayamConstants{
 
    private Context _context;
    private ArrayList<String> _filePaths = new ArrayList<String>();
    private int imageWidth;
	private ImageLoader imageLoader;
    private DisplayImageOptions optionsImgLoader;
 
    public GridViewImageAdapter(Context context, ArrayList<String> filePaths, int imageWidth) {
        this._context = context;
        this._filePaths = filePaths;
        this.imageWidth = imageWidth;

		// Universal Image Loader Configuration
		imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited())
			imageLoader.init(ImageLoaderConfiguration.createDefault(_context));
        optionsImgLoader = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_gallery))
                .showImageOnFail(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_gallery))
                .showImageOnLoading(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_gallery))
                .build();

    }
 
    @Override
    public int getCount() {
        return this._filePaths.size();
    }
 
    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_context);
        } else {
            imageView = (ImageView) convertView;
        }
 
        // get screen dimensions
//        Bitmap image = decodeFile(_filePaths.get(position), imageWidth, imageWidth);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
//        imageView.setImageBitmap(image);
        imageLoader.displayImage("file:///" + _filePaths.get(position), imageView, optionsImgLoader);
        
        // image view click listener
        imageView.setOnClickListener(new OnImageClickListener(position));
 
        return imageView;
    }
 
    class OnImageClickListener implements OnClickListener {
 
        int _postion;
 
        // constructor
        public OnImageClickListener(int position) {
            this._postion = position;
        }
 
        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            Intent i = new Intent(_context, FullScreenImageViewActivity.class);
            i.putExtra(SLCTD_POSITION, _postion);
            i.putStringArrayListExtra(ARRYLST_FILEPATHS_FULLSCRNIMAGE, _filePaths);
            _context.startActivity(i);
        }
 
    }
 
    /*
     * Resizing image size
     */
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {
 
            File f = new File(filePath);
 
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
 
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;
 
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
