let currentPage = 1;
const rowsPerPage = 20;
let table = null;
let rows = [];

// =======================================================
// INIT PAGINATION
// =======================================================
function setupPagination() {
    table = document.querySelector("#padchest-table tbody");

    // Si le tableau n'est pas trouvé → on arrête
    if (!table) {
        console.error("Erreur : tbody introuvable");
        return;
    }

    rows = Array.from(table.querySelectorAll("tr"));

    // Afficher la première page
    showPage(1);
}

// =======================================================
// AFFICHER UNE PAGE
// =======================================================
function showPage(pageNumber) {
    currentPage = pageNumber;

    let start = (pageNumber - 1) * rowsPerPage;
    let end = start + rowsPerPage;

    // Afficher/cache chaque ligne selon l'intervalle
    for (let i = 0; i < rows.length; i++) {
        if (i >= start && i < end) {
            rows[i].style.display = "";
        } else {
            rows[i].style.display = "none";
        }
    }

    // Informations sur la page
    let totalPages = Math.ceil(rows.length / rowsPerPage);
    let pageInfo = document.getElementById("page-info");

    if (pageInfo) {
        pageInfo.textContent = "Page " + currentPage + " / " + totalPages;
    }
}

// =======================================================
// BOUTON PAGE SUIVANTE
// =======================================================
function nextPage() {
    let totalPages = Math.ceil(rows.length / rowsPerPage);

    if (currentPage < totalPages) {
        showPage(currentPage + 1);
    }
}

// =======================================================
// BOUTON PAGE PRÉCÉDENTE
// =======================================================
function prevPage() {
    if (currentPage > 1) {
        showPage(currentPage - 1);
    }
}

// =======================================================
// TRI DES COLONNES
// =======================================================
function setupSorting() {
    let headers = document.querySelectorAll("th");

    if (!headers.length) {
        console.error("Aucun <th> trouvé pour le tri");
        return;
    }

    headers.forEach((header, index) => {
        header.addEventListener("click", function () {

            // Tri basique, A → Z
            rows.sort(function(a, b) {
                let textA = a.children[index].innerText;
                let textB = b.children[index].innerText;

                return textA.localeCompare(textB);
            });

            // Réinsérer chaque ligne (ordre trié)
            for (let r of rows) {
                table.appendChild(r);
            }

            // Après tri, revenir à la page 1
            showPage(1);
        });
    });
}


// =======================================================
// BOUTONS
// =======================================================
document.getElementById("prev-btn").addEventListener("click", prevPage);
document.getElementById("next-btn").addEventListener("click", nextPage);


// =======================================================
// Rajouter une barre de recherche
// =======================================================

// =======================================================
// maybe des graphique si possible jsp et si le temp
// =======================================================

