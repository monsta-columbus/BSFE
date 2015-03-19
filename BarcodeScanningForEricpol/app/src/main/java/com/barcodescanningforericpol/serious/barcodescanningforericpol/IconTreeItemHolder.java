package com.barcodescanningforericpol.serious.barcodescanningforericpol;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;
//import com.unnamed.b.atv.model.TreeNode;

//import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by emergency on 3/5/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    public static final String LOG_TAG = IconTreeItemHolder.class.getSimpleName();
    private TextView tvValue;
    private PrintView arrowView;
    private View view;
    static  String[] clickedParentInfo = new String[]{null,null,null,null};
    static int nodeLevel;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_icon_node, null, false);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.publicText +'\n' + value.text);

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);

        view.findViewById(R.id.btn_addFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final TreeNode node, IconTreeItem value
                if (node.getLevel() == 1) {
                    nodeLevel = 1;
                    clickedParentInfo[0] = ((IconTreeItem)node.getValue()).text;
                    clickedParentInfo[1] = ((IconTreeItem)node.getValue()).publicText;
                    MyTreeList.scanIntegrator.initiateScan();
                    Log.d(IconTreeItemHolder.LOG_TAG, clickedParentInfo[0] + " " + clickedParentInfo[1]);
                    Log.d(IconTreeItemHolder.LOG_TAG, "Scanning");
                }
                if (node.getLevel() == 2) {
                    nodeLevel = 2;
                    clickedParentInfo[0] = ((IconTreeItem)node.getValue()).text;
                    clickedParentInfo[1] = ((IconTreeItem)node.getValue()).publicText;
                    clickedParentInfo[2] = ((IconTreeItem)node.getParent().getValue()).text;
                    clickedParentInfo[3] = ((IconTreeItem)node.getParent().getValue()).publicText;
                    MyTreeList.scanIntegrator.initiateScan();
                    Log.d(IconTreeItemHolder.LOG_TAG, clickedParentInfo[0] + " " + clickedParentInfo[1]);
                    Log.d(IconTreeItemHolder.LOG_TAG, "Scanning");

                }
//                TreeNode newFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "New Folder", "NewFolder"));
//                getTreeView().addNode(node, newFolder);
            }
        });

        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (node.getLevel() == 1) {
                    Log.d(IconTreeItemHolder.LOG_TAG, "You don't have permission to delete rows at this level");



                }
                if (node.getLevel() == 2) {

                    if(MainActivity.mydb.isItem(((IconTreeItem)node.getValue()).text)){
                        Log.d(IconTreeItemHolder.LOG_TAG, "Deleting item from the tree at level two");
                        MainActivity.mydb.deleteExactItem(new String[]{((IconTreeItem) node.getValue()).text});
                        Log.d(IconTreeItemHolder.LOG_TAG, "One item deleted");
                        MainActivity.formatTxt.setText("Row deleted. Last scanned barcode:");
                        MainActivity.contentTxt.setText(MainActivity.mydb.getLastRow());


                        //todo toast
//                        Toast toast = Toast.makeText(MyTreeList.getActivity().getApplicationContext(),
//                                "Scan added. Reopen list to see changes in the tree-list", Toast.LENGTH_SHORT);
//                        toast.show();
                    }
                    else{
                        Log.d(IconTreeItemHolder.LOG_TAG, "Deleting person from the tree at level two");
                        MainActivity.mydb.deleteExactPersonAndChildren(new String[]{((IconTreeItem)node.getValue()).text});
                        MainActivity.formatTxt.setText("Person " + ((IconTreeItem)node.getValue()).publicText + " and his children deleted!. Last scanned barcode:");
                        MainActivity.contentTxt.setText(MainActivity.mydb.getLastRow());


                        //todo toast
                    }


                }
                if (node.getLevel() == 3) {
                    Log.d(IconTreeItemHolder.LOG_TAG, "Deleting item from the tree at level three");
                    Log.d(IconTreeItemHolder.LOG_TAG, ((IconTreeItem)node.getValue()).text);
                    MainActivity.mydb.deleteExactItem(new String[]{((IconTreeItem) node.getValue()).text});
                    Log.d(IconTreeItemHolder.LOG_TAG, "One item deleted");


                    MainActivity.formatTxt.setText("Row deleted. Last scanned barcode:");
                    MainActivity.contentTxt.setText(MainActivity.mydb.getLastRow());

                    //todo toast
                }

                getTreeView().removeNode(node);
            }
        });

        //if ROOM
        if (node.getLevel() == 1) {
            view.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        }
        //if donno type at lvl 2

        if (node.getLevel() == 2){
            if (MainActivity.mydb.isItem(value.text)){
                view.findViewById(R.id.arrow_icon).setVisibility(View.GONE);
                view.findViewById(R.id.btn_addFolder).setVisibility(View.GONE);
                view.findViewById(R.id.icon).setPadding(68,0,0,0);
            }
            else{
                view.findViewById(R.id.arrow_icon).setPadding(26,0,0,0);
            }
        }

        //if ITEM
        if (node.getLevel() == 3) {
            view.findViewById(R.id.arrow_icon).setVisibility(View.GONE);
            view.findViewById(R.id.btn_addFolder).setVisibility(View.GONE);
            view.findViewById(R.id.icon).setPadding(96,0,0,0);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));

//        view.setBackgroundColor(active ? Color.LTGRAY : Color.TRANSPARENT);
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
        public String publicText;

        public IconTreeItem(int icon, String text, String publicText) {
            this.icon = icon;
            this.text = text;
            this.publicText = publicText;
        }
    }
}