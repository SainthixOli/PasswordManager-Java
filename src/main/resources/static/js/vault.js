function showToast(message) {
    const toast = document.getElementById('toast');
    toast.innerText = message;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 3000);
}

// Navigation Logic
function showSection(sectionId, linkElement) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(el => el.classList.remove('active'));
    // Show target
    document.getElementById(sectionId).classList.add('active');

    // Update active link
    document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
    linkElement.classList.add('active');
}

// Filter Logic
function filterPasswords() {
    const input = document.getElementById('searchInput');
    const filter = input.value.toLowerCase();
    const grid = document.getElementById('passwordGrid');
    const items = grid.getElementsByClassName('password-item');

    for (let i = 0; i < items.length; i++) {
        const text = items[i].getElementsByTagName('h3')[0].innerText.toLowerCase();
        if (text.indexOf(filter) > -1) {
            items[i].style.display = "";
        } else {
            items[i].style.display = "none";
        }
    }
}

// Async Actions
async function copyPassword(serviceName) {
    try {
        const response = await fetch('/api/decrypt', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ serviceName: serviceName })
        });

        const data = await response.json();

        if (response.ok) {
            navigator.clipboard.writeText(data.password);
            showToast('Senha copiada para a área de transferência!');
        } else {
            showToast('Erro: ' + (data.error || 'Falha ao copiar'));
        }
    } catch (e) {
        showToast('Erro de conexão');
    }
}

async function showPassword(serviceName, btnElement) {
    const parent = btnElement.parentElement.parentElement;
    const span = parent.querySelector('.service-info span:last-child');

    if (span.innerText !== '*****') {
        span.innerText = '*****';
        btnElement.innerText = '👁️';
        return;
    }

    try {
        const response = await fetch('/api/decrypt', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ serviceName: serviceName })
        });

        const data = await response.json();

        if (response.ok) {
            span.innerText = data.password;
            btnElement.innerText = '🔒';
            setTimeout(() => {
                span.innerText = '*****';
                btnElement.innerText = '👁️';
            }, 10000);
        } else {
            showToast('Erro: ' + (data.error || 'Falha ao revelar'));
        }
    } catch (e) {
        showToast('Erro de conexão');
    }
}

// Modal Logic
function openAddModal() {
    document.getElementById('addModal').classList.add('active');
    document.getElementById('newServiceName').focus();
}

function closeAddModal() {
    document.getElementById('addModal').classList.remove('active');
    document.getElementById('addForm').reset();
}

document.getElementById('addModal').addEventListener('click', (e) => {
    if (e.target.id === 'addModal') closeAddModal();
});

async function addPassword(event) {
    event.preventDefault();
    const serviceName = document.getElementById('newServiceName').value;
    const password = document.getElementById('newPassword').value;

    try {
        const response = await fetch('/api/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ serviceName, password })
        });

        if (response.ok) {
            showToast('Senha salva com sucesso!');
            closeAddModal();
            setTimeout(() => window.location.reload(), 1000);
        } else {
            const data = await response.json();
            showToast('Erro: ' + (data.error || 'Falha ao salvar'));
        }
    } catch (e) {
        showToast('Erro de conexão');
    }
}

async function deletePassword(serviceName) {
    if (!confirm('Tem certeza que deseja remover a senha de "' + serviceName + '"?')) return;

    try {
        const response = await fetch('/api/delete', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ serviceName })
        });

        if (response.ok) {
            showToast('Senha removida.');
            setTimeout(() => window.location.reload(), 1000);
        } else {
            showToast('Erro ao remover');
        }
    } catch (e) {
        showToast('Erro de conexão');
    }
}

// --- ADVANCED GENERATOR LOGIC ---
function generateAdvancedPassword() {
    const length = document.getElementById('pwdLength').value;
    const useUpper = document.getElementById('useUpper').checked;
    const useLower = document.getElementById('useLower').checked;
    const useNumbers = document.getElementById('useNumbers').checked;
    const useSymbols = document.getElementById('useSymbols').checked;

    const upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const lowerChars = "abcdefghijklmnopqrstuvwxyz";
    const numberChars = "0123456789";
    const symbolChars = "!@#$%^&*()_+~`|}{[]:;?><,./-=";

    let chars = "";
    if (useUpper) chars += upperChars;
    if (useLower) chars += lowerChars;
    if (useNumbers) chars += numberChars;
    if (useSymbols) chars += symbolChars;

    if (chars === "") {
        document.getElementById('generatedPassword').value = "Selecione uma opção!";
        return;
    }

    let pass = "";
    // Ensure at least one of each selected type
    const securityBuffer = [];
    if (useUpper) securityBuffer.push(upperChars.charAt(Math.floor(Math.random() * upperChars.length)));
    if (useLower) securityBuffer.push(lowerChars.charAt(Math.floor(Math.random() * lowerChars.length)));
    if (useNumbers) securityBuffer.push(numberChars.charAt(Math.floor(Math.random() * numberChars.length)));
    if (useSymbols) securityBuffer.push(symbolChars.charAt(Math.floor(Math.random() * symbolChars.length)));

    for (let i = 0; i < length; i++) {
        // Use security buffer for first chars
        if (i < securityBuffer.length) {
            pass += securityBuffer[i];
        } else {
            pass += chars.charAt(Math.floor(Math.random() * chars.length));
        }
    }

    // Shuffle the result to mix the mandatory chars
    pass = pass.split('').sort(function () { return 0.5 - Math.random() }).join('');

    document.getElementById('generatedPassword').value = pass;
}

function copyGenerated() {
    const pass = document.getElementById('generatedPassword').value;
    if (pass) {
        navigator.clipboard.writeText(pass);
        showToast("Copiada!");
    }
}
