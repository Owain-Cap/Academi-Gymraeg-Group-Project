package uk.ac.bangor.cs.group2.academicymraeg;

import java.sql.ResultSet;
import java.util.Collection;

/**
 * Interface to define transformations between JDBC ResultSets/SQL and DTOs.
 *
 * @param <T> The DTO Type.
 */
public interface SQLObject<T> {
	/**
	 * Create DTO instances for all results in the supplied ResultSet.
	 *
	 * If there are no (compatible) results an empty collection should be returned.
	 *
	 * @param rs The ResultSet to retrieve data from. It is assumed the Iterator is
	 *           prior to the first result.
	 * @return A collection of all valid results.
	 */
	Collection<T> fromResultSet(ResultSet rs);

	/**
	 * Produce an DELETE SQL statement based on the data in this instance.
	 *
	 * @return the SQL statement.
	 */
	String toDeleteSQL();

	/**
	 * Produce an INSERT SQL statement based on the data in this instance.
	 *
	 * @return the SQL statement.
	 */
	String toInsertSQL();

	/**
	 * Produce an UPDATE SQL statement based on the data in this instance.
	 *
	 * @return the SQL statement.
	 */
	String toUpdateSQL();
}
