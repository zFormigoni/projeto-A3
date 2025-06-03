package com.mycompany.projetoa3.telas.gasto;

import com.mycompany.projetoa3.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO {

    public static boolean inserirGasto(Gasto gasto) {
        String sql = "INSERT INTO tb_gastos (descricao, categoria_id, data, valor, cpf_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gasto.getDescricao());
            stmt.setInt(2, gasto.getIdCategoria());
            stmt.setDate(3, new java.sql.Date(gasto.getDataGasto().getTime()));
            stmt.setDouble(4, gasto.getValor());
            stmt.setString(5, gasto.getCpfUsuario());


            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir gasto: " + e.getMessage());
            return false;
        }
    }

    public static boolean atualizarGasto(Gasto gasto) {
        String sql = "UPDATE tb_gastos SET descricao = ?, valor = ?, data = ?, categoria_id = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gasto.getDescricao());
            stmt.setDouble(2, gasto.getValor());
            stmt.setDate(3, new java.sql.Date(gasto.getDataGasto().getTime()));
            stmt.setInt(4, gasto.getIdCategoria());
            stmt.setInt(5, gasto.getId());

            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar gasto: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluirGasto(int id) {
        String sql = "DELETE FROM tb_gastos WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasExcluidas = stmt.executeUpdate();
            return linhasExcluidas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir gasto: " + e.getMessage());
            return false;
        }
    }

    public static List<Gasto> listarGastos() {
        List<Gasto> lista = new ArrayList<>();
        String sql = "SELECT id, descricao, categoria_id, data, valor FROM tb_gastos";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setIdCategoria(rs.getInt("categoria_id"));
                gasto.setDataGasto(rs.getDate("data"));
                gasto.setValor(rs.getDouble("valor"));
                gasto.setNomeCategoria(rs.getString("nome_categoria"));

                lista.add(gasto);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos: " + e.getMessage());
        }

        return lista;
    }

    public static Gasto buscarGastoPorId(int id) {
        String sql = "SELECT id, descricao, valor, data, categoria_id FROM tb_gastos WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setValor(rs.getDouble("valor"));
                gasto.setDataGasto(rs.getDate("data"));
                gasto.setIdCategoria(rs.getInt("categoria_id"));
                return gasto;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar gasto: " + e.getMessage());
        }
        return null;
    }
    
    public static List<Gasto> listarGastosPorUsuario(String cpfUsuario) {
        List<Gasto> lista = new ArrayList<>();
        String sql = "SELECT id, descricao, categoria_id, data, valor FROM tb_gastos WHERE cpf_usuario = ?";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Gasto gasto = new Gasto();
                    gasto.setId(rs.getInt("id"));
                    gasto.setDescricao(rs.getString("descricao"));
                    gasto.setIdCategoria(rs.getInt("categoria_id"));
                    gasto.setDataGasto(rs.getDate("data"));
                    gasto.setValor(rs.getDouble("valor"));
                    gasto.setCpfUsuario(cpfUsuario);

                    lista.add(gasto);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos por usuário: " + e.getMessage());
        }

        return lista;
    }
            public static List<Gasto> listarGastosFiltrados(
        String cpfUsuario,
        String dataInicio,
        String dataFim,
        Double valorMin,
        Double valorMax,
        String nomeCategoria
    ) {
        List<Gasto> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.data, g.descricao, g.categoria_id, g.valor " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id " +
                     "WHERE g.cpf_usuario = ? ";

        List<Object> params = new ArrayList<>();
        params.add(cpfUsuario);

        if (dataInicio != null) {
            sql += " AND g.data >= ? ";
            params.add(java.sql.Date.valueOf(dataInicio));
        }
        if (dataFim != null) {
            sql += " AND g.data <= ? ";
            params.add(java.sql.Date.valueOf(dataFim));
        }
        if (valorMin != null) {
            sql += " AND g.valor >= ? ";
            params.add(valorMin);
        }
        if (valorMax != null) {
            sql += " AND g.valor <= ? ";
            params.add(valorMax);
        }
        if (nomeCategoria != null) {
            sql += " AND c.nome = ? ";
            params.add(nomeCategoria);
        }

        sql += " ORDER BY g.valor DESC";

        // Preparar e executar PreparedStatement, preencher lista de Gasto...

        return lista;
    }
}
