package ar.fiuba.tdd.template.tp0;

/**
 * Created by Nico on 19/03/2016.
 */
public class CuantificadorFactory {

    protected int maxLength;

    public CuantificadorFactory(int maxLength) {
        this.maxLength = maxLength;
    }

    public Cuantificador getCuantificador(char cuantificador) throws UnkownCuantificadorException {
        switch (cuantificador) {
            case '*':
                return new CuantificadorAsterisco(this.maxLength);
            case '?':
                return new CuantificadorPregunta();
            case '+':
                return new CuantificadorSuma(this.maxLength);
            default:
                throw new UnkownCuantificadorException();
        }
    }
}
