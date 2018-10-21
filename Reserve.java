import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.text.*;

public class Reserve {
    private JFrame frame;
    private int userId = 0;
    private JTextField text;
    private JList<String> list1;
    private JList<String> list2;
    private DefaultListModel<String> clothList;
    private DefaultListModel<String> listModel;
    private JComboBox<String> gendercombo;
    private JComboBox<String> genrecombo;
    private Connection connection = null;
    private Statement statement = null;
    private int cond1 = 0;
    private int cond2 = 0;
    private String[][] ad = new String[][] { { "", " and genre = 1", " and genre = 2" },
            { " and gender = 1", " and gender = 1 and genre = 1", " and gender = 1 and genre = 2" },
            { " and gender = 2", " and gender = 2 and genre = 1", " and gender = 2 and genre = 2" } };

    Reserve(int id){	
        super();	
        userId = id;	
        frame = new JFrame("SEARCH PAGE");	
        Component contents = createComponents();	
        frame.getContentPane().add(contents, BorderLayout.CENTER);	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
        frame.pack();	
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    
    class SearchButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String str = text.getText();
            makelist1(str);
        }
    }

    class ReserveButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String str = text.getText();
            int num = list1.getSelectedIndex();
            int id = getid(str, num);
            if (num != -1 && id != 0) {
                if (update(id) == 1) {
                    JOptionPane.showMessageDialog(list1, "Reserve Completed", "Info", JOptionPane.INFORMATION_MESSAGE);
                    makelist1("");
                    text.setText("");
                } else if (update(id) == 0) {
                    JOptionPane.showMessageDialog(list1, "You already reserved!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(list1, "Unexpected error ocurred", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(list1, "None selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class DetailButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String str = text.getText();
            int num = list1.getSelectedIndex();
            int id = getid(str, num);
            if (num != -1 && id != 0) {
                makelist2(id);
            } else {
                JOptionPane.showMessageDialog(list1, "None selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class GenderCombo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (gendercombo.getSelectedIndex() == 1) {
                cond1 = 1;
            } else if (gendercombo.getSelectedIndex() == 2) {
                cond1 = 2;
            } else {
                cond1 = 0;
            }
        }
    }

    class GenreCombo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (genrecombo.getSelectedIndex() == 1) {
                cond2 = 1;
            } else if (genrecombo.getSelectedIndex() == 2) {
                cond2 = 2;
            } else {
                cond2 = 0;
            }
        }
    }

    class BackButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            MyReservedPage reserved = new MyReservedPage(userId);
            reserved.setVisible(true);
            frame.setVisible(false);
        }
    }

    public void makelist0(String sql) {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:system.sqlite");
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            clothList.clear();
            while (rs.next()) {
                clothList.addElement(rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void makelist1(String str) {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:system.sqlite");
            statement = connection.createStatement();
            String sq = "select name from clothes where rental_user_id != 0 and rental_user_id != " + userId
                    + " and name like '%" + str + "%'";
            StringBuilder bui = new StringBuilder();
            bui.append(sq);
            bui.append(ad[cond1][cond2]);
            String sql = bui.toString();
            ResultSet rs = statement.executeQuery(sql);
            clothList.clear();
            while (rs.next()) {
                clothList.addElement(rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void makelist2(int id) {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:system.sqlite");
            statement = connection.createStatement();
            String sql = "select * from clothes where rental_user_id != " + userId + " and id = " + id;
            ResultSet rs = statement.executeQuery(sql);
            listModel.clear();
            listModel.addElement("ID: " + rs.getInt("id"));
            listModel.addElement("Name: " + rs.getString("name"));
            if(rs.getInt("gender") == 1){
                listModel.addElement("Gender: Man");
            }
            else{
                listModel.addElement("Gender: Woman");
            }
            if(rs.getInt("genre") == 1){
                listModel.addElement("Genre: Japanese");
            }
            else{
                listModel.addElement("Genre: Western");
            }
            listModel.addElement("Height: " + rs.getInt("size"));
            listModel.addElement("Color: " + rs.getString("color"));
            listModel.addElement("Price: " + rs.getInt("price"));
            listModel.addElement("Period: " + rs.getInt("period"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getid(String str, int num) {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:system.sqlite");
            statement = connection.createStatement();
            String sq = "select id from clothes where rental_user_id != 0 and rental_user_id != " + userId
                    + " and name like '%" + str + "%'";
            StringBuilder bui = new StringBuilder();
            bui.append(sq);
            bui.append(ad[cond1][cond2]);
            String sql = bui.toString();
            ResultSet rs = statement.executeQuery(sql);
            for (int i = 0; i < num + 1; i++) {
                rs.next();
            }
            int id = Integer.parseInt(rs.getString(1));
            return id;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int update(int id) {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:system.sqlite");
            statement = connection.createStatement();
            String sql = "select * from reservations";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt(1) == userId && rs.getInt(2) == id) {
                    return 0;
                }
            }
            sql = "insert into reservations values(" + userId + "," + id + ")";
            statement.executeUpdate(sql);
            return 1;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Component createComponents() {
        String[] gender = { "", "Mens", "Womens" };
        String[] genre = { "", "Japanese", "Western" };
        JLabel genderLabel = new JLabel("Gender");
        JLabel genreLabel = new JLabel("Genre");
        gendercombo = new JComboBox<String>(gender);
        genrecombo = new JComboBox<String>(genre);
        JButton searchbutton = new JButton("Search");
        JButton detailbutton = new JButton("Detail");
        JButton reservebutton = new JButton("Reserve");
        JButton backbutton = new JButton("Back");
        text = new JTextField("", 20);
        clothList = new DefaultListModel<String>();
        listModel = new DefaultListModel<String>();

        makelist0("select name from clothes where rental_user_id != 0 and rental_user_id != " + userId);

        list1 = new JList<String>(clothList);
        list2 = new JList<String>(listModel);
        list1.setVisibleRowCount(10);
        list2.setVisibleRowCount(10);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane1 = new JScrollPane(list1);
        JScrollPane scrollPane2 = new JScrollPane(list2);
        scrollPane1.createVerticalScrollBar();
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.createVerticalScrollBar();
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        SearchButton searchbuttonListener = new SearchButton();
        searchbutton.addActionListener(searchbuttonListener);

        DetailButton detailbuttonListener = new DetailButton();
        detailbutton.addActionListener(detailbuttonListener);

        ReserveButton reservebuttonListener = new ReserveButton();
        reservebutton.addActionListener(reservebuttonListener);

        GenderCombo gendercomboListener = new GenderCombo();
        gendercombo.addActionListener(gendercomboListener);

        GenreCombo genrecomboListener = new GenreCombo();
        genrecombo.addActionListener(genrecomboListener);

        BackButton backbuttonListener = new BackButton();
        backbutton.addActionListener(backbuttonListener);

        JPanel subPane1 = new JPanel();
        subPane1.setLayout(new BoxLayout(subPane1, BoxLayout.X_AXIS));
        subPane1.add(reservebutton);
        subPane1.add(Box.createRigidArea(new Dimension(60, 30)));
        subPane1.add(detailbutton);
        subPane1.add(Box.createRigidArea(new Dimension(60, 30)));
        subPane1.add(backbutton);

        JPanel subPane2 = new JPanel();
        subPane2.setLayout(new BoxLayout(subPane2, BoxLayout.X_AXIS));
        subPane2.add(genderLabel);
        subPane2.add(Box.createRigidArea(new Dimension(10, 30)));
        subPane2.add(gendercombo);
        subPane2.add(Box.createRigidArea(new Dimension(30, 30)));
        subPane2.add(genreLabel);
        subPane2.add(Box.createRigidArea(new Dimension(10, 30)));
        subPane2.add(genrecombo);

        JPanel subPane3 = new JPanel();
        subPane3.setLayout(new BoxLayout(subPane3, BoxLayout.X_AXIS));
        subPane3.add(text);
        subPane3.add(Box.createRigidArea(new Dimension(10, 30)));
        subPane3.add(searchbutton);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(subPane2);
        pane.add(Box.createRigidArea(new Dimension(10, 30)));
        pane.add(subPane3);
        pane.add(Box.createRigidArea(new Dimension(10, 30)));
        pane.add(scrollPane1);
        pane.add(Box.createRigidArea(new Dimension(10, 10)));
        pane.add(scrollPane2);
        pane.add(Box.createRigidArea(new Dimension(10, 20)));
        pane.add(subPane1);

        return pane;
    }
    /*
     * Reserve(int id) { userId = id; JFrame frame = new JFrame("SEARCH PAGE");
     * Component contents = createComponents(); frame.getContentPane().add(contents,
     * BorderLayout.CENTER);
     * 
     * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.pack();
     * frame.setVisible(true); }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SEARCH PAGE");
        Reserve reserve = new Reserve();
        Component contents = reserve.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    */
}