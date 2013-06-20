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

import br.org.tiktak.core.TikTak;

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
		excluiArquivoCriadoParaTeste();
		excluiDiretorioCriadoParaTeste();
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		systemPropertyTiktakDir = System.getProperty("tiktak.dir");
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		if(systemPropertyTiktakDir == null){
			System.clearProperty("tiktak.dir");
		}
		else{
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
	public void testeVerificarLogTeste() {
		setUsuario();
		setEvento();
		tiktak.logTeste(this.usuario, this.evento);
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
		String caminhoDoArquivo = this.tiktak.getCaminhoDoArquivo();
		assertTrue(caminhoDoArquivo.contains("tik.tak"));
	}
	
	@Test
	public void testaHierarquiaDePropriedadesParaDiretorio() {
		String diretorioPadrao = System.getProperty("user.dir") + "/";
		String testDir = diretorioPadrao + "setDir/";
		String testPropertiesFileDir = diretorioPadrao;// + "propertiesFile/";
		String testSystemPropertiesDir = diretorioPadrao + "systemProperty/";

		criarArquivoDeProperties(testPropertiesFileDir);
		System.setProperty("tiktak.dir", testSystemPropertiesDir);
		
		// Primeira prisetPropertyoridade: programatico	ok
		TikTak tiktakLocal = new TikTak();
		String caminhoDoDiretorio = tiktakLocal.getCaminhoDoDiretorio();
		tiktakLocal.setDir(testDir);
		caminhoDoDiretorio = tiktakLocal.getCaminhoDoDiretorio();
		assertTrue(caminhoDoDiretorio.equals(testDir));
		
		// Segunda prioridade: tiktak.properties
		// FIXME dar uma olhada a onde vocë deve colocar um arquivo .properties?
		tiktakLocal = new TikTak();
		caminhoDoDiretorio = tiktakLocal.getCaminhoDoDiretorio();
		assertTrue(caminhoDoDiretorio.equals(testPropertiesFileDir));
		excluiArquivoDeProperties(testPropertiesFileDir);
		
		// Terceira prioridade: setProperty
		tiktakLocal = new TikTak();
		caminhoDoDiretorio = tiktakLocal.getCaminhoDoDiretorio();
		assertTrue(caminhoDoDiretorio.equals(testSystemPropertiesDir));
		System.clearProperty("tiktak.dir");
		
		// Quarta prioridade: default
		tiktakLocal = new TikTak();
		caminhoDoDiretorio = tiktakLocal.getCaminhoDoDiretorio();
		assertTrue(caminhoDoDiretorio.equals(diretorioPadrao));
	}
	
	private void excluiArquivoDeProperties(String dir){
		File arquivo = new File(dir + "tiktak.properties");
		if(arquivo.exists())
			arquivo.delete();
	}

	private void criarArquivoDeProperties(String testDir) {
		File arquivo = new File(testDir + "tiktak.properties");
		try {
			File diretorioFisico = new File(testDir);			
			if (!diretorioFisico.exists()) {
				diretorioFisico.mkdir();
			}
			if(!arquivo.exists()){
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