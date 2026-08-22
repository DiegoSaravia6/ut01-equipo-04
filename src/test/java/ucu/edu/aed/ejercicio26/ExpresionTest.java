package ucu.edu.aed.ejercicio26;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

public class ExpresionTest extends TestCase {

    private Expresion expresion;
    private List<Character> casoCorrecto;
    private List<Character> casoIncorrecto;

    protected void setUp() {

        expresion = new Expresion();

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

        assertTrue(expresion.controlCorchetes(casoCorrecto));
    }

    public void testCasoIncorrecto() {
        assertFalse(expresion.controlCorchetes(casoIncorrecto));
    }    

    public void testCasoNulo(){
        try{
            expresion.controlCorchetes(null);
            fail("Se esperaba IllegalArgumentException");
        }catch (IllegalArgumentException e){
            
        }
    }

}
