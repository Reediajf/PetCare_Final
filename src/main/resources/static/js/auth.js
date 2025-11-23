

// Configuração da API
const API_BASE_URL = 'http://localhost:8080';

// Armazenamento do token
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser'));

// Função para fazer login
async function login(email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                senha: password
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Credenciais inválidas');
        }

        const token = await response.text();
        authToken = token.replace('Bearer ', '');

        // Salvar token no localStorage
        localStorage.setItem('authToken', authToken);

        // Buscar informações do usuário
        await fetchUserInfo(email);

        showNotification('Login realizado com sucesso!', 'success');

        // Redirecionar para o calendário após 1 segundo
        setTimeout(() => {
            window.location.href = 'calendar.html';
        }, 1000);

    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// Função para registrar usuário
async function registerUser(name, email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/usuarios`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                nome: name,
                email: email,
                senha: password
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao criar conta');
        }

        const userData = await response.json();
        showNotification('Conta criada com sucesso! Faça login para continuar.', 'success');

        // Voltar para o formulário de login
        showForm('loginForm');

    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// Função para buscar informações do usuário
async function fetchUserInfo(email) {
    try {
        const response = await fetch(`${API_BASE_URL}/usuarios/email/${encodeURIComponent(email)}`, {
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (response.ok) {
            const user = await response.json();
            currentUser = user;
            localStorage.setItem('currentUser', JSON.stringify(user));
        }
    } catch (error) {
        console.error('Erro ao buscar informações do usuário:', error);
    }
}

// Função para verificar autenticação
function checkAuth() {
    if (!authToken && !window.location.href.includes('login.html') && !window.location.href.includes('index.html')) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

// Função para fazer logout
function logout() {
    authToken = null;
    currentUser = null;
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    window.location.href = 'login.html';
}

// Função para fazer requisições autenticadas
async function authenticatedFetch(url, options = {}) {
    if (!checkAuth()) {
        throw new Error('Não autenticado');
    }

    const defaultOptions = {
        headers: {
            'Authorization': `Bearer ${authToken}`,
            'Content-Type': 'application/json',
            ...options.headers
        }
    };

    console.log(`Fetch: ${API_BASE_URL}${url}`, options);

    const response = await fetch(`${API_BASE_URL}${url}`, { ...defaultOptions, ...options });

    console.log(`Response: ${response.status} for ${url}`);

    if (response.status === 401) {
        logout();
        throw new Error('Sessão expirada');
    }

    return response;
}

// Verificar autenticação ao carregar a página
document.addEventListener('DOMContentLoaded', function() {
    if (window.location.href.includes('calendar.html')) {
        if (!checkAuth()) {
            return;
        }
        // Testar conexão com API
        testAPIConnection();
    }
});

// Testar conexão com API
async function testAPIConnection() {
    try {
        const response = await fetch(`${API_BASE_URL}/agendamentos`, {
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });
        console.log('Teste de conexão API:', response.status);
    } catch (error) {
        console.error('Erro na conexão com API:', error);
    }


}