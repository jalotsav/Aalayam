package com.jalotsav.aalayam.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import com.jalotsav.aalayam.common.AalayamConstants;

public class ServiceHandler implements AalayamConstants{
		
	// Generate URL for get Images
	public String getImagesFromUrl(String webFolderName, String imageName){
		
		String url = WEBSITE_NAME + IMAGES_SMALL + FORWARD_SLASH + webFolderName + FORWARD_SLASH + imageName;
//		http://aalayamrehab.com/hrmasteradmin/images/doctor/1434224075.jpg
			// For Doctor Profile Image --> http://aalayamrehab.com/hrmasteradmin/images/doctor/1434224075.jpg
			// For Patient Profile Image --> http://aalayamrehab.com/hrmasteradmin/images/patient/profile/1434224075.jpg
			// For Patient Case Image --> http://aalayamrehab.com/hrmasteradmin/images/patient/case/1434224075.jpg
			// For Patient Images --> http://aalayamrehab.com/hrmasteradmin/images/patient/images/1434224075.jpg
			// For Patient Videos --> http://aalayamrehab.com/hrmasteradmin/images/patient/videos/1-1424591549.mp4
		
		return url;
	}
	
	// Function for get data from WebService
	public String get_makeServiceCall(){
		
		HttpURLConnection httpurlconctn = null;
		String response = "";
		try {
			/*
			HttpClient httpclient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 100000);
			HttpEntity httpentity = null;
			HttpPost httppost = new HttpPost(websrvc_url);
			HttpResponse httpresponse = httpclient.execute(httppost);
			
			httpentity = httpresponse.getEntity();
			
			response = EntityUtils.toString(httpentity);
			*/
			
			URL url = new URL(WEBSERVICE_URL);
			httpurlconctn = (HttpURLConnection) url.openConnection();
			httpurlconctn.setRequestMethod("GET");
			int responseCode = httpurlconctn.getResponseCode();
			Log.i(AalayamConstants.TAG, "\nSending 'GET' request to URL : " + url);
			Log.i(AalayamConstants.TAG, "Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(httpurlconctn.getInputStream()));
			String inputLine;
			StringBuffer response_strbufr = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response_strbufr.append(inputLine);
			}
			in.close();
			
			response = response.toString();
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(AalayamConstants.TAG, "sh.get_makeServiceCall Catch Block: " +  e.getMessage());
		}finally {
			if(httpurlconctn != null)
				httpurlconctn.disconnect();
		}
		
		return response;
	}
	
	// Function for Post data(JSON) to WebService
	public String post_makeServiceCall(String jsonObj) {
		
//		HttpURLConnection httpurlconctn = null;
		BufferedReader inBuffer = null;
		String response = "";
		try {
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 100000);
			HttpPost request = new HttpPost(WEBSERVICE_URL);
            
            StringEntity se = new StringEntity(jsonObj.toString()); 
            request.addHeader("content-type", "application/x-www-form-urlencoded");	            
			
			request.setEntity(se);
			
			HttpResponse httpResponse = httpClient.execute(request);
			
			inBuffer = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String newLine = System.getProperty("line.separator");
			while ((line = inBuffer.readLine()) != null) {
				stringBuffer.append(line + newLine);
			}
			inBuffer.close();

			response = stringBuffer.toString();

/*
			DataOutputStream printout;
			DataInputStream  input;
			URL url = new URL(WEBSERVICE_URL);
			httpurlconctn = (HttpURLConnection) url.openConnection();
			httpurlconctn.setDoInput(true);
	        httpurlconctn.setDoOutput(true);
	        httpurlconctn.setRequestMethod("POST");
	        httpurlconctn.setUseCaches(false);  
	        httpurlconctn.setConnectTimeout(10000);  
	        httpurlconctn.setReadTimeout(10000);
	        httpurlconctn.setRequestProperty("Host", "aalayamrehab.com");
	        httpurlconctn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");;
	        httpurlconctn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	        httpurlconctn.connect();
	        
	        /*OutputStreamWriter wr = new OutputStreamWriter(httpurlconctn.getOutputStream());
	        wr.write(jsonObj.toString());
	        wr.flush();*
	        
	        // Send POST output.
	        printout = new DataOutputStream(httpurlconctn.getOutputStream ());
//	        byte[] data = jsonObj.getBytes("UTF-8");
	        printout.writeBytes(jsonObj.toString());
//	        printout.write(URLEncoder.encode(jsonObj.toString(),"UTF-8"));
	        printout.flush ();
	        printout.close ();
	        
	        /*OutputStream os = httpurlconctn.getOutputStream();
	        os.write(jsn_obj.getBytes());
	        os.flush();*/

	        /*if (httpurlconctn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                + httpurlconctn.getResponseCode());
	        }

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                (httpurlconctn.getInputStream())));

	        String output;
	        System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	            System.out.println(output);
	        }*
	        /**********************************************
	        
	        StringBuilder sb = new StringBuilder();  

	        int HttpResult = httpurlconctn.getResponseCode(); 
	        Log.e(TAG, httpurlconctn.getResponseMessage());
	        if(HttpResult ==HttpURLConnection.HTTP_OK){

	            BufferedReader br = new BufferedReader(new InputStreamReader(httpurlconctn.getInputStream(),"utf-8"));  
	            String line = null;  
	            while ((line = br.readLine()) != null) {
	            	
	            	sb.append(line + "\n");  
	            }
	            
//	            wr.close();
	            printout.close();
	            br.close();
	            response = sb.toString();  

	        }else{
	        	Log.e(TAG, "HttpUrlConnection  Response Message: " + httpurlconctn.getResponseMessage());  
	        }
*/	        
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(AalayamConstants.TAG, "sh.post_makeServiceCall Catch Block: " +  e.getMessage());
		} finally {
			if (inBuffer != null) {
				try {
					inBuffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
//			if(httpurlconctn != null) httpurlconctn.disconnect();
		}
		
		return response;
	}

/*
	public String post_makeServiceCall(String jsonObj) {
		StringBuffer buffer = new StringBuffer();
		try {
			
			HttpURLConnection con = (HttpURLConnection) ( new URL(WEBSERVICE_URL)).openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			con.getOutputStream().write( (jsonObj).getBytes());
			
			InputStream is = con.getInputStream();
			byte[] b = new byte[1024];
			
			while ( is.read(b) != -1)
				buffer.append(new String(b));
			
			con.disconnect();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return buffer.toString();
	}
*/	
}