package com.airaroundme.airaroundme.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.airaroundme.airaroundme.MainActivity;
import com.airaroundme.airaroundme.interfaces.AsyncResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by borax12 on 10/05/15.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {

    private String TAG="HttpServiceSensor";
    public AsyncResponse delegate = null;

    public HttpAsyncTask(AsyncResponse response){
        delegate=response;
    }
    @Override
    protected String doInBackground(String... url) {

        return getJSON(url[0]);
    }
    // onPostExecute displays the results of the AsyncTask.


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
    }

    public String getJSON(String address){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failedet JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }

}
