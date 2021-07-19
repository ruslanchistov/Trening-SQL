package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Work_with_db {
    public static void main(String[] args) {
        Work_with_db db = new Work_with_db();
        db.retrievingDatabaseData();
    }
    private void retrievingDatabaseData (){
        try {
//            загружаем класс из JDBC который реализует интерфейс java.sql.Driver
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Shop";
            String login = "postgres";
            String password = "petya";
            Connection con = DriverManager.getConnection(url,login,password);

//                создаём объект запрос в базу данных
            try (Statement stm = con.createStatement();
//                создаём запрос к таблице
                 ResultSet res = stm.executeQuery("select * from customer")){

//                считываем данные из таблицы
                while (res.next()){
                    String str = res.getString("name")+"  "+res.getString("city")+"  "+
                           res.getString("phone")+"  "+res.getString( "email");
                    System.out.println("person -- "+str);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
