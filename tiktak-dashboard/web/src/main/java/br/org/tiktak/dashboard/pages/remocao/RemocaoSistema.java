package br.org.tiktak.dashboard.pages.remocao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jmine.tec.web.wicket.pages.Template;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.google.gson.reflect.TypeToken;

import bancosys.tec.exception.MessageCreator;
import br.org.tiktak.core.Eventv2;
import br.org.tiktak.core.GsonFactory;
import br.org.tiktak.dashboard.core.ProcessamentoArquivo;

public class RemocaoSistema extends Template {

	private List<String> listaDeSistemas;
	private String sistemaEscolhido;
	private FileReader reader;
 
	@Override
	protected void onInitialize() {
		super.onInitialize(); 
		listaDeSistemas = new ArrayList<String>();
		try {
			ProcessamentoArquivo processamentoArquivo = new ProcessamentoArquivo();
			Set<String> setDeSistemas = processamentoArquivo.getNomesSistemas();
			for (String nomeDoSistema : setDeSistemas)
				listaDeSistemas.add(nomeDoSistema);
			sistemaEscolhido = listaDeSistemas.get(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		add(new FeedbackPanel("feedback")); 
		DropDownChoice<String> listSites = new DropDownChoice<String>(
			"sistemas", new PropertyModel<String>(this, "sistemaEscolhido"), listaDeSistemas); 
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() { 
				removeSistema(sistemaEscolhido);
				info("O sitema " + sistemaEscolhido + " foi excluido."); 
			}
		}; 
		add(form);
		form.add(listSites); 
	}
	
	public void removeSistema(String sistemaEscolhido){
		try {
			RandomAccessFile raf = new RandomAccessFile(new File("dashboard.bd"), "rw");
			File arquivoAuxiliar = criaArquivoAuxiliar();
			RandomAccessFile rafAux = new RandomAccessFile(arquivoAuxiliar, "rw");
			//TODO copiar todos os sistemas diferentes do sistemaEscolhido para o arquivo auxiliar
			//excluir dashboard.bd e renomear o arquivo auxilliar para dashboard.bd
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private File criaArquivoAuxiliar() {
		File arquivoAuxiliar = new File("arquivoAuxiliar.bd");
		try {
			arquivoAuxiliar.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arquivoAuxiliar;
	}
	
	@Override
	protected MessageCreator getHelpTextCreator() {
		return null;
	}

}
