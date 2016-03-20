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
        Token token = new Token();
        final char NULL_CHR = '\0'; // Caracter nulo
        boolean saltarIteracion = false; // Indica que la iteracion debe ser salteada

        for (int i = this.currentIndex; i < this.regEx.length(); i++) {
            if (saltarIteracion) {
                saltarIteracion = false;
                continue;
            }

            currentChar = this.regEx.charAt(i);
            nextChar = (i + 1 < this.regEx.length()) ? this.regEx.charAt(i + 1) : NULL_CHR;

            if (!this.conjuntoAbierto && this.isConjuntoCierre(currentChar)) {
                throw new InvalidRegexException("Se encontro un cierre de conjunto sin tener uno abierto");
            }

            if (nextChar != NULL_CHR) {

                // Si me encuentro analizando un conjunto
                if (this.conjuntoAbierto) {
                    if (this.isConjuntoCierre(currentChar)) {
                        if (token.getTokenString().length() == 0) {
                            throw new InvalidRegexException("No puede haber un conjunto vacío");
                        } else {
                            this.conjuntoAbierto = false;
                            this.currentIndex = i + 1;
                            if (this.isCuantificador(nextChar)) {
                                // Se encontró un cuantificador para el conjunto. Avanzo dos posiciones el puntero
                                this.currentIndex = i + 2;
                                token.setCuantificador(this.cuantificadorFactory.getCuantificador(nextChar));
                            }
                            break;
                        }
                    } else if (this.isEscape(currentChar)) {
                        token.appendChar(nextChar);
                        saltarIteracion = true; // Me salteo la proxima iteracion para no agregar dos veces lo mismo
                    } else {
                        token.appendChar(currentChar);
                    }
                    continue;
                }

                if (this.isLiteral(currentChar) || this.isComodin(currentChar)) {
                    if (this.isCuantificador(nextChar)) {
                        // Se trata de un literal con un cuantificador. Avanzo dos posiciones el puntero
                        this.currentIndex = i + 2;
                        token = new Token(currentChar, this.cuantificadorFactory.getCuantificador(nextChar));
                        break;
                    } else {
                        // Se debe imprimir solo 1 literal. Avanzo una posicion el puntero
                        this.currentIndex = i + 1;
                        token = new Token(currentChar);
                        break;
                    }

                } else if (this.isEscape(currentChar)) {
                    // Se debe mostrar literalmente lo escapeado. Avanzo dos posiciones el puntero
                    this.currentIndex = i + 2;
                    token = new Token(Character.toString(currentChar) + nextChar);
                    break;

                } else if (this.isConjunto(currentChar)) {
                    if (this.isConjuntoCierre(nextChar)) {
                        throw new InvalidRegexException("No puede haber un conjunto vacío");
                    }
                    if (this.conjuntoAbierto == false && this.isConjuntoApertura(currentChar)) {
                        this.conjuntoAbierto = true;
                        token = new Token();
                        token.setEsConjunto();
                        continue;
                    }
                }
            } else {
                // No hay mas caracteres a continuación

                // Si me encuentro analizando un conjunto, el char actual debe ser
                // de cierre, sino esta mal formada la regex
                if (this.conjuntoAbierto) {
                    if (!this.isConjuntoCierre(currentChar)) {
                        throw new InvalidRegexException("Se abrió un conjunto pero nunca se cerró");
                    }
                    this.currentIndex = i + 1;
                    break;
                }

                if (this.isLiteral(currentChar) || this.isComodin(currentChar)) {
                    this.currentIndex = i + 1;
                    token = new Token(Character.toString(currentChar));
                } else {
                    throw new InvalidRegexException("La expresion regular no esta bien formada");
                }
            }
        }
        return token;
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
}
