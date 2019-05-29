package mx.edu.ittepic.anelcruzag.tpdm_u3_practica2_angelcruz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatosAlumnos extends AppCompatActivity {
    EditText nocontrol, nombre, apellidos, carrera, fechaaplicacion;
    Button modificar, eliminar, cancelar;
    FirebaseFirestore firestore;
    CollectionReference alumnos;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_alumnos);

        nocontrol=findViewById(R.id.txtNoControl);
        nombre=findViewById(R.id.txtNombre);
        apellidos=findViewById(R.id.txtApellidos);
        carrera=findViewById(R.id.txtCarrera);
        fechaaplicacion=findViewById(R.id.txtFechaAplicacion);

        modificar=findViewById(R.id.btnModificarAlumno);
        eliminar=findViewById(R.id.btnEliminarAlumno);
        cancelar=findViewById(R.id.btnCancelarAlumno);

        firestore=FirebaseFirestore.getInstance();
        alumnos=firestore.collection("alumnos");
        datos=getIntent().getExtras();

        nocontrol.setText(datos.get("NoControl").toString());
        nombre.setText(datos.get("Nombre").toString());
        apellidos.setText(datos.get("Apellidos").toString());
        carrera.setText(datos.get("Carrera").toString());
        fechaaplicacion.setText(datos.get("FechaAplicacion").toString());

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificardatos();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminardatos();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatosAlumnos.this,ListadoAlumnos.class));
                finish();
            }
        });
    }

    private void modificardatos(){
        alumnos.document(datos.get("Id").toString()).update(campos()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Actualizado correctamente");
                startActivity(new Intent(DatosAlumnos.this,ListadoAlumnos.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error!! No modificado");
                startActivity(new Intent(DatosAlumnos.this,ListadoAlumnos.class));
                finish();
            }
        });
    }

    private void eliminardatos(){
        alumnos.document(datos.get("Id").toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Eliminado correctamente");
                startActivity(new Intent(DatosAlumnos.this,ListadoAlumnos.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error!! No eliminado");
                startActivity(new Intent(DatosAlumnos.this,ListadoAlumnos.class));
                finish();
            }
        });
    }

    private Map<String,Object> campos(){
        Map<String,Object> data=new HashMap<>();
        data.put("NoControl",nocontrol.getText().toString());
        data.put("Nombre",nombre.getText().toString());
        data.put("Apellidos",apellidos.getText().toString());
        data.put("Carrera",carrera.getText().toString());
        data.put("FechaAplicacion",fechaaplicacion.getText().toString());
        return data;
    }

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
