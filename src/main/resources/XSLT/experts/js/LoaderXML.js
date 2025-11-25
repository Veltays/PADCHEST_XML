async function loadXML(url) {
    const response = await fetch(url);
    const text = await response.text();
    return new window.DOMParser().parseFromString(text, "text/xml");
}


async function loadXSL(url) {
    const response = await fetch(url);
    const text = await response.text();
    return new window.DOMParser().parseFromString(text, "text/xml");
}


async function init() {
    const xml = await loadXML("../../PADCHEST.xml");
    const xsl = await loadXSL("./style.xsl");

    const xsltProcessor = new XSLTProcessor();
    xsltProcessor.importStylesheet(xsl);

    const fragment = xsltProcessor.transformToFragment(xml, document);
    document.getElementById("table-container").innerHTML = "";
    document.getElementById("table-container").appendChild(fragment);

    setupPagination();
    setupSorting();
}

init();