package com.simplicite.extobjects.DemoAPIs;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.ObjectDB;
import com.simplicite.util.exceptions.HTTPException;
import com.simplicite.util.exceptions.SearchException;
import com.simplicite.util.tools.Parameters;

public class DemoAPI2 extends com.simplicite.webapp.services.RESTServiceExternalObject {
	private static final long serialVersionUID = 1L;

	@Override
	public Object get(Parameters params) throws HTTPException
	{
		ObjectDB obj = borrowAPIObject("DemoProduct"); // borrow an API object instance from the pool
		try {
			List<String> parts = params.getURIParts(getName());

			if (parts.size() == 0) { // search
				List<String[]> rows = obj.getTool().search(params.getParameters()); // filtered search

				// Remove row IDs
				JSONArray list = new JSONArray(obj.toJSON(rows, null, false, true, true));
				for (int i = 0; i < list.length(); i++)
					list.getJSONObject(i).remove("row_id");

				return list;
			} else { // get (/<reference>)
				String reference = parts.get(0);
				List<String[]> rows = obj.getTool().search(new JSONObject().put("demoPrdReference", reference)); // filtered search using reference only

				// No unique record matching reference => not found
				if (rows.size() != 1)
					return notFound("No prodduct for reference " + reference);

				return new JSONObject(obj.toJSON(rows.get(0), null, false, true)).remove("row_id");
			}
		} catch (SearchException e) {
			return error(e);
		} finally {
			returnAPIObject(obj); // return the API object intance to the pool
		}
	}
}
