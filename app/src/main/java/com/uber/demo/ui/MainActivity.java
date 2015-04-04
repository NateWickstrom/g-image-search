package com.uber.demo.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.uber.demo.App;
import com.uber.demo.Config;
import com.uber.demo.R;
import com.uber.demo.io.ImageSearchRequest;
import com.uber.demo.model.GoogleImage;
import com.android.volley.Request;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements
        Response.Listener<List<GoogleImage>>, Response.ErrorListener,
        TextView.OnEditorActionListener, AbsListView.OnScrollListener {

    private List<GoogleImage> mImageList;
    private LayoutInflater mInflater;
    private MyAdapter mAdapter;
    private EditText mEditText;

    private Request mInflightRequest;

    private String tempQuery = "cars";
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageList = new ArrayList<>();
        mAdapter = new MyAdapter();
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        GridView grid = (GridView) findViewById(R.id.gridView);
        grid.setAdapter(mAdapter);
        grid.setOnScrollListener(this);


        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setOnEditorActionListener(this);

        search(tempQuery);
    }

    private String buildUrl(String query, int page){
        try {
            return Config.SEARCH_URL + "&q=" + URLEncoder.encode(query, "UTF-8") + "&start=" + page;
        } catch (Exception e) {}
        return null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_ENTER ) {
            Toast.makeText(this, "Query", Toast.LENGTH_SHORT).show();
            //return mFragment.onKeyDown(keyCode, event);
        }
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        mInflightRequest = null;
    }

    @Override
    public void onResponse(List<GoogleImage> googleImages) {
        mInflightRequest = null;
        mImageList.addAll(googleImages);
        mAdapter.notifyDataSetChanged();
    }

    private void search(String query){
        if (mInflightRequest != null)
            mInflightRequest.cancel();
        mInflightRequest = null;
        mImageList.clear();
        mAdapter.notifyDataSetChanged();

        makeRequest(query, 0);
    }

    private void makeRequest(String query, int page){
        if (mInflightRequest != null)
            return;

        mCurrentPage = page;
        mInflightRequest = App.getQueue().add(new ImageSearchRequest(buildUrl(query, page), this, this));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mInflightRequest != null)
            return;

        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            makeRequest(tempQuery, mCurrentPage + 1);
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public GoogleImage getItem(int position) {
            return mImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            GoogleImage googleImage = getItem(position);
            ImageView imageView = (ImageView) mInflater.inflate(R.layout.grid_item, parent, false);
            final String imageUrl = googleImage.getUrl();

            // Set the Thumbnail
            Picasso.with(parent.getContext())
                    .load(imageUrl)
                    .error(R.drawable.ic_no_image_available)
                    .into(imageView);

            return imageView;
        }
    }
}
