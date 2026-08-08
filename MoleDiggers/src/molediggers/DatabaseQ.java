
package molediggers;
import java.sql.*;

public class DatabaseQ {

    private Connection connect() throws Exception {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/moletainquest",
            "root",
            ""
        );
    }

    public Question getRandomQuestion(String difficulty) {
        Question q = null;

        try (Connection conn = connect()) {

            String sql = "SELECT * FROM tbl_mquest WHERE Difficulty = ? ORDER BY RAND() LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, difficulty);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                q = new Question(
                    rs.getString("Difficulty"),
                    rs.getString("Questions"),
                    rs.getString("Answers")
                    
                );
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return q;
    }
}

