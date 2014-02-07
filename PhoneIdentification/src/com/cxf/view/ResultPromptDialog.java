package com.cxf.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cxf.PhoneIdentification.R;

public class ResultPromptDialog extends DialogFragment implements
		OnClickListener {
	public static FragmentManager fm;
	private static Callback callback;
	public static final int OK = 411;
	public static final int CANCEL = -411;
	public static final int SHOWUP = 412;
	Dialog dialog;

	public static interface DialogClickListener {
		public void doPositiveClick();

		public void doNegativeClick();
	}

	public static ResultPromptDialog newInstance(String title, String message,
			FragmentManager fm, Callback cb) {
		ResultPromptDialog frag = new ResultPromptDialog();
		ResultPromptDialog.fm = fm;
		callback = cb;
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("message", message);
		frag.setArguments(b);

		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (dialog == null) {
			dialog = new Dialog(getActivity(), R.style.DialogStyle);

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.dialog_result_prompt, null,
					false);

			String title = getArguments().getString("title");
			String message = getArguments().getString("message");
			if (title != null && title.length() > 0) {
				TextView t = (TextView) view.findViewById(R.id.title_text_view);
				t.setText(title);
			}

			if (message != null && message.length() > 0) {
				TextView m = (TextView) view
						.findViewById(R.id.content_text_view);
				m.setText(message);
			}

			View ok = view.findViewById(R.id.dialog_ok);
			ok.setOnClickListener(this);
			dialog.setContentView(view);
		}

		return dialog;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok:
			if(callback!=null)
			{
			Message okMsg = new Message();
			okMsg.what = OK;
			callback.handleMessage(okMsg);
			}
			dialog.dismiss();
			break;
		case R.id.dialog_cancel:
			dialog.dismiss();
			break;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Message okMsg = new Message();
		okMsg.what = CANCEL;
		callback.handleMessage(okMsg);
	}

	public int show(FragmentTransaction transaction, String tag) {
		return show(transaction, tag, true);
	}

	public int show(FragmentTransaction transaction, String tag,
			boolean allowStateLoss) {
		transaction.add(this, tag);
		int mBackStackId = transaction.commitAllowingStateLoss();
		return mBackStackId;
	}

}
