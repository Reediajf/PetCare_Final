// Sistema de Notificações WebSocket
let notificationSocket = null;
let notificationCounter = 0;

// Inicializar WebSocket para notificações
function initializeNotifications() {
    try {
        // Para desenvolvimento, simularemos notificações já que o WebSocket real precisa do backend
        // Em produção, substitua por:
        // notificationSocket = new WebSocket('ws://localhost:8080/ws');
        
        simulateNotifications();
        
        // Código real para WebSocket (comentado para desenvolvimento)
        /*
        notificationSocket = new WebSocket('ws://localhost:8080/ws');
        
        notificationSocket.onopen = function(event) {
            console.log('WebSocket conectado para notificações');
        };
        
        notificationSocket.onmessage = function(event) {
            showNotification(event.data, 'info');
        };
        
        notificationSocket.onclose = function(event) {
            console.log('WebSocket desconectado, tentando reconectar...');
            setTimeout(initializeNotifications, 5000);
        };
        */
        
    } catch (error) {
        console.error('Erro ao inicializar notificações:', error);
    }
}

// Simular notificações para demonstração
function simulateNotifications() {
    // Notificação de boas-vindas
    setTimeout(() => {
        showNotification('Bem-vindo ao PetSchedule! 🐾', 'success');
    }, 2000);
    
    // Notificações periódicas
    setInterval(() => {
        const messages = [
            'Lembrete: Consulta do Thor agendada para amanhã! 🐕',
            'Vacinação do Luna está em dia! ✅',
            'Novo horário disponível para banho & tosa! 🛁',
            'Não se esqueça do check-up mensal do seu pet! 🩺'
        ];
        const randomMessage = messages[Math.floor(Math.random() * messages.length)];
        showNotification(randomMessage, 'info');
    }, 30000); // A cada 30 segundos
}

// Função para exibir notificações
function showNotification(message, type = 'info') {
    const container = document.getElementById('notificationContainer');
    if (!container) return;
    
    notificationCounter++;
    const notificationId = `notification-${notificationCounter}`;
    
    const notification = document.createElement('div');
    notification.id = notificationId;
    notification.className = `notification ${type}`;
    
    // Ícone baseado no tipo
    let icon = '💡';
    if (type === 'success') icon = '✅';
    if (type === 'error') icon = '❌';
    if (type === 'warning') icon = '⚠️';
    
    notification.innerHTML = `
        <span class="notification-icon">${icon}</span>
        <span class="notification-message">${message}</span>
        <button class="notification-close" onclick="closeNotification('${notificationId}')">&times;</button>
    `;
    
    container.appendChild(notification);
    
    // Auto-remover após 5 segundos
    setTimeout(() => {
        closeNotification(notificationId);
    }, 5000);
}

// Função para fechar notificação
function closeNotification(id) {
    const notification = document.getElementById(id);
    if (notification) {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            notification.remove();
        }, 300);
    }
}

// CSS para animação de saída
const style = document.createElement('style');
style.textContent = `
    @keyframes slideOutRight {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
    
    .notification-close {
        background: none;
        border: none;
        font-size: 1.2rem;
        cursor: pointer;
        color: var(--text-light);
        margin-left: auto;
    }
    
    .notification-icon {
        font-size: 1.2rem;
    }
`;
document.head.appendChild(style);

// Enviar notificação de teste (para integração com o backend)
async function sendTestNotification(message) {
    try {
        const response = await fetch(`${API_BASE_URL}/api/notify`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(message)
        });
        
        if (response.ok) {
            console.log('Notificação enviada com sucesso');
        }
    } catch (error) {
        console.error('Erro ao enviar notificação:', error);
    }
}