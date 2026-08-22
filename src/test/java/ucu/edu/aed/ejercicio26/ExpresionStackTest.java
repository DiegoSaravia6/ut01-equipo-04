package ucu.edu.aed.ejercicio26;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

public class ExpresionStackTest extends TestCase {

    private ExpresionStack expresionStack;
    private List<Character> casoCorrecto;
    private List<Character> casoIncorrecto;

    protected void setUp() {

        expresionStack = new ExpresionStack();

        casoCorrecto = new ArrayList<>();
        casoCorrecto.add('{');
        casoCorrecto.add('[');
        casoCorrecto.add(']');
        casoCorrecto.add('}');

        casoIncorrecto = new ArrayList<>();
        casoIncorrecto.add('{');
        casoIncorrecto.add('[');
        casoIncorrecto.add('{');
        casoIncorrecto.add('}');
        casoIncorrecto.add(']');
    }



    public void testExpresionCorrecta(){

        assertTrue(expresionStack.controlCorchetes(casoCorrecto));
    }

    public void testCasoIncorrecto() {
        assertFalse(expresionStack.controlCorchetes(casoIncorrecto));
    }    

    public void testCasoNulo(){
        try{
            expresionStack.controlCorchetes(null);
            fail("Se esperaba IllegalArgumentException");
        }catch (IllegalArgumentException e){
            
        }
    }
}
