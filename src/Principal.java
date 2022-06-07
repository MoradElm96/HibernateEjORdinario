
import POJOS.Departamentos;
import POJOS.Empleados;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alumno
 */
public class Principal {
     public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        SessionFactory sf = HibernateUtil.sessionFactory();
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();

        //para la consulta
        Query selectEmpleados = s.createQuery("From Empleados");
        List<Empleados> listaEmpleados = selectEmpleados.list();
        System.out.println("***************************ANTES DE ACTUALIZAR***********************");

        for (Empleados listaEmpleado : listaEmpleados) {
            System.out.println(listaEmpleado);
        }
        Query selectDepartamento = s.createQuery("From Departamentos");
        List<Departamentos> listaDepartamento = selectDepartamento.list();
        //recorremos y mostramos los departamentos
        for (Departamentos listaDepartamentos : listaDepartamento) {
            System.out.println(listaDepartamentos);
        }

        Departamentos dep;
        Empleados emp;

        int[] cant = new int[4];
        int gratificacion, j;

        //recorremos la lista de empleados
        for (int i = 0; i < listaEmpleados.size(); i++) {
            System.out.println(listaEmpleados.get(i).toString());
            int alta = listaEmpleados.get(i).getAlta();// obtenemos el año del alta
            //sacamos los años,restandole el año actual
            int anios = 2022 - alta;
            if (anios < 3) {
                s.delete(listaEmpleados.get(i));//borramos el objeto

            } else {
                if (anios < 11) {
                    gratificacion = (anios - 2) * 50;
                } else {
                    gratificacion = 350 + (anios - 9) * 50 * 3 / 100;
                    j = listaEmpleados.get(i).getDepartamentos().getDeptNo() / 10 - 1;
                    cant[j] = cant[j] + gratificacion;
                    listaEmpleados.get(i).setSalario(listaEmpleados.get(i).getSalario() + gratificacion / 12);
                    s.update(listaEmpleados.get(i));
                }
            }
        }
        //recorremos el array
        for (int i = 0; i < cant.length; i++) {

            dep = (Departamentos) s.get(Departamentos.class, (i + 1) * 10);
            if (dep == null) {
                System.out.println("Ese departamento no existe");

            } else {
                dep.setPresupuesto(dep.getPresupuesto() + cant[i]);
                s.update(dep);

            }

        }
        
        
        System.out.println("--------------------------------------------------------------");
        //mostrar un empleado en concreto
        System.out.println("Introduce numero de empleado a mostrar");
        String codigoEmpleado = teclado.next();//no nextLine
        emp= (Empleados) s.get(Empleados.class,codigoEmpleado);//como un id
        if(emp==null){
            System.out.println("El empleado "+ codigoEmpleado + " no existe");
            
        }else{
            System.out.println(emp.toString());
        }
        
        System.out.println("--------------------------------------------------------------");
        
        
        
        
        //muestra todos los empleados de un departamento
        System.out.println("Introduce un departamento");
        int numDepartamento = teclado.nextInt();
        dep = (Departamentos) s.get(Departamentos.class, numDepartamento);
        if(dep==null){
            System.out.println("El departamento no existe");
            
        }else{
            Iterator iterador = dep.getEmpleadoses().iterator();
            while(iterador.hasNext()){
                emp=(Empleados)iterador.next();
                System.out.println(emp.toString());
            }
        }
        
        selectEmpleados= s.createQuery("From Empleados");
        listaEmpleados= selectEmpleados.list();
      
        for (int i = 0; i <listaEmpleados.size(); i++) {
            System.out.println(listaEmpleados.get(i).toString());
        }
        
        selectDepartamento = s.createQuery("From Departamentos");
        listaDepartamento= selectDepartamento.list();
        
        for (int i = 0; i <listaDepartamento.size(); i++) {
            System.out.println(listaDepartamento.get(i).toString());
        }
     
        
        t.commit();
        s.close();
        sf.close();
        
        System.exit(0);
      
        

                

    }
}
