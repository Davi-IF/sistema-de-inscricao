package br.edu.ifce;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.type.Estado;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.stella.validation.TituloEleitoralValidator;

/**
 * Servlet implementation class SistemaServlet
 */
@WebServlet("/sistema-inscricao")
public class SistemaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Cadastro c = new Cadastro();
		BancoCadastro b = new BancoCadastro();
		
		String nome = req.getParameter("nome");
		String sobrenome = req.getParameter("sobrenome");
		String cep = req.getParameter("cep");
		String endereco = req.getParameter("endereco");
		String rg = req.getParameter("rg");
		
		String cpf = req.getParameter("cpf");
		String titulo = req.getParameter("titulo");
		
		PrintWriter out = res.getWriter();
		
		CPFValidator cpfvalidator = new CPFValidator();
		TituloEleitoralValidator tituloValidator = new TituloEleitoralValidator();
		
		
		out.println("<html><head><link rel=stylesheet href=https://www.w3schools.com/w3css/4/w3.css>");
		out.println("<meta charset=ISO-8859-1 name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		try {
			cpfvalidator.assertValid(cpf);
			tituloValidator.assertValid(titulo);
		} catch (InvalidStateException e) {
			List<ValidationMessage> cpfValidationMessages = cpfvalidator.invalidMessagesFor(cpf);
			List<ValidationMessage> tituloValidationMessages = tituloValidator.invalidMessagesFor(titulo);
			
			out.println("<title>ERRO</title></head>");
			
			if(!cpfValidationMessages.isEmpty()) {
				out.println("<body><div class=\"w3-panel w3-red w3-card-4 w3-margin\">\n" + 
						"  <h3>CPF Invalido!</h3>\n" + 
						"  <p>"+ cpfValidationMessages.toString()+"</p>\n" + 
						"</div> ");
			}
			if(!tituloValidationMessages.isEmpty()) {
				out.println("<body><div class=\"w3-panel w3-red w3-card-4 w3-margin\">\n" + 
						"  <h3>Título de Eleitor Invalido!</h3>\n" + 
						"  <p>"+ tituloValidationMessages.toString()+"</p>\n" + 
						"</div> ");
			}
			
			out.println("<a href=http://localhost:8080/sistema-inscricao/formInscricao.html class=\"w3-btn w3-green w3-round w3-margin\">Voltar</a> ");
			out.println("</body></html>");
			return;
		}
		
		if(nome.isEmpty() || sobrenome.isEmpty() || cep.isEmpty()
		|| endereco.isEmpty() || rg.isEmpty()) {
			out.println("<body><div class=\"w3-panel w3-yellow w3-card-4 w3-margin\">\n" + 
					"  <h3>Valores nulos ou vazios!</h3>\n" + 
					"  <p>Desculpe, você esqueceu de preencher algum campo.</p>\n" + 
					"</div> ");
			out.println("<a href=http://localhost:8080/sistema-inscricao/formInscricao.html class=\"w3-btn w3-green w3-round w3-margin\">Voltar</a> ");
			out.println("</body></html>");
			return;
		}
		
		c.setNome(nome);
		c.setSobrenome(sobrenome);
		c.setCep(cep);
		c.setEndereco(endereco);
		c.setRg(rg);
		c.setCpf(cpf);
		c.setTitulo(titulo);
		
		b.adicionar(c);
		
		out.println("<title>Sucesso no Cadastro</title></head>");
		out.println("<body><div class=\"w3-panel w3-green\">\n" + 
				"  <h3>Cadastro Efetuado com Sucesso!</h3>\n" + 
				"  <p>Para um novo cadastro click "
				+ "<a href=http://localhost:8080/sistema-inscricao/formInscricao.html>aqui</a>.</p>\n" + 
				"</div> ");
		
		out.println("<a href=mostrarCadastro>"
				+ "<input class=\"w3-btn w3-green w3-round w3-margin\" value=\"Cadastrados\"></a>");
		
		out.println("</body></html>");
	}
	
	

}
