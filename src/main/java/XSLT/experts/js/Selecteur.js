// -------------------------------------------------------
// Création du panneau des colonnes
// -------------------------------------------------------
function setupColumnSelector() {
    const table = document.getElementById("padchest-table");
    if (table === null) {
        console.error("Table introuvable pour setupColumnSelector");
        return;
    }

    const headers = table.querySelectorAll("th");
    const selectorBox = document.getElementById("checkbox-columns");

    if (!selectorBox) {
        console.error("Container #checkbox-columns introuvable");
        return;
    }

    // On crée les checkbox
    for (let i = 0; i < headers.length; i++) {
        const th = headers[i];

        const label = document.createElement("label");
        const checkbox = document.createElement("input");

        checkbox.type = "checkbox";
        checkbox.checked = true;
        checkbox.dataset.col = i;

        checkbox.addEventListener("change", function () {

            console.log("Changement de colonne détecté pour la colonne " + this.dataset.col);
            const index = parseInt(this.dataset.col);
            const isVisible = this.checked;

            toggleColumn(index, isVisible);

        });

        label.appendChild(checkbox);
        label.append(" " + th.textContent);
        selectorBox.appendChild(label);
    }
}


// -------------------------------------------------------
// Masquer / afficher une colonne entière
// -------------------------------------------------------
function toggleColumn(colIndex, show) {
    const table = document.getElementById("padchest-table");

    if (table === null) {
        console.error("Impossible d'afficher / masquer la colonne");
        return;
    }

    const displayValue = show ? "" : "none";

    // 1) TH
    const headers = table.querySelectorAll("th");
    if (headers[colIndex]) {
        headers[colIndex].style.display = displayValue;
    }

    // 2) TD
    const rows = table.querySelectorAll("tbody tr");

    for (let r = 0; r < rows.length; r++) {
        const cell = rows[r].children[colIndex];
        if (cell) {
            cell.style.display = displayValue;
        }
    }
}


// -------------------------------------------------------
// Initialisation : attendre que le XSLT ait généré la table
// -------------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {

    const observer = new MutationObserver(function () {
        const tableReady = document.getElementById("padchest-table");

        if (tableReady) {
            setupColumnSelector();
            observer.disconnect();
        }
    });

    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
});
