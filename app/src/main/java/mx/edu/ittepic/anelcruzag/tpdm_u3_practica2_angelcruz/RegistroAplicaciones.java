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

public class RegistroAplicaciones extends AppCompatActivity {
    EditText fechaaplicacion, aplicador, aula, horainicio, horafin;
    Button insertar, cancelar;
    FirebaseFirestore firestore;
    CollectionReference aplicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_aplicaciones);

        fechaaplicacion=findViewById(R.id.txtFechaAplicacion);
        aplicador=findViewById(R.id.txtNombreAplicador);
        aula=findViewById(R.id.txtAulaAplicador);
        horainicio=findViewById(R.id.txtHoraInicioAplicacion);
        horafin=findViewById(R.id.txtHoraFinAplicacion);

        insertar=findViewById(R.id.btnInsertarAplicacion);
        cancelar=findViewById(R.id.btnCancelarAplicacion);

        firestore=FirebaseFirestore.getInstance();
        aplicaciones=firestore.collection("aplicaciones");

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
                startActivity(new Intent(RegistroAplicaciones.this,ListadoAplicaciones.class));
                finish();
            }
        });
    }

    private void insertar(){
        aplicaciones.add(campos()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mensaje("Insertado con exito");
                Intent i=new Intent(RegistroAplicaciones.this,ListadoAplicaciones.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                insertar.setEnabled(true);
                mensaje("Error! No se inserto");
            }
        });
    }

    private Map<String,Object> campos(){
        Map<String,Object> data=new HashMap<>();
        data.put("FechaAplicacion",fechaaplicacion.getText().toString());
        data.put("Aplicador",aplicador.getText().toString());
        data.put("Aula",aula.getText().toString());
        data.put("HoraInicio",horainicio.getText().toString());
        data.put("HoraFin",horafin.getText().toString());
        return data;
    }

    private void mensaje(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}
