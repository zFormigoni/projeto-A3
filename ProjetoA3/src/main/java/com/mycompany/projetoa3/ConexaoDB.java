package com.mycompany.projetoa3;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;

public class ConexaoDB {

    // Conecta ao banco de dados
    public static Connection conectar() {
        String conGUSTAVO = "jdbc:mysql://localhost/db_a3";
        String conVITOR = "jdbc:mysql://localhost/";
        
        //String conDB = "vitor";
        String conDB = "gustavo";
        Connection conexao = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            if (conDB == "vitor"){
                conexao = DriverManager.getConnection(conVITOR, "root", "9411");
                System.out.println("CONEXAO DB VITOR");
            } else if (conDB == "gustavo"){
                conexao = DriverManager.getConnection(conGUSTAVO, "root", "1234");
                System.out.println("CONEXAO DB GUSTAVO");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do BD não localizado.");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return conexao;
    }

    // Insere novo usuário (tipo padrão é "padrao")
    public static boolean inserirUsuario(String cpf, String nome, String telefone, String email, String senha) {
        String sql = "INSERT INTO tb_usuarios (cpf, nome, telefone, email, senha, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'padrao')";
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
    
    public static boolean inserirUsuario2(Usuario usuario, String senha) {
        String sql = "INSERT INTO tb_usuarios (cpf, nome, telefone, email, senha, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'padrao')";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, senha);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }
    
   public static boolean consultarEmailCPF(String email, String cpf) {
    boolean achouEmail = false;
    boolean achouCpf = false;

    String sqlEmail = "SELECT * FROM tb_usuarios WHERE email = ?";
    String sqlCpf = "SELECT * FROM tb_usuarios WHERE cpf = ?";
    
    try (Connection conn = conectar(); 
         PreparedStatement stmtEmail = conn.prepareStatement(sqlEmail)) {

        stmtEmail.setString(1, email);
        ResultSet rsEmail = stmtEmail.executeQuery();
        if (rsEmail.next()) {
            achouEmail = true;
        }

    } catch (SQLException e) {
        e.printStackTrace();  // Opcional: para debug
    }

    try (Connection conn = conectar(); 
         PreparedStatement stmtCpf = conn.prepareStatement(sqlCpf)) {

        stmtCpf.setString(1, cpf);
        ResultSet rsCpf = stmtCpf.executeQuery();
        if (rsCpf.next()) {
            achouCpf = true;
        }

    } catch (SQLException e) {
        e.printStackTrace();  // Opcional: para debug
    }

    return achouEmail || achouCpf;
}

    
    public static boolean consultarEmail(String email) {
        String sql = "SELECT * FROM tb_usuarios WHERE email = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar login: " + e.getMessage());
            return false;
        }
    }

    // Verifica login apenas (true/false)
    public static boolean verificarLogin(String cpf, String senha) {
        String sql = "SELECT * FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar login: " + e.getMessage());
            return false;
        }
    }

    // Verifica login e retorna o nome do usuário
    public static String verificarLoginERetornarNome(String cpf, String senha) {
        String sql = "SELECT nome FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar nome do usuário: " + e.getMessage());
        }
        return null;
    }

    // ✅ Verifica login e retorna nome + tipo
    public static String[] verificarLoginERetornarDados(String cpf, String senha) {
        String sql = "SELECT nome, tipo_usuario FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo_usuario");
                return new String[] { nome, tipo };
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar dados do usuário: " + e.getMessage());
        }
        return null;
    }

    // Busca dados do usuário por CPF
public static Usuario buscarUsuarioPorCpf(String cpf) {
    String sql = "SELECT cpf, nome, telefone, email, tipo_usuario FROM tb_usuarios WHERE cpf = ?";
    try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, cpf);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Usuario(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("tipo_usuario")  // novo
            );
        }
    } catch (SQLException e) {
        System.out.println("Erro ao buscar usuário: " + e.getMessage());
    }
    return null;
}

    // Atualiza nome, telefone e email do usuário
    public static boolean atualizarUsuario(String cpf, String nome, String telefone, String email) {
        String sql = "UPDATE tb_usuarios SET nome = ?, telefone = ?, email = ? WHERE cpf = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, email);
            stmt.setString(4, cpf);
            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    // Exclui um usuário pelo CPF
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

    // --- CATEGORIAS ---

    public static boolean adicionarCategoria(Categoria categoria) {
        String sql = "INSERT INTO tb_categorias (nome) VALUES (?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar categoria: " + e.getMessage());
            return false;
        }
    }

    public static boolean editarCategoria(Categoria categoria) {
        String sql = "UPDATE tb_categorias SET nome = ? WHERE id_categoria = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao editar categoria: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluirCategoria(int idCategoria) {
        String sql = "DELETE FROM tb_categorias WHERE id_categoria = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }

    public static List<Categoria> listarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM tb_categorias";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Categoria cat = new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nome")
                );
                categorias.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }
    
    public static List<Usuario> listarUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT cpf, nome, telefone, email, tipo_usuario FROM tb_usuarios";
    try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Usuario u = new Usuario(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("tipo_usuario")
            );
            usuarios.add(u);
        }
    } catch (SQLException e) {
        System.out.println("Erro ao listar usuários: " + e.getMessage());
    }
    return usuarios;
}
    
    public static boolean editarUsuario(Usuario usuario) {
    String sql = "UPDATE tb_usuarios SET nome = ?, email = ?, tipo_usuario = ? WHERE cpf = ?";
    try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, usuario.getNome());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getTipo());
        stmt.setString(4, usuario.getCpf());
        int linhasAtualizadas = stmt.executeUpdate();
        return linhasAtualizadas > 0;
    } catch (SQLException e) {
        System.out.println("Erro ao editar usuário: " + e.getMessage());
        return false;
    }
}

    public static boolean excluirUsuario(String cpf) {
    return excluirUsuarioPorCpf(cpf);
}


}
