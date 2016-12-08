package com.jalotsav.aalayam.adapters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;

public class Listvw_Adptr_PymntHstry extends BaseAdapter implements AalayamConstants {


	Activity _context;
	ArrayList<String> _arrylst_SrowFelmnt, _arrylst_FrowFelmnt, _arrylst_SrowSelmnt, _arrylst_FrowSelmnt;
	ArrayList<Integer> _arrylst_prmryclr;
	String _comeFrom, _iconFirstCharType;
	
	public Listvw_Adptr_PymntHstry(Activity context, String comeFrom, String iconFirstCharType,
			ArrayList<String> arrylst_FrowFelmnt, ArrayList<String> arrylst_FrowSelmnt,
			ArrayList<String> arrylst_SrowFelmnt, ArrayList<String> arrylst_SrowSelmnt) {
		super();
		this._context = context;
		this._comeFrom = comeFrom;
		this._iconFirstCharType = iconFirstCharType;
		this._arrylst_FrowFelmnt = arrylst_FrowFelmnt;
		this._arrylst_FrowSelmnt = arrylst_FrowSelmnt;
		this._arrylst_SrowFelmnt = arrylst_SrowFelmnt;
		this._arrylst_SrowSelmnt = arrylst_SrowSelmnt;
		this._arrylst_prmryclr = General_Fnctns.getPrimaryColorArray(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _arrylst_SrowFelmnt.size();
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
        TextView tvFirstChar, tvSrowFelmntLbl, tvSrowFelmnt, tvFrowFelmnt, tvSrowSelmnt, tvFrowSelmnt;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		LayoutInflater inflater = _context.getLayoutInflater();
		
		if(convertview == null){
			
			convertview = inflater.inflate(R.layout.lo_lstvw_item_2row4elmnt_lstvwrow, null);
			holder = new ViewHolder();
			holder.tvFirstChar = (TextView)convertview.findViewById(R.id.tv_lstvw_item_tworowfourelmnt_firstchr);
			holder.tvSrowFelmntLbl = (TextView)convertview.findViewById(R.id.tv_lstvw_item_tworowfourelmnt_srow_felmnt_lbl);
			holder.tvSrowFelmnt = (TextView)convertview.findViewById(R.id.tv_lstvw_item_tworowfourelmnt_srow_felmnt);
			holder.tvFrowFelmnt = (TextView)convertview.findViewById(R.id.tv_lstvw_item_tworowfourelmnt_frow_felmnt);
			holder.tvSrowSelmnt = (TextView)convertview.findViewById(R.id.tv_lstvw_item_tworowfourelmnt_srow_selmnt);
			holder.tvFrowSelmnt = (TextView)convertview.findViewById(R.id.tv_lstvw_item_tworowfourelmnt_frow_selmnt);
			convertview.setTag(holder);
			
		}else{
			
			holder = (ViewHolder)convertview.getTag();
		}
		
		int n = _arrylst_prmryclr.size();
		Random rndm_no = new Random();
		n = rndm_no.nextInt(n);
		
		GradientDrawable bgShape = (GradientDrawable)holder.tvFirstChar.getBackground();
		bgShape.setColor(_arrylst_prmryclr.get(n));

		String strHtmlPrtclr = Html.fromHtml(_arrylst_FrowFelmnt.get(position)).toString();
		
		if(_iconFirstCharType.equals(ICON_FIRST_CHAR_OFTEXT)){

			if(!TextUtils.isEmpty(strHtmlPrtclr)){

				holder.tvFirstChar.setText(Html.fromHtml(strHtmlPrtclr).toString().substring(0,1).toUpperCase(Locale.getDefault()));
			}
		}else if(_iconFirstCharType.equals(ICON_FIRST_TWODIGIT_OFTEXT)){
			
			holder.tvFirstChar.setText(Html.fromHtml(strHtmlPrtclr).toString().substring(0,2));
		}
		
		holder.tvFrowFelmnt.setText(Html.fromHtml(strHtmlPrtclr).toString().trim());
		holder.tvFrowSelmnt.setText(_arrylst_FrowSelmnt.get(position));
		if(_comeFrom.equals(COME_FROM_PAYMENTS)) holder.tvSrowFelmntLbl.setText(_context.getResources().getString(R.string.pymnt_id_coln));
		else if(_comeFrom.equals(COME_FROM_INVENTORY)) holder.tvSrowFelmntLbl.setText(_context.getResources().getString(R.string.qty_coln));
		holder.tvSrowFelmnt.setText(_arrylst_SrowFelmnt.get(position));
		holder.tvSrowSelmnt.setText(_context.getResources().getString(R.string.symbl_rupee) + " " + _arrylst_SrowSelmnt.get(position));
		
		return convertview;
	}
}
