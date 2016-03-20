package ar.fiuba.tdd.template.tp0;

import java.util.Random;

/**
 * Created by Nico on 19/03/2016.
 */
public abstract class Cuantificador {
    protected int minLength;
    protected int maxLength;
    protected char symbol; // Símbolo del cuantificador

    public Cuantificador(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getRandomAmount() {
        Random rand = new Random();
        return rand.nextInt(this.maxLength - this.minLength) + this.minLength;
    }

    public char getSymbol() {
        return this.symbol;
    }
}
