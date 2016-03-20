package ar.fiuba.tdd.template.tp0;

import java.util.Random;

/**
 * Created by Nico on 19/03/2016.
 */
public class Token {

    private String string;


    private Cuantificador cuantificador;
    private boolean hasCuantificador = false;
    private boolean esUnConjunto = false; // Indica si el token es para un conjunto

    public Token() {
        this.string = "";
    }

    public Token(String string) {
        this.string = string;
    }

    public Token(char chr) {
        this(Character.toString(chr));
    }

    public Token(String string, Cuantificador cuantificador) {
        this.string = string;
        this.setCuantificador(cuantificador);
    }

    public Token(char chr, Cuantificador cuantificador) {
        this(Character.toString(chr), cuantificador);
    }

    public void setCuantificador(Cuantificador cuantificador) {
        this.cuantificador = cuantificador;
        this.hasCuantificador = true;
    }

    public void setEsConjunto() {
        this.esUnConjunto = true;
    }

    public void appendChar(char chr) {
        if (this.esUnConjunto && chr == '\\') {
            // No agrego escapes en conjuntos
            return;
        }
        this.string += chr;
    }

    public String getTokenString() {
        return this.string;
    }

    /** Genera un string aleatoreo a partir del token string parseado
     * @return String
     */
    public String getRandomString() {
        StringBuilder randomString = new StringBuilder();

        if (hasCuantificador) {
            for (int i = 0; i < this.cuantificador.getRandomAmount(); i++) {
                randomString.append(parseToken());
            }
        } else {
            randomString.append(parseToken());
        }

        return randomString.toString();
    }

    /** Parsea el token sin tener en cuenta el cuantificador
     * @return String
     */
    private String parseToken() {
        StringBuilder parsedToken = new StringBuilder();

        if (this.esUnConjunto) {
            if (this.string.length() == 1) {
                parsedToken.append(this.string);
            } else {
                Random rand = new Random();
                int index = rand.nextInt(this.string.length() - 1);
                parsedToken.append(this.string.charAt(index));
            }
        } else {
            if (this.string.equals(".")) {
                parsedToken.append(this.getRandomCharImprimible());
            } else if (this.string.charAt(0) == '\\') {
                parsedToken.append(this.string.charAt(1));
            } else {
                parsedToken.append(this.string);
            }
        }

        return parsedToken.toString();
    }

    private char getRandomCharImprimible() {
        Random rand = new Random();
        int minAscii = 32;
        int maxAscii = 126;
        return (char)(rand.nextInt(maxAscii - minAscii) + minAscii);
    }
}
