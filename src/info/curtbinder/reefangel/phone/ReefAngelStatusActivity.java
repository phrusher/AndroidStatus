package info.curtbinder.reefangel.phone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ReefAngelStatusActivity extends Activity implements OnClickListener {
	private static final String TAG = "RAStatus";
		
	// Display views
	private View refreshButton;
	private TextView updateTime;
	//private TextView messageText;
	private TextView t1Text;
	private TextView t2Text;
	private TextView t3Text;
	private TextView phText;
	private TextView dpText;
	private TextView apText;
	private TextView salinityText;
	private TextView t1Label;
	private TextView t2Label;
	private TextView t3Label;
	private TextView dpLabel;
	private TextView apLabel;
	private TextView salinityLabel;
	
	// Threading
	private Handler guiThread;
	private ExecutorService statusThread;
	private Runnable updateTask;
	@SuppressWarnings("rawtypes")
	private Future statusPending;
	
	// View visibility
	//private boolean showMessageText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViews();
        initThreading();
               
        updateViewsVisibility();
        
        refreshButton.setOnClickListener(this);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();   
        guiUpdateDisplay();
	}

	private void findViews() {
		refreshButton = findViewById(R.id.refresh_button);
		updateTime = (TextView) findViewById(R.id.updated);
		t1Text = (TextView) findViewById(R.id.temp1);
		t2Text = (TextView) findViewById(R.id.temp2);
		t3Text = (TextView) findViewById(R.id.temp3);
		phText = (TextView) findViewById(R.id.ph);
		dpText = (TextView) findViewById(R.id.dp);
		apText = (TextView) findViewById(R.id.ap);
		salinityText = (TextView) findViewById(R.id.salinity);
		t1Label = (TextView) findViewById(R.id.t1_label);
		t2Label = (TextView) findViewById(R.id.t2_label);
		t3Label = (TextView) findViewById(R.id.t3_label);
		dpLabel = (TextView) findViewById(R.id.dp_label);
		apLabel = (TextView) findViewById(R.id.ap_label);
		salinityLabel = (TextView) findViewById(R.id.salinity_label);
	}
	
	private void updateViewsVisibility() {
		// updates all the views visibility based on user settings
		// get values from Preferences
        //showMessageText = false;
        
        // Labels
        t1Label.setText(Prefs.getT1Label(getBaseContext()));
        t2Label.setText(Prefs.getT2Label(getBaseContext()));
        t3Label.setText(Prefs.getT3Label(getBaseContext()));
        dpLabel.setText(Prefs.getDPLabel(getBaseContext()));
        apLabel.setText(Prefs.getAPLabel(getBaseContext()));
        
        // Visibility
		if ( Prefs.getT2Visibility(getBaseContext()) ) {
			Log.d(TAG, "T2 visible");
			t2Text.setVisibility(View.VISIBLE);
			t2Label.setVisibility(View.VISIBLE);
		} else {
			Log.d(TAG, "T2 gone");
			t2Text.setVisibility(View.GONE);
			t2Label.setVisibility(View.GONE);
		}
		if ( Prefs.getT3Visibility(getBaseContext()) ) {
			Log.d(TAG, "T3 visible");
			t3Text.setVisibility(View.VISIBLE);
			t3Label.setVisibility(View.VISIBLE);
		} else {
			Log.d(TAG, "T3 gone");
			t3Text.setVisibility(View.GONE);
			t3Label.setVisibility(View.GONE);
		}
		if ( Prefs.getDPVisibility(getBaseContext()) ) {
			Log.d(TAG, "DP visible");
			dpText.setVisibility(View.VISIBLE);
			dpLabel.setVisibility(View.VISIBLE);
		} else {
			Log.d(TAG, "DP gone");
			dpText.setVisibility(View.GONE);
			dpLabel.setVisibility(View.GONE);
		}
		if ( Prefs.getAPVisibility(getBaseContext()) ) {
			Log.d(TAG, "AP visible");
			apText.setVisibility(View.VISIBLE);
			apLabel.setVisibility(View.VISIBLE);
		} else {
			Log.d(TAG, "AP gone");
			apText.setVisibility(View.GONE);
			apLabel.setVisibility(View.GONE);
		}
		if ( Prefs.getSalinityVisibility(getBaseContext()) ) {
			Log.d(TAG, "Salinity visible");
			salinityText.setVisibility(View.VISIBLE);
			salinityLabel.setVisibility(View.VISIBLE);
		} else {
			Log.d(TAG, "Salinity gone");
			salinityText.setVisibility(View.GONE);
			salinityLabel.setVisibility(View.GONE);
		}
		//if ( ! showMessageText )
		//	messageText.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
    	switch ( v.getId() ){
    	case R.id.refresh_button:
    		// launch the update
    		Log.d(TAG, "onClick Refresh button");
    		launchStatusTask();
    		break;
    	}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch ( keyCode ) {
		case KeyEvent.KEYCODE_R:
			// launch the update
			Log.d(TAG, "onKeyDown R");
			launchStatusTask();
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	
	private void initThreading() {
		guiThread = new Handler();
		statusThread = Executors.newSingleThreadExecutor();
		updateTask = new Runnable() {
			public void run() {
				// Task to be run
				
				// Cancel any previous status check if exists
				if ( statusPending != null )
					statusPending.cancel(true);

				try {
					// Get IP & Port
					String[] devicesArray = getBaseContext().getResources().getStringArray(R.array.devicesValues);
					String device = Prefs.getDevice(getBaseContext());
					Host h = new Host();
					if ( device.equals(devicesArray[0]) ) {
						// controller
						h.setHost(Prefs.getHost(getBaseContext()));
						h.setPort(Prefs.getPort(getBaseContext()));
						h.setCommand(Globals.requestStatusOld);
					} else {
						// reeefangel.com
						h.setUserId(Prefs.getUserId(getBaseContext()));
						h.setCommand(Globals.requestReefAngel);
					}
					Log.d(TAG, h.toString());
					// Create ControllerTask
					ControllerTask cTask = new ControllerTask(
							ReefAngelStatusActivity.this,
							h,
							true);
					statusPending = statusThread.submit(cTask);
					// Add ControllerTask to statusThread to be run
				} catch ( RejectedExecutionException e) {
					Log.e(TAG, "initThreading RejectedExecution");
					updateTime.setText(R.string.messageError);
				}
			}
		};
	}
	
	private void launchStatusTask() {
		/**
		 * Creates the thread that communicates with the controller
		 * Then that function calls updateDisplay when it finishes
		 */
		Log.d(TAG, "launchStatusTask");
		// cancel any previous update if it hasn't started yet
		guiThread.removeCallbacks(updateTask);
		// start an update
		guiThread.post(updateTask);
	}
	
	private RADbAdapter openDatabase() {
		// open the database
		Log.d(TAG, "Open database");
		RADbAdapter dbAdapter = new RADbAdapter(this);
		dbAdapter.open();
        return dbAdapter;
	}
	
	private void closeDatabase(RADbAdapter db) {
		// close the database
		Log.d(TAG, "Close database");
		db.close();
	}
	
	public void guiUpdateDisplay() {
		/**
		 * Updates the display with the values from the Controller
		 * 
		 * Called from other threads
		 */
		guiThread.post(new Runnable() {
			public void run() {
				Log.d(TAG, "updateDisplay");
				try {
					RADbAdapter dbAdapter = openDatabase();
					Cursor c = dbAdapter.getLatestParams();
					String [] values;
					
					if ( c.moveToFirst() ) {
						/*
						Log.d(TAG, "Cursor Column Count: " + c.getColumnCount());
						StringBuilder builder = new StringBuilder();
						for ( int i = 1; i < c.getColumnCount(); i++ ) {
							builder.append(c.getColumnName(i)).append(": ");
							builder.append(c.getString(i)).append(", ");
						}
						Log.d(TAG, "Columns: " + builder);
						*/
						values = new String [] {
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_LOGDATE)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_T1)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_T2)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_T3)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_PH)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_DP)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_AP)),
							c.getString(c.getColumnIndex(RADbAdapter.PCOL_SAL))
						};
					} else {
						values = getNeverValues();
					}
					c.close();
					closeDatabase(dbAdapter);
					loadDisplayedControllerValues(values);
				} catch ( SQLException e ) {
					Log.d(TAG, "SQLException: " + e.getMessage());
				} catch ( CursorIndexOutOfBoundsException e ) {
					Log.d(TAG, "CursorIndex out of bounds: " + e.getMessage());
				}
			}
		});
	}
	
	public void guiAddParamsEntry(final Controller ra) {
		/** 
		 * Adds parameters entry to database
		 * 
		 * Called from other threads
		 */
		guiThread.post(new Runnable() {
			public void run() {
				Log.d(TAG, "addParamsEntry");
				RADbAdapter dbAdapter = openDatabase();
				dbAdapter.addParamsEntry(ra);
				closeDatabase(dbAdapter);
			}
		});
	}
	
	
	public void guiUpdateTimeText(final String msg) {
		/**
		 * Updates the UpdatedTime text box only
		 * 
		 * Called from other threads to indicate an error or interruption
		 */
		guiThread.post(new Runnable() {
			public void run() {
				Log.d(TAG, "updateTimeText");
				updateTime.setText(msg);
			}
		});
	}
	
	
	private void loadDisplayedControllerValues(String[] v) {
		// The order must match with the order in getDisplayedControllerValues
		updateTime.setText(v[0]);
		t1Text.setText(v[1]);
		t2Text.setText(v[2]);
		t3Text.setText(v[3]);
		phText.setText(v[4]);
		dpText.setText(v[5]);
		apText.setText(v[6]);
		salinityText.setText(v[7]);
	}
	
	private String[] getNeverValues() {
		return new String[] {
        		getString(R.string.messageNever),
        		getString(R.string.defaultStatusText),
        		getString(R.string.defaultStatusText),
        		getString(R.string.defaultStatusText),
        		getString(R.string.defaultStatusText),
        		getString(R.string.defaultStatusText),
        		getString(R.string.defaultStatusText),
        		getString(R.string.defaultStatusText)
        	};
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.settings:
        	// launch settings
        	Log.d(TAG, "Menu Settings clicked");
        	startActivity(new Intent(this, Prefs.class));
            break;
        case R.id.about:
        	// launch about box
        	Log.d(TAG, "Menu About clicked");
        	startActivity(new Intent(this, About.class));
            break;
        case R.id.params:
        	Log.d(TAG, "Menu Parameters clicked");
        	startActivity(new Intent(this, ParamsListActivity.class));
        	break;
        //case R.id.memory:
        	// launch memory
        //	break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}