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

public class ListadoAlumnos extends AppCompatActivity {
    Button regresar;
    ListView listaalumnos;
    FirebaseFirestore firestore;
    CollectionReference alumnos;
    List<Map> datosalumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_alumnos);

        listaalumnos=findViewById(R.id.listaAlumno);
        regresar=findViewById(R.id.btnRegresarListaAlumno);
        firestore=FirebaseFirestore.getInstance();
        alumnos=firestore.collection("alumnos");

        listaalumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i>=0){
                    AlertDialog.Builder alerta=new AlertDialog.Builder(ListadoAlumnos.this);
                    alerta.setTitle("Alerta")
                            .setMessage("¿Deseas modificar/eliminar el alumno "+datosalumnos.get(i).get("Nombre")+"?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent in=new Intent(ListadoAlumnos.this, DatosAlumnos.class);
                                    in.putExtra("Id",datosalumnos.get(i).get("Id").toString());
                                    in.putExtra("NoControl",datosalumnos.get(i).get("NoControl").toString());
                                    in.putExtra("Nombre",datosalumnos.get(i).get("Nombre").toString());
                                    in.putExtra("Apellidos",datosalumnos.get(i).get("Apellidos").toString());
                                    in.putExtra("Carrera",datosalumnos.get(i).get("Carrera").toString());
                                    in.putExtra("FechaAplicacion",datosalumnos.get(i).get("FechaAplicacion").toString());
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
                Intent i=new Intent(ListadoAlumnos.this, Principal.class);
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
        alumnos.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if  (queryDocumentSnapshots.size()<=0){
                    mensaje("Sin registros para mostrar");
                    return;
                }
                datosalumnos=new ArrayList<>();
                for (QueryDocumentSnapshot otro:queryDocumentSnapshots){
                    Alumnos alumno=otro.toObject(Alumnos.class);
                    Map<String,Object> datos=new HashMap<>();
                    datos.put("NoControl",alumno.getNocontrol());
                    datos.put("Nombre",alumno.getNombre());
                    datos.put("Apellidos",alumno.getApellidos());
                    datos.put("Carrera",alumno.getCarrera());
                    datos.put("FechaAplicacion",alumno.getFechaaplicacion());
                    datos.put("Id",otro.getId());
                    datosalumnos.add(datos);
                    llenarlista();
                }
            }
        });
    }
    private void llenarlista(){
        String data[]=new String[datosalumnos.size()];
        for (int i=0;i<data.length;i++){
            String cad=datosalumnos.get(i).get("Nombre").toString();
            data[i]=cad;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ListadoAlumnos.this,android.R.layout.simple_list_item_1,data);
        listaalumnos.setAdapter(adapter);
    }

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
