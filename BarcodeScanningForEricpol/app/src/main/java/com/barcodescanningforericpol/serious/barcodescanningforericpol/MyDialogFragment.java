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
    final String LOG_TAG = "epolLogs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        String[] Info = MainActivity.myidb.getInfo(new String[]{MainActivity.tempSource});
        getDialog().setTitle(Info[1]);
        View v = inflater.inflate(R.layout.skip_save_dialog, null);
        v.findViewById(R.id.skip_button).setOnClickListener(this);
        v.findViewById(R.id.save_button).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "SkipSaveDialog: " + ((Button) v).getText());
        if(v.getId() == R.id.save_button) {
//            DateFormat fr = SimpleDateFormat.getDateTimeInstance();
//            DateFormat fr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            String date = fr.format(new Date());
            //TODO take from imported database type and value of scanned barcode
            String lastRow = MainActivity.mydb.specialGetLastRow()[0];
            if(lastRow != null){
                String[] Info = MainActivity.myidb.getInfo(new String[]{MainActivity.tempSource});
//                MainActivity.mydb.updateContent(Info[0], Info[1], MainActivity.tempSource, MainActivity.mydb.getFather(MainActivity.tempSource,Info[0]));
                MainActivity.mydb.updateContent(Info[0], Info[1], MainActivity.tempSource, MainActivity.mydb.getFather(Info[0]));
                Log.d(LOG_TAG, "Saved scanned content");
                dismiss();
            }
            else{
                String[] Info = MainActivity.myidb.getInfo(new String[]{MainActivity.tempSource});
                if (Info[0].equals("room")){
                    MainActivity.mydb.updateContent(Info[0], Info[1], MainActivity.tempSource, "empty");
                    Log.d(LOG_TAG, "Saved scanned content");
                    dismiss();
                }
                else {
                    //getActivity().getApplicationContext()
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "You should scan room first. Please, press the 'Skip' button and start again", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        if(v.getId() == R.id.skip_button){
            MainActivity.tempSource = null;
            Log.d(LOG_TAG, "Skipped. Nothing added");
            dismiss();
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "SkipSaveDialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "SkipSaveDialog: onCancel");
    }
}