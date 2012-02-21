package com.devcamp.db;

import android.content.Context;

public class CBCdb extends SQLiteDb {

	public CBCdb(Context ctx, int version, boolean readOnly) throws Exception {
		super(ctx, version, readOnly);
	}

	@Override
	protected String getDBName() {
		return "cbc.sqlite";
	}

}
