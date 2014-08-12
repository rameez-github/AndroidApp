package com.example.androidapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.developer.adapter.ContactAdapter;
import com.developer.adapter.ContactGridAdapter;
import com.developer.custom.CustomGridView;
import com.developer.custom.CustomListView;
import com.developer.model.ContactModel;

public class FragmentContact extends Fragment implements OnItemClickListener {
	
	ArrayList<ContactModel> list;
	ArrayList<ContactModel> other_list;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_contacts,
		        container, false);

		CustomGridView gridview = (CustomGridView) view.findViewById(R.id.gridView1);
		CustomListView contact_listview = (CustomListView) view.findViewById(R.id.contact_list_view);
		CustomListView other_contact_listview = (CustomListView) view.findViewById(R.id.other_contact_list_view);
		
		gridview.setExpanded(true);
		contact_listview.setExpanded(true);
		other_contact_listview.setExpanded(true);
		
		contact_listview.setOnItemClickListener(this);
		other_contact_listview.setOnItemClickListener(this);
		
		addContactList ();
		otherContactList ();

		gridview.setAdapter(new ContactGridAdapter(getActivity(), list));
		contact_listview.setAdapter(new ContactAdapter(getActivity(), list));
		other_contact_listview.setAdapter(new ContactAdapter(getActivity(), other_list));
		
		//setGridViewHeightBasedOnChildren(gridview, 3);
		//getListViewSize (listview, list.size());
		return view;
	}
	
	private void addContactList () {
		list = new ArrayList<ContactModel>();

		list.add(new ContactModel("Bobby", "Online via InTouch!", R.drawable.pic1));
		list.add(new ContactModel("Dave", "Enjoying life backpacking India", R.drawable.pic2));
		list.add(new ContactModel("Oliver", "Online via InTouch", R.drawable.pic3));
		list.add(new ContactModel("Ganesh", "Life is like a box of chocolates...", R.drawable.pic4));
		list.add(new ContactModel("Mimi", "In a meeting. InTouch only", R.drawable.pic5));
		list.add(new ContactModel("Kalyan", "Busy", R.drawable.pic6));
	}
	
	private void otherContactList () {
		other_list = new ArrayList<ContactModel>();

		other_list.add(new ContactModel("Mimi", "In a meeting. InTouch only", R.drawable.pic5));
		other_list.add(new ContactModel("Bobby", "Online via InTouch!", R.drawable.pic1));
		other_list.add(new ContactModel("Oliver", "Online via InTouch", R.drawable.pic3));
		other_list.add(new ContactModel("Dave", "Enjoying life backpacking India", R.drawable.pic2));
		other_list.add(new ContactModel("Kalyan", "Busy", R.drawable.pic6));
		other_list.add(new ContactModel("Ganesh", "Life is like a box of chocolates...", R.drawable.pic4));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, new FragmentProfile());
		//////ft.addToBackStack(null);
		ft.commit();
	}
	
	@SuppressWarnings("unused")
	private void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        LinearLayout.LayoutParams params = (LayoutParams) gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

	}
	
	@SuppressWarnings("unused")
	private void getListViewSize(ListView mListView, int items) {
		ListAdapter mAdapter = mListView.getAdapter();

	    int listviewElementsHeight = 0;
	    
	    for (int i = 0; i < items; i++) {
	    	View childView = mAdapter.getView(i, null, mListView);
	        childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        listviewElementsHeight+= childView.getMeasuredHeight();
	    }
	    
	    LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, listviewElementsHeight);
	    
	    mListView.setLayoutParams(lparam);
	    mListView.requestLayout();
	}
}
