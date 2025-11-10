package com.petcare.PetCare.Service;

import com.petcare.PetCare.DTO.MedicamentoDTO;
import com.petcare.PetCare.Model.Medicamento;
import com.petcare.PetCare.Repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class MedicamentoService {
    @Autowired
    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public Medicamento cadastrar(Medicamento novoMedicamento) {
        return medicamentoRepository.save(novoMedicamento);
    }

    @Transactional
    public Medicamento atualizar(Long id, Medicamento medicamentoAtualizado){
        var valido = medicamentoRepository.findById(id);

        if(valido.isEmpty()) throw new RuntimeException("Medicamento não encontrado");
        Medicamento medicamento = valido.get();
        medicamento.setNome(medicamentoAtualizado.getNome());
        medicamento.setDosagem(medicamentoAtualizado.getDosagem());
        medicamento.setViaAdministracao(medicamentoAtualizado.getViaAdministracao());

        return medicamentoRepository.save(medicamento);
    }

    @Transactional
    public void deletar(Long id) {
        if(!medicamentoRepository.existsById(id)){
            throw new RuntimeException("Medicamento não econtrado");
        }
        medicamentoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Medicamento> listarMedicamentos(){
        return medicamentoRepository.findAll();
    }

    public Medicamento converterDTOparaEntidade(MedicamentoDTO dto){
        Medicamento medicamento = new Medicamento();
        medicamento.setNome(dto.getNome());
        medicamento.setDosagem(dto.getDosagem());
        medicamento.setViaAdministracao(dto.getViaAdministracao());

        return medicamento;
    }

}
