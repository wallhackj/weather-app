async function hashPassword(password) {
    const passwordBuffer = new TextEncoder().encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', passwordBuffer);
    const hashedPasswordArray = Array.from(new Uint8Array(hashBuffer));
    return hashedPasswordArray.map(byte => ('00' + byte.toString(16)).slice(-2)).join('');
}

async function submitForm() {
    const login = document.getElementById('login').value;
    const password = document.getElementById('password').value;
    const hashedPassword = await hashPassword(password);

    const formData = new URLSearchParams();
    formData.append('login', login);
    formData.append('password', hashedPassword);

    fetch('http://localhost:8082/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData.toString()
    })
        .then(response => response.json().then(data => ({ status: response.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                window.location.href = 'login.html';
            } else {
                console.error('Registration failed:', body);
                alert(`Error: ${body.message}`);
            }
        })
        .catch(error => {
            console.error('Error during registration:', error);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    getUrlParams();
});

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();
    submitForm();
});
