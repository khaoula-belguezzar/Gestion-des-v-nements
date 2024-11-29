document.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.card');

    // Ajout d'un effet de survol interactif
    cards.forEach(card => {
        card.addEventListener('mouseover', function () {
            card.style.transform = 'scale(1.05)';
            card.style.boxShadow = '0 8px 15px rgba(0, 0, 0, 0.1)';
        });

        card.addEventListener('mouseout', function () {
            card.style.transform = 'scale(1)';
            card.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
        });
    });

    // Exemple de fonction de clic pour afficher une alerte (ajustez selon vos besoins)
    cards.forEach(card => {
        card.addEventListener('click', function () {
            alert('Détails de l\'événement: ' + card.querySelector('.card-title').textContent);
        });
    });
});
