package br.org.tiktak.dashboard.core;

import java.util.HashMap;

/**
 * 
 * @author roberto.rodrigues
 * 
 */
public class Core {
	private String funcionalidade;

	private Integer quantidadeUsadaDaFuncionalidade;

	private final HashMap<String, Integer> funcionalidadePorQuantidade = new HashMap<String, Integer>();

	public String getFuncionaidade() {
		return this.funcionalidade;
	}

}
