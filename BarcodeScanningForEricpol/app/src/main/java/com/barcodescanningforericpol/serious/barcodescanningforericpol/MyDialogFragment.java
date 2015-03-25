package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MyDialogFragment extends DialogFragment implements OnClickListener{
    public static final String LOG_TAG = MyDialogFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        String[] Info = MainFragment.myidb.getInfo(new String[]{MainFragment.tempSource});
        getDialog().setTitle(Info[1]);
        View v = inflater.inflate(R.layout.skip_save_dialog, null);
        v.findViewById(R.id.skip_button).setOnClickListener(this);
        v.findViewById(R.id.save_button).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        Log.d(MyDialogFragment.LOG_TAG, "SkipSaveDialog: " + ((Button) v).getText());
        if(v.getId() == R.id.save_button) {
            //TODO take from imported database type and value of scanned barcode
            String lastRow = MainFragment.mydb.specialGetLastRow()[0];
            if (MainFragment.myidb.haveInIdb(MainFragment.tempSource)) {
                if (lastRow != null) {
                    String[] Info = MainFragment.myidb.getInfo(new String[]{MainFragment.tempSource});
                    MainFragment.mydb.updateContent(Info[0], Info[1], MainFragment.tempSource, MainFragment.mydb.getFather(Info[0]));
                    MainFragment.contentTxt.setText(Info[1] + '\n' + MainFragment.tempSource);
                    Log.d(MyDialogFragment.LOG_TAG, "Saved scanned content");
                    dismiss();
                } else {
                    String[] Info = MainFragment.myidb.getInfo(new String[]{MainFragment.tempSource});
                    if (Info[0].equals("room")) {
                        MainFragment.mydb.updateContent(Info[0], Info[1], MainFragment.tempSource, "empty");
                        MainFragment.contentTxt.setText(Info[1] + '\n' + MainFragment.tempSource);
                        Log.d(MyDialogFragment.LOG_TAG, "Saved scanned content");
                        dismiss();
                    } else {
                        //getActivity().getApplicationContext()
                        MainFragment.formatTxt.setText("Maybe you are not imported database with all existing barcodes? ");
                        MainFragment.contentTxt.setText("Scan room first");
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "You should scan room first."+'\n'+
                                        "Please, press the 'Skip' button and start again", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
            else {
                MainFragment.formatTxt.setText("Press 'Update imported DB'" + '\n' +
                        "This will help to fill database with all existing barcodes");
                MainFragment.contentTxt.setText("Last scanned barcode:"+'\n' + MainFragment.mydb.getLastRow());
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "You should scan barcodes that you have in imported database file." + '\n' +
                                "Please, update your imported database file", Toast.LENGTH_LONG);
                toast.show();
                dismiss();
            }
        }
        if(v.getId() == R.id.skip_button){
            MainFragment.tempSource = null;
            MainFragment.contentTxt.setText("Skipped. Nothing added");
            Log.d(MyDialogFragment.LOG_TAG, "Skipped. Nothing added");
            dismiss();
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(MyDialogFragment.LOG_TAG, "SkipSaveDialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(MyDialogFragment.LOG_TAG, "SkipSaveDialog: onCancel");
    }
}