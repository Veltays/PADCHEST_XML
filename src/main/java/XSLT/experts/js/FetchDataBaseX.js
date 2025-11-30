// =======================================================
// CONFIG AVEC PROXY
// =======================================================

let BASEX_URL = "http://localhost:3001/xquery";   // PROXY NODE

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
                let $res :=
                  for $label in /Images/image/Labels/Label/text()
                  group by $name := $label
                  let $count := count($label)
                  order by $count descending
                  return
                    <label>
                      <name>{$name}</name>
                      <count>{$count}</count>
                    </label>
                return subsequence($res, 1, 10)
          ]]>
          </text>
        </query>
    `;

    try {
        const response = await fetch(BASEX_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/xml",
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
// INITIALISATION — ON GARDE EXACTEMENT TON CODE
// =======================================================

document.addEventListener("DOMContentLoaded", async function () {

    const btnLoc = document.getElementById("NB_OF_LOC_RIGHT");
    const btnLabels = document.getElementById("NB_OF_MOST_SEEN_LABEL");

    if (btnLoc) btnLoc.addEventListener("click", fetchLocRight);
    if (btnLabels) btnLabels.addEventListener("click", fetchMostSeenLabels);
});
