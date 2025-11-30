package com.petcare.PetCare.Service;

import com.petcare.PetCare.Model.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TutorService tutorService;

    public void enviarEmailAgendamento(Agenda agenda) {

        String emailTutor = tutorService.buscarEmail(agenda.getTutor().getId());

        String mensagemHtml = montarEmailAgendamento(agenda);

        emailService.sendHtmlEmail(
                emailTutor,
                "Agendamento confirmado",
                mensagemHtml
        );
    }

    private String montarEmailAgendamento(Agenda agenda) {
        return """
        <div style="font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4;">
            
            <div style="max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 8px;">
                
                <h2 style="text-align: center; color: #4ECDC4;">
                    🐾 Confirmação de Agendamento – PetCare
                </h2>

                <p style="font-size: 16px; color: #333;">
                    Olá <strong>%s</strong>,
                </p>

                <p style="font-size: 16px; color: #333;">
                    Seu agendamento foi realizado com sucesso! 🎉  
                    Aqui estão os detalhes:
                </p>

                <table style="width: 100%%; border-collapse: collapse; margin-top: 20px;">
                    <tr><td><strong>Animal:</strong></td><td>%s</td></tr>
                    <tr><td><strong>Medicamento:</strong></td><td>%s</td></tr>
                    <tr><td><strong>Data:</strong></td><td>%s</td></tr>
                    <tr><td><strong>Observações:</strong></td><td>%s</td></tr>
                </table>

                <p style="font-size: 14px; text-align:center; margin-top: 20px; color:#777;">
                    PetCare © %s
                </p>

            </div>
        </div>
        """.formatted(
                agenda.getTutor().getNome(),
                agenda.getAnimal().getNome(),
                agenda.getMedicamento().getNome(),
                agenda.getDataInicio(),
                agenda.getObservacao(),
                java.time.Year.now()
        );
    }
}


