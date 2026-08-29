import { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [contador, setContador] = useState(0);


  // -------------------------
  // EJEMPLO DE EVENTO
  // -------------------------
  const [mensaje, setMensaje] = useState('');

  function manejarClick() {
    setMensaje('¡El botón fue presionado!');
  }

  useEffect(() => {
    if (contador > 0) {
      setMensaje(`El contador cambió a ${contador}.`);
    }
  }, [contador]);

  return (
    <div className="contenedor">
      <section>
        <h2>Ejemplo de Estado en React</h2>

        <p>Valor actual: {contador}</p>

        <button onClick={() => setContador(contador + 1)}>
          Incrementar
        </button>

        <button onClick={() => setContador(contador - 1)}>
          Decrementar
        </button>

      </section>
      
      <section>
        <h2>Ejemplo de Evento</h2>

        <button onClick={manejarClick}>
          Presionar botón
        </button>

        <p>{mensaje}</p>
      </section>
    </div>
  );
}

export default App;