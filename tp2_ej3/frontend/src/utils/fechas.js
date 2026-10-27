// El backend envía las fechas como texto "yyyy-MM-dd" (ej: "1990-01-01").
// Si se hace new Date("1990-01-01"), JavaScript lo interpreta como medianoche UTC,
// y en Argentina (UTC-3) se muestra como 31/12/1989. Por eso formateamos el texto directamente.
export const formatearFecha = (fechaTexto) => {
    if (!fechaTexto) return '';
    const [anio, mes, dia] = fechaTexto.split('-');
    return `${dia}/${mes}/${anio}`;
};
