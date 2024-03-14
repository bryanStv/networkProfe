import java.time.LocalDate;
import java.util.Scanner;
import java.sql.*;
public class Network {
    private static int currentScreen = 0;
    private static java.sql.Connection con;

    private static int userID;
    private static String userName;
    public static void main(String[] args) throws SQLException {
        int option;
        String host = "jdbc:sqlite:src/main/resources/network";
        con = java.sql.DriverManager.getConnection(host);
        while(true){
            printMenu();
            option = getOption();
            if(option == 0) break;
            if(currentScreen == 0){
                switch (option){
                    case 1:
                        allPost();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        register();
                        break;
                }
            }else{
                switch (option){
                    case 1:
                        myPost();
                        break;
                    case 2:
                        newPost();
                        break;
                    case 5:
                        otherPost();
                        break;
                    case 6:
                        logout();
                        break;
                }
            }
        }
    }


    private static int getOption(){
        Scanner tc = new Scanner(System.in);
        int option = -1;
        try{
            option =  Integer.parseInt(tc.next());
            if ((currentScreen == 0 && option > 3) || (currentScreen == 1 && option > 6)){
                System.out.println("Incorrect option");
            }
        }catch (IllegalArgumentException e){
            System.out.println("Incorrect option");
        }
        return option;
    }

    private static void printMenu(){
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";
        System.out.println(ANSI_GREEN+"--------------------------------------------------------------------------------------"+ANSI_RESET);
        if(currentScreen == 0){
            System.out.println(ANSI_CYAN+"0 Exit | 1 All Post | 2 Login | 3 Register"+ANSI_RESET);
        }else{
            System.out.println(ANSI_CYAN+"0 Exit | 1 My Post | 2 New Post | 3 New comment | 4 Like | 5 Other Post | 6 Logout "+userName+ANSI_RESET);
        }
        System.out.println(ANSI_GREEN+"--------------------------------------------------------------------------------------"+ANSI_RESET);
    }
    private static void login() throws SQLException {
        Scanner tc = new Scanner(System.in);
        //Coger datos
        System.out.print("Name: ");
        String name = tc.nextLine();

        //Hacer connsulta
        PreparedStatement st = null;
        String query = "SELECT * FROM users WHERE name = ?";
        st = con.prepareStatement(query);
        st.setString(1,name);
        ResultSet rs = st.executeQuery();
        //Actuar en consecuencia
        if (rs.next()){
            userID = rs.getInt("id");
            userName = rs.getString("name");
            currentScreen = 1;
        }else{
            System.out.println("User not found");
        }
    }

    private static void logout(){
        currentScreen = 0;
    }

    private static void allPost() throws SQLException {
        //Hacer connsulta
        PreparedStatement st = null;
        String query = "SELECT * FROM post";
        st = con.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            System.out.println(rs.getInt("id") + " - " + rs.getString("text"));
        }
    }

    private static void myPost() throws SQLException{
        //Hacer connsulta
        PreparedStatement st = null;
        String query = "SELECT * FROM post WHERE userID = ?";
        st = con.prepareStatement(query);
        st.setInt(1,userID);
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            System.out.println(rs.getInt("id") + " - " + rs.getString("text"));
        }
    }

    private static void otherPost() throws SQLException{
        //Hacer connsulta
        PreparedStatement st = null;
        String query = "SELECT * FROM post WHERE userID != ?";
        st = con.prepareStatement(query);
        st.setInt(1,userID);
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            System.out.println(rs.getInt("id") + " - " + rs.getString("text"));
        }
    }

    private static void register() throws SQLException{
        Scanner tc = new Scanner(System.in);
        PreparedStatement st = null;
        System.out.print("Name: ");
        String name = tc.nextLine();
        System.out.print("Last Name: ");
        String lastname = tc.nextLine();
        String query = "INSERT INTO users(name,apellido) VALUES(?,?)";
        st = con.prepareStatement(query);
        st.setString(1,name);
        st.setString(2,lastname);
        st.executeUpdate();
    }

    private static void newPost() throws SQLException{
        Scanner tc = new Scanner(System.in);
        PreparedStatement st = null;
        System.out.print("New Post: ");
        String text = tc.nextLine();
        LocalDate date = LocalDate.now();
        String idUser = ""+userID;
        String query = "INSERT INTO post(text,date,likes,userID) VALUES(?,?,?,?)";
        st = con.prepareStatement(query);
        st.setString(1,text);
        st.setString(2,date.toString());
        st.setInt(3,0);
        st.setString(4,idUser);
        st.executeUpdate();
    }

}
