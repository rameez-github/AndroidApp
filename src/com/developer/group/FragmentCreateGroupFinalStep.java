package com.developer.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.group.adapter.AutoCompleteContactAdapter;
import com.developer.model.ContactModel;
import com.example.androidapp.R;

public class FragmentCreateGroupFinalStep  extends Fragment implements OnClickListener{
	
	private AutoCompleteContactAdapter mAutoCompleteContactAdapter;
	private EditText search_edit;
	private ListView list_view;
	private String groupName, filePath;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_create_group_final_step,
		        container, false);
		
		view.findViewById(R.id.btn_step_back).setOnClickListener(this);
		view.findViewById(R.id.btn_done).setOnClickListener(this);
		
		getPreviousFragmentArgumentValue ();
		init (view);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_step_back){
			getActivity().getSupportFragmentManager().popBackStack();
		}
		else if (v.getId() == R.id.btn_done){
			/*String chips[] = search_edit.getText().toString().trim().split(",");
			StringBuilder sb = new StringBuilder();
			for(String c : chips){
				sb.append("\n--: "+c);
			}
			Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();*/
			Intent newIntent = new Intent();
			newIntent.putExtra("group_name", groupName);
			newIntent.putExtra("file_path", filePath);
			newIntent.putExtra("contacts", search_edit.getText().toString().trim());
			getActivity().setResult(Activity.RESULT_OK, newIntent);
			getActivity().finish();
		}
		
	}

	private void getPreviousFragmentArgumentValue() {
		groupName = getArguments().getString("group_name");
		filePath = getArguments().getString("file_path");
	}
	
	private void init(View view) {

		search_edit = (EditText) view.findViewById(R.id.edit_search_contact);
		mAutoCompleteContactAdapter = new AutoCompleteContactAdapter (getActivity(), new ArrayList<HashMap<String, String>>());
		search_edit.addTextChangedListener(new TextWatcher (){
			
			//ArrayAdapter <String> adapter = new ArrayAdapter <String> (MainScreen.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());

			@Override
			public void afterTextChanged(Editable arg0) { // TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { // TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence userInput, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				/*if(count >=1){
					if(userInput.charAt(start) == ',')
						setChips(); // generate chips
				}
				*/
	            // update the adapater
				mAutoCompleteContactAdapter.notifyDataSetChanged();
	             
	            // get suggestions from the database
	            List<HashMap<String, String>> myObjs = getContactNameFromNumber(userInput.toString());//readContacts(userInput.toString());
	            
	            // clear items
	            mAutoCompleteContactAdapter.clear();
	            
	            // update the adapter
	            mAutoCompleteContactAdapter.addAllItems(myObjs);
	            
	            // set adapter
	            list_view.setAdapter(mAutoCompleteContactAdapter);
				
				//Toast t = Toast.makeText(MainScreen.this, s, Toast.LENGTH_SHORT);
				//t.setGravity(Gravity.TOP, 0, 100);
				//t.show();
				
			}
			
		});
		list_view = (ListView) view.findViewById(R.id.list_view);
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				String str = search_edit.getText().toString().trim();
				int endIndex = str.lastIndexOf(",");
				if (endIndex != -1){
					str = str.substring(0, endIndex).concat(","); // not forgot to put check if(endIndex != -1)
				}else{
					str = "";
					search_edit.setText(str);
				}
				@SuppressWarnings("unchecked")
				HashMap<String, String> contact = (HashMap<String, String>)view.findViewById(R.id.textview_name).getTag();
				String number = contact.get(ContactModel.DEVICE_CONTACT_DISPLAY_NUMBER);
				//String number = mAutoCompleteContactAdapter.getItem(position).get(ContactModel.DEVICE_CONTACT_DISPLAY_NUMBER);
				search_edit.setText(str.concat(number).concat(","));
				setChips();
			}
		});

	}
	
	/*This function has whole logic for chips generate*/
	@SuppressWarnings("deprecation")
	public void setChips(){
		if(search_edit.getText().toString().contains(",")) // check comman in string
		{
			
			SpannableStringBuilder ssb = new SpannableStringBuilder(search_edit.getText());
			// split string wich comma
			String chips[] = search_edit.getText().toString().trim().split(",");
			int x =0;
			// loop will generate ImageSpan for every country name separated by comma
			for(String c : chips){
				// inflate chips_edittext layout 
				LayoutInflater lf = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				TextView textView = (TextView) lf.inflate(R.layout.chips_edittext, null);
				textView.setText(c); // set text
				//int image = ((ChipsAdapter) getAdapter()).getImage(c);
				//textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, image, 0);
				// capture bitmapt of genreated textview
				int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				textView.measure(spec, spec);
				textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
				Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(),Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(b);
				canvas.translate(-textView.getScrollX(), -textView.getScrollY());
				textView.draw(canvas);
				textView.setDrawingCacheEnabled(true);
				Bitmap cacheBmp = textView.getDrawingCache();
				Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
				textView.destroyDrawingCache();  // destory drawable
				// create bitmap drawable for imagespan
				BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
				bmpDrawable.setBounds(0, 0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
				// create and set imagespan 
				ssb.setSpan(new ImageSpan(bmpDrawable),x ,x + c.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				x = x+ c.length() +1;
			}
			// set chips span 
			search_edit.setText(ssb);
			// move cursor to last 
			search_edit.setSelection(search_edit.getText().length());
		}		
	}

	private List<HashMap<String, String>> getContactNameFromNumber(String searchTerm) { 

		String chips[] = search_edit.getText().toString().trim().split(",");
		int last_index = chips.length - 1;
		Log.v("chips", "chip: "+chips[last_index]);
		List<HashMap<String, String>> list= new ArrayList<HashMap<String, String>>();

		ContentResolver cr = getActivity().getContentResolver();
		Cursor pCur = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,

				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" LIKE '%"+chips[last_index].trim()
				+"%' or "
				+ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%"+chips[last_index].trim()+"%'",
				null, //new String[]{"%"+searchTerm +"%"},  
				null
				);
		while (pCur.moveToNext()) {
			String phone = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			String name = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			HashMap<String, String> contactHash = new HashMap<String, String> ();
			contactHash.put(ContactModel.DEVICE_CONTACT_DISPLAY_NAME, name);
			contactHash.put(ContactModel.DEVICE_CONTACT_DISPLAY_NUMBER, phone);
			list.add(contactHash);
			//sb.append("\n Phone number:" + phone);
			//System.out.println("phone" + phone);
		}
		pCur.close();
		
	    return list;
	    //proceed as you need 

	}
}
