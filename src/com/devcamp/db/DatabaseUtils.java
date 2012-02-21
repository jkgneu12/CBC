package com.devcamp.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;

public class DatabaseUtils {

	/**
	 * Get the values from a cursor in an array list
	 * of hash tables where the key is the name 
	 * @param results
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getResultsTable(Cursor results){
		
		ArrayList<HashMap<String, Object>> values = new ArrayList<HashMap<String, Object>>();
		
		String[] columnNames = null;
		
		if( results.moveToFirst() ){
			
			do{ 
				
				HashMap<String, Object> ret = new HashMap<String, Object>();
				
				if( columnNames == null ) { 
					columnNames = results.getColumnNames();
				}
				
				for( int i=0; i<columnNames.length; i++ ) { 
					ret.put(columnNames[i], results.getString(i));
				}
				
				values.add(ret);
				
			} while ( results.moveToNext() );
			
		}
		
		return values;
		
	}
	
	/**
	 * Assumes that the cursor being passed in was
	 * generated with a select count() query with a unique
	 * int result being returned
	 * @param result
	 * @return
	 */
	public static Integer getCount(Cursor result){
		if( result.moveToFirst() ) { 
			return result.getInt(0);
		}
		return null;
	}
	
}
