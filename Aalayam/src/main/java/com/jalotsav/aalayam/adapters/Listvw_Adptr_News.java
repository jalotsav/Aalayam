package com.jalotsav.aalayam.adapters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;

public class Listvw_Adptr_News extends BaseAdapter implements AalayamConstants {


	Activity _context;
	ArrayList<String> _arrylst_NewsTitle, _arrylst_ForWho, _arrylst_NewsDate;
	ArrayList<Boolean> _arrylst_ReadStatus;
	ArrayList<Integer> _arrylst_prmryclr;
	
	public Listvw_Adptr_News(Activity context,
			ArrayList<String> arrylst_NewsTitle, ArrayList<String> arrylst_NewsDate, 
			ArrayList<String> arrylst_ForWho, ArrayList<Boolean> arrylst_ReadStatus) {
		super();
		this._context = context;
		this._arrylst_NewsTitle = arrylst_NewsTitle;
		this._arrylst_NewsDate = arrylst_NewsDate;
		this._arrylst_ForWho = arrylst_ForWho;
		this._arrylst_ReadStatus = arrylst_ReadStatus;
		this._arrylst_prmryclr = General_Fnctns.getPrimaryColorArray(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _arrylst_NewsTitle.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class ViewHolder {
		
        TextView tvFirstChar, tvNewsTitle, tvForWho, tvNewsDate;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		LayoutInflater inflater = _context.getLayoutInflater();
		
		if(convertview == null){
			
			convertview = inflater.inflate(R.layout.lo_lstvw_item_doctor_frgmnt_news, null);
			holder = new ViewHolder();
			holder.tvFirstChar = (TextView)convertview.findViewById(R.id.tv_lstvw_item_doctor_frgmnt_news_firstchr);
			holder.tvNewsTitle = (TextView)convertview.findViewById(R.id.tv_lstvw_item_doctor_frgmnt_news_title);
			holder.tvForWho = (TextView)convertview.findViewById(R.id.tv_lstvw_item_doctor_frgmnt_news_forwho);
			holder.tvNewsDate = (TextView)convertview.findViewById(R.id.tv_lstvw_item_doctor_frgmnt_news_date);
			convertview.setTag(holder);
			
		}else{
			
			holder = (ViewHolder)convertview.getTag();
		}
		
		int n = _arrylst_prmryclr.size();
		Random rndm_no = new Random();
		n = rndm_no.nextInt(n);
		
		GradientDrawable bgShape = (GradientDrawable)holder.tvFirstChar.getBackground();
		bgShape.setColor(_arrylst_prmryclr.get(n));

		String strHtmlPrtclr = Html.fromHtml(_arrylst_NewsTitle.get(position)).toString();
		holder.tvFirstChar.setText(Html.fromHtml(strHtmlPrtclr).toString().substring(0,1).toUpperCase(Locale.getDefault()));
		holder.tvNewsTitle.setText(Html.fromHtml(strHtmlPrtclr).toString().trim());
		holder.tvNewsDate.setText(_arrylst_NewsDate.get(position));
		holder.tvForWho.setText(_arrylst_ForWho.get(position));
		if(!_arrylst_ReadStatus.get(position)){
			
			holder.tvNewsTitle.setTextColor(ContextCompat.getColor(_context, R.color.black));
			holder.tvNewsTitle.setTypeface(null, Typeface.BOLD);
			holder.tvNewsDate.setTextColor(ContextCompat.getColor(_context, R.color.red_alizarin));
		}
		
		return convertview;
	}
}
