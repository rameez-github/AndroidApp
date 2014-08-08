package com.developer.adapter;


import com.developer.utils.Const;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;
import com.example.androidapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


public class SettingsAdapter extends BaseAdapter {

	private Context 				mContext = null;
	private SafeJSONArray			mArray;
	
	private static final int		CT_PROFILE	 = 0;
	private static final int		CT_HEADER	 = 1;
	private static final int		CT_TEXT_1	 = 2;
	private static final int		CT_TEXT_2	 = 3;
	private static final int		CT_TEXT_SW	 = 3;

	public SettingsAdapter(Context context, SafeJSONArray mArray) {
		this.mContext 	= context;
		this.mArray 	= mArray;
	}
	
	@Override
	public int getCount() {
		if (mArray == null) 
			return 0;
		return mArray.length();
	}
	
	@Override
    public int getItemViewType(int position) {
		SafeJSONObject object = getItem(position);
		String mType = object.getString("item_type");
	    if (mType.equals(Const.TYPE_PROFILE)) {
			return CT_PROFILE;
	            
        } else if (mType.equals(Const.TYPE_HEADER)) {
        	return CT_HEADER;
            
        } else if (mType.equals(Const.TYPE_TEXT_1)){
	    	return CT_TEXT_1;
	    
        }else if (mType.equals(Const.TYPE_TEXT_2)){
	    	return CT_TEXT_2;
	    
        }else {
    	    return CT_TEXT_SW;
    	    
        }
	    
    }
	
    @Override
    public int getViewTypeCount() {
        return 5;
    }

	@Override
	public SafeJSONObject getItem(int position) {
		return mArray.getJSONObject(position);
	} 

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ItemHolder {
		
		private TextView			mHeaderText;
		private TextView			mTextView1;
		private TextView			mTextView2;
		private ImageView 			mEditProfile;
		private ImageView 			mProfile;
		private Switch				mSwitch;
		
		public void initUIElements(View view, Context context, String mType) {
			
			mProfile		= (ImageView) view.findViewById(R.id.imageView1);
			mEditProfile	= (ImageView) view.findViewById(R.id.imageView2);
			
			mTextView1		= (TextView) view.findViewById(R.id.textView1);
			mTextView2		= (TextView) view.findViewById(R.id.textView2);
			mTextView2		= (TextView) view.findViewById(R.id.heading1);
			
			mSwitch			= (Switch) view.findViewById(R.id.switch1);
						
		}
		
		public void configureValues(SafeJSONObject itemObject, View view) {
			
			String mType = itemObject.getString("item_type");
			
			if (mType.equals(Const.TYPE_PROFILE)) {
				
			}else if (mType.equals(Const.TYPE_HEADER)) {
				
			}else if (mType.equals(Const.TYPE_TEXT_1)) {
				
			}else if (mType.equals(Const.TYPE_TEXT_2)) {
				
			}else if (mType.equals(Const.TYPE_TEXT_SWITCH)) {
				
			}

		}
				
    }
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ItemHolder holder = null;

		final SafeJSONObject object = getItem(position);
		Log.v("jsonOB", "jsonOB: "+object.toString());
		final String mType = object.getString("item_type");
		
		if(view == null) {
		    if (getItemViewType(position) == CT_PROFILE){
		    	view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_type_profile, null);
		    
		    } else if (getItemViewType(position) == CT_HEADER) {
		    	view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_header, null);
	            
	        } else if (getItemViewType(position) == CT_TEXT_1){
		    	view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_type_text_1, null);
		    
	        } else if (getItemViewType(position) == CT_TEXT_2) {
		    	view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_type_text_2, null);
	            
	        } else if (getItemViewType(position) == CT_TEXT_SW) {
		    	view = LayoutInflater.from(mContext).inflate(R.layout.adapter_settings_type_text_switch, null);
	            
	        }
		    
			holder = new ItemHolder();
			view.setTag(holder);
			
			holder.initUIElements(view, mContext, mType);
        }
		else {
			holder = (ItemHolder) view.getTag();
		}

		holder.configureValues(object, view);
		

		return view;
	}
}
