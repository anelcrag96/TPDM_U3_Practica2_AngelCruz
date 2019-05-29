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

public class DatosAplicaciones extends AppCompatActivity {
    EditText fechaaplicacion, aplicador, aula, horainicio, horafin;
    Button modificar, eliminar, cancelar;
    FirebaseFirestore firestore;
    CollectionReference aplicaciones;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_aplicaciones);

        fechaaplicacion=findViewById(R.id.txtFechaAplicacion);
        aplicador=findViewById(R.id.txtNombreAplicador);
        aula=findViewById(R.id.txtAulaAplicador);
        horainicio=findViewById(R.id.txtHoraInicioAplicacion);
        horafin=findViewById(R.id.txtHoraFinAplicacion);

        modificar=findViewById(R.id.btnModificarAlumno);
        eliminar=findViewById(R.id.btnEliminarAlumno);
        cancelar=findViewById(R.id.btnCancelarAlumno);

        firestore= FirebaseFirestore.getInstance();
        aplicaciones=firestore.collection("aplicaciones");
        datos=getIntent().getExtras();

        fechaaplicacion.setText(datos.get("FechaAplicacion").toString());
        aplicador.setText(datos.get("Aplicador").toString());
        aula.setText(datos.get("Aula").toString());
        horainicio.setText(datos.get("HoraInicio").toString());
        horafin.setText(datos.get("HoraFin").toString());

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
                startActivity(new Intent(DatosAplicaciones.this,ListadoAplicaciones.class));
                finish();
            }
        });
    }

    private void modificardatos(){
        aplicaciones.document(datos.get("Id").toString()).update(campos()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Actualizado correctamente");
                startActivity(new Intent(DatosAplicaciones.this,ListadoAplicaciones.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error!! No modificado");
                startActivity(new Intent(DatosAplicaciones.this,ListadoAplicaciones.class));
                finish();
            }
        });
    }

    private void eliminardatos(){
        aplicaciones.document(datos.get("Id").toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Eliminado correctamente");
                startActivity(new Intent(DatosAplicaciones.this,ListadoAplicaciones.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error!! No eliminado");
                startActivity(new Intent(DatosAplicaciones.this,ListadoAplicaciones.class));
                finish();
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

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
