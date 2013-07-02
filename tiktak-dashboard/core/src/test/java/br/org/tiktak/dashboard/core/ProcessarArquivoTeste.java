package br.org.tiktak.dashboard.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ProcessarArquivoTeste {
	
	@Test
	public void testGetNomesSistemas(){
		InputStream reader = this.getClass().getClassLoader().getResourceAsStream("dashboard.bd");
		ProcessamentoArquivo processador = new ProcessamentoArquivo(new InputStreamReader(reader));
		List<String> nomesSistemas = processador.getNomesSistemas();
		Assert.assertThat(nomesSistemas.size(), Matchers.is(2));
		Assert.assertThat(nomesSistemas, Matchers.hasItem("Jupiter-Web"));
		Assert.assertThat(nomesSistemas, Matchers.hasItem("Citati-Prod"));
	}
}
