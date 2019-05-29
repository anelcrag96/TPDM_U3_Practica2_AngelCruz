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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroAlumnos extends AppCompatActivity {
    EditText nocontrol, nombre, apellidos, carrera, fechaaplicacion;
    Button insertar, cancelar;
    FirebaseFirestore firestore;
    CollectionReference alumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_alumnos);

        nocontrol=findViewById(R.id.txtNoControl);
        nombre=findViewById(R.id.txtNombre);
        apellidos=findViewById(R.id.txtApellidos);
        carrera=findViewById(R.id.txtCarrera);
        fechaaplicacion=findViewById(R.id.txtFechaAplicacion);

        insertar=findViewById(R.id.btnInsertarAlumno);
        cancelar=findViewById(R.id.btnCancelarAlumno);

        firestore=FirebaseFirestore.getInstance();
        alumnos=firestore.collection("alumnos");

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertar.setEnabled(false);
                insertar();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroAlumnos.this,ListadoAlumnos.class));
                finish();
            }
        });
    }

    private void insertar(){
        alumnos.add(campos()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mensaje("Se inserto correctamente");
                Intent i=new Intent(RegistroAlumnos.this,ListadoAlumnos.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                insertar.setEnabled(true);
                mensaje("Error!! No se inserto");
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
    }//campos

    private void mensaje(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }//mensaje
}
