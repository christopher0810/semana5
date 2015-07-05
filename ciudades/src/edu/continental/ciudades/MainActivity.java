package edu.continental.ciudades;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	ListView listaciudades;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listaciudades = (ListView) findViewById(R.id.listaciudades);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class TareaWSListar extends AsyncTask <String,Integer, Boolean> {
    	
    	private ProgressDialog cargando = new ProgressDialog(MainActivity.this);
    	private String [] ciudades;    	
    			
		@Override
		protected void onPreExecute() {
			cargando.setMessage("Canrgando...");
			cargando.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			boolean resultado = true;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet llamada = new HttpGet("http://172.16.82.29:8080/restfull/rest/PrimerN5/ciudades/");
			llamada.setHeader("content-type", "application/json");
			try {
				HttpResponse respuesta = httpClient.execute(llamada);
				String cadena = EntityUtils.toString(respuesta.getEntity());
				JSONArray respuestaJSON = new JSONArray(cadena);
				ciudades = new String [respuestaJSON.length()];
				
				for (int i=0; i<respuestaJSON.length(); i++ ) {
					JSONObject Objeto = respuestaJSON.getJSONObject(i);
					
					String idciudad = Objeto.getString("id");
					String nombre = Objeto.getString("nombre");
					String altitud = Objeto.getString("altitud");
					
					ciudades[i] = idciudad + "-" + nombre + "-" + altitud;											
				} 
							
			}
			catch(Exception ex){
				Log.e("Serviciorest", "Error", ex);
				resultado = false;
			}
			
			return resultado;
			
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			cargando.dismiss();
			if(result){
				ArrayAdapter<String> Adaptador = new ArrayAdapter<String> (MainActivity.this, android.R.layout.simple_list_item_1,ciudades);
				listaciudades.setAdapter(Adaptador);
			}
		}
    } 
    
}
