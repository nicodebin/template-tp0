package ar.fiuba.tdd.template.tp0;

/**
 * Created by Nico on 19/03/2016.
 */
public class CuantificadorPregunta extends Cuantificador {
    public CuantificadorPregunta() {
        super(1); // Asigna maxLength en 1
        this.minLength = 0;
        this.symbol = '?';
    }
}
