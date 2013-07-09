package br.org.tiktak.testeAPI;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.org.tiktak.api.TikTak;

public class TesteAPI {
	private TikTak tiktak;
	private String usuario;
	private String evento;
	private String diretorio = "";
	private static String systemPropertyTiktakDir = "";
	private final String sistema = "testes";

	@Before
	public void setUp() throws Exception {
		this.tiktak = new TikTak(sistema);
	}

	@After
	public void tearDown() throws Exception {
		excluiDiretorioCriadoParaTeste();
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		systemPropertyTiktakDir = System.getProperty("tiktak.dir");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		if (systemPropertyTiktakDir == null) {
			System.clearProperty("tiktak.dir");
		} else {
			System.setProperty("tiktak.dir", systemPropertyTiktakDir);
		}
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
			reader = new BufferedReader(new FileReader(tiktak.getFilePath()));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			reader.close();
			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// XXX refatorar, muito código repetido
	private void excluiDiretorioCriadoParaTeste() {
		File arquivo = new File("tik.tak");
		arquivo.delete();
		diretorio = "setDir";
		File diretorio = new File(this.diretorio);
		arquivo = new File(diretorio + "/tik.tak");
		arquivo.delete();
		diretorio.delete();
		diretorio = new File("systemProperty");
		arquivo = new File(diretorio + "/tik.tak");
		arquivo.delete();
		arquivo = new File(diretorio + "/tiktak.properties");
		arquivo.delete();
		diretorio.delete();
	}

	@Test
	public void testaConstrutorPadrao() {
		new TikTak();
	}

	@Test
	public void testeSetDirVazio() {
		tiktak.setDir("");
		assertTrue(!tiktak.getFilePath().equals(""));
	}

	@Test
	public void testeVerificarSetDirv() {
		tiktak.setDir(diretorio);
		String pathDoArquivo = tiktak.getFilePath();
		for (int i = 0; i < 20; i++) {
			resultadoDaChamadaDoAPI();
		}
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
	public void testeVerificarLogTeste() {
		setUsuario();
		setEvento();
		tiktak.logTest(this.usuario, this.evento);
		String conteudoArquivo = carregarConteudoArquivo();
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
		String caminhoDoArquivo = this.tiktak.getFilePath();
		assertTrue(caminhoDoArquivo.contains("tik.tak"));
	}

	@Test
	public void testaHierarquiaDePropriedadesParaDiretorio() {
		testPropriedadesProgramaticas();
		testPropriedadesPontoProperties();
		testPropriedadesSetProperty();
		testPropriedadesDefault();
	}

	@Test
	public void testPropriedadesProgramaticas() {
		// Primeira prisetPropertyoridade
		String diretorioPadrao = System.getProperty("user.dir") + "/";
		String testDir = diretorioPadrao + "setDir/";
		TikTak tiktakLocal = new TikTak();
		String caminhoDoDiretorio = tiktakLocal.getDirectoryPath();
		tiktakLocal.setDir(testDir);
		caminhoDoDiretorio = tiktakLocal.getDirectoryPath();
		assertTrue(caminhoDoDiretorio.equals(testDir));
	}

	@Test
	public void testPropriedadesPontoProperties() {
		// Segunda prioridade: tiktak.properties
		String diretorioPadrao = System.getProperty("user.dir") + "/";
		TikTak tiktakLocal = new TikTak();
		String caminhoDoDiretorio = tiktakLocal.getDirectoryPath();
		String testPropertiesFileDir = diretorioPadrao;
		assertTrue(caminhoDoDiretorio.equals(testPropertiesFileDir));
		excluiArquivoDeProperties(testPropertiesFileDir);
	}

	@Test
	public void testPropriedadesSetProperty() {
		// Terceira prioridade: setProperty
		String diretorioPadrao = System.getProperty("user.dir") + "/";
		String testSystemPropertiesDir = diretorioPadrao + "systemProperty/";
		criarArquivoDeProperties(testSystemPropertiesDir);
		System.setProperty("tiktak.dir", testSystemPropertiesDir);
		TikTak tiktakLocal = new TikTak();
		String caminhoDoDiretorio = tiktakLocal.getDirectoryPath();
		assertTrue(caminhoDoDiretorio.equals(testSystemPropertiesDir));
		System.clearProperty("tiktak.dir");
	}

	@Test
	public void testPropriedadesDefault() {
		// Quarta prioridade: default
		String diretorioPadrao = System.getProperty("user.dir") + "/";
		TikTak tiktakLocal = new TikTak();
		String caminhoDoDiretorio = tiktakLocal.getDirectoryPath();
		assertTrue(caminhoDoDiretorio.equals(diretorioPadrao));
	}

	private void excluiArquivoDeProperties(final String dir) {
		File arquivo = new File(dir + "tiktak.properties");
		if (arquivo.exists()) {
			arquivo.delete();
		}
	}

	private void criarArquivoDeProperties(final String testDir) {
		File arquivo = new File(testDir + "tiktak.properties");
		try {
			File diretorioFisico = new File(testDir);
			if (!diretorioFisico.exists()) {
				diretorioFisico.mkdir();
			}
			if (!arquivo.exists()) {
				arquivo.createNewFile();
				RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
				String caminho = "tiktak.dir=" + testDir;
				raf.write(caminho.getBytes());
				raf.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}