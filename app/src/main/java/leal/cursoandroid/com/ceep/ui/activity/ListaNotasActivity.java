package leal.cursoandroid.com.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import leal.cursoandroid.com.ceep.R;
import leal.cursoandroid.com.ceep.dao.NotaDAO;
import leal.cursoandroid.com.ceep.model.Nota;
import leal.cursoandroid.com.ceep.ui.recycler.adapter.ListaNotasAdapter;
import leal.cursoandroid.com.ceep.ui.recycler.adapter.listener.OnItemClickListener;

public class ListaNotasActivity extends AppCompatActivity {

    public static final int CODIGO_REQUISICAO_INSERE_NOTA = 1;
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();

    }

    private void configuraBotaoInsereNota() {
        View botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
        Intent iniciaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaFormulario, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 0; i < 10; i++){
            dao.insere(new Nota("titulo" + (i+1), "descriçao" + (i + 1)));
        }
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(ehResultadoComNota(requestCode, resultCode, data)){

            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            new NotaDAO().insere(notaRecebida);
            adapter.adiciona(notaRecebida);
        }

        if(requestCode == 2&& resultCode == 2 && temNota(data) && data.hasExtra("posicao")){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            int posicaoRecebida = data.getIntExtra("posicao", -1);
            new NotaDAO().altera(posicaoRecebida, notaRecebida);
            adapter.altera(posicaoRecebida,notaRecebida);
        }
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && ehCodigoResultadoNotaCriada(resultCode) && temNota(data);
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == 2;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == 1;
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra("nota");
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.recyclerView);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                abreFormularioComNota.putExtra("nota", nota);
                abreFormularioComNota.putExtra("posicao", posicao);
                startActivityForResult(abreFormularioComNota, 2);
            }
        });
    }

}
