package br.com.ftech.clinica.controller;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ftech.clinica.domain.Consulta;
import br.com.ftech.clinica.repository.ConsultaRepository;
import br.com.ftech.clinica.util.Mensagem;
import br.com.ftech.clinica.util.Mensagem.TipoMensagem;


@Controller
@RequestMapping("/consulta")
public class ConsultaController {
	
	@Autowired
	private ConsultaRepository consultaRepository;

	@RequestMapping(value="/agendar.do", method=RequestMethod.POST)
	public String agendar(Consulta consulta, String data, String hora, Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");		
		Date dataConsulta;
		try {
			Date parsed = format.parse(data);
			String dataFormatada = format2.format(parsed);
			System.out.println(dataFormatada);

			
			dataConsulta = sdf.parse(dataFormatada + " " + hora);
			consulta.setDataConsulta(dataConsulta);
			consultaRepository.salvaConsulta(consulta);
			model.addAttribute("mensagem", new Mensagem("Sucesso ao cadastrar a consulta", TipoMensagem.SUCESSO));
		} catch (ParseException e) {
			model.addAttribute("mensagem", new Mensagem("Erro ao fazer a convers�o de data/hora. Observe os padr�es a serem seguidos", TipoMensagem.ERRO));
		}
		
		return "forward:/preparaCadastroConsulta.do";
	}
	
	@RequestMapping(value="/detalharConsulta.do", method=RequestMethod.GET)
	public String detalhar(Integer idConsulta, Model model) {
		Consulta consulta = consultaRepository.recuperaConsulta(idConsulta);
		model.addAttribute("consulta", consulta);
		
		return "realizarAtendimento";
	}
	
	@RequestMapping(value="/atender.do", method=RequestMethod.POST)
	public String gravarAtendimento(Consulta consulta, Model model) {
		consulta.setDataAtendimento(new Date());
		consultaRepository.atualizaConsulta(consulta);
		model.addAttribute("mensagem", new Mensagem("Sucesso ao cadastrar o atendimento", TipoMensagem.SUCESSO));
		
		return "forward:/preparaCadastroAtendimento.do";
	}
	
	@RequestMapping(value="/listarPorPaciente.do", method=RequestMethod.GET)
	public @ResponseBody List<Consulta> listarPorPaciente(Integer idPaciente) {
		return consultaRepository.listarPorPaciente(idPaciente);
	}
}
