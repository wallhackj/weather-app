// navigation.js

function navigateTo(pageUrl) {
    window.location.href = pageUrl;
}

function logout() {
    fetch('/logout', {
        method: 'GET', // sau 'POST' în funcție de cum este implementat pe server
        credentials: 'same-origin' // asigură-te că trimiti cookie-urile pentru a gestiona sesiunile
    })
        .then(response => {
            if (response.ok) {
                // Redirect user to login page or any other page after logout
                window.location.href = '/login'; // redirecționează la pagina de login după deconectare
            } else {
                console.error('Logout failed'); // afișează un mesaj de eroare în caz de problemă
            }
        })
        .catch(error => {
            console.error('Error during logout:', error); // gestionează erorile de rețea sau de alt tip
        });
}

