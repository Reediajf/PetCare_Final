// ===============================
// Variáveis Globais
// ===============================
let currentDate = new Date();
let appointments = [];
let tutors = [];
let pets = [];
let medicamentos = [];

// ===============================
// Utilidades
// ===============================
function showNotification(message, type = "success") {
    const container = document.getElementById("notificationContainer");
    const notif = document.createElement("div");
    notif.className = `notification ${type}`;
    notif.innerText = message;

    container.appendChild(notif);

    setTimeout(() => {
        notif.style.opacity = "0";
        setTimeout(() => notif.remove(), 300);
    }, 3000);
}

// ===============================
// API
// ===============================
async function api(url, method = "GET", body = null) {
    const options = {
        method,
        headers: { "Content-Type": "application/json" }
    };

    if (body) options.body = JSON.stringify(body);

    try {
        const res = await fetch(url, options);
        if (!res.ok) throw new Error(`Erro: ${res.status}`);
        return await res.json();
    } catch (e) {
        showNotification(e.message, "error");
        return null;
    }
}

// ===============================
// Carregamento Inicial
// ===============================
document.addEventListener("DOMContentLoaded", () => {
    loadAllData();
    setupButtons();
    renderCalendar();
});

// ===============================
// Carregar dados do backend
// ===============================
async function loadAllData() {
    tutors = await api("/tutores") || [];
    pets = await api("/animais") || [];
    medicamentos = await api("/medicamento") || [];
    appointments = await api("/agendamentos") || [];

    loadTutorSelect();
    loadPetsSelect();
    loadMedicamentoSelect();
    renderCalendar();
}

// ===============================
// Selects de Tutor/Pet/Medicamento
// ===============================
function loadTutorSelect() {
    const select = document.getElementById("tutorSelect");
    select.innerHTML = `<option value="">Selecione um tutor</option>`;
    tutors.forEach(t => {
        select.innerHTML += `<option value="${t.id}">${t.nome}</option>`;
    });
}

function loadPetsSelect() {
    const select = document.getElementById("petSelect");
    select.innerHTML = `<option value="">Selecione um pet</option>`;
    pets.forEach(p => {
        select.innerHTML += `<option value="${p.id}">${p.nome}</option>`;
    });
}

function loadMedicamentoSelect() {
    const select = document.getElementById("medicamentoSelect");
    select.innerHTML = `<option value="">Selecione um medicamento</option>`;
    medicamentos.forEach(m => {
        select.innerHTML += `<option value="${m.id}">${m.nome}</option>`;
    });
}

// ===============================
// Botões
// ===============================
function setupButtons() {
    document.getElementById("prevMonth").addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar();
    });

    document.getElementById("nextMonth").addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar();
    });

    document.getElementById("todayBtn").addEventListener("click", () => {
        currentDate = new Date();
        renderCalendar();
    });

    document.getElementById("newAppointment").addEventListener("click", () => openAppointmentModal());
    document.getElementById("btnRelatorio").onclick = () => openModal("relatorioModal");

    document.querySelectorAll(".close-btn, .cancel-btn").forEach(btn => {
        btn.addEventListener("click", closeAllModals);
    });
    document.getElementById("closeRelatorioModal").onclick = () => closeAllModals();

    document.getElementById("logoutBtn").addEventListener("click", () => {
        localStorage.removeItem("token");
        window.location.href = "index.html";
    });

    document.getElementById("btnCadastroTutor").onclick = () => openModal("tutorModal");
    document.getElementById("btnCadastroAnimal").onclick = () => openModal("animalModal");
    document.getElementById("btnCadastroMedicamento").onclick = () => openModal("medicamentoModal");
    document.getElementById("btnBaixarPdf").onclick = baixarPdf;

    document.getElementById("appointmentForm").addEventListener("submit", saveAppointment);
    document.getElementById("tutorForm").addEventListener("submit", saveTutor);
    document.getElementById("animalForm").addEventListener("submit", saveAnimal);
    document.getElementById("medicamentoForm").addEventListener("submit", saveMedicamento);
    document.getElementById("deleteAppointment").addEventListener("click", deleteAppointment);
}

// ===============================
// Renderizar Calendário
// ===============================
function renderCalendar() {
    const grid = document.getElementById("calendarGrid");
    grid.innerHTML = "";

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    document.getElementById("currentMonth").textContent =
        currentDate.toLocaleDateString("pt-BR", { month: "long", year: "numeric" });

    const firstDay = new Date(year, month, 1);
    const startDay = firstDay.getDay();

    const daysInMonth = new Date(year, month + 1, 0).getDate();

    // Dias vazios antes
    for (let i = 0; i < startDay; i++) {
        const div = document.createElement("div");
        div.className = "calendar-day other-month";
        grid.appendChild(div);
    }

    // Dias do mês
    for (let d = 1; d <= daysInMonth; d++) {
        const date = new Date(year, month, d);
        const div = document.createElement("div");
        div.className = "calendar-day";

        if (isToday(date)) div.classList.add("today");

        div.innerHTML = `<div class="calendar-day-number">${d}</div>`;

        // Exibir eventos
        appointments
            .filter(a => {
                if (!a || !a.dataInicio) return false;

                const eventDate = a.dataInicio.split("T")[0];
                const currentDateISO = date.toISOString().split("T")[0];

                return eventDate === currentDateISO;
            })
            .forEach(a => {
                const event = document.createElement("div");
                event.className = "calendar-event";

                const hora = a.dataInicio.split("T")[1].slice(0, 5);
                const pet = pets.find(p => p.id === a.animalId);
                const medicamento = medicamentos.find(m => m.id === a.medicamentoId);

                // MOSTRAR PET + MEDICAMENTO
                event.innerHTML = `
                    <strong>${pet?.nome ?? "Pet"}</strong>
                    <small>${medicamento?.nome ?? ""}</small>
                    <small>${hora}</small>
                `;

                event.onclick = () => openAppointmentModal(a.id);
                div.appendChild(event);
            });

        // Impedir conflito de clique entre evento e dia
        div.addEventListener("click", (e) => {
            if (e.target.classList.contains("calendar-event")) return;
            openAppointmentModal(null, date);
        });

        grid.appendChild(div);
    }
}

// ===============================
// Funções de Modal
// ===============================
function openModal(id) {
    document.getElementById(id).classList.add("active");
}

function closeAllModals() {
    document.querySelectorAll(".modal").forEach(m => m.classList.remove("active"));
}

// ===============================
// Modal de Agendamento
// ===============================
function openAppointmentModal(id = null, date = null) {
    const modal = document.getElementById("appointmentModal");
    modal.classList.add("active");

    const deleteBtn = document.getElementById("deleteAppointment");

    if (id) {
        const a = appointments.find(x => x.id === id);
        document.getElementById("modalTitle").innerText = "Editar Agendamento";

        document.getElementById("appointmentId").value = a.id;
        document.getElementById("tutorSelect").value = a.tutorId;
        document.getElementById("petSelect").value = a.animalId;
        document.getElementById("medicamentoSelect").value = a.medicamentoId;

        // Ajuste seguro para datetime-local
        document.getElementById("appointmentDate").value = a.dataInicio.slice(0, 16);

        document.getElementById("appointmentNotes").value = a.observacao || "";

        deleteBtn.style.display = "inline-block";
    } else {
        document.getElementById("modalTitle").innerText = "Novo Agendamento";
        document.getElementById("appointmentForm").reset();
        deleteBtn.style.display = "none";

        if (date) {
            const local = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
                .toISOString()
                .slice(0, 16);
            document.getElementById("appointmentDate").value = local;
        }
    }
}

// ===============================
// Salvar agendamento
// ===============================
async function saveAppointment(e) {
    e.preventDefault();

    const id = document.getElementById("appointmentId").value;

    const obj = {
        tutorId: parseInt(document.getElementById("tutorSelect").value),
        animalId: parseInt(document.getElementById("petSelect").value),
        medicamentoId: parseInt(document.getElementById("medicamentoSelect").value),
        dataInicio: document.getElementById("appointmentDate").value,
        observacao: document.getElementById("appointmentNotes").value
    };

    const url = id ? `/agendamentos/${id}` : "/agendamentos";

    const saved = await api(url, id ? "PUT" : "POST", obj);

    if (saved) {
        showNotification("Agendamento salvo com sucesso!");
        closeAllModals();
        loadAllData();
    }
}

// ===============================
// Remover agendamento
// ===============================
async function deleteAppointment() {
    const id = document.getElementById("appointmentId").value;
    if (!id) return;

    const res = await fetch(`/agendamentos/${id}`, { method: "DELETE" });

    if (res.ok) {
        showNotification("Agendamento excluído!", "success");
        closeAllModals();
        await loadAllData();  // recarrega tudo
        renderCalendar();     // força re-render
    } else {
        showNotification("Erro ao excluir agendamento", "error");
    }

}

// ===============================
// Cadastros (Tutor, Animal, Medicamento)
// ===============================
async function saveTutor(e) {
    e.preventDefault();
    const obj = {
        nome: document.getElementById("tutorNome").value,
        email: document.getElementById("tutorEmail").value
    };

    const saved = await api("/tutores", "POST", obj);
    if (saved) {
        showNotification("Tutor cadastrado!");
        closeAllModals();
        loadAllData();
    }
}

async function saveAnimal(e) {
    e.preventDefault();

    const obj = {
        nome: document.getElementById("animalNome").value,
        raca: document.getElementById("animalRaca").value,
        peso: parseFloat(document.getElementById("animalPeso").value),
        dataNascimento: document.getElementById("animalDataNascimento").value
    };

    const saved = await api("/animais", "POST", obj);
    if (saved) {
        showNotification("Animal cadastrado!");
        closeAllModals();
        loadAllData();
    }
}

async function saveMedicamento(e) {
    e.preventDefault();

    const obj = {
        nome: document.getElementById("medicamentoNome").value,
        dosagem: document.getElementById("medicamentoDosagem").value,
        via: document.getElementById("medicamentoVia").value
    };

    const saved = await api("/medicamento", "POST", obj);
    if (saved) {
        showNotification("Medicamento cadastrado!");
        closeAllModals();
        loadAllData();
    }
}

async function baixarPdf() {
    try {
        const response = await fetch("/agendamentos/pdf", { method: "GET" });

        if (!response.ok) throw new Error("Erro ao gerar PDF");

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = url;
        a.download = "agendamentos.pdf";
        document.body.appendChild(a);
        a.click();

        a.remove();
        window.URL.revokeObjectURL(url);

        showNotification("PDF gerado com sucesso!");
    } catch (e) {
        showNotification(e.message, "error");
    }

    window.addEventListener("click", function(event) {
        document.querySelectorAll(".modal.active").forEach(modal => {
            if (event.target === modal) {
                modal.classList.remove("active");
            }
        });
    });

}

// ===============================
// Helpers
// ===============================
function isToday(date) {
    const t = new Date();
    return (
        date.getDate() === t.getDate() &&
        date.getMonth() === t.getMonth() &&
        date.getFullYear() === t.getFullYear()
    );
}
