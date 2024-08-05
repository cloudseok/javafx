package com.itgroup.dao;

import com.itgroup.bean.Player;
import com.itgroup.utility.Paging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDao extends SuperDao {

    public List<Player> getPaginationData(Paging pageInfo) {
        List<Player> allData = new ArrayList<>();

        String sql = "SELECT pnum, name, weight, rank, record, image, nationality, style, age, debut "
                + "FROM ( "
                + "   SELECT pnum, name, weight, rank, record, image, nationality, style, age, debut, "
                + "          ROW_NUMBER() OVER ("
                + "              ORDER BY "
                + "                  CASE WHEN rank = 'C' THEN 0 ELSE 1 END, "
                + "                  CASE WHEN rank = 'C' THEN NULL ELSE TO_NUMBER(REGEXP_SUBSTR(rank, '[0-9]+')) END "
                + "          ) AS ranking "
                + "   FROM players ";

        String mode = pageInfo.getMode();
        boolean isAllMode = mode == null || mode.equals("null") || mode.isEmpty() || mode.equals("all");

        if (!isAllMode) {
            sql += " WHERE weight = ? ";
        }
        sql += " )  "
                + "WHERE ranking BETWEEN ? AND ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int parameterIndex = 1;
            if (!isAllMode) {
                pstmt.setString(parameterIndex++, mode);
            }
            pstmt.setInt(parameterIndex++, pageInfo.getBeginRow());
            pstmt.setInt(parameterIndex, pageInfo.getEndRow());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Player bean = makeBean(rs);
                    allData.add(bean);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return allData;
    }

    public int deleteData(int pnum) {
        System.out.println("기본 키=" + pnum);
        String sql = "DELETE FROM players WHERE pnum = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            pstmt.setInt(1, pnum);

            int cnt = pstmt.executeUpdate();
            conn.commit();
            return cnt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try (Connection conn = super.getConnection()) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return -1;
        }
    }

    public int updateData(Player bean) {
        System.out.println(bean);
        String sql = "UPDATE players SET name = ?, weight = ?, rank = ?, record = ?, image = ?, nationality = ?, style = ?, age = ?, debut = ? WHERE pnum = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getWeight());
            pstmt.setString(3, bean.getRank());
            pstmt.setString(4, bean.getRecord());
            pstmt.setString(5, bean.getImage());
            pstmt.setString(6, bean.getNationality());
            pstmt.setString(7, bean.getStyle());
            pstmt.setInt(8, bean.getAge());
            pstmt.setString(9, bean.getDebut());
            pstmt.setInt(10, bean.getPnum());

            int cnt = pstmt.executeUpdate();
            conn.commit();
            return cnt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try (Connection conn = super.getConnection()) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return -1;
        }
    }

    public int insertData(Player bean) {
        System.out.println(bean);
        String sql = "INSERT INTO players(pnum, name, weight, rank, record, image, nationality, style, age, debut) VALUES(seqplayer.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getWeight());
            pstmt.setString(3, bean.getRank());
            pstmt.setString(4, bean.getRecord());
            pstmt.setString(5, bean.getImage());
            pstmt.setString(6, bean.getNationality());
            pstmt.setString(7, bean.getStyle());
            pstmt.setInt(8, bean.getAge());
            pstmt.setString(9, bean.getDebut());

            int cnt = pstmt.executeUpdate();
            conn.commit();
            return cnt;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try (Connection conn = super.getConnection()) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return -1;
        }
    }

    private Player makeBean(ResultSet rs) {
        Player bean = new Player();
        try {
            bean.setPnum(rs.getInt("pnum"));
            bean.setName(rs.getString("name"));
            bean.setWeight(rs.getString("weight"));
            bean.setRank(rs.getString("rank"));
            bean.setRecord(rs.getString("record"));
            bean.setImage(rs.getString("image"));
            bean.setNationality(rs.getString("nationality"));
            bean.setStyle(rs.getString("style"));
            bean.setAge(rs.getInt("age"));
            bean.setDebut(rs.getString("debut"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return bean;
    }

    public int getTotalCount(String weight) {
        String sql = "SELECT COUNT(*) AS mycnt FROM players";
        boolean isAll = weight == null || weight.equals("all");

        if (!isAll) {
            sql += " WHERE weight = ?";
        }

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!isAll) {
                pstmt.setString(1, weight);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("mycnt");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
