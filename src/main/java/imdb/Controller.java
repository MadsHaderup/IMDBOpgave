package imdb;

import imdb.model.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class Controller {
    Connection connection = DBManager.getConnection();
    @RequestMapping(value="/")
    public static String Welcome() {
        return "Welcome, to my IMDB";
    }

    @RequestMapping(value="/get-first")
    public static String GetFirst() {
        String str = "SELECT * FROM `imdb-data` LIMIT 0, 1";
        PreparedStatement preparedStatement;
        Connection connection = DBManager.getConnection();


        try {
            preparedStatement = connection.prepareStatement(str);

            ResultSet resset = preparedStatement.executeQuery();
            resset.next();
            String str2 = resset.getString(3);
            return str2;
        } catch (SQLException err) {
            System.out.println("Fejl i count err=" + err.getMessage());
        }
        return "ressetFejl";
    }
    @RequestMapping(value="/get-random")
    public static String GetRandom() {
        String str = "SELECT * FROM `imdb-data` ORDER BY RAND() LIMIT 0, 1";
        PreparedStatement preparedStatement;
        Connection connection = DBManager.getConnection();


        try {
            preparedStatement = connection.prepareStatement(str);

            ResultSet resset = preparedStatement.executeQuery();
            resset.next();
            String str2 = resset.getString(3);
            return str2;
        } catch (SQLException err) {
            System.out.println("Fejl i count err=" + err.getMessage());
        }
        return "ressetFejl";
    }
    @GetMapping("/getTenSortByPopularity")
    public ArrayList<Movie> getTenSortByPopularity(){
        ArrayList<Movie> result = new ArrayList<>();
        String sqlStr = "SELECT * FROM (SELECT * FROM `imdb-data` ORDER BY RAND() LIMIT 10) as random ORDER BY popularity ASC;";
        PreparedStatement preparedStatement;

        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(sqlStr);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Movie movie = new Movie();

                movie.setYear(resultSet.getInt(1));
                movie.setLength(resultSet.getInt(2));
                movie.setTitle(resultSet.getString(3));
                movie.setSubject(resultSet.getString(4));
                movie.setPopularity(resultSet.getInt(5));
                movie.setAwards(resultSet.getString(6));

                result.add(movie);
            }
            return result;
        } catch (SQLException err) {
            System.out.println(err);
        }
        return result;
    }
    @GetMapping("/howManyWonAnAward")
    public int howManyWonAnAward(){
        int result = 0;
        String sqlStr = "SELECT * FROM `imdb-data`";
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(sqlStr);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString(6).equals("Yes")){
                    result = result + 1;
                }
            }
            return result;
            } catch (SQLException err) {
                System.out.println(err);
            }
            return result;
    }
}
