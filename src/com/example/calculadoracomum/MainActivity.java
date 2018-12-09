package com.example.calculadoracomum;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	TextView LabNumero, Lab;
	EditText EdtTitulo,EdtTexto;
	Button But0, But1, But2, But3, But4, But5, But6, But7, But8, But9, 
	ButVirgula, ButMais, ButMenos, ButIgual, ButPorcento, ButVezes, ButOn, ButCola,ButApagar,ButExcluir,ButSalvar;
	ListView LvSalvos;
	boolean EscreverDoZero = true;
	String Ultimo = "0";
	String Sinal = "";
	SQLiteDatabase bancoDados = null;
	Cursor cursor;
	
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	
	public String NumeroFormatar(String Numero)
	{		
		if (Numero.indexOf(".") > -1)
		{
			int Ini = Numero.indexOf(".") + 1;
			String Valor = Numero.substring(Ini, Numero.length());
			if (MyStrToFloat(Valor) > 0)
			{
				return Numero;
			}
			else
			{
				return Numero.substring(0, Ini-1);
			}
		}
		else
			return Numero;
	}
	
	public void MsgInf(String Msg)
	{
		AlertDialog ad = new AlertDialog.Builder(this).create();  
		ad.setCancelable(false); 
		ad.setMessage(Msg);  
		ad.setButton("OK", new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int which) {  
		        dialog.dismiss();                      
		    }  
		});  
		ad.show(); 
	}
	
	public void Calcular()
	{		
		double DUltimo, DNumero, DTotal;
		DTotal = 0;
		DUltimo = MyStrToFloat(Ultimo);
		DNumero = MyStrToFloat(LabNumero.getText().toString());
	    if (Sinal.equals("+")) DTotal = DUltimo + DNumero;
	    if (Sinal.equals("-")) DTotal = DUltimo - DNumero;
	    if (Sinal.equals("X")) DTotal = DUltimo * DNumero;
	    if (Sinal.equals("/")) DTotal = DUltimo / DNumero;

	    	
	    LabNumero.setText(String.valueOf(DTotal));
	}
	
	public void CalcularPorc()
	{		
		double DUltimo, DNumero, DTotal;
		DTotal = 0;
		DUltimo = MyStrToFloat(Ultimo);
		DNumero = MyStrToFloat(LabNumero.getText().toString());
	    if (Sinal.equals("+")) DTotal = DUltimo + (DUltimo / 100 * DNumero);
	    if (Sinal.equals("-")) DTotal = DUltimo - (DUltimo / 100 * DNumero);
	    if (Sinal.equals("X")) DTotal = DUltimo / 100 * DNumero;
	    if (Sinal.equals("/")) DTotal = (DUltimo / (DUltimo / 100 * DNumero)) * DUltimo;
	    	
	    LabNumero.setText(String.valueOf(DTotal));
	    LabNumero.setText(NumeroFormatar(LabNumero.getText().toString()));
	}
	
	public void Igual(boolean Repetir)
	{
		if ((Sinal.equals("") == false) && (Ultimo.equals("") == false))
		{
			if (Repetir)
			{
				Calcular();
			}
			else
			{
				if (EscreverDoZero ==false)
					Calcular();
			}
			
		}
		Sinal = "";
		LabNumeroZero();
		LabNumero.setText(NumeroFormatar(LabNumero.getText().toString()));
	}
		
	public void Incluir(String Num)
	{
		if (EscreverDoZero)
		{
			LabNumero.setText(Num);
			LabNumeroZero();
			EscreverDoZero = false;
		}
		else
		{
			LabNumero.setText(LabNumero.getText().toString() + Num);
			LabNumeroZero();
		}		
	}
	
	public String StrPermanece(String Str, String Permanece)
	{
		int I, V;
		String S;
		S = "";
		for (I = 0; I < Str.length(); I++)
		{
			for (V = 0; V < Permanece.length(); V++)
			{
				if (Str.substring(I, I+1).equals(Permanece.substring(V, V+1)))
				{
					S = S + Str.substring(I, I+1);
				}				
			}
		}
		return S;
	}
	
	public Double MyStrToFloat(String Str)
	{
		String VStr;
		VStr = StrPermanece("0" + Str,".0123456789-").toString();
		return Double.valueOf(VStr);			
	}
	
	public void LabNumeroZero()
	{
		if ((LabNumero.length() > 0) && (LabNumero.getText().toString().substring(0, 1)).equals("0"))
		{
			if (LabNumero.length() > 1)
			{
				
				if (LabNumero.getText().toString().substring(1, 2).equals(".") == false)
				{
					LabNumero.setText(LabNumero.getText().toString().substring(1, LabNumero.getText().length()));					
				}
			}
			else
			{
				LabNumero.setText(LabNumero.getText().toString().substring(1, LabNumero.getText().length()));
			}
		}
		

		if (MyStrToFloat(LabNumero.getText().toString()) == 0)
		{
			if (LabNumero.getText().toString().equals("."))
			{
				LabNumero.setText("0.");
			}
			else
			{
				LabNumero.setText("0");
			}
		}		
	}
	
   public void ButtonCriar(int Nome)
	{
		Button ButTemp;
		ButTemp = (Button) findViewById(Nome);
		ButTemp.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Button ButTemp2;
				ButTemp2 = (Button) v;
								
				if (ButTemp2.getText().toString().equals("."))
				{
					if (LabNumero.getText().toString().indexOf(".") == -1)
					{
						Incluir(".");
					}
					else
					{
						if (EscreverDoZero == true)
						{
							Incluir(".");
						}
					}
				}
				else
				{
					Incluir(ButTemp2.getText().toString());
				}
			}
		});		
	}
   
   public void ButtonCriarSinal(int Nome)
   {
	   Button ButTemp;
		ButTemp = (Button) findViewById(Nome);
		ButTemp.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Button ButTemp2;
				ButTemp2 = (Button) v;
								
				//LabNumero.Visible:= False;
				Igual(false);
				Sinal = ButTemp2.getText().toString();
				EscreverDoZero = true;
				Ultimo = LabNumero.getText().toString();
				//Esperar2(20);
				//LabNumero.Visible:= True;
			}
		});

   }
   
   public void ChamaCalculadora(){
	  
       setContentView(R.layout.activity_main);
       
       abreouCriaBanco();
       
       LabNumero = (TextView) findViewById(R.id.LabNumero);
       //Lab = (TextView) findViewById(R.id.textView1);
       ButtonCriar(R.id.But0);
       ButtonCriar(R.id.But1);
       ButtonCriar(R.id.But2);
       ButtonCriar(R.id.But3);
       ButtonCriar(R.id.But4);
       ButtonCriar(R.id.But5);
       ButtonCriar(R.id.But6);
       ButtonCriar(R.id.But7);
       ButtonCriar(R.id.But8);
       ButtonCriar(R.id.But9);
       ButtonCriar(R.id.ButVirgula);
       ButtonCriarSinal(R.id.ButDividir);
       ButtonCriarSinal(R.id.ButMais);
       ButtonCriarSinal(R.id.ButMenos);
       ButtonCriarSinal(R.id.ButVezes);
       
       ButOn = (Button) findViewById(R.id.ButOn);
       ButOn.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				LabNumero.setText("0");        
			    Ultimo = "";
			    Sinal = "";
			    LabNumeroZero();
			    EscreverDoZero = true;
			}
		});
       
       ButCola = (Button) findViewById(R.id.ButCola);
       ButCola.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				ChamaCola();		
			}
		});
       
       ButIgual = (Button) findViewById(R.id.ButIgual);
       ButIgual.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Igual(true);
			}
		});
       
       ButPorcento = (Button) findViewById(R.id.ButPorcento);
       ButPorcento.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				if ((Sinal.equals("") == false) && (Ultimo.equals("") == false))
				{
					CalcularPorc();
				}
				Sinal = "";
				Ultimo = "0";
				LabNumeroZero();	
			}
		});
       
      ButApagar= (Button) findViewById(R.id.ButApagar);
      ButApagar.setOnClickListener(new View.OnClickListener() {
    	  public void onClick(View v) {
    		  if (LabNumero.getText().toString().length()>1){
    		     LabNumero.setText(LabNumero.getText().toString().substring(0,LabNumero.getText().toString().length()-1));
    		  }else{
    			  LabNumero.setText("0");
    		  }
    	  }
      });
       
       EscreverDoZero = true;
       LabNumero.setText("0");        
       LabNumeroZero();
       mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
  	   mSensorListener = new ShakeEventListener();
  	 
   }

   public void ChamaCola(){
	 setContentView(R.layout.cola);
	 
	 EdtTexto = (EditText)findViewById(R.id.editTextTexto);
	 EdtTitulo = (EditText)findViewById(R.id.editTextTitulo);
	 LvSalvos =(ListView)findViewById(R.id.listViewSalvos);
	
     LvSalvos.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				String item;
				int ititulo,itexto;
				item = String.valueOf(LvSalvos.getItemAtPosition(position));
				cursor = bancoDados.query("cola", new String[] {"titulo","texto"},"titulo='"+item+"'", null, null, null, null);
				ititulo = cursor.getColumnIndex("titulo");
				itexto = cursor.getColumnIndex("texto");
				if ( cursor.getCount()>0) {
					cursor.moveToFirst();
					EdtTexto.setText(cursor.getString(itexto));
					EdtTitulo.setText(cursor.getString(ititulo));					
				}
			}
		});	
			
	 ButExcluir = (Button)findViewById(R.id.buttonExcluir);
	 ButExcluir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {             
				bancoDados.delete("cola","titulo='"+EdtTitulo.getText().toString()+"'", null);
				lista();
				EdtTitulo.setText("");	
				EdtTexto.setText("");
			}
		});
	 
	 ButSalvar = (Button)findViewById(R.id.buttonSalvar);	 
	 ButSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
               try {			
			
				if (EdtTitulo.getText().toString().trim().isEmpty()) {
					msg("Digite o Titulo para salvar", "Atençao");
					return;
				}
				if (EdtTexto.getText().toString().trim().isEmpty()) {
					msg("Digite o Texto para salvar", "Atençao");
					return;
				}
				 cursor = bancoDados.query("cola", null,"titulo='"+EdtTitulo.getText().toString()+"'", null, null, null, null);
					if (cursor.getCount() > 0) {
						String sSql ="UPDATE cola SET texto='"+EdtTexto.getText().toString()+"' WHERE titulo='"+EdtTitulo.getText().toString()+"'";
						bancoDados.execSQL(sSql);
					}else{
						String sSql ="INSERT INTO cola (titulo,texto)values('"+EdtTitulo.getText().toString()+"','"+EdtTexto.getText().toString()+"')";
						bancoDados.execSQL(sSql);
					}
				  lista();
				  EdtTitulo.setText("");	
				  EdtTexto.setText("");
               } catch (Exception e) {
   				msg("Erro "+e,"Erro Salvar");
   			}	
			}
		});
	
	 mSensorListener.setOnShakeListener(new OnShakeListener() {
	  		public void onShake() {
	  		      ChamaCalculadora();						
	  		}
	  	 });	 
	 lista();
   }
   
   public void lista(){
	   try {
			ArrayList<String> lista = new ArrayList<String>();
			cursor = bancoDados.query("cola", new String[] { "titulo" }, null,
					null, null, null, null);
			if (cursor.getCount() > 0) {
				int ser = cursor.getColumnIndex("titulo");
				int numregistr = cursor.getCount();
				cursor.moveToFirst();
				while (numregistr > 0) {
					lista.add(cursor.getString(ser));
					cursor.moveToNext();
					numregistr--;
				}				
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, lista);
			LvSalvos.setAdapter(adapter);
		} catch (Exception e) {
			msg("Erro " + e, "Erro Carregar list");
		}  
   }
   
   public void abreouCriaBanco() {
		try {
			String nomeBanco = "School";
			bancoDados = openOrCreateDatabase(nomeBanco, MODE_WORLD_READABLE,null);
			String sql = "CREATE TABLE IF NOT EXISTS cola (id INTEGER PRIMARY KEY, titulo TEXT,texto TEXT);";
			bancoDados.execSQL(sql);
		} catch (Exception erro) {
			
		}
	}
   
   public void msg(String mensagen, String titulo) {
		AlertDialog.Builder mesg = new AlertDialog.Builder(this);
		mesg.setMessage(mensagen);
		mesg.setTitle(titulo);
		mesg.setNeutralButton("Ok", null);
		mesg.show();
	}
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   ChamaCalculadora();
    }

    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
  
   @Override
   protected void onResume() {
		super.onResume();
		try {	
		
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
		} catch (Exception e) {
			msg("Erro "+e,"onResume");
		}
  }
 
   @Override
   protected void onPause() {
	   try{
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
     } catch (Exception e) {
		msg("Erro "+e,"onPause");
	}
	}
  
   public interface OnShakeListener {
		 
	    /**
	     * Called when shake gesture is detected.
	     */
	    void onShake();
	  }
   
   public class ShakeEventListener implements SensorEventListener {
		/** Minimum movement force to consider. */
	  private static final int MIN_FORCE = 12;
	 
	  /**
	   * Minimum times in a shake gesture that the direction of movement needs to
	   * change.
	   */
	  private static final int MIN_DIRECTION_CHANGE = 3;
	 
	  /** Maximum pause between movements. */
	  private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;
	 
	  /** Maximum allowed time for shake gesture. */
	  private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;
	 
	  /** Time when the gesture started. */
	  private long mFirstDirectionChangeTime = 0;
	 
	  /** Time when the last movement started. */
	  private long mLastDirectionChangeTime;
	 
	  /** How many movements are considered so far. */
	  private int mDirectionChangeCount = 0;
	 
	  /** The last x position. */
	  private float lastX = 0;
	 
	  /** The last y position. */
	  private float lastY = 0;
	 
	  /** The last z position. */
	  private float lastZ = 0;
	 
	  /** OnShakeListener that is called when shake is detected. */
	  private OnShakeListener mShakeListener;
	 
	  /**
	   * Interface for shake gesture.
	   */
	  
	  
	 
	  public void setOnShakeListener(OnShakeListener listener) {
	    mShakeListener = listener;
	  }
	 
	  
	  public void onSensorChanged(SensorEvent se) {
	    // get sensor data
	    float x = se.values[SensorManager.DATA_X];
	    float y = se.values[SensorManager.DATA_Y];
	    float z = se.values[SensorManager.DATA_Z];
	 
	    // calculate movement
	    float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);
	 
	    if (totalMovement > MIN_FORCE) {
	 
	      // get time
	      long now = System.currentTimeMillis();
	 
	      // store first movement time
	      if (mFirstDirectionChangeTime == 0) {
	        mFirstDirectionChangeTime = now;
	        mLastDirectionChangeTime = now;
	      }
	 
	      // check if the last movement was not long ago
	      long lastChangeWasAgo = now - mLastDirectionChangeTime;
	      if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {
	 
	        // store movement data
	        mLastDirectionChangeTime = now;
	        mDirectionChangeCount++;
	 
	        // store last sensor data 
	        lastX = x;
	        lastY = y;
	        lastZ = z;
	 
	        // check how many movements are so far
	        if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {
	 
	          // check total duration
	          long totalDuration = now - mFirstDirectionChangeTime;
	          if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
	            mShakeListener.onShake();
	            resetShakeParameters();
	          }
	        }
	 
	      } else {
	        resetShakeParameters();
	      }
	    }
	  }
	 
	  /**
	   * Resets the shake parameters to their default values.
	   */
	  private void resetShakeParameters() {
	    mFirstDirectionChangeTime = 0;
	    mDirectionChangeCount = 0;
	    mLastDirectionChangeTime = 0;
	    lastX = 0;
	    lastY = 0;
	    lastZ = 0;
	  }
	 
	 
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	  }
	}
}
