package com.jalotsav.aalayam.navgndrwer.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jalotsav.aalayam.R;
import com.jalotsav.aalayam.common.AalayamConstants;
import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.NavgnDrawer_Adapter;
import com.jalotsav.aalayam.common.UserSessionManager;
import com.jalotsav.aalayam.service.InternetService;
import com.jalotsav.aalayam.webservice.ServiceHandler;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

// Come from package com.jalotsav.aalayam; --> Aalayam, Login_Pswrd

public class NavgnDrawer_Main_Doctor extends AppCompatActivity implements AalayamConstants{

	static UserSessionManager session;

	//First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    String navgndrwer_menu_titles[];
//    int navgndrwer_menu_icons[] = {R.drawable.ic_dashboard_gray, R.drawable.ic_patient_gray, R.drawable.ic_news_gray, R.drawable.ic_discussion_gray, R.drawable.ic_inventory_gray, R.drawable.ic_payment_gray, R.drawable.ic_doctor_gray};
    int navgndrwer_menu_icons[] = {R.drawable.ic_dashboard_gray, R.drawable.ic_patient_gray, R.drawable.ic_news_gray, R.drawable.ic_inventory_gray, R.drawable.ic_payment_gray, R.drawable.ic_doctor_gray};
    private Toolbar toolbar;                              // Declaring the Toolbar Object

    // nav drawer title
    private CharSequence mdrwer_title;
    // used to store app title
    private CharSequence app_title;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    String mDoctrName, mDoctrEmail, mProfileImageName;
    Bitmap mDoctrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_drwerlyot_doctor);
        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar_drwerlyot_doctor);
        setSupportActionBar(toolbar);

        session = new UserSessionManager(NavgnDrawer_Main_Doctor.this);
        // Set selected position to Shared preferences
        session.setSlctdPostnDctrNavdrwr(1);

        app_title = mdrwer_title = getTitle();

        mRecyclerView = (RecyclerView) findViewById(R.id.rcyclrvw_drwerlyot_doctor); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        //Similarly we Create a String Resource for the name and email in the header view
        //And we also create a int resource for profile picture in the header view
        mDoctrName = "Loading...";
        mDoctrEmail = session.getEmail();
        mDoctrImage = BitmapFactory.decodeResource(getResources(), R.drawable.person_bg_trqs);

        navgndrwer_menu_titles = getResources().getStringArray(R.array.navgndrawer_items_doctor);

        mAdapter = new NavgnDrawer_Adapter(NavgnDrawer_Main_Doctor.this, navgndrwer_menu_titles, navgndrwer_menu_icons, mDoctrName, mDoctrEmail, mDoctrImage);  // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
                                                                             // And passing the titles,icons,header view name, header view email and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        Drawer = (DrawerLayout) findViewById(R.id.drwerlyot_doctor_root); // Drawer object Assigned to the view

        TextView tvJalotsavdev = (TextView) findViewById(R.id.tv_drwerlyot_doctor_jalotsavdev);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/segoeprint.ttf");
		tvJalotsavdev.setTypeface(tf);

        if(!session.checkLogin()){

        	finish();
        }else{

            // Check Internet check service is running or not, If not then Start Service
            if(General_Fnctns.isServiceRunning(InternetService.class, this))
                Log.i(TAG, "InternetCheck Service already Running");
            else{

                Log.e(TAG, "InternetCheck Service NOT Running, Start Service");
                startService(new Intent(this, InternetService.class));
            }

            final GestureDetector mGestureDetector = new GestureDetector(NavgnDrawer_Main_Doctor.this, new GestureDetector.SimpleOnGestureListener() {

                 @Override public boolean onSingleTapUp(MotionEvent e) {
                     return true;
                 }

             });

             mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                 @Override
                 public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                	 View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                     if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){

                    	 // Close Navigation Drawer
                    	 Drawer.closeDrawers();

                         if(mRecyclerView.getChildLayoutPosition(child) > 0){

                        	 // Set selected position to Shared preferences
                             session.setSlctdPostnDctrNavdrwr(recyclerView.getChildLayoutPosition(child));

                             mAdapter.notifyDataSetChanged();

                             displayFragmntView(mRecyclerView.getChildLayoutPosition(child));
                         }

                         return true;
                     }
                     return false;
                 }

                 @Override
                 public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {}

                 @Override
                 public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
             });

            mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

            mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

            mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close){

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                    toolbar.setTitle(mdrwer_title);

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);

                    toolbar.setTitle(app_title);
                }

            }; // Drawer Toggle Object Made
            Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
            mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

            // Directly call DashBoard Fragment rather than Header
            int openFrgmntPostn = getIntent().getIntExtra(OPEN_FRGMNT_BY_POSTN, NAVDRWER_FRGMNT_DASHBOARD);
            if(openFrgmntPostn == NAVDRWER_FRGMNT_DASHBOARD)
                displayFragmntView(1);
            else if(openFrgmntPostn == NAVDRWER_FRGMNT_NEWS){

                // Set selected position to Shared preferences
                session.setSlctdPostnDctrNavdrwr(3);
                mAdapter.notifyDataSetChanged();
                displayFragmntView(3);
            }

    		if(General_Fnctns.isNetConnected(NavgnDrawer_Main_Doctor.this)){

    			new GetDoctorProfileFromWebSrve().execute();
    			// Get Doctor Image from External storage or URL
    			checkGetDoctorImage();
    		}
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    @SuppressLint("NewApi")
    private void displayFragmntView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new DashboardDoctor();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;
        case 1:
            fragment = new DashboardDoctor();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(true);
            break;
        case 2:
            fragment = new MyPatients();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;
        case 3:
            fragment = new NewsFrgmnt();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;
        /*case 4:
            fragment = new DashboardDoctor(); // DISCUSSION
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;*/
        case 4:
            fragment = new MyEquipmentFrgmnt();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;
        case 5:
            fragment = new MyPaymentsFrgmnt();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;
        case 6:
            fragment = new DoctorProfileFrgmnt();
            if(session !=null)
                session.setIsHomeDctrNavdrwr(false);
            break;

        default:
            break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container_doctor, fragment).commit();

            // update selected item and title, then close the drawer
//            lstvw_drwrlst.setItemChecked(position, true);
//            lstvw_drwrlst.setSelection(position);
            setTitle(navgndrwer_menu_titles[position - 1]);
//            mdrwer_lyot.closeDrawer(lstvw_drwrlst);
        } else {
            // error in creating fragment
            Log.e(AalayamConstants.TAG, "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        app_title = title;
        toolbar.setTitle(app_title);
    }

    public void onSectionAttached(int navDrwrFrgmnt) {
        switch (navDrwrFrgmnt) {
            case NAVDRWER_FRGMNT_DASHBOARD:
                app_title = navgndrwer_menu_titles[0];
                break;
            case NAVDRWER_FRGMNT_PATIENTS:
                app_title = navgndrwer_menu_titles[1];
                break;
            case NAVDRWER_FRGMNT_NEWS:
                app_title = navgndrwer_menu_titles[2];
                break;
            case NAVDRWER_FRGMNT_DISCUSSION:
//                app_title = navgndrwer_menu_titles[3];
                break;
            case NAVDRWER_FRGMNT_MYEQUIPMENT:
                app_title = navgndrwer_menu_titles[3];
                break;
            case NAVDRWER_FRGMNT_PAYMENTS:
                app_title = navgndrwer_menu_titles[4];
                break;
            case NAVDRWER_FRGMNT_PROFILE:
                app_title = navgndrwer_menu_titles[5];
                break;
        }
        setActiobarTitle();
    }

    private void setActiobarTitle() {
        getSupportActionBar().setTitle(app_title);
    }

    public class GetDoctorProfileFromWebSrve extends AsyncTask<Void, Void, Void>{

		String websrvc_response;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			ServiceHandler obj_srvchndlr = new ServiceHandler();

			JSONObject jsnobj = new JSONObject();
			try {
				jsnobj.put(FUNCTION_SMALL, FNCTN_SELECT_DOCTOR_BY_DR_ID);
				jsnobj.put(DR_ID, String.valueOf(session.getDrId()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String json = WEBSRVC_JSONKEY + jsnobj.toString();
			websrvc_response = obj_srvchndlr.post_makeServiceCall(json);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(websrvc_response.equals(ACCESS_DENIED)){

				// There are some server problem
			}else{
				try {
					JSONObject websrvc_jsnobj = new JSONObject(websrvc_response);
					String success = websrvc_jsnobj.getString(SUCCESS_SMALL);
					String websrvc_msg = websrvc_jsnobj.getString(MESSAGE_SMALL);
					JSONArray jsonArrayFirst = websrvc_jsnobj.getJSONArray(DOCTOR_SMALL);
					if(success.equals(TRUE_SMALL)){

						if(!websrvc_msg.equals("null") && websrvc_msg.equals(DOCTOR_FOUND)){

							if(jsonArrayFirst.length() !=0){

								for(int i=0; i< jsonArrayFirst.length(); i++){

									JSONArray jsonArraySecnd = jsonArrayFirst.getJSONArray(i);

									for(int j=0; j<jsonArraySecnd.length(); j++){

										JSONObject jsnobj_dctrprfl = jsonArraySecnd.getJSONObject(j);

										mDoctrName = jsnobj_dctrprfl.getString(NAME_FN_SML) + " " + jsnobj_dctrprfl.getString(NAME_LN_SML);
										mDoctrEmail = jsnobj_dctrprfl.getString(EMAIL_SML);
										mProfileImageName = jsnobj_dctrprfl.getString(PROFILE_IMAGE_SML);

										mAdapter = new NavgnDrawer_Adapter(NavgnDrawer_Main_Doctor.this, navgndrwer_menu_titles, navgndrwer_menu_icons, mDoctrName, mDoctrEmail, mDoctrImage);
										mRecyclerView.setAdapter(mAdapter);

										// Check image to External storage if not available Get from AdminPanel and store to device
										checkGetDoctorImage();
									}
								}
							}
						}
					}else if(success.equals(FALSE_SMALL)){

						// There are some server problem
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// Check image to External storage if not available Get from AdminPanel and store to device
	private void checkGetDoctorImage() {
		// TODO Auto-generated method stub
		if(mProfileImageName == null || mProfileImageName.equals("")){

        	mAdapter = new NavgnDrawer_Adapter(NavgnDrawer_Main_Doctor.this, navgndrwer_menu_titles, navgndrwer_menu_icons, mDoctrName, mDoctrEmail, mDoctrImage);
			mRecyclerView.setAdapter(mAdapter);
		}else{

			// Get Image from External Storage
			File file_slsmn_img = new File(STRNG_PATH_IMAGES_DOCTOR + FORWARD_SLASH + mProfileImageName +".jpg");
			if(file_slsmn_img.exists()){

				mDoctrImage = BitmapFactory.decodeFile(file_slsmn_img.getAbsolutePath());

				mAdapter = new NavgnDrawer_Adapter(NavgnDrawer_Main_Doctor.this, navgndrwer_menu_titles, navgndrwer_menu_icons, mDoctrName, mDoctrEmail, mDoctrImage);
				mRecyclerView.setAdapter(mAdapter);
			}else{

				if(General_Fnctns.isNetConnected(NavgnDrawer_Main_Doctor.this)){

					ServiceHandler srvc_hndlr = new ServiceHandler();
					String url_getimg = srvc_hndlr.getImagesFromUrl(DOCTOR_SMALL, mProfileImageName);

					// Get Doctor Profile image from AdminPanel
					new Get_Image_Websrvc().execute(url_getimg);
				}
			}
		}
	}

	// Get Doctor Profile image from AdminPanel
	public class Get_Image_Websrvc extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... urls) {
		    String urldisplay = urls[0];
		    Bitmap bitmap_websrvc = null;
		    try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        bitmap_websrvc = BitmapFactory.decodeStream(in);
		    } catch (Exception e) {
		        Log.e(TAG, e.getMessage());
		        e.printStackTrace();
		    }
		    return bitmap_websrvc;
		}

		protected void onPostExecute(Bitmap result) {

			if(result != null){

				mDoctrImage = result;
				mAdapter = new NavgnDrawer_Adapter(NavgnDrawer_Main_Doctor.this, navgndrwer_menu_titles, navgndrwer_menu_icons, mDoctrName, mDoctrEmail, mDoctrImage);
				mRecyclerView.setAdapter(mAdapter);

				// Store image on External Storage
				storeDoctorImageToDevice(result);
			}
		}
	}

	// Save Web-service getted Image on External Device
	private void storeDoctorImageToDevice(Bitmap bitmap_websrvc_result) {

	    File dctrProfileImgPath = PATH_IMAGES_DOCTOR;
	    dctrProfileImgPath.mkdirs();

	    String dctr_img_name = mProfileImageName +".jpg";
	    File file = new File (dctrProfileImgPath, dctr_img_name);
	    if (file.exists ()) file.delete ();

	    try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap_websrvc_result.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aalayam, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

        if(session !=null) {

            if(session.getIsHomeDctrNavdrwr()){

                super.onBackPressed();
            }else{

                displayFragmntView(1);

                // Set selected position to Shared preferences
                session.setSlctdPostnDctrNavdrwr(1);
                // Set selection on DashBoard
                mAdapter.notifyDataSetChanged();
            }
        }else{

            super.onBackPressed();
        }
	}
}
