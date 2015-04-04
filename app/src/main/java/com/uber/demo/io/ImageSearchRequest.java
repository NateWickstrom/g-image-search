package com.uber.demo.io;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.uber.demo.model.GoogleImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImageSearchRequest extends Request<List<GoogleImage>> {

    /** tag for logging **/
    private static final String TAG = ImageSearchRequest.class.getSimpleName();
    /** listener for successful responses **/
    private final Response.Listener<List<GoogleImage>> mListener;

    /** Creates new request object
     * @param listener listener for successful responses
     * @param errorListener error listener in case an error happened
     */
    public ImageSearchRequest(String url, Response.Listener<List<GoogleImage>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected void deliverResponse(List<GoogleImage> response) {
        // notify observer object, thereby notifying front-end UI
        mListener.onResponse(response);
    }

    /**
     * Volley Request method override to catch raw network Response
     *
     * @param response the raw network response
     * @return Response list
     */
    @Override
    protected final Response<List<GoogleImage>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            List<GoogleImage> data = parseResponse(json);
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonParseException e) {
            Log.e(TAG, "Json Parse Exception", e);
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception", e);
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unsupported Encoding Exception", e);
            return Response.error(new ParseError(e));
        }
    }

    private List<GoogleImage> parseResponse(String response) throws JsonParseException, JSONException {

        ArrayList<GoogleImage> images = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonResponse = jsonObject.getJSONObject("responseData");
        JSONArray jsonArray = jsonResponse.getJSONArray("results");
        Gson gson = new Gson();

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            images.add(gson.fromJson(json.toString(), GoogleImage.class));
        }

        return images;
    }
}
