import { useState } from "react";

function App() {
  const [tareas, setTareas] = useState([]);
  const [tarea, setTarea] = useState("");

  const agregarTarea = () => {
    if (tarea.trim() === "") return;

    setTareas([...tareas, tarea]);
    setTarea("");
  };

  return (
    <div>
      <h1>Mis tareas</h1>

      <input
        type="text"
        value={tarea}
        onChange={(e) => setTarea(e.target.value)}
        placeholder="Escribi una tarea"
      />

      <button onClick={agregarTarea}>
        Agregar
      </button>

      <ul>
        {tareas.map((tarea, index) => (
          <li key={index}>{tarea}</li>
        ))}
      </ul>
    </div>
  );
}

export default App;