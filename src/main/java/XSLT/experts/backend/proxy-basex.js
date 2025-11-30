import express from "express";
import fetch from "node-fetch";
import cors from "cors";
import fs from "fs";
import path from "path";

// ======================================================================
// LECTURE DE project.properties
// ======================================================================

const __dirname = path.resolve();

function loadProperties() {
    const propsPath = path.join(__dirname, "../../../../resources/project.properties");

    if (!fs.existsSync(propsPath)) {
        console.error("Fichier project.properties introuvable :", propsPath);
        process.exit(1);
    }

    const raw = fs.readFileSync(propsPath, "utf-8");
    const lines = raw.split("\n");

    const config = {};

    for (let line of lines) {
        line = line.trim();
        if (line === "" || line.startsWith("#")) continue;

        const parts = line.split("=");
        if (parts.length === 2) {
            const key = parts[0].trim();
            const value = parts[1].trim();
            config[key] = value;
        }
    }

    console.log("📄 project.properties chargé :", config);
    return config;
}

const CONFIG = loadProperties();

// ======================================================================
// CONFIG BASEx (depuis project.properties)
// ======================================================================

const BASEX_URL = CONFIG["basex.url"];
const USERNAME = CONFIG["basex.username"];
const PASSWORD = CONFIG["basex.password"];

function getAuthHeader() {
    return (
        "Basic " + Buffer.from(`${USERNAME}:${PASSWORD}`).toString("base64")
    );
}

// ======================================================================
// SERVEUR PROXY NODE
// ======================================================================

const app = express();
app.use(express.text({ type: "*/*" }));
app.use(cors()); // Autorise tout

// ----------- ROUTE PROXY --------------
app.post("/xquery", async (req, res) => {
    try {
        const basexResponse = await fetch(BASEX_URL, {
            method: "POST",
            headers: {
                "Authorization": getAuthHeader(),
                "Content-Type": "application/xml",
            },
            body: req.body,
        });

        const text = await basexResponse.text();
        res.send(text);

    } catch (err) {
        console.error("Erreur proxy:", err);
        res.status(500).send("Erreur proxy vers BaseX");
    }
});

// --------------------------------------

app.listen(3001, () => {
    console.log("🟢 Proxy BaseX actif sur http://localhost:3001");
});
