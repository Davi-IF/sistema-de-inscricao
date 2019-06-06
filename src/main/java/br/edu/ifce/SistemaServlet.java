package br.edu.ifce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
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
    
	/*Metodos usados para a comunicação 
	 * com a API, atraves do JSON*/ 
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static JSONObject readJsonFromUrl(String link) throws IOException, JSONException {
		URL url = new URL(link);
		URLConnection urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
		InputStream is = urlConn.getInputStream();

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	
	
	/*Metodo DoPost
	 * Aqui o programa recebe uma request da pagina formInscricao.html
	 * E irá trata da maneira adequada com o response*/
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//Instanciamento de cadastro e também do banco onde o cadastro será armazenado
		Cadastro c = new Cadastro();
		BancoCadastro b = new BancoCadastro();
		PrintWriter out = res.getWriter();
		//Inicio do front-end da pagina web que será exibida 
		out.println("<html><head><link rel=stylesheet href=https://www.w3schools.com/w3css/4/w3.css>");
		out.println("<meta charset=ISO-8859-1 name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		//Recebimento dos dados do formulario
		String nome = req.getParameter("nome");
		String sobrenome = req.getParameter("sobrenome");
		String cep = req.getParameter("cep");
		
		//tratamento para que o CEP esteja de acordo com a API
		JSONObject json = null;
		String cepJson = cep.replace(".", "").replace("-", "");
		
		/*try catch usado para receber o objeto JSON com as informações 
		 * do cep fornecido*/
		try {
			json = readJsonFromUrl("https://viacep.com.br/ws/"+cepJson+"/json/");
		} catch (JSONException e1) {
			out.println("<body><div class=\"w3-panel w3-red w3-card-4 w3-margin\">\n" + 
					"  <h3>CEP Invalido!</h3>\n" + 
					"  <p>Por favor, digite novamente seu CEP.</p>\n" + 
					"</div> ");
			out.println("<a href=http://localhost:8080/sistema-inscricao/formInscricao.html class=\"w3-btn w3-green w3-round w3-margin\">Voltar</a> ");
			out.println("</body></html>");
			
			return;
		} catch (IOException e1) {
			out.println("<body><div class=\"w3-panel w3-red w3-card-4 w3-margin\">\n" + 
					"  <h3>CEP Invalido!</h3>\n" + 
					"  <p>Por favor, digite novamente seu CEP.</p>\n" + 
					"</div> ");
			out.println("<a href=http://localhost:8080/sistema-inscricao/formInscricao.html class=\"w3-btn w3-green w3-round w3-margin\">Voltar</a> ");
			out.println("</body></html>");
			
			return;
		}
		
		
		//Recebimento dos dados do formulario
		String endereco = req.getParameter("endereco");
		String rg = req.getParameter("rg");
		
		String cpf = req.getParameter("cpf");
		String titulo = req.getParameter("titulo");
		
		
		
		//Instanciamento dos validadores: CPF e Titulo Eleitoral
		CPFValidator cpfvalidator = new CPFValidator();
		TituloEleitoralValidator tituloValidator = new TituloEleitoralValidator();
		
		//tratamento de erros dos validadores
		try {
			cpfvalidator.assertValid(cpf);
			tituloValidator.assertValid(titulo);
		} catch (InvalidStateException e) {
			List<ValidationMessage> cpfValidationMessages = cpfvalidator.invalidMessagesFor(cpf);
			List<ValidationMessage> tituloValidationMessages = tituloValidator.invalidMessagesFor(titulo);
			
			out.println("<title>ERRO</title></head>");
			
			//Caso o cpf tenha erro
			if(!cpfValidationMessages.isEmpty()) {
				out.println("<body><div class=\"w3-panel w3-red w3-card-4 w3-margin\">\n" + 
						"  <h3>CPF Invalido!</h3>\n" + 
						"  <p>"+ cpfValidationMessages.toString()+"</p>\n" + 
						"</div> ");
			}
			//Caso o titulo tenha erro
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
		
		//Tratamento caso o usuario esqueça de digitar algum campo
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
		
		//Caso tudo esteja correto a servlet irá setar o dados
		//no cadastro instanciado no começo do codigo
		c.setNome(nome);
		c.setSobrenome(sobrenome);
		c.setCep(cep);
		c.setEndereco(endereco);
		c.setCidade(json.get("localidade").toString());
		c.setEstado(json.get("uf").toString());
		c.setRg(rg);
		c.setCpf(cpf);
		c.setTitulo(titulo);
		
		//Verifica se o usuario já foi cadastrado
		//Caso tenha sido, o cadastro não é efetuado
		if(b.contem(c)) {
			out.println("<body><div class=\"w3-panel w3-yellow w3-card-4 w3-margin\">\n" + 
					"  <h3>Cadastro não efetuado!</h3>\n" + 
					"  <p>Desculpe, dados já cadastrados.</p>\n" + 
					"</div> ");
			out.println("<a href=http://localhost:8080/sistema-inscricao/formInscricao.html class=\"w3-btn w3-green w3-round w3-margin\">Voltar</a> ");
			out.println("</body></html>");
			return;
		}else {
			b.adicionar(c);
		}
		out.println("<title>Sucesso no Cadastro</title></head>");
		out.println("<body><div class=\"w3-panel w3-green\">\n" + 
				"  <h3>Cadastro Efetuado com Sucesso!</h3>\n" + 
				"  <p>Para um novo cadastro click "
				+ "<a href=http://localhost:8080/sistema-inscricao/formInscricao.html>aqui</a>.</p>\n" + 
				"</div> ");
		
		out.println("<a href=mostrarCadastro>"
				+ "<input class=\"w3-btn w3-green w3-round w3-margin\" value=\"Cadastrados\">"
				+ "</a>");
		
		out.println("</body></html>");
	}

}
