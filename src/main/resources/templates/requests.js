// Funcție pentru a calcula hash-ul SHA-256 al unei parole
async function hashPassword(password) {
    // Convertim parola într-un array de bytes (Uint8Array)
    const passwordBuffer = new TextEncoder().encode(password);

    // Obținem un obiect de hash SHA-256
    const hashBuffer = await crypto.subtle.digest('SHA-256', passwordBuffer);

    // Convertim rezultatul într-un șir hexazecimal
    const hashedPasswordArray = Array.from(new Uint8Array(hashBuffer));
    return hashedPasswordArray.map(byte => ('00' + byte.toString(16)).slice(-2)).join('');
}

// Exemplu de utilizare:
async function submitForm() {
    const login = document.getElementById('login').value;
    const password = document.getElementById('password').value;

    const hashedPassword = await hashPassword(password);

    const formData = {
        login: login,
        hashedPassword: hashedPassword
    };

    // Trimite cererea POST către server
    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                console.log('User registered successfully!');
                // Poți redirecționa utilizatorul sau face alte acțiuni după înregistrare
            } else {
                console.error('Registration failed');
            }
        })
        .catch(error => {
            console.error('Error during registration:', error);
        });
}

// Funcție pentru a obține parametrii URL
function getUrlParams() {
    const urlParams = new URLSearchParams(window.location.search);
    const loginParam = urlParams.get('login');
    if (loginParam) {
        document.getElementById('login').value = loginParam;
    }
}

// Verificăm dacă avem parametrii în URL la încărcarea paginii
document.addEventListener('DOMContentLoaded', function() {
    getUrlParams();
});

// Salvăm opțiunea Remember Me în cookie la submiterea formularului
document.getElementById('loginForm').addEventListener('submit', function() {
    const rememberMeCheckbox = document.getElementById('rememberMe');
    if (rememberMeCheckbox.checked) {
        // Salvăm valoarea într-un cookie
        document.cookie = `rememberMe=true; max-age=3600`; // Exemplu: cookie expiră după 1 oră
    } else {
        // Ștergem cookie-ul dacă Remember Me nu este bifat
        document.cookie = `rememberMe=; expires=Thu, 01 Jan 1970 00:00:00 UTC;`;
    }
});

