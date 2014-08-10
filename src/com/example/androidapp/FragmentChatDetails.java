package com.example.androidapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import com.developer.adapter.EmoticonsGridAdapter.KeyClickListener;
import com.developer.adapter.EmoticonsPagerAdapter;
import com.developer.adapter.MessageAdapter;
import com.developer.model.Message;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class FragmentChatDetails extends Fragment implements KeyClickListener {
	

	private static HashMap<String, Integer> emoticons_map;
	private ArrayList<Message> messages;
	private MessageAdapter adapter;
	private ListView chatList;
	private EditText text;
	private Button send;


	//-------------EMOTICONS ATTRIBUTES--------------
	private static final int NO_OF_EMOTICONS = 54;
	private LinearLayout emoticonsCover;
	private LinearLayout parentLayout;
	private PopupWindow popupWindow;
	private boolean isKeyBoardVisible;
	private Bitmap[] emoticons;
	private int keyboardHeight;
	private View popUpView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_chat_details,
		        container, false);

		emoticons_map = new HashMap<String, Integer>();
		emoticons_map.put(":)", R.drawable.load);
		  
		text = (EditText) view.findViewById(R.id.text);
		send = (Button) view.findViewById(R.id.btn_send);
		
		messages = new ArrayList<Message>();

		readEmoticons();
		messages.add(new Message(getSmileyText("How about dinner tonight?", "11.png"), "2:19 PM", R.drawable.pic7, false));
		messages.add(new Message(Html.fromHtml("Ok. See you at 7 o'clock!", null, null), "2:23 PM", true));
		messages.add(new Message(Html.fromHtml("Wassup??", null, null), "2:31 PM", R.drawable.pic8, false));
		messages.add(new Message(getSmileyText("nothing much, working on speech bubbles.", "8.png"), "2:33 PM", true));
		messages.add(new Message(Html.fromHtml("you say!", null, null), "2:47 PM", true));
		messages.add(new Message(getSmileyText("oh thats great. how are you showing them", "24.png"), "2:50 PM", R.drawable.pic7, false));
		

		adapter = new MessageAdapter(getActivity(), messages);
		setListAdapter(view, adapter);
		addNewMessage(new Message(Html.fromHtml("mmm, well, using 9 patches png to show them.", null, null), "2:59 PM", true));
		

		//-------------EMOTICONS CODE--------------  
		parentLayout = (LinearLayout) view.findViewById(R.id.list_parent);

		emoticonsCover = (LinearLayout) view.findViewById(R.id.footer_for_emoticons);

		popUpView = getActivity().getLayoutInflater().inflate(R.layout.emoticons_popup, null);
		
		// Defining default height of keyboard which is equal to 230 dip
		final float popUpheight = getResources().getDimension(
				R.dimen.keyboard_height);
		changeKeyboardHeight((int) popUpheight);
		
		// Showing and Dismissing pop up on clicking emoticons button
		Button emoticonsButton = (Button) view.findViewById(R.id.emoticons_button);
		emoticonsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!popupWindow.isShowing()) {
					
					popupWindow.setHeight((int) (keyboardHeight));
					
					if (isKeyBoardVisible) {
						emoticonsCover.setVisibility(LinearLayout.GONE);
					} else {
						emoticonsCover.setVisibility(LinearLayout.VISIBLE);
					}
					popupWindow.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
					
				} else {
					popupWindow.dismiss();
				}
				
			}
		});
		
		//readEmoticons();
		enablePopUpView();
		checkKeyboardHeight(parentLayout);
		enableFooterView();
		
		return view;
	}
	

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
			return false;
		}
		
		return true;
	}

	/**
	 * Reading all emoticons in local cache
	 */
	private void readEmoticons () {
		
		emoticons = new Bitmap[NO_OF_EMOTICONS];
		for (short i = 0; i < NO_OF_EMOTICONS; i++) {			
			emoticons[i] = getImage((i+1) + ".png");
		}
		
	}

	/**
	 * For loading smileys from assets
	 */
	private Bitmap getImage(String path) {
		AssetManager mngr = getActivity().getAssets();
		InputStream in = null;
		try {
			in = mngr.open("emoticons/" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Bitmap temp = BitmapFactory.decodeStream(in, null, null);
		return temp;
	}
	
	/**
	 * Checking keyboard height and keyboard visibility
	 */
	int previousHeightDiffrence = 0;
	private void checkKeyboardHeight(final View parentLayout) {

		parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					
					@Override
					public void onGlobalLayout() {
						
						Rect r = new Rect();
						parentLayout.getWindowVisibleDisplayFrame(r);
						
						int screenHeight = parentLayout.getRootView()
								.getHeight();
						int heightDifference = screenHeight - (r.bottom);
						
						if (previousHeightDiffrence - heightDifference > 50) {							
							popupWindow.dismiss();
						}
						
						previousHeightDiffrence = heightDifference;
						if (heightDifference > 100) {

							isKeyBoardVisible = true;
							changeKeyboardHeight(heightDifference);

						} else {

							isKeyBoardVisible = false;
							
						}

					}
				});

	}

	/**
	 * change height of emoticons keyboard according to height of actual
	 * keyboard
	 * 
	 * @param height
	 *            minimum height by which we can make sure actual keyboard is
	 *            open or not
	 */
	private void changeKeyboardHeight(int height) {

		if (height > 100) {
			keyboardHeight = height;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, keyboardHeight);
			emoticonsCover.setLayoutParams(params);
		}

	}

	/**
	 * Defining all components of emoticons keyboard
	 */
	private void enablePopUpView() {

		ViewPager pager = (ViewPager) popUpView.findViewById(R.id.emoticons_pager);
		pager.setOffscreenPageLimit(3);
		
		ArrayList<String> paths = new ArrayList<String>();

		for (short i = 1; i <= NO_OF_EMOTICONS; i++) {			
			paths.add(i + ".png");
		}

		EmoticonsPagerAdapter adapter = new EmoticonsPagerAdapter(getActivity(), paths, this);
		pager.setAdapter(adapter);

		// Creating a pop window for emoticons keyboard
		popupWindow = new PopupWindow(popUpView, LayoutParams.MATCH_PARENT,
				(int) keyboardHeight, false);
		
		TextView backSpace = (TextView) popUpView.findViewById(R.id.back);
		backSpace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				text.dispatchKeyEvent(event);	
			}
		});

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				emoticonsCover.setVisibility(LinearLayout.GONE);
			}
		});
	}

	/**
	 * Enabling all content in footer i.e. post window
	 */
	private void enableFooterView() {

		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Toast.makeText(getActivity(), "edittext clicked", Toast.LENGTH_LONG).show();
				if (popupWindow.isShowing()) {
					
					popupWindow.dismiss();
					
				}
				
			}
		});

		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(text.getText().toString().length() > 0)
				{
					text.setText("");
					//addNewMessage(new Message(getSmiledText(getActivity(),newMessage), "--:-- AM", true, true));
					Log.v("text", "--- "+text.getText().toString());
					addNewMessage(new Message(text.getText(), "--:-- AM", true));
					//new SendMessage().execute();
				}
			}
		});
	}

	private void setListAdapter(View view, MessageAdapter adapter2) {
		// TODO Auto-generated method stub
		chatList = (ListView) view.findViewById(R.id.list);
		chatList.setAdapter(adapter2);
		

		//-------------EMOTICONS CODE--------------  
		chatList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow.isShowing())
					popupWindow.dismiss();	
				return false;
			}
		});
	}
	
	private void addNewMessage(Message m)
	{
		messages.add(m);
		adapter.notifyDataSetChanged();
		chatList.setSelection(messages.size()-1);
	}

	
	// Get image for each text smiles
	@SuppressWarnings("unused")
	private Spannable getSmiledText(Context context, String text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		int index;
		for (index = 0; index < builder.length(); index++) {
			for (Entry<String, Integer> entry : emoticons_map.entrySet()) {
				int length = entry.getKey().length();
				if (index + length > builder.length())
					continue;
				if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
					builder.setSpan(new ImageSpan(context, entry.getValue()), index, index + length,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					index += length - 1;
					break;
				}
			}
		}
		return builder;
	}

	@Override
	public void keyClickedIndex(final String index) {
		// TODO Auto-generated method stub
		ImageGetter imageGetter = new ImageGetter() {
            public Drawable getDrawable(String source) {    
            	StringTokenizer st = new StringTokenizer(index, ".");
                Drawable d = new BitmapDrawable(getResources(),emoticons[Integer.parseInt(st.nextToken()) - 1]);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };
        
        Spanned cs = Html.fromHtml("<img src ='"+ index +"'/>", imageGetter, null);        
		
		int cursorPosition = text.getSelectionStart();		
        text.getText().insert(cursorPosition, cs);
        
	}
	
	private Spannable getSmileyText (String text, final String index){
		ImageGetter imageGetter = new ImageGetter() {
            public Drawable getDrawable(String source) {    
            	StringTokenizer st = new StringTokenizer(index, ".");
                Drawable d = new BitmapDrawable(getResources(),emoticons[Integer.parseInt(st.nextToken()) - 1]);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };
        
        return (Spannable) Html.fromHtml(text+" <img src ='"+ index +"'/>", imageGetter, null);        
		
	}
}
