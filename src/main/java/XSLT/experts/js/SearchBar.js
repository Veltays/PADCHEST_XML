// =======================================================
// SEARCH BAR (recherche par colonne)
// =======================================================

let allRowsBackup = []; // On garde une copie des lignes originales

// -------------------------------------------------------
// Charger les colonnes dans le select
// -------------------------------------------------------
function setupSearchBar() {
    const table = document.getElementById("padchest-table");
    if (!table) return;

    const headers = table.querySelectorAll("th");
    const select = document.getElementById("search-column");

    // Remplir le select avec les noms des colonnes
    for (let i = 0; i < headers.length; i++) {
        const th = headers[i];
        const option = document.createElement("option");

        option.value = i;
        option.textContent = th.textContent;

        select.appendChild(option);
    }

    // garder une copie initiale des lignes
    const rows = table.querySelectorAll("tbody tr");
    allRowsBackup = Array.from(rows);

    // Bouton "Rechercher"
    document.getElementById("search-btn").addEventListener("click", function () {
        const col = parseInt(document.getElementById("search-column").value);
        const text = document.getElementById("search-input").value.toLowerCase();

        if (isNaN(col) || text.trim() === "") {
            alert("Choisis une colonne + texte de recherche !");
            return;
        }

        applySearch(col, text);
    });

    // Bouton reset
    document.getElementById("search-reset").addEventListener("click", resetSearch);
}


// -------------------------------------------------------
// Filtrer les lignes selon colonne + texte
// -------------------------------------------------------
function applySearch(colIndex, searchText) {
    const table = document.getElementById("padchest-table");
    const tbody = table.querySelector("tbody");

    // Vider le tableau
    tbody.innerHTML = "";

    // Filtrer
    const filtered = allRowsBackup.filter(row => {
        const cell = row.children[colIndex];
        if (!cell) return false;

        return cell.textContent.toLowerCase().includes(searchText);
    });

    // Afficher résultats
    filtered.forEach(row => tbody.appendChild(row));

    // mettre à jour pagination
    if (typeof setupPagination === "function") {
        setupPagination();
    }
}


// -------------------------------------------------------
// Réinitialiser la recherche
// -------------------------------------------------------
function resetSearch() {
    const table = document.getElementById("padchest-table");
    const tbody = table.querySelector("tbody");

    tbody.innerHTML = "";

    // Remettre les lignes originales
    allRowsBackup.forEach(r => tbody.appendChild(r));

    document.getElementById("search-input").value = "";
    document.getElementById("search-column").value = "";

    // Remettre la pagination à jour
    if (typeof setupPagination === "function") {
        setupPagination();
    }
}


// -------------------------------------------------------
// Détecter que le tableau XSLT est prêt
// -------------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {

    const observer = new MutationObserver(function () {
        const tableReady = document.getElementById("padchest-table");

        if (tableReady) {
            setupSearchBar();
            observer.disconnect();
        }
    });

    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
});

