package com.sweetmilkcake.beauty.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sweetmilkcake.beauty.R;

public class NavDialogHelper {
    private final static String TAG = "NavDialogHelper";
    private AppCompatActivity mActivity;

    public static void showSimpleDialog(AppCompatActivity activity, String title, String message, int type) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("dialog_simple");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putInt("type", type);

        SimpleDialogFragment simpleDialogFragment = new SimpleDialogFragment();
        simpleDialogFragment.setArguments(bundle);
        simpleDialogFragment.show(fragmentTransaction, "dialog_simple");
//        Log.d(TAG, "simpleDialogFragment");
    }


    public static class SimpleDialogFragment extends DialogFragment {
        String[] messages;
        int mType = 0;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            Bundle bundle = getArguments();
            String title = "";
            String message = "";
            if (bundle != null) {
                title = bundle.getString("title", "");
                message = bundle.getString("message", "");
                mType = bundle.getInt("type", 0);
            }
            messages = new String[]{ message };
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.visit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            if (mType == 1) {
                                composeEmail(getActivity(), messages, "");
                            } else if (mType == 2) {
                                openWebPage(getActivity(), messages[0]);
                            } else {

                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        private void composeEmail(FragmentActivity activity, String[] addresses, String subject) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        }

        private void openWebPage(FragmentActivity activity, String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        }
    }

    public static void showAboutDialog(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("dialog_about");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.addToBackStack(null);

        new AboutDialogFragment().show(fragmentTransaction, "dialog_about");
//        Log.d(TAG, "simpleDialogFragment");
    }

    public static class AboutDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout aboutBodyView = (LinearLayout) layoutInflater.inflate(R.layout.dialog_about, null);

            TextView appversion = (TextView) aboutBodyView.findViewById(R.id.app_version_name);
            TextView dismiss = (TextView) aboutBodyView.findViewById(R.id.dismiss_dialog);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                String version = pInfo.versionName;
                int versionCode = pInfo.versionCode;
                appversion.setText(getString(R.string.app_name) + " " + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return new AlertDialog.Builder(getActivity())
                    .setView(aboutBodyView)
                    .create();
        }
    }
}
