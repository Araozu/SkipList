public class Inicio {

    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>(5);
        sl.insertar(10);
        sl.insertar(124);
        sl.insertar(554);
        sl.insertar(-2453);
        sl.insertar(-745);
        sl.insertar(5674);
        sl.insertar(876);
        sl.insertar(56);
        sl.insertar(9);

        System.out.println(sl);
        System.out.println(sl.contiene(876));
        sl.eliminar(876);
        System.out.println(sl.contiene(876));

        System.out.println(sl);
    }

}
