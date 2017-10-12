package com.example.osbete.contactossqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class activity_datos extends AppCompatActivity {

    Button agregar;
    Button modificar;
    Button borrar;
    EditText id;
    EditText nombre;
    EditText email;
    EditText twitter;
    EditText tel;
    EditText fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        modificar = (Button) findViewById(R.id.btnModificar);
        borrar = (Button) findViewById(R.id.btnBorrar);
        id = (EditText) findViewById(R.id.txtid);
        nombre = (EditText) findViewById(R.id.txtName);
        email = (EditText) findViewById(R.id.txtCorreo);
        twitter = (EditText) findViewById(R.id.txtTwitter);
        tel = (EditText) findViewById(R.id.txtPhone);
        fecha = (EditText) findViewById(R.id.txtFechaNac);


        agregar();
        buscarcontacto();
        modificar();
        BorrarContacto();

    }

    public void buscarcontacto() {

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Pattern p = Pattern.compile("[0-9]+");
                DaoContactos dao = new DaoContactos(activity_datos.this);
                ArrayList<Contacto> contacto;

                contacto = (ArrayList<Contacto>) dao.obtenercontacto(id.getText().toString());
                if (id.getText().length() > 0 && p.matcher(id.getText().toString()).matches() == true && contacto.size() > 0) {
                    /*id.setText(contacto.get(0).getId());*/
                    nombre.setText(contacto.get(0).getNombre());
                    email.setText(contacto.get(0).getCorreo_electronico());
                    twitter.setText(contacto.get(0).getTwitter());
                    tel.setText(contacto.get(0).getTelefono());
                    fecha.setText(contacto.get(0).getFecha_nacimiento());
                } else {
                    nombre.setText("");
                    email.setText("");
                    twitter.setText("");
                    tel.setText("");
                    fecha.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void modificar() {
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DaoContactos buscar = new DaoContactos(activity_datos.this);

                    Contacto contacto1 = new Contacto(Integer.parseInt(id.getText().toString()), nombre.getText().toString(), email.getText().toString(), twitter.getText().toString(), tel.getText().toString(), fecha.getText().toString());
                    if (validacion().length() == 0) {
                        if (buscar.update(contacto1) > 0) {
                            Toast.makeText(getBaseContext(), "Contacto Actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Ocurrio un Error al modificar", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception err) {
                    Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void agregar() {
        agregar = (Button) findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent atras = new Intent();

                if (validacion().length() == 0) {
                    Contacto alum = new Contacto();
                    alum.setNombre(nombre.getText().toString());
                    alum.setCorreo_electronico(email.getText().toString());
                    alum.setTwitter(twitter.getText().toString());
                    alum.setTelefono(tel.getText().toString());
                    alum.setFecha_nacimiento(fecha.getText().toString());

                    atras.putExtra("micontacto", alum);

                    setResult(RESULT_OK, atras);
                    finish();
                }

            }
        });
    }

    public void BorrarContacto() {

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DaoContactos dao = new DaoContactos(activity_datos.this);
                    Pattern p = Pattern.compile("[0-9]+");
                    if (id.getText().length() > 0 && p.matcher(id.getText().toString()).matches() == true) {
                        if (dao.delete(id.getText().toString()) > 0) {
                            Toast.makeText(getBaseContext(), "Contacto Eliminado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "No se pudo eliminar el Contacto", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception err) {
                    Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public String validacion() {

        String troubles = "";


        if (nombre.getText().toString().length() > 0) {
            nombre.setError(null);
        } else {
            troubles += ">Nombre Obligatorio";
            nombre.setError("Nombre Obligatorio");

        }

        if (email.getText().toString().length() > 0) {
            if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() == true) {
                email.setError(null);
            } else {
                troubles += ">Correo Electronico Invalido";
                email.setError("Correo Invalido");
            }
        }

        if (tel.getText().toString().length() > 0) {
            tel.setError(null);
        } else {
            troubles += ">Telefono Obligatorio";
            tel.setError("Telefono Obligatorio");

        }

        Pattern p = Pattern.compile("([0-9]{2})[/]([0-9]{2})[/]([0-9]{2})");
        if (p.matcher(fecha.getText().toString()).matches() == true) {
            fecha.setError(null);
        } else {
            troubles += ">Formato de Fecha Incorrecto";
            fecha.setError("Formato de Fecha Incorrecto");
        }

        return troubles;
    }

}
