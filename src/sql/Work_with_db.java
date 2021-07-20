package sql;

import java.sql.*;

public class Work_with_db {
    public static void main(String[] args) {
        Work_with_db db = new Work_with_db();

        try (Connection con = db.connectionDatabase()) {
        db.retrievingDatabaseData(con);
        int id = db.addingDataToDatabase(con,"Владимир","Kirov","80","vlad@yandex.ru");
        System.out.println("Adding person with id : "+id);
        db.updateDatabase(con,1,"10");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Connection connectionDatabase() throws ClassNotFoundException,SQLException {
        try {
//            загружаем класс из JDBC который реализует интерфейс java.sql.Driver
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Shop";
            String login = "postgres";
            @SuppressWarnings("SpellCheckingInspection") String password = "petya";
            return DriverManager.getConnection(url,login,password);
    }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

    private void retrievingDatabaseData (Connection con) throws SQLException{
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
            }catch (SQLException e){
                e.printStackTrace();
                throw e;
            }
    }
    @SuppressWarnings("SameParameterValue")
    private int addingDataToDatabase(Connection con, String name, String city,
                                     String phone, String email) throws SQLException{
        int id = -1;
//        передаём строку с запросом и массив имён столбцов,значение которых нам нужно
        try(PreparedStatement stmt = con.prepareStatement("insert into" +
                " customer(name,city,phone,email)values(?,?,?,?)",new String[] {"id"})) {
            stmt.setString(1,name);
            stmt.setString(2,city);
            stmt.setString(3,phone);
            stmt.setString(4,email);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                id = rs.getInt("id");
            }
            return id;
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
    @SuppressWarnings("SameParameterValue")
    private void updateDatabase(Connection con,int id,String phone) throws SQLException{
        try(PreparedStatement stmt = con.prepareStatement("update customer" +
                " set phone = ? where id = ?")) {
            stmt.setString(1,phone);
            stmt.setInt(2,id);
            stmt.executeUpdate();
            stmt.executeUpdate();
            System.out.println("update was successful");
        }catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }
}
