package mx.edu.ittepic.anelcruzag.tpdm_u3_practica2_angelcruz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListadoAplicaciones extends AppCompatActivity {
    Button regresar;
    ListView listaalumnos;
    FirebaseFirestore firestore;
    CollectionReference aplicaciones;
    List<Map> datosaplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_aplicaciones);

        listaalumnos=findViewById(R.id.listaAlumno);
        regresar=findViewById(R.id.btnRegresarListaAlumno);
        firestore=FirebaseFirestore.getInstance();
        aplicaciones=firestore.collection("aplicaciones");

        listaalumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i>=0){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(ListadoAplicaciones.this);
                    alerta.setTitle("Alerta")
                            .setMessage("¿Deseas modificar/eliminar "+datosaplicacion.get(i).get("FechaAplicacion")+"?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent in=new Intent(ListadoAplicaciones.this, DatosAlumnos.class);
                                    in.putExtra("Id",datosaplicacion.get(i).get("Id").toString());
                                    in.putExtra("FechaAplicacion",datosaplicacion.get(i).get("FechaAplicacion").toString());
                                    in.putExtra("Aplicador",datosaplicacion.get(i).get("Aplicador").toString());
                                    in.putExtra("Aula",datosaplicacion.get(i).get("Aula").toString());
                                    in.putExtra("HoraInicio",datosaplicacion.get(i).get("HoraInicio").toString());
                                    in.putExtra("HoraFin",datosaplicacion.get(i).get("HoraFin").toString());
                                    startActivity(in);
                                }//onClick
                            })
                            .setNegativeButton("No",null)
                            .show();
                }//onItemClick
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ListadoAplicaciones.this, Principal.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargardatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opciones,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insertar:
                Intent insertar = new Intent(this,RegistroAlumnos.class);
                startActivity(insertar);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargardatos(){
        aplicaciones.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if  (queryDocumentSnapshots.size()<=0){
                    mensaje("Sin registros para mostrar");
                    return;
                }
                datosaplicacion=new ArrayList<>();
                for (QueryDocumentSnapshot otro:queryDocumentSnapshots){
                    Aplicaciones aplicacion=otro.toObject(Aplicaciones.class);
                    Map<String,Object> datos=new HashMap<>();
                    datos.put("FechaAplicacion",aplicacion.getFechaaplicacion());
                    datos.put("Aplicador",aplicacion.getAplicador());
                    datos.put("Aula",aplicacion.getAula());
                    datos.put("HoraInicio",aplicacion.getHorainicio());
                    datos.put("HoraFin",aplicacion.getHorafin());
                    datos.put("Id",otro.getId());
                    datosaplicacion.add(datos);
                    llenarlista();
                }
            }
        });
    }
    private void llenarlista(){
        String data[]=new String[datosaplicacion.size()];
        for (int i=0;i<data.length;i++){
            String cad=datosaplicacion.get(i).get("FechaAplcacion").toString();
            data[i]=cad;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ListadoAplicaciones.this,android.R.layout.simple_list_item_1,data);
        listaalumnos.setAdapter(adapter);
    }

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
