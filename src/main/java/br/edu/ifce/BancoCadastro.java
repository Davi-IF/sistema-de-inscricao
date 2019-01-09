package br.edu.ifce;

import java.util.ArrayList;
import java.util.List;

public class BancoCadastro {
	
	private static List<Cadastro> banco = new ArrayList<Cadastro>();
	
	public void adicionar(Cadastro c) {
		banco.add(c);
	}
	
	public List<Cadastro> getTodos(){
		return BancoCadastro.banco;
	}
	
	public boolean contem(Cadastro cad) {
		for(Cadastro c : banco) {
			if(c.getCpf().equals(cad.getCpf()) ||
			   c.getRg().equals(cad.getRg()) ||
			   c.getTitulo().equals(cad.getTitulo())) {
				return true;
			}
		}
		return false;
	}

}
