package info.curtbinder.reefangel.phone;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Prefs extends PreferenceActivity implements OnPreferenceChangeListener {

	private static final String TAG = "RAPrefs";
	private static final String NUMBER_PATTERN = "\\d+";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		Preference portkey = getPreferenceScreen().findPreference(getBaseContext().getString(R.string.prefPortKey));
		portkey.setOnPreferenceChangeListener(this);
	}
	
	public static String getHost(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefHostKey), 
				context.getString(R.string.prefHostDefault));
	}
	
	public static String getPort(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefPortKey), 
				context.getString(R.string.prefPortDefault));
	}
	
	public static boolean getT2Visibility(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				context.getString(R.string.prefT2VisibilityKey), 
				true);
	}
	
	public static boolean getT3Visibility(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				context.getString(R.string.prefT3VisibilityKey), 
				true);
	}
	
	public static boolean getDPVisibility(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				context.getString(R.string.prefDPVisibilityKey),
				true);
	}
	
	public static boolean getAPVisibility(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				context.getString(R.string.prefAPVisibilityKey),
				true);
	}
	
	public static boolean getSalinityVisibility(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
				context.getString(R.string.prefSalinityVisibilityKey),
				false);
	}
	
	public static String getT1Label(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefT1LabelKey), 
				context.getString(R.string.temp1_label));
	}
	
	public static String getT2Label(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefT2LabelKey), 
				context.getString(R.string.temp2_label));
	}
	
	public static String getT3Label(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefT3LabelKey), 
				context.getString(R.string.temp3_label));
	}
	
	public static String getDPLabel(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefDPLabelKey), 
				context.getString(R.string.dp_label));
	}
	
	public static String getAPLabel(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				context.getString(R.string.prefAPLabelKey), 
				context.getString(R.string.ap_label));
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// return true to change, false to not
		if ( preference.getKey().equals(preference.getContext().getString(R.string.prefPortKey))) {
			Log.d(TAG, "Validate entered port");
			if ( ! isNumber(newValue) ) {
				// not a number
				Toast.makeText(preference.getContext(), getResources().getString(R.string.messageNotNumber) + ": " + newValue.toString(), Toast.LENGTH_SHORT).show();				
				return false;
			} else {
				// it's a number, verify it's within range
				int min = Integer.parseInt((String)preference.getContext().getString(R.string.prefPortMin));
				int max = Integer.parseInt((String)preference.getContext().getString(R.string.prefPortMax));
				int v = Integer.parseInt((String)newValue.toString());
				
				// check if it's less than the min value or if it's greater than the max value
				if ( (v < min) || (v > max) ) {
					Toast.makeText(preference.getContext(), getResources().getString(R.string.prefPortInvalidPort) + ": " + newValue.toString(), Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		if ( preference.getKey().equals(preference.getContext().getString(R.string.prefHostKey))) {
			// host validation here
			Log.d(TAG, "Validate entered host");
		}
		return true;
	}
	
	private boolean isNumber(Object value) {
		if ( (! value.toString().equals("")) &&
			 ( value.toString().matches(NUMBER_PATTERN)) )
		{
			// we have a number
			return true;
		}
		return false;
	}
}
