// =======================================================
// LECTURE DU FICHIER project.properties
// =======================================================

async function loadProperties() {
    const config = {};

    try {
        const response = await fetch("../../project.properties");

        if (!response.ok) {
            console.error("Impossible de charger project.properties");
            return null;
        }

        const text = await response.text();
        const lines = text.split("\n");

        // Lecture simple ligne par ligne
        for (let line of lines) {
            line = line.trim();

            // on skip les commentaires et lignes vides
            if (line.startsWith("#") || line === "") {
                continue;
            }

            // format : clé = valeur
            const parts = line.split("=");
            if (parts.length === 2) {
                const key = parts[0].trim();
                const value = parts[1].trim();
                config[key] = value;
            }
        }

        return config;

    } catch (err) {
        console.error("Erreur loadProperties()", err);
        return null;
    }
}


// =======================================================
// CONNEXION BASEx AVEC CONFIG
// =======================================================

let BASEX_URL = "";
let BASEx_USERNAME = "";
let BASEx_PASSWORD = "";

// Générer le header "Authorization: Basic"
function getAuthHeader() {
    const token = btoa(BASEx_USERNAME + ":" + BASEx_PASSWORD);
    return "Basic " + token;
}


// =======================================================
// 1) Requête : nombre de 'loc right'
// =======================================================
async function fetchLocRight() {
    const queryXML = `
        <query xmlns="http://basex.org/rest">
            <text>
                let $all-images := /Images/image
                let $filtered :=
                    for $image in $all-images
                    let $locs := $image/Localizations/Localization
                    where $locs = "loc right"
                    return $image
                return count($filtered)
            </text>
        </query>
    `;

    try {
        const response = await fetch(BASEX_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/xml",
                "Authorization": getAuthHeader(),
            },
            body: queryXML
        });

        const text = await response.text();
        alert("Nombre d'images contenant « loc right » : " + text);

    } catch (err) {
        alert("Erreur lors de la requête loc right.");
        console.error(err);
    }
}


// =======================================================
// 2) Requête : labels les plus fréquents
// =======================================================
async function fetchMostSeenLabels() {

    const queryXML = `
        <query xmlns="http://basex.org/rest">
        <text><![CDATA[
            let $all-images := /Images/image

            let $all-labels :=
                for $image in $all-images
                let $labels := $image/Labels/Label/text()
                return $labels

            let $unique-labels := distinct-values($all-labels)

            let $frequencies :=
                for $unique in $unique-labels
                let $count :=
                    count(
                        for $label in $all-labels
                        where $label = $unique
                        return $label
                    )
                order by $count descending
                return
                    <label>
                        <name>{$unique}</name>
                        <count>{$count}</count>
                    </label>

            return subsequence($frequencies, 1, 10)
        ]]></text>
        </query>
    `;

    try {
        const response = await fetch(BASEX_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/xml",
                "Authorization": getAuthHeader(),
            },
            body: queryXML
        });

        const xml = await response.text();
        console.log("Labels les plus fréquents :", xml);
        alert("Top 10 labels récupérés (voir console).");

    } catch (err) {
        alert("Erreur lors de la requête des labels.");
        console.error(err);
    }
}


// =======================================================
// INITIALISATION
// =======================================================

document.addEventListener("DOMContentLoaded", async function () {

    const props = await loadProperties();

    if (!props) {
        alert("Impossible de charger la configuration BaseX.");
        return;
    }

    BASEX_URL = props["basex.url"];
    BASEx_USERNAME = props["basex.username"];
    BASEx_PASSWORD = props["basex.password"];

    console.log("BaseX config chargée :", props);

    // Binder les boutons
    const btnLoc = document.getElementById("NB_OF_LOC_RIGHT");
    const btnLabels = document.getElementById("NB_OF_MOST_SEEN_LABEL");

    if (btnLoc) {
        btnLoc.addEventListener("click", fetchLocRight);
    }

    if (btnLabels) {
        btnLabels.addEventListener("click", fetchMostSeenLabels);
    }
});
