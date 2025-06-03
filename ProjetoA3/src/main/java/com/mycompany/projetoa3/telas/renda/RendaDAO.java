package com.mycompany.projetoa3.telas.renda;

import com.mycompany.projetoa3.ConexaoDB; // [cite: 314]
import java.sql.Connection; // [cite: 314]
import java.sql.PreparedStatement; // [cite: 314]
import java.sql.ResultSet; // [cite: 314]
import java.sql.SQLException; // [cite: 314]
import java.util.ArrayList; // [cite: 314]
import java.util.List; // [cite: 314]

public class RendaDAO {

    public static boolean inserirRenda(Renda renda) {
        String sql = "INSERT INTO tb_rendas (descricao, categoria_id, data, valor, cpf_usuario) VALUES (?, ?, ?, ?, ?)"; // [cite: 315]
        try (Connection conn = ConexaoDB.conectar(); // [cite: 316]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 316]
            stmt.setString(1, renda.getDescricao()); // [cite: 317]
            stmt.setInt(2, renda.getIdCategoria()); // [cite: 317]
            stmt.setDate(3, new java.sql.Date(renda.getDataRenda().getTime())); // [cite: 317]
            stmt.setDouble(4, renda.getValor()); // [cite: 317]
            stmt.setString(5, renda.getCpfUsuario()); // [cite: 317]
            stmt.executeUpdate(); // [cite: 317]
            return true; // [cite: 318]
        } catch (SQLException e) {
            System.out.println("Erro ao inserir renda: " + e.getMessage()); // [cite: 318]
            return false; // [cite: 318]
        }
    }

    public static boolean atualizarRenda(Renda renda) {
        String sql = "UPDATE tb_rendas SET descricao = ?, valor = ?, data = ?, categoria_id = ? WHERE id = ?"; // [cite: 319]
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, renda.getDescricao()); // [cite: 319]
            stmt.setDouble(2, renda.getValor()); // [cite: 320]
            stmt.setDate(3, new java.sql.Date(renda.getDataRenda().getTime())); // [cite: 320]
            stmt.setInt(4, renda.getIdCategoria()); // [cite: 320]
            stmt.setInt(5, renda.getId()); // [cite: 320]
            int linhasAtualizadas = stmt.executeUpdate(); // [cite: 320]
            return linhasAtualizadas > 0; // [cite: 320]
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar renda: " + e.getMessage()); // [cite: 321]
            return false; // [cite: 321]
        }
    }

    public static boolean excluirRenda(int id) {
        String sql = "DELETE FROM tb_rendas WHERE id = ?"; // [cite: 322]
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 322]
            stmt.setInt(1, id); // [cite: 322]
            int linhasExcluidas = stmt.executeUpdate(); // [cite: 323]
            return linhasExcluidas > 0; // [cite: 323]
        } catch (SQLException e) {
            System.out.println("Erro ao excluir Renda: " + e.getMessage()); // [cite: 323]
            return false; // [cite: 324]
        }
    }

    public static List<Renda> listarRenda() {
        List<Renda> lista = new ArrayList<>();
        // CORRIGIDO: Consulta tb_rendas e faz JOIN para buscar nome da categoria
        String sql = "SELECT r.id, r.descricao, r.categoria_id, c.nome AS nome_categoria, r.data, r.valor, r.cpf_usuario " +
                     "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id";
        try (Connection conn = ConexaoDB.conectar(); // [cite: 326]
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // [cite: 326]
            while (rs.next()) {
                Renda renda = new Renda();
                renda.setId(rs.getInt("id")); // [cite: 327]
                renda.setDescricao(rs.getString("descricao")); // [cite: 327]
                renda.setIdCategoria(rs.getInt("categoria_id")); // [cite: 327]
                renda.setNomeCategoria(rs.getString("nome_categoria")); // Nome da categoria do JOIN
                renda.setDataRenda(rs.getDate("data")); // [cite: 327]
                renda.setValor(rs.getDouble("valor")); // [cite: 327]
                // Se cpf_usuario for necessário no objeto Renda em outros contextos:
                // renda.setCpfUsuario(rs.getString("cpf_usuario"));
                lista.add(renda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar rendas: " + e.getMessage()); // Mensagem de erro genérica, pode ser melhorada [cite: 327]
        }
        return lista; // [cite: 328]
    }

    public static Renda buscarRendaPorld(int id) {
        // CORRIGIDO: Faz JOIN para buscar nome da categoria
        String sql = "SELECT r.id, r.descricao, r.valor, r.data, r.categoria_id, c.nome AS nome_categoria, r.cpf_usuario " +
                     "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id WHERE r.id = ?";
        try (Connection conn = ConexaoDB.conectar(); // [cite: 329]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 329]
            stmt.setInt(1, id); // [cite: 329]
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // [cite: 330]
                Renda renda = new Renda();
                renda.setId(rs.getInt("id")); // [cite: 330]
                renda.setDescricao(rs.getString("descricao")); // [cite: 330]
                renda.setValor(rs.getDouble("valor")); // [cite: 330]
                renda.setDataRenda(rs.getDate("data")); // [cite: 330]
                renda.setIdCategoria(rs.getInt("categoria_id")); // [cite: 330]
                renda.setNomeCategoria(rs.getString("nome_categoria")); // Nome da categoria
                // Se cpf_usuario for necessário:
                // renda.setCpfUsuario(rs.getString("cpf_usuario"));
                return renda; // [cite: 330]
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar renda: " + e.getMessage()); // [cite: 331]
        }
        return null; // [cite: 331]
    }

    public static List<Renda> listarRendasPorUsuario(String cpfUsuario) {
        List<Renda> lista = new ArrayList<>();
        // CORRIGIDO: Faz JOIN para buscar nome da categoria
        String sql = "SELECT r.id, r.descricao, r.categoria_id, c.nome AS nome_categoria, r.data, r.valor " +
                     "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id " +
                     "WHERE r.cpf_usuario = ?"; // [cite: 333]
        try (Connection conn = ConexaoDB.conectar(); // [cite: 334]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 334]
            stmt.setString(1, cpfUsuario); // [cite: 334]
            try (ResultSet rs = stmt.executeQuery()) { // [cite: 335]
                while (rs.next()) {
                    Renda renda = new Renda();
                    renda.setId(rs.getInt("id")); // [cite: 335]
                    renda.setDescricao(rs.getString("descricao")); // [cite: 335]
                    renda.setIdCategoria(rs.getInt("categoria_id")); // [cite: 335]
                    renda.setNomeCategoria(rs.getString("nome_categoria")); // Preenche o nome da categoria
                    renda.setDataRenda(rs.getDate("data")); // [cite: 335]
                    renda.setValor(rs.getDouble("valor")); // [cite: 335]
                    renda.setCpfUsuario(cpfUsuario); // [cite: 335]
                    lista.add(renda);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar rendas por usuário: " + e.getMessage()); // Mensagem de erro genérica [cite: 336]
        }
        return lista; // [cite: 336]
    }

    // MÉTODO RENOMEADO E CORRIGIDO
    public static List<Renda> listarRendasFiltradas(
            String cpfUsuario,
            String dataInicio, // Espera-se formato "YYYY-MM-DD"
            String dataFim,    // Espera-se formato "YYYY-MM-DD"
            Double valorMin,
            Double valorMax,
            String nomeCategoriaFiltro) {
        List<Renda> lista = new ArrayList<>();
        // CORRIGIDO: Alias da tabela e condição de JOIN, e seleciona nome da categoria
        StringBuilder sql = new StringBuilder(
            "SELECT r.id, r.data, r.descricao, r.categoria_id, c.nome AS nome_categoria, r.valor, r.cpf_usuario " +
            "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id " + // Condição de JOIN corrigida para r.categoria_id
            "WHERE r.cpf_usuario = ?" // [cite: 339]
        );
        List<Object> params = new ArrayList<>();
        params.add(cpfUsuario); // [cite: 339]

        if (dataInicio != null && !dataInicio.isEmpty()) {
            sql.append(" AND r.data >= ?"); // [cite: 339]
            params.add(java.sql.Date.valueOf(dataInicio)); // [cite: 339]
        }
        if (dataFim != null && !dataFim.isEmpty()) {
            sql.append(" AND r.data <= ?"); // [cite: 340]
            params.add(java.sql.Date.valueOf(dataFim)); // [cite: 340]
        }
        if (valorMin != null) {
            sql.append(" AND r.valor >= ?"); // [cite: 340]
            params.add(valorMin);
        }
        if (valorMax != null) {
            sql.append(" AND r.valor <= ?"); // [cite: 340]
            params.add(valorMax);
        }
        if (nomeCategoriaFiltro != null && !nomeCategoriaFiltro.trim().isEmpty()) {
            sql.append(" AND c.nome = ?"); // [cite: 340]
            params.add(nomeCategoriaFiltro); // [cite: 341]
        }
        sql.append(" ORDER BY r.data DESC, r.valor DESC"); // [cite: 341] (data adicionada para melhor visualização)

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Renda renda = new Renda();
                renda.setId(rs.getInt("id"));
                renda.setDataRenda(rs.getDate("data"));
                renda.setDescricao(rs.getString("descricao"));
                renda.setIdCategoria(rs.getInt("categoria_id"));
                renda.setNomeCategoria(rs.getString("nome_categoria")); // Preenche nome da categoria
                renda.setValor(rs.getDouble("valor"));
                renda.setCpfUsuario(rs.getString("cpf_usuario"));
                lista.add(renda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar rendas filtradas: " + e.getMessage());
            e.printStackTrace(); // Para debug
        }
        return lista; // [cite: 341]
    }
}