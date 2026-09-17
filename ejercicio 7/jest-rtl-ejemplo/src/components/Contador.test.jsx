import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import "@testing-library/jest-dom";
import Contador from "./Contador";

test("el contador aumenta al hacer clic", async () => {

    // Renderizamos el componente
    render(<Contador />);

    // Comprobamos el valor inicial
    expect(screen.getByText("Valor: 0")).toBeInTheDocument();

    // Buscamos el botón
    const boton = screen.getByRole("button", {
        name: "Incrementar"
    });

    // Creamos un usuario simulado
    const user = userEvent.setup();

    // Simulamos un clic
    await user.click(boton);

    // Comprobamos que el contador aumentó
    expect(screen.getByText("Valor: 1")).toBeInTheDocument();
});