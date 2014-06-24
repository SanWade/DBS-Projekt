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
      String     password   = "PiT1234";    
      
      
      Connection connection = null;
     
      //* Constructor, lädt DriverClass, öffnet Verbindung zur DB, ruft Features auf, schließt Verbindung
      
      public Database ()
      {
        loadJdbcDriver ();
        openConnection ();
        showTore ();
        showGegenTore ();
        showNiederlagen ();
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
    
       return ("jdbc:postgresql://localhost:5432/bundesliga" );
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
     
      //Verbindung öffnen mittels URL,user,password
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
     
          String query1 = "Select neu.Name, sum(neu.Tore)"+
        		  		  " From ("+
        				  " SELECT V.Name, sum(S.Tore_Heim) as Tore"+
        				  " FROM Verein V,Spiel S"+
        				  " WHERE (V.V_ID = S.Heim ) AND S.Spieltag IN ("+
        				  " Select distinct Spieltag FROM Spiel"+
        				  " Order by Spieltag desc "+
        				  " FETCH FIRST 3 ROWS ONLY)"+
        				  " Group By V.Name"+
        				  " Union all"+
        				  " SELECT V.Name, sum(S.Tore_Gast)"+ 
        				  " FROM Verein V,Spiel S"+
        				  " WHERE (V.V_ID = S.Gast ) AND S.Spieltag IN ("+
        				  " Select distinct Spieltag FROM Spiel"+
        				  " Order by Spieltag desc"+
        				  " FETCH FIRST 3 ROWS ONLY)"+
        				  " Group By V.Name"+
        				  " ) neu"+
        				  " Group by neu.Name";
          
          
          
          
          ResultSet resultSet = statement.executeQuery (	query1	  );
          ResultSetMetaData resultSetMetaData = resultSet.getMetaData ();
      
     
          System.out.println ("\n Feature 1: Tore der letzten 3 Spiele\n");
     
          String format = "%35s   %-6s   \n";
     
          System.out.printf (format + "\n",
                             resultSetMetaData.getColumnLabel (1),
                             resultSetMetaData.getColumnLabel (2)
                             );

          while (resultSet.next ())
          {
            System.out.printf (format,
                               resultSet.getString (1),
                               resultSet.getString (2)
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
     
          String query2 = "Select neu.Name, sum(neu.Tore)"+
        		  			" From ("+
        					" SELECT V.Name, sum(S.Tore_Gast) as Tore"+
        					" FROM Verein V,Spiel S"+
        					" WHERE (V.V_ID = S.Heim ) AND S.Spieltag IN ("+
        					" Select distinct Spieltag FROM Spiel"+
        					" Order by Spieltag desc"+
        					" FETCH FIRST 3 ROWS ONLY)"+
        					" Group By V.Name"+
        					" Union all"+
        					" SELECT V.Name, sum(S.Tore_Heim)"+ 
        					" FROM Verein V,Spiel S"+
        					" WHERE (V.V_ID = S.Gast ) AND S.Spieltag IN ("+
        					" Select distinct Spieltag FROM Spiel"+
        					" Order by Spieltag desc"+
        					" FETCH FIRST 3 ROWS ONLY)"+
        					" Group By V.Name"+
        					" ) neu"+
        					" Group by neu.Name";
          
          
          
          
          ResultSet resultSet = statement.executeQuery (	query2	  );
          ResultSetMetaData resultSetMetaData = resultSet.getMetaData ();
      
     
          System.out.println ("\n Feature 2: Gegentore der letzten 3 Spiele\n");
     
          String format = "%35s   %-6s   \n";
     
          System.out.printf (format + "\n",
                             resultSetMetaData.getColumnLabel (1),
                             resultSetMetaData.getColumnLabel (2)
                             );

          while (resultSet.next ())
          {
            System.out.printf (format,
                               resultSet.getString (1),
                               resultSet.getString (2)
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

    //Feature3: 'Anzahl der Niederlagen in den letzten 5 Spielen' 
      private void showNiederlagen() {
        try {
          Statement statement = connection.createStatement ();
     
          String query3 = "Select neu.Name, sum(neu.Niederlagen)"+
        		  			" From ("+
        					" SELECT V.Name, count(*) as Niederlagen"+
        					" FROM Verein V,Spiel S"+
        					" WHERE (V.V_ID = S.Heim )"+ 
        					" AND (S.Tore_Heim < S.Tore_Gast)"+  
        					" AND S.Spieltag IN ("+
        					" Select distinct Spieltag FROM Spiel"+
        					" Order by Spieltag desc"+
        					" FETCH FIRST 5 ROWS ONLY)"+
        					" Group By V.Name"+
        					" Union all"+
        					" SELECT V.Name, count(*)"+ 
        					" FROM Verein V,Spiel S"+
        					" WHERE (V.V_ID = S.Gast )"+ 
        					" AND (S.Tore_Heim > S.Tore_Gast)"+
        					" AND S.Spieltag IN ("+
        					" Select distinct Spieltag FROM Spiel"+
        					" Order by Spieltag desc"+
        					" FETCH FIRST 5 ROWS ONLY)"+
        					" Group By V.Name"+
        		  			" ) neu"+
        		  			" Group by neu.Name";
          
          
          
          
          ResultSet resultSet = statement.executeQuery (	query3	  );
          ResultSetMetaData resultSetMetaData = resultSet.getMetaData ();
      
     
          System.out.println ("\n Feature 3: Niederlagen der letzten 5 Spiele\n");
     
          String format = "%35s   %-6s   \n";
     
          System.out.printf (format + "\n",
                             resultSetMetaData.getColumnLabel (1),
                             resultSetMetaData.getColumnLabel (2)
                             );

          while (resultSet.next ())
          {
            System.out.printf (format,
                               resultSet.getString (1),
                               resultSet.getString (2)
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
