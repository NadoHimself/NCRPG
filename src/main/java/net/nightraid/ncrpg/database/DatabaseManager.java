package net.nightraid.ncrpg.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.nightraid.ncrpg.NCRPG;
import net.nightraid.ncrpg.models.PlayerSkillData;
import net.nightraid.ncrpg.models.SkillType;

import java.sql.*;
import java.util.*;

/**
 * Database manager with async operations and connection pooling
 */
public class DatabaseManager {

    private final NCRPG plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(NCRPG plugin) {
        this.plugin = plugin;
    }

    /**
     * Connect to database with HikariCP pooling
     */
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + 
            plugin.getConfigManager().getString("database.host", "localhost") + ":" +
            plugin.getConfigManager().getInt("database.port", 3306) + "/" +
            plugin.getConfigManager().getString("database.database", "ncrpg"));
        config.setUsername(plugin.getConfigManager().getString("database.username", "root"));
        config.setPassword(plugin.getConfigManager().getString("database.password", "password"));
        config.setMaximumPoolSize(plugin.getConfigManager().getInt("database.pool-size", 10));
        config.setConnectionTimeout(plugin.getConfigManager().getInt("database.connection-timeout", 30000));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        try {
            dataSource = new HikariDataSource(config);
            plugin.getLogger().info("Database connected successfully!");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create database tables
     */
    public void createTables() {
        String playersTable = "CREATE TABLE IF NOT EXISTS ncrpg_players (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "name VARCHAR(16) NOT NULL," +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";

        String skillsTable = "CREATE TABLE IF NOT EXISTS ncrpg_skills (" +
                "uuid VARCHAR(36) NOT NULL," +
                "skill VARCHAR(32) NOT NULL," +
                "level INT NOT NULL DEFAULT 1," +
                "xp DOUBLE NOT NULL DEFAULT 0," +
                "skill_cap INT NOT NULL DEFAULT 100," +
                "PRIMARY KEY (uuid, skill)," +
                "FOREIGN KEY (uuid) REFERENCES ncrpg_players(uuid) ON DELETE CASCADE" +
                ")";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(playersTable);
            stmt.execute(skillsTable);
            plugin.getLogger().info("Database tables created successfully!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save player to database
     */
    public void savePlayer(UUID uuid, String name) {
        String sql = "INSERT INTO ncrpg_players (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, name);
            stmt.setString(3, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player: " + e.getMessage());
        }
    }

    /**
     * Load all skills for a player
     */
    public Map<SkillType, PlayerSkillData> loadPlayerSkills(UUID uuid) {
        Map<SkillType, PlayerSkillData> skills = new HashMap<>();
        String sql = "SELECT skill, level, xp, skill_cap FROM ncrpg_skills WHERE uuid=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String skillName = rs.getString("skill");
                SkillType skillType = SkillType.fromString(skillName);
                if (skillType != null) {
                    int level = rs.getInt("level");
                    double xp = rs.getDouble("xp");
                    int skillCap = rs.getInt("skill_cap");
                    skills.put(skillType, new PlayerSkillData(uuid, skillType, level, xp, skillCap));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load player skills: " + e.getMessage());
        }

        return skills;
    }

    /**
     * Save skill data
     */
    public void saveSkillData(PlayerSkillData data) {
        String sql = "INSERT INTO ncrpg_skills (uuid, skill, level, xp, skill_cap) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE level=?, xp=?, skill_cap=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, data.getUuid().toString());
            stmt.setString(2, data.getSkillType().name());
            stmt.setInt(3, data.getLevel());
            stmt.setDouble(4, data.getXp());
            stmt.setInt(5, data.getSkillCap());
            stmt.setInt(6, data.getLevel());
            stmt.setDouble(7, data.getXp());
            stmt.setInt(8, data.getSkillCap());
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save skill data: " + e.getMessage());
        }
    }

    /**
     * Get leaderboard for a specific skill
     */
    public List<Map.Entry<String, Integer>> getSkillLeaderboard(SkillType skillType, int limit) {
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>();
        String sql = "SELECT p.name, s.level FROM ncrpg_skills s " +
                     "JOIN ncrpg_players p ON s.uuid = p.uuid " +
                     "WHERE s.skill = ? ORDER BY s.level DESC, s.xp DESC LIMIT ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, skillType.name());
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int level = rs.getInt("level");
                leaderboard.add(new AbstractMap.SimpleEntry<>(name, level));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get leaderboard: " + e.getMessage());
        }

        return leaderboard;
    }

    /**
     * Get power level leaderboard
     */
    public List<Map.Entry<String, Integer>> getPowerLevelLeaderboard(int limit) {
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>();
        String sql = "SELECT p.name, SUM(s.level) as power_level FROM ncrpg_skills s " +
                     "JOIN ncrpg_players p ON s.uuid = p.uuid " +
                     "GROUP BY s.uuid, p.name ORDER BY power_level DESC LIMIT ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int powerLevel = rs.getInt("power_level");
                leaderboard.add(new AbstractMap.SimpleEntry<>(name, powerLevel));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get power level leaderboard: " + e.getMessage());
        }

        return leaderboard;
    }

    /**
     * Close database connection
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().info("Database connection closed.");
        }
    }
}
