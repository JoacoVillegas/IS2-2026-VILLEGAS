import React, { createContext, useContext, useState } from "react";

// Creamos el contexto
const TemaContext = createContext();

function App() {
    const [tema, setTema] = useState("claro");

    return (
        <TemaContext.Provider value={{ tema, setTema }}>
            <Pagina />
        </TemaContext.Provider>
    );
}

function Pagina() {
    return (
        <div>
            <h1>Mi página</h1>
            <BotonTema />
        </div>
    );
}

function BotonTema() {
    const { tema, setTema } = useContext(TemaContext);

    return (
        <div>
            <p>Tema actual: {tema}</p>

            <button
                onClick={() =>
                    setTema(tema === "claro" ? "oscuro" : "claro")
                }
            >
                Cambiar tema
            </button>
        </div>
    );
}

export default App;
