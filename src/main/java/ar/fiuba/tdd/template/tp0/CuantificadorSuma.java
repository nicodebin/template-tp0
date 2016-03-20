package ar.fiuba.tdd.template.tp0;

/**
 * Created by Nico on 19/03/2016.
 */
public class CuantificadorSuma extends Cuantificador {
    public CuantificadorSuma(int maxLength) {
        super(maxLength);
        this.minLength = 1;
        this.symbol = '+';
    }
}
