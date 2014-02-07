package com.cxf.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

public class MyAutoCompleteTextView extends AutoCompleteTextView {
	private int myThreshold;


	public MyAutoCompleteTextView(Context context) {
		super(context);
	}

	public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean enoughToFilter() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		
		if (focused) {  
            performFiltering(getText(), 0);  
            showDropDown();  
        }  
	}

	 public void setThreshold(int threshold) {  
	        if (threshold < 0) {  
	            threshold = 0;  
	        }  
	        myThreshold = threshold;  
	    }  
	  
	    public int getThreshold() {  
	        return myThreshold;  
	    }  
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
		return super.onKeyDown(keyCode, event);
	}
	
}