/*
 * Copyright (c) 2011-13 by Curt Binder (http://curtbinder.info)
 * 
 * This work is made available under the terms of the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * http://creativecommons.org/licenses/by-nc-sa/3.0/
 */

package info.curtbinder.reefangel.phone.pages;

import info.curtbinder.reefangel.controller.Controller;
import info.curtbinder.reefangel.phone.Globals;
import info.curtbinder.reefangel.phone.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TableRow;
import android.widget.TextView;

public class AIPage extends RAPage
	implements OnLongClickListener {
	// private static final String TAG = AIWidget.class.getSimpleName();

	Context ctx; // saved context from parent
	private TextView[] aiText = new TextView[Controller.MAX_AI_CHANNELS];
	private short[] aiValues = new short[Controller.MAX_AI_CHANNELS];

	public AIPage ( Context context ) {
		super( context );
		ctx = context;
		addViewsFromLayout( context );
	}

	public AIPage ( Context context, AttributeSet attrs ) {
		super( context, attrs );
		ctx = context;
		addViewsFromLayout( context );
	}

	private void addViewsFromLayout ( Context context ) {
		LayoutInflater layoutInflater =
				(LayoutInflater) context
						.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		layoutInflater.inflate( R.layout.ai, this );
		findViews();
	}

	private void findViews ( ) {
		TableRow tr;
		TextView tv;
		tr = (TableRow) findViewById( R.id.rowAIWhite );
		aiText[0] = (TextView) tr.findViewById( R.id.rowValue );
		tv = (TextView) tr.findViewById( R.id.rowTitle );
		tv.setText( R.string.labelWhite );
		tr = (TableRow) findViewById( R.id.rowAIBlue );
		aiText[1] = (TextView) tr.findViewById( R.id.rowValue );
		aiText[1].setTextColor( ctx.getResources().getColor( R.color.blue ) );
		tv = (TextView) tr.findViewById( R.id.rowTitle );
		tv.setTextColor( ctx.getResources().getColor( R.color.blue ) );
		tv.setText( R.string.labelBlue );
		tr = (TableRow) findViewById( R.id.rowAIRoyalBlue );
		aiText[2] = (TextView) tr.findViewById( R.id.rowValue );
		aiText[2]
				.setTextColor( ctx.getResources().getColor( R.color.royalblue ) );
		tv = (TextView) tr.findViewById( R.id.rowTitle );
		tv.setTextColor( ctx.getResources().getColor( R.color.royalblue ) );
		tv.setText( R.string.labelRoyalBlue );
		
		for ( int i = 0; i < Controller.MAX_AI_CHANNELS; i++ ) {
			aiText[i].setLongClickable( true );
			aiText[i].setOnLongClickListener( this );
		}
	}

	public void setLabel ( int channel, String label ) {
		TableRow tr = (TableRow) aiText[channel].getParent();
		((TextView) tr.findViewById( R.id.rowTitle )).setText( label );
	}

	public void updateDisplay ( String[] v ) {
		for ( int i = 0; i < Controller.MAX_AI_CHANNELS; i++ ) {
			aiText[i].setText( v[i] );
		}
	}
	
	public void updatePWMValues ( short[] v ) {
		for ( int i = 0; i < Controller.MAX_AI_CHANNELS; i++ ) {
			aiValues[i] = v[i];
		}
	}

	@Override
	public String getPageTitle ( ) {
		return ctx.getString( R.string.labelAI );
	}

	@Override
	public boolean onLongClick ( View v ) {
		View parent = (View) v.getParent();
		switch ( parent.getId() ) {
			default:
				return false;
			case R.id.rowAIWhite:
				displayOverridePopup(Globals.OVERRIDE_AI_WHITE, aiValues[0]);
				break;
			case R.id.rowAIBlue:
				displayOverridePopup(Globals.OVERRIDE_AI_BLUE, aiValues[1]);
				break;
			case R.id.rowAIRoyalBlue:
				displayOverridePopup(Globals.OVERRIDE_AI_ROYALBLUE, aiValues[2]);
				break;
		}
		return true;
	}
}
