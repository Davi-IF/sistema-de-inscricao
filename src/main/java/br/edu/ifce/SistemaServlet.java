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
		String cpf = req.getParameter("cpf");
		String titulo = req.getParameter("titulo");
		
		PrintWriter out = res.getWriter();
		
		CPFValidator cpfvalidator = new CPFValidator();
		TituloEleitoralValidator tituloValidator = new TituloEleitoralValidator();
		
		out.println("<html><head><link rel=stylesheet href=https://www.w3schools.com/w3css/4/w3.css>");
		out.println("<title>ERRO</title>"
				+ "<meta charset=\"UTF-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>");
		try {
			cpfvalidator.assertValid(cpf);
			tituloValidator.assertValid(titulo);
		} catch (InvalidStateException e) {
			List<ValidationMessage> cpfValidationMessages = cpfvalidator.invalidMessagesFor(cpf);
			List<ValidationMessage> tituloValidationMessages = tituloValidator.invalidMessagesFor(titulo);
			
			
			if(!cpfValidationMessages.isEmpty()) {
				out.println("<body><div class=\"w3-panel w3-red\">\n" + 
						"  <h3>CPF Invalido!</h3>\n" + 
						"  <p>"+ cpfValidationMessages.toString()+"</p>\n" + 
						"</div> ");
			}
			if(!tituloValidationMessages.isEmpty()) {
				out.println("<body><div class=\"w3-panel w3-red\">\n" + 
						"  <h3>Titulo de Eleitor Invalido!</h3>\n" + 
						"  <p>"+ tituloValidationMessages.toString()+"</p>\n" + 
						"</div> ");
			}
			
		}
		out.println("<a href=http://localhost:8080/sistema-inscricao/formInscricao.html class=\"w3-btn w3-green w3-round w3-margin\">Voltar</a> ");
		out.println("</body>");
	}

}
