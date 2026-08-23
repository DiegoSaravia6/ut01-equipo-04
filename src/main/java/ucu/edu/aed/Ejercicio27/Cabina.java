package ucu.edu.aed.ProblemSet01.Ejercicio27;

public class Cabina 
{
    public int capacidadMaxima;
    public int ocupacionActual;

    public Cabina(int capacidadMaxima) 
    {
        this.capacidadMaxima = capacidadMaxima;
        this.ocupacionActual = 0;
    }

    public boolean subir(int cantidad)
    {
        if (cantidad < 0) 
        {
            return false;
        }
        else if (ocupacionActual + cantidad <= capacidadMaxima) 
        {
            ocupacionActual += cantidad;
            return true;
        } 
        else 
        {
            return false;
        }
    }

    public boolean bajar(int cantidad) 
    {
        if (cantidad < 0) 
        {
            return false;
        }
        else if (ocupacionActual - cantidad >= 0) 
        {
            ocupacionActual -= cantidad;
            return true;
        } 
        else 
        {
            return false;
        }
    }

    public int getOcupacion() 
    {
        return ocupacionActual;
    }

    public int setOcupacion(int ocupacion) 
    {
        if (ocupacion >= 0 && ocupacion <= capacidadMaxima) 
        {
            this.ocupacionActual = ocupacion;
            return ocupacionActual;
        } 
        else 
        {
            throw new IllegalArgumentException("Ocupación fuera de rango");
        }
    }
}
