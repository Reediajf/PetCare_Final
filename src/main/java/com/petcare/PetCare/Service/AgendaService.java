package com.petcare.PetCare.Service;

import com.petcare.PetCare.DTO.AgendaDTO;
import com.petcare.PetCare.DTO.AgendaResponseDTO;
import com.petcare.PetCare.Util.HorarioOcupadoException;
import com.petcare.PetCare.Model.*;
import com.petcare.PetCare.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    NotificationService notificationService;

    public Agenda criarAgendamento(AgendaDTO dto) {

        boolean horarioOcupado = agendaRepository.existsByDataInicio(dto.getDataInicio());
        if (horarioOcupado) {
            throw new HorarioOcupadoException("Já existe um agendamento para esse horário.");
        }
        Agenda agenda = converterDTOParaEntidade(dto);

        notificationService.enviarEmailAgendamento(agenda);

        return agendaRepository.save(agenda);
    }

    public java.util.List<Agenda> listarAgendamentos() {
        return agendaRepository.findAll();
    }

    public Agenda atualizar(Long id, Agenda agendaAtualizada) {

        Optional<Agenda> agendaExistente = agendaRepository.findById(id);
        if (agendaExistente.isEmpty()) {
            throw new EntityNotFoundException("Agendamento não encontrado com ID: " + id);
        }

        Agenda agenda = agendaExistente.get();


        if (agendaRepository.existsByDataInicio(agendaAtualizada.getDataInicio())
                && !agenda.getDataInicio().equals(agendaAtualizada.getDataInicio())) {

            throw new HorarioOcupadoException("Não é possível atualizar: horário já ocupado por outro agendamento.");
        }

        agenda.setTutor(agendaAtualizada.getTutor());
        agenda.setAnimal(agendaAtualizada.getAnimal());
        agenda.setMedicamento(agendaAtualizada.getMedicamento());
        agenda.setDataInicio(agendaAtualizada.getDataInicio());
        agenda.setObservacao(agendaAtualizada.getObservacao());

        return agendaRepository.save(agenda);
    }

    public Agenda converterDTOParaEntidade(AgendaDTO dto) {

        Agenda agenda = new Agenda();

        if (dto.getId() != null) {
            agenda.setId(dto.getId());
        }

        agenda.setDataInicio(dto.getDataInicio());
        agenda.setObservacao(dto.getObservacao());

        if (dto.getTutorId() != null) {
            Tutor tutor = tutorRepository.findById(dto.getTutorId())
                    .orElseThrow(() -> new EntityNotFoundException("Tutor não encontrado"));
            agenda.setTutor(tutor);
        }

        if (dto.getAnimalId() != null) {
            Animal animal = animalRepository.findById(dto.getAnimalId())
                    .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));
            agenda.setAnimal(animal);
        }

        if (dto.getMedicamentoId() != null) {
            Medicamento medicamento = medicamentoRepository.findById(dto.getMedicamentoId())
                    .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrado"));
            agenda.setMedicamento(medicamento);
        }

        return agenda;
    }

    public AgendaResponseDTO toResponseDTO(Agenda agenda) {
        AgendaResponseDTO dto = new AgendaResponseDTO();

        dto.setId(agenda.getId());
        dto.setTutorNome(agenda.getTutor().getNome());
        dto.setAnimalNome(agenda.getAnimal().getNome());
        dto.setMedicamentoNome(agenda.getMedicamento().getNome());
        dto.setObservacao(agenda.getObservacao());
        dto.setDataInicio(agenda.getDataInicio());

        return dto;
    }


    @Transactional
    public void excluir(Long id) {
        agendaRepository.deleteById(id);
    }

    public Agenda buscarPorId(Long id) {
        agendaRepository.findById(id).orElseThrow();
        return agendaRepository.findById(id).get();
    }
}
