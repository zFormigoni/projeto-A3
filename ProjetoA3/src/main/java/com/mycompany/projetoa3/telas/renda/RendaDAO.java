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
        String sql = "INSERT INTO tb_rendas (descricao, categoria_id, data, valor, cpf_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, renda.getDescricao());
            stmt.setInt(2, renda.getIdCategoria());
            stmt.setDate(3, new java.sql.Date(renda.getDataRenda().getTime()));
            stmt.setDouble(4, renda.getValor());
            stmt.setString(5, renda.getCpfUsuario());


            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir renda: " + e.getMessage());
            return false;
        }
    }

    public static boolean atualizarRenda(Renda renda) {
        String sql = "UPDATE tb_rendas SET descricao = ?, valor = ?, data = ?, categoria_id = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, renda.getDescricao());
            stmt.setDouble(2, renda.getValor());
            stmt.setDate(3, new java.sql.Date(renda.getDataRenda().getTime()));
            stmt.setInt(4, renda.getIdCategoria());
            stmt.setInt(5, renda.getId());

            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar renda: " + e.getMessage());
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
            System.out.println("Erro ao excluir Renda: " + e.getMessage());
            return false;
        }
    }

    public static List<Renda> listarRenda() {
        List<Renda> lista = new ArrayList<>();
        String sql = "SELECT id, descricao, categoria_id, data, valor FROM tb_gastos";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Renda renda = new Renda();
                renda.setId(rs.getInt("id"));
                renda.setDescricao(rs.getString("descricao"));
                renda.setIdCategoria(rs.getInt("categoria_id"));
                renda.setDataRenda(rs.getDate("data"));
                renda.setValor(rs.getDouble("valor"));
                renda.setNomeCategoria(rs.getString("nome_categoria"));

                lista.add(renda);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos: " + e.getMessage());
        }

        return lista;
    }

    public static Renda buscarRendaPorId(int id) {
        String sql = "SELECT id, descricao, valor, data, categoria_id FROM tb_rendas WHERE id = ?";
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
                return renda;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar renda: " + e.getMessage());
        }
        return null;
    }
    
    public static List<Renda> listarRendasPorUsuario(String cpfUsuario) {
        List<Renda> lista = new ArrayList<>();
        String sql = "SELECT id, descricao, categoria_id, data, valor FROM tb_rendas WHERE cpf_usuario = ?";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Renda renda = new Renda();
                    renda.setId(rs.getInt("id"));
                    renda.setDescricao(rs.getString("descricao"));
                    renda.setIdCategoria(rs.getInt("categoria_id"));
                    renda.setDataRenda(rs.getDate("data"));
                    renda.setValor(rs.getDouble("valor"));
                    renda.setCpfUsuario(cpfUsuario);

                    lista.add(renda);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos por usuário: " + e.getMessage());
        }

        return lista;
    }
            public static List<Renda> listarGastosFiltrados(
        String cpfUsuario,
        String dataInicio,
        String dataFim,
        Double valorMin,
        Double valorMax,
        String nomeCategoria
    ) {
        List<Renda> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.data, r.descricao, r.categoria_id, r.valor " +
                     "FROM tb_rendas r JOIN tb_categorias c ON g.categoria_id = c.id " +
                     "WHERE r.cpf_usuario = ? ";

        List<Object> params = new ArrayList<>();
        params.add(cpfUsuario);

        if (dataInicio != null) {
            sql += " AND r.data >= ? ";
            params.add(java.sql.Date.valueOf(dataInicio));
        }
        if (dataFim != null) {
            sql += " AND r.data <= ? ";
            params.add(java.sql.Date.valueOf(dataFim));
        }
        if (valorMin != null) {
            sql += " AND r.valor >= ? ";
            params.add(valorMin);
        }
        if (valorMax != null) {
            sql += " AND r.valor <= ? ";
            params.add(valorMax);
        }
        if (nomeCategoria != null) {
            sql += " AND c.nome = ? ";
            params.add(nomeCategoria);
        }

        sql += " ORDER BY r.valor DESC";

        // Preparar e executar PreparedStatement, preencher lista de Gasto...

        return lista;
    }
}
