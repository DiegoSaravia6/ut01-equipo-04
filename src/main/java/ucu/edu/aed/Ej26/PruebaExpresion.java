package ucu.edu.aed.Ej26;

import java.util.List;
import java.util.ArrayList;

public class PruebaExpresion {

    public static void main(String[] args){

        Expresion expresion = new Expresion();

        List<Character> caso1 = new ArrayList<>();
        List<Character> caso2 = new ArrayList<>();

        caso1.add('{');
        caso1.add('[');
        caso1.add(']');
        caso1.add('}');

        caso2.add('{');
        caso2.add('[');
        caso2.add('{');
        caso2.add('}');
        caso2.add(']');

        boolean resultado1 = expresion.controlCorchetes(caso1);

        boolean resultado2 = expresion.controlCorchetes(caso2);

        System.out.println(resultado1); //true
        System.out.println(resultado2); //false
    }

}
