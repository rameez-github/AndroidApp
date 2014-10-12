package com.developer.group.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.developer.model.ContactModel;
import com.example.androidapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoCompleteContactAdapter extends BaseAdapter implements Filterable {

	public void addAllItems(List<HashMap<String, String>> myObjs) {
		// TODO Auto-generated method stub

    	//Log.v("collection-length", "--- "+list.size());
        fullList = myObjs;
        mOriginalValues = new ArrayList<HashMap<String, String>>(fullList);
		
	}

	private LayoutInflater mInflater;
	private List<HashMap<String, String>> fullList;
    private List<HashMap<String, String>> mOriginalValues;
    private ArrayFilter mFilter;

    public AutoCompleteContactAdapter(Context context, List<HashMap<String, String>> objects) {
    	
    	super();
		mInflater = LayoutInflater.from(context);
    	fullList = (ArrayList<HashMap<String, String>>) objects;
    	mOriginalValues = new ArrayList<HashMap<String, String>>(fullList);

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return fullList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_device_contact_item, null);
			holder = new ViewHolder();
			
			holder.user_pic = (ImageView) convertView.findViewById(R.id.new_contact_pic);
			holder.name = (TextView) convertView.findViewById(R.id.textview_name);
			holder.contact_number = (TextView) convertView.findViewById(R.id.textview_number);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// set tag with hashmap, later use get tag in onItemClick callback  
		holder.name.setTag(fullList.get(position));
		
		holder.name.setText(fullList.get(position).get(ContactModel.DEVICE_CONTACT_DISPLAY_NAME));
		holder.contact_number.setText(fullList.get(position).get(ContactModel.DEVICE_CONTACT_DISPLAY_NUMBER));
		
		//holder.user_pic.setImageResource(list.get(position).imgRes);
		return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

	static class ViewHolder {
		public ImageView user_pic;
		public TextView name, contact_number;
	}

    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<HashMap<String, String>>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                	//Log.v("lock", "---");
                    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                List<HashMap<String, String>> values = mOriginalValues;
                int count = values.size();
                //Log.v("else-lock", "--- "+count);
                ArrayList<HashMap<String, String>> newValues = new ArrayList<HashMap<String, String>>(count);

                for (int i = 0; i < count; i++) {
                    HashMap<String, String> item = values.get(i);
                    if (item.get(ContactModel.DEVICE_CONTACT_DISPLAY_NAME).toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                    }

                }
                //Log.v("after-loop", "--- "+newValues.size());
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	//Log.v("publish-start", "---");
        	if(results.values!=null){
        		fullList = (ArrayList<HashMap<String, String>>) results.values;
        	}else{
        		fullList = new ArrayList<HashMap<String, String>>();
        	}
            if (results.count > 0) {
            	//Log.v("publish-result", "---"+results.count);
                notifyDataSetChanged();
            } else {
            	//Log.v("publish-zero", "---");
                notifyDataSetInvalidated();
            }
        }
    }

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void clear() {
		// TODO Auto-generated method stub
		fullList.clear();
		fullList = null;
	}
}
