package leal.cursoandroid.com.ceep.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import leal.cursoandroid.com.ceep.R;
import leal.cursoandroid.com.ceep.dao.NotaDAO;
import leal.cursoandroid.com.ceep.model.Nota;
import leal.cursoandroid.com.ceep.ui.adapter.ListaNotasAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        ListView listaNotas = findViewById(R.id.listView);

        NotaDAO dao = new NotaDAO();
        for (int i=1; i<=10000; i++){
            dao.insere(new Nota("Titulo" + i,"Descrição" +i));
        }
        dao.insere(new Nota("primeira nota", "primeira descriçao"));

        List<Nota> todasNotas = dao.todos();

        listaNotas.setAdapter(new ListaNotasAdapter(this,todasNotas));
    }
}
