package ar.fiuba.tdd.template.tp0;

/**
 * Created by Nico on 19/03/2016.
 */
public class RegexParser {

    protected String regEx;
    protected int currentIndex; // Indice actual de parseo de this.regEx
    protected CuantificadorFactory cuantificadorFactory;
    protected boolean conjuntoAbierto; // Indica si me encuentro parseando dentro de un conjunto

    public RegexParser(String regEx, int maxLength) {
        this.regEx = regEx;
        this.currentIndex = 0;
        this.cuantificadorFactory = new CuantificadorFactory(maxLength);
        this.conjuntoAbierto = false;
    }

    public String getRandomString() throws Exception {
        StringBuilder result = new StringBuilder();

        int iterationControl = 0; // Prevenir loop infinito, por las dudas..
        while (this.currentIndex < this.regEx.length()) {
            if (iterationControl > 100) {
                throw new Exception("LOOP INFINITO");
            }
            Token token = this.getToken();
            result.append(token.getRandomString());
        }

        return result.toString();
    }

    public boolean isEscape(char chr) {
        return chr == '\\';
    }

    public boolean isCuantificador(char chr) {
        return chr == '?' || chr == '*' || chr == '+';
    }

    public boolean isConjunto(char chr) {
        return this.isConjuntoApertura(chr) || this.isConjuntoCierre(chr);
    }

    public boolean isConjuntoApertura(char chr) {
        return chr == '[';
    }

    public boolean isConjuntoCierre(char chr) {
        return chr == ']';
    }

    public boolean isComodin(char chr) {
        return chr == '.';
    }

    public boolean isLiteral(char chr) {
        String str = "" + chr;
        return str.matches("\\w");
    }

    public Token getToken() throws InvalidRegexException, UnkownCuantificadorException {
        char currentChar;
        char nextChar;
        Token token;
        final char NULL_CHR = '\0'; // Caracter nulo

        currentChar = this.regEx.charAt(this.currentIndex);
        nextChar = (this.currentIndex + 1 < this.regEx.length()) ? this.regEx.charAt(this.currentIndex + 1) : NULL_CHR;

        // Verifico que no haya corchetes sin cerrar
        this.checkConjuntoAbierto(currentChar);

        if (nextChar != NULL_CHR) {
            token = analizarChar(currentChar, nextChar);
        } else {
            // Analizo el ultimo caracter como un caso especial
            token = analizarUltimoChar(currentChar);
        }

        return token;
    }

    private void checkConjuntoAbierto(char currentChar) throws InvalidRegexException {
        if (!this.conjuntoAbierto && this.isConjuntoCierre(currentChar)) {
            throw new InvalidRegexException("Se encontro un cierre de conjunto sin tener uno abierto");
        }
    }

    private Token analizarChar(char currentChar, char nextChar) throws InvalidRegexException, UnkownCuantificadorException {
        Token token = new Token();
        if (this.isLiteral(currentChar) || this.isComodin(currentChar)) {
            if (this.isCuantificador(nextChar)) {
                // Se trata de un literal con un cuantificador. Avanzo dos posiciones el puntero
                this.currentIndex += 2;
                return new Token(currentChar, this.cuantificadorFactory.getCuantificador(nextChar));
            }

            // Se debe imprimir solo 1 literal. Avanzo una posicion el puntero
            this.currentIndex++;
            token = new Token(currentChar);

        } else if (this.isEscape(currentChar)) {
            // Se debe mostrar literalmente lo escapeado. Avanzo dos posiciones el puntero
            this.currentIndex += 2;
            token = new Token(Character.toString(currentChar) + nextChar);

        } else {
            // Es un conjunto
            if (this.isConjuntoCierre(nextChar)) {
                throw new InvalidRegexException("No puede haber un conjunto vacío");
            }
            if (this.conjuntoAbierto == false && this.isConjuntoApertura(currentChar)) {
                this.currentIndex++;
                token = this.analizarConjunto();
            }
        }

        return token;
    }

    private Token analizarUltimoChar(char currentChar) throws InvalidRegexException {
        if (this.isLiteral(currentChar) || this.isComodin(currentChar)) {
            this.currentIndex++;
            return new Token(Character.toString(currentChar));
        } else {
            throw new InvalidRegexException("La expresion regular no esta bien formada");
        }
    }

    private Token analizarConjunto() throws InvalidRegexException, UnkownCuantificadorException {
        this.conjuntoAbierto = true;
        Token token = new Token();
        token.setEsConjunto();
        char currentChar = 'x';
        boolean charConEscape = false;

        while (this.currentIndex < this.regEx.length()) {
            currentChar = this.regEx.charAt(this.currentIndex);
            this.currentIndex++;
            if (this.isConjuntoCierre(currentChar) && !charConEscape) {
                break;
            }
            charConEscape = this.isEscape(currentChar);

            token.appendChar(currentChar);
        }

        if (!this.isConjuntoCierre(currentChar)) {
            throw new InvalidRegexException("Se abrió un conjunto pero nunca se cerró");
        }

        // Analizo si hay un cuatificador para el conjunto
        analizarConjuntoCuantificador(token);

        this.conjuntoAbierto = false;
        return token;
    }

    private void analizarConjuntoCuantificador(Token token) throws UnkownCuantificadorException {
        // Verifico si tiene cuantificador
        if (this.currentIndex < this.regEx.length()) {
            char currentChar = this.regEx.charAt(this.currentIndex);
            this.currentIndex++;
            if (this.isCuantificador(currentChar)) {
                token.setCuantificador(this.cuantificadorFactory.getCuantificador(currentChar));
            }
        }
    }
}
