import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
 
 
// susi mag kuchen sehr gerne
public class Database
{
  String     driver     = "org.postgresql.Driver";
 
  // --------------------------------------------------------------------------
 
  String     host       = "localhost";            // !!! anpassen !!!
 
  String     port       = "5432";                 // !!! anpassen !!!
 
  String     database   = "postgres";             // !!! anpassen !!!
 
  String     user       = "postgres";                  // !!! anpassen !!!
 
  String     password   = "PiT1234";                  // !!! anpassen !!!
 
  // --------------------------------------------------------------------------
 
  Connection connection = null;
 
 
 
 
 
  /**
   * Constructor
   */
  public Database ()
  {
    loadJdbcDriver ();
    openConnection ();
    showTimezones ();
    closeConnection ();
  }
 
 
 
 
 
  /**
   * close the connection
   */
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
  private String getUrl ()
  {
    // PostgreSQL takes one of the following url-forms:
    // ================================================
    // jdbc :p ostgresql:database
    // jdbc :p ostgresql://host/database
    // jdbc :p ostgresql://host :p ort/database
 
    //return ("jdbc :p ostgresql:" + (host != null ? ("//" + host) + (port != null ? ":" + port : "") + "/" : "") + database);
	  return ("jdbc:postgresql://localhost:5432/bundesliga");
  }
 
 
 
 
 
  /**
   * loading the JDBC driver
   */
  private void loadJdbcDriver ()
  {
    try
    {
      Class.forName (driver);
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace ();
      System.exit (1);
    }
 
    System.out.println ("driver loaded");
  }
 
 
 
 
 
  /**
   * opening the connection
   */
  private void openConnection ()
  {
    try
    {
      connection = DriverManager.getConnection (getUrl(),
                                                user,
                                                password);
    }
    catch (SQLException e)
    {
      e.printStackTrace ();
      System.exit (1);
    }
 
    System.out.println ("connection opened");
  }
 
 
 
 
 
  /**
   * show the timezones used in the database
   */
  private void showTimezones ()
  {
    try
    {
      Statement statement = connection.createStatement ();
 
      ResultSet resultSet = statement.executeQuery ("select * from pg_catalog.pg_timezone_names");
 
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData ();
 
      System.out.println ("\n\nshow data from table: 'pg_catalog.pg_timezone_names'\n");
 
      String format = "%35s   %-6s   %10s   %s\n";
 
      System.out.printf (format + "\n",
                         resultSetMetaData.getColumnLabel (1),
                         resultSetMetaData.getColumnLabel (2),
                         resultSetMetaData.getColumnLabel (3),
                         resultSetMetaData.getColumnLabel (4));
 
      while (resultSet.next ())
      {
        System.out.printf (format,
                           resultSet.getString (1),
                           resultSet.getString (2),
                           resultSet.getString (3),
                           resultSet.getString (4));
      }
 
      resultSet.close ();
      statement.close ();
    }
    catch (SQLException e)
    {
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
