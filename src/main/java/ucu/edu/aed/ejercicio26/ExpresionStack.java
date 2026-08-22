package ucu.edu.aed.ejercicio26;
import java.util.Stack;
import java.util.List;

public class ExpresionStack {

    public boolean controlCorchetes(List<Character> listaDeEntrada){


        if (listaDeEntrada == null){
            throw new IllegalArgumentException("La lista de entrada no puede ser nula");
        }
        Stack<Character> revision = new Stack<>(); 

        for(Character caracter : listaDeEntrada){
            if(caracter == '{' || caracter == '(' || caracter == '['){
                revision.push(caracter);
            }

            if(caracter == '}' || caracter == ')' || caracter == ']'){
                if(revision.isEmpty()){
                    return false;
                }

                Character tope = revision.peek();

                if( (caracter == '}' && tope =='{' ) || 
                (caracter == ')' && tope == '(')  || 
                (caracter == ']' && tope == '[')){

                    revision.pop();

                } else {
                    return false;
                }
            }
        }

        return revision.isEmpty();

    }

}
