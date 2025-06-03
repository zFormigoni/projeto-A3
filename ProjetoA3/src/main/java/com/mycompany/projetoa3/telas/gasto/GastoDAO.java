package com.mycompany.projetoa3.telas.gasto;

import com.mycompany.projetoa3.ConexaoDB; // [cite: 222]
import java.sql.Connection; // [cite: 222]
import java.sql.PreparedStatement; // [cite: 222]
import java.sql.ResultSet; // [cite: 222]
import java.sql.SQLException; // [cite: 222]
import java.util.ArrayList; // [cite: 222]
import java.util.List; // [cite: 222]

public class GastoDAO {

    public static boolean inserirGasto(Gasto gasto) {
        String sql = "INSERT INTO tb_gastos (descricao, categoria_id, data, valor, cpf_usuario) VALUES (?, ?, ?, ?, ?)"; // [cite: 222]
        try (Connection conn = ConexaoDB.conectar(); // [cite: 223]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 223]
            stmt.setString(1, gasto.getDescricao()); // [cite: 223]
            stmt.setInt(2, gasto.getIdCategoria()); // [cite: 223]
            stmt.setDate(3, new java.sql.Date(gasto.getDataGasto().getTime())); // [cite: 223]
            stmt.setDouble(4, gasto.getValor()); // [cite: 223]
            stmt.setString(5, gasto.getCpfUsuario()); // [cite: 223]
            stmt.executeUpdate(); // [cite: 224]
            return true; // [cite: 224]
        } catch (SQLException e) {
            System.out.println("Erro ao inserir gasto: " + e.getMessage()); // [cite: 224]
            return false; // [cite: 225]
        }
    }

    public static boolean atualizarGasto(Gasto gasto) {
        String sql = "UPDATE tb_gastos SET descricao = ?, valor = ?, data = ?, categoria_id = ? WHERE id = ?"; // [cite: 225]
        try (Connection conn = ConexaoDB.conectar(); // [cite: 226]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 226]
            stmt.setString(1, gasto.getDescricao()); // [cite: 226]
            stmt.setDouble(2, gasto.getValor()); // [cite: 226]
            stmt.setDate(3, new java.sql.Date(gasto.getDataGasto().getTime())); // [cite: 227]
            stmt.setInt(4, gasto.getIdCategoria()); // [cite: 227]
            stmt.setInt(5, gasto.getId()); // [cite: 227]
            int linhasAtualizadas = stmt.executeUpdate(); // [cite: 227]
            return linhasAtualizadas > 0; // [cite: 227]
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar gasto: " + e.getMessage()); // [cite: 228]
            return false; // [cite: 228]
        }
    }

    public static boolean excluirGasto(int id) {
        String sql = "DELETE FROM tb_gastos WHERE id = ?"; // [cite: 229]
        try (Connection conn = ConexaoDB.conectar(); // [cite: 230]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 230]
            stmt.setInt(1, id); // [cite: 230]
            int linhasExcluidas = stmt.executeUpdate(); // [cite: 230]
            return linhasExcluidas > 0; // [cite: 230]
        } catch (SQLException e) {
            System.out.println("Erro ao excluir gasto: " + e.getMessage()); // [cite: 231]
            return false; // [cite: 231]
        }
    }

    public static List<Gasto> listarGastos() {
        List<Gasto> lista = new ArrayList<>();
        // CORRIGIDO: JOIN com tb_categorias para obter nome_categoria
        String sql = "SELECT g.id, g.descricao, g.categoria_id, c.nome AS nome_categoria, g.data, g.valor, g.cpf_usuario " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id"; // [cite: 232] (SQL alterado)
        try (Connection conn = ConexaoDB.conectar(); // [cite: 233]
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // [cite: 233]
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id")); // [cite: 233]
                gasto.setDescricao(rs.getString("descricao")); // [cite: 233]
                gasto.setIdCategoria(rs.getInt("categoria_id")); // [cite: 234]
                gasto.setNomeCategoria(rs.getString("nome_categoria")); // Populado aqui! [cite: 234]
                gasto.setDataGasto(rs.getDate("data")); // [cite: 234]
                gasto.setValor(rs.getDouble("valor")); // [cite: 234]
                // Se cpf_usuario for necessário no objeto Gasto em outros contextos:
                // gasto.setCpfUsuario(rs.getString("cpf_usuario"));
                lista.add(gasto); // [cite: 234]
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos: " + e.getMessage()); // [cite: 235]
        }
        return lista; // [cite: 235]
    }

    public static Gasto buscarGastoPorld(int id) {
        // CORRIGIDO: JOIN com tb_categorias para obter nome_categoria
        String sql = "SELECT g.id, g.descricao, g.valor, g.data, g.categoria_id, c.nome AS nome_categoria, g.cpf_usuario " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id WHERE g.id = ?"; // [cite: 236] (SQL alterado)
        try (Connection conn = ConexaoDB.conectar(); // [cite: 237]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 237]
            stmt.setInt(1, id); // [cite: 237]
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // [cite: 238]
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id")); // [cite: 238]
                gasto.setDescricao(rs.getString("descricao")); // [cite: 238]
                gasto.setValor(rs.getDouble("valor")); // [cite: 238]
                gasto.setDataGasto(rs.getDate("data")); // [cite: 238]
                gasto.setIdCategoria(rs.getInt("categoria_id")); // [cite: 238]
                gasto.setNomeCategoria(rs.getString("nome_categoria")); // Populado aqui!
                // Se cpf_usuario for necessário:
                // gasto.setCpfUsuario(rs.getString("cpf_usuario"));
                return gasto; // [cite: 238]
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar gasto: " + e.getMessage()); // [cite: 239]
        }
        return null; // [cite: 239]
    }

    public static List<Gasto> listarGastosPorUsuario(String cpfUsuario) {
        List<Gasto> lista = new ArrayList<>();
        // CORRIGIDO: JOIN com tb_categorias para obter nome_categoria
        String sql = "SELECT g.id, g.descricao, g.categoria_id, c.nome AS nome_categoria, g.data, g.valor " +
                     "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id " +
                     "WHERE g.cpf_usuario = ?"; // [cite: 241] (SQL alterado)
        try (Connection conn = ConexaoDB.conectar(); // [cite: 242]
             PreparedStatement stmt = conn.prepareStatement(sql)) { // [cite: 242]
            stmt.setString(1, cpfUsuario); // [cite: 242]
            try (ResultSet rs = stmt.executeQuery()) { // [cite: 243]
                while (rs.next()) {
                    Gasto gasto = new Gasto();
                    gasto.setId(rs.getInt("id")); // [cite: 243]
                    gasto.setDescricao(rs.getString("descricao")); // [cite: 243]
                    gasto.setIdCategoria(rs.getInt("categoria_id")); // [cite: 243]
                    gasto.setNomeCategoria(rs.getString("nome_categoria")); // Populado aqui!
                    gasto.setDataGasto(rs.getDate("data")); // [cite: 244]
                    gasto.setValor(rs.getDouble("valor")); // [cite: 244]
                    gasto.setCpfUsuario(cpfUsuario); // [cite: 244]
                    lista.add(gasto); // [cite: 244]
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos por usuário: " + e.getMessage()); // [cite: 244]
        }
        return lista; // [cite: 244]
    }

    public static List<Gasto> listarGastosFiltrados(
            String cpfUsuario,
            String dataInicio, // Espera-se formato "YYYY-MM-DD"
            String dataFim,    // Espera-se formato "YYYY-MM-DD"
            Double valorMin,
            Double valorMax,
            String nomeCategoriaFiltro) { // Nome da categoria para filtrar, não o ID
        List<Gasto> lista = new ArrayList<>();
        // SQL já faz o JOIN, apenas garantir que c.nome seja selecionado e usado.
        StringBuilder sql = new StringBuilder(
            "SELECT g.id, g.data, g.descricao, g.categoria_id, c.nome AS nome_categoria, g.valor, g.cpf_usuario " +
            "FROM tb_gastos g JOIN tb_categorias c ON g.categoria_id = c.id " + // [cite: 246]
            "WHERE g.cpf_usuario = ?" // [cite: 246]
        );
        List<Object> params = new ArrayList<>();
        params.add(cpfUsuario); // [cite: 247]

        if (dataInicio != null && !dataInicio.isEmpty()) {
            sql.append(" AND g.data >= ?"); // [cite: 247]
            params.add(java.sql.Date.valueOf(dataInicio)); // [cite: 247]
        }
        if (dataFim != null && !dataFim.isEmpty()) {
            sql.append(" AND g.data <= ?"); // [cite: 247]
            params.add(java.sql.Date.valueOf(dataFim)); // [cite: 247]
        }
        if (valorMin != null) {
            sql.append(" AND g.valor >= ?"); // [cite: 247]
            params.add(valorMin); // [cite: 247]
        }
        if (valorMax != null) {
            sql.append(" AND g.valor <= ?"); // [cite: 247]
            params.add(valorMax); // [cite: 247]
        }
        // O filtro por nomeCategoria já está na query JOIN, agora aplicamos o WHERE nele
        if (nomeCategoriaFiltro != null && !nomeCategoriaFiltro.trim().isEmpty()) {
            sql.append(" AND c.nome = ?"); // [cite: 247]
            params.add(nomeCategoriaFiltro); // [cite: 247]
        }
        sql.append(" ORDER BY g.data DESC, g.valor DESC"); // [cite: 248] (data adicionada para melhor visualização)

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            // A parte de popular a lista estava faltando no PDF[cite: 248], adicionada abaixo:
            while (rs.next()) {
                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id"));
                gasto.setDataGasto(rs.getDate("data"));
                gasto.setDescricao(rs.getString("descricao"));
                gasto.setIdCategoria(rs.getInt("categoria_id"));
                gasto.setNomeCategoria(rs.getString("nome_categoria")); // Populado aqui!
                gasto.setValor(rs.getDouble("valor"));
                gasto.setCpfUsuario(rs.getString("cpf_usuario"));
                lista.add(gasto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos filtrados: " + e.getMessage());
            e.printStackTrace(); // Para debug
        }
        return lista; // [cite: 248]
    }
}