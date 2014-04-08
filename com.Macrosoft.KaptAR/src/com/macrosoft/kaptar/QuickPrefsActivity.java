package com.macrosoft.kaptar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class QuickPrefsActivity extends PreferenceActivity  {

	@Override
	public void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState);       
		addPreferencesFromResource(R.xml.preference);    
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, "Show current settings");
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case 0:
	                startActivity(new Intent(this, QuickPrefsActivity.class));
	                return true;
	        }
	        return false;
	    }

}
