package com.mycompany.projetoa3.telas.renda;

import com.mycompany.projetoa3.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RendaDAO {

    public static boolean inserirRenda(Renda renda) {
        // Adicionar eh_recorrente ao SQL INSERT
        String sql = "INSERT INTO tb_rendas (descricao, categoria_id, data, valor, cpf_usuario, eh_recorrente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, renda.getDescricao());
            stmt.setInt(2, renda.getIdCategoria());
            stmt.setDate(3, new java.sql.Date(renda.getDataRenda().getTime()));
            stmt.setDouble(4, renda.getValor());
            stmt.setString(5, renda.getCpfUsuario());
            stmt.setBoolean(6, renda.isEhRecorrente()); // Salvar o novo campo
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir renda: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarRenda(Renda renda) {
        // Adicionar eh_recorrente ao SQL UPDATE
        String sql = "UPDATE tb_rendas SET descricao = ?, valor = ?, data = ?, categoria_id = ?, eh_recorrente = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, renda.getDescricao());
            stmt.setDouble(2, renda.getValor());
            stmt.setDate(3, new java.sql.Date(renda.getDataRenda().getTime()));
            stmt.setInt(4, renda.getIdCategoria());
            stmt.setBoolean(5, renda.isEhRecorrente()); // Atualizar o novo campo
            stmt.setInt(6, renda.getId());
            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar renda: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean excluirRenda(int id) {
        String sql = "DELETE FROM tb_rendas WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasExcluidas = stmt.executeUpdate();
            return linhasExcluidas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir Renda: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Renda> listarRenda() {
        List<Renda> lista = new ArrayList<>();
        // Adicionar eh_recorrente ao SELECT
        String sql = "SELECT r.id, r.descricao, r.categoria_id, c.nome AS nome_categoria, r.data, r.valor, r.cpf_usuario, r.eh_recorrente " +
                     "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Renda renda = new Renda();
                renda.setId(rs.getInt("id"));
                renda.setDescricao(rs.getString("descricao"));
                renda.setIdCategoria(rs.getInt("categoria_id"));
                renda.setNomeCategoria(rs.getString("nome_categoria"));
                renda.setDataRenda(rs.getDate("data"));
                renda.setValor(rs.getDouble("valor"));
                renda.setCpfUsuario(rs.getString("cpf_usuario"));
                renda.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                lista.add(renda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar rendas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public static Renda buscarRendaPorld(int id) {
        // Adicionar eh_recorrente ao SELECT
        String sql = "SELECT r.id, r.descricao, r.valor, r.data, r.categoria_id, c.nome AS nome_categoria, r.cpf_usuario, r.eh_recorrente " +
                     "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id WHERE r.id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Renda renda = new Renda();
                renda.setId(rs.getInt("id"));
                renda.setDescricao(rs.getString("descricao"));
                renda.setValor(rs.getDouble("valor"));
                renda.setDataRenda(rs.getDate("data"));
                renda.setIdCategoria(rs.getInt("categoria_id"));
                renda.setNomeCategoria(rs.getString("nome_categoria"));
                renda.setCpfUsuario(rs.getString("cpf_usuario"));
                renda.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                return renda;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar renda por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static List<Renda> listarRendasPorUsuario(String cpfUsuario) {
        List<Renda> lista = new ArrayList<>();
        // Adicionar eh_recorrente ao SELECT
        String sql = "SELECT r.id, r.descricao, r.categoria_id, c.nome AS nome_categoria, r.data, r.valor, r.eh_recorrente " +
                     "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id " +
                     "WHERE r.cpf_usuario = ? ORDER BY r.data DESC";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpfUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Renda renda = new Renda();
                    renda.setId(rs.getInt("id"));
                    renda.setDescricao(rs.getString("descricao"));
                    renda.setIdCategoria(rs.getInt("categoria_id"));
                    renda.setNomeCategoria(rs.getString("nome_categoria"));
                    renda.setDataRenda(rs.getDate("data"));
                    renda.setValor(rs.getDouble("valor"));
                    renda.setCpfUsuario(cpfUsuario);
                    renda.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                    lista.add(renda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar rendas por usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Renda> listarRendasFiltradas(
            String cpfUsuario,
            String dataInicio,
            String dataFim,
            Double valorMin,
            Double valorMax,
            String nomeCategoriaFiltro) {
        List<Renda> lista = new ArrayList<>();
        // Adicionar eh_recorrente ao SELECT
        StringBuilder sql = new StringBuilder(
            "SELECT r.id, r.data, r.descricao, r.categoria_id, c.nome AS nome_categoria, r.valor, r.cpf_usuario, r.eh_recorrente " +
            "FROM tb_rendas r JOIN tb_categorias c ON r.categoria_id = c.id " +
            "WHERE r.cpf_usuario = ?"
        );
        List<Object> params = new ArrayList<>();
        params.add(cpfUsuario);

        if (dataInicio != null && !dataInicio.trim().isEmpty()) {
            sql.append(" AND r.data >= ?");
            params.add(java.sql.Date.valueOf(dataInicio));
        }
        if (dataFim != null && !dataFim.trim().isEmpty()) {
            sql.append(" AND r.data <= ?");
            params.add(java.sql.Date.valueOf(dataFim));
        }
        if (valorMin != null) {
            sql.append(" AND r.valor >= ?");
            params.add(valorMin);
        }
        if (valorMax != null) {
            sql.append(" AND r.valor <= ?");
            params.add(valorMax);
        }
        if (nomeCategoriaFiltro != null && !nomeCategoriaFiltro.trim().isEmpty()) {
            sql.append(" AND c.nome = ?");
            params.add(nomeCategoriaFiltro);
        }
        sql.append(" ORDER BY r.data DESC, r.valor DESC");

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
                renda.setNomeCategoria(rs.getString("nome_categoria"));
                renda.setValor(rs.getDouble("valor"));
                renda.setCpfUsuario(rs.getString("cpf_usuario"));
                renda.setEhRecorrente(rs.getBoolean("eh_recorrente")); // Carregar o novo campo
                lista.add(renda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar rendas filtradas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}