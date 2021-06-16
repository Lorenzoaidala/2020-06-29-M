package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {

	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));

				result.add(actor);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));

				result.add(movie);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public void listAllDirectors(Map<Integer,Director> idMap){
		String sql = "SELECT * FROM directors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));

					idMap.put(res.getInt("id"), director);
				}
			}
			conn.close();


		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	/*public void setAttoriPerRegista(Map<Integer,Director> idMap,Integer anno){

		String sql="SELECT director_id,actor_id,a.first_name,a.last_name,a.gender "
				+ "FROM movies_directors md, movies m, roles r, actors a "
				+ "WHERE md.movie_id=m.id AND r.movie_id = m.id AND a.id=r.actor_id "
				+ "AND m.year=?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("director_id"))) {
					Director d = idMap.get(res.getInt("director_id"));
					Actor a = new Actor(res.getInt("actor_id"), res.getString("first_name"), res.getString("last_name"),res.getString("gender"));
					if(!d.getAttoriPartner().contains(a)) {
						d.getAttoriPartner().add(a);
					}
				}

			}
			conn.close();


		} catch (SQLException e) {
			e.printStackTrace();

		}
	}*/


	public List<Director> getVertici(Map<Integer,Director> idMap,Integer anno){
		String sql="SELECT distinct director_id "
				+ "FROM movies m, movies_directors md "
				+ "WHERE m.id=md.movie_id "
				+ "AND m.year=?";
		List<Director> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while(res.next()) {
				if(idMap.containsKey(res.getInt("director_id"))){
					result.add(idMap.get(res.getInt("director_id")));
				}
			}
			conn.close();
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;

		}
	}


	public List<Adiacenza> getArchi(Map<Integer,Director>idMap, Integer anno){
		String sql="SELECT md1.director_id AS d1, md2.director_id AS d2,COUNT(*) AS peso "
				+ "FROM movies_directors md1, movies m1, roles r1, "
				+ "movies_directors md2, movies m2, roles r2 "
/*condizioni*/	+ "WHERE md1.movie_id = m1.id AND r1.movie_id = md1.movie_id "
/*di*/			+ "AND md2.movie_id=m2.id AND r2.movie_id = md2.movie_id "
/*join*/		+ "AND m1.year = m2.year AND m2.year = ? "
	/**/		+ "AND r1.actor_id=r2.actor_id "
				+ "AND md1.director_id<md2.director_id "
				+ "GROUP BY d1,d2";
				

		List<Adiacenza> result = new ArrayList<Adiacenza>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while(res.next()) {
				if(idMap.containsKey(res.getInt("d1")) && idMap.containsKey(res.getInt("d2"))) {
					Adiacenza a = new Adiacenza(idMap.get(res.getInt("d1")), idMap.get(res.getInt("d2")), res.getDouble("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
		}catch(SQLException e) {
			throw new RuntimeException("Error Connection Database");
		}
	}




}
