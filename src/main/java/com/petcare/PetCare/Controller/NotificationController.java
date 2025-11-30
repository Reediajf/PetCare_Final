package com.petcare.PetCare.Controller;

import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.DTO.AgendaResponseDTO;
import com.petcare.PetCare.Model.Agenda;
import com.petcare.PetCare.Service.AgendaService;
import com.petcare.PetCare.Service.EmailService;
import com.petcare.PetCare.Service.TutorService;
import com.petcare.PetCare.Util.EmailTemplateBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private AgendaService agendaService;

    @PostMapping("/sendmail")
    public ResponseEntity<?> sendMail(@RequestBody AgendaDTO agendaDTO) {

        try {

            Agenda agenda = agendaService.buscarPorId(agendaDTO.getId());

            AgendaResponseDTO dto = agendaService.toResponseDTO(agenda);


            String html = EmailTemplateBuilder.buildAgendamentoTemplate(
                    dto.getTutorNome(),
                    dto.getAnimalNome(),
                    dto.getMedicamentoNome(),
                    dto.getDataInicio().toString(),
                    dto.getObservacao()
            );

            emailService.sendHtmlEmail(
                    tutorService.buscarEmail(agenda.getTutor().getId()),
                    "PetCare - Seu agendamento foi confirmado!",
                    html
            );

            return ResponseEntity.ok("E-mail enviado!");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
