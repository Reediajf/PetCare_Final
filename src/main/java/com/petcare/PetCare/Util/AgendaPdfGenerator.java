package com.petcare.PetCare.Util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.Service.AnimalService;
import com.petcare.PetCare.Service.MedicamentoService;
import com.petcare.PetCare.Service.TutorService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AgendaPdfGenerator {

    private final TutorService tutorService;
    private final AnimalService animalService;
    private final MedicamentoService medicamentoService;

    public AgendaPdfGenerator(TutorService tutorService, AnimalService animalService, MedicamentoService medicamentoService) {
        this.tutorService = tutorService;
        this.animalService = animalService;
        this.medicamentoService = medicamentoService;
    }

    public ByteArrayInputStream gerarPdf(List<AgendaDTO> agendamentos) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document document = new Document(pdf);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // ======================================================
        // TÍTULO
        // ======================================================
        Paragraph titulo = new Paragraph("Relatório de Agendamentos - PetCare")
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);

        Paragraph linhaDecorativa = new Paragraph("______________________________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(78, 205, 196))
                .setMarginBottom(20);

        document.add(titulo);
        document.add(linhaDecorativa);

        // ======================================================
        // CONFIG TABELA
        // ======================================================
        float[] colWidths = {40, 140, 140, 120, 120, 300};
        Table tabela = new Table(colWidths);
        tabela.setWidth(UnitValue.createPercentValue(100));

        DeviceRgb headerColor = new DeviceRgb(78, 205, 196);

        tabela.addHeaderCell(novoHeader("ID", headerColor));
        tabela.addHeaderCell(novoHeader("Tutor", headerColor));
        tabela.addHeaderCell(novoHeader("Pet", headerColor));
        tabela.addHeaderCell(novoHeader("Data", headerColor));
        tabela.addHeaderCell(novoHeader("Medicamento", headerColor));
        tabela.addHeaderCell(novoHeader("Observação", headerColor));

        int linha = 0;

        for (AgendaDTO dto : agendamentos) {

            String tutorNome = dto.getTutorId() != null ?
                    tutorService.buscarPorId(dto.getTutorId()).getNome() : "-";

            String animalNome = dto.getAnimalId() != null ?
                    animalService.buscarPorID(dto.getAnimalId()).getNome() : "-";

            String medicamento = dto.getMedicamentoId() != null ?
                    medicamentoService.buscarPorID(dto.getMedicamentoId()).getNome() : "-";

            String data = dto.getDataInicio() != null ?
                    dto.getDataInicio().format(formatter) : "-";

            // Remove quebras de linha
            String observacao = dto.getObservacao() != null ?
                    dto.getObservacao().replace("\n", " ").replace("\r", " ") : "-";

            // Limitar textos longos
            tutorNome = limitarTexto(tutorNome, 30);
            animalNome = limitarTexto(animalNome, 30);
            medicamento = limitarTexto(medicamento, 25);
            observacao = limitarTexto(observacao, 60);

            // Linhas zebrada
            DeviceRgb bg = (linha % 2 == 0)
                    ? new DeviceRgb(248, 248, 248)
                    : (DeviceRgb) ColorConstants.WHITE;

            tabela.addCell(novaCell(dto.getId(), bg));
            tabela.addCell(novaCell(tutorNome, bg));
            tabela.addCell(novaCell(animalNome, bg));
            tabela.addCell(novaCell(data, bg));
            tabela.addCell(novaCell(medicamento, bg));
            tabela.addCell(novaCell(observacao, bg));

            linha++;
        }

        document.add(tabela);

        // ======================================================
        // RODAPÉ
        // ======================================================
        int paginas = pdf.getNumberOfPages();
        for (int i = 1; i <= paginas; i++) {
            document.showTextAligned(
                    new Paragraph(String.format("Página %d de %d", i, paginas))
                            .setFontSize(10)
                            .setFontColor(ColorConstants.GRAY),
                    550, 20, i,
                    TextAlignment.RIGHT,
                    VerticalAlignment.BOTTOM,
                    0
            );
        }

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // ======================================================
    // MÉTODOS AUXILIARES
    // ======================================================

    private static Cell novoHeader(String texto, DeviceRgb bgColor) {
        Cell cell = new Cell();
        cell.add(new Paragraph(texto)
                .setBold()
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
        );
        cell.setBackgroundColor(bgColor);
        cell.setPadding(8);
        cell.setBorder(new SolidBorder(ColorConstants.GRAY, 0.5f));
        return cell;
    }

    private static Cell novaCell(Object valor, DeviceRgb bgColor) {
        Cell cell = new Cell()
                .add(new Paragraph(valor != null ? valor.toString() : "-"))
                .setPadding(6)
                .setBackgroundColor(bgColor)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.3f));

        return cell;
    }

    private String limitarTexto(String texto, int max) {
        if (texto == null) return "-";
        return texto.length() > max ? texto.substring(0, max) + "..." : texto;
    }
}
