package ar.fiuba.tdd.template.tp0;

/**
 * Created by Nico on 19/03/2016.
 */
public class CuantificadorAsterisco extends Cuantificador {
    public CuantificadorAsterisco(int maxLength) {
        super(maxLength);
        this.minLength = 0;
        this.symbol = '*';
    }
}
