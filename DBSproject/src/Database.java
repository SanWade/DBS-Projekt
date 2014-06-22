    import java.awt.EventQueue;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.ResultSetMetaData;
    import java.sql.SQLException;
    import java.sql.Statement;
     
     
     
    public class Database
    {
      String     driver     = "org.postgresql.Driver"; 
      String     host       = "localhost";
      String     port       = "5432";     
      String     database   = "postgres";              
      String     user       = "postgres";                  
      String     password   = "pit1234";    
      
      
      Connection connection = null;
     
      //* Constructor
      
      public Database ()
      {
        loadJdbcDriver ();
        openConnection ();
        showTore ();
        showGegenTore();
        closeConnection ();
      }
     
     
     
      // schließe Verbindung
      private void closeConnection ()
      {
        try
        {
          connection.close ();
        }
        catch (SQLException e)
        {
          e.printStackTrace ();
          System.exit (1);
        }
     
        System.out.println ("\nconnection closed");
      }
    
      /**
       * @return Url-string for postgreSQL-database connection
       */
      private String getUrl (){
    
       return ("jdbc:postgresql://localhost:5432/Bundesliga" );
      }
     
      //Treiber laden
      private void loadJdbcDriver () {
        try {
          Class.forName (driver);
        }
        catch (ClassNotFoundException e) {
          e.printStackTrace ();
          System.exit (1);
        }
        System.out.println ("driver loaded");
      }
     
      //Verbindung öffnen
      private void openConnection () {
        try {
          connection = DriverManager.getConnection (getUrl (),user,password);
        }
        catch (SQLException e){
          e.printStackTrace ();
          System.exit (1);
        }
        System.out.println ("connection opened");
      }
     
     
      //Feature1: 'Tore der letzten 3 Spiele' 
      private void showTore() {
        try {
          Statement statement = connection.createStatement ();
     
          ResultSet resultSet = statement.executeQuery (		  
        		  "SELECT V.Name, sum(Tore_Heim) as Tore_Heim,sum(Tore_Gast) as Tore_Gast FROM Verein V,Spiel S WHERE (V.V_ID = S.Heim  OR V.V_ID = S.Gast) AND S.Datum IN (Select distinct Datum FROM Spiel Order by Datum desc FETCH FIRST 3 ROWS ONLY) GROUP BY V.Name"
        		  );
          ResultSetMetaData resultSetMetaData = resultSet.getMetaData ();
     
          System.out.println ("\n Feature 1: Tore der letzten 3 Spiele\n");
     
          String format = "%35s   %-6s   %6s \n";
     
          System.out.printf (format + "\n",
                             resultSetMetaData.getColumnLabel (1),
                             resultSetMetaData.getColumnLabel (2),
                             resultSetMetaData.getColumnLabel (3)
                             );

          while (resultSet.next ())
          {
            System.out.printf (format,
                               resultSet.getString (1),
                               resultSet.getString (2),
                               resultSet.getString (3)
                               );
          }
     
          resultSet.close ();
          statement.close ();
        }
        catch (SQLException e){
          e.printStackTrace ();
          System.exit (1);
        }
      }
     
      //Feature2: 'Gegentore der letzten 3 Spiele' 
      private void showGegenTore() {
        try {
          Statement statement = connection.createStatement ();
     
          ResultSet resultSet = statement.executeQuery (		  
        		  "SELECT V.Name, sum(Tore_Heim) as Tore_Heim,sum(Tore_Gast) as Tore_Gast FROM Verein V,Spiel S WHERE (V.V_ID = S.Heim  OR V.V_ID = S.Gast) AND S.Datum IN (Select distinct Datum FROM Spiel Order by Datum desc FETCH FIRST 3 ROWS ONLY) GROUP BY V.Name"
        		  );
          ResultSetMetaData resultSetMetaData = resultSet.getMetaData ();
     
          System.out.println ("\n Feature 2: Gegentore der letzten 3 Spiele\n");
     
          String format = "%35s   %-6s   %6s \n";
     
          System.out.printf (format + "\n",
                             resultSetMetaData.getColumnLabel (1),
                             resultSetMetaData.getColumnLabel (2),
                             resultSetMetaData.getColumnLabel (3)
                             );

          while (resultSet.next ())
          {
            System.out.printf (format,
                               resultSet.getString (1),
                               resultSet.getString (2),
                               resultSet.getString (3)
                               );
          }
     
          resultSet.close ();
          statement.close ();
        }
        catch (SQLException e){
          e.printStackTrace ();
          System.exit (1);
        }
      }
      
      public static void main (String[] args)
      {
        EventQueue.invokeLater (new Runnable ()
        {
          public void run ()
          {
            Database database = new Database ();
     
            System.exit (0);
          }
        });
      }
    }
