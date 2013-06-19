package br.org.tiktak.testeAPI;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.org.tiktak.core.TikTak;

public class TesteAPI {
	private TikTak tiktak;
	private String usuario;
	private String evento;
	private String diretorio = "";
	private final String sistema = "testes";

	@Before
	public void setUp() throws Exception {
		this.tiktak = new TikTak(sistema);
	}

	@After
	public void tearDown() throws Exception {
		excluiArquivoCriadoParaTeste();
		excluiDiretorioCriadoParaTeste();
	}

	public void setUsuario() {
		this.usuario = "ALBERT" + System.currentTimeMillis();
	}

	public void setEvento() {
		this.evento = "TESTE-API-NOVO" + System.currentTimeMillis();
	}

	public String resultadoDaChamadaDoAPI() {
		setUsuario();
		setEvento();
		tiktak.log(this.usuario, this.evento);
		return carregarConteudoArquivo();
	}

	private String carregarConteudoArquivo() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(tiktak.getCaminhoDoArquivo()));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void excluiArquivoCriadoParaTeste() {
		File arquivo = new File(sistema);
		arquivo.delete();
	}

	private void excluiDiretorioCriadoParaTeste() {
		this.diretorio = "tiktakdir/";
		File diretorio = new File(this.diretorio);
		String nomeDiretorioAbsoluto = diretorio.getAbsolutePath();
		File diretorioAbsoluto = new File(nomeDiretorioAbsoluto);
		if (diretorio.isDirectory()) {
			for (File arquivo : diretorioAbsoluto.listFiles()) {
				arquivo.delete();
			}
			diretorioAbsoluto.delete();
		}
	}

	@Test
	public void testaConstrutorPadrao() {
		new TikTak();
	}

	@Test
	public void testeSetDirVazio() {
		tiktak.setDir("");
		assertTrue(!tiktak.getCaminhoDoArquivo().equals(""));
	}
	
	@Test
	public void testeVerificarSetDirv() {
		tiktak.setDir(diretorio);
		String pathDoArquivo = tiktak.getCaminhoDoArquivo();
		resultadoDaChamadaDoAPI();
		assertTrue(pathDoArquivo.contains(diretorio));
	}

	@Test
	public void testeVerificarUsuario() {
		String conteudoArquivo = resultadoDaChamadaDoAPI();
		assertTrue(conteudoArquivo.contains(this.usuario));
	}

	@Test
	public void testeVerificarEvento() {
		String conteudoArquivo = resultadoDaChamadaDoAPI();
		assertTrue(conteudoArquivo.contains(this.evento));
	}

	@Test
	public void testeVerificarArquivo() {
		String conteudoArquivo = resultadoDaChamadaDoAPI();
		conteudoArquivo = carregarConteudoArquivo();
		assertTrue(conteudoArquivo.contains(sistema));
		assertTrue(conteudoArquivo.contains(this.evento));
	}

	@Test
	public void testaSePathDoArquivoEhTikPontoTak() {
		String caminhoDoArquivo = this.tiktak.getCaminhoDoArquivo();
		assertTrue(caminhoDoArquivo.contains("tik.tak"));
	}
}