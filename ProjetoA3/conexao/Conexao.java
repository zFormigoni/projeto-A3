import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conexao {
    public static void main(String[] args) throws SQLException{
        Connection conexao=null;
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        conexao = DriverManager.getConnection("jdbc:mysql://localhost/db_pessoas", "root", "0004574a");
        ResultSet rsCliente=conexao.createStatement().executeQuery("SELECT * FROM tb_pessoa");
        while(rsCliente.next()){
            System.out.println("Nome:"+rsCliente.getString("nome"));
            
        }
    }catch (ClassNotFoundException ex) {
            System.out.println("Driver do BD não localizado");
            //procura no google maven driver mysql
            //se for pela pasta Dependencies: mysql connector
    }catch (SQLException ex) {
            System.out.println("Ocorreu um erro ao acessar o banco"+ex.getMessage());
    } finally {
        if (conexao !=null){
            conexao.close();
        }   
    }
    }
}