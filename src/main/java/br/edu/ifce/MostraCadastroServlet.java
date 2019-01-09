package br.edu.ifce;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MostraCadastroServlet
 */
@WebServlet("/mostrarCadastro")
public class MostraCadastroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BancoCadastro b = new BancoCadastro();
		
		List<Cadastro> cadastrados = b.getTodos();
		
		PrintWriter out = resp.getWriter();
		
		out.println("<html><head><link rel=stylesheet href=https://www.w3schools.com/w3css/4/w3.css>");
		out.println("<meta charset=ISO-8859-1 name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		out.println("<title>Lista de Cadastrados</title></head>");
		out.println("<div class=\"w3-container w3-green\" >");
		out.println("<h2>Lista de Cadastrados</h2></div>");
		
		out.println("<table class=\"w3-table w3-card-4 w3-centered\">");
		out.println("<thead><tr class=w3-green>");
		out.println("<th>Nome</th>");
		out.println("<th>Sobrenome</th>");
		out.println("<th>CEP</th>");
		out.println("<th>Endereço</th>");
		out.println("<th>Cidade</th>");
		out.println("<th>UF</th>");
		out.println("<th>RG</th>");
		out.println("<th>CPF</th>");
		out.println("<th>Título de Eleitor</th></tr></thead>");
		
		for(Cadastro c : cadastrados) {
			out.println("<tr class=w3-hover-light-grey>");
				out.println("<th>"+ c.getNome()+"</th>");
				out.println("<th>"+ c.getSobrenome()+"</th>");
				out.println("<th>"+ c.getCep()+"</th>");
				out.println("<th>"+ c.getEndereco()+"</th>");
				out.println("<th>"+ c.getCidade()+"</th>");
				out.println("<th>"+ c.getEstado()+"</th>");
				out.println("<th>"+ c.getRg()+"</th>");
				out.println("<th>"+ c.getCpf()+"</th>");
				out.println("<th>"+ c.getTitulo()+"</th>");
			out.println("</tr>");
		}
		
		out.println("<a href=formInscricao.html>"
				+ "<input class=\"w3-btn w3-green w3-round w3-display-bottomleft w3-margin\" value=\"Novo cadastro\"></a>");
		out.println("</body></html>");
	}


}
