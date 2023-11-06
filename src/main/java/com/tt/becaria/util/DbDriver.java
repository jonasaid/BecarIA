package com.tt.becaria.util;

import com.tt.becaria.model.InsertionResult;
import java.sql.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

/**
 * La clase DbDriver proporciona métodos para interactuar con la base de datos
 * utilizando JDBC.
 */
public class DbDriver {

    private final Connection connection;

    /**
     * Constructor privado de DbDriver. Se usa el patrón de diseño Singleton.
     *
     * @param connection La conexión JDBC establecida con la base de datos.
     */
    private DbDriver(Connection connection) {
        this.connection = connection;
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/becaria";
    private static final String USER = "becariaJona";
    private static final String PASSWORD = "3533";

    private static final int MAX_CONNECTION_ATTEMPTS = 3;
    private static final long CONNECTION_RETRY_DELAY_MS = 5000; // 5 segundos

    /**
     * Obtiene una instancia de DbDriver.
     *
     * @return Una instancia de DbDriver si se puede establecer la conexión con la
     *         base de datos, o null en caso contrario.
     */
    public static DbDriver getInstance() {
        Connection connection = null;
        int connectionAttempts = 0;
        while (connectionAttempts < MAX_CONNECTION_ATTEMPTS) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                return new DbDriver(connection);
            } catch (SQLException e) {
                System.err.println("Error de conexión: " + e.getMessage());
                e.printStackTrace();
                connectionAttempts++;
                if (connectionAttempts < MAX_CONNECTION_ATTEMPTS) {
                    System.out.println("Reintentando la conexión en " + CONNECTION_RETRY_DELAY_MS / 1000 + " segundos...");
                    try {
                        Thread.sleep(CONNECTION_RETRY_DELAY_MS);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * Devuelve el nombre de la primera columna de una tabla.
     *
     * El objetivo de este método es identificar la columna que contiene el ID de
     * cualquier tabla,
     * permitiendo así la inserción de un ID faltante o el siguiente ID disponible.
     *
     * @param tableName El nombre de la tabla.
     * @return El nombre de la primera columna de la tabla.
     */
    public String getFirstColumnName(String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
            if (resultSet.next()) {
                return resultSet.getString("COLUMN_NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encuentra el primer ID faltante en una lista de IDs.
     *
     * El propósito de este método es encontrar el ID faltante más cercano.
     * En caso de que no exista ningún ID faltante, se inserta el siguiente valor
     * disponible,
     * para posteriormente insertarlo en una tabla.
     *
     * @param ids La lista de IDs existentes.
     * @return El primer ID faltante.
     */
    private int findMissingId(List<Integer> ids) {
        int missingId = 1;
        for (Integer id : ids) {
            if (id != missingId) {
                return missingId;
            }
            missingId++;
        }
        return missingId;
    }

    /**
     * Formatea el valor del objeto para su uso en una consulta SQL.
     *
     * @param value El valor del objeto.
     * @return El valor formateado.
     */
    private String getFormattedValue(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return "'" + value + "'";
        } else {
            return value.toString();
        }
    }

    /**
     * Inserta un nuevo registro en una tabla de la base de datos.
     *
     * @param table El nombre de la tabla.
     * @param data  Los datos del registro a insertar.
     * @return true si se inserta el registro correctamente, false en caso
     *         contrario.
     */
    public boolean insert(String table, Map<String, Object> data) {
        // Construye el query INSERT
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(table).append(" (");

        StringBuilder values = new StringBuilder(") VALUES (");

        for (Map.Entry<String, Object> key_value : data.entrySet()) {
            String key = key_value.getKey();
            Object value = key_value.getValue();

            query.append(key).append(",");
            values.append(getFormattedValue(value)).append(",");
        }

        // Elimina la última coma en el query
        query.setLength(query.length() - 1);
        values.setLength(values.length() - 1);

        query.append(values).append(")");

        try {
            PreparedStatement psql = connection.prepareStatement(query.toString());
            int total = psql.executeUpdate();
            return total > 0;
        } catch (SQLException ignored) {
            return false;
        }
    }

    /**
     * Inserta un nuevo registro en la base de datos y devuelve el resultado de la
     * inserción junto con el ID asignado.
     *
     * @param table El nombre de la tabla.
     * @param data  Los datos del registro a insertar.
     * @return El resultado de la inserción junto con el ID asignado.
     */
    public InsertionResult insertWithId(String table, Map<String, Object> data) {
        String columnId = getFirstColumnName(table);
        LinkedList<Map<String, Object>> existingData = select(columnId, table);
        List<Integer> existingIds = existingData.stream()
                .map(row -> (Integer) row.get(columnId))
                .sorted()
                .collect(Collectors.toList());
        int missingId = findMissingId(existingIds);
        data.put(columnId, missingId);
        boolean resultInsert = insert(table, data);
        return new InsertionResult(resultInsert, missingId);
    }

    /**
     * Realiza una consulta SELECT en la base de datos.
     *
     * @param columns        Las columnas a seleccionar.
     * @param tableName      El nombre de la tabla.
     * @param where          Los criterios de búsqueda para la cláusula WHERE (puede
     *                       ser null o vacío).
     * @param isConsultaLike Indica si la consulta utiliza la cláusula LIKE en lugar
     *                       de "=" en las condiciones (true/false).
     * @return Una lista de mapas que representan las filas y columnas
     *         seleccionadas.
     */
    public LinkedList<Map<String, Object>> select(String columns, String tableName, Map<String, Object> where,
                                                  boolean isConsultaLike) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(columns).append(" FROM ").append(tableName);

        if (where != null && !where.isEmpty()) {
            query.append(" WHERE ");

            int index = 0;
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (index > 0) {
                    query.append(isConsultaLike ? " AND " : " OR ");
                }

                query.append(key);

                if (isConsultaLike) {
                    query.append(" LIKE ").append("'").append(value).append("'");
                } else {
                    query.append(" = ").append(getFormattedValue(value));
                }

                index++;
            }
        }

        LinkedList<Map<String, Object>> results = new LinkedList<>();

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString())) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }

        } catch (SQLException ignored) {
        }

        return results;
    }

    /**
     * Realiza una consulta SELECT en la base de datos.
     *
     * @param columns   Las columnas a seleccionar.
     * @param tableName El nombre de la tabla.
     * @return Una lista de mapas que representan las filas y columnas
     *         seleccionadas.
     */
    public LinkedList<Map<String, Object>> select(String columns, String tableName) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(columns).append(" FROM ").append(tableName);

        LinkedList<Map<String, Object>> results = new LinkedList<>();

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString())) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Realiza una consulta JOIN en la base de datos.
     *
     * @param columns        Las columnas a seleccionar.
     * @param tableName1     El nombre de la primera tabla.
     * @param tableName2     El nombre de la segunda tabla.
     * @param joinType       El tipo de JOIN a realizar (INNER JOIN, LEFT JOIN,
     *                       RIGHT JOIN, FULL JOIN).
     * @param whereCondition La condición de unión para el JOIN (puede ser null o
     *                       vacío).
     * @return Una lista de mapas que representan las filas y columnas
     *         seleccionadas.
     */
    public LinkedList<Map<String, Object>> join(String columns, String tableName1, String tableName2, String joinType,
                                                Map<String, Object> whereCondition) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(columns).append(" FROM ").append(tableName1);
        query.append(" ").append(" JOIN ").append(tableName2).append(" ").append(" ON ").append(joinType);

        if (whereCondition != null && !whereCondition.isEmpty()) {
            query.append(" WHERE ");
            for (Map.Entry<String, Object> entry : whereCondition.entrySet()) {
                String columnName = entry.getKey();
                Object columnValue = entry.getValue();
                query.append(columnName).append(" = '").append(columnValue).append("' AND ");
            }
            query.delete(query.length() - 5, query.length());
        }

        LinkedList<Map<String, Object>> results = new LinkedList<>();

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString())) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Actualiza registros en la base de datos.
     *
     * @param table La tabla en la que se realizará la actualización.
     * @param data  Los datos a actualizar.
     * @param where La condición de actualización.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean update(String table, Map<String, Object> data, Map<String, Object> where) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ").append(table).append(" SET ");

            List<String> setStatements = new ArrayList<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String column = entry.getKey();
                Object value = entry.getValue();
                String setStatement = column + " = " + getFormattedValue(value);
                setStatements.add(setStatement);
            }
            queryBuilder.append(String.join(", ", setStatements));

            queryBuilder.append(" WHERE ");
            List<String> whereStatements = new ArrayList<>();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                String column = entry.getKey();
                Object value = entry.getValue();
                String whereStatement = column + " = " + getFormattedValue(value);
                whereStatements.add(whereStatement);
            }
            queryBuilder.append(String.join(" AND ", whereStatements));

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina registros de la base de datos.
     *
     * @param table La tabla de la que se eliminarán los registros.
     * @param where La condición de eliminación.
     * @return true si se eliminan registros correctamente, false en caso contrario.
     */
    public boolean delete(String table, Map<String, Object> where) {
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("DELETE FROM ").append(table).append(" WHERE ");

            List<String> whereStatements = new ArrayList<>();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                String column = entry.getKey();
                Object value = entry.getValue();
                String whereStatement = column + " = " + getFormattedValue(value);
                whereStatements.add(whereStatement);
            }
            queryBuilder.append(String.join(" AND ", whereStatements));

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
