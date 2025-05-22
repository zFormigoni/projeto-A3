package com.mycompany.projetoa3.TelaInicial;

import java.sql.*;

public class ConexaoDB {
    public static Connection conectar() {
        Connection conexao = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(
                "jdbc:mysql://localhost/db_a3", "root", "1234");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do BD não localizado.");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return conexao;
    }

    public static boolean inserirUsuario(String cpf, String nome, String telefone, String email, String senha) {
        String sql = "INSERT INTO tb_usuarios (cpf, nome, telefone, email, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setString(5, senha);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }

    public static boolean verificarLogin(String cpf, String senha) {
        String sql = "SELECT * FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Se houver um resultado, login é válido
        } catch (SQLException e) {
            System.out.println("Erro ao verificar login: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean excluirUsuarioPorCpf(String cpf) {
        String sql = "DELETE FROM tb_usuarios WHERE cpf = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }
    
    public static String verificarLoginERetornarNome(String cpf, String senha) {
        String sql = "SELECT nome FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("nome"); // Login válido, retorna o nome
        }
    } catch (SQLException e) {
    }
    return null; // Login inválido
}  
    public static Usuario buscarUsuarioPorCpf(String cpf) {
    String sql = "SELECT cpf, nome, telefone, email FROM tb_usuarios WHERE cpf = ?";
    try (Connection conn = conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, cpf);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Usuario(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email")
            );
        }
    } catch (SQLException e) {
    }
    return null;
}

public static boolean atualizarUsuario(String cpf, String nome, String telefone, String email) {
    String sql = "UPDATE tb_usuarios SET nome = ?, telefone = ?, email = ? WHERE cpf = ?";
    try (Connection conn = conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, nome);
        stmt.setString(2, telefone);
        stmt.setString(3, email);
        stmt.setString(4, cpf);
        int linhasAtualizadas = stmt.executeUpdate();
        return linhasAtualizadas > 0;
    } catch (SQLException e) {
    }
    return false;
}
}
