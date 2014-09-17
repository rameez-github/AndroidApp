package com.example.androidapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import com.developer.adapter.EmoticonsGridAdapter.KeyClickListener;
import com.developer.adapter.EmoticonsPagerAdapter;
import com.developer.adapter.MessageAdapter;
import com.developer.album.ActivityAlbumDetail;
import com.developer.album.ActivityAlbumList;
import com.developer.model.Album;
import com.developer.model.Message;
import com.developer.utils.DataKeeper;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FragmentChatDetails extends Fragment implements KeyClickListener, LocationListener {
	

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
	private ImageLoader imageLoader;
	private MainActivity fromMainActivity;
	private SupportMapFragment mMapFragment;
	protected GoogleMap mMap;
	private LocationManager locManager;
	private ToggleButton toggle_drawer;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.fromMainActivity = (MainActivity) activity;
		fromMainActivity.mLayout.canOpen = false;
		fromMainActivity.changeTopBarIcons(getClass());
		fromMainActivity.findViewById(R.id.album_button).setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(
						new Intent (getActivity(), ActivityAlbumList.class),
						100
						);
			}
			
		});
		toggle_drawer = (ToggleButton)fromMainActivity.findViewById(R.id.toggle_drawer_layout);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		fromMainActivity.mLayout.canOpen = true;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_chat_details,
		        container, false);

		emoticons_map = new HashMap<String, Integer>();
		emoticons_map.put(":)", R.drawable.load);
		  
		text = (EditText) view.findViewById(R.id.text);
		send = (Button) view.findViewById(R.id.btn_send);

        final DrawerLayout mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DrawerListener (){

			@Override
			public void onDrawerClosed(View arg0) {}

			@Override
			public void onDrawerOpened(View arg0) {
				// TODO Auto-generated method stub
				showMap();}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {}

			@Override
			public void onDrawerStateChanged(int arg0) {}
        	
        });
        if (!toggle_drawer.isChecked())
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        
        toggle_drawer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
					//mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
				}else {
					//mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				}
			}
		});
		initImageLoader ();
		
		messages = new ArrayList<Message>();

		readEmoticons();
		messages.add(new Message(getSmileyText("How about dinner tonight?", "11.png"), "2:19 PM", R.drawable.pic7, false));
		messages.add(new Message(Html.fromHtml("Ok. See you at 7 o'clock!", null, null), "2:23 PM", true));
		messages.add(new Message(Html.fromHtml("Wassup??", null, null), "2:31 PM", R.drawable.pic8, false));
		messages.add(new Message(getSmileyText("nothing much, working on speech bubbles.", "8.png"), "2:33 PM", true));
		messages.add(new Message(Html.fromHtml("you say!", null, null), "2:47 PM", true));
		messages.add(new Message(getSmileyText("oh thats great. how are you showing them", "24.png"), "2:50 PM", R.drawable.pic7, false));
		

		adapter = new MessageAdapter(getActivity(), messages, imageLoader);
		setListAdapter(view, adapter);
		addNewMessage(new Message(Html.fromHtml("mmm, well, using 9 patches png to show them.", null, null), "2:59 PM", true));
		
		Album album = getNewAlbumCreated();
		if (album != null)
			addNewMessage(new Message(getNewAlbumCreated(), false));

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

	private void showMap() {
		FragmentTransaction ft = 
				getChildFragmentManager().beginTransaction();
		
		if (mMapFragment == null) {
			mMapFragment = new SupportMapFragment(){
				@Override
				public void onActivityCreated(Bundle savedInstanceState) {
					super.onActivityCreated(savedInstanceState);
					mMap = mMapFragment.getMap();
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					
					addMapPins(getLocation());
				}
			};
	        ft.replace(R.id.left_drawer, mMapFragment).commit();
		}
	}
	
	private void addMapPins(Location location) {
		
		if (location == null)
			return;
		
		mMap.clear();
		
		LatLng myLatlng = new LatLng (location.getLatitude(), location.getLongitude());
		mMap.addMarker(new MarkerOptions()
						.position(myLatlng)
						.title("")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location_pin)));
		
		if (myLatlng != null)
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 12));
		
	}
	

	private Location getLocation()
	{
		Location location = null;
		try
		{
			final long	MIN_JARAK_GPS_UPDATE	= 10;				// meter
			final long	MIN_WAKTU_GPS_UPDATE	= 1000 * 60 * 1;
			if (locManager == null)
				locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			boolean isGPSEnable = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean isNetworkEnable = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnable && !isNetworkEnable)
			{
			} else {
				if (isGPSEnable)
				{
					locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_WAKTU_GPS_UPDATE, MIN_JARAK_GPS_UPDATE, this);
					location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					
				}
				
				if (isNetworkEnable)
				{
					locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_WAKTU_GPS_UPDATE, MIN_JARAK_GPS_UPDATE, this);
					location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return location;
	}
	
	private Album getNewAlbumCreated (){
		SafeJSONArray album_array = DataKeeper.sharedInstance().getAlbums(getActivity());
		
		// get most recent album created
		SafeJSONObject album_item = album_array.getJSONObject(album_array.length() - 1);

		// assign message album
		if (album_array != null && album_array.length() > 0){
			if (album_item != null){
				Album album = new Album ();
				album.album_index = album_array.length() - 1;
				album.album_title = album_item.getString("album_title");
				album.pic_path = album_item.getJSONArray("all_path").getString(0);
				album.total_album_pics = album_item.getString("total_album_pics");
				return album;
			}
		}
		return null;
	}
	
	private void initImageLoader() {
		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			Album album = getNewAlbumCreated();
			if (album != null)
				addNewMessage(new Message(getNewAlbumCreated(), true));
		}
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
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) popupWindow.dismiss();
			}
		});
		

		text.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) 
					if (popupWindow.isShowing()) popupWindow.dismiss();
			}
		});

		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(text.getText().toString().length() > 0)
				{
					//addNewMessage(new Message(getSmiledText(getActivity(),newMessage), "--:-- AM", true, true));
					//Log.v("text", "--- "+text.getText().toString());
					addNewMessage(new Message(text.getText(), "--:-- AM", true));
					text.setText("");
					//new SendMessage().execute();
				}
			}
		});
	}

	private void setListAdapter(View view, MessageAdapter adapter2) {
		// TODO Auto-generated method stub
		chatList = (ListView) view.findViewById(R.id.list);
		chatList.setAdapter(adapter2);
		chatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (messages.get(position).hasAlbum()){
					Intent i = new Intent(getActivity(), ActivityAlbumDetail.class);
					i.putExtra("start_from_class", getClass());
					i.putExtra("index_of_album", messages.get(position).getAlbum().album_index);
					startActivity(i);
				}
					
			}
		});

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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		addMapPins(location);
		locManager.removeUpdates(this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
