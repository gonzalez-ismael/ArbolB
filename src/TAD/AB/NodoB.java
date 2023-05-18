package TAD.AB;

/**
 *
 * @author Ismael
 */
public class NodoB {
    private final int m;            //cantidad de hijos = orden
    private NodoB padre;            //Nodo del padre
    private int []claves;           //arreglo de claves
    private final NodoB []hijos;    //arreglo de hijos
    private int cantClaves;         //cantidad de claves

    /*
        Constructor creado por el desarrollador
    */
    public NodoB(int orden, int clave) {
        this.m = orden;
        this.padre = null;
        this.claves = new int[m-1];
        this.claves[0] = clave;
        this.hijos = new NodoB[m];
        this.cantClaves = 1;
    }
    
    public int getM() {
        return m;
    }
    
    public int getCantClaves() {
        return cantClaves;
    }

    public void setCantClaves(int cantClaves) {
        this.cantClaves = cantClaves;
    }
    
    public int getClaveEn(int indice){
        return claves[indice];
    }
    
    public void setClaveEn(int indice, int clave){
        this.claves[indice] = clave;
    }
    
    public NodoB getHijoEn(int indice){
        return hijos[indice];
    }
    
    public void setHijoEn(int indice, NodoB hijo){
        this.hijos[indice] = hijo;
    }
  
    public boolean esHoja(){
        boolean esHoja = true;
        int i=0;
        while((i<this.m)&&(esHoja==true)){
            if(this.hijos[i]!=null)
                esHoja = false;
            i++;
        }
        return esHoja;
    }
    
    public boolean esLleno(){
        return (this.getCantClaves() == this.m-1);
    }
    
    public void mostrarClaves(){
        int i = 0;
        System.out.print("Claves del nodo: ");
        while(i<this.getCantClaves()){
            System.out.print(this.getClaveEn(i)+" ");
            i++;
        }
        System.out.println("");
    }
    
    /*    retorna la posicion de la clave buscada, si no existe retorna -1    */
    public int buscarClave(int clave){
        int i = 0;
        int pos = -1;
        while((i<this.getCantClaves()) && (pos == -1)){
            if(this.getClaveEn(i)==clave)
                pos = i;
            i++;
        }
        return pos;
    }
    
    public NodoB getPadre() {
        return padre;
    }

    public void setPadre(NodoB padre) {
        this.padre = padre;
    }

    public int[] getClaves() {
        return claves;
    }

    public void setClaves(int[] claves) {
        this.claves = claves;
    }
}
