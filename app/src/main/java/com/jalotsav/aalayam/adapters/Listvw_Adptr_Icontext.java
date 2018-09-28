package com.jalotsav.aalayam.adapters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;

public class Listvw_Adptr_Icontext extends BaseAdapter implements AalayamConstants, Filterable {

	Activity _context;
	ArrayList<String> _arrylst_title, _arrylst_title_temp;
	ArrayList<Integer> _arrylst_prmryclr;
	String _iconFirstCharType;
	ValueFilter valueFilter;
	
	public Listvw_Adptr_Icontext(Activity context,	String iconFirstCharType, ArrayList<String> arrylst_title) {
		super();
		this._context = context;
		this._arrylst_title = arrylst_title;
		this._arrylst_title_temp = arrylst_title;
		this._arrylst_prmryclr = General_Fnctns.getPrimaryColorArray(context);
		this._iconFirstCharType = iconFirstCharType;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _arrylst_title_temp.size();
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
        TextView tvFirstChar;
        TextView tvTitle;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		LayoutInflater inflater = _context.getLayoutInflater();
		
		if(convertview == null){
			
			convertview = inflater.inflate(R.layout.lo_lstvw_item_icontext, null);
			holder = new ViewHolder();
			holder.tvFirstChar = (TextView)convertview.findViewById(R.id.tv_lstvw_item_icontext_firstchr);
			holder.tvTitle = (TextView)convertview.findViewById(R.id.tv_lstvw_item_icontext_title);
			convertview.setTag(holder);
		}else{
			
			holder = (ViewHolder)convertview.getTag();
		}
		
		int n = _arrylst_prmryclr.size();
		Random rndm_no = new Random();
		n = rndm_no.nextInt(n);
		
		GradientDrawable bgShape = (GradientDrawable)holder.tvFirstChar.getBackground();
		bgShape.setColor(_arrylst_prmryclr.get(n));
		
		if(_iconFirstCharType.equals(ICON_FIRST_CHAR_OFTEXT)){
			
			holder.tvFirstChar.setText(_arrylst_title_temp.get(position).substring(0,1).toUpperCase(Locale.getDefault()));
		}else if(_iconFirstCharType.equals(ICON_FIRST_TWODIGIT_OFTEXT)){
			
			holder.tvFirstChar.setText(_arrylst_title_temp.get(position).substring(0,2));
		}
		holder.tvTitle.setText(_arrylst_title_temp.get(position));
		
		return convertview;
	}

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
            	
                ArrayList<String> filterList = new ArrayList<String>();
                
                for (int i = 0; i < _arrylst_title.size(); i++) {

                    if ( (_arrylst_title.get(i).toUpperCase(Locale.getDefault()) )
                            .contains(constraint.toString().toUpperCase(Locale.getDefault()))) {

                        filterList.add(_arrylst_title.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = _arrylst_title.size();
                results.values = _arrylst_title;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                FilterResults results) {
            _arrylst_title_temp = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
