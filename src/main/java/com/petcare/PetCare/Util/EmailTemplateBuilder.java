package com.petcare.PetCare.Util;

public class EmailTemplateBuilder {

    public static String buildAgendamentoTemplate(
            String nomeTutor,
            String nomeAnimal,
            String nomeMedicamento,
            String dataInicio,
            String observacao
    ) {

        return """
                <div style="font-family: Arial, sans-serif; background-color:#f6f6f6; padding: 20px;">
                    <div style="max-width: 600px; margin:auto; background:white; border-radius:10px; padding:20px;
                                box-shadow:0 4px 10px rgba(0,0,0,0.1);">
                        
                        <h2 style="color:#2BBBAD; text-align:center;">Agendamento Confirmado 🐾</h2>

                        <p style="font-size:16px;">Olá <strong>%s</strong>,</p>

                        <p style="font-size:16px;">
                            Seu agendamento no <strong>PetCare</strong> foi registrado com sucesso!
                        </p>

                        <div style="margin-top:20px; padding:15px; border-left:4px solid #4ECDC4; background:#f0fffd;">
                            <p style="margin:5px 0;"><strong>Animal:</strong> %s</p>
                            <p style="margin:5px 0;"><strong>Medicamento:</strong> %s</p>
                            <p style="margin:5px 0;"><strong>Data e horário:</strong> %s</p>
                            <p style="margin:5px 0;"><strong>Observação:</strong> %s</p>
                        </div>

                        <p style="margin-top:20px; font-size:15px; color:#555;">
                            Caso precise alterar seu agendamento, entre em contato conosco.
                        </p>

                        <p style="margin-top:20px; text-align:center;">
                            <strong style="color:#2BBBAD;">Equipe PetCare 🐶🐱</strong>
                        </p>

                    </div>
                </div>
                """.formatted(
                nomeTutor,
                nomeAnimal,
                nomeMedicamento,
                dataInicio,
                observacao == null || observacao.isBlank() ? "Nenhuma" : observacao
        );
    }

}
