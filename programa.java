
// GSON
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Libs
import java.io.*;
import java.net.*;
import java.util.Scanner;

// class programa
public class programa {
    static String ip_maquina;

    public static class Usuario {
        String email;
        String nombre;
        String apellido_paterno;
        String apellido_materno;
        String fecha_nacimiento;
        String telefono;
        String genero;
        byte[] foto;

        void mostrarInformacion() {
            System.out.println("\nUsuario.\n" + "email: " + email + "\n" + "nombre: " + nombre + "\n"
                    + "apellido_paterno: " + apellido_paterno + "\n" + "apellido_materno: " + apellido_materno + "\n"
                    + "fecha_nacimiento: " + fecha_nacimiento + "\n" + "telefono: " + telefono + "\n" + "genero: "
                    + genero + "\n");
        }
    }

    public static void main(String[] args) {
        char opcion;
        Scanner input = new Scanner(System.in);
        // Ingresar ip de la maquina virtual
        System.out.println("Ingresar ip de la maquina virtual: ");
        ip_maquina = input.nextLine();
        // Menu
        do {
            mostrarMenu();
            opcion = input.nextLine().charAt(0);
            seleccionarOpcion(opcion);
        } while (opcion != 'e');
        input.close();
    }

    private static void mostrarMenu() {
        System.out.print("MENU\n" + "a. Alta usuario\n" + "b. Consulta usuario\n" + "c. Borra usuario\n"
                + "d. Borra todos los usuarios\n" + "e. Salir\n" + "Opcion:__");
    }

    private static void seleccionarOpcion(char opcion) {
        switch (opcion) {
            case 'a':
                altaUsuario();
                break;
            case 'b':
                consultaUsuario();
                break;
            case 'c':
                borraUsuario();
                break;
            case 'd':
                borraTodosUsuarios();
                break;
        }
    }

    private static void altaUsuario() {
        Gson gson = new Gson();

        final Usuario usuario = crearUsuario();
        final String usuario_json = gson.toJson(usuario);
        System.out.println("USUARIO: "+usuario_json);
        invocarAlta(usuario_json);
    }

    private static void consultaUsuario() {
        Gson gson = new Gson();
        String email;

        System.out.println("Ingresar email: ");
        email = new Scanner(System.in).nextLine();
        // PETICION POST
        String cadena = invocarConsulta(email);
        // System.out.println("CADENA:\n\n\n"+cadena);
        Usuario usuario = gson.fromJson(cadena, Usuario.class);
        // Mostrar usuario
        usuario.mostrarInformacion();
    }

    private static void borraUsuario() {
        Gson gson = new Gson();
        String email;

        System.out.println("Ingresar email: ");
        email = new Scanner(System.in).nextLine();
        invocarBorra(email);
    }

    private static void borraTodosUsuarios() {
        Gson gson = new Gson();
        String email;

        System.out.println("Ingresar email: ");
        email = new Scanner(System.in).nextLine();
        invocarBorraUsuarios(email);
    }

    private static void invocarAlta(String usuario) {
        URL url;
        try {
            url = new URL("http://" + ip_maquina + ":8080/Servicio/rest/ws/alta");
        
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);

            // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
            
                conexion.setRequestMethod("POST");
            
            // indica que la petición estará codificada como URL
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // el método web "consulta" recibe como parámetro el email de un usuario, en
            // este caso el email es obtenido de los parametros de la función
            String parametros;
        
            parametros = "usuario=" + URLEncoder.encode(usuario, "UTF-8");
            OutputStream os;
            os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            
            // se debe verificar si hubo error
            if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;
            // el método web regresa una string en formato JSON
            while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
            conexion.disconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static String invocarConsulta(String email){

        try{
            URL url = new URL("http://"+ip_maquina+":8080/Servicio/rest/ws/consulta");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);

            // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
            conexion.setRequestMethod("POST");
            // indica que la petición estará codificada como URL
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es obtenido de los parametros de la función
            String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            // se debe verificar si hubo error
            if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;
            String cadena = "";
            // el método web regresa una string en formato JSON
            // while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
            while ((respuesta = br.readLine()) != null) cadena += respuesta;
            conexion.disconnect();

            return cadena;
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    private static void invocarBorra(String email){
        try{
            URL url = new URL("http://"+ip_maquina+":8080/Servicio/rest/ws/borra");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);

            // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
            conexion.setRequestMethod("POST");
            // indica que la petición estará codificada como URL
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es obtenido de los parametros de la función
            String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            // se debe verificar si hubo error
            if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;
            // el método web regresa una string en formato JSON
            while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
            conexion.disconnect();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static void invocarBorraUsuarios(String email){
        try{
            URL url = new URL("http://"+ip_maquina+":8080/Servicio/rest/ws/borrausuarios");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);

            // se utiliza el método HTTP POST (ver el método en la clase Servicio.java)
            conexion.setRequestMethod("POST");
            // indica que la petición estará codificada como URL
            conexion.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // el método web "consulta" recibe como parámetro el email de un usuario, en este caso el email es obtenido de los parametros de la función
            // String parametros = "email=" + URLEncoder.encode(email,"UTF-8");
            // OutputStream os = conexion.getOutputStream();
            // os.write(parametros.getBytes());
            // os.flush();
            // se debe verificar si hubo error
            if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;
            // el método web regresa una string en formato JSON
            while ((respuesta = br.readLine()) != null) System.out.println(respuesta);
            conexion.disconnect();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static Usuario crearUsuario(){
        Scanner input = new Scanner(System.in); 
        Usuario nuevo_usuario = new Usuario();
        // PEDIR INFORMACION AL USUARIO
        System.out.print("\nIngresar email: ");
        nuevo_usuario.email = input.nextLine();
        System.out.print("\nIngresar nombre: ");
        nuevo_usuario.nombre = input.nextLine();
        System.out.print("\nIngresar apellido_paterno: ");
        nuevo_usuario.apellido_paterno = input.nextLine();
        System.out.print("\nIngresar apellido_materno: ");
        nuevo_usuario.apellido_materno = input.nextLine();
        System.out.print("\nIngresar fecha_nacimiento: ");
        nuevo_usuario.fecha_nacimiento = input.nextLine();
        System.out.print("\nIngresar telefono: ");
        nuevo_usuario.telefono = input.nextLine();
        System.out.print("\nIngresar genero (\"M\" o \"F\"): ");
        nuevo_usuario.genero = input.nextLine();

        nuevo_usuario.foto = null;
        
        input.close();

        return nuevo_usuario;
    }
}
