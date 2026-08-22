package ucu.edu.aed.Ej26;
import ucu.edu.aed.tda.Pila;
import java.util.List;

public class Expresion {


    public boolean controlCorchetes(List<Character> listaDeEntrada){
        
        if (listaDeEntrada == null){
            throw new IllegalArgumentException("La lista de entrada no puede ser nula");
        }
        Pila<Character> revision = new Pila<>();

        for(Character caracter : listaDeEntrada){
            if(caracter == '{' || caracter == '(' || caracter == '['){
                revision.mete(caracter);
            }

            if(caracter == '}' || caracter == ')' || caracter == ']'){
                if(revision.esVacio()){
                    return false;
                }

                Character tope = revision.tope();

                if( (caracter == '}' && tope =='{' ) || 
                (caracter == ')' && tope == '(')  || 
                (caracter == ']' && tope == '[')){

                    revision.saca();

                } else {
                    return false;
                }
            }
        }

        return revision.esVacio();
    }
    

}
