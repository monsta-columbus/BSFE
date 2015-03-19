package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

public  class MyTreeList extends Fragment {
    public static final String LOG_TAG = MyTreeList.class.getSimpleName();
    private AndroidTreeView tView;
    private TextView statusBar;
    private TextView nodeValueBar;
    public static com.google.zxing.integration.android.IntentIntegrator scanIntegrator;
    public static String elasticValue1 = null;
    public static String elasticValue2 = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        scanIntegrator = new com.google.zxing.integration.android.IntentIntegrator(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        statusBar = (TextView) rootView.findViewById(R.id.status_bar);

        //rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);
        TreeNode root = TreeNode.root();

        ArrayList<String> allRooms = MainActivity.mydb.getAllRooms();

        for(int i=0; i<allRooms.size(); i++){
            root.addChild(new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_home,allRooms.get(i),MainActivity.mydb.getColumnDescriptionName(allRooms.get(i)))));

        }

        List<TreeNode> roomNode = root.getChildren();
        ArrayList<String> roomSettlers;
        String roomSettler;
        for(int i = 0;i<roomNode.size();i++){
            roomSettlers = MainActivity.mydb.getAllRoomSettlers(((IconTreeItemHolder.IconTreeItem)roomNode.get(i).getValue()).text);
            Log.d(MyTreeList.LOG_TAG, "room " + ((IconTreeItemHolder.IconTreeItem) roomNode.get(i).getValue()).text);
            for(int k=0; k<roomSettlers.size(); k++){
                Log.d(MyTreeList.LOG_TAG, "person " + roomSettlers.get(k));
                roomSettler = roomSettlers.get(k);
                if(MainActivity.mydb.isItem(roomSettler))
                    roomNode.get(i).addChild(new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string. ic_settings,roomSettler,MainActivity.mydb.getColumnDescriptionName(roomSettler))));
                else
                    roomNode.get(i).addChild(new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string. ic_work,roomSettler,MainActivity.mydb.getColumnDescriptionName(roomSettler))).addChildren(MainActivity.mydb.getChildrenOfSettlers(roomSettler)));
            }
        }

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultNodeClickListener(nodeClickListener);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                tView.expandAll();
                break;

            case R.id.collapseAll:
                tView.collapseAll();
                break;
        }
        return true;
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            statusBar.setText("Last clicked: " + item.text);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    public void onActivityResult (int requestCode, int resultCode, Intent intent) {
        com.google.zxing.integration.android.IntentResult scanningResult = com.google.zxing.integration.android.IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            MainActivity.formatTxt.setText("Currently scanned barcode:");
//            TODO check content for existing in imported db
            MainActivity.contentTxt.setText(scanContent);
            if (scanContent != null){
                MainActivity.tempSource = scanContent;
                String[] Info = MainActivity.myidb.getInfo(new String[]{MainActivity.tempSource});
                if (!Info[0].equals("room")){
                    if(IconTreeItemHolder.nodeLevel == 1){
                        MainActivity.mydb.updateContent("room", IconTreeItemHolder.clickedParentInfo[1], IconTreeItemHolder.clickedParentInfo[0], "empty");
                        MainActivity.mydb.updateContent(Info[0], Info[1], MainActivity.tempSource, IconTreeItemHolder.clickedParentInfo[0]);
                        Log.d(MyTreeList.LOG_TAG, "Saved scanned content");

                    }
                    if(IconTreeItemHolder.nodeLevel == 2){
                        MainActivity.mydb.updateContent("room", IconTreeItemHolder.clickedParentInfo[3], IconTreeItemHolder.clickedParentInfo[2], "empty");
                        MainActivity.mydb.updateContent("user", IconTreeItemHolder.clickedParentInfo[1], IconTreeItemHolder.clickedParentInfo[0], IconTreeItemHolder.clickedParentInfo[2]);
                        MainActivity.mydb.updateContent(Info[0], Info[1], MainActivity.tempSource, IconTreeItemHolder.clickedParentInfo[0]);
                        Log.d(MyTreeList.LOG_TAG, "Saved scanned content");
                        elasticValue1 = Info[1];
                        elasticValue2 = MainActivity.tempSource;

                    }
//                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
//                            "Scan added. Reopen list to see changes in the tree-list", Toast.LENGTH_SHORT);
//                    toast.show();
                }
//                IconTreeItemHolder.clicked_parent;
                Log.d(MyTreeList.LOG_TAG, "FORMAT and CONTENT received");
                Log.d(MyTreeList.LOG_TAG, "Starting to show dialog content");
            }
            else{
                elasticValue1 = "interrupt";
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}