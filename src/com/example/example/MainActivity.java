package com.example.example;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import teech.sdk.Assignments;
import teech.sdk.Submission;
import teech.sdk.Teech;
import teech.sdk.TeechQuery;
import teech.sdk.exceptions.APIConnectionException;
import teech.sdk.exceptions.InvalidRequestException;
import teech.sdk.exceptions.TeechAuthenticationException;
import teech.sdk.exceptions.TeechException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;


public class MainActivity extends Activity {
	
	ProgressDialog dialog;
	String title = "";
	String description = "";
	String idMaterial = "";
	String[] question = new String[3];
	Double[] questionValue = new Double[3];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		Teech.init("XXXXXXXXXXapi-keyXXXXXXXXXXXXXXXXXXXXc", "xxxxxxxxxxxxxxAPP-IDxxxxxxxxxxxxxxxxxxx"); // insert your apiKey and appKey and we are ready!!! :D
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED){
			new Mat().execute();
		}else{
			Toast.makeText(getApplicationContext(), "There is not a internet connection", Toast.LENGTH_SHORT).show();//not connection
		}
		
	}
	
	public class Mat extends AsyncTask<String, Integer, String>{
		
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setTitle("Check acces data in progress");
			dialog.setMessage("Wait a few seconds...");
			dialog.show();
		}
		

		protected String doInBackground(String... urls){
			TeechQuery q = new TeechQuery("materials");
			JSONArray result = null;
			try {
				JSONObject constraints = new JSONObject();
				constraints.put("title", "UK capital city"); //query in TeechMaterial with "title"="UK capital city"
				result = q.search(constraints).get();
			} catch (InvalidRequestException e) {
				e.printStackTrace();
			} catch (TeechAuthenticationException e) {
				System.out.println("You must insert a valid API-KEY and a valid APP-ID");
				e.printStackTrace();
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (TeechException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if(result.length()==0){
				System.out.println("You need to insert a TeechioMaterial, go to your dashboard at www.teech.io");
			}
			
			return result.toString();
		}
	
		protected void onPostExecute(String result){
		
			try {
				JSONArray jsonA = new JSONArray(result);
				for(int i =0; i<jsonA.length();i++){
					JSONObject material = jsonA.getJSONObject(i);
					description = material.getString("description");
					idMaterial = material.getString("_id");
					System.out.println(idMaterial);
					JSONObject choices = material.getJSONObject("choices");
					Iterator<String> keys = choices.keys();
					int z= 0;
					while(keys.hasNext()){
						String Key = keys.next();
						Double value = choices.getDouble(Key);
						question[z]= Key;
						questionValue[z]= value;
						z++;
					}
				}	
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
			Button btn1 = (Button) findViewById(R.id.button1);
			btn1.setText(question[0]);
		
			Button btn2 = (Button) findViewById(R.id.button2);
			btn2.setText(question[1]);
		
			Button btn3 = (Button) findViewById(R.id.button3);
			btn3.setText(question[2]);
		
			TextView desc = (TextView) findViewById(R.id.textView2);
			desc.setText(description);
		
			dialog.dismiss();
		
			btn1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
				
					ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
					if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED){
						new b1().execute();
					}else{
						Toast.makeText(getApplicationContext(), "There is not a internet connection", Toast.LENGTH_SHORT).show();//not connection
					}
				}
			});	
			
			btn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
				
					ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
					if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED){
						new b2().execute();
					}else{
						Toast.makeText(getApplicationContext(), "There is not a internet connection", Toast.LENGTH_SHORT).show();//not connection
					}
				}
			});	
			
			btn3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
				
					ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
					if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED){
						new b3().execute();
					}else{
						Toast.makeText(getApplicationContext(), "There is not a internet connection", Toast.LENGTH_SHORT).show();//not connection
					}
				}
			});	
		
		
		}
		
	
	}
	
	public class b1 extends AsyncTask<String, Integer, String>{
		
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setTitle("Check acces data in progress");
			dialog.setMessage("Wait a few seconds...");
			dialog.show();
		}
		

		protected String doInBackground(String... urls){
			String idAssignment = null;
			try {
				Assignments a = new Assignments();
				a.put("title", description);
				a.put("material", idMaterial);
				a.save();
				idAssignment = a.get("_id").toString();
			} catch (InvalidRequestException e) {
				e.printStackTrace();
			} catch (TeechAuthenticationException e) {
				e.printStackTrace();
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (TeechException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Submission s = new Submission();
				s.put("assignment",idAssignment );
				s.put("user","538e2623e4b0aae566c807ba"); //insert a valid id user
				s.put("body",question[0]);
				s.save();
			} catch (InvalidRequestException e) {
				e.printStackTrace();
			} catch (TeechAuthenticationException e) {
				e.printStackTrace();
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (TeechException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return "";
		}
	
		protected void onPostExecute(String result){
			dialog.dismiss();
		}

	}
	
public class b2 extends AsyncTask<String, Integer, String>{
		
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setTitle("Check acces data in progress");
			dialog.setMessage("Wait a few seconds...");
			dialog.show();
		}
		

		protected String doInBackground(String... urls){
			String idAssignment = null;
			try {
				Assignments a = new Assignments();
				a.put("title", description);
				a.put("material", idMaterial);
				a.save();
				idAssignment = a.get("_id").toString();
			} catch (InvalidRequestException e) {
				e.printStackTrace();
			} catch (TeechAuthenticationException e) {
				e.printStackTrace();
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (TeechException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Submission s = new Submission();
				s.put("assignment",idAssignment );
				s.put("user","538e2623e4b0aae566c807ba"); //insert a valid id user
				s.put("body",question[1]);
				s.save();
			} catch (InvalidRequestException e) {
				e.printStackTrace();
			} catch (TeechAuthenticationException e) {
				e.printStackTrace();
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (TeechException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return "";
		}
	
		protected void onPostExecute(String result){
			dialog.dismiss();
		}

	}

public class b3 extends AsyncTask<String, Integer, String>{
	
	protected void onPreExecute() {
		dialog = new ProgressDialog(MainActivity.this);
		dialog.setTitle("Check acces data in progress");
		dialog.setMessage("Wait a few seconds...");
		dialog.show();
	}
	

	protected String doInBackground(String... urls){
		String idAssignment = null;
		try {
			Assignments a = new Assignments();
			a.put("title", description);
			a.put("material", idMaterial);
			a.save();
			idAssignment = a.get("_id").toString();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (TeechAuthenticationException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (TeechException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Submission s = new Submission();
			s.put("assignment",idAssignment );
			s.put("user","538e2623e4b0aae566c807ba"); //insert a valid id user
			s.put("body",question[2]);
			s.save();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (TeechAuthenticationException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (TeechException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}

	protected void onPostExecute(String result){
		dialog.dismiss();
	}

}
}
