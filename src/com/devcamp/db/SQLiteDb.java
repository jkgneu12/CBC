package com.devcamp.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.devcamp.logging.Logger;

/**
 * SQLiteDb is a SQLite database wrapper class 
 * that can be used to perform all necessary SQLite operations.
 * @author ntikku
 *
 */
public abstract class SQLiteDb {

	static final Logger L = Logger.getLogger(SQLiteDb.class);
	
	private static String DB_PATH = "/data/data/%s/databases/";
	private static Map<String, SQLiteDatabase> mConnections = new HashMap<String, SQLiteDatabase>();
	
	// this is the actual database instance
	private SQLiteDatabase database = null; 
	private String location = null;
	private Context ctx = null;
	
	/**
	 * SQLiteDb requires a path, and the particular access
	 * that is desired, either read or read/write
	 * @param dbLocation
	 * @param readOnly
	 */
	public SQLiteDb(Context ctx, int version, boolean readOnly) 
				throws Exception{

		this.ctx = ctx;
		String dbName = getDBName();
		this.location = String.format(DB_PATH, ctx.getPackageName());

		int flag = readOnly ? SQLiteDatabase.OPEN_READONLY 
							: SQLiteDatabase.OPEN_READWRITE;
		
		File f = new File(this.location + dbName); // integrity check
		
		if(!f.exists()){
			try{
				File dir = new File(this.location);
				if(!dir.exists()){
					dir.mkdir();
				}
				f.createNewFile();
			}catch(Exception e){
				throw new Exception("failed to create database file");
			}
		} else { 
			//inited = true;
			// check version
		}
		
		this.location = this.location + dbName;
		
		//
		// Copy over the database if it hasn't been init'd already.
		//
		if(!mConnections.containsKey(this.location)){
			L.warn("No SQLiteDb found. Initing @ " + this.location);
			init(dbName, version);
		}
		
		L.debug("Establishing SQLiteDb conn @ " + this.location);
		this.database = getDatabaseConnection(this.location, flag);
		
	}
	
	public synchronized SQLiteDatabase getDatabaseConnection(String location, int flag){
		SQLiteDatabase db = mConnections.get(location);
		if( db == null ) { 
			db = SQLiteDatabase.openDatabase(location, null, flag);
			mConnections.put(location, db);
		}
		return db;
	}
	
	/**
	 * The name of the database you're creating
	 * @return
	 */
	protected abstract String getDBName();
	
	/**
	 * Assumes you only want a read-only connection.
	 * Use {@link #isUsable(boolean)} if you want to write as well.
	 * @return
	 */
	public static boolean isUsable(){
		return isUsable(true);
	}
	
	/**
	 * A method to see if SQLite is a usable storage option
	 * @return
	 */
	public static boolean isUsable(boolean readOnly){
		
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		if(readOnly) return mExternalStorageAvailable;
		return mExternalStorageAvailable && mExternalStorageWriteable;
		
	}
	
	public void beginTransaction(){
		if(this.database != null) { 
			if(!this.database.inTransaction())
				this.database.beginTransaction();
		}
	}
	
	public void endTransaction(){
		if(this.database != null) { 
			this.database.endTransaction();
		}
	}
	
	public void close(){
		if(this.database != null) { 
			mConnections.remove(this.database.getPath());
			this.database.close();
		}
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		if(this.database != null){
			return this.database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		}
		return null;
	}
	
	public Cursor query(String raw, String[] params){
		if(this.database != null){
			return this.database.rawQuery(raw, params);
		}
		return null;
	}
	
	/**
	 * Initialize the actual database - this will find the database in assets
	 * with dbName, and then utilize the context's package name, and the version
	 * @param dbName
	 * @param ctx
	 * @param version
	 */
	private void init(String dbName, int version){
		OpenHelper helper = new OpenHelper(dbName, this.location, this.ctx, version);
		try {
			helper.getReadableDatabase(); // force init file
			helper.copyDataBase();
		} catch (IOException e) {
			L.error("Failed to initialize SQLite database");
		} finally { 
			helper.close();
		}
	}
	
	
	/**
	 * Copies over the database and initializes it as necessary
	 * @author ntikku
	 *
	 */
	private static class OpenHelper extends SQLiteOpenHelper {
		
		Context ctx;
		private String dbName = null;
		private String dbPath = null;
		
		public OpenHelper(String dbName, String dbLocation, Context ctx, int version) {
			super(ctx, dbLocation, null, version);
			this.ctx = ctx;
			this.dbName = dbName;
			this.dbPath = dbLocation;
		}
		
		/**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     **/
	    private void copyDataBase() throws IOException {
	    	
	    	File f = new File(dbPath);
	    	if( f.exists() ) {
	    		
	    		L.info("The database has already been copied");
	    		// check versions here and pick a different db
	    		
	    	}
	 
	    	//Open your local db as the input stream
	    	InputStream myInput = ctx.getAssets().open("sqlite/"+dbName);
	    	
	    	L.info(ctx.getAssets().toString());
	    	
	    	if(myInput == null || myInput.available()==0){
	    		L.error("No database found in assets with name: "+dbName);
	    	}
	 
	    	// Path to the just created empty db
	    	String outFileName = dbPath;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	    	
	    	L.info("Done copying database to -> " + outFileName);
	 
	    }
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			L.info("creating!");
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
		@Override
		public synchronized SQLiteDatabase getReadableDatabase() {
			return super.getReadableDatabase();
		}
		
		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			return super.getWritableDatabase();
		}
		
		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			L.info("opening!");
		}
		
	}
	
}